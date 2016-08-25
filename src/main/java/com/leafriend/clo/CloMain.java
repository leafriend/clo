package com.leafriend.clo;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

public class CloMain {

    public static void main(String[] args)
            throws DocumentException, IOException {

        String lyricsPath = "Buck-Tick/[2010-03-24] 独壇場Beauty/01-独壇場Beauty.txt";

        File lyricsDir = new File("resources/lyrics/txt");
        File lyricsFile = new File(lyricsDir, lyricsPath);

        File pdfDir = new File("resources/lyrics/pdf");
        String pdfPath = lyricsPath.substring(0, lyricsPath.length() - 4)
                + ".pdf";
        File pdfFile = new File(pdfDir, pdfPath);
        pdfFile.getParentFile().mkdirs();

        new PdfGenerator().generate(lyricsFile.getCanonicalPath(),
                pdfFile.getCanonicalPath());

    }

}
