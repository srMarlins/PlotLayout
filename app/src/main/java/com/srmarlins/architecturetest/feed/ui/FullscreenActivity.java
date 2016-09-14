package com.srmarlins.architecturetest.feed.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.feed.data.api.model.Photo;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JaredFowler on 8/16/2016.
 */

public class FullscreenActivity extends AppCompatActivity {

    public static final String PHOTO_ARG = "photo";
    private static final int REQUEST_STORAGE = 111;

    @BindView(R.id.fabSave)
    FloatingActionButton saveButton;

    @BindView(R.id.feedImage)
    ImageView feedImage;

    private Photo photo;
    private Target storeTarget;

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

    @OnClick(R.id.fabSave)
    public void onSaveClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            downloadAndSavePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAndSavePhoto();
            }
        }
    }

    private void downloadAndSavePhoto() {
        storeTarget = new StoreTarget(getContentResolver(), photo.id, photo.toString());
        Picasso.with(this).load(photo.urls.full).into(storeTarget);
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

    public class StoreTarget implements Target {
        private final WeakReference<ContentResolver> resolver;
        private final String name;
        private final String desc;

        public StoreTarget(ContentResolver r, String name, String desc) {
            this.resolver = new WeakReference<>(r);
            this.name = name;
            this.desc = desc;
        }

        @Override
        public void onPrepareLoad(Drawable arg0) {
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
            ContentResolver r = resolver.get();
            if (r != null) {
                MediaStore.Images.Media.insertImage(r, bitmap, name, desc);
            }
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            Log.i("Save", "Error saving");
        }
    }
}
