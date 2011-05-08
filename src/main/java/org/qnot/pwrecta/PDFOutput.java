package org.qnot.pwrecta;

import java.awt.Color;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class PDFOutput {
    private static int X_START = 140;
    private static int Y_START = 570;

    private static int X_SPACE = 15;
    private static int Y_SPACE = 15;

    private static int BOX_WIDTH = 14;
    private static Color BOX_COLOR = new Color(204, 204, 204);
    private static Color FONT_COLOR = Color.BLACK;

    private static PDFont DEFAULT_FONT = PDType1Font.COURIER;
    private static PDFont BOLD_FONT = PDType1Font.COURIER_BOLD;

    public static void output(String file, String[][] tabulaRecta) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        page.setMediaBox(PDPage.PAGE_SIZE_A4);
        page.setRotation(90);
        document.addPage( page );
        float pageWidth = PDPage.PAGE_SIZE_A4.getWidth();

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);
        contentStream.setFont(PDFOutput.DEFAULT_FONT, 12);

        int x = PDFOutput.X_START;
        int y = PDFOutput.Y_START;

        for(int i = 0; i < tabulaRecta.length; i++) {
            for(int j = 0; j < tabulaRecta[0].length; j++) {
                if((i % 2) != 0) {
                    contentStream.setNonStrokingColor(PDFOutput.BOX_COLOR);
                    contentStream.fillRect(x-2, y-2, PDFOutput.BOX_WIDTH, PDFOutput.BOX_WIDTH);
                    contentStream.setNonStrokingColor(PDFOutput.FONT_COLOR);
                }
                if((j % 2) != 0) {
                    contentStream.setFont(PDFOutput.BOLD_FONT, 12);
                } else {
                    contentStream.setFont(PDFOutput.DEFAULT_FONT, 12);
                }

                contentStream.beginText();
                contentStream.moveTextPositionByAmount(x,y);
                contentStream.drawString(tabulaRecta[i][j]);
                contentStream.endText();
                x += PDFOutput.X_SPACE;
            }
            y -= PDFOutput.Y_SPACE;
            x = PDFOutput.X_START;
        }

        contentStream.drawLine(130, 567, 695, 567);
        contentStream.drawLine(152, 585, 152, 20);
        contentStream.close();

        try {
            document.save(file);
            document.close();
        } catch(Exception e) {
            throw new IOException("Failed to create PDF: "+file, e);
        }
    }
}
