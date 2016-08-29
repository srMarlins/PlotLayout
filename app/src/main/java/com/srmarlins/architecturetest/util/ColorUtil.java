package com.srmarlins.architecturetest.util;


import android.graphics.Color;
import android.support.annotation.NonNull;

/**
 * Created by JaredFowler on 8/16/2016.
 */

public class ColorUtil {
    public static int equalizeContrast(@NonNull String hexColor, double threshold) {
        double contrastThreshold = threshold > 0 ? threshold : .7;
        int color = (int) Long.parseLong(hexColor.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b) / 255;
        a = contrastThreshold / a;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] /= a;
        color = Color.HSVToColor(hsv);

        return color;
    }
}
