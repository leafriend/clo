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

public class PageFooter extends PdfPageEventHelper {

    private int pageCount;

    private Font font;

    private float margin;

    public PageFooter(int pageCount, Format format, FontManager fontManager)
            throws DocumentException, IOException {
        this.pageCount = pageCount;
        this.margin = format.getFooterMargin();

        font = fontManager.getFont(format.getFooterFontFamily(),
                format.getFooterFontSize());
    }

    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(document.getPageNumber() + " / " + pageCount,
                font);
        float x = (document.right() - document.left()) / 2
                + document.leftMargin();
        float y = document.bottom() - margin;
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, x, y, 0);
    }

}
