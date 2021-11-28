package com.lopatinm.intercity.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lopatinm.intercity.MainActivity;
import com.lopatinm.intercity.R;
import com.lopatinm.intercity.ui.registration.UserFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetorderFragment extends Fragment {

    private static final String APP_PREFERENCES = "IntercitySettings";
    String access_token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_getorder, container, false);
        SharedPreferences sp = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        access_token = sp.getString("access_token", "");



        Button createOrderButton = (Button) root.findViewById(R.id.button2);
        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText address1   = (EditText) root.findViewById(R.id.editTextTextPersonName2);
                EditText address2   = (EditText) root.findViewById(R.id.editTextTextPersonName);
                EditText date   = (EditText) root.findViewById(R.id.editTextDate);
                EditText time   = (EditText) root.findViewById(R.id.editTextTime);
                EditText auto   = (EditText) root.findViewById(R.id.editTextTextPersonName3);
                EditText pay   = (EditText) root.findViewById(R.id.editTextTextPersonName4);


                JSONObject order = new JSONObject();
                try {
                    order.put("date", date.getText());
                    order.put("organization_id", "1");
                    order.put("driver_id", "1");
                    order.put("route_id", "1");
                    order.put("address_start", address1.getText());
                    order.put("address_end", address2.getText());
                    order.put("coord_start", "1");
                    order.put("coord_end", "1");
                    order.put("status_id", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AsyncCreateOrder().execute("http://192.168.1.14/v1/order", order.toString());
            }
        });

        return root;
    }

    public class AsyncCreateOrder extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String data = params[1];
            OutputStream out = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                out.close();

                urlConnection.connect();
                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return urlString;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}