package com.example.fullnetworkapplication;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public final class DataContainerModel {

    //section of private data
    private String thumbnailURL;
    private String BookName;
    private String PreviewLink;
    private String PageCount;
    //end of the private section


    //section of constructors
    public DataContainerModel(String thumbnail, String BookName, String prviewLinks, String pages) {
        this.thumbnailURL = thumbnail;
        this.BookName = BookName;
        this.PreviewLink = prviewLinks;
        this.PageCount = pages;
    }
    //end of constructors section


    //section of getters
    public String getthumbnailURL() {
        return this.thumbnailURL;
    }

    public String getBookName() {
        return this.BookName;
    }

    public String getPreviewLink() {
        return this.PreviewLink;
    }

    public String getPageCount() {
        return this.PageCount;
    }
    //end of getters section


    //special section  ... Converting the Strings to url

    public URL ConvertToUrl(String thumbnailURL) {
        URL url = null;
        try {
            url = new URL(thumbnailURL);
            return url;
        } catch (MalformedURLException e) {
            //handled later
            Log.e(DataContainerModel.class.getSimpleName(), "failed in converting the strings to url ", e);
            return null;
        }
    }


}
