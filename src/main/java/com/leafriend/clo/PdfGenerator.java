package com.leafriend.clo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {

    private Format format = new Format();

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

        albumFont = loadFont(format.getAlbumFontFamily(), format.getAlbumFontSize());
        titleFont = loadFont(format.getTitleFontFamily(), format.getTitleFontSize());
        bodyFont = loadFont(format.getFontFamily(), format.getFontSize());

        Document document = new Document();
        PdfWriter.getInstance(document, pdfOut);

        document.setPageSize(
                new Rectangle(format.getWidth(), format.getHeight()));
        document.setMargins(format.getMarginLeft(), format.getMarginRight(),
                format.getMarginTop(), format.getMarginBottom());

        document.open();

        document.add(new Paragraph(album, albumFont));
        document.add(new Paragraph(title, titleFont));
        document.add(new Paragraph(" ", bodyFont));

        printLyrics(document, lyricsIn);

        document.close();

    }

    private Font loadFont(String fontFamily, float fontSize)
            throws DocumentException, IOException {
        File fontsDir = new File("resources/fonts");
        for (File fontFile : fontsDir.listFiles()) {
            String fileName = fontFile.getName().toLowerCase();
            if (!fileName.endsWith(".ttf") && !fileName.endsWith(".otf"))
                continue;

            BaseFont bf = BaseFont.createFont(fontFile.getPath(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            for (String[] strs : bf.getFullFontName()) {
                if (fontFamily.equals(strs[3])) {
                    return new Font(bf, fontSize);
                }
            }
        }
        return new Font();
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

                PdfPCell leftCell = new PdfPCell(new Phrase(line, bodyFont));
                leftCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
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
        }

        document.add(table);
    }

}
