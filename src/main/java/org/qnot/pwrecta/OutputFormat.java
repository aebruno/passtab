package org.qnot.pwrecta;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputFormat {
    
    public void output(OutputStream out, TabulaRecta tabulaRecta) throws IOException;

}
