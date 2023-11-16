package com.example.asyntask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayCourse;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        arrayCourse = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayCourse);
        listView.setAdapter(adapter);

        new Readjson().execute("https://vnexpress.net/rss/giai-tri.rss");
    }

        private class Readjson extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strings) {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL(strings[0]);
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }

                    bufferedReader.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return content.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i =0; i < array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        String ten = object.getString("ten");
                        String gia = object.getString("gia");
                        arrayCourse.add(ten + "-" + gia);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

    }
}