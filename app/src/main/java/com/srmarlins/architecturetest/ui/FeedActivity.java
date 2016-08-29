package com.srmarlins.architecturetest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.srmarlins.architecturetest.ArchitectureApp;
import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.data.PhotoManager;
import com.srmarlins.architecturetest.data.api.model.Photo;
import com.srmarlins.architecturetest.ui.adapter.FeedAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class FeedActivity extends BaseActivity {

    @Inject
    PhotoManager photoManager;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.recycler_feed)
    RecyclerView feed;

    private FeedAdapter feedAdapter;
    private LinearLayoutManager layoutManager;
    private Subscriber<List<Photo>> photosResponseSubscriber;
    private Subscription previousCall;
    private Observable<View> photoObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        ArchitectureApp.getDataComponent(this).inject(this);
        initFeed();
        photoObservable = feedAdapter.getPositionClicks();
        photoObservable.subscribe(new Subscriber<View>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(View view) {
                Intent intent = FullscreenActivity.newIntent(FeedActivity.this, (Photo) view.getTag());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(FeedActivity.this, view, "fullscreen");
                startActivity(intent, optionsCompat.toBundle());
            }
        });
    }

    private void initFeed() {
        layoutManager = new LinearLayoutManager(this);
        feed.setLayoutManager(layoutManager);
        feedAdapter = new FeedAdapter();
        feed.setAdapter(feedAdapter);
        photosResponseSubscriber = new Subscriber<List<Photo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<Photo> photosResponse) {
                FeedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        feedAdapter.addData(photosResponse);
                        feedAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        feed.addOnScrollListener(new LoadingScrollListener(layoutManager) {
            @Override
            public void onLoadData(int pageNumber) {
                if (previousCall != null) {
                    previousCall.unsubscribe();
                }
                previousCall = photoManager.getPhotos(photosResponseSubscriber, pageNumber, null);
            }
        });

        photoManager.getPhotos(photosResponseSubscriber, 1, null);
    }
}
