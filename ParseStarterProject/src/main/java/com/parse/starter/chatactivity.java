package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class chatactivity extends AppCompatActivity {
    String chatwith;
    EditText editChatText;
    ArrayList<String> messages=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    public void send(View view)
    {
        final String message=editChatText.getText().toString();
        ParseObject parseObject=new ParseObject("message");
        parseObject.put("from", ParseUser.getCurrentUser().getUsername());
        parseObject.put("to",chatwith);
        parseObject.put("message",message);
        editChatText.setText("");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                messages.add(message);
                arrayAdapter.notifyDataSetChanged();

                }
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        Intent intent=getIntent();
        chatwith=intent.getStringExtra("username");
       editChatText=findViewById(R.id.editChatText);
        getSupportActionBar().setTitle("Chat with "+chatwith);

        ListView chatListView=findViewById(R.id.chatlist);
        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,messages);
        chatListView.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> query1=new ParseQuery<>("message");
                  query1.whereEqualTo("from",ParseUser.getCurrentUser().getUsername());
                  query1.whereEqualTo("to",chatwith);
        ParseQuery<ParseObject> query2=new ParseQuery<>("message");
        query2.whereEqualTo("to",ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("from",chatwith);
        List<ParseQuery<ParseObject>> queries=new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        ParseQuery<ParseObject> query=ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        messages.clear();
                        for(ParseObject object:objects)
                        {
                            String messageContent=(String)object.get("message");
                            if(object.getString("from").equals(ParseUser.getCurrentUser().getUsername()))
                            {
                               messageContent=">"+messageContent;
                            }
                            messages.add(messageContent);
                        }
                    arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



    }
}
