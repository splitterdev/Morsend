package com.morsend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.morsend.function.ActionCaller;
import com.morsend.util.MessageLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecentMessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_messages);

        RecentMessagesActivity activity = this;

        ArrayList<String> messages = new ArrayList<>(ActionCaller.getMessageLog().getMessages());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.activity_recent_message_view,
                R.id.itemLabel,
                messages
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof LinearLayout) {
                    LinearLayout layout = (LinearLayout) view;

                    String message = getItem(position);

                    Button editButton = layout.findViewById(R.id.itemEditMessage);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.setEditTextMessage(message);
                            ActionCaller.changeActivity(activity, MainActivity.class);
                        }
                    });

                    Button sendButton = layout.findViewById(R.id.itemSendMessage);
                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActionCaller.changeActivity(activity, activity.getClass());
                            ActionCaller.sendMessage(getItem(position), v.getContext());
                        }
                    });

                    TextView labelView = layout.findViewById(R.id.itemLabel);
                    labelView.setText(message);
                }
                return view;
            }
        };

        ListView recentMessagesList = findViewById(R.id.messageLog);
        recentMessagesList.setAdapter(adapter);

        Button goBackButton = findViewById(R.id.goBack_2);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.changeActivity(activity, MainActivity.class);
            }
        });

        /*TextView messageLogItem = findViewById(R.id.messageLog);
        MessageLog messageLog = ActionCaller.getMessageLog();
        StringBuilder sBuilder = new StringBuilder();
        for (String message : messageLog.getMessages()) {
            sBuilder.append(message);
            sBuilder.append("\n");
        }
        messageLogItem.setText(sBuilder.toString());*/
    }
}