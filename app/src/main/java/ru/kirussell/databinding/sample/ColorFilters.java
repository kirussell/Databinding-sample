package ru.kirussell.databinding.sample;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/**
 * Created by russellkim on 15/11/15.
 * Utils for colorfilters
 */
public class ColorFilters {
    public static ColorFilter GRAYSCALE = createGrayScale();

    private static ColorFilter createGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return new ColorMatrixColorFilter(matrix);
    }
}
