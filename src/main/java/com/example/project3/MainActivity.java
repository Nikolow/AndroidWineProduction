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
import android.text.TextUtils;
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

// това ни е UsersManagment обаче направих първо това и после почнах да билдвам останалите "модули"
public class MainActivity extends AppCompatActivity implements UsersAdapter.SelectedUser
{
    Toolbar toolbar;
    RecyclerView recyclerView;

    List<UserModel> userModelList = new ArrayList<>();

    UsersAdapter usersAdapter;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ADD
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(MainActivity.this, AddUser.class),2);
            }
        });




        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);

        linearLayout = findViewById(R.id.LinearLayout);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        usersAdapter = new UsersAdapter(userModelList,this);

        recyclerView.setAdapter(usersAdapter);

        All(); // get ALL

        //final UserModel user = SharedPrefManager.getInstance(this).getUser();
        //if(String.valueOf(user.getAccess()).equals("3"))
        enableSwipeToDeleteAndUndo(); // swipe to delete
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // функцията, която приема резултата
    {
        super.onActivityResult(requestCode, resultCode, data);

        int EXIT2 = 0;
        int EXIT = data.getIntExtra("exit", EXIT2);

        int POS2 = 0;
        int POS = data.getIntExtra("position", POS2);

        if(requestCode==1) // ако кода е 1
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "User on position " + (POS + 1) + " has been Updated!", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
        }
        else if(requestCode==2) // ако кода е 2
        {
            if(EXIT == 1)
            {
                Toast.makeText(getApplicationContext(), "The new user has been added successfully !", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
        }

    }

    @Override
    public void selectedUser(UserModel userModel, int pos) // selected item
    {
        startActivityForResult(new Intent(MainActivity.this, SelectedUserActivity.class).putExtra("data",userModel).putExtra("position", pos),1);
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

                usersAdapter.getFilter().filter(newText);
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

    private void All() // GET
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_USERS,
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
                                JSONObject usrs = array.getJSONObject(i);

                                UserModel userModel = new UserModel
                                (
                                        usrs.getInt("id"),
                                        usrs.getString("username"),
                                        usrs.getString("password"),
                                        usrs.getString("email"),
                                        usrs.getInt("access")
                                );

                                userModelList.add(userModel);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        usersAdapter.notifyDataSetChanged();
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

    private void enableSwipeToDeleteAndUndo() // swipe to deleteee
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                final int position = viewHolder.getAdapterPosition();
                final UserModel userModelItem = usersAdapter.getData().get(position);

                usersAdapter.removeItem(position);

                deleteUser((userModelItem.getId())+"");

                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        usersAdapter.restoreItem(userModelItem, position);
                        recyclerView.scrollToPosition(position);

                        undoDeleteUser((userModelItem.getId())+"", (userModelItem.getUserName()), (userModelItem.getEmail()), (userModelItem.getPassword()), (userModelItem.getAccess())+"");
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteUser(final String id) // delete userr
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_DELETE,
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

    private void undoDeleteUser(final String id, final String username, final String password, final String email, final String access) // restore user
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UNDODELETE,
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
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("access", access);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
