package com.example.stpl.firechat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextInputLayout mStatus;
    private  Button  mSaveButton;
    //firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);



        //firebase
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        mStatusDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);

        mToolBar=(Toolbar)findViewById(R.id.status_appBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value=getIntent().getStringExtra("status_value");


        mStatus=(TextInputLayout)findViewById(R.id.status_input);
        mSaveButton=(Button)findViewById(R.id.status_savebtn);

        mStatus.getEditText().setText(status_value);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //progress
                mprogress=new ProgressDialog(StatusActivity.this);
                mprogress.setTitle("Saving Changes");
                mprogress.setMessage("Please Wait While We Save The Changes");
                mprogress.show();

                String status=mStatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mprogress.dismiss();
                        }
                        else {
                            Toast.makeText(StatusActivity.this,"There Was Error In Saving Changes",Toast.LENGTH_LONG).show();

                        }

                    }
                });


            }
        });
    }
}
