package com.lopatinm.intercity.ui.registration;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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

public class UserFragment extends Fragment {

    public static final String APP_PREFERENCES = "IntercitySettings";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        Button register = (Button) root.findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name   = (EditText) root.findViewById(R.id.editTextTextPersonName5);
                EditText phone   = (EditText) root.findViewById(R.id.editTextPhone);
                JSONObject data = new JSONObject();

                try {
                    data.put("name", name.getText().toString());
                    data.put("phone", phone.getText().toString());
                    data.put("role", "user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AsyncRegister().execute("http://192.168.1.14/v1/user/registration", data.toString());
            }
        });


        return root;
    }



    public class AsyncRegister extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String data = params[1];
            OutputStream out = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
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
                    JSONObject user = new JSONObject(sb.toString());

                    try {
                        user.put("phone", user.getString("phone"));
                        user.put("password", user.getString("password"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new AsyncLogin().execute("http://192.168.1.14/v1/user/login", user.toString());
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


    public class AsyncLogin extends AsyncTask<String, String, String> {


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

                    JSONObject profile = new JSONObject(sb.toString());

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone", profile.getJSONObject("data").getString("phone"));
                    editor.putString("role", profile.getJSONObject("data").getString("role"));
                    editor.putString("access_token", profile.getJSONObject("data").getString("access_token"));
                    editor.apply();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((MainActivity)getActivity()).roleCheck();
                            ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_order);
                        }
                    });

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