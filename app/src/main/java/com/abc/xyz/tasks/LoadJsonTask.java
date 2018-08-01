package com.abc.xyz.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    @Override
    protected String doInBackground(String... params) {
        String jsonData = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            jsonData = EntityUtils.toString(entity);

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
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
