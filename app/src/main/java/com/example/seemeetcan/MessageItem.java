package com.example.seemeetcan;
public class MessageItem {

    String name;
    String message;
    String time;

    public MessageItem(String name, String message, String time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }
    //firebase DB에 객체로 값을 읽어올 때
    public MessageItem() {
    }

    //Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}