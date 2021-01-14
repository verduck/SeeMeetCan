package com.example.seemeetcan;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profile implements Parcelable, Comparable<Profile> {
    private int id;
    private String studentId;
    private String name;
    private boolean gender;
    private int age;
    private int height;
    private String mbti;
    private String introduction;
    private ArrayList<Integer> favoriteIds = new ArrayList<>();

    public Profile() {
        //picture = ContextCompat.getDrawable(null, R.drawable.ic_baseline_face_24);
        name = "홍길동";
    }

    public Profile(int id, String studentId, String name) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
    }

    public Profile(String studentId, String name) {
        this.id = 0;
        this.studentId = studentId;
        this.name = name;
    }

    public Profile(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("Id");
        studentId = jsonObject.getString("StudentId");
        name = jsonObject.getString("Name");
        gender = jsonObject.getBoolean("Gender");
        age = jsonObject.getInt("Age");
        height = jsonObject.getInt("Height");
        mbti = jsonObject.getString("MBTI");
        if (!jsonObject.isNull("FavoriteId")) {
            JSONArray jsonArray = jsonObject.getJSONArray("FavoriteId");

            for (int i = 0; i < jsonArray.length(); i++) {
                favoriteIds.add(jsonArray.getInt(i));
            }
        }
        introduction = "소개글이 없습니다.";
    }

    protected Profile(Parcel in) {
        id = in.readInt();
        studentId = in.readString();
        name = in.readString();
        gender = in.readBoolean();
        age = in.readInt();
        height = in.readInt();
        mbti = in.readString();
        introduction = in.readString();
        favoriteIds = in.readArrayList(Integer.class.getClassLoader());
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public String getMbti() {
        return mbti;
    }

    public String getIntroduction() {
        return introduction;
    }

    public ArrayList<Integer> getFavoriteIds() {
        return favoriteIds;
    }

    public void replace(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("Id");
        studentId = jsonObject.getString("StudentId");
        name = jsonObject.getString("Name");
        gender = jsonObject.getBoolean("Gender");
        age = jsonObject.getInt("Age");
        height = jsonObject.getInt("Height");
        mbti = jsonObject.getString("MBTI");
        favoriteIds.removeAll(favoriteIds);
        if (!jsonObject.isNull("FavoriteId")) {
            JSONArray jsonArray = jsonObject.getJSONArray("FavoriteId");

            for (int i = 0; i < jsonArray.length(); i++) {
                favoriteIds.add(jsonArray.getInt(i));
            }
        }
    }

    public JSONObject getProfileJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Id", id);
        jsonObject.put("StudentId", studentId);
        jsonObject.put("Name", name);
        jsonObject.put("Gender", gender);
        jsonObject.put("Age", age);
        jsonObject.put("Height", height);
        jsonObject.put("MBTI", mbti);
        JSONArray jsonArray = new JSONArray();
        for (int i : favoriteIds) {
            jsonArray.put(i);
        }
        jsonObject.put("FavoriteId", jsonArray);
        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(studentId);
        parcel.writeString(name);
        parcel.writeBoolean(gender);
        parcel.writeInt(age);
        parcel.writeInt(height);
        parcel.writeString(mbti);
        parcel.writeString(introduction);
        parcel.writeList(favoriteIds);
    }

    @Override
    public int compareTo(Profile profile) {
        return this.name.compareTo(profile.getName());
    }
}
