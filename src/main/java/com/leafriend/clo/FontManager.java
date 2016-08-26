package com.leafriend.clo;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;
import static com.itextpdf.text.pdf.BaseFont.createFont;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class FontManager {

    private Map<String, BaseFont> baseFonts = new HashMap<>();

    public FontManager(String... dirs) {
        Arrays.stream(dirs).forEach(this::scan);
    }

    private void scan(String dir) {
        File fontsDir = new File(dir);
        for (File fontFile : fontsDir.listFiles()) {
            String fileName = fontFile.getName().toLowerCase();
            if (!fileName.endsWith(".ttf") && !fileName.endsWith(".otf"))
                continue;
            try {
                scan(fontFile);
            } catch (DocumentException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException("Not handled", e);
            }
        }
    }

    private void scan(File fontFile) throws DocumentException, IOException {
        BaseFont baseFont = createFont(fontFile.getPath(), IDENTITY_H,
                EMBEDDED);
        for (String[] strs : baseFont.getFullFontName()) {
            String fontFamily = strs[3];
            if (!baseFonts.containsKey(fontFamily)) {
                baseFonts.put(fontFamily, baseFont);
            }
        }
    }

    public Font getFont(String familyName, float size)
            throws DocumentException, IOException {
        BaseFont baseFont = baseFonts.get(familyName);
        if (baseFont == null)
            baseFont = createFont();
        return new Font(baseFont, size);
    }

}
