package com.example.dell.newsfeedtest2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button buttonShowNews = (Button) findViewById(R.id.btnShowNews);
        buttonShowNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String restURL = "http://test.peppersquare.com/api/v1/article";
                new RestOperation().execute(restURL);
            }
        });
    }

    private class RestOperation extends AsyncTask<String, Void, Void> {

        final HttpClient httpClient = new DefaultHttpClient();
        String content, error;
        String data = "";

        ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);

        TextView textViewTitle = (TextView) findViewById(R.id.textViewUserTitle);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewUserDescription);

        EditText editTextAuthor = (EditText) findViewById(R.id.editTextUserAuthorName);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait...");
            progressDialog.show();

            try {
                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + editTextAuthor.getText();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            BufferedReader br = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(data);
                outputStreamWriter.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                }
            } catch (MalformedURLException e) {
                error = e.getMessage();
            } catch (IOException e) {
                error = e.getMessage();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (error != null) {
                textViewTitle.setText("Error" + error);
            } else {
                textViewTitle.setText(content);

                JSONObject jsonResponse;

                try {
                    String outputTitle = null;
                    String outputdescription = null;
                    jsonResponse = new JSONObject(content);
                    JSONArray jsonArray = jsonResponse.optJSONArray("Rishi");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject child = jsonArray.getJSONObject(i);

                        String title = child.getString("title");
                        String description = child.getString("description");

                        outputTitle = "Title =" + title + System.getProperty("line.separator");
                        outputdescription = "Description =" + description + System.getProperty("line.separator");
                    }

                    textViewTitle.setText(outputTitle);
                    textViewDescription.setText(outputdescription);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
