package com.leafriend.clo;

import java.io.FileOutputStream;
import java.io.IOException;

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

    public void generate() throws DocumentException, IOException {

        BaseFont bf = BaseFont.createFont(
                "src/main/resources/NotoSansCJKjp-Regular.otf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font = new Font(bf, 10);

        String dest = "target/lyrics.pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.setPageSize(
                new Rectangle(format.getWidth(), format.getHeight()));
        document.setMargins(format.getMarginLeft(), format.getMarginRight(),
                format.getMarginTop(), format.getMarginBottom());

        document.open();

        printLyrics(document);

        document.close();

    }

    private void printLyrics(Document document)
            throws DocumentException, IOException {

        String lyrics = "Yeah Yeah 独壇場beauty\n孤独 絶望 Yeah Yeah お前が自由\n\nYeah Yeah 花も灰も beauty\n風も命も Yeah Yeah 無限も永遠も\n\nオーマイガッ\n神様も使えないな それなら勝手にやっちゃえ\nワインも煙草も薔薇もある 俺が笑って見ててやる\n\nYeah Yeah 飛ばしてくれ beauty\nあの空の上 Yeah Yeah お前の自由\n\nYeah Yeah 優しんだな beauty\nもう大丈夫 Yeah Yeah 何も起きないさ\n\nオーマイガッ\n神様は見ないふり それなら派手にやっちゃえ\n死ぬほど楽しめ踊れ 俺が笑って見ててやる\n\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop Yeah\n\nオーマイガッ\n神様も止められない 思うがままが人生\n最後に全部食べちゃえよ 俺が笑って見ててやる\n\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop Yeah\n\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop\nGo Go beauty round round Nothing's gonna stop\nYeah beauty round round Nothing's gonna stop Yeah";

        float[] columnWidths = { format.getLeftWidth(),
                format.getBetweenWidth(), format.getRightWidth() };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        for (String line : lyrics.split("\n", -1)) {
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

        document.add(table);
    }

}
