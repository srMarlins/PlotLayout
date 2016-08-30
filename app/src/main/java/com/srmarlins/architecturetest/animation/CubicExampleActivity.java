package com.srmarlins.architecturetest.animation;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.feed.ui.BaseActivity;
import com.srmarlins.plotlayout.animation.PlotAnimator;
import com.srmarlins.plotlayout.util.PointFactory;
import com.srmarlins.plotlayout.animation.GraphAnimation;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.widget.PlotLayout;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class CubicExampleActivity extends BaseActivity {

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
                if (!initialized) {
                    init();
                }
            }
        });
    }

    public void init() {
        initialized = true;
        plotAnimator = new PlotAnimator(plotLayout);
        PointPath pointList = new PointPath();
        pointList.addPoint(PointFactory.getCubicPoint(0, 0, 500, 120));
        pointList.addPoint(PointFactory.getCubicPoint(30, 20, 500, 80));
        pointList.addPoint(PointFactory.getCubicPoint(40, 0, 500, 90));

        pointList.setPathTag(getString(R.string.simple_single_animation_tag));
        GraphAnimation graphAnimation = new GraphAnimation();
        graphAnimation.addPath(pointList);
        plotAnimator.setAnimation(graphAnimation);
        plotAnimator.start();
    }
}
