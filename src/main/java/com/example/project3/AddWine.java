package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddWine extends AppCompatActivity
{
    EditText editTextWinename, editTextWineq;
    Spinner spinner_1;

    String name1 = "";
    String last_selected = "";
    List<String> grapes_list_name = new ArrayList<String>();
    List<String> grapes_list_id = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wine);

        editTextWinename = (EditText) findViewById(R.id.editTextWinename);
        editTextWineq = (EditText) findViewById(R.id.editTextWineq);


        // dropdown (спинър)
        spinner_1 = (Spinner) findViewById(R.id.spinner);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) // при натискане на елемент
            {
                name1 = parent.getItemAtPosition(position).toString(); // името = това което сме селектнали
                last_selected = grapes_list_id.get(position); // ид-то запазваме тук
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //status = false;
            }
        });

        // по една стойност добавяме (позиция 0)
        grapes_list_name.add("Select Grape");
        grapes_list_id.add("-1");
        GetAllGrapes(); // взимаме всички сортове грозде

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grapes_list_name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter); // сетваме адаптера


        // след 1 секунда
        new CountDownTimer(1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                //textview10.setText("Loading.. " + millisUntilFinished / 1000);
                //textview11.setText("Loading.. " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                // избираме позиция 0 + казваме че има промяна и да я отрази
                spinner_1.setSelection(0);
                adapter.notifyDataSetChanged();
            }
        }.start();



        // при сейв - създава ново вино
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                registerWine();
            }
        });

        // бутон за назад
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("exit", 0); // няма промяна
                setResult(2, intent); // код 2
                finish(); // финишираме активитито
            }
        });
    }

    private void registerWine() // регистрира ново вино
    {
        // взима написаното
        final String name = editTextWinename.getText().toString();
        final String q = editTextWineq.getText().toString();

        // валидираме го
        if (TextUtils.isEmpty(name))
        {
            editTextWinename.setError("Please enter name field");
            editTextWinename.requestFocus();
            return;
        }

        if(name1.equals("Select Grape")) // ако сме избрали първата позиция (0) = проблемче
        {
            Toast.makeText(getApplicationContext(), "Please Choose a Grape!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(q))
        {
            editTextWineq.setError("Please enter q field");
            editTextWineq.requestFocus();
            return;
        }

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_WINE_ADD,
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // пращаните параметри
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("grape", last_selected);
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
        intent.putExtra("exit", 0); // няма промяна
        setResult(2,intent); // код 2
        finish(); // финиширане активитито
        super.onBackPressed();
    }

    // взимаме всички сортове грозде
    private void GetAllGrapes()
    {
        // GET заявка
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GRAPES,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++)
                            {
                                JSONObject Grape = array.getJSONObject(i);

                                String grape_type_name = "";
                                if(Grape.getInt("type") == 0) grape_type_name = "White";
                                else grape_type_name = "Red";

                                grapes_list_name.add(grape_type_name + " " + Grape.getString("name") + " from " + Grape.getString("producer")); // тип + име + от кого е
                                grapes_list_id.add(Grape.getInt("id")+""); // само ид-та
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        //adapter.notifyDataSetChanged();
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
}
