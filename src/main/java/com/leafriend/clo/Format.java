package com.leafriend.clo;

public class Format {

    private double width_mm = 297;
    private double height_mm = 210;
    private double horizontalMargin = 25.4;
    private double verticalMargin = 19.1;

    private float width = mm2pt(width_mm);
    private float height = mm2pt(height_mm);

    private float marginLeft = mm2pt(horizontalMargin);
    private float marginRight = mm2pt(horizontalMargin);
    private float marginTop = mm2pt(verticalMargin);
    private float marginBottom = mm2pt(verticalMargin);

    private float spaceBetween = mm2pt(horizontalMargin);

    private float leftWidth = mm2pt(width - horizontalMargin * 3) / 2;
    private float betweenWidth = spaceBetween;
    private float rightWidth = mm2pt(width - horizontalMargin * 3) / 2;

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

}
