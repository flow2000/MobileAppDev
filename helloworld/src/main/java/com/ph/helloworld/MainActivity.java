package com.ph.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.h1);
        TextView textView = (TextView) findViewById(R.id.h2);
        button.setOnClickListener(new View.OnClickListener() {
            int cnt=1;
            @Override
            public void onClick(View view) {
                textView.setText(cnt+"");
                cnt++;
            }
        });
    }
}