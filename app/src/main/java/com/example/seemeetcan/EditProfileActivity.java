package com.example.seemeetcan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.net.MalformedURLException;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editName, editHeight;
    private NumberPicker agePicker;
    private RadioGroup rgGender;
    private Spinner spinnerMBTI;
    private Button btnSave;

    private Profile myProfile;

    private boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editName = (EditText) findViewById(R.id.edit_name);
        agePicker = (NumberPicker) findViewById(R.id.np_age);
        rgGender = (RadioGroup) findViewById(R.id.rg_gender);
        editHeight = (EditText) findViewById(R.id.edit_height);
        spinnerMBTI = (Spinner) findViewById(R.id.spinner_mbti);
        btnSave = (Button) findViewById(R.id.btn_save);
        myProfile = getIntent().getParcelableExtra("profile");
        isNew = getIntent().getBooleanExtra("is_new", false);

        editName.setText(myProfile.getName());
        agePicker.setMinValue(19);
        agePicker.setMaxValue(30);
        agePicker.setWrapSelectorWheel(false);
        agePicker.setValue(myProfile.getAge());

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_male) {
                    myProfile.setGender(false);
                } else if (i == R.id.rb_female) {
                    myProfile.setGender(true);
                }
            }
        });

        if (myProfile.getGender()) {
            rgGender.check(R.id.rb_female);
        } else {
            rgGender.check(R.id.rb_male);
        }
        editHeight.setText("" + myProfile.getHeight());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mbti_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMBTI.setAdapter(adapter);
        spinnerMBTI.setSelection(adapter.getPosition(myProfile.getMbti()));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProfile.setAge(agePicker.getValue());
                myProfile.setHeight(Integer.parseInt(editHeight.getText().toString()));
                myProfile.setMbti(spinnerMBTI.getSelectedItem().toString());
                new CreateProfile().execute();
            }
        });
    }

    private class CreateProfile extends AsyncTask {
        private String result;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                RequestHttpURLConnection con = new RequestHttpURLConnection("edit");
                result = con.request(myProfile.getProfileJSONObject().toString());
                result = result.replace("\n", "");
                System.out.println(result);
                if (!result.equals("")) {
                    myProfile.setId(Integer.parseInt(result));
                }
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (isNew) {
                Intent mainIntent = new Intent(getApplication(), MainActivity.class);
                mainIntent.putExtra("profile", myProfile);
                startActivity(mainIntent);
            }
            finish();
        }
    }
}
