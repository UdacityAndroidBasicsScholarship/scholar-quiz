package org.sairaa.scholarquiz.ui.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.ChannelListModel;
import org.sairaa.scholarquiz.ui.Moderator.Question.QuestionListActivity;

public class MasterAdminActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText channelName, moderator;
    private Button createNewChennel;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_admin);

        channelName = findViewById(R.id.new_channel_name);
        moderator = findViewById(R.id.assign_mod);
        createNewChennel = findViewById(R.id.create_new_channel);
        createNewChennel.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.create_new_channel:
                AppInfo.databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                            String email = snapshot.child("email").getValue(String.class);
                            String name = snapshot.child("name").getValue(String.class);

                            if(email != null){
                                if(!channelName.getText().toString().trim().equals("") && !moderator.getText().toString().trim().equals("")){
                                    if(email.equals(moderator.getText().toString().trim())){
                                        alertBuilder = new AlertDialog.Builder(MasterAdminActivity.this);
                                        alertBuilder.setTitle("New Channel");
                                        alertBuilder.setMessage("You are going to publish "+channelName.getText().toString().trim()+" and its Moderator is :"
                                                +name+"\n Want to Create it !");
                                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // get the moderator Id
                                                final String moderatorId = snapshot.getKey().toString();
                                                // create New channel and assign new moderator to it.
                                                createNewChannelWithModerator(channelName.getText().toString().trim(),moderatorId);
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(MasterAdminActivity.this," NO",Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog alertDialog = alertBuilder.create();
                                        alertDialog.show();
                                        Toast.makeText(MasterAdminActivity.this,"Email id exist"+email,Toast.LENGTH_SHORT).show();



                                    }else{
                                        Toast.makeText(MasterAdminActivity.this,"Email id does not exist",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(MasterAdminActivity.this,"Please Fill Channel name and Moderator email",Toast.LENGTH_SHORT).show();
                                }
                            }




//                            if(snapshot.child("email").getValue().toString().equals("")){
//
//                            }else{
//                                String email = snapshot.child("email").getValue().toString();
//                                Log.i("aaaadmin: ",snapshot.getKey()+" : "+email);
//                            }

                            Log.i("aaaadmin: ",snapshot.getKey()+" : "+email);
                        }


//                        FirebaseUser user = FirebaseAuth.getInstance().
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }



    }

    private void createNewChannelWithModerator(final String channelName, final String moderatorId) {

        AppInfo.databaseReference.child("ChannelList").push().setValue(new ChannelListModel(moderatorId,channelName)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MasterAdminActivity.this,""+channelName+" Created with moderator"+moderatorId,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Not created
                }
            }
        });
        Toast.makeText(MasterAdminActivity.this,"Chammel Name : "+channelName+"\nModerator Id : "+moderatorId,Toast.LENGTH_SHORT).show();
    }
}
