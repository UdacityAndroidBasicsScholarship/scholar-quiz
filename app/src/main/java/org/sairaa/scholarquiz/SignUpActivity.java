package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    // Declare Firebase Instance
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private DatabaseReference mDatabase;
    FirebaseUser user;

    String adminFlag;
    String userId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();

        /**
         * When Already Have Account Button is clicked, Move back to Sign In Screen
         */
        findViewById(R.id.textview_AlreadyAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        // Check is user is connected to internet

        if (!isNetworkAvailable()) {
            Toast.makeText(SignUpActivity.this,"To Register your account, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
        }


        /**
         * When Signup button is clicked,
         * - check if the information entered is valid and display message
         * - create new signup if no issue
         * */
        findViewById(R.id.button_SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if user is connected to internet
                if (isNetworkAvailable()){

                    // Declare variable to fetch values entered in email and password field

                    EditText userName_EditText = findViewById(R.id.editText_Name);
                    EditText emailId_EditText = findViewById(R.id.editText_EmailId);
                    EditText slackId_EditText = findViewById(R.id.editText_SlackId);
                    EditText password_EditText = findViewById(R.id.editText_Password);
                    EditText confirmPassword_EditText = findViewById(R.id.editText_ConfirmPassword);

                    // Store the value in variables after converting them to string
                    String userInputName = String.valueOf(userName_EditText.getText());
                    String email = String.valueOf(emailId_EditText.getText());
                    String userInputSlackId = String.valueOf(slackId_EditText.getText());
                    String password = String.valueOf(password_EditText.getText());
                    String confirmPassword = String.valueOf(confirmPassword_EditText.getText());

                    if (userInputName.isEmpty()) {

                        // Show message if User Name is not provided
                        Toast.makeText(SignUpActivity.this, "User Name is required for Sign Up...", Toast.LENGTH_SHORT).show();

                        // Set the focus to email id Field
                        userName_EditText.requestFocus();
                    }else if (email.isEmpty()) {

                        // Show message if email is not provided
                        Toast.makeText(SignUpActivity.this, "Email ID is required for Sign Up...", Toast.LENGTH_SHORT).show();

                        // Set the focus to email id Field
                        emailId_EditText.requestFocus();

                    }else if (!isValidEmail(email)){

                        // Show message if email is not in valid format
                        Toast.makeText(SignUpActivity.this, "Please enter a Valid Email ID...", Toast.LENGTH_SHORT).show();

                        // Set the focus to email id Field
                        emailId_EditText.requestFocus();

                    }else if (userInputSlackId.isEmpty()) {

                        // Show message if User Name is not provided
                        Toast.makeText(SignUpActivity.this, "Slack ID is required for Sign Up...", Toast.LENGTH_SHORT).show();

                        // Set the focus to email id Field
                        slackId_EditText.requestFocus();
                    } else if (password.isEmpty()) {

                        // Show message if password is not provided
                        Toast.makeText(SignUpActivity.this, "Please enter a Password...", Toast.LENGTH_SHORT).show();

                        // Set the focus to Password Field
                        emailId_EditText.requestFocus();

                    }else if (password.equals(confirmPassword)) {
                        // Check if Password and Confirm Password field matches

                        // Sign Up the user with Email ID and Password provided or show error if any
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Call function to update the Firebase Database
                                            updateDatabase();

                                            // SignUp user

                                            signUpUser();

                                        } else {

                                            // If sign in fails, display a message to the user.
                                            String err = "";

                                            String errorCode = ((FirebaseAuthUserCollisionException) task.getException()).getErrorCode();

                                            if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                                err = "User already exists. You can use different Email ID to Sign Up...";
                                                Toast.makeText(SignUpActivity.this, err, Toast.LENGTH_SHORT).show();
                                            }else{
                                                err = errorCode;
                                            }

                                        }
                                    }
                                });
                    } else {
                        // Show message if Password and Confirm Password fields does not match
                        Toast.makeText(SignUpActivity.this, "Password and Confirm Password Field should have same values...", Toast.LENGTH_SHORT).show();

                        // Set the focus to Password Field
                        password_EditText.requestFocus();
                    }
                }else {
                    Toast.makeText(SignUpActivity.this,"To Register your account, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * Function to Update the Firebase Database
     * - Inserts Name of the user in the Users table in Firebase
     * - Inserts EmailId of the user in the Users table in Firebase
     * - Inserts SlackId of the user in the Users table in Firebase
     */

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
        mDatabase = FirebaseDatabase.getInstance().getReference("SQ_Users");

        // Save the Users information in Users table in Firebase
        mDatabase.child(userId).child("Name").setValue(name);
        mDatabase.child(userId).child("EmailId").setValue(userEmail);
        mDatabase.child(userId).child("SlackId").setValue(slackId);
        mDatabase.child(userId).child("ModeratorFlag").setValue("No");
        mDatabase.child(userId).child("AdminFlag").setValue("No");

    }

    public void signUpUser(){

        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = String.valueOf(user.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("SQ_Users/").child(user.getUid());

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adminFlag = String.valueOf(dataSnapshot.child("AdminFlag").getValue());
                userName = String.valueOf(dataSnapshot.child("Name").getValue());

                // Sign up success, take signed-up user's information to home activity
                Intent signUpIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                signUpIntent.putExtra("userName", userName);
                startActivity(signUpIntent);
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    /**
     * Function to check if the Email Id entered is in valid format i.e contains @ and .com
     */
    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Function to check if Device is connected to Internet
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
