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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;

    private Button mProfileSendReqBtn, mDeclinebtn;
    private ImageView mProfileImage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mFriendReqDatabase;

    private DatabaseReference mFriendDatabase;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mRootRef;

    private DatabaseReference mNotoficationDatabase;

    private FirebaseUser mCurrent_user;

    private ProgressDialog mProgressDialog;

    private String mcurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotoficationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");

        mProfileName = (TextView) findViewById(R.id.profile_display_name);
        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileStatus = (TextView) findViewById(R.id.profile_display_status);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalfriends);
        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_req_btn);
        mDeclinebtn = (Button) findViewById(R.id.profile_decline_btn);

        mcurrent_state = "not_friends";

        mDeclinebtn.setVisibility(View.INVISIBLE);
        mDeclinebtn.setEnabled(false);



        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("loading user data");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);


                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.jellyfish1).into(mProfileImage);

                //-------------------------FRIENDS LIST/ REQUEST FEATURE---------------------------

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)) {
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                        /*    firebaseAuth = FirebaseAuth.getInstance();
                            String from_user_id = firebaseAuth.getCurrentUser().getUid();*/

                            if (req_type.equals("received")) {

                                mcurrent_state = "req_received";
                                mProfileSendReqBtn.setText(R.string.accept_friend);

                                mDeclinebtn.setVisibility(View.VISIBLE);
                                mDeclinebtn.setEnabled(true);

                            } else if (req_type.equals("sent")) {

                                mcurrent_state = "req_sent";
                                mProfileSendReqBtn.setText(R.string.cancel_friend);

                                mDeclinebtn.setVisibility(View.INVISIBLE);
                                mDeclinebtn.setEnabled(false);

                            }

                            mProgressDialog.dismiss();
                        } else {

                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)) {

                                        mcurrent_state = "friends";
                                        mProfileSendReqBtn.setText(R.string.unfriend_person);

                                        mDeclinebtn.setVisibility(View.INVISIBLE);
                                        mDeclinebtn.setEnabled(false);


                                    }
                                    mProgressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();

                                }
                            });
                        }


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

                if (mcurrent_state.equals("not_friends")) {

                    DatabaseReference  newNotificationRef=mRootRef.child("notifications").child(user_id).push();
                    String newNotificationId=newNotificationRef.getKey();


                    HashMap<String, String> notificationData = new HashMap();
                    notificationData.put("from", mCurrent_user.getUid());
                    notificationData.put("type", "request");


                    Map requestMap= new HashMap();
                    requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" +user_id + "/request_type" , "sent");
                    requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid()  + "/request_type" , "received" );
                    requestMap.put("notifications/"+ user_id +"/" + newNotificationId,notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError !=null){
                                Toast.makeText(ProfileActivity.this,"There was some error in sending request",Toast.LENGTH_LONG).show();

                            }

                            mProfileSendReqBtn.setEnabled(true);

                            mcurrent_state = "req_sent";
                            mProfileSendReqBtn.setText("Cancel Friend request");




                        }
                    });

                }


                //--------------CANCEL REQUEST STATE-------------------------------

                if (mcurrent_state.equals("req_sent")) {

                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id)
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mProfileSendReqBtn.setEnabled(true);
                                    mcurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText(R.string.send_frnd_req);

                                    mDeclinebtn.setVisibility(View.INVISIBLE);
                                    mDeclinebtn.setEnabled(false);
                                }
                            });

                        }
                    });
                }

                //--------------request recieved-------------------

                if (mcurrent_state.equals("req_received")) {

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());


                    Map friendsMap= new HashMap();
                    friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" +user_id + "/date" , currentDate);
                    friendsMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid()  + "/date" , currentDate);

                    friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" +user_id , null);
                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() , null );

                  mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                      @Override
                      public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                          if(databaseError==null){


                              mProfileSendReqBtn.setEnabled(true);
                              mcurrent_state = "friends";
                              mProfileSendReqBtn.setText("Unfriend this person");

                              mDeclinebtn.setVisibility(View.INVISIBLE);
                              mDeclinebtn.setEnabled(false);

                          }
                          else{
                              String error=databaseError.getMessage();
                              Toast.makeText(ProfileActivity.this, error,Toast.LENGTH_LONG).show();

                          }
                      }
                  });

                }

                //=============UN FRIENDS================//

                if(mcurrent_state.equals("friends")){


                    Map unfriendMap= new HashMap();


                    unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" +user_id , null);
                    unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid() , null );

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError==null){

                                mcurrent_state = "not_friends";
                                mProfileSendReqBtn.setText("Send Friend Request");

                                mDeclinebtn.setVisibility(View.INVISIBLE);
                                mDeclinebtn.setEnabled(false);

                            }
                            else{
                                String error=databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error,Toast.LENGTH_LONG).show();

                            }
                            mProfileSendReqBtn.setEnabled(true);
                        }
                    });
                }



            }
        });

    }
}
