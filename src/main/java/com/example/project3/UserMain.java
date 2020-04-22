package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserMain extends AppCompatActivity
{
    TextView textViewAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        textViewAccess = (TextView) findViewById(R.id.textViewAccess);

        UserModel user = SharedPrefManager.getInstance(this).getUser();

        textViewAccess.setText("Welcome back User "+user.getUserName()); // get name


        // достъп само до бутилките...
        findViewById(R.id.BottlesManagment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), BottlesManagment.class)); // bottles management activity
            }
        });
    }
}
