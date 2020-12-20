package com.example.money_saver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.money_saver.currency_converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data = "";
    String rate;
    Double answer,con1;
    String Url="https://api.exchangeratesapi.io/latest?symbols="+currency_converter.c2+"&base="+currency_converter.c1;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(Url);
            Log.d("CREATION",Url);
            Log.d("STATE",currency_converter.c1+"   "+currency_converter.c2);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject reader = new JSONObject(data);
            JSONObject rates  = reader.getJSONObject("rates");
            rate = rates.getString(currency_converter.c2);
            if(currency_converter.amount != Math.floor(currency_converter.amount))
            {
                answer = (currency_converter.amount)*(Double.parseDouble(rate));
            }
            else {
                answer = (currency_converter.amount)*(Double.parseDouble(rate));;
            }
//            Log.d("STATE","  answer:"+rate);
            rate = Double.toString(answer);
//            Log.d("STATE",rate);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        currency_converter.data.setText(this.rate);
    }
}