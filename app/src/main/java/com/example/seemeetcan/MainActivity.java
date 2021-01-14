package com.example.seemeetcan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;

    private ProfileListFragment profileListFragment;
    private ChatListFragment chatListFragment;
    private MoreFragment moreFragment;

    private BottomNavigationView bottomNavigationView;

    private Profile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myProfile = getIntent().getParcelableExtra("profile");
        profileListFragment = new ProfileListFragment();
        chatListFragment = new ChatListFragment();
        moreFragment = new MoreFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, profileListFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RequestProfile().execute();
        ProfileListFragment profileListFragment = (ProfileListFragment) getSupportFragmentManager().findFragmentByTag("profileListFragment");
        ChatListFragment chatListFragment = (ChatListFragment) getSupportFragmentManager().findFragmentByTag("chatListFragment");
        //profileListFragment.refresh();

    }

    public Profile getMyProfile() {
        return myProfile;
    }

    private class ItemSelectedListener implements  BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            transaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.tab1:
                    transaction.replace(R.id.frame_layout, profileListFragment).commitAllowingStateLoss();
                    return true;
                case R.id.tab2:
                    transaction.replace(R.id.frame_layout, chatListFragment).commitAllowingStateLoss();
                    return true;
                case R.id.tab3:
                    transaction.replace(R.id.frame_layout, moreFragment).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    }

    private class RequestProfile extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                RequestHttpURLConnection con = new RequestHttpURLConnection("profile");
                String json;
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Id", myProfile.getId());
                json = con.request(jsonObject.toString());
                System.out.println(json);
                myProfile.replace(new JSONObject(json));
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}