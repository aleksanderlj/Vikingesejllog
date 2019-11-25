package com.example.vikingesejllog;

import android.content.Context;
import android.os.AsyncTask;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MyWindSpeed {

    // hardcoded apikey
    private String apikey = "0b9eb862d51001fb2a2c48a84892d04e";
    private Context mContext;
    private String windSpeed;



    public MyWindSpeed(Context mContext) {
        this.mContext = mContext;
    }

    public String getWindSpeed(){

        new getWeather().execute();

        return windSpeed;
    }


    class getWeather extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {

            MyGPS location = new MyGPS(mContext);
            String LAT = String.valueOf(location.getLocation().getLatitude());
            String LON = String.valueOf(location.getLocation().getLongitude());

            return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&units=metric&appid=" +apikey);
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject jsonObj = new JSONObject(result);

                JSONObject wind = jsonObj.getJSONObject("wind");

                windSpeed = wind.getString("speed");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
