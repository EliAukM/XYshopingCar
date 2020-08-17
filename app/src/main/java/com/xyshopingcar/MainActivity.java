package com.xyshopingcar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单商家购物车
                startActivity(new Intent(MainActivity.this,AloneShopActivity.class));
            }
        });


        findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //多商家购物车
                startActivity(new Intent(MainActivity.this,MultiShopActivity.class));
            }
        });

    }
}
