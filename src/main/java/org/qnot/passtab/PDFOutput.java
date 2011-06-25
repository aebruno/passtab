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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFOutput implements OutputFormat {
    private static String FONT_MONOSPACE = "org/qnot/passtab/fonts/DejaVuSansMono.ttf";
    private static String FONT_MONOSPACE_BOLD = "org/qnot/passtab/fonts/DejaVuSansMono-Bold.ttf";
    private static int CELL_WIDTH = 15;
    private static float DEFAULT_FONT_SIZE = 12f;

    private boolean withColor;
    private Font font;
    private Font fontBold;

    public PDFOutput() {
        this(true);
    }

    public PDFOutput(boolean withColor) {
        this.withColor = withColor;

        try {
            font = new Font(BaseFont.createFont(PDFOutput.FONT_MONOSPACE,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            fontBold = new Font(BaseFont.createFont(
                    PDFOutput.FONT_MONOSPACE_BOLD, BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED));
        } catch (Exception e) {
            try {
                font = new Font(BaseFont.createFont());
                fontBold = new Font(BaseFont.createFont());
            } catch (Exception ignored) {
            }
        }

        font.setSize(DEFAULT_FONT_SIZE);
        fontBold.setSize(DEFAULT_FONT_SIZE);
    }

    public void output(OutputStream out, TabulaRecta tabulaRecta) throws IOException {
        try {
            createPDF(out, tabulaRecta.asStringArray());
        } catch (DocumentException e) {
            throw new IOException("failed to create pdf file: "
                    + e.getMessage(), e);
        }
    }

    private void createPDF(OutputStream out, String[][] array)
            throws IOException, DocumentException {
        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, out);
        document.open();

        PdfPTable table = new PdfPTable(array[0].length);
        table.setTotalWidth((float) array.length * PDFOutput.CELL_WIDTH);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                addCell(table, array[i][j], (j % 2) != 0, (i % 2) != 0, j == 0,
                        i == 0);
            }
        }

        document.add(table);
        document.close();
    }

    private void addCell(PdfPTable table, String str, boolean bold,
            boolean fill, boolean rightBorder, boolean bottomBorder) {
        Phrase phrase = withColor ? new Phrase(str, bold ? fontBold : font)
                : new Phrase(str, font);

        PdfPCell cell = new PdfPCell(phrase);

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0f);
        cell.setPaddingTop(2f);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (rightBorder) {
            cell.setBorderWidthRight(1f);
            cell.setPaddingRight(3f);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        }
        if (bottomBorder) {
            cell.setBorderWidthBottom(1f);
            cell.setPaddingBottom(3f);
        }

        if (fill && this.withColor) {
            cell.setGrayFill(0.80f);
        }

        table.addCell(cell);
    }
}
