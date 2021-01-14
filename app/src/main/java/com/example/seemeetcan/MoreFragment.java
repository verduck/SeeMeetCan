package com.example.seemeetcan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MoreFragment extends Fragment {
    private View view;
    private TextView tvName;
    private Button btnProfileModify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);

        final Profile myProfile = ((MainActivity) getActivity()).getMyProfile();
        tvName = (TextView) view.findViewById(R.id.tv_more_name);
        tvName.setText(myProfile.getName());

        btnProfileModify = (Button) view.findViewById(R.id.btn_profile_modify);
        btnProfileModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileEditIntent = new Intent(getActivity().getApplication(), EditProfileActivity.class);
                profileEditIntent.putExtra("profile", myProfile);
                startActivity(profileEditIntent);
            }
        });
        return view;
    }
}