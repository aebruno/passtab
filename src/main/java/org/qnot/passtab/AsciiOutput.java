/*
 * Copyright (C) 2011  Andrew E. Bruno <aeb@qnot.org>
 *
 * This file is part of passtab.
 *
 * passtab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * passtab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with passtab.  If not, see <http://www.gnu.org/licenses/>.
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
