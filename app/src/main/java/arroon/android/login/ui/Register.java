package arroon.android.login.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import arroon.android.login.R;
import arroon.android.login.utils.SimpleTransitionListener;

public class Register extends AppCompatActivity {

    public static void start(Activity activity, View view) {
        activity.getWindow().setExitTransition(null);
        activity.getWindow().setEnterTransition(null);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, view.getTransitionName());
        activity.startActivity(new Intent(activity, Register.class), options.toBundle());
    }

    private FloatingActionButton mDoClose;
    private CardView mCardView;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private Button mDoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ShowEnterAnimation();
        initView();
    }

    private void initView() {
        mDoClose = findViewById(R.id.fab);
        mCardView = findViewById(R.id.cv_add);

        mUsername = findViewById(R.id.et_username);
        mPassword = findViewById(R.id.et_password);
        mRepeatPassword = findViewById(R.id.et_repeatpassword);
        mDoRegister = findViewById(R.id.bt_go);


        mDoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        mDoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String repassword = mRepeatPassword.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(Register.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Register.this, "密码至少6位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(Register.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
//                BmobUser.register(username, password, new CCallBack() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        animateRevealClose();
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new SimpleTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mCardView.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, mCardView.getWidth() / 2, 0, mDoClose.getWidth() / 2, mCardView
                .getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mCardView.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, mCardView.getWidth() / 2, 0, mCardView.getHeight(), mDoClose
                .getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCardView.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mDoClose.setImageResource(R.drawable.plus);
                Register.super.onBackPressed();
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
