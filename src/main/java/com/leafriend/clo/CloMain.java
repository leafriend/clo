package com.leafriend.clo;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

public class CloMain {

    private File txtDir;

    private File pdfDir;

    public static void main(String[] args)
            throws DocumentException, IOException {
        new CloMain("resources/lyrics/txt", "resources/lyrics/pdf")
                .traverse("");
    }

    public CloMain(String txtName, String pdfName) {
        txtDir = new File(txtName);
        pdfDir = new File(pdfName);
    }

    public void traverse(String path) throws DocumentException, IOException {
        File dir = new File(txtDir, path);
        for (File file : dir.listFiles()) {
            String subpath = path + "/" + file.getName();
            if (file.isDirectory()) {
                traverse(subpath);
            } else if (file.isFile()) {
                generate(subpath);
            }
        }

    }

    public void generate(String txtPath) throws DocumentException, IOException {

        if (!txtPath.endsWith(".txt"))
            return;
        System.out.println(txtPath);

        File txtFile = new File(txtDir, txtPath);

        String album = txtFile.getParentFile().getName();
        String title = txtFile.getName();
        title = title.substring(0, title.lastIndexOf('.'));

        String pdfPath = txtPath.substring(0, txtPath.length() - 4) + ".pdf";
        File pdfFile = new File(pdfDir, pdfPath);
        pdfFile.getParentFile().mkdirs();

        String txt = txtFile.getCanonicalPath();
        String pdf = pdfFile.getCanonicalPath();
        new PdfGenerator().generate(album, title, txt, pdf);

    }

}
