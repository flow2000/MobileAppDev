package com.ph.callandsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {

    /**
     * 控件
     */
    private EditText sms_recipient, sms_text;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 设置监听器
        setListener();
        // 设置上一个页面传来的数据
        setIntentData();
        // 设置文本变化监听器
        setTextChangedListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        sms_recipient = findViewById(R.id.sms_recipient);
        sms_text = findViewById(R.id.sms_text);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(view -> {
            if (!judePhoneNumber()){
                showMsg("请输入正确的号码");
                return;
            }
            sendSMS();
        });
    }

    /**
     * 发送短信
     */
    private void sendSMS(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+ sms_recipient.getText()));
        String text = sms_text.getText().toString();
        intent.putExtra("sms_body", text);
        startActivity(intent);
    }

    /**
     * 判断电话号码的合法性
     */
    private boolean judePhoneNumber() {
        String pattern = "^1[3|4|5|7|8]\\d{9}$";
        if (!Pattern.matches(pattern, sms_recipient.getText())) {
            return false;
        }
        return true;
    }

    /**
     * 设置上一个页面传来的数据
     */
    private void setIntentData() {
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phonenumber");
        sms_recipient.setText(phoneNumber);
    }

    /**
     * 添加输入框变化
     */
    private void setTextChangedListener() {
        sms_recipient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pattern = "^1[3|4|5|7|8]\\d{9}$";
                if (!Pattern.matches(pattern, editable)) {
                    sms_recipient.setError("请输入正确的号码");
                }
            }
        });
    }

    /**
     * Toast提示
     *
     * @param msg 文本
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}