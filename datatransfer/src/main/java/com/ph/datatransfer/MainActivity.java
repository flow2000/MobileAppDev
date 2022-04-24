package com.ph.datatransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 控件
     */
    private EditText num1, num2;
    private TextView tv_op, tv_result;
    private Button btn_multiply, btn_divide;

    /**
     * 操作符，例如除法、乘法
     */
    private String op;

    /**
     * 计算结果
     */
    private Double ans;

    /**
     * 常量0
     */
    private static final Double zero = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
        setTextChangedListener();
        Intent intent = getIntent();
        op = intent.getStringExtra("op");
        if (op != null) {
            ans = intent.getDoubleExtra("ans", 0.0);
            tv_op.setText(op);
            tv_result.setText(ans.toString());
        }
    }

    /**
     * 为各个按钮设置监听器。
     */
    private void setListener() {
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        tv_op = findViewById(R.id.op);
        tv_result = findViewById(R.id.result);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_divide = findViewById(R.id.btn_divide);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
    }

    /**
     * 添加输入框变化
     */
    private void setTextChangedListener() {
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                try {
                    Double num = Double.valueOf(str);
                } catch (Exception e) {
                    num1.setError("请输入正确的数字");
                }
            }
        });

        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                try {
                    Double num = Double.valueOf(str);
                } catch (Exception e) {
                    num2.setError("请输入正确的数字");
                }
            }
        });
    }

    /**
     * 监听按钮
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        String str1, str2;
        switch (view.getId()) {
            case R.id.btn_multiply:
                str1 = num1.getText().toString();
                str2 = num2.getText().toString();
                if (str1.length()==0){
                    num1.setError("不能为空");
                    num1.requestFocus();
                    break;
                }
                if (num2.length()==0){
                    num2.setError("不能为空");
                    num2.requestFocus();
                    break;
                }
                try {
                    Double num1 = Double.valueOf(str1);
                    Double num2 = Double.valueOf(str2);
                    Intent intent = new Intent(this, MainActivity2.class);
                    Double result = num1 * num2;
                    result = new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    String res = str1 + "*" + str2 + "=" + result;
                    intent.putExtra("op", "乘法");
                    intent.putExtra("ans", result);
                    intent.putExtra("result", res);
                    startActivity(intent);
                } catch (Exception e) {
                    showMsg("出错");
                }
                break;
            case R.id.btn_divide:
                str1 = num1.getText().toString();
                str2 = num2.getText().toString();
                if (str1.length()==0){
                    num1.setError("不能为空");
                    num1.requestFocus();
                    break;
                }
                if (num2.length()==0){
                    num2.setError("不能为空");
                    num2.requestFocus();
                    break;
                }
                try {
                    Double num1 = Double.valueOf(str1);
                    Double num2 = Double.valueOf(str2);
                    // 应该在传输数据前就判断是否符合，不符合则给出提示，不能将错误传递到下一层
                    if (zero.equals(num2)) {
                        showMsg("被除数不能为0");
                        showMsg("传输数据前判断数据是否合法");
                        showMsg("不符合则给出提示");
                        showMsg("不能将错误传递到下一层");
                        break;
                    }
                    Intent intent = new Intent(this, MainActivity2.class);
                    Double result = num1 / num2;
                    result = new BigDecimal(result).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                    String res = str1 + "/" + str2 + "=" + result;
                    intent.putExtra("op", "除法");
                    intent.putExtra("ans", result);
                    intent.putExtra("result", res);
                    startActivity(intent);
                } catch (Exception e) {
                    showMsg("出错");
                }
                break;
        }
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}