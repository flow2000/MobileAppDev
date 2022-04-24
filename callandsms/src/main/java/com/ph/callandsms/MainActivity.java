package com.ph.callandsms;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 读取通讯录请求码
     */
    public static final int READ_CONTACTS_CODE = 8848;

    /**
     * 控件
     */
    private Button btn_import, btn_call, btn_sms;
    private EditText callNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置监听器
        setListener();
        // 设置文本变化监听器
        setTextChangedListener();
        // 申请通讯录权限和电话权限
        requestPermission();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        callNumber = findViewById(R.id.callNumber);
        btn_import = findViewById(R.id.btn_import);
        btn_import.setOnClickListener(this);
        btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);
        btn_sms = findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(this);
    }

    /**
     * 添加输入框变化
     */
    private void setTextChangedListener() {
        callNumber.addTextChangedListener(new TextWatcher() {
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
                    callNumber.setError("请输入正确的号码");
                }
            }
        });

    }

    /**
     * 监听按钮
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_import:
                intent = new Intent();
                intent.setAction("android.intent.action.PICK");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setType("vnd.android.cursor.dir/phone_v2");
                startActivityForResult(intent, READ_CONTACTS_CODE);
                break;
            case R.id.btn_call:
                if (!judePhoneNumber()) {
                    showMsg("请输入正确的号码");
                    break;
                }
                intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + callNumber.getText());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.btn_sms:
                if (!judePhoneNumber()) {
                    showMsg("请输入正确的号码");
                    break;
                }
                intent = new Intent(this, MainActivity2.class);
                String number = callNumber.getText().toString();
                intent.putExtra("phonenumber", number);
                startActivity(intent);
                break;
        }
    }

    /**
     * 判断电话号码的合法性
     */
    private boolean judePhoneNumber() {
        String pattern = "^1[3|4|5|7|8]\\d{9}$";
        if (!Pattern.matches(pattern, callNumber.getText())) {
            return false;
        }
        return true;
    }

    /**
     * 回调方法
     *
     * @param requestCode 请求码
     * @param resultCode  响应码
     * @param data        数据
     */
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_CONTACTS_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                cursor = contentResolver.query(uri, new String[]{"display_name", "data1"}, null, null, null);
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    phoneNum = phoneNum.replaceAll(" ", "");
                }
                callNumber.setText(phoneNum);
            }
        }
    }

    /**
     * 权限请求
     */
    @AfterPermissionGranted(READ_CONTACTS_CODE)
    private void requestPermission() {
        String[] param = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, param)) {
            //已有权限
            showMsg("已获得权限");
        } else {
            //无权限 则进行权限请求
            EasyPermissions.requestPermissions(this, "请求权限", READ_CONTACTS_CODE, param);
        }
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