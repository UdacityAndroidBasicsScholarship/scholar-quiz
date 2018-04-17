package org.sairaa.scholarquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText forgotEmail, forgotSlackId, forgotEmail2, forgotSlackId2, forgotNewPwd, forgotConfPwd;
    TextView forgotPwdTextView;
    private LinearLayout forgotBeforeValidateLayout, forgotAfterValidateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmail = findViewById(R.id.forgot_pwd_email);
        forgotSlackId = findViewById(R.id.forgot_pwd_slack);
        forgotEmail2 = findViewById(R.id.forgot_pwd_email2);
        forgotSlackId2 = findViewById(R.id.forgot_pwd_slack2);
        forgotNewPwd = findViewById(R.id.forgot_pwd_new_pwd);
        forgotConfPwd = findViewById(R.id.forgot_pwd_conf_pwd);
        forgotBeforeValidateLayout = findViewById(R.id.forgot_before_validate_layout);
        forgotAfterValidateLayout = findViewById(R.id.forgot_after_validate_layout);
    }

    public void checkEmailSlackIds(View view) {
        TextView forgotNote = findViewById(R.id.forgotNote);
        if (forgotEmail.getText().toString().isEmpty() && forgotSlackId.getText().toString().isEmpty()) {
            forgotNote.setText(getResources().getString(R.string.forgotEmptyfield1));
        } else if (forgotEmail.getText().toString().isEmpty()) {
            forgotNote.setText(getResources().getString(R.string.forgotEmptyfield2));
        } else if (forgotSlackId.getText().toString().isEmpty()) {
            forgotNote.setText(getResources().getString(R.string.forgotEmptyfield3));
        } else {
            forgotBeforeValidateLayout.setVisibility(View.INVISIBLE);
            forgotAfterValidateLayout.setVisibility(View.VISIBLE);
            forgotEmail2.setText(forgotEmail.getText().toString());
            forgotSlackId2.setText(forgotSlackId.getText().toString());
        }
    }
}
