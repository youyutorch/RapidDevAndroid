package com.torch.chainmanage.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.torch.chainmanage.constant.RequestUrl;
import com.torch.chainmanage.http.OkHttpHelper;
import com.torch.chainmanage.R;
import com.torch.chainmanage.http.bean.LoginResult;
import com.torch.chainmanage.http.bean.Params;
import com.torch.chainmanage.model.User;
import com.torch.chainmanage.util.GsonUtil;
import com.torch.chainmanage.util.MD5Utils;
import com.torch.chainmanage.util.SharePreUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputLayout mUsernameLayout;
    private TextInputEditText mUsernameText;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordText;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private RelativeLayout mLoadingLayout;

    private Handler mHandler = new Handler();

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
        //判断是否已登录
        String userId = SharePreUtil.GetShareString(this, "userId");
        if (!TextUtils.isEmpty(userId)) {
            //跳转到主页面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        mLoadingLayout = (RelativeLayout) findViewById(R.id.loading);
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
//        String encryptPwd = MD5Utils.encryptByMD5(pwd);
//        User user = new User(name, encryptPwd);
//        if(user.save()) {
//            showToast("注册成功");
//        } else {
//            showToast("注册失败，请重试！！");
//        }
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
        //服务器验证
        verifyUser(name, pwd);
    }

    /**
     * 用户名验证
     * @param name
     * @param pwd
     */
    private void verifyUser(String name, String pwd) {
        //显示loading
        mLoadingLayout.setVisibility(View.VISIBLE);
        //新建请求params对象
        Params params = new Params();
        params.putParam(Params.KEY_USERID, name);
        params.putParam(Params.KEY_PASSWORD, pwd);
        OkHttpHelper.getInstance().doPost(RequestUrl.Login, params, new OkHttpHelper.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                User user = GsonUtil.parseLoginJson(result);
                if (user == null) {
                    String msg = "登录失败：";
                    if (TextUtils.isEmpty(GsonUtil.getReason())) {
                        msg += "json解析出错";
                    } else {
                        msg += GsonUtil.getReason();
                    }
                    showToast(msg);
                } else {
                    boolean saveFlag = user.save();
                    Log.d(TAG, "保存User到数据库:" + user.toString());
                    //保存到sharepreference中，下次进入应用默认登入
                    if (saveFlag) {
                        SharePreUtil.SetShareString(LoginActivity.this, "userId", user.getUserId());
                    }
                    //跳转到主页面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                mLoadingLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
                showToast("登录失败：" + e.getMessage());
                mLoadingLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 用户名验证
     * @param name
     * @param pwd
     */
    private void verifyUser1(String name, String pwd) {
        //显示loading
        mLoadingLayout.setVisibility(View.VISIBLE);
        OkHttpHelper.doLogin(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //验证失败
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        mLoadingLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //验证成功
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        if(response.isSuccessful()) {
                            try {
                                String result =  response.body().string();
                                Log.d(TAG, result);
                                //解析json数据
                                Gson gson = new Gson();
                                LoginResult loginResult = gson.fromJson(result, LoginResult.class);

                                //利用loginResult对象，构造user对象
                                if (loginResult != null) {
                                    if (loginResult.getCode() == 0) {
                                        //成功获取到用户信息
                                        LoginResult.BodyBean bodyBean = loginResult.getBody();
                                        User user = new User();
                                        user.setUserId(bodyBean.getUserid());
                                        user.setArea(bodyBean.getArea());
                                        user.setImg(bodyBean.getImg());
                                        user.setNickName(bodyBean.getNickname());
                                        user.setSex(bodyBean.getSex());
                                        user.setPhoneNum(bodyBean.getPhonenum());
                                        user.setJob(bodyBean.getJob());
                                        //保存到本地数据库
                                        Log.d(TAG, "保存前:" + user.toString());
                                        boolean saveFlag = user.save();
                                        Log.d(TAG, "保存后:" + user.toString());
                                        //保存到sharepreference中，下次进入应用默认登入
                                        if (saveFlag) {
                                            SharePreUtil.SetShareString(LoginActivity.this, "userId", bodyBean.getUserid());
                                        }
                                        //跳转到主页面
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mLoadingLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
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
