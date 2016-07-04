package com.example.android.booklisting;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URI;

public class RestTask extends AsyncTask<Uri, Void, String>
{
    private OnRestTaskCompleted mListener;

    public RestTask(OnRestTaskCompleted aListener)
    {
        mListener = aListener;
    }

    @Override
    protected String doInBackground(Uri... params)
    {
        try
        {
            return "This is a Test!";
        }
        catch (Exception e)
        {
            // TODO handle this properly
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String aResult)
    {
        Log.i("httpRequest finished:", "RESULT = " + aResult);
        mListener.onRestTaskCompleted(aResult);
    }

}