package com.leafriend.clo;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

public class CloMain {

    public static void main(String[] args)
            throws DocumentException, IOException {

        new PdfGenerator().generate();

    }

}
