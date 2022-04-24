package com.ph.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final char num_0 = '0';
    private static final char num_1 = '1';
    private static final char num_2 = '2';
    private static final char num_3 = '3';
    private static final char num_4 = '4';
    private static final char num_5 = '5';
    private static final char num_6 = '6';
    private static final char num_7 = '7';
    private static final char num_8 = '8';
    private static final char num_9 = '9';
    private static final char point = '.';
    private static final char plus = '+';
    private static final char minus = '-';
    private static final char multiply = 'x';
    private static final char divide = '÷';

    private StringBuilder currentInput = new StringBuilder("");
    private String currentOut = "";
    private boolean hasNum = false;
    private TextView inputText;
    private TextView outputText;
    private SharedPreferences sharedPreferences;

    /**
     * activity启动时调用的方法。
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_port);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        }
        setListener();
        readSavedState();
    }

    /**
     * 读取SharedPreferences持久化的数据
     */
    private void readSavedState() {
        SharedPreferences pref = getSharedPreferences("calculator", MODE_PRIVATE);
        String str1 = pref.getString("inputText", "");
        String str2 = pref.getString("outputText", "");
        Log.d("text", "str1=" + str1 + " str2=" + str2);
        if (str1 != null && str2 != null) {
            inputText.setText(str1);
            currentInput = new StringBuilder(str1);
            currentOut = str2;
            outputText.setText(str2);
        }
    }

    /**
     * 当横屏时调用的方法。
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 设置横屏
            setContentView(R.layout.activity_main_land);
            // 设置监听器
            setListener();
            // 保留输入输出数据
            inputText.setText(currentInput);
            outputText.setText(currentOut);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 设置竖屏
            setContentView(R.layout.activity_main_port);
            setListener();
            inputText.setText(currentInput);
            outputText.setText(currentOut);
        }
    }

    /**
     * activity暂停调用，用于保存数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        writeState();
    }

    /**
     * 保存状态
     */
    private void writeState() {
        sharedPreferences = getSharedPreferences("calculator", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("inputText", currentInput.toString());
        editor.putString("outputText", currentOut);
        editor.apply();
    }

    /**
     * 为各个按钮设置监听器。
     */
    private void setListener() {
        outputText = findViewById(R.id.outputText);
        outputText.setOnClickListener(this);
        inputText = findViewById(R.id.inputText);
        inputText.setOnClickListener(this);
        Button btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        Button btn_backspace = findViewById(R.id.btn_backspace);
        btn_backspace.setOnClickListener(this);
        Button btn_equal = findViewById(R.id.btn_equal);
        btn_equal.setOnClickListener(this);
        Button btn_point = findViewById(R.id.btn_point);
        btn_point.setOnClickListener(this);
        Button btn_plus = findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(this);
        Button btn_minus = findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(this);
        Button btn_multiply = findViewById(R.id.btn_multiply);
        btn_multiply.setOnClickListener(this);
        Button btn_divide = findViewById(R.id.btn_divide);
        btn_divide.setOnClickListener(this);
        Button btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        Button btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        Button btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        Button btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        Button btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        Button btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        Button btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        Button btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        Button btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        Button btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
    }

    /**
     * 监听按钮的操作，实现计算器的功能。
     *
     * @param view 视图
     */
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_0:
                addInput(num_0);
                break;
            case R.id.btn_1:
                addInput(num_1);
                break;
            case R.id.btn_2:
                addInput(num_2);
                break;
            case R.id.btn_3:
                addInput(num_3);
                break;
            case R.id.btn_4:
                addInput(num_4);
                break;
            case R.id.btn_5:
                addInput(num_5);
                break;
            case R.id.btn_6:
                addInput(num_6);
                break;
            case R.id.btn_7:
                addInput(num_7);
                break;
            case R.id.btn_8:
                addInput(num_8);
                break;
            case R.id.btn_9:
                addInput(num_9);
                break;
            case R.id.btn_point:
                addOperator(point);
                break;
            case R.id.btn_plus:
                hasNum = false;
                addOperator(plus);
                break;
            case R.id.btn_minus:
                hasNum = false;
                addOperator(minus);
                break;
            case R.id.btn_multiply:
                hasNum = false;
                addOperator(multiply);
                break;
            case R.id.btn_divide:
                hasNum = false;
                addOperator(divide);
                break;
            case R.id.btn_backspace:
                if (currentInput.length() > 0) {
                    currentInput.deleteCharAt(currentInput.length() - 1);
                }
                break;
            case R.id.btn_clear:
                currentInput = new StringBuilder("");
                outputText.setText("");
                break;
            case R.id.btn_equal:
                currentOut = compute();
                outputText.setText(currentOut);
                hasNum = true;
                writeState();
                break;
        }
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        inputText.setText(currentInput);
    }

    /**
     * 向输入框添加操作符
     * 添加规则：
     * 如果输入框没有字符，则不添加；
     * 如果输入框字符数大于等于13个，则不添加
     * 如果最后一个字符为数字，则添加；
     * 如果最后一个字符为非数字且不等于待添加字符，则替换最后一个字符；
     * 如果最后一个字符为非数字且等于待添加字符，则不添加。
     *
     * @param str 待添加字符
     */
    private void addOperator(char str) {
        if (currentInput.length() == 0) {
            return;
        }
        if (currentInput.length() >= 13) {
            return;
        }
        if (isNum(currentInput.charAt(currentInput.length() - 1))) {
            currentInput.append(str);
            return;
        }
        if (currentInput.charAt(currentInput.length() - 1) != str) {
            currentInput.replace(currentInput.length() - 1, currentInput.length(), String.valueOf(str));
            return;
        }
        return;
    }

    /**
     * 向输入框添加操作数
     * 添加规则：
     * 如果第一次输入则正常添加；
     * 如果已经输出了结果，输入框再添加；
     * 如果输入框字符数大于等于10个，则不添加
     *
     * @param str 待添加字符
     */
    private void addInput(char str) {
        if (currentInput.length() >= 10) {
            return;
        }
        if (hasNum == false) {
            currentInput.append(str);
        } else {
            currentInput = new StringBuilder("");
            hasNum = false;
            currentInput.append(str);
        }
    }

    /**
     * 当点击等于号时调用此方法，计算当前输入的字符所表示的结果。
     *
     * @return 计算结果
     */
    private String compute() {
        double ans = 0.0;
        // 后缀表达式队列
        Queue<String> suffixQueue = new LinkedList<>();
        //  运算符栈
        Stack<Character> opStack = new Stack<>();
        // 操作数栈
        Stack<Double> numStack = new Stack<>();

        if (currentInput.length() == 0) {
            return "";
        }
        // 开头是乘号和除号或结尾是操作符视为不合法
        if (currentInput.charAt(0) == multiply || currentInput.charAt(0) == divide || !isNum(currentInput.charAt(currentInput.length() - 1))) {
            Log.e("error", "开头是乘号和除号或结尾是操作符视为不合法！");
            return "出错";
        }
        // 不能除以0
        if (currentInput.indexOf(String.valueOf(divide) + String.valueOf(num_0)) > 0) {
            Log.e("error", "不能除以0！");
            return "不能除以0";
        }

        // 中缀表达式转后缀表达式
        try {
            for (int i = 0; i < currentInput.length(); i++) {
                // 将数字组成double类型并入栈
                StringBuilder str = new StringBuilder();
                while (i == 0 || i <= currentInput.length() - 1 && !isFourOps(currentInput.charAt(i))) {
                    str.append(currentInput.charAt(i));
                    i++;
                }
                Double num = Double.valueOf(String.valueOf(str));
                suffixQueue.add(num.toString());

                if (i == currentInput.length()) {
                    while (!opStack.empty()) {
                        suffixQueue.add(String.valueOf(opStack.pop()));
                    }
                    break;
                }

                // 运算符栈空，则入栈
                if (opStack.empty()) {
                    opStack.push(currentInput.charAt(i));
                    continue;
                }

                char top = opStack.pop();
                // 如果栈顶优先级大于等于当前运算符，栈顶元素入栈后缀栈，当前运算符入运算符栈，否则当前运算符入运算符栈
                if (priority(top, currentInput.charAt(i))) {
                    suffixQueue.add(String.valueOf(top));
                    opStack.push(currentInput.charAt(i));
                } else {
                    opStack.push(top);
                    opStack.push(currentInput.charAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "中缀表达式转换为后缀表达式错误！ ");
            return "错误";
        }
        // 后缀表达式计算
        try {
            while (!suffixQueue.isEmpty()) {
                String str = suffixQueue.remove();
                // 如果是数字则入操作数栈
                if (str.length() != 1 || !isFourOps(str.charAt(0))) {
                    Double num = Double.valueOf(str);
                    numStack.push(num);
                } else {
                    Double num1 = numStack.pop();
                    Double num2 = numStack.pop();
                    Double res;
                    switch (str.charAt(0)) {
                        case plus:
                            res = num2 + num1;
                            break;
                        case minus:
                            res = num2 - num1;
                            break;
                        case multiply:
                            res = num2 * num1;
                            break;
                        case divide:
                            res = num2 / num1;
                            break;
                        default:
                            Log.e("error", "计算错误 ");
                            return "错误";
                    }
                    numStack.push(res);
                }
            }
        } catch (Exception e) {
            Log.e("error", "计算后缀表达式错误！ ");
            return "错误";
        }
        ans = numStack.pop();
        // 四舍五入，保留五位小数
        ans = new BigDecimal(ans).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        return ans + "";
    }

    /**
     * 比较栈顶运算符与当前运算符的优先级
     * 乘号和除号优先级大于加号减号
     *
     * @return 布尔值
     */
    private boolean priority(char top, char curr) {
        if (top == multiply || top == divide) {
            return true;
        } else if (curr == multiply || curr == divide) {
            return false;
        }
        return true;
    }

    /**
     * 判断该字符是否为数字，是数字返回true，否则返回false。
     *
     * @return 布尔值
     */
    private static boolean isNum(char ch) {
        return ch >= num_0 && ch <= num_9;
    }

    /**
     * 判断该字符是否为小数点，是小数点返回true，否则返回false。
     *
     * @return 布尔值
     */
    private static boolean isPoint(char ch) {
        return ch == point;
    }

    /**
     * 判断该字符是否为四则运算符，是四则运算符返回true，否则返回false。
     *
     * @return 布尔值
     */
    private static boolean isFourOps(char ch) {
        return ch == plus || ch == minus || ch == multiply || ch == divide;
    }

}