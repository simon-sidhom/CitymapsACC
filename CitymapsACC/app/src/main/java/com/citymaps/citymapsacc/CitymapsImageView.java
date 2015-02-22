package com.citymaps.citymapsacc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CitymapsImageView extends ImageView implements GetBitmapAsync.OnBitmapRetrievedListener {
    private static final int TRANSITION_TIME_MILLIS = 500;
    private View progressView;

    public CitymapsImageView(Context context) {
        super(context);
    }

    public CitymapsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CitymapsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CitymapsImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImageUrl(String url, View progressView, int position) {
        this.progressView = progressView;
        progressView.setVisibility(VISIBLE);
        setVisibility(GONE);
        new GetBitmapAsync(this, getContext()).execute(
                new GetBitmapAsync.Params(position, url));
    }

    @Override
    public void onBitmapRetrieved(Bitmap bitmap) {
        final TransitionDrawable transitionDrawable =
                new TransitionDrawable(new Drawable[]{
                        new ColorDrawable(Color.TRANSPARENT),
                        new BitmapDrawable(getResources(), bitmap)
                });


        setImageDrawable(transitionDrawable);
        if (progressView != null) {
            progressView.setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        invalidate();
        requestLayout();
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(TRANSITION_TIME_MILLIS);
    }
}
