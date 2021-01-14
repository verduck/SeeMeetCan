package com.example.seemeetcan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class DetailActivity extends AppCompatActivity {
    private Profile myProfile;
    private Profile profile;
    private TextView tvName;
    private TextView tvAge;
    private TextView tvMBTI;
    private TextView tvHeight;
    private TextView tvIntro;
    private Button btnFavor;
    private Button btnReport;

    private boolean isFavor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myProfile = getIntent().getParcelableExtra("my_profile");
        profile = getIntent().getParcelableExtra("profile");
        tvName = findViewById(R.id.tv_detail_name);
        tvAge = findViewById(R.id.tv_detail_age);
        tvMBTI = findViewById(R.id.tv_detail_mbti);
        tvHeight = findViewById(R.id.tv_detail_height);
        tvIntro = findViewById(R.id.tv_detail_intro);
        btnFavor = findViewById(R.id.btn_favor);
        btnReport = findViewById(R.id.btn_report);

        tvName.setText(profile.getName());
        tvAge.setText("나이 : " + profile.getAge());
        tvMBTI.setText("MBTI : " + profile.getMbti());
        tvHeight.setText("키 : " + profile.getHeight());
        tvIntro.setText("간단한 소개글 : \n" + profile.getIntroduction());

        btnFavor.setText("좋아요");
        for (int i : myProfile.getFavoriteIds()) {
            if (i == profile.getId()) {
                isFavor = true;
                btnFavor.setText("좋아요 취소");
                break;
            }
        }

        btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendFavor().execute();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] type = {"부적절한 내용의 소개글", "광고 및 스팸"};
                EditText etReport = new EditText(DetailActivity.this);
                final AlertDialog.Builder dialog = new AlertDialog.Builder((DetailActivity.this));
                dialog.setTitle("신고");
                dialog.setSingleChoiceItems(type, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                etReport.setHint("설명");
                dialog.setView(etReport);
                dialog.setNegativeButton("신고", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                dialog.show();
            }
        });

    }

    private class SendFavor extends AsyncTask {
        int result;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                RequestHttpURLConnection con = new RequestHttpURLConnection("favor");
                String json;
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("UserId", myProfile.getId());
                jsonObject.accumulate("FavoriteId", profile.getId());
                json = jsonObject.toString();

                json = con.request(json);
                result = Integer.parseInt(json.toString().replace("\n", ""));
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (result == 0) { // 좋아요 취소
                btnFavor.setText("좋아요");
            } else if (result == 1) { // 좋아요
                btnFavor.setText("좋아요 취소");
            }
        }
    }

    private class SendReport extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
