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

public class SelectedGrapeActivity extends AppCompatActivity
{
    TextView tvUser, textview9;

    EditText editTextGrapename, editTextGrapeproducer, editTextGrapeq;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_grape);

        tvUser = findViewById(R.id.selectedGrape);
        textview9 = findViewById(R.id.textView9);

        editTextGrapename = (EditText) findViewById(R.id.editTextGrapename);
        editTextGrapeproducer = (EditText) findViewById(R.id.editTextGrapeproducer);
        editTextGrapeq = (EditText) findViewById(R.id.editTextGrapeq);

        Intent intent =getIntent();

        String StringID = "0";

        if(intent.getExtras() != null) // ОК
        {
            Grape GrapesModel = (Grape) intent.getSerializableExtra("data");
            pos = (Integer) intent.getSerializableExtra("position");

            tvUser.setText("Editing " + GrapesModel.getName());
            textview9.setText("Created at: "+GrapesModel.getTime());

            int type = GrapesModel.getType();
            String q = "" + GrapesModel.getQ();

            // за радио бутоните за типа
            RadioButton radiobutton1 = (RadioButton) findViewById(R.id.radioButton1);
            RadioButton radiobutton2 = (RadioButton) findViewById(R.id.radioButton2);

            if(type == 0)
                radiobutton1.setChecked(true);
            else if(type == 1)
                radiobutton2.setChecked(true);

            editTextGrapename.setText(GrapesModel.getName());
            editTextGrapeproducer.setText(GrapesModel.getProducer());
            editTextGrapeq.setText(q);

            StringID = ""+GrapesModel.getId();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "PROBLEM!", Toast.LENGTH_SHORT).show();
        }

        // бутон SAVE
        final String finalStringID = StringID;
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GrapeEdit(finalStringID);
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
                intent.putExtra("exit", 0); // БЕЗ
                setResult(1, intent); // код 1
                finish(); // финишираме активитито
            }
        });
    }

    private void GrapeEdit(final String id) // редакция
    {
        // взимаме
        final String name = editTextGrapename.getText().toString();
        final String producer = editTextGrapeproducer.getText().toString();
        final String q = editTextGrapeq.getText().toString();

        // валидираме
        if (TextUtils.isEmpty(name))
        {
            editTextGrapename.setError("Please enter name field");
            editTextGrapename.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(producer))
        {
            editTextGrapeproducer.setError("Please enter producer field");
            editTextGrapeproducer.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(q))
        {
            editTextGrapeq.setError("Please enter q field");
            editTextGrapeq.requestFocus();
            return;
        }


        // избора от радио бутоните - за типа
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GRAPE_EDIT,
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
                                intent.putExtra("exit", 1); // ИМА промяна
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
                // параметрите които подаваме
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
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
        intent.putExtra("position", pos);
        intent.putExtra("exit", 0); // БЕЗ
        setResult(1, intent); // код 1
        finish(); // финиширане активитито
        super.onBackPressed();
    }



}
