package org.qnot.pwrecta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

public class Main {
    private static Log logger = LogFactory.getLog(Main.class);
    private static Options options;

    public static void main(String[] args) throws Exception {
        Main.buildOptions();

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            Main.printHelpAndExit(options, e.getMessage());
        }

        if (cmd.hasOption("h")) {
            Main.printHelpAndExit(options);
        }

        if (cmd.hasOption("p")) {
            Main.print(cmd);
        } else if (cmd.hasOption("g")) {
            Main.fetchPassword(cmd);
        } else {
            Main.generate(cmd);
        }

        System.exit(0);
    }
    
    public static void print(CommandLine cmd) throws IOException {
        TabulaRecta tabulaRecta = Main.getDatabase(cmd);
        Main.getOutputFormat(cmd).output(Main.getOutputStream(cmd), tabulaRecta);
    }
    
    public static void fetchPassword(CommandLine cmd) throws IOException {
        String coords = cmd.getOptionValue("g");
        if (coords == null || coords.length() == 0) {
            Main.printHelpAndExit(options,
                    "Please provide a row:column (ex. C:F)");
        }

        String[] parts = coords.split(":");
        if (parts == null || parts.length != 2) {
            Main.printHelpAndExit(options,
                    "Invalid value. Please provide a row:column (ex. C:F)");
        }

        TabulaRecta tabulaRecta = Main.getDatabase(cmd);
        
        int row = tabulaRecta.getHeader().getIndex(parts[0]);
        int col = tabulaRecta.getHeader().getIndex(parts[1]);
        if (row == -1 || col == -1) {
            Main.printHelpAndExit(options,
                            "Symbol not found. Please provide a valid row:column (ex. C:F)");
        }

        System.out.println(tabulaRecta.get(row, col));     
    }
    
    public static void generate(CommandLine cmd) throws IOException {
        TabulaRecta tabulaRecta = new TabulaRecta(Alphabet.ALPHA_UPPER_NUM,
                Alphabet.ALPHA_NUM_SYMBOL);
        tabulaRecta.generate();

        if (cmd.hasOption("s")) {
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
            Main.getOutputFormat(cmd).output(Main.getOutputStream(cmd), tabulaRecta);
        }
    }
    
    private static OutputFormat getOutputFormat(CommandLine cmd) {
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
    
    private static TabulaRecta getDatabase(CommandLine cmd) throws IOException {
        File jsonFile = null;

        String inFile = cmd.getOptionValue("i");
        if (inFile != null && inFile.length() > 0) {
            jsonFile = new File(inFile);
        } else {
            jsonFile = new File(System.getProperty("user.home"), ".pwrecta");
        }
        
        if(!jsonFile.exists() || !jsonFile.canRead()) {
            Main.printHelpAndExit(options, "Failed to read db file: "+jsonFile.getAbsolutePath());
        }

        String json = FileUtils.readFileToString(jsonFile);
        Gson gson = new Gson();
        TabulaRecta tabulaRecta = gson.fromJson(json, TabulaRecta.class);
        
        return tabulaRecta;
    }
    
    private static OutputStream getOutputStream(CommandLine cmd) throws IOException {
        String outFile = cmd.getOptionValue("o");
        OutputStream outputStream = System.out;

        if (outFile != null && outFile.length() > 0) {
            outputStream = new FileOutputStream(outFile);
        }
        
        return outputStream;
    }
  
    @SuppressWarnings("static-access")
    public static void buildOptions() {
        Main.options = new Options();
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
            OptionBuilder.withLongOpt("store")
                         .withDescription("store pwrecta to JSON and write out PDF file")
                         .create("s")
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
    }

    public static void printHelpAndExit(Options options, String message) {
        if (message != null)
            logger.fatal("Usage error: " + message);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pwrecta", options);
        if (message != null) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    public static void printHelpAndExit(Options options) {
        printHelpAndExit(options, null);
    }

}
