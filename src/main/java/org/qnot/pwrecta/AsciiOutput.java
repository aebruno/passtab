package org.qnot.pwrecta;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class AsciiOutput {

    public static void output(File outfile, String[][] tabulaRecta) {
        String output = "";
        for(int i = 0; i < tabulaRecta.length; i++) {
            for(int j = 0; j < tabulaRecta[0].length; j++) {
                output += tabulaRecta[i][j]+" ";
            }
            output += "\n";
        }

        if(outfile == null) {
            System.out.print(output);
            return;
        }

        try {
            FileUtils.writeStringToFile(outfile, 
                                        output,
                                        "UTF-8");
        } catch(IOException e) {
            System.err.println("Failed to write Ascii output: "+e.getMessage());
        }
    }
}
