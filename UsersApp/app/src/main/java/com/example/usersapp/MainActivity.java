package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv_name,tv_age,tv_gender,tv_email;
    ImageView iv_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_image=findViewById(R.id.iv_image);
        tv_name =findViewById(R.id.tv_name);
        tv_email =findViewById(R.id.tv_email);
        tv_age =findViewById(R.id.tv_age);
        tv_gender =findViewById(R.id.tv_gender);


        findViewById(R.id.generate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask nt=new NetworkTask();
                nt.execute();
            }

        });
    }
    class NetworkTask extends AsyncTask   {
        public List<User> userList=new ArrayList<>();
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url=new URL("https://randomuser.me/api/");
                InputStream is=url.openStream();
                byte [] buffer=new byte[4096];
                StringBuilder sb=new StringBuilder("");

                while(is.read(buffer)!=-1){
                    sb.append(new String(buffer));
                }

                Log.i("apiresponse",sb.toString());


                User u=new User();

                JSONObject obj=new JSONObject(sb.toString());
                JSONArray results=obj.getJSONArray("results");
                JSONObject user=results.getJSONObject(0);
                JSONObject nameObj=user.getJSONObject("name");
                u._name=nameObj.getString("title")+". "+nameObj.getString("first")+" "+nameObj.getString("last");
                 u._email=user.getString("email");
                JSONObject ageObj=user.getJSONObject("dob");
                u._age=ageObj.getString("age");
                u._gender=user.getString("gender");
                u._imageUrl=user.getJSONObject("picture").getString("medium");


                publishProgress(u._imageUrl,0);
                publishProgress( u._name,1);
                publishProgress(u._email,2);
                publishProgress(u._age,3);
                publishProgress(u._gender,4);

                userList.add(u);




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
        protected void onProgressUpdate(Object[] values) {
            Toast.makeText(MainActivity.this, values[0].toString(), Toast.LENGTH_SHORT).show();
            switch (Integer.parseInt(values[1]+"")){
                case 0:
                    Picasso.get().load(values[0].toString()).into(iv_image);
                    break;
                case 1:
                    tv_name.setText(values[0].toString());
                    break;
                case 2:
                    tv_email.setText(values[0].toString());
                    break;
                case 3:
                    tv_age.setText(values[0].toString());
                    break;
                case 4:
                    tv_gender.setText(values[0].toString());
                    break;

            }
        }


    }
}