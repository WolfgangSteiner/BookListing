package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Book> mBookList;
    BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookList = new ArrayList<Book>();

        ListView listView = (ListView) findViewById(R.id.list_view);
        mBookAdapter = new BookAdapter(this, mBookList);
        listView.setAdapter(mBookAdapter);
    }

    public void onStartSearch(View aView)
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            mBookList.clear();
            mBookList.add(new Book("20000 miles under the sea", "Jules Verne"));
            mBookAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, R.string.network_not_reachable, Toast.LENGTH_SHORT).show();
        }
    }
}
