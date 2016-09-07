package com.srmarlins.plotlayout.util;

import com.srmarlins.plotlayout.animation.PlotAnimator;

/**
 * Created by JaredFowler on 9/2/2016.
 */

public class AnimationUtil {
    public static PlotAnimator.StartDirection getSweepDirection(int x1, int y1, int x2, int y2) {
        PlotAnimator.StartDirection direction;
        if (x1 > x2) {
            if(y1 > y2) {
                direction = PlotAnimator.StartDirection.TOP_RIGHT;
            } else {
                direction = PlotAnimator.StartDirection.BOTTOM_RIGHT;
            }
        } else {
            if(y1 > y2) {
                direction = PlotAnimator.StartDirection.TOP_LEFT;
            } else {
                direction = PlotAnimator.StartDirection.BOTTOM_LEFT;
            }
        }
        return direction;
    }
}
