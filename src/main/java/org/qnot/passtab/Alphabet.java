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
