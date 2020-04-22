package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddGrape extends AppCompatActivity
{
    EditText editTextGrapename, editTextGrapeproducer, editTextGrapeq;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grape);

        editTextGrapename = (EditText) findViewById(R.id.editTextGrapename);
        editTextGrapeproducer = (EditText) findViewById(R.id.editTextGrapeproducer);
        editTextGrapeq = (EditText) findViewById(R.id.editTextGrapeq);


        // бутона за сейф - регистрира нов сорт грозде
        findViewById(R.id.button).setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                registerGrape();
            }
        });

        // бутона за назад - код 2 + няма промяна
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("exit", 0);
                setResult(2, intent); // код 2
                finish(); // финишираме активитито
            }
        });
    }

    private void registerGrape() // регисриране на грозде
    {
        // взимаме стойностите от написаното
        final String name = editTextGrapename.getText().toString();
        final String producer = editTextGrapeproducer.getText().toString();
        final String q = editTextGrapeq.getText().toString();

        // валидираме стойностите
        if (TextUtils.isEmpty(name))
        {
            editTextGrapename.setError("Please enter name field");
            editTextGrapename.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(q))
        {
            editTextGrapeq.setError("Please enter q field");
            editTextGrapeq.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(producer))
        {
            editTextGrapeproducer.setError("Please enter producer field");
            editTextGrapeproducer.requestFocus();
            return;
        }

        // парсване на радио бутоните за типа
        String type = "0";
        radioGroup = (RadioGroup) findViewById(R.id.group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        CharSequence text = radioButton.getText();
        if ("White".equals(text))
            type = "0";
        else if ("Red".equals(text))
            type = "1";


        final String finalType = type;

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GRAPE_ADD,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) // ОК е
                            {
                                Intent intent = new Intent();
                                intent.putExtra("exit", 1); // има промяна
                                setResult(2, intent); // код 2
                                finish(); // финишираме активитито
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "PROBLEM: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // параметрите които пращаме
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("type", finalType);
                params.put("producer", producer);
                params.put("q", q);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() // при натискането на бутона за назад от андроид
    {
        Intent intent=new Intent();
        intent.putExtra("exit", 0); // без промяна
        setResult(2,intent); // код 2
        finish(); // финиширане активитито
        super.onBackPressed();
    }

}
