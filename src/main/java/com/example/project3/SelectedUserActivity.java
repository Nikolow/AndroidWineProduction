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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SelectedUserActivity extends AppCompatActivity {

    TextView tvUser;

    EditText editTextUsername, editTextPassword, editTextEmail;

    private RadioGroup radioGroup;
    private RadioButton radioButton;


    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);

        tvUser = findViewById(R.id.selectedUser);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);


        Intent intent =getIntent();

        String StringID = "0";


        if(intent.getExtras() != null) // ОК
        {
            UserModel userModel = (UserModel) intent.getSerializableExtra("data");
            pos = (Integer) intent.getSerializableExtra("position");

            tvUser.setText("Editing " + userModel.getUserName());

            int access = userModel.getAccess();

            editTextUsername.setText(userModel.getUserName());
            //editTextPassword.setText(userModel.getPassword());
            editTextPassword.setText("empty");
            editTextEmail.setText(userModel.getEmail());



            // радио бутоните - кое е избрано

            RadioButton radiobutton1 = (RadioButton) findViewById(R.id.radioButton1);
            RadioButton radiobutton2 =(RadioButton)findViewById(R.id.radioButton2);
            RadioButton radiobutton3 =(RadioButton)findViewById(R.id.radioButton3);

            if(access == 2)
                radiobutton3.setChecked(true);
            else if(access == 1)
                radiobutton2.setChecked(true);
            else if(access == 3)
                radiobutton1.setChecked(true);


            StringID = ""+userModel.getId();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "PROBLEM!", Toast.LENGTH_SHORT).show();
        }

        final String finalStringID = StringID;
        // SAVE
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userEdit(finalStringID);
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
                intent.putExtra("exit", 0);
                setResult(1, intent); // код 1
                finish(); // финишираме активитито
            }
        });
    }


    private void userEdit(final String id)
    {
        // взимаме
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String email = editTextEmail.getText().toString();

        // радио бутоните
        String access = "0";
        radioGroup = (RadioGroup) findViewById(R.id.group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        CharSequence text = radioButton.getText();
        if ("Administrator".equals(text))
            access = "3";
        else if ("User".equals(text))
            access = "1";
        else if ("Operator".equals(text))
            access = "2";

        // валидация
        if (TextUtils.isEmpty(username))
        {
            editTextUsername.setError("Please enter username field");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            editTextPassword.setError("Please enter password field");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            editTextEmail.setError("Please enter email field");
            editTextEmail.requestFocus();
            return;
        }

        final String finalAccess = access;

        // POST заявка
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_EDIT,
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
                        Toast.makeText(getApplicationContext(), "ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                // параметрите които пращаме
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("username", username);
                params.put("password", sha1Hash(password));
                params.put("email", email);
                params.put("access", finalAccess);
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
        intent.putExtra("exit", 0);// няма
        setResult(1, intent); // код 1
        finish(); // финиширане активитито
        super.onBackPressed();
    }

    String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }
}
