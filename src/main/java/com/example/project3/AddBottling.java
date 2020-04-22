package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddBottling extends AppCompatActivity
{
    Spinner spinner_1;
    Spinner spinner_2;

    List<String> wines_list_name = new ArrayList<String>();
    List<String> wines_list_id = new ArrayList<String>();

    List<String> bottles_list_name = new ArrayList<String>();
    List<String> bottles_list_id = new ArrayList<String>();

    List<String> grapes_list_name = new ArrayList<String>();
    List<String> grapes_list_id = new ArrayList<String>();

    // ползват се при пост заявката
    String last_selected = "";
    String last_selected2 = "";

    // ползват се за името което се създава според избраното
    String name1 = "";
    String name2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bottling);

        // добавяме по една стойност в двата списъка (реално са позиция 0)
        grapes_list_name.add("Select Grape");
        grapes_list_id.add("-1");
        GetAllGrapes(); // взимаме всички сортове грозде

        spinner_1 = (Spinner) findViewById(R.id.spinner);
        spinner_2 = (Spinner) findViewById(R.id.spinner2);


        // при натискане на бутона Save (влиза във функцията за създаване на бутилиране)
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                registerBottling();
            }
        });

        // при натискане на бутона за назад (exit = 0 - без промяна)
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("exit", 0);
                setResult(2, intent); // код 1
                finish(); // финишираме активитито
            }
        });



        // при натискане на спинър2 (dropdown)
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                name2 = parent.getItemAtPosition(position).toString(); // името е избрания елемент
                last_selected2 = bottles_list_id.get(position); // последното избрано е позицията на избрания елемент (от id листата) -> реално тук държим ID-то на избрания елемент
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //status2 = false;
            }
        });
        // добавяме по една стойност в двата списъка (реално са позиция 0)
        bottles_list_name.add("Select Bottle");
        bottles_list_id.add("-1");
        GetAllBottles();// взимаме всички бутилки

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottles_list_name);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter2); // сетваме адаптера




        // същото правим и за спинър1 (dropdown)
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                name1 = parent.getItemAtPosition(position).toString();
                last_selected = wines_list_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //status = false;
            }
        });
        wines_list_name.add("Select Wine");
        wines_list_id.add("-1");
        GetAllWines();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wines_list_name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter);





        // след 1 секунда казваме на двата спинъра да заредят дефаулт позицията от списъка (0) като кажем че има промяна в спинърите (защото добавяме нови данни)
        new CountDownTimer(1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                //textview10.setText("Loading.. " + millisUntilFinished / 1000);
                //textview11.setText("Loading.. " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                spinner_2.setSelection(0);
                spinner_1.setSelection(0);

                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        }.start();
    }


    // ф-я за добавяне на ново бутилиране
    private void registerBottling()
    {
        if(name1.equals("Select Wine")) // ако е избрано от спинъра първия елемент
        {
            Toast.makeText(getApplicationContext(), "Please Choose a Wine!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(name2.equals("Select Bottle")) // ако е избрано от спинъра първия елемент
        {
            Toast.makeText(getApplicationContext(), "Please Choose a Bottle!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ако всичко е ок, съставяме името, което реално ще е името на виното - името на бутилката
        final String name = name1+" - "+name2;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLING_ADD,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) // ако отговора е ОК
                            {
                                // финиш с код 2 + exit=1 (има промяна)
                                Intent intent = new Intent();
                                intent.putExtra("exit", 1);
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
                // параметрите които пращаме
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("wineid", last_selected);
                params.put("bottleid", last_selected2);
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

    private void GetAllWines() // GET заявка за вината
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_WINES,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            // json array object
                            JSONArray array = new JSONArray(response);

                            String grape;
                            int index;

                            // лооп през всички обекти
                            for (int i = 0; i < array.length(); i++)
                            {
                                //взима objectа от json арея
                                JSONObject Wine = array.getJSONObject(i);

                                index = grapes_list_id.indexOf(Wine.getString("grape")); // vzima indexa na tekushtiq row ot grape
                                grape = grapes_list_name.get(index); // getva imeto ot toq index


                                wines_list_name.add(Wine.getString("name") + " (" + grape + ")"); // в листата с вината името е съставено от името + гроздето
                                wines_list_id.add(Wine.getInt("id")+""); // само ид-тата
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


    private void GetAllBottles() // GET заявка
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_BOTTLES,
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
                                JSONObject Bottle = array.getJSONObject(i);

                                //0-white
                                //1-red
                                String bottle_type_name = "";
                                if(Bottle.getInt("type") == 0) bottle_type_name = "Glass";
                                else bottle_type_name = "Plastic";

                                // името на бутилката е създадено от (х мл бутилка - името на бутилкаа)
                                bottles_list_name.add(Bottle.getInt("ml") + "ml " + bottle_type_name + " Bottle (" + Bottle.getString("name") + ")");
                                bottles_list_id.add(Bottle.getInt("id")+""); // само ид-тата
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

    private void GetAllGrapes() // GET заявка
    {
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

                                //0-white
                                //1-red
                                String grape_type_name = "";
                                if(Grape.getInt("type") == 0) grape_type_name = "White";
                                else grape_type_name = "Red";



                                grapes_list_name.add(grape_type_name + " " + Grape.getString("name") + " from " + Grape.getString("producer")); // сорта + име на гроздето + на кого е
                                grapes_list_id.add(Grape.getInt("id")+"");
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
