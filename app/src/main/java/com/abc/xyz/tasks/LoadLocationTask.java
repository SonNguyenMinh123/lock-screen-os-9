package com.abc.xyz.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.view.Window;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.abc.xyz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by DucNguyen on 8/1/2015.
 */
public class LoadLocationTask extends AsyncTask<String, Void, Void> {
    private Dialog dialog;
    private Context mContext;
    private CommunicatorLoadLocation communicator;
    private static Location[] mLatLong;
    private List<String> mAddressList;
    //screen size

    public LoadLocationTask(Context mContext, CommunicatorLoadLocation communicator) {
        this.mContext = mContext;
        this.communicator = communicator;
        mAddressList = new ArrayList<String>();

    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_wheel);
        dialog.show();
        super.onPreExecute();

    }


    @Override
    protected Void doInBackground(final String... params) {
        String locateUrl = params[0];
        locateUrl = locateUrl.replace(" ", "%20");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(locateUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {


            @Override
            public void onFailure(Request request, IOException e) {
//                alertUserAboutError("OKHTTP Location Error");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    loadLocationList(jsonData);
                    if (response.isSuccessful()) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                communicator.onPostExecute(mAddressList, mLatLong);
                                if (dialog.getWindow() != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
        return null;
    }


    private void loadLocationList(String jsonData) throws JSONException {
        JSONObject addressJson = new JSONObject(jsonData);
        JSONArray results = addressJson.getJSONArray("RESULTS");
        int loop;
        if (results!=null && results.length() > 0) {
            if (results.length() > 5) {
                loop = 5;
            } else {
                loop = results.length();
            }
            mLatLong = new Location[loop];
            for (int i = 0; i < loop; i++) {
                mAddressList.add(results.getJSONObject(i).getString("name"));
                mLatLong[i] = new Location(results.getJSONObject(i).getString("name"));
                mLatLong[i].setLatitude(Double.parseDouble(results.getJSONObject(i).getString("lat")));
                mLatLong[i].setLongitude(Double.parseDouble(results.getJSONObject(i).getString("lon")));
            }
        }
    }

    public interface CommunicatorLoadLocation {
        public void onPostExecute(List<String> addressList, Location[] latLong);

    }

}
