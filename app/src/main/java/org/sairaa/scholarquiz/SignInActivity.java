package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    // Declare an instance of FirebaseAuth
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
        setContentView(R.layout.activity_sign_in);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        /**
         * Check if the user is already signed in
         */
        if (mAuth.getCurrentUser() != null) {
            // Call Function to move user to next screen as user is already Signed In
            signInUser();
        }

        /**
         * When Sign Up button is pressed move to SignUp Screen\
         */
        findViewById(R.id.textview_SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
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

                // Check if User is connected to Internet
                if (isNetworkAvailable()) {

                    // Declare variable to fetch values entered in email and password field
                    EditText emailId_EditText = findViewById(R.id.editText_EmailId);
                    EditText password_EditText = findViewById(R.id.editText_Password);

                    // Store the value in variables after converting them to string
                    String email = String.valueOf(emailId_EditText.getText());
                    String password = String.valueOf(password_EditText.getText());

                    /**
                     * - Check if the values were entered in the fields by the user
                     * - Check if the Email id entered in correct Format
                     */

                    if ((email.isEmpty()) || (password.isEmpty())) {

                        Toast.makeText(SignInActivity.this, "Email ID/Password cannot be Blank..!!", Toast.LENGTH_LONG).show();

                    } else if (!isValidEmail(email)) {

                        Toast.makeText(SignInActivity.this, "Please provide Valid Email Id..!!", Toast.LENGTH_LONG).show();

                    } else {

                        // Email Id is in correct format , try to Login the User
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Call function to move user to next screen on successful Sign In
                                            signInUser();

                                        } else {

                                            // If sign in fails, display a message to the user.
                                            String err = "";

                                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                err += "Please provide a Valid Email Id and Password..";
                                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {

                                                String errorCode = ((FirebaseAuthInvalidUserException) task.getException()).getErrorCode();

                                                if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                                    err += "No matching user found. If you are new user, please Sign Up..";
                                                } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                                                    err += "User account has been disabled.";
                                                } else {
                                                    err += task.getException().getLocalizedMessage();
                                                }

                                            }else {
                                                err += task.getException().getLocalizedMessage();
                                            }

                                            Toast.makeText(SignInActivity.this, err ,Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                });


                    }
                } else {
                    Toast.makeText(SignInActivity.this,"To Sign Up, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    /**
     * Function to execute when forgot password button is pressed
     * */
    public void forgotPassword(View view){

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        // Initialize view that will contain custom layout for Alert box to show
        View mView = layoutInflater.inflate(R.layout.forgot_password_input_box,null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(mView);

        // Instance to access values entered in forgotPassword_TextView
        final EditText forgotPasswordInputText = mView.findViewById(R.id.forgotPassword_Text_View);

        // Title of Alert Dialog box
        TextView title = new TextView(this);

        title.setText("Forgot Password");
        title.setBackgroundColor(Color.parseColor("#E91E63"));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setCancelable(true);

        // Setting Cancel button in AlerDialog
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // Setting action for Send Button in AlertDialog
        alertDialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String forgotPasswordEmail = forgotPasswordInputText.getText().toString();

                // Check if the email id entered and is a valid email id.
                if(forgotPasswordEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Registered Email Id...",Toast.LENGTH_LONG).show();

                }else if (!isValidEmail(forgotPasswordEmail)){
                    Toast.makeText(getApplicationContext(),"Please Enter valid Email Id...",Toast.LENGTH_LONG).show();
                } else{

                    // Send Password reset email
                    FirebaseAuth.getInstance().sendPasswordResetEmail(forgotPasswordEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Reset Email sent to the Registered Email...",Toast.LENGTH_LONG).show();
                            }else{
                                String errorMessage = "";
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthException e) {
                                    switch (e.getErrorCode()) {
                                        case "ERROR_INVALID_EMAIL":
                                            errorMessage = "Please provide Correct Email ID...";
                                            break;
                                        case "ERROR_USER_NOT_FOUND":
                                            errorMessage = "User does not exists with this Email ID...";
                                            break;
                                        default:
                                            errorMessage = e.getLocalizedMessage();
                                            break;
                                    }

                                } catch (Exception e) {
                                    errorMessage = "Please provide valid Email ID...";
                                }
                                Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    /** Function to Move the User to Next screen on Successful Sign In or
     *  if user is already Signed In
     */

    public void signInUser() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = String.valueOf(user.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("SQ_Users/").child(user.getUid());

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adminFlag = String.valueOf(dataSnapshot.child("AdminFlag").getValue());
                userName = String.valueOf(dataSnapshot.child("Name").getValue());

                if(adminFlag.equals("Yes")) {
                    //Take the user to Admin Screen
                    Intent adminIntent = new Intent(SignInActivity.this, AdminHomeActivity.class);
                    startActivity(adminIntent);

                }else {
                    Intent homeIntent = new Intent(SignInActivity.this,HomeActivity.class);
                    homeIntent.putExtra("userName", userName);
                    startActivity(homeIntent);
                    finish();
                }
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
