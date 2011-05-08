package org.qnot.pwrecta;

public class AsciiOutput {

    public static void output(String[][] tabulaRecta) {
        for(int i = 0; i < tabulaRecta.length; i++) {
            for(int j = 0; j < tabulaRecta[0].length; j++) {
                System.out.print(tabulaRecta[i][j]+" ");
            }
            System.out.println();
        }
    }
}
