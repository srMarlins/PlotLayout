package com.srmarlins.architecturetest.animation;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.feed.ui.BaseActivity;
import com.srmarlins.plotlayout.animation.PlotAnimator;
import com.srmarlins.plotlayout.util.PointFactory;
import com.srmarlins.plotlayout.animation.GraphAnimation;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.widget.PlotLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class CubicExampleActivity extends BaseActivity {

    public static final int SIDE = 3;

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
        List<View> viewMap = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        int sideSq = SIDE * SIDE;
        for(int i = 0; i < sideSq; i++) {
            ImageView imageView = new ImageView(this);
            plotLayout.addView(imageView);
            PlotLayout.LayoutParams layoutParams = (PlotLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.pathTag = String.valueOf(i);
            layoutParams.width = 30;
            layoutParams.height = 30;
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.circle);
            if(sideSq / 2 == i) {
                drawable.setColorFilter(Color.MAGENTA, PorterDuff.Mode.ADD);
            }
            imageView.setImageDrawable(drawable);
            pathList.add(layoutParams.pathTag);
            viewMap.add(imageView);
        }
        RotatingSquare rotatingSquare = new RotatingSquare();
        rotatingSquare.arrangeViews(viewMap, SIDE, 10);
        GraphAnimation graphAnimation = new GraphAnimation();
        graphAnimation.addPaths(rotatingSquare.getPaths(pathList, SIDE, 10, true));
        plotAnimator.setAnimation(graphAnimation);
    }

    @OnClick(R.id.start)
    public void onStartClick(){
        plotAnimator.start();
    }

    @OnClick(R.id.pause)
    public void onPauseClick(){
        plotAnimator.pause();
    }

    @OnClick(R.id.stop)
    public void onStopClick(){
        plotAnimator.stop();
    }
}
