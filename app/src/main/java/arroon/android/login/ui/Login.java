package arroon.android.login.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import arroon.android.login.R;
import arroon.android.login.bmobobject.BmobUser;
import arroon.android.login.utils.CCallBack;

public class Login extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mDoLogin;
    private FloatingActionButton mGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        setListener();
    }

    private void initView() {
        mUsername = findViewById(R.id.et_username);
        mPassword = findViewById(R.id.et_password);
        mDoLogin = findViewById(R.id.bt_go);
        mGoRegister = findViewById(R.id.fab);
    }

    private void setListener() {
        mDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                BmobUser.login(username, password, new CCallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        User.start(Login.this);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register.start(Login.this, view);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mGoRegister.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoRegister.setVisibility(View.VISIBLE);
    }
}
