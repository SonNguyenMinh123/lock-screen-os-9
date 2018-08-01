package com.abc.xyz.os10.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by DucNguyen on 23/02/2016.
 */
public class LoadJsonTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private LoadJsonTaskCallback mLoadJsonTaskCallback;

    public LoadJsonTask(Context context, LoadJsonTaskCallback loadJsonTaskCallback) {
        mContext = context;
        mLoadJsonTaskCallback = loadJsonTaskCallback;
    }

    public String readURL(String theUrl) {
        Log.e("readURL", "readURL");
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e("Loi", e.toString());
            e.printStackTrace();
        }
        Log.e("Content:__", content.toString());
        return content.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonData = "";
        jsonData = readURL(params[0]);
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONObject query = jsonObj.getJSONObject("query");
            String count = query.getString("count");
            if (count.equals("1"))
                return jsonData;
            else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mLoadJsonTaskCallback.onPostExecute(s);
    }

    public interface LoadJsonTaskCallback {
        public void onPostExecute(String jsonData);
    }
}
