package org.sairaa.scholarquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{

    // Declare Firebase Instance
    private FirebaseAuth mAuth;

    // Declare Firebase Database Reference
    private DatabaseReference mDatabase;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();

        // When Already Have Account Button is clicked, Move back to Sign In Screen
        findViewById(R.id.button_AlreadyAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        findViewById(R.id.button_SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Declare variable to fetch values entered in email and password field
                EditText emailId_EditText = findViewById(R.id.editText_EmailId);
                EditText password_EditText = findViewById(R.id.editText_Password);
                EditText confirmPassword_EditText = findViewById(R.id.editText_ConfirmPassword);

                // Store the value in variables after converting them to string
                final String email = String.valueOf(emailId_EditText.getText());
                String password = String.valueOf(password_EditText.getText());
                String confirmPassword = String.valueOf(confirmPassword_EditText.getText());

                // Check if Password and Confirm Password field matches
                if (password.equals(confirmPassword)) {

                    // Sign Up the user with Email ID and Password provided or show error if any
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Call function to update the Firebase Database
                                        updateDatabase();

                                        // Sign in success, take signed-in user's information to channel activity
                                        Intent channelIntent = new Intent(RegisterActivity.this,UserChannelActivity.class);
                                        startActivity(channelIntent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    // Show message if Password and Confirm Password fields does not match
                    Toast.makeText(RegisterActivity.this, "Password and Confirm Password Field should have same values",
                            Toast.LENGTH_SHORT).show();

                    // Set the focus to Password Field
                    password_EditText.requestFocus();
                }

            }
        });

    }

    /**
     * Function to Update the Firebase Database
     * - Inserts Name of the user in the Users table in Firebase
     * - Inserts EmailId of the user in the Users table in Firebase
     * - Inserts SlackId of the user in the Users table in Firebase
     * */

    private void updateDatabase() {

        // Get the values entered by user
        // Declare variable to fetch values entered in email and password field
        EditText name_EditText = findViewById(R.id.editText_Name);
        EditText slackId_EditText = findViewById(R.id.editText_SlackId);

        String name = String.valueOf(name_EditText.getText());
        String slackId = String.valueOf(slackId_EditText.getText());

        // Initialize the instance of Firebase database to get current logged in users information
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Store the user ID of the logged in user in variable
        String userId = String.valueOf(user.getUid());
        String userEmail = String.valueOf(user.getEmail());

        // Initialize Firebase Database Instance to the table Users
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Save the Users information in Users table in Firebase
        mDatabase.child(userId).child("Name").setValue(name);
        mDatabase.child(userId).child("EmailId").setValue(userEmail);
        mDatabase.child(userId).child("SlackId").setValue(slackId);

    }
}