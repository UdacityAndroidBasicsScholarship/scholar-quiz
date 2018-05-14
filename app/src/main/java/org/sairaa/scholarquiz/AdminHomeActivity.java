package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void manageChannel(View view) {
        startActivity(new Intent(AdminHomeActivity.this, AdminCreateChannelActivity.class));
    }

    public void manageModerator(View view) {
        startActivity(new Intent(AdminHomeActivity.this, AdminChannelListActivity.class));
    }


    public void logout(View view) {
        if (isNetworkAvailable()) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminHomeActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(AdminHomeActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Function to check if Device is connected to Internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
