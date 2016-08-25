package com.leafriend.clo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {

    private Format format = new Format();

    private Font font;

    public void generate(String lyricsPath, String pdfPath)
            throws DocumentException, IOException {
        InputStream lyricsIn = new FileInputStream(lyricsPath);
        OutputStream pdfOut = new FileOutputStream(pdfPath);
        generate(lyricsIn, pdfOut);
    }

    public void generate(InputStream lyricsIn, OutputStream pdfOut)
            throws DocumentException, IOException {

        BaseFont bf = BaseFont.createFont("resources/fonts/NotoSansCJKjp-Regular.otf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font = new Font(bf, 10);

        Document document = new Document();
        PdfWriter.getInstance(document, pdfOut);

        document.setPageSize(
                new Rectangle(format.getWidth(), format.getHeight()));
        document.setMargins(format.getMarginLeft(), format.getMarginRight(),
                format.getMarginTop(), format.getMarginBottom());

        document.open();

        printLyrics(document, lyricsIn);

        document.close();

    }

    private void printLyrics(Document document, InputStream lyricsIn)
            throws DocumentException, IOException {

        float[] columnWidths = { format.getLeftWidth(),
                format.getBetweenWidth(), format.getRightWidth() };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(lyricsIn, "UTF-8"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0)
                    line = " ";

                PdfPCell leftCell = new PdfPCell(new Phrase(line, font));
                leftCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
                leftCell.setPaddingTop(10);
                leftCell.setPaddingBottom(5);
                table.addCell(leftCell);

                PdfPCell betweenCell = new PdfPCell(new Phrase(" ", font));
                betweenCell.setBorder(0);
                table.addCell(betweenCell);

                PdfPCell rightCell = new PdfPCell(new Phrase(" ", font));
                rightCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
                rightCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
                table.addCell(rightCell);
            }
        }

        document.add(table);
    }

}
