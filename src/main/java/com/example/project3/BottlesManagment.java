package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottlesManagment extends AppCompatActivity implements BottlesAdapter.SelectedBottle
{
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Bottles> bottlesModelList = new ArrayList<>();
    BottlesAdapter bottlesAdapter;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottles_managment);

        // стартираме активити което чака резултат с код 2 (при натискане на бутона за добавяне)
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(BottlesManagment.this, AddBottle.class),2);
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);

        linearLayout = findViewById(R.id.LinearLayout);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        bottlesAdapter = new BottlesAdapter(bottlesModelList,this);
        recyclerView.setAdapter(bottlesAdapter);

        All(); // взима всичките и ги попълва

        //final UserModel user = SharedPrefManager.getInstance(this).getUser();
        //if(String.valueOf(user.getAccess()).equals("3") || String.valueOf(user.getAccess()).equals("1"))
        enableSwipeToDeleteAndUndo(); // да може да се swipe-ва за delete
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // функцията, която приема резултата
    {
        super.onActivityResult(requestCode, resultCode, data);

        int POS2 = 0;
        int POS = data.getIntExtra("position", POS2);

        int EXIT2 = 0;
        int EXIT = data.getIntExtra("exit", EXIT2);

        if(requestCode==1) // ако кода е 1
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "Bottle on position " + (POS + 1) + " has been Updated!", Toast.LENGTH_SHORT).show();
                this.recreate(); // да влезе в create отново (щото имаме промяна) = ще ребилдне всичко с новите данни
            }
        }
        else if(requestCode==2) // ако кода е 2
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "The new bottle has been added successfully !", Toast.LENGTH_SHORT).show();
                this.recreate(); // да влезе в create отново (щото имаме промяна) = ще ребилдне всичко с новите данни
            }
        }
    }

    @Override
    public void selectedBottle(Bottles bottlesModel, int pos) // ползваме тук - като се цъкне върху избраната позиция
    {
        // стартираме ново активити с код (чакаме 1) и му пращаме данните от обекта + позицията
        startActivityForResult(new Intent(BottlesManagment.this, SelectedBottleActivity.class).putExtra("data",bottlesModel).putExtra("position", pos),1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) // SEARCH
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                bottlesAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.search_view)
        {
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void All() // взима всички - GET
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
                                JSONObject bottle = array.getJSONObject(i);

                                Bottles bottlesModel = new Bottles // попълва ги в обект
                                        (
                                                bottle.getInt("id"),
                                                bottle.getString("name"),
                                                bottle.getInt("type"),
                                                bottle.getInt("ml"),
                                                bottle.getString("time")
                                        );

                                bottlesModelList.add(bottlesModel); // добавя ги в листата
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        bottlesAdapter.notifyDataSetChanged();
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

    private void enableSwipeToDeleteAndUndo() // за swap to delete
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                // взима данните
                final int position = viewHolder.getAdapterPosition();
                final Bottles bottlesModelItem = bottlesAdapter.getData().get(position);

                bottlesAdapter.removeItem(position); // маха го от адаптера

                deleteBottle((bottlesModelItem.getId())+""); // трие

                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        // при натискане на UNDO
                        bottlesAdapter.restoreItem(bottlesModelItem, position); // ресторва итема с взетите данни
                        recyclerView.scrollToPosition(position);
                        // връща в DB
                        undoDeleteBottle((bottlesModelItem.getId())+"", (bottlesModelItem.getName()), (bottlesModelItem.getType())+"", (bottlesModelItem.getMl())+"");
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteBottle(final String id) // трие - POST
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLE_DELETE,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) // OK
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                // пращаме само 1 параметър - ID-то
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void undoDeleteBottle(final String id, final String name, final String type, final String ml) // връща изтритото
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLE_UNDODELETE,
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
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // пращаме параметрите - всичките (и ИД също!)
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("type", type);
                params.put("ml", ml);
                //params.put("time", time); -> не ни трябва
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
