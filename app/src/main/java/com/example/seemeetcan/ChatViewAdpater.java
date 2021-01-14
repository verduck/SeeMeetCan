package com.example.seemeetcan;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class ChatViewAdpater extends BaseAdapter {
    private ArrayList<ChatRoom> chatList = new ArrayList<>();

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setItems(ArrayList<ChatRoom> items) {
        chatList = items;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_chat, viewGroup, false);
        }

        TextView tvChatName = view.findViewById(R.id.tv_chat_name);
        TextView tvChatLastMessage = view.findViewById(R.id.tv_chat_last_message);
        TextView tvTimeStamp = view.findViewById(R.id.tv_chat_timestamp);

        ChatRoom chatRoom = chatList.get(i);
        tvChatName.setText(chatRoom.getName());
        tvChatLastMessage.setText(chatRoom.getLastMessage());
        tvTimeStamp.setText(chatRoom.getTimeStamp());


        return view;
    }
}
