package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewProfile extends AppCompatActivity
{
    TextView textViewUsername, textViewEmail, textViewAccess;

    int LOW = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewAccess = (TextView) findViewById(R.id.textViewAccess);


        //getting the current user
        final UserModel user = SharedPrefManager.getInstance(this).getUser();




        if(String.valueOf(user.getAccess()).equals("3")) // АКО Е АДМИН
        {
            All(); // събиране на инфо + нотификейшърн
            textViewAccess.setText("Administrator");
        }
        else if(String.valueOf(user.getAccess()).equals("2"))
            textViewAccess.setText("Operator");
        else if(String.valueOf(user.getAccess()).equals("1"))
            textViewAccess.setText("User");
        else
            textViewAccess.setText("Profile Access Problem!");



        textViewUsername.setText(user.getUserName()); // име
        textViewEmail.setText(user.getEmail()); // мейл



        // бутона за логаут
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish(); // финиш текущото
                SharedPrefManager.getInstance(getApplicationContext()).logout(); // логаут шаред преференция
                startActivity(new Intent(getApplicationContext(), Login.class)); // връщане в главното активити за логин
            }
        });


        // NEXT бутона
        findViewById(R.id.buttonGoToActions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                switch(user.getAccess()) // типа на потребителя
                {
                    case 3: // АДМИН
                    {
                        startActivity(new Intent(getApplicationContext(), AdministratorMain.class)); // administrator activity
                        break;
                    }
                    case 2: // ОПЕРАТОР
                    {
                        startActivity(new Intent(getApplicationContext(), OperatorMain.class)); // administrator activity
                        break;
                    }
                    case 1: // ЮЗЪР
                    {
                        startActivity(new Intent(getApplicationContext(), UserMain.class)); // administrator activity
                        break;
                    }
                    default: // НЕЩО СЕ Е ОБЪРКАЛО :(
                    {
                        Toast.makeText(getApplicationContext(), "Error: Something went wrong!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }

    private void All() // събиране на инфо + нотификейшърн
    {
        // GET заявка
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_INFO_NOTIFICATION,
                new Response.Listener<String>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            int white, red, glass, plastic;

                            JSONObject obj = new JSONObject(response);

                            JSONObject grapes = new JSONObject(obj.getString("grapes")); // за гроздето

                            white = grapes.getInt("type0");
                            red = grapes.getInt("type1");


                            JSONObject bottles = new JSONObject(obj.getString("bottles")); // за бутилката

                            glass = bottles.getInt("type0");
                            plastic = bottles.getInt("type1");


                            show_Notification(white, red, glass, plastic); // нотификейшън с параметри
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void show_Notification(int white, int red, int glass, int plastic) // самия нотификейшън
    {
        boolean low = false;

        Intent intent=new Intent(getApplicationContext(),AppReport.class);
        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel;
        notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);

        String result = ""; // резултата който ще го има на нотификешйъна
        if(white < LOW)
        {
            low = true;
            result = "White Grapes ("+white+")\n";
        }

        if(red < LOW)
        {
            low = true;
            result += "Red Grapes (" + red + ")\n";
        }

        if(glass < LOW)
        {
            low = true;
            result += "Glass Bottles (" + glass + ")\n";
        }

        if(plastic < LOW)
        {
            low = true;
            result += "Platic Bottles (" + plastic + ")";
        }

        if(low) // ако има изобщо LOW
        {
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText(result)
                    .setContentTitle("There have LOW quantity..")
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.ic_dialog_dialer, "View Report", pendingIntent) // при натискане ще отиде в Report активитито
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(1, notification);
        }
    }
}