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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AddBottle extends AppCompatActivity
{
    EditText editTextBottlename;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private RadioGroup radioGroup2;
    private RadioButton radioButton2;

    Logger log = LoggerFactory.getLogger(AddBottle.class);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bottle);


        editTextBottlename = (EditText) findViewById(R.id.editTextBottlename);




        // при натискане на бутона с id button
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                registerBottle(); // влизаме във функцията за създаване на нова бутилка
            }
        });

        // при натискане на бутона с id button2
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // финишваме активитито с код 2 и изпращаме "exit" със стойност 0 (0 = без промени)
                Intent intent = new Intent();
                intent.putExtra("exit", 0);
                setResult(2, intent); // код 1
                finish(); // финишираме активитито
            }
        });
    }

    // функцията за създаване на нова бутилка
    private void registerBottle()
    {
        final String name = editTextBottlename.getText().toString(); // взимаме стойността на текста записан в полето за име

        // валидация за името - дали е празно
        if (TextUtils.isEmpty(name))
        {
            editTextBottlename.setError("Please enter name field");
            editTextBottlename.requestFocus();
            log.info("N.Log : Add Bottle : Empty Name Field!");
            return;
        }

        // парсване на радио бутоните (кое е избраното) - за тип
        String type = "0";
        radioGroup = (RadioGroup) findViewById(R.id.group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        CharSequence text = radioButton.getText();
        if ("Glass".equals(text))
            type = "0";
        else if ("Plastic".equals(text))
            type = "1";


        // парсване на радио бутоните (кое е избраното) - за милилитрите
        String ml = "1337";
        radioGroup2 = (RadioGroup) findViewById(R.id.group2);
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();
        radioButton2 = (RadioButton) findViewById(selectedId2);

        CharSequence text2 = radioButton2.getText();
        if ("750".equals(text2))
            ml = "750";
        else if ("375".equals(text2))
            ml = "375";
        else if ("200".equals(text2))
            ml = "200";
        else if ("187".equals(text2))
            ml = "187";


        // финалните стойности (ползват се за при пост заявката)
        final String finalType = type;
        final String finalMl = ml;


        log.info("N.Log : Add Bottle : Creating Request - POST");
        // пост заявка към URL за добавяне на нова бутилка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLE_ADD,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) // ако отговорът който е върнал НЕ Е грешка
                            {
                                log.info("N.Log : Add Bottle : Send Request without error!");
                                // финишваме активитито с код 2 и стойност на exit 1 (има промяна)
                                Intent intent = new Intent();
                                intent.putExtra("exit", 1);
                                setResult(2, intent); // код 2
                                finish(); // финишираме активитито
                            }
                            else // значи имаме проблем - изписваме какъв е !
                            {
                                log.info("N.Log : Add Bottle : Send Request - PROBLEM FOUND - "+obj.getString("message"));
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
                        log.info("N.Log : Add Bottle : Error Response - "+ error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // параметрите които пращаме
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("type", finalType);
                params.put("ml", finalMl);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() // при натискането на бутона за назад от андроид
    {
        // финишираме актвитито с код 2 и exit със стойност 0 (без промяна)
        Intent intent=new Intent();
        intent.putExtra("exit", 0);
        setResult(2,intent); // код 2
        finish(); // финиширане активитито
        super.onBackPressed();
    }
}
