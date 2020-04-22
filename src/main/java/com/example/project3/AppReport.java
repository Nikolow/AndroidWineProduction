package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppReport extends AppCompatActivity
{
    DatePickerDialog picker;
    EditText eText, eText2;
    Button btnGet;
    TextView tvw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_report);



        /*
           при натискането за писане на Start и End Date
           стартираме DatePicker с което по-лесно се избира датата
           преобразувамия в нужния вид, понеже в DB е по спецефичен начин настроена
           за да можем после да направим сравнение и SQL заявката да излезе правилна
        */

        tvw = (TextView) findViewById(R.id.textView1);
        eText = (EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AppReport.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String Month = "";
                                String Day = "";
                                if ((monthOfYear + 1) < 10)
                                    Month = "0" + (monthOfYear + 1);
                                else
                                    Month = "" + (monthOfYear + 1);

                                if (dayOfMonth < 10)
                                    Day = "0" + (dayOfMonth);
                                else
                                    Day = "" + (dayOfMonth);


                                eText.setText(year + "-" + Month + "-" + Day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        eText2 = (EditText) findViewById(R.id.editText2);
        eText2.setInputType(InputType.TYPE_NULL);
        eText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AppReport.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String Month = "";
                                String Day = "";
                                if ((monthOfYear + 1) < 10)
                                    Month = "0" + (monthOfYear + 1);
                                else
                                    Month = "" + (monthOfYear + 1);

                                if (dayOfMonth < 10)
                                    Day = "0" + (dayOfMonth);
                                else
                                    Day = "" + (dayOfMonth);


                                eText2.setText(year + "-" + Month + "-" + Day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        // бутона за показване на репорта
        btnGet = (Button) findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                All(eText.getText().toString(), eText2.getText().toString()); // даваме 2 параметъра които реално са ни старт и енд датите
            }
        });
    }

    private void All(final String time1, final String time2)  // ф-ята за изкарване
    {
        // правим нужните проверки
        if (time1.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Start Date is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time2.equals(""))
        {
            Toast.makeText(getApplicationContext(), "End Date is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // правим проверки, да не стане някой SQL Injection - валидация да е правилна ДАТА
        if (time1.matches("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$") == false)
        {
            Toast.makeText(getApplicationContext(), "Start Date is not Correct!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time2.matches("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$") == false)
        {
            Toast.makeText(getApplicationContext(), "End Date is not Correct!", Toast.LENGTH_SHORT).show();
            return;
        }


        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_INFO_COUNT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            //Toast.makeText(getApplicationContext(), "API Response: "+response, Toast.LENGTH_SHORT).show();

                            if (!obj.getBoolean("error"))  // ОК е
                            {
                                String result = ""; // тук ще добавяме всички данни 1 по 1

                                // обекта за сортовете грозде
                                JSONObject grapes = new JSONObject(obj.getString("grapes"));

                                result += "Added Grapes Between " + time1 + " And " + time2 + ":\n";
                                result += "All Types: " + grapes.getString("all") + "\n";
                                result += "White: " + grapes.getString("type0") + "\n";
                                result += "Red: " + grapes.getString("type1") + "\n\n\n";


                                // обекта за бутилките
                                JSONObject bottles = new JSONObject(obj.getString("bottles"));

                                result += "Added Bottles Between " + time1 + " And " + time2 + ":\n";
                                result += "All Types: " + bottles.getString("all") + "\n";
                                result += "Glass: " + bottles.getString("type0") + "\n";
                                result += "Plastic: " + bottles.getString("type1") + "\n\n";

                                result += "Glass - 750 ml: " + bottles.getString("0_750") + "\n";
                                result += "Glass - 375 ml: " + bottles.getString("0_375") + "\n";
                                result += "Glass - 200 ml: " + bottles.getString("0_200") + "\n";
                                result += "Glass - 187 ml: " + bottles.getString("0_187") + "\n\n";

                                result += "Plastic - 750 ml: " + bottles.getString("1_750") + "\n";
                                result += "Plastic - 375 ml: " + bottles.getString("1_375") + "\n";
                                result += "Plastic - 200 ml: " + bottles.getString("1_200") + "\n";
                                result += "Plastic - 187 ml: " + bottles.getString("1_187") + "\n\n\n";


                                // нямаме нов обект за бутилирането, щото не сме го направили както предните 2
                                result += "Made Bottlings Between " + time1 + " And " + time2 + ":\n";
                                result += "All: " + obj.getString("bottling");

                                tvw.setText(result); // слагаме резултата в текст вюто
                            }
                            else
                                {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e)
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // параметрите които пращаме
                Map<String, String> params = new HashMap<>();
                params.put("time1", time1);
                params.put("time2", time2);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
