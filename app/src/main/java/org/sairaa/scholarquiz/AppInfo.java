package org.sairaa.scholarquiz;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.sairaa.scholarquiz.RequestProvider.GetFirebaseAuth_Instance;

/**
 * Created by Vinay Gupta on 21-04-2018.
 */

public class AppInfo extends Application {

    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        firebaseAuth = GetFirebaseAuth_Instance.getFirebaseAuth_Instance();
        databaseReference = GetFirebaseAuth_Instance.getFirebaseDatabase_Instance();
    }


}
