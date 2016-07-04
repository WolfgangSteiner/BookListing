package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnRestTaskCompleted {
    ArrayList<Book> mBookList;
    BookAdapter mBookAdapter;
    RestTask mRestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookList = new ArrayList<Book>();

        ListView listView = (ListView) findViewById(R.id.list_view);
        mBookAdapter = new BookAdapter(this, mBookList);
        listView.setAdapter(mBookAdapter);
    }

    public void onStartSearch(View aView) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            EditText searchField = (EditText) findViewById(R.id.search_field);
            String searchString = searchField.getText().toString();

            if (!searchString.trim().isEmpty()) {
                Uri myUri = buildUri(searchString);
                new RestTask(this).execute(myUri);
            }
        } else {
            Toast.makeText(this, R.string.network_not_reachable, Toast.LENGTH_SHORT).show();
        }
    }

    public void onRestTaskCompleted(String aResultString) {
        mBookList.clear();
        parseJson(aResultString);
        mBookAdapter.notifyDataSetChanged();
    }

    private Uri.Builder buildUriBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books")
                .appendPath("v1");

        return builder;
    }

    private Uri buildUri(String aSearchString) {
        Uri.Builder builder = buildUriBuilder();
        builder.appendPath("volumes");
        builder.appendQueryParameter("q", aSearchString);
        builder.appendQueryParameter("key", BuildConfig.GOOGLE_BOOKS_API_KEY);

        return builder.build();
    }

    private void parseJson(String jsonString) {
        try
        {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray volumes = jsonObj.getJSONArray("items");

            for (int i = 0; i < volumes.length(); i++) 
            {
                JSONObject volume = volumes.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String title = volumeInfo.getString("title");
                mBookList.add(new Book(title, formatAuthors(authors)));
            }
        }
        catch (JSONException e)
        {
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatAuthors(JSONArray aAuthors)
    {
        if (aAuthors.length() == 0)
        {
            return "";
        }

        try {
            String Result = aAuthors.getString(0);

            for (int i = 1; i < aAuthors.length(); i++) {
                Result += ", ";
                Result += aAuthors.getString(i);
            }

            return Result;
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
