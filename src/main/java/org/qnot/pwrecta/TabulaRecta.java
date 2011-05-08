package org.qnot.pwrecta;

import java.security.Security;
import java.security.Provider;
import org.apache.commons.math.random.RandomDataImpl;

public class TabulaRecta {

    public static String[][] generate(String[] header, String[] alphabet) throws Exception {
        String[][] tabulaRecta = new String[header.length+1][header.length+1];

        RandomDataImpl rand = new RandomDataImpl();
        rand.setSecureAlgorithm("SHA1PRNG","SUN"); 
        //rand.reSeedSecure();

        tabulaRecta[0][0] = " ";

        for(int i = 1; i <= header.length; i++) {
            tabulaRecta[0][i] = header[i-1];
        }

        for(int i = 0; i < header.length; i++) {
            tabulaRecta[i+1] = TabulaRecta.generateRow(i, rand, header, alphabet);
        }

        return tabulaRecta;
    }

    private static String[] generateRow(int rowIndex, RandomDataImpl rand, String[] header, String[] alphabet) {
        String[] row = new String[header.length+1];
        row[0] = header[rowIndex];

        for(int i = 1; i <= header.length; i++) {
            int index = rand.nextSecureInt(0,(alphabet.length-1));
            row[i] = alphabet[index];
        }

        return row;
    }
}
