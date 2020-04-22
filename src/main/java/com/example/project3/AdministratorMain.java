package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdministratorMain extends AppCompatActivity
{
    TextView textViewAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_main);


        textViewAccess = (TextView) findViewById(R.id.textViewAccess);

        // обект
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        textViewAccess.setText("Welcome back Administrator "+user.getUserName()); // името


        // бутони, които водят кум различни активитита

        findViewById(R.id.UserManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); // user management activity
            }
        });

        findViewById(R.id.BottlesManagment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), BottlesManagment.class)); // bottles management activity
            }
        });

        findViewById(R.id.GrapesManagment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), GrapesManagment.class)); // grapes management activity
            }
        });

        findViewById(R.id.WinesManagment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), WinesManagment.class)); // wines management activity
            }
        });

        findViewById(R.id.BottlingManagment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), BottlingManagment.class)); // bottling management activity
            }
        });

        findViewById(R.id.AppReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), AppReport.class)); // reports activity
            }
        });
    }
}
