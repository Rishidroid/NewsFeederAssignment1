package com.example.dell.newsfeedtest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Button button_submit;
    EditText editText_author, editText_title;
    String title, author;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content    =   (TextView)findViewById( R.id.textView_content );

        editText_title= (EditText) findViewById(R.id.editText_Article);
        editText_author=(EditText) findViewById(R.id.editText_AuthorName);

        Button btnSwitchtoUserView= (Button)findViewById(R.id.buttonSwitchtoUserView);
        button_submit=(Button)findViewById(R.id.button_Submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);


//                try{
//                    // CALL GetText method to make post method call
//                   GetText();
//                }
//                catch(Exception ex)
//                {
//                    content.setText("Error");
//                }
            }
        });

    }
    public  void  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values
        author = editText_author.getText().toString();
        title = editText_title.getText().toString();

        // Create data variable for sent values to server

        String data = URLEncoder.encode("author", "UTF-8")
                + ":" + URLEncoder.encode(author, "UTF-8");

        data += "&" + URLEncoder.encode("published", "UTF-8") + ":"
                + URLEncoder.encode(title, "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {
            // Defined URL  where to send data
            URL url = new URL("http://test.peppersquare.com/api/v1/article");

            // POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            //server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // read server response
            while((line = reader.readLine()) != null)
            {
                // Append response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        // Show response on activity
        content.setText( text  );

    }

}
