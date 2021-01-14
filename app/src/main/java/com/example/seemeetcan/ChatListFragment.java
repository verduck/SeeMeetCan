package com.example.seemeetcan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

public class ChatListFragment extends Fragment {
    private View view;
    private ListView listView;
    private ArrayList<ChatRoom> chats = new ArrayList<>();
    private ChatViewAdpater chatViewAdpater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatViewAdpater = new ChatViewAdpater();
    }

    @Override
    public void onResume() {
        super.onResume();

        new RequestChatList().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_chat);

        return view;
    }

    class RequestChatList extends AsyncTask {
        ChatRoom c;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result;
                JSONObject jsonObject;
                MainActivity activity = (MainActivity) getActivity();
                RequestHttpURLConnection con = new RequestHttpURLConnection("chatlist");
                String json;
                jsonObject = new JSONObject();
                jsonObject.accumulate("UserId", activity.getMyProfile().getId());
                json = jsonObject.toString();

                result = con.request(json);
                jsonObject = new JSONObject(result);
                System.out.println(jsonObject.toString());
                chats.removeAll(chats);
                if (!jsonObject.isNull("List")) {
                    JSONArray arr = jsonObject.getJSONArray("List");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject tmp = (JSONObject) arr.get(i);
                        c = new ChatRoom(tmp);


                        c.getUUID(); // 이 UUID 값에 해당되는 채팅방에 있는 마지막 메시지랑 시간을 가져와서 저장
                        // 아무데이터가 없으면 아무것도 하지 않음
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference chatMessage = firebaseDatabase.getReference("chat").child(c.getUUIDToString());
                        Query q = chatMessage.limitToLast(1);

                        q.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                MessageItem item = snapshot.getValue(MessageItem.class);
                                c.setLastMessage(item.getMessage());
                                c.setTimeStamp(item.getTime());
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        chats.add(c);
                    }
                }
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            chatViewAdpater.setItems(chats);
            listView.setAdapter(chatViewAdpater);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity activity = (MainActivity) getActivity();
                    ChatRoom selectedChatRoom = (ChatRoom) adapterView.getAdapter().getItem(i);

                    System.out.println(selectedChatRoom.getUUIDToString());


                    // 채팅방 클릭시 채팅 액티비티 인텐트 실행
                    Intent chatIntent = new Intent(activity.getApplication(), ChatActivity.class);
                    chatIntent.putExtra("UUID", selectedChatRoom.getUUIDToString());
                    chatIntent.putExtra("my_profile", activity.getMyProfile());
                    startActivity(chatIntent);
                }
            });
        }
    }
}