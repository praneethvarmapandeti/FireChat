package com.example.stpl.firechat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private  String  mChatUser;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;

    private TextView mTitleView,mLastSeenView;
    private CircleImageView mProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolbar=(Toolbar)findViewById(R.id.chat_app_bar);

        setSupportActionBar(mChatToolbar);

        ActionBar actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef= FirebaseDatabase.getInstance().getReference();

        mChatUser=getIntent().getStringExtra("user_id");
        String userName=getIntent().getStringExtra("user_name");

        getSupportActionBar().setTitle(userName);
        LayoutInflater inflater=(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View action_bar_view =inflater.inflate(R.layout.chat_custom_bar,null);

        actionBar.setCustomView(action_bar_view);

        mTitleView=(TextView)findViewById(R.id.custom_bar_title);
        mLastSeenView=(TextView)findViewById(R.id.custom_bar_seen);
        mProfileImage=(CircleImageView)findViewById(R.id.custom_bar_image);

        mTitleView.setText(userName);

        mRootRef.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                String online=dataSnapshot.child("online").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                if(online.equals("true")){
                    mLastSeenView.setText("Online");

                }else {
                    mLastSeenView.setText(online);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
}