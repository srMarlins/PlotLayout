package com.srmarlins.architecturetest.feed.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.srmarlins.architecturetest.R;
import com.srmarlins.architecturetest.feed.data.api.model.Photo;
import com.srmarlins.architecturetest.util.ColorUtil;
import com.srmarlins.architecturetest.util.ViewUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by JaredFowler on 8/15/2016.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    public static final String JSON_DATE_PATTERN = "yyyy-MM-DD'T'HH:mm:ssZZZ";
    public static final String DISPLAY_DATE_PATTERN = "MM/DD/yyyy";

    public List<Photo> data = new ArrayList<>();
    private DateFormat jsonDateFormat = new SimpleDateFormat(JSON_DATE_PATTERN);
    private DateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_PATTERN);

    private final PublishSubject<View> onClickSubject = PublishSubject.create();

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final Photo photo = data.get(position);
        holder.feedImage.setMinimumHeight((int) (ViewUtil.pxToDp(holder.itemView.getContext(), photo.height) / 2));
        Picasso.with(holder.itemView.getContext()).load(photo.urls.regular).into(holder.feedImage);
        Picasso.with(holder.itemView.getContext()).load(photo.user.profile_image.medium).into(holder.profileImage);
        holder.feedImage.setContentDescription(photo.toString());
        holder.nameText.setText(photo.user.name);
        holder.usernameText.setText(photo.user.username);
        try {
            holder.dateText.setText(displayDateFormat.format(jsonDateFormat.parse(photo.created_at)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setTextColor(ColorUtil.equalizeContrast(photo.color, 0));
        holder.feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(photo);
                onClickSubject.onNext(view);
            }
        });
    }

    public Observable<View> getPositionClicks() {
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void addData(List<Photo> data) {
        this.data.addAll(data);
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feedImage)
        ImageView feedImage;

        @BindView(R.id.nameText)
        TextView nameText;

        @BindView(R.id.usernameText)
        TextView usernameText;

        @BindView(R.id.dateText)
        TextView dateText;

        @BindView(R.id.profileImage)
        ImageView profileImage;

        public FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTextColor(int color) {
            nameText.setTextColor(color);
            dateText.setTextColor(color);
            usernameText.setTextColor(color);
        }
    }
}
