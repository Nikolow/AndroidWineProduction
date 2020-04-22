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

public class SelectedWineActivity extends AppCompatActivity
{
    Spinner spinner_1;

    TextView tvUser, textview9;

    EditText editTextWinename, editTextWineq;

    int pos = 0;

    String name1 = "";
    String last_selected = "";
    int type = 0;
    int grapes_id_index = 0;
    List<String> grapes_list_name = new ArrayList<String>();
    List<String> grapes_list_id = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_wine);

        tvUser = findViewById(R.id.selectedWine);
        textview9 = findViewById(R.id.textView9);

        editTextWinename = (EditText) findViewById(R.id.editTextWinename);
        editTextWineq = (EditText) findViewById(R.id.editTextWineq);


        Intent intent =getIntent();

        String StringID = "0";

        if(intent.getExtras() != null) // ОК
        {
            Wine WinesModel = (Wine) intent.getSerializableExtra("data");
            pos = (Integer) intent.getSerializableExtra("position");

            tvUser.setText("Editing " + WinesModel.getName());
            textview9.setText("Created at: "+WinesModel.getTime());

            editTextWinename.setText(WinesModel.getName());

            type = WinesModel.getGrape(); // INT
            last_selected = "" + WinesModel.getGrape(); // STRING


            String q = "" + WinesModel.getQ();
            editTextWineq.setText(q);


            StringID = ""+WinesModel.getId();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "PROBLEM!", Toast.LENGTH_SHORT).show();
        }



        // спинър - дропдаун
        spinner_1 = (Spinner) findViewById(R.id.spinner);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) // клик на итем
            {
                name1 = parent.getItemAtPosition(position).toString(); // име1 = избрания елемент
                last_selected = grapes_list_id.get(position); // ид-то
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //status = false;
            }
        });

        GetAllGrapes(); // всички сортове
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grapes_list_name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter);



        final String finalStringID = StringID;
        // сейф
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                WineEdit(finalStringID);
            }
        });

        // бек
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("position", pos);
                intent.putExtra("exit", 0); // няма
                setResult(1, intent); // код 1
                finish(); // финишираме активитито
            }
        });

        // след 1 сек
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished)
            {
                //textview10.setText("Loading.. " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                // избраното + има промяна
                grapes_id_index = grapes_list_id.indexOf(last_selected);
                spinner_1.setSelection(grapes_id_index);
                adapter.notifyDataSetChanged();
            }
        }.start();

    }


    private void WineEdit(final String id) // редакция
    {
        // взимаме
        final String name = editTextWinename.getText().toString();
        final String q = editTextWineq.getText().toString();

        // валидираме
        if (TextUtils.isEmpty(name))
        {
            editTextWinename.setError("Please enter name field");
            editTextWinename.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(q))
        {
            editTextWineq.setError("Please enter q field");
            editTextWineq.requestFocus();
            return;
        }

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_WINE_EDIT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error"))
                            {
                                Intent intent = new Intent();
                                // пращаме данните към активитито
                                intent.putExtra("position", pos);
                                intent.putExtra("exit", 1); // има
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
        intent.putExtra("position", pos);
        setResult(1, intent); // код 1
        intent.putExtra("exit", 0); // няма
        finish(); // финиширане активитито
        super.onBackPressed();
    }

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
