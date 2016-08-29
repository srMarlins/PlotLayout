package com.srmarlins.architecturetest.ui;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.ui.model.BitmapPointFactory;
import com.srmarlins.architecturetest.ui.model.GraphAnimation;
import com.srmarlins.architecturetest.ui.model.PointPath;
import com.srmarlins.architecturetest.ui.widget.PlotAnimator;
import com.srmarlins.architecturetest.ui.widget.PlotLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class PointTestActivity extends BaseActivity {

    @BindView(R.id.graphView)
    PlotLayout plotLayout;

    private PlotAnimator plotAnimator;
    private boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_test);
        ButterKnife.bind(this);
        plotLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!initialized) {
                    init();
                }
            }
        });
    }

    public void init() {
        initialized = true;
        plotAnimator = new PlotAnimator(plotLayout);
        PointPath pointList = new PointPath();
        pointList.addPoint(BitmapPointFactory.getAnimateablePoint(PlotAnimator.PathType.CUBIC, 0, 0, 1000, 0, 10, 270, -180, -1000));
        pointList.addPoint(BitmapPointFactory.getAnimateablePoint(PlotAnimator.PathType.CUBIC, 30, 30, 1000, 0, 10, 180, 90, 40));
        pointList.addPoint(BitmapPointFactory.getAnimateablePoint(PlotAnimator.PathType.CUBIC, plotLayout.sizeOfX() - 2, plotLayout.sizeOfY() - 2, 300, 0, 10, 10, 20, 35));

        pointList.setPathTag(getString(R.string.simple_single_animation_tag));
        GraphAnimation graphAnimation = new GraphAnimation();
        graphAnimation.addPath(pointList);
        plotAnimator.setAnimation(graphAnimation);
        plotAnimator.start();
    }
}
