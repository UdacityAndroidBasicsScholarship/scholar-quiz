package org.sairaa.scholarquiz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText forgotEmail, forgotSlackId, forgotEmail2, forgotSlackId2, forgotNewPwd, forgotConfPwd;
    TextView forgotPwdTextView;
    private LinearLayout forgotBeforeValidateLayout, forgotAfterValidateLayout;

    Unbinder unbinder;

    @BindView(R.id.forgot_pwd_email)
    EditText forgotEmail_Edittext;


    final ButterKnife.Action<View> EMPTY_FIELD = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {

            Toast.makeText(ForgotPasswordActivity.this, "Enter email", Toast.LENGTH_LONG).show();
            forgotEmail_Edittext.setError("enter email");
        }
    };

    private boolean checkEmptyField() {

        return forgotEmail_Edittext.getText().toString().trim().isEmpty() ? true : false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        unbinder = ButterKnife.bind(this);

    }

    @OnClick(R.id.forgot_pwd_submit)
    public void forgotButton_Onclick(View view) {

        if (checkEmptyField()) {

            ButterKnife.apply(forgotEmail_Edittext, EMPTY_FIELD);
        } else {
            forgetEmail(forgotEmail_Edittext.getText().toString().trim());
        }
    }

    private void forgetEmail(String email) {

        AppInfo.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
