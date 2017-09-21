package com.example.stpl.firechat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    Toolbar mToolbar;

    RecyclerView mUserslist;
    //private FirebaseArray mSnapshots;
    private DatabaseReference mUsersDatabase;

   /* FirebaseDatabase database;
    DatabaseReference myRef;
    List<Users> list;
    private RecyclerView recycle;
    Button view;
*/

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = findViewById(R.id.users_appbar);
        mToolbar.setTitle("All Users");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserslist = (RecyclerView)findViewById(R.id.users_list);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(new LinearLayoutManager(this));
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");


    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase)
        {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users1, int position) {

                usersViewHolder.setDisplayName(users1.getName());
                usersViewHolder.setUserStatus(users1.getStatus());
                usersViewHolder.setUserImage(users1.getThumb_image(),getApplicationContext());

                final String user_id= getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile_intent=new Intent(UsersActivity.this,ProfileActivity.class);
                        profile_intent.putExtra("user_id",user_id);
                        startActivity(profile_intent);
                    }
                });
            }
        };
        mUserslist.setAdapter(firebaseRecyclerAdapter);
    }




    public static class UsersViewHolder extends  RecyclerView.ViewHolder{
        View mView;
        private TextView userNameView;
        private TextView userStatusView;
        public UsersViewHolder(View itemView) {
            super(itemView);
             mView = itemView;
            userNameView =(TextView) mView.findViewById(R.id.user_single_name);
            userStatusView =(TextView)mView.findViewById(R.id.user_single_status);
        }
        public  void  setDisplayName(String name){

            userNameView.setText(name);

        }

        public void setUserStatus(String status) {
            userStatusView.setText(status);

        }

        public void setUserImage(String thumb_image, Context ctx) {
            CircleImageView userImageView=(CircleImageView)mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.jellyfish1).into(userImageView);
        }
    }



    }




