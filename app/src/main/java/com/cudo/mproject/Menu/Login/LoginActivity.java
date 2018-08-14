package com.cudo.mproject.Menu.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.BuildConfig;
import com.cudo.mproject.Dialog.ProgressDialogGanz;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.AnimUtils;
import com.cudo.mproject.Utils.GetAllPermissionV2;
import com.cudo.mproject.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginInterface.View {

    @BindView(R.id.username)
    EditText etUsername;
    @BindView(R.id.password)
    EditText etPassword;
    @BindView(R.id.btnlogin)
    Button btnLogin;
    //    @BindView(R.id.powered)
//    TextView powered;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.splash)
    View splash;
    @BindView(R.id.main_lay)
    View main_lay;
   /* @BindView(R.id.lokasi)
    EditText etLokasi;*/
   /* @BindView(R.id.setting)
    Button setting;*/

    LoginPresenter loginPresenter;
    ProgressDialogGanz progressDialog;

    GetAllPermissionV2 getAllPermissionV2;
    String TAG = getClass().getSimpleName();
    String IMEI_Number_Holder = null;
    String versionApk =   BuildConfig.VERSION_NAME;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this, this, realm);
        initSetup();

    }

    private void splashVisible() {
        AnimUtils.animate(splash, false);
        AnimUtils.animate(main_lay, true);
    }

    void checkLog() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(TAG, "checkLog: data " + data);
        Log.d(TAG, "checkLog: action " + action);
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI_Number_Holder = telephonyManager.getDeviceId();
        if (username != null && password != null)
            try {
                Log.d(TAG, "checkLogin: " + username + password+IMEI_Number_Holder);
                loginPresenter.login(username, password,IMEI_Number_Holder,versionApk);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    void initSetup() {
        version.setText("V"+ BuildConfig.VERSION_NAME);
//        version.setText("v"+"0.1.0");
//        powered.setText("powered by www.cudocomm.com");
        getAllPermissionV2 = new GetAllPermissionV2(this);
        progressDialog = new ProgressDialogGanz(this);
        getPermission();
    }

    private void getPermission() {
        if (getAllPermissionV2.checkPermission()) {
            doAutoLogin();
        }

    }

    void doAutoLogin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashVisible();
                if (User.isLogin(realm)) {
//                    if (RcCode.Rc_Code == "xx01") {
//                        Toast.makeText(getApplicationContext(), "User sedang aktif" , Toast.LENGTH_SHORT).show();
//                    }else{
                        User.doLogin(LoginActivity.this);
//                    }
                } else {
                    checkLog();
                }
            }
        }, 1000);

    }

    @OnClick(R.id.btnlogin)
    void setBtnLogin() {
        loginPresenter.login(etUsername.getText().toString(), etPassword.getText().toString(), IMEI_Number_Holder, versionApk);
    }


    @Override
    public void onUsernameError() {
        etUsername.setError(getString(R.string.empty));
    }

    @Override
    public void onPasswordError() {
        etPassword.setError(getString(R.string.empty));
    }

    @Override
    public void onSuccess() {

        showProgress(false);
        User.doLogin(this);
    }

    @Override
    public void showProgress(boolean show) {


        if (show)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    @Override
    public void onError(String error) {
        Log.d(LoginActivity.class.getSimpleName(), "onError: " + error);
        showProgress(false);
        showAlertDialog(getString(R.string.warning), error, null, true);
    }

    @Override
    public void onToast(String toast) {
        Utils.showToast(this, toast);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                Log.d(LoginActivity.class.getSimpleName(), "onRequestPermissionsResult: " + permissions[i]);
            }
        }
        if (grantResults.length > 0) {
            Log.d(LoginActivity.class.getSimpleName(), "onRequestPermissionsResult: " + grantResults.length);
        }
        Log.d(LoginActivity.class.getSimpleName(), "onRequestPermissionsResult: ");
        if (requestCode == GetAllPermissionV2.PERMISSION_ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                doAutoLogin();
            } else {
                getPermission();
            }
        }

    }
}