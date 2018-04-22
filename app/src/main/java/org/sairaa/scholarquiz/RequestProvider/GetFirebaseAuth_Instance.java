package org.sairaa.scholarquiz.RequestProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vinay Gupta on 21-04-2018.
 */

public class GetFirebaseAuth_Instance {

    private GetFirebaseAuth_Instance(){}

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference_Instance;

    public static FirebaseAuth getFirebaseAuth_Instance(){
        if(firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        return firebaseAuth;
    }

    public static DatabaseReference getFirebaseDatabase_Instance(){
        if(databaseReference_Instance == null)
            databaseReference_Instance = FirebaseDatabase.getInstance().getReference();

        return databaseReference_Instance;
    }
}
