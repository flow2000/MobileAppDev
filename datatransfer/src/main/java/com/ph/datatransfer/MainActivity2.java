package com.ph.datatransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity{

    /**
     * 控件
     */
    private TextView expression;
    private Button btn_return;

    /**
     * 操作符，例如除法、乘法
     */
    private String op;

    /**
     * 计算结果
     */
    private Double ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setListener();
        Intent intent = getIntent();
        op = intent.getStringExtra("op");
        ans = intent.getDoubleExtra("ans", 0.0);
        String result = intent.getStringExtra("result");
        expression.setText(result);
    }

    /**
     * 为各个按钮设置监听器。
     */
    private void setListener() {
        expression = findViewById(R.id.expression);
        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("op", op);
                intent.putExtra("ans", ans);
                startActivity(intent);
            }
        });
    }

}