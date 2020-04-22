package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class SelectedBottleActivity extends AppCompatActivity
{
    TextView tvUser, textview8;

    EditText editTextBottlename;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private RadioGroup radioGroup2;
    private RadioButton radioButton2;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_bottle);

        tvUser = findViewById(R.id.selectedBottle);
        textview8 = findViewById(R.id.textView8);

        editTextBottlename = (EditText) findViewById(R.id.editTextBottlename);


        Intent intent =getIntent();

        String StringID = "0";

        if(intent.getExtras() != null) // ако е взело данните прехвърлени
        {
            Bottles bottlesModel = (Bottles) intent.getSerializableExtra("data");
            pos = (Integer) intent.getSerializableExtra("position"); // позицията

            tvUser.setText("Editing " + bottlesModel.getName());

            int type = bottlesModel.getType(); // типа
            int ml = bottlesModel.getMl(); // милилитрите

            RadioButton radiobutton1 = (RadioButton) findViewById(R.id.radioButton1);
            RadioButton radiobutton2 = (RadioButton) findViewById(R.id.radioButton2);

            // макрираме съответния радио бутон според избора на типа
            if(type == 0)
                radiobutton1.setChecked(true);
            else if(type == 1)
                radiobutton2.setChecked(true);



            // макрираме съответния радио бутон според избора на милилитри
            RadioButton radiobutton3 = (RadioButton) findViewById(R.id.radioButton3);
            RadioButton radiobutton4 = (RadioButton) findViewById(R.id.radioButton4);
            RadioButton radiobutton5 = (RadioButton) findViewById(R.id.radioButton5);
            RadioButton radiobutton6 = (RadioButton) findViewById(R.id.radioButton6);

            if(ml == 750)
                radiobutton3.setChecked(true);
            else if(ml == 375)
                radiobutton4.setChecked(true);
            else if(ml == 200)
                radiobutton5.setChecked(true);
            else if(ml == 187)
                radiobutton6.setChecked(true);




            editTextBottlename.setText(bottlesModel.getName()); // сетва името което е от DB в едит текста

            textview8.setText("Created at: "+bottlesModel.getTime()); // времето

            StringID = ""+bottlesModel.getId();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "PROBLEM!", Toast.LENGTH_SHORT).show();
        }

        // SAVE
        final String finalStringID = StringID;
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottleEdit(finalStringID);
            }
        });

        // BACK
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("position", pos);
                intent.putExtra("exit", 0); // без промяна
                setResult(1, intent); // код 1
                finish(); // финишираме активитито
            }
        });
    }

    private void bottleEdit(final String id) // редакция
    {
        final String name = editTextBottlename.getText().toString();

        // валидация
        if (TextUtils.isEmpty(name))
        {
            editTextBottlename.setError("Please enter name field");
            editTextBottlename.requestFocus();
            return;
        }


        // ТИПА
        String type = "0";
        radioGroup = (RadioGroup) findViewById(R.id.group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        CharSequence text = radioButton.getText();
        if ("Glass".equals(text))
            type = "0";
        else if ("Plastic".equals(text))
            type = "1";


        // МИЛИЛИТРИТЕ
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






        final String finalType = type;
        final String finalMl = ml;

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLE_EDIT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) // ОК
                            {
                                Intent intent = new Intent();
                                // пращаме данните към активитито
                                intent.putExtra("position", pos);
                                intent.putExtra("exit", 1); // със
                                setResult(1, intent); // код 1
                                finish(); // финишираме активитито
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // пращаните параметри
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
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
        Intent intent=new Intent();
        intent.putExtra("position", pos);
        intent.putExtra("exit", 0); // без
        setResult(1, intent); // код 1
        finish(); // финиширане активитито
        super.onBackPressed();
    }
}
