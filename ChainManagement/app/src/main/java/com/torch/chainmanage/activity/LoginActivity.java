package com.torch.chainmanage.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.torch.chainmanage.R;
import com.torch.chainmanage.model.User;
import com.torch.chainmanage.util.MD5Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout mUsernameLayout;
    private TextInputEditText mUsernameText;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordText;
    private Button mLoginBtn;
    private Button mRegisterBtn;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mUsernameLayout.setErrorEnabled(false);
            mPasswordLayout.setErrorEnabled(false);
            String pwd = mPasswordText.getText().toString();
            //密码位数超限
            if (!TextUtils.isEmpty(pwd) && pwd.length() > 10) {
                mPasswordLayout.setHint("密码位数不能大于10");
            } else {
                mPasswordLayout.setHint("请输入密码");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initStatusBarColor();
        initView();
    }

    /**
     * 初始化状态栏颜色透明，和背景色一致
     */
    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        mUsernameLayout = (android.support.design.widget.TextInputLayout) findViewById(R.id.il_username);
        mUsernameText = (android.support.design.widget.TextInputEditText) findViewById(R.id.et_username);
        mUsernameText.addTextChangedListener(mTextWatcher);
        mPasswordLayout = (android.support.design.widget.TextInputLayout) findViewById(R.id.il_password);
        mPasswordText = (android.support.design.widget.TextInputEditText) findViewById(R.id.et_password);
        mPasswordText.addTextChangedListener(mTextWatcher);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = mUsernameText.getText().toString().trim();
        String pwd = mPasswordText.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_login:
                login(name, pwd);
                break;
            case R.id.btn_register:
                addUser(name, pwd);
                break;
        }
    }

    /**
     * 添加用户
     */
    private void addUser(String name, String pwd) {
        if(!checkUser(name, pwd)) {
            return;
        }
        //判断用户是否已存在
        List<User> userList = DataSupport.where("name = ?", name).find(User.class);
        if (userList != null && !userList.isEmpty()) {
            showToast("用户名已存在");
            return;
        }
        //加密密码明文
        String encryptPwd = MD5Utils.encryptByMD5(pwd);
        User user = new User(name, encryptPwd);
        if(user.save()) {
            showToast("注册成功");
        } else {
            showToast("注册失败，请重试！！");
        }
    }

    private void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void login(String name, String pwd) {
        if(!checkUser(name, pwd)) {
            return;
        }
        //暂时先从本地数据库读取
        if(verifyUser(name, pwd)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户名验证
     * @param name
     * @param pwd
     */
    private boolean verifyUser(String name, String pwd) {
        List<User> userList = DataSupport.where("name = ?", name).find(User.class);
        if (userList == null || userList.isEmpty()) {
            showToast("用户名不存在");
            return false;
        }
        User user = userList.get(0);
        String encryptPwd = MD5Utils.encryptByMD5(pwd);
        if (!encryptPwd.equals(user.getPassword())) {
            showToast("密码错误");
            return false;
        }
        return true;
    }

    /**
     * 检测用户名和密码是否符合要求
     * @return
     */
    private boolean checkUser(String name, String pwd) {
        if (TextUtils.isEmpty(name)) {
            mUsernameLayout.setError("用户名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            mPasswordLayout.setError("密码不能为空");
            return false;
        }
        if (pwd.length() < 6 || pwd.length() > 10) {
            mPasswordLayout.setError("密码位数要在6到10之间");
            return false;
        }

        return true;
    }
}
