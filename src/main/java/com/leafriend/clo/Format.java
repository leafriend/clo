package com.leafriend.clo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class Format {

    private static final String MM = "mm";
    private static final String PT = "pt";

    private final Properties props;

    private final float width;
    private final float height;

    private final float marginLeft;
    private final float marginRight;
    private final float marginTop;
    private final float marginBottom;

    private final float leftWidth;
    private final float betweenWidth;
    private final float rightWidth;

    private String textBodyFont;
    private float textBodySize;

    public Format() {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream("clo.properties");

        props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Not handled", e);
        }

        width = loadAsPt("format.paper.width");
        height = loadAsPt("format.paper.height");

        marginTop = loadAsPt("format.margin.top");
        marginLeft = loadAsPt("format.margin.left");
        marginRight = loadAsPt("format.margin.right");
        marginBottom = loadAsPt("format.margin.bottom");

        betweenWidth = loadAsPt("format.margin.center");
        leftWidth = (width - (marginLeft + betweenWidth + marginRight)) / 2;
        rightWidth = (width - (marginLeft + betweenWidth + marginRight)) / 2;

        textBodyFont = props.getProperty("text.body.font");
        textBodySize = loadAsPt("text.body.size");

    }

    private float loadAsPt(String key) {
        String value = props.getProperty(key);
        if (Pattern.matches("^\\d+(\\.\\d+)?" + MM + "$", value)) {
            double scalar = Double.parseDouble(
                    value.substring(0, value.length() - MM.length()));
            return mm2pt(scalar);
        } else if (Pattern.matches("^\\d+(\\.\\d+)?" + PT + "$", value)) {
            double scalar = Double.parseDouble(
                    value.substring(0, value.length() - PT.length()));
            return (float) scalar;
        } else {
            throw new RuntimeException("Unsupported unit value: " + value);
        }
    }

    public static float mm2pt(double mm) {
        return (float) (mm / 25.4 * 72);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public float getLeftWidth() {
        return leftWidth;
    }

    public float getBetweenWidth() {
        return betweenWidth;
    }

    public float getRightWidth() {
        return rightWidth;
    }

    public String getFontFamily() {
        return textBodyFont;
    }

    public float getFontSize() {
        return textBodySize;
    }

}
