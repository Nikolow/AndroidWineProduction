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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectedBottlingActivity extends AppCompatActivity
{
    Spinner spinner_1;
    Spinner spinner_2;

    TextView tvUser, textview9;

    // 3 списъка - вино, бутилки, грозде
    // 2 за избраните вино и бутилка
    // 2 типа
    // 2 имена

    int pos = 0;
    String last_selected = "";
    String last_selected2 = "";

    int type2 = 0;
    int type3 = 0;


    List<String> wines_list_name = new ArrayList<String>();
    List<String> wines_list_id = new ArrayList<String>();
    int wines_id_index = -1;

    List<String> bottles_list_name = new ArrayList<String>();
    List<String> bottles_list_id = new ArrayList<String>();
    int bottles_id_index = -1;

    List<String> grapes_list_name = new ArrayList<String>();
    List<String> grapes_list_id = new ArrayList<String>();

    String name1 = "";
    String name2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_bottling);

        tvUser = findViewById(R.id.selectedBotteling);
        textview9 = findViewById(R.id.textView9);

        GetAllGrapes(); // всички сортове грозде


        Intent intent =getIntent();

        String StringID = "0";

        if(intent.getExtras() != null) // ОК е парсването
        {
            BottlingModel Bottlingmodel = (BottlingModel) intent.getSerializableExtra("data");
            pos = (Integer) intent.getSerializableExtra("position");

            tvUser.setText("Editing " + Bottlingmodel.getName());
            textview9.setText("Created at: "+Bottlingmodel.getTime());

            type2 = Bottlingmodel.getWineid(); // INT
            last_selected = "" + Bottlingmodel.getWineid(); // STRING


            type3 = Bottlingmodel.getBottleid(); // INT
            last_selected2 = "" + Bottlingmodel.getBottleid(); // STRING

            StringID = ""+Bottlingmodel.getId();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "PROBLEM!", Toast.LENGTH_SHORT).show();
        }









        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottles_list_name);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wines_list_name);






        // SAVE
        final String finalStringID = StringID;
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BottlingEdit(finalStringID);
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
                intent.putExtra("exit", 0); // НЕ
                setResult(1, intent); // код 1
                finish(); // финишираме активитито
            }
        });

        // 0,5 сек - двата спинъра
        new CountDownTimer(500, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                //textview10.setText("Loading.. " + millisUntilFinished / 1000);
                //textview11.setText("Loading.. " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                spinner_2 = (Spinner) findViewById(R.id.spinner);
                spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
                    {
                        name2 = parent.getItemAtPosition(position).toString();
                        last_selected2 = bottles_list_id.get(position);
                        //Toast.makeText(getApplicationContext(), "Selected: "+last_selected2, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView)
                    {
                        //status = false;
                    }
                });
                GetAllBottles();
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_2.setAdapter(adapter2);



                spinner_1 = (Spinner) findViewById(R.id.spinner2);
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
                GetAllWines();
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_1.setAdapter(adapter);
            }
        }.start();



        // 1,0 сек (тоест +0,5 от миналото) - за инфото което е селектнато
        new CountDownTimer(1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                //textview10.setText("Loading.. " + millisUntilFinished / 1000);
                //textview11.setText("Loading.. " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                bottles_id_index = bottles_list_id.indexOf(last_selected2);
                spinner_2.setSelection(bottles_id_index);
                adapter2.notifyDataSetChanged();

                wines_id_index = wines_list_id.indexOf(last_selected);
                spinner_1.setSelection(wines_id_index);
                adapter.notifyDataSetChanged();

            }
        }.start();
    }


    private void BottlingEdit(final String id) // ф-я за едит
    {
        final String name = name1+" - "+name2; // новото име

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLING_EDIT,
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
                                intent.putExtra("exit", 1); // има проманя
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
        intent.putExtra("position", pos);
        intent.putExtra("exit", 0); // без
        setResult(1, intent); // код 1
        finish(); // финиширане активитито
        super.onBackPressed();
    }

    private void GetAllWines() // всички вина - GET
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_WINES,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray array = new JSONArray(response);

                            String grape;
                            int index;

                            for (int i = 0; i < array.length(); i++)
                            {
                                JSONObject Wine = array.getJSONObject(i);

                                index = grapes_list_id.indexOf(Wine.getString("grape")); // vzima indexa na tekushtiq row ot grape
                                grape = grapes_list_name.get(index); // getva imeto ot toq index


                                wines_list_name.add(Wine.getString("name") + " (" + grape + ")");
                                wines_list_id.add(Wine.getInt("id")+"");

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

    private void GetAllBottles() // всички бутилки
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

                                //bottles_list_name.add(Bottle.getString("name") + " (" + Bottle.getInt("ml")  + " ml - " + bottle_type_name + ")");
                                bottles_list_name.add(Bottle.getInt("ml") + "ml " + bottle_type_name + " Bottle (" + Bottle.getString("name") + ")");
                                bottles_list_id.add(Bottle.getInt("id")+"");
                            }
                        }
                        catch (JSONException e)
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


    private void GetAllGrapes() // всички сортове грозде
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


                                //grapes_list_name.add(Grape.getString("name") + " - " + grape_type_name + " - " + Grape.getString("producer"));
                                grapes_list_name.add(grape_type_name + " " + Grape.getString("name") + " from " + Grape.getString("producer"));
                                grapes_list_id.add(Grape.getInt("id")+"");
                            }
                        }
                        catch (JSONException e)
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
