package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class userlist extends AppCompatActivity {
    String username;
    ListView list;
    ArrayList<String> user;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        getSupportActionBar().setTitle("User List");
        final Intent intent=getIntent();
        username =intent.getStringExtra("username") ;
        user=new ArrayList<String>();
        list=findViewById(R.id.list);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,user);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1=new Intent(getApplicationContext(),chatactivity.class);
                intent1.putExtra("username",user.get(position));
               finish();
               startActivity(intent1);
            }
        });

        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",username);
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser object :objects)
                        {
                            user.add(object.getUsername());

                        }



                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });






    }
}
