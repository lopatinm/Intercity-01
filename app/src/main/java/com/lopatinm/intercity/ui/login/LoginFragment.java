package com.lopatinm.intercity.ui.login;

import static android.content.Context.MODE_PRIVATE;

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

public class LoginFragment extends Fragment {

    public static final String APP_PREFERENCES = "IntercitySettings";
    String role;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        Button login = (Button) root.findViewById(R.id.button3);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText password   = (EditText) root.findViewById(R.id.editTextTextPassword);
                EditText phone   = (EditText) root.findViewById(R.id.editTextPhone2);
                JSONObject user = new JSONObject();

                try {
                    user.put("phone", phone.getText().toString());
                    user.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new AsyncLogin().execute("http://192.168.1.14/v1/user/login", user.toString());
            }
        });

        return root;
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
                    role = profile.getJSONObject("data").getString("role");
                    editor.putString("role", role);
                    editor.putString("access_token", profile.getJSONObject("data").getString("access_token"));
                    editor.apply();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((MainActivity)getActivity()).roleCheck();
                            if(role.equals("user")){
                                ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_order);
                            }else{
                                ((MainActivity)getActivity()).fragmentReplacer(R.id.nav_profile_dispatcherprofile);
                            }
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