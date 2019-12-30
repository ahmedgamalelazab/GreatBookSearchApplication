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


    private String BOOKS_URL_REQUEST;
    private TemplateAdapter BooksAdapter;

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
            BOOKS_URL_REQUEST = "https://www.googleapis.com/books/v1/volumes?q=" + searchKey + "&key=AIzaSyDxXZT1gUcFlTEaC95oysA3MHrZl7qmHss";
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
        return new BooksLoader(this, BOOKS_URL_REQUEST);
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
