package com.example.stpl.firechat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

private TextView mProfileName,mProfileStatus,mProfileFriendsCount;

    private Button mProfileSendReqBtn;
    private ImageView mProfileImage;

    private DatabaseReference mFriendReqDatabase;

    private DatabaseReference mUsersDatabase;
    private FirebaseUser mCurrent_user;

    private ProgressDialog mProgressDialog;

    private  String mcurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id= getIntent().getStringExtra("user_id");

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        mFriendReqDatabase=FirebaseDatabase.getInstance().getReference().child("Friend_req");

        mCurrent_user= FirebaseAuth.getInstance().getCurrentUser();

        mProfileName=(TextView)findViewById(R.id.profile_display_name);
        mProfileImage=(ImageView)findViewById(R.id.profile_image);
        mProfileStatus=(TextView)findViewById(R.id.profile_display_status);
        mProfileFriendsCount=(TextView)findViewById(R.id.profile_totalfriends);
        mProfileSendReqBtn=(Button)findViewById(R.id.profile_send_req_btn);

        mcurrent_state ="not_friends";


        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("loading user data");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
           String display_name=dataSnapshot.child("name").getValue().toString() ;
                String status=dataSnapshot.child("status").getValue().toString() ;
                String image=dataSnapshot.child("image").getValue().toString() ;

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.jellyfish1).into(mProfileImage);

                //-------------------------FRIENDS LIST/ REQUEST FEATURE---------------------------

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)){
                            String req_type=dataSnapshot.child("request_type").getValue().toString();

                            if (req_type.equals("received")){

                                mcurrent_state="req_received";
                                mProfileSendReqBtn.setText("Accept friend request");

                            }else if(req_type.equals("sent")){

                                mcurrent_state="req_sent";
                                mProfileSendReqBtn.setText("Cancel friend request");

                            }
                        }


                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSendReqBtn.setEnabled(false);

                // --------------------NOT FRIENDS STATE-----------------

                if(mcurrent_state.equals("not_friends")){
                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mProfileSendReqBtn.setEnabled(true);
                                        mcurrent_state="req_sent";
                                        mProfileSendReqBtn.setText("Cancel Friend request");

                                   Toast.makeText(ProfileActivity.this,"Request sent successfully",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else{
                                Toast.makeText(ProfileActivity.this,"failed sending request",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }


                //--------------CANCEL REQUEST STATE-------------------------------

                if (mcurrent_state.equals("req_sent")){

                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id)
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mProfileSendReqBtn.setEnabled(true);
                                    mcurrent_state="not_friends";
                                    mProfileSendReqBtn.setText("Send friend request");
                                }
                            });

                        }
                    });
                }
            }
        });

         }
}
