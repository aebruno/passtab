package org.qnot.pwrecta;

public class Main {
    public static void main(String[] args) throws Exception {
        String[][] tabulaRecta = TabulaRecta.generate(Alphabet.ALPHA_UPPER_NUM, Alphabet.ALPHA_NUM_SYMBOL);
        AsciiOutput.output(tabulaRecta);
        JSONOutput.output(tabulaRecta);
        PDFOutput.output("pwrecta.pdf", tabulaRecta);
    }
}
