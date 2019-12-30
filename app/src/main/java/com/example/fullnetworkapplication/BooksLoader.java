package com.example.fullnetworkapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class BooksLoader extends AsyncTaskLoader<ArrayList<DataContainerModel>> {

    private String mURL;

    public BooksLoader(Context context, String mURL) {
        super(context);
        this.mURL = mURL;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<DataContainerModel> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        ArrayList<DataContainerModel> data = bookUtilities.FetchDataFromUrl(mURL);
        return data;
    }
}
