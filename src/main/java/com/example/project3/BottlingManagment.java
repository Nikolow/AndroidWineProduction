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

public class BottlingManagment extends AppCompatActivity implements BottlingAdapter.SelectedBottling
{

    Toolbar toolbar;
    RecyclerView recyclerView;

    List<BottlingModel> bottlingModelList = new ArrayList<>();

    BottlingAdapter BottlingAdapter;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottling_managment);

        // АДД бутона - активити чакащо код 2
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(BottlingManagment.this, AddBottling.class),2);
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);

        linearLayout = findViewById(R.id.LinearLayout);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        BottlingAdapter = new BottlingAdapter(bottlingModelList,this);

        recyclerView.setAdapter(BottlingAdapter);

        All(); // взима всички

        //final UserModel user = SharedPrefManager.getInstance(this).getUser();
        //if(String.valueOf(user.getAccess()).equals("3"))
        enableSwipeToDeleteAndUndo(); // swipe to delete
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
                this.recreate();
            }
        }
        else if(requestCode==2) // ако кода е 2
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "The new bottle has been added successfully !", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
        }

    }

    @Override
    public void selectedBottling(BottlingModel bottlingModel, int pos)
    {
        startActivityForResult(new Intent(BottlingManagment.this, SelectedBottlingActivity.class).putExtra("data",bottlingModel).putExtra("position", pos),1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
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

                BottlingAdapter.getFilter().filter(newText);
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

    private void All()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_BOTTLING,
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
                                JSONObject bottling = array.getJSONObject(i);

                                BottlingModel bottlingModel = new BottlingModel
                                        (
                                                bottling.getInt("id"),
                                                bottling.getString("name"),
                                                bottling.getInt("wineid"),
                                                bottling.getInt("bottleid"),
                                                bottling.getString("time")
                                        );

                                bottlingModelList.add(bottlingModel);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        BottlingAdapter.notifyDataSetChanged();
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

    private void enableSwipeToDeleteAndUndo()
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                final int position = viewHolder.getAdapterPosition();
                final BottlingModel bottlingModelItem = BottlingAdapter.getData().get(position);

                BottlingAdapter.removeItem(position);

                deleteBottling((bottlingModelItem.getId())+"");

                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        BottlingAdapter.restoreItem(bottlingModelItem, position);
                        recyclerView.scrollToPosition(position);

                        undoDeleteBottling((bottlingModelItem.getId())+"", (bottlingModelItem.getName()), (bottlingModelItem.getWineid())+"", (bottlingModelItem.getBottleid())+"");
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteBottling(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLING_DELETE,
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
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void undoDeleteBottling(final String id, final String name, final String wineid, final String bottleid)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BOTTLING_UNDODELETE,
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("wineid", wineid);
                params.put("bottleid", bottleid);
                //params.put("time", time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
