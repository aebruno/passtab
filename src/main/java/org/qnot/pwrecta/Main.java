package org.qnot.pwrecta;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Main {
    private static Log logger = LogFactory.getLog(Main.class);

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("output").withDescription(
                "output file").hasArg().create("o"));
        options.addOption(OptionBuilder.withLongOpt("help").withDescription(
                "print usage info").create("h"));
        options.addOption(OptionBuilder.withLongOpt("save").withDescription(
                "save pwrecta to JSON and write out PDF file").create("s"));
        options.addOption(OptionBuilder
                        .withLongOpt("format")
                        .withDescription(
                                "Output format [pdf|json|ascii]")
                        .hasArg().create("f"));
        options.addOption(OptionBuilder
                        .withLongOpt("password")
                        .withDescription(
                                "[row:column] - get the password at row:column (ex. B:W)")
                        .hasArg().create("p"));
        options.addOption(OptionBuilder.withLongOpt("input").withDescription(
                "input file for retrieving passwords").hasArg().create("i"));

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            Main.printHelpAndExit(options, e.getMessage());
        }

        if(cmd.hasOption("h")) {
            Main.printHelpAndExit(options);
        }

        String format = cmd.getOptionValue("f");
        String fileName = cmd.getOptionValue("o");

        File output = null;
        if(fileName != null && fileName.length() > 0) {
            output = new File(fileName);
        }

        if(output == null && "pdf".equals(format)) {
            Main.printHelpAndExit(options, "Missing filename for PDF");
        }

        String[] headerAlphabet = Alphabet.ALPHA_UPPER_NUM;
        String[] dataAlphabet = Alphabet.ALPHA_NUM_SYMBOL;


        if(cmd.hasOption("p")) {
            String coords = cmd.getOptionValue("p");
            if(coords == null || coords.length() == 0) {
                Main.printHelpAndExit(options, "Please provide a row:column (ex. C:F)");
            }

            String[] parts = coords.split(":");
            if(parts == null || parts.length != 2) {
                Main.printHelpAndExit(options, "Invalid value. Please provide a row:column (ex. C:F)");
            }

            int row = Alphabet.getIndex(parts[0], headerAlphabet);
            int col = Alphabet.getIndex(parts[1], headerAlphabet);
            if(row == -1 || col == -1) {
                Main.printHelpAndExit(options, "Symbol not found. Please provide a valid row:column (ex. C:F)");
            }
            row++;
            col++;

            String infile = cmd.getOptionValue("i");
            File input = null;
            if(infile != null && infile.length() > 0) {
                input = new File(infile);
            } else {
                input = new File(System.getProperty("user.home"), ".pwrecta");
            }

            String json = null;
            try {
                json = FileUtils.readFileToString(input);
            } catch(IOException e) {
                Main.printHelpAndExit(options, "Failed to read input file: "+e.getMessage());
            }

            Gson gson = new Gson();
            String[][] tabulaRecta = gson.fromJson(json, String[][].class);

            System.out.println(tabulaRecta[row][col]);
            System.exit(0);
        }


        String[][] tabulaRecta = TabulaRecta.generate(headerAlphabet, dataAlphabet);
        if(cmd.hasOption("s")) {
            if(output == null) {
                output = new File(System.getProperty("user.home"), ".pwrecta");
                fileName = "pwrecta.pdf";
            } else {
                output = new File(fileName+".json");
                fileName = fileName+".pdf";
            }

            JSONOutput.output(output, tabulaRecta);
            PDFOutput.output(fileName, tabulaRecta);
            System.exit(0);
        } else {
            if("json".equals(format)) {
                JSONOutput.output(output, tabulaRecta);
            } else if("pdf".equals(format)) {
                PDFOutput.output(fileName, tabulaRecta);
            } else {
                AsciiOutput.output(output, tabulaRecta);
            }
        }

        System.exit(0);
    }

    public static void printHelpAndExit(Options options, String message) {
        if(message != null)
            logger.fatal("Usage error: " + message);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pwrecta", options);
        if(message != null) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    public static void printHelpAndExit(Options options) {
        printHelpAndExit(options, null);
    }

}
