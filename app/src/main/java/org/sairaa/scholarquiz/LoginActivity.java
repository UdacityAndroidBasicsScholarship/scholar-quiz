package org.sairaa.scholarquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in

        if (mAuth.getCurrentUser() != null) {
            // Call Function to move user to next screen as user is already Signed In
            signInUser();
        }

        // When Sign Up button is pressed move to SignUp Screen
        findViewById(R.id.button_SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }

        });


        /**
         * When Sign In button is clicked perform the actions
         * - Check if Email ID and Password are provided
         * - Check if Email ID is of correct format
         * - Sign In user on succeesful Login Else show Message to the user
         * */
        findViewById(R.id.button_SignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Declare variable to fetch values entered in email and password field
                EditText emailId_EditText = findViewById(R.id.editText_EmailId);
                EditText password_EditText = findViewById(R.id.editText_Password);

                // Store the value in variables after converting them to string
                String email = String.valueOf(emailId_EditText.getText());
                String password = String.valueOf(password_EditText.getText());

                /*
                 * - Check if the values were entered in the fields by the user
                 * - Check if the Email id entered in correct Format
                 */

                if ((email.isEmpty()) || (password.isEmpty())) {

                    Toast.makeText(LoginActivity.this, "Email ID/ Password are required to Sign In!!", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email)) {

                    Toast.makeText(LoginActivity.this, "Please provide Valid Email Id..!!", Toast.LENGTH_LONG).show();

                } else {

                    // Email Id is in correct format , try to Login the User
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Call function to move user to next screen on successful Sign In
                                        signInUser();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        });

    }


    /** Function to Move the User to Next screen on Successful Sign In or
     *  if user is already Signed In
     */

    public void signInUser() {

        // Sign in success, take signed-in user's information to channel activity
        Intent channelIntent = new Intent(LoginActivity.this,UserChannelActivity.class);
        startActivity(channelIntent);
        finish();
    }

    /**
     * Function to check if the Email Id entered is in valid format i.e contains @ and .com
     */
    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}

