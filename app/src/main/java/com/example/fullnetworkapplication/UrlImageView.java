package com.example.fullnetworkapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlImageView extends ImageView {

    private AsyncTask<URL, Void, Bitmap> CurrentLoadingTask;
    private Object LoadingMonitor = new Object(); //for asynchroized state


    public UrlImageView(Context context, AttributeSet att, int defStyle) {
        super(context, att, defStyle);
    }

    public UrlImageView(Context context, AttributeSet att) {
        super(context, att);
    }

    public UrlImageView(Context context) {
        super(context);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        CancelLoading();
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageResource(int resId) {
        CancelLoading();
        super.setImageResource(resId);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        CancelLoading();
        super.setImageURI(uri);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        CancelLoading();
        super.setImageDrawable(drawable);
    }

    public void setImageURL(URL url) {
        synchronized (LoadingMonitor) {
            CancelLoading();
            this.CurrentLoadingTask = new UrlImageTask(this).execute(url);
        }
    }

    public void CancelLoading() {

        synchronized (LoadingMonitor) {
            if (this.CurrentLoadingTask != null) {
                this.CurrentLoadingTask.cancel(true);
                this.CurrentLoadingTask = null;
            }
        }
    }

    private static class UrlImageTask extends AsyncTask<URL, Void, Bitmap> {

        private final ImageView UpdateView;
        private boolean isCancelled = false;
        private InputStream inputStream;

        private UrlImageTask(ImageView updateView) {
            this.UpdateView = updateView;
        }

        @Override
        protected Bitmap doInBackground(URL... Params) {
            try {

                URLConnection urlconnection = Params[0].openConnection();
                urlconnection.setUseCaches(true);
                this.inputStream = urlconnection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {
                Log.e(UrlImageView.class.getSimpleName(), "error with ur data on the link !", e);
                return null;
            } finally {
                if (this.inputStream != null) {
                    try {
                        this.inputStream.close();
                    } catch (IOException e) {
                        //handled later
                    } finally {
                        this.inputStream = null;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (!this.isCancelled) {
                this.UpdateView.setImageBitmap(result);
            }
        }


        @Override
        protected void onCancelled() {
            this.isCancelled = true;
            try {

                if (this.inputStream != null) {

                    try {
                        this.inputStream.close();
                    } catch (IOException e) {
                        //handled later
                    } finally {
                        this.inputStream = null;
                    }

                }

            } finally {
                super.onCancelled();
            }
        }
    }


}
