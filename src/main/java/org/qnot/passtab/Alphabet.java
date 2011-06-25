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

import java.util.ArrayList;
import java.util.List;

/**
 * Alphabet class. Provides a default set of alphabets to use for generating
 * the Tabula Recta. 
 * 
 * @author Andrew E. Bruno <aeb@qnot.org>
 */
public class Alphabet {
     
    public static final Alphabet ALPHA_UPPER_NUM = new Alphabet(new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    });

    public static final Alphabet ALPHA_LOWER_NUM = new Alphabet(new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    });

    public static final Alphabet ALPHA_NUM_SYMBOL = new Alphabet(new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
        "~","`","!","@","#","$","%","^","&","*","(",")","-","_","=","+","{","}",";",":","\"","'","?","/",">","<",
        ",","."
    });
    
    private String[] alphabet;
    
    private Alphabet(String[] alphabet) {
        this.alphabet = alphabet;
    }
    
    public String[] getAlphabet() {
         return this.alphabet;
    }
    
    public int size() {
        return this.alphabet.length;
    }
    
    public String get(int index) {
        return this.alphabet[index];
    }

    public int getIndex(String symbol) {
        for(int i = 0; i < this.alphabet.length; i++) {
            if(symbol.equals(this.alphabet[i])) {
                return i;
            }
        }

        return -1;
    }
    
    public static Alphabet fromString(String str) throws AlphabetParseException {
        String[] symbols = str.split(",");
        if(symbols == null || symbols.length == 0) {
            throw new AlphabetParseException("Empty alphabet");
        }
        
        List<String> list = new ArrayList<String>();
        for(String s : symbols) {
            list.add(s.trim());
        }

        String[] strArray = list.toArray(new String[list.size()]);
        Alphabet.check(strArray);
        
        return new Alphabet(strArray);
    }
    
    public static Alphabet fromStringArray(String[] strArray) throws AlphabetParseException {
        Alphabet.check(strArray);
        return new Alphabet(strArray);
    }
    
    private static void check(String[] strArray) throws AlphabetParseException {
        if(strArray.length <= 1) {
            throw new AlphabetParseException("Alphabets must have more than 1 symbol");
        }
        
        for(int i = 0; i < strArray.length; i++) {
            if(strArray[i].length() != 1) {
                throw new AlphabetParseException("Alphabet symbols must be 1 character in length");
            }
        }
    }


}
