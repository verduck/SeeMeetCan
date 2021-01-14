package com.example.seemeetcan;

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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextId;
    private EditText editTextPw;
    private TextView tvErrorMessage;

    private Button btnLogin;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextId = (EditText) findViewById(R.id.edit_id);
        editTextPw = (EditText) findViewById(R.id.edit_pw);
        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editTextId.getText().toString();
                password = editTextPw.getText().toString();
                if (username.equals("") || password.equals("")) {
                    tvErrorMessage.setText("아이디또는 비밀번호를 입력하세요.");
                    tvErrorMessage.setVisibility(TextView.VISIBLE);
                } else {
                    new Login().execute();
                }
            }
        });
    }

    private class Login extends AsyncTask {
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
        private boolean isSuccess;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Connection.Response loginPage = Jsoup.connect("https://cyber.jj.ac.kr/login.php")
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();
                Connection.Response homePage = Jsoup.connect("https://cyber.jj.ac.kr/login/index.php")
                        .cookies(loginPage.cookies())
                        .data("username", username)
                        .data("password", password)
                        .method(Connection.Method.POST)
                        .userAgent(USER_AGENT)
                        .execute();
                isSuccess = !homePage.cookies().isEmpty();
                if (isSuccess) { // 로그인 성공
                    Document doc = homePage.parse();
                    String name = doc.select("#page-header > nav > div > div.usermenu > ul > li.user_department.hidden-xs").text();

                    RequestHttpURLConnection con = new RequestHttpURLConnection();
                    String json;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("StudentId", username);
                    json = jsonObject.toString();

                    json = con.request(json);


                    JSONObject resultJson = new JSONObject(json);
                    System.out.println(resultJson.toString());
                    boolean result = (boolean) resultJson.get("Result");
                    if (result) {
                        Profile p = new Profile(resultJson.getJSONObject("User"));
                        Intent mainIntent = new Intent(getApplication(), MainActivity.class);
                        mainIntent.putExtra("profile", p);
                        startActivity(mainIntent);
                        LoginActivity.this.finish();
                    } else {
                        Profile p = new Profile(username, name);
                        Intent profileEditIntent = new Intent(getApplication(), EditProfileActivity.class);
                        profileEditIntent.putExtra("profile", p);
                        profileEditIntent.putExtra("is_new", true);
                        startActivity(profileEditIntent);
                        LoginActivity.this.finish();
                    }
                } else { // 로그인 실패

                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            ;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (!isSuccess) {
                tvErrorMessage.setText("전주대학교 학생이 아니거나, 잘못된 비밀번호입니다.");
                tvErrorMessage.setVisibility(TextView.VISIBLE);
            }
        }
    }
}
