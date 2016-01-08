package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.support.annotation.IntRange;

import com.tamerbarsbay.depothouston.R;

public class WidgetUtils {

    // Size options
    public static final int SIZE_1X1 = 0;
    public static final int SIZE_2X1 = 1;

    // Background color options
    public static final int BG_NAVY = 0;
    public static final int BG_DARK_GRAY = 1;
    public static final int BG_RED = 2;
    public static final int BG_BLUE = 3;
    public static final int BG_GREEN = 4;
    public static final int BG_ORANGE = 5;
    public static final int BG_MAROON = 6;
    public static final int BG_WHITE = 7;
    public static final int BG_BLACK = 8;

    public static final String[] BG_COLOR_STRINGS = new String[] {
            "Navy",
            "Dark Gray",
            "Red",
            "Blue",
            "Green",
            "Orange",
            "Maroon",
            "White",
            "Black"
    };

    private static final int[] PRIMARY_BG_COLOR_RES_ID = new int[] {
            R.color.navy,
            R.color.dark_gray,
            R.color.red,
            R.color.blue,
            R.color.green,
            R.color.orange,
            R.color.maroon,
            R.color.white,
            R.color.black
    };

    private static final int[] SECONDARY_BG_COLOR_RES_ID = new int[] {
            R.color.navy_secondary,
            R.color.dark_gray_secondary,
            R.color.red_secondary,
            R.color.blue_secondary,
            R.color.green_secondary,
            R.color.orange_secondary,
            R.color.maroon_secondary,
            R.color.white_secondary,
            R.color.black_secondary
    };

    public static int getPrimaryColorInt(Context context, @IntRange(from=0, to=8) int bgColorIndex) {
        return context.getResources().getColor(PRIMARY_BG_COLOR_RES_ID[bgColorIndex]);
    }

    public static int getSecondaryColorInt(Context context, @IntRange(from=0, to=8) int bgColorIndex) {
        return context.getResources().getColor(SECONDARY_BG_COLOR_RES_ID[bgColorIndex]);
    }
}
