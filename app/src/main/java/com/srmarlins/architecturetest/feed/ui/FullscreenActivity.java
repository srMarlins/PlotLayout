package com.srmarlins.architecturetest.feed.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.feed.data.api.model.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JaredFowler on 8/16/2016.
 */

public class FullscreenActivity extends AppCompatActivity {

    public static final String PHOTO_ARG = "photo";

    @BindView(R.id.fabSave)
    FloatingActionButton saveButton;

    @BindView(R.id.feedImage)
    ImageView feedImage;

    private Photo photo;

    public static Intent newIntent(Context context, Photo photo) {
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra(PHOTO_ARG, photo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        photo = getIntent().getParcelableExtra(PHOTO_ARG);
        if (displayFull(photo)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.feedImage)
    public void onImageClick() {
        saveButton.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveButton.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void init() {
        if (photo != null) {
            displayPhoto(photo);
        }
    }

    public void displayPhoto(Photo photo) {
        if (displayFull(photo)) {
            Picasso.with(this).load(photo.urls.full).fit().into(feedImage);
        } else {
            Picasso.with(this).load(photo.urls.regular).into(feedImage);
        }
    }

    public boolean displayFull(Photo photo) {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && photo.height > photo.width)
                || (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && photo.height < photo.width);
    }
}
