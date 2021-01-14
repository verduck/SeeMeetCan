package com.example.seemeetcan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class ChatRoom {
    private int id;
    private UUID uuid;
    private String name;
    private String lastMessage;
    private String timeStamp;

    public ChatRoom() {
        id = 0;
        uuid = UUID.randomUUID();
        name = "테스트 채팅방";
        lastMessage = "테스트중입니다.";
    }

    public ChatRoom(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("Id");
        uuid = UUID.nameUUIDFromBytes(jsonObject.getString("UUID").getBytes());
        name = jsonObject.getString("Name");
        lastMessage = jsonObject.getString("LastMessage");
        timeStamp = jsonObject.getString("TimeStamp");
    }

    public int getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUUIDToString() {
        return uuid.toString();
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setLastMessage(String message) {
        this.lastMessage = message;
    }

    public void setTimeStamp(String time) {
        this.timeStamp = time;
    }
}
