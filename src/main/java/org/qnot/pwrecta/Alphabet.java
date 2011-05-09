package org.qnot.pwrecta;

public class Alphabet {

    public static String[] ALPHA_UPPER_NUM = new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    public static String[] ALPHA_LOWER_NUM = new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    };

    public static String[] ALPHA_NUM_SYMBOL = new String[]{
        "0","1","2","3","4","5","6","7","8","9",
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
        "~","`","!","@","#","$","%","^","&","*","(",")","-","_","=","+","{","}",";",":","\"","'","?","/",">","<",
        ",","."
    };

    public static int getIndex(String symbol, String[] alphabet) {
        for(int i = 0; i < alphabet.length; i++) {
            if(symbol.equals(alphabet[i])) {
                return i;
            }
        }

        return -1;
    }
}
