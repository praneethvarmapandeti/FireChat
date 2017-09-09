package com.example.stpl.firechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    //private String TAG;
    private FirebaseAuth mAuth;
    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout mTabLayout;
    // private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Fire Chat");
        //tabs
        mViewPager=(ViewPager)findViewById(R.id.view_pager);
        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);

        mTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        //  mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            sendToStart();
            /*Intent start_intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(start_intent);
            finish();*/
            // User is signed out
        }

    }

    private void sendToStart() {
            Intent start_intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(start_intent);
            finish();
    }


}
