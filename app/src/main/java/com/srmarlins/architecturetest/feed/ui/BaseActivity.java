package com.srmarlins.architecturetest.feed.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.srmarlins.architecturetest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.content_container)
    FrameLayout container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(R.layout.activity_base);
        container = ButterKnife.findById(this, R.id.content_container);
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
        container.addView(view);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    public void setView(View view){
        container.removeAllViews();
        container.addView(view);
    }
}
