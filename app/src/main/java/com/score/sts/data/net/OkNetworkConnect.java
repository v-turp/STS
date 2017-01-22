package com.score.sts.data.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Who Dat on 1/18/2017.
 */
public class OkNetworkConnect {

    private static final String TAG = OkNetworkConnect.class.getSimpleName();

    public OkNetworkConnect(){}

    public String run(String url){

        /** STEP 1: initialize the client*/
        OkHttpClient client = new OkHttpClient();

        /** STEP 2: create a request url*/
        Request request = new Request.Builder().url(url).build();

        /** STEP 3: gather a response*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "Call failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Response is successful!" + response.toString());
                Log.d(TAG, "Response is successful!" + response.body().toString());
            }
        });

        /**OPTIONAL STEP 3: gather the response*/
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response==null){
            return "Null response";
        }else
            return response.toString();
    }
}
