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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFOutput implements OutputFormat {
    private static int X_START = 140;
    private static int Y_START = 570;

    private static int X_SPACE = 15;
    private static int Y_SPACE = 15;

    private static int BOX_WIDTH = 14;
    private static Color BOX_COLOR = new Color(204, 204, 204);
    private static Color FONT_COLOR = Color.BLACK;

    private static PDFont DEFAULT_FONT = PDType1Font.COURIER;
    private static PDFont BOLD_FONT = PDType1Font.COURIER_BOLD;
    
    private boolean withColor;
    
    public PDFOutput() { 
        this.withColor = true;
    }
    
    public PDFOutput(boolean withColor) {
        this.withColor = withColor;
    }
    
    public void output(OutputStream out, TabulaRecta tabulaRecta) throws IOException {
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

        String[][] array = tabulaRecta.asStringArray();
        
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                if(this.withColor && (i % 2) != 0) {
                    contentStream.setNonStrokingColor(PDFOutput.BOX_COLOR);
                    contentStream.fillRect(x-2, y-4, PDFOutput.BOX_WIDTH, PDFOutput.BOX_WIDTH);
                    contentStream.setNonStrokingColor(PDFOutput.FONT_COLOR);
                }
                if(this.withColor && (j % 2) != 0) {
                    contentStream.setFont(PDFOutput.BOLD_FONT, 12);
                } else {
                    contentStream.setFont(PDFOutput.DEFAULT_FONT, 12);
                }

                contentStream.beginText();
                contentStream.moveTextPositionByAmount(x,y);
                contentStream.drawString(array[i][j]);
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
            document.save(out);
            document.close();
        } catch(Exception e) {
            throw new IOException("Failed to create PDF: ", e);
        }
    }
}
