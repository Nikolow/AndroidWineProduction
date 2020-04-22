package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OperatorMain extends AppCompatActivity
{
    TextView textViewAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_main);

        textViewAccess = (TextView) findViewById(R.id.textViewAccess);

        UserModel user = SharedPrefManager.getInstance(this).getUser();

        textViewAccess.setText("Welcome back Operator "+user.getUserName()); // вземи името му

        // имаме само 2 буона щото оператора трябв да има достъп само до ТЕЗИ - грозде + вина

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

    }
}
