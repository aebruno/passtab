package org.qnot.pwrecta;

/**
 * Alphabet class. Provides a default set of alphabets to use for generating
 * the Tabula Recta. 
 * @author aeb
 *
 */
public class Alphabet {
    private String[] alphabet;
    
    public Alphabet() { }
    
    public Alphabet(String[] alphabet) {
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

}
