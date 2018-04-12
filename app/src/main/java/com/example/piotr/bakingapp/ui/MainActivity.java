package com.example.piotr.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.utils.CakeController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CakeController cakeController = new CakeController();
        cakeController.start();
    }
}
