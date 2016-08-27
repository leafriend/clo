package com.leafriend.clo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {

    private Format format = new Format();

    private FontManager fontManager = new FontManager("resources/fonts");

    private Font albumFont;
    private Font titleFont;
    private Font bodyFont;

    public void generate(String ablum, String title, String lyricsPath,
            String pdfPath) throws DocumentException, IOException {
        InputStream lyricsIn = new FileInputStream(lyricsPath);
        OutputStream pdfOut = new FileOutputStream(pdfPath);
        generate(ablum, title, lyricsIn, pdfOut);
    }

    public void generate(String album, String title, InputStream lyricsIn,
            OutputStream pdfOut) throws DocumentException, IOException {

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(lyricsIn, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        albumFont = fontManager.getFont(format.getAlbumFontFamily(),
                format.getAlbumFontSize());
        titleFont = fontManager.getFont(format.getTitleFontFamily(),
                format.getTitleFontSize());
        bodyFont = fontManager.getFont(format.getFontFamily(),
                format.getFontSize());

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, pdfOut);

        float width = format.getWidth();
        float height = format.getHeight();
        document.setPageSize(new Rectangle(width, height));

        float marginLeft = format.getMarginLeft();
        float marginRight = format.getMarginRight();
        float marginTop = format.getMarginTop();
        float marginBottom = format.getMarginBottom();
        document.setMargins(marginLeft, marginRight, marginTop, marginBottom);

        int albumHeight = 12;
        int titleHeight = 24;
        int paddingHeight = 14;
        int cellHeight = 10 + 15 + 5;

        float availableHeight = height - marginTop - marginBottom;
        int pageCount = 1;
        float restHeight = availableHeight;
        restHeight -= (albumHeight + titleHeight + paddingHeight);
        for (@SuppressWarnings("unused") String _ : lines) {
            if (restHeight >= cellHeight) {
                restHeight -= cellHeight;
            } else {
                restHeight = availableHeight;
                pageCount++;
            }
        }

        writer.setPageEvent(
                new AlbumTitleHeader(album, title, format, fontManager));
        writer.setPageEvent(new PageFooter(pageCount, format, fontManager));

        document.open();

        document.add(new Paragraph(albumHeight, album, albumFont));
        document.add(new Paragraph(titleHeight, title, titleFont));
        document.add(new Paragraph(paddingHeight, " ", bodyFont));

        printLyrics(document, lines);

        document.close();

    }

    private void printLyrics(Document document, List<String> lines)
            throws DocumentException, IOException {

        float[] columnWidths = { format.getLeftWidth(),
                format.getBetweenWidth(), format.getRightWidth() };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        for (String line : lines) {
            if (line.length() == 0)
                line = " ";

            PdfPCell leftCell = new PdfPCell(new Phrase(line, bodyFont));
            leftCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            leftCell.setPadding(0);
            leftCell.setPaddingTop(10);
            leftCell.setPaddingBottom(5);
            table.addCell(leftCell);

            PdfPCell betweenCell = new PdfPCell(new Phrase(" ", bodyFont));
            betweenCell.setBorder(0);
            table.addCell(betweenCell);

            PdfPCell rightCell = new PdfPCell(new Phrase(" ", bodyFont));
            rightCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            rightCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(rightCell);
        }

        document.add(table);
    }

}
