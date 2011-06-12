/*
 * Copyright 2011 Andrew E. Bruno <aeb@qnot.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.qnot.passtab;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * Output Tabula Recta in ASCII format
 * 
 * @author Andrew E. Bruno <aeb@qnot.org>
 */
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
