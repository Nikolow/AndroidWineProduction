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

public class WinesManagment extends AppCompatActivity implements WinesAdapter.SelectedWine
{
    Toolbar toolbar;
    RecyclerView recyclerView;

    List<Wine> WinesModelList = new ArrayList<>();

    WinesAdapter WinesAdapter;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wines_managment);

        // бутона ADD
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(WinesManagment.this, AddWine.class),2);
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);

        linearLayout = findViewById(R.id.LinearLayout);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        WinesAdapter = new WinesAdapter(WinesModelList,this);

        recyclerView.setAdapter(WinesAdapter);

        All(); // get ALL

        //final UserModel user = SharedPrefManager.getInstance(this).getUser();
        //if(String.valueOf(user.getAccess()).equals("3") || String.valueOf(user.getAccess()).equals("2"))
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
                Toast.makeText(getApplicationContext(), "Wine on position " + (POS + 1) + " has been Updated!", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
        }
        else if(requestCode==2) // ако кода е 2
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "The new Wine has been added successfully !", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
        }
    }

    @Override
    public void selectedWine(Wine WinesModel, int pos) // избрания итем
    {
        // активити чакащо резултат с код 1 + пращаме данни
        startActivityForResult(new Intent(WinesManagment.this, SelectedWineActivity.class).putExtra("data",WinesModel).putExtra("position", pos),1);
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

                WinesAdapter.getFilter().filter(newText);
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
        // GET заявка
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_WINES,
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
                                JSONObject Wine = array.getJSONObject(i);

                                Wine WinesModel = new Wine
                                        (
                                                Wine.getInt("id"),
                                                Wine.getString("name"),
                                                Wine.getInt("grape"),
                                                Wine.getInt("q"),
                                                Wine.getString("time")
                                        );

                                WinesModelList.add(WinesModel);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        WinesAdapter.notifyDataSetChanged();
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

    private void enableSwipeToDeleteAndUndo() // swipe to delete
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                final int position = viewHolder.getAdapterPosition();
                final Wine WinesModelItem = WinesAdapter.getData().get(position);

                WinesAdapter.removeItem(position);

                deleteWine((WinesModelItem.getId())+"");

                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        WinesAdapter.restoreItem(WinesModelItem, position);
                        recyclerView.scrollToPosition(position);

                        undoDeleteWine((WinesModelItem.getId())+"", (WinesModelItem.getName()), (WinesModelItem.getGrape())+"", (WinesModelItem.getQ())+"");
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteWine(final String id) // delete
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_WINE_DELETE,
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
                // параметрите
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void undoDeleteWine(final String id, final String name, final String grape, final String q) // връщане
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_WINE_UNDODELETE,
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
                // параметрите
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("grape", grape);
                params.put("q", q);
                //params.put("time", time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
