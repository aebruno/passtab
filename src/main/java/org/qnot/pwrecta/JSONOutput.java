package org.qnot.pwrecta;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class JSONOutput implements OutputFormat {

    public void output(OutputStream out, TabulaRecta tabulaRecta) throws IOException {
        Gson gson = new Gson();
        IOUtils.write(gson.toJson(tabulaRecta), out, "UTF-8");
    }
}
