package org.qnot.pwrecta;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class AsciiOutput implements OutputFormat {

    public void output(OutputStream out, TabulaRecta tabulaRecta) throws IOException {
        
        String[][] array = tabulaRecta.asStringArray();
        
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                IOUtils.write(array[i][j]+" ", out, "UTF-8");
            }
            IOUtils.write("\n", out, "UTF-8");
        }
    }
}
