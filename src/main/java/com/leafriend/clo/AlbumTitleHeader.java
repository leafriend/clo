package com.leafriend.clo;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class AlbumTitleHeader extends PdfPageEventHelper {

    private String album;

    private String title;

    private Font font;

    private float margin;

    public AlbumTitleHeader(String album, String title, Format format,
            FontManager fontManager) throws DocumentException, IOException {
        this.album = album;
        this.title = title;
        this.margin = format.getHeaderMargin();

        font = fontManager.getFont(format.getFontFamily(),
                format.getFontSize());
    }

    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();
        Phrase header = new Phrase(album + " / " + title, font);
        float x = (document.right() - document.left()) / 2
                + document.leftMargin();
        float y = document.top() + margin;
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header, x, y, 0);
    }

}
