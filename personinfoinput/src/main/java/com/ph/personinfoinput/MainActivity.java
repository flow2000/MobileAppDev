package com.ph.personinfoinput;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 外部存储权限请求码
     */
    public static final int REQUEST_EXTERNAL_STORAGE_CODE = 9527;
    /**
     * 打开相册请求码
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * 图片剪裁请求码
     */
    public static final int PICTURE_CROPPING_CODE = 200;

    private static final String TAG = "MainActivity";

    private User user = new User();

    /**
     * 控件
     */
    private ImageView avatar;
    private EditText name;
    private RadioButton radio_man, radio_men;
    private RadioGroup radioGroup;
    private EditText national;
    private EditText idNumber;
    private EditText address;
    private EditText birth;
    private EditText email;
    private Button save;

    /**
     * 控件是否已全部正确输入
     */
    private boolean isAvatar = false;
    private boolean isName = false;
    private boolean isRadioGroup = false;
    private boolean isNational = false;
    private boolean isIdNumber = false;
    private boolean isAddress = false;
    private boolean isEmail = false;

    /**
     * Glide请求图片选项配置
     */
    private RequestOptions requestOptions = RequestOptions
//            .circleCropTransform()//圆形剪裁
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
        setTextChangedListener();
        requestPermission();
    }


    /**
     * 为各个按钮设置监听器。
     */
    private void setListener() {
        avatar = findViewById(R.id.avatar);
        avatar.setOnClickListener(this);
        name = findViewById(R.id.input_name);
        radioGroup = findViewById(R.id.radioGroup);
        radio_men = findViewById(R.id.radio_men);
        radio_man = findViewById(R.id.radio_man);
        national = findViewById(R.id.input_national);
        idNumber = findViewById(R.id.input_idNumber);
        address = findViewById(R.id.input_address);
        birth = findViewById(R.id.input_birth);
        email = findViewById(R.id.input_email);
        save = findViewById(R.id.btn_save);
        save.setOnClickListener(this);
    }

    /**
     * 添加输入框变化
     */
    private void setTextChangedListener() {

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (str.length() == 0) {
                    return;
                }
                String pattern = "[\\u4E00-\\u9FA5]+";
                if (!Pattern.matches(pattern, editable)) {
                    name.setError("请输入汉字");
                    isName = false;
                    return;
                }
                isName = true;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radio_men.getId() == i) {
                    user.setSex(1);
                    isRadioGroup = true;
                } else if (radio_man.getId() == i) {
                    isRadioGroup = true;
                    user.setSex(0);
                }
            }
        });

        national.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (str.length() == 0) {
                    return;
                }
                String s = "汉族、满族、蒙古族、回族、藏族、维吾尔族、苗族、彝族、壮族、布依族、侗族、瑶族、白族、土家族、哈尼族、哈萨克族、傣族、黎族、傈僳族、佤族、畲族、高山族、拉祜族、水族、东乡族、纳西族、景颇族、柯尔克孜族、土族、达斡尔族、仫佬族、羌族、布朗族、撒拉族、毛南族、仡佬族、锡伯族、阿昌族、普米族、朝鲜族、塔吉克族、怒族、乌孜别克族、俄罗斯族、鄂温克族、德昂族、保安族、裕固族、京族、塔塔尔族、独龙族、鄂伦春族、赫哲族、门巴族、珞巴族、基诺族";
                if (!s.contains(str)) {
                    national.setError("请输入正确的民族");
                    isNational = false;
                    return;
                }
                user.setNational(str);
                isNational = true;
            }
        });

        idNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (str.length() == 0) {
                    idNumber.setError("请输入身份证号");
                }
                if (str.length() != 18) {
                    idNumber.setError("请输入正确的身份证号");
                }
                String pattern = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
                if (!Pattern.matches(pattern, editable)) {
                    idNumber.setError("请输入正确的身份证号");
                    isIdNumber = false;
                    return;
                }
                isIdNumber = true;
                if (str.length() == 18) {
                    int year = Integer.parseInt(idNumber.getText().toString().substring(6, 10));
                    int month = Integer.parseInt(idNumber.getText().toString().substring(10, 12));
                    int day = Integer.parseInt(idNumber.getText().toString().substring(12, 14));
                    birth.setText(year + "-" + month + "-" + day);
                    user.setIdNumber(str);
                    user.setBirth(year + "-" + month + "-" + day);
                }
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAddress = true;
            }
        });

        birth.setInputType(InputType.TYPE_NULL);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (str.length() == 0) {
                    email.setError("请输入邮箱");
                }
                String pattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
                if (!Pattern.matches(pattern, editable)) {
                    email.setError("请输入正确的邮箱格式");
                    isEmail = false;
                    return;
                }
                isEmail = true;
                user.setEmail(str);
            }
        });
    }

    /**
     * 监听头像和保存按钮
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                openAlbum(view);
                break;
            case R.id.btn_save:
                if (!isAvatar) {
                    showMsg("请上传头像");
                    break;
                }
                if (!isName) {
                    name.requestFocus();
                    name.setError("姓名为空或不符合");
                    break;
                }
                if (!isRadioGroup) {
                    showMsg("请选择性别");
                    break;
                }
                if (!isNational) {
                    national.requestFocus();
                    national.setError("民族为空或不符合");
                    break;
                }
                if (!isIdNumber) {
                    idNumber.requestFocus();
                    idNumber.setError("身份证为空或不符合");
                    break;
                }
                if (!isAddress) {
                    address.requestFocus();
                    address.setError("地址为空或不符合");
                    break;
                }
                if (!isEmail) {
                    email.requestFocus();
                    email.setError("邮箱为空或不符合");
                    break;
                }
                showMsg("保存成功");
                break;
        }
    }

    /**
     * 权限请求
     */
    @AfterPermissionGranted(REQUEST_EXTERNAL_STORAGE_CODE)
    private void requestPermission() {
        String[] param = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, param)) {
            //已有权限
            showMsg("已获得权限");
        } else {
            //无权限 则进行权限请求
            EasyPermissions.requestPermissions(this, "请求权限", REQUEST_EXTERNAL_STORAGE_CODE, param);
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

    /**
     * 打开相册
     */
    public void openAlbum(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_ALBUM_CODE);
    }

    /**
     * 图片剪裁
     *
     * @param uri 图片uri
     */
    private void pictureCropping(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);


        File cropPhoto;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //虽然getExternalStoragePublicDirectory方法被淘汰了，但是不影响使用
            cropPhoto = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "crop_image.jpg");
        } else {
            cropPhoto = new File(getExternalCacheDir(), "crop_image.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropPhoto));

        startActivityForResult(intent, PICTURE_CROPPING_CODE);
    }

    /**
     * 返回Activity结果
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_ALBUM_CODE && resultCode == RESULT_OK) {
            //打开相册返回
            final Uri imageUri = Objects.requireNonNull(data).getData();

            try {
                Drawable d = Drawable.createFromStream(getContentResolver().openInputStream(imageUri), null);
                avatar.setBackground(d);
                isAvatar = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //图片剪裁
//             pictureCropping(imageUri);
        } else if (requestCode == PICTURE_CROPPING_CODE && resultCode == RESULT_OK) {
            //图片剪裁返回
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                //在这里获得了剪裁后的Bitmap对象，可以用于上传
                Bitmap image = bundle.getParcelable("outputFormat");
                //设置到ImageView上
                Glide.with(this).load(image).into(avatar);
            } else {
                showMsg("未找到截图");
            }
        }
    }

    /**
     * 权限请求结果
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}