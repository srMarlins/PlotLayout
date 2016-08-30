package com.srmarlins.plotlayout.util;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by JaredFowler on 8/16/2016.
 */

public class ViewUtil {
    public static RectF getVisibleViewRectF(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        RectF rectF = new RectF(rect);
        return rectF;
    }
}
