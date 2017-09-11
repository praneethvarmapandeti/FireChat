package com.example.stpl.firechat;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUSer;
    private CircleImageView mDisplayImage;
    private TextView mName,mStatus;
    Button mChangeImage,mChangeStatus;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDisplayImage=(CircleImageView)findViewById(R.id.settings_image) ;
        mName=(TextView)findViewById(R.id.settings_name);
        mStatus=(TextView)findViewById(R.id.settings_status);
        mChangeStatus=(Button)findViewById(R.id.change_status) ;
        mChangeImage=(Button)findViewById(R.id.change_image) ;
        mCurrentUSer= FirebaseAuth.getInstance().getCurrentUser();

        String current_uid=mCurrentUSer.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Toast.makeText(SettingsActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();
                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String thumb_image=dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
               // mDisplayImage.setImageResource(R.drawable.jellyfish1);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value=mStatus.getText().toString();


                Intent status_intent=new Intent(SettingsActivity.this,StatusActivity.class);
                status_intent.putExtra("status_value",status_value);
                startActivity(status_intent);

            }
        });
        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent galleryIntent=new Intent();
                galleryIntent.setType("image*//*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);*/


            }
        });


    }

}
