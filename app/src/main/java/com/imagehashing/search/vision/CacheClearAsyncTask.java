package com.imagehashing.search.vision;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

public class CacheClearAsyncTask extends AsyncTask<Void, Void, Void> {
    Context context;

    public CacheClearAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Glide.get(context).clearDiskCache();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Glide.get(context).clearMemory();
    }
}
