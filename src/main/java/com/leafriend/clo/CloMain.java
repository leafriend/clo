package com.leafriend.clo;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

public class CloMain {

    public static void main(String[] args)
            throws DocumentException, IOException {

        File lyricsDir = new File("resources/lyrics/txt");
        String lyricsFile = "Buck-Tick/[2010-03-24] 独壇場Beauty/01-独壇場Beauty.txt";
        File lyrics = new File(lyricsDir, lyricsFile);

        File pdfDir = new File("resources/lyrics/pdf");
        String pdfFile = lyricsFile.substring(0, lyricsFile.length() - 4)
                + ".pdf";
        File pdf = new File(pdfDir, pdfFile);
        pdf.getParentFile().mkdirs();

        new PdfGenerator().generate(lyrics.getCanonicalPath(),
                pdf.getCanonicalPath());

    }

}
