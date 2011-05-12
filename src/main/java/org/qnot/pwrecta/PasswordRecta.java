package org.qnot.pwrecta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

public class PasswordRecta {
    private static Log logger = LogFactory.getLog(PasswordRecta.class);
    private Options options;
    protected PropertiesConfiguration properties;
    
    public PasswordRecta() {
        properties = new PropertiesConfiguration();
        try {
            properties.load(".pwrecta_rc");
        } catch(ConfigurationException ignored) {
        }
    }
    
    public static void main(String[] args) {
        PasswordRecta pwrecta = new PasswordRecta();
        
        try {
            pwrecta.run(args);
        } catch(Exception e) {
            logger.fatal("Something really bad happened: "+e.getMessage());
            e.printStackTrace();
        }   
    }

    public void run(String[] args) throws IOException {
        buildOptions();

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            printHelpAndExit(options, e.getMessage());
        }

        if (cmd.hasOption("h")) {
            printHelpAndExit(options);
        }

        if (cmd.hasOption("p")) {
            print(cmd);
        } else if (cmd.hasOption("g") || cmd.hasOption("t")) {
            fetchPassword(cmd);
        } else {
            generate(cmd);
        }

        System.exit(0);
    }
    
    public void print(CommandLine cmd) throws IOException {
        TabulaRecta tabulaRecta = getDatabase(cmd);
        getOutputFormat(cmd).output(getOutputStream(cmd), tabulaRecta);
    }
    
    @SuppressWarnings("unchecked")
    public void fetchPassword(CommandLine cmd) throws IOException {
        String coords = null;
        
        if(cmd.hasOption("t")) {
            String tag = cmd.getOptionValue("t");
            if(tag == null || tag.length() == 0) {
                printHelpAndExit(options, "Please provide a non empty tag");
            }
            
            Iterator<String> kit = properties.getKeys("tag");
            if(kit.hasNext()) {
                while(kit.hasNext()) {
                    String key = kit.next();
                    if(key.contains(tag)) {
                        coords = properties.getString(key);
                        break;
                    }
                }
            }
            
            if(coords == null) {
                printHelpAndExit(options, "Tag not found in configuration file: "+tag);
            }
        } else {
            coords = cmd.getOptionValue("g");
        }
        
        if (coords == null || coords.length() == 0) {
            printHelpAndExit(options,
                "Please provide a row:column (ex. C:F)");
        }

        String[] parts = coords.split(":");
        if (parts == null || parts.length != 2) {
            printHelpAndExit(options,
                "Invalid value. Please provide a row:column (ex. C:F)");
        }
        
        String row = parts[0];
        String col = parts[1];
        
        String seq = cmd.getOptionValue("s");
        if (seq == null || seq.length() == 0) {
            seq = properties.getString("sequence");
        }
        
        if(seq == null) {
            seq = Sequence.DEFAULT_SEQUENCE;
        }
        
        Sequence sequence = null;
        try {
            sequence = Sequence.fromString(seq);
        } catch(SequenceParseException e) {
            printHelpAndExit(options, "Invalid sequence string: "+e.getMessage());
        }
        
        Direction[] directionPriority = null;
        if(cmd.hasOption("c")) {
            String collision = cmd.getOptionValue("c");
            if("cc".equalsIgnoreCase(collision) || 
               "clockwise_compass".equalsIgnoreCase(collision)) {
                directionPriority = Direction.clockwiseCompass();
            } else if("ccc".equalsIgnoreCase(collision) || 
                      "counterclockwise_compass".equalsIgnoreCase(collision)) {
                directionPriority = Direction.counterclockwiseCompass();
            } else {
            String[] cparts = collision.split(",");
                List<Direction> list = new ArrayList<Direction>();
                for(String c : cparts) {
                    try {
                        list.add(Direction.fromString(c));
                    } catch(SequenceParseException e) {
                        printHelpAndExit(options, "Direction parse error: "+e.getMessage());
                    }
                }
                directionPriority = list.toArray(new Direction[list.size()]);
            }
        }

        TabulaRecta tabulaRecta = getDatabase(cmd);
        
        int rowIndex = tabulaRecta.getHeader().getIndex(row);
        int colIndex = tabulaRecta.getHeader().getIndex(col);
        if (rowIndex == -1 || colIndex == -1) {
            printHelpAndExit(options,
                            "Symbol not found. Please provide a valid row:column (ex. C:F)");
        }

        System.out.println(tabulaRecta.getPassword(rowIndex, colIndex, sequence, directionPriority));     
    }
    
    public void generate(CommandLine cmd) throws IOException {
        Alphabet headerAlphabet = Alphabet.ALPHA_UPPER_NUM;
        Alphabet dataAlphabet = Alphabet.ALPHA_NUM_SYMBOL;
        
        if(cmd.hasOption("a")) {
            try {
                dataAlphabet = Alphabet.fromString(cmd.getOptionValue("a"));
            } catch(AlphabetParseException e) {
                printHelpAndExit(options, "Alphabet parsing error: "+e.getMessage());
            }
        }
        
        if(cmd.hasOption("x")) {
            try {
                headerAlphabet = Alphabet.fromString(cmd.getOptionValue("x"));
                if(headerAlphabet.size() > 36) {
                    printHelpAndExit(options, "Header alphabets with more than 36 symbols are not supported yet :)");
                }
            } catch(AlphabetParseException e) {
                printHelpAndExit(options, "Alphabet parsing error: "+e.getMessage());
            }
        }
        
        TabulaRecta tabulaRecta = new TabulaRecta(headerAlphabet, dataAlphabet);
        
        logger.info("Generating a Password Recta...");
        tabulaRecta.generate();

        if (cmd.hasOption("d")) {
            OutputStream jsonOut = null;
            OutputStream pdfOut = null;

            String name = cmd.getOptionValue("n");
            if (name != null && name.length() > 0) {
                jsonOut = new FileOutputStream(name + ".json");
                pdfOut = new FileOutputStream(name + ".pdf");
            } else {
                jsonOut = new FileOutputStream(new File(System
                        .getProperty("user.home"), ".pwrecta"));
                pdfOut = new FileOutputStream("pwrecta.pdf");
            }

            OutputFormat jsonFormat = new JSONOutput();
            OutputFormat pdfFormat = new PDFOutput();
            jsonFormat.output(jsonOut, tabulaRecta);
            pdfFormat.output(pdfOut, tabulaRecta);
        } else {
            getOutputFormat(cmd).output(getOutputStream(cmd), tabulaRecta);
        }
    }
    
    private OutputFormat getOutputFormat(CommandLine cmd) {
        String format = cmd.getOptionValue("f");
        OutputFormat outputFormat = null;

        if ("json".equals(format)) {
            outputFormat = new JSONOutput();
        } else if ("pdf".equals(format)) {
            outputFormat = new PDFOutput();
        } else {
            outputFormat = new AsciiOutput();
        }
        
        return outputFormat;
    }
    
    private TabulaRecta getDatabase(CommandLine cmd) throws IOException {
        File jsonFile = null;

        String inFile = cmd.getOptionValue("i");
        String inFileProps = properties.getString("pwrecta.path");
        if (inFile != null && inFile.length() > 0) {
            jsonFile = new File(inFile);
        } else if(inFileProps != null && inFileProps.length() > 0){
            jsonFile = new File(inFileProps);
        } else {
            jsonFile = new File(System.getProperty("user.home"), ".pwrecta");
        }
        
        if(!jsonFile.exists() || !jsonFile.canRead()) {
            printHelpAndExit(options, "Failed to read db file: "+jsonFile.getAbsolutePath());
        }

        String json = FileUtils.readFileToString(jsonFile);
        Gson gson = new Gson();
        TabulaRecta tabulaRecta = gson.fromJson(json, TabulaRecta.class);
        
        // Check to make sure we loaded a valid JSON file
        if(tabulaRecta == null ||
           tabulaRecta.getHeader() == null || tabulaRecta.getHeader().size() == 0 ||
           tabulaRecta.getDataAlphabet() == null || tabulaRecta.getDataAlphabet().size() == 0 ||
           tabulaRecta.getRawData() == null || tabulaRecta.rows() == 0 || tabulaRecta.cols() == 0
           ) {
            printHelpAndExit(options, "Not a valid pwrecta JSON file: "+jsonFile.getAbsolutePath());
        }
        
        return tabulaRecta;
    }
    
    private OutputStream getOutputStream(CommandLine cmd) throws IOException {
        String outFile = cmd.getOptionValue("o");
        OutputStream outputStream = System.out;

        if (outFile != null && outFile.length() > 0) {
            outputStream = new FileOutputStream(outFile);
        }
        
        return outputStream;
    }
  
    @SuppressWarnings("static-access")
    public void buildOptions() {
        options = new Options();
        options.addOption(
            OptionBuilder.withLongOpt("output")
                         .withDescription("output file")
                         .hasArg()
                         .create("o")
        );
        options.addOption(
            OptionBuilder.withLongOpt("help")
                         .withDescription("print usage info")
                         .create("h")
        );
        options.addOption(
            OptionBuilder.withLongOpt("dbsave")
                         .withDescription("save pwrecta db to JSON and write out PDF file")
                         .create("d")
        );
        options.addOption(
            OptionBuilder.withLongOpt("name")
                         .withDescription("name of database, creates [name].pdf and [name].json files")
                         .hasArg()
                         .create("n")
        );
        options.addOption(
            OptionBuilder.withLongOpt("format")
                         .withDescription("Output format [pdf|json|ascii]")
                         .hasArg()
                         .create("f")
        );
        options.addOption(
            OptionBuilder.withLongOpt("getpass")
                         .withDescription("[row:column] - get the password at row:column (ex. B:W)")
                         .hasArg()
                         .create("g")
        );
        options.addOption(
                OptionBuilder.withLongOpt("print")
                             .withDescription("print existing pwrecta database")
                             .create("p")
        );
        options.addOption(
            OptionBuilder.withLongOpt("input")
                         .withDescription("input file for retrieving passwords")
                         .hasArg()
                         .create("i")
        );
        options.addOption(
                OptionBuilder.withLongOpt("sequence")
                             .withDescription("sequence for fetching password")
                             .hasArg()
                             .create("s")
            );
        options.addOption(
                OptionBuilder.withLongOpt("alphabet")
                             .withDescription("alphabet to use for Password Recta")
                             .hasArg()
                             .create("a")
            );
        options.addOption(
                OptionBuilder.withLongOpt("header")
                             .withDescription("header alphabet to use for row/column headings")
                             .hasArg()
                             .create("x")
            );
        options.addOption(
                OptionBuilder.withLongOpt("collision")
                             .withDescription("direction precedence should a collision occur")
                             .hasArg()
                             .create("c")
            );
        options.addOption(
                OptionBuilder.withLongOpt("tag")
                             .withDescription("get password for tag")
                             .hasArg()
                             .create("t")
            );
    }

    public void printHelpAndExit(Options options, String message) {
        if (message != null)
            logger.fatal("Usage error: " + message + "\n");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pwrecta", options);
        if (message != null) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    public void printHelpAndExit(Options options) {
        printHelpAndExit(options, null);
    }

}
