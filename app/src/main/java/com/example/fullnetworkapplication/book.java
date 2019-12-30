package com.example.fullnetworkapplication;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class book extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<DataContainerModel>> {


    private String BOOKS_URL_REQUEST = "https://www.googleapis.com/books/v1/volumes"; //this will work as query
    private Uri BaseUri; //base query my url 
    Uri.Builder uriBuilder; //this will control the behaviour of our search 
    private TemplateAdapter BooksAdapter; //this will control the preview of our data 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        final ListView BookList = findViewById(R.id.List);
        BooksAdapter = new TemplateAdapter(this, new ArrayList<DataContainerModel>());
        BookList.setAdapter(BooksAdapter);
        Bundle Extras = getIntent().getExtras();
        if (Extras != null) {
            String searchKey = Extras.getString("Key");
            Log.e(book.class.getSimpleName(), searchKey);
            //BOOKS_URL_REQUEST = "https://www.googleapis.com/books/v1/volumes?q=" + searchKey + "&key=AIzaSyDxXZT1gUcFlTEaC95oysA3MHrZl7qmHss";
            BaseUri = Uri.parse(BOOKS_URL_REQUEST);
            uriBuilder = BaseUri.buildUpon();//parsing the url to have a control on it 
            uriBuilder.appendQueryParameter("key", "AIzaSyDxXZT1gUcFlTEaC95oysA3MHrZl7qmHss"); //this is my key , u can have another one from google easy 
            uriBuilder.appendQueryParameter("q", searchKey); //this is what u typed in search bar 
            uriBuilder.appendQueryParameter("maxResults","40"); //max results will appear in the search will be 40 
            Log.e(book.class.getSimpleName(),uriBuilder.toString());
        }
        BookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataContainerModel currentData = BooksAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentData.getPreviewLink()));
                startActivity(intent);
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);

    }

    @Override
    public Loader<ArrayList<DataContainerModel>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<DataContainerModel>> loader, ArrayList<DataContainerModel> data) {
        BooksAdapter.clear();
        if (data != null && !data.isEmpty()) {
            BooksAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<DataContainerModel>> loader) {
        BooksAdapter.clear();
    }
}
