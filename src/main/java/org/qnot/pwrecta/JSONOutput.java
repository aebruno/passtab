package org.qnot.pwrecta;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

public class JSONOutput {

    public static void output(String[][] tabulaRecta) {
        Gson gson = new Gson();

        try {
            FileUtils.writeStringToFile(new File("pwrecta.json"), 
                                        gson.toJson(tabulaRecta),
                                        "UTF-8");
        } catch(IOException e) {
            System.err.println("Failed to write JSON output: "+e.getMessage());
        }
    }
}
