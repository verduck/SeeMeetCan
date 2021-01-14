package com.example.seemeetcan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private String CHAT_NAUUID;
    private Profile myProfile;

    private ListView chat_listview;
    private EditText chat_Message;
    private Button chat_send;

    ChatAdapter adapter;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;

    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef,chatRoom;

    ArrayList<MessageItem> messageItems=new ArrayList<>();  // 메세지 저장 어레이 리스트트 써야댐?



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 위젯 ID 참조
        chat_listview = (ListView) findViewById(R.id.chat_Listview);
        chat_Message = (EditText) findViewById(R.id.chat_msg);
        chat_send = (Button) findViewById(R.id.chat_sent);


        CHAT_NAUUID = getIntent().getStringExtra("UUID");
        myProfile = getIntent().getParcelableExtra("my_profile");


        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= firebaseDatabase.getReference("chat");
        chatRoom =  firebaseDatabase.getReference("chat").child("chatRoom");

        // 채팅 방 입장
        openChat(CHAT_NAUUID);
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_Message.getText().toString().equals(""))
                    return;

                //  ChatDTO chat = new ChatDTO(USER_NAME, chat_Message.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                //hatRef.child(CHAT_NAUUID).push().setValue(chat); // 데이터 푸쉬
                //  chat_Message.setText(""); //입력창 초기화

                //메세지 작성 시간 문자열로..
                Calendar calendar = Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

                //firebase DB에 저장할 값(MessageItem객체) 설정
                MessageItem messageItem = new MessageItem(myProfile.getName(), chat_Message.getText().toString(), time);
                //'chat'노드에 MessageItem객체를 통해

                chatRoom=chatRef.child(CHAT_NAUUID);
               chatRoom.push().setValue(messageItem);
                chat_Message.setText(""); //입력창 초기화


                //소프트키패드를 안보이도록..
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                //처음 시작할때 EditText가 다른 뷰들보다 우선시 되어 포커스를 받아 버림.
                //즉, 시작부터 소프트 키패드가 올라와 있음.
            } });
    }


   /* private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }*/
    //uuid 는 chat 방 번호 id
    // 메시지 전송 버튼에 대한 클릭 리스너 지정


    private void openChat(String CHAT_NAUUID) {
        // 리스트 어댑터 생성 및 세팅
      // final ArrayAdapter<String> adapter

             //   = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        adapter=new ChatAdapter(messageItems,getLayoutInflater(), myProfile);
        chat_listview.setAdapter(adapter);

        chatRoom=chatRef.child(CHAT_NAUUID);
        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        chatRoom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                MessageItem messageItem= dataSnapshot.getValue(MessageItem.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(messageItem);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                chat_listview.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}