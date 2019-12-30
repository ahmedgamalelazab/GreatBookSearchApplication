package com.example.fullnetworkapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class bookUtilities {

    private static final String TAG = bookUtilities.class.getSimpleName();
    //we will use this tag in fixing the exception handling

    private bookUtilities() {
        //using private constructors ensuring that no more than one instance for this class
    }

    public static ArrayList<DataContainerModel> FetchDataFromUrl(String mURL) {

        //starting with url
        URL url = MakeURL(mURL);
        //the file json section
        String JsonResponse = "";
        try {

            JsonResponse = MakeHttpRequest(url);
        } catch (IOException e) {
            //handled later on
        }


        ArrayList<DataContainerModel> data = FetchDataFromJsonResponse(JsonResponse);

        return data;

    }


    private static ArrayList<DataContainerModel> FetchDataFromJsonResponse(String JsonResponse) {

        ArrayList<DataContainerModel> arr = new ArrayList<>();
        try {
            JSONObject Root = new JSONObject(JsonResponse);
            JSONArray innerRootArr = Root.getJSONArray("items");

            for (int i = 0; i < innerRootArr.length(); i++) {

                try {
                    JSONObject innerArrayObject = innerRootArr.getJSONObject(i);
                    JSONObject volumeInfo = innerArrayObject.getJSONObject("volumeInfo");
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                    String thumbnail = imageLinks.getString("thumbnail");
                    String BookTitle = volumeInfo.getString("title");
                    String previewLink = volumeInfo.getString("previewLink");
                    String PageCount = volumeInfo.getString("pageCount");

                    arr.add(new DataContainerModel(thumbnail, BookTitle, previewLink, PageCount));
                } catch (JSONException e) {
                    continue;
                }

            }


        } catch (JSONException e) {
            Log.e(TAG, "failed parsing the json file ", e);
        }
        return arr;
    }


    private static String MakeHttpRequest(URL url) throws IOException {
        String JsonResponse = "";
        if (url == null) {
            return JsonResponse;
        }
        HttpURLConnection URLConnection = null;
        InputStream inputStream = null;
        try {

            URLConnection = (HttpURLConnection) url.openConnection();
            URLConnection.setRequestMethod("GET");
            URLConnection.setConnectTimeout(10000);
            URLConnection.setReadTimeout(15000);
            URLConnection.connect();

            if (URLConnection.getResponseCode() == 200 || URLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = URLConnection.getInputStream();
                JsonResponse = ReadInputStream(inputStream);
            }

        } catch (IOException e) {
            //HANDLED LATER ON
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //HANDLED LATER
                } finally {
                    inputStream = null;
                }
            }
            if (URLConnection != null) {
                try {
                    URLConnection.disconnect();
                } finally {
                    URLConnection = null;
                }
            }
        }

        return JsonResponse;

    }


    private static String ReadInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader InputStreamOutput = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader read = new BufferedReader(InputStreamOutput);
            String Line = read.readLine();
            while (Line != null) {
                output.append(Line);
                Line = read.readLine();
            }

        }
        return output.toString();
    }


    private static URL MakeURL(String mURL) {
        URL url = null;
        try {
            url = new URL(mURL);
            return url;
        } catch (MalformedURLException e) {
            Log.e(TAG, "failed to convert the strings to url in utility section ", e);
            return null;
        }
    }


}
