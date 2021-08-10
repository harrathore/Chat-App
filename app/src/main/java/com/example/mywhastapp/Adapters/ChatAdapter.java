package com.example.mywhastapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywhastapp.ChatDetailActivity;
import com.example.mywhastapp.Models.MessagesModel;
import com.example.mywhastapp.R;
import com.example.mywhastapp.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessagesModel> list;
    Context context;
    String recId;
    int SENDER_VIEW_TYPE = 1, RECIEVER_VIEW_TYPE = 2;
    public ChatAdapter(ArrayList<MessagesModel> list, Context context)
    {
        this.list = list;
        this. context = context;
    }

    public ChatAdapter(ArrayList<MessagesModel> list, Context context, String recId) {
        this.list = list;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
       if(list.get(position).getMsgId().equals(FirebaseAuth.getInstance().getUid()))
           return SENDER_VIEW_TYPE;
       else
           return RECIEVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

      MessagesModel message = list.get(position);                          //Getting Current message from the list sent from caller Activity(


      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {         //Long Press on particular Message(Delete)
          @Override
          public boolean onLongClick(View v) {
              new AlertDialog.Builder(context)
                      .setTitle("Delete").setMessage("Are You Sure You want to Delete this message")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {     // Delete the mesage
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              FirebaseDatabase database = FirebaseDatabase.getInstance();
                              String senderRoom =  FirebaseAuth.getInstance().getUid() + recId;
                              database.getReference().child("Chats").child(senderRoom).
                                      child(message.getMessageId()).setValue(null);               //Deleting message
                              Toast.makeText(context, "Message Deleted", Toast.LENGTH_LONG).show();
                          }
                      }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();                                     //Simply dismiss the dialog when User press No
                  }
              }).show();
              return false;
          }
      });


      if(holder.getClass() == SenderViewHolder.class){
          ((SenderViewHolder)holder).senderMsg.setText(message.getMessage());
          ((SenderViewHolder)holder).senderTime.setText(DateFormat.format("MM/dd/yyyy", new Date(message.getTimeStamp())).toString());
      }
      else{
          ((RecieverViewHolder)holder).recieverMsg.setText(message.getMessage());
          ((RecieverViewHolder)holder).recieverTime.setText(DateFormat.format("MM/dd/yyyy", new Date(message.getTimeStamp())).toString());
      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView recieverMsg, recieverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = (TextView) itemView.findViewById(R.id.tv_reciever_msg);
            recieverTime = (TextView) itemView.findViewById(R.id.tv_recieve_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = (TextView) itemView.findViewById(R.id.tv_sender_msg);
            senderTime = (TextView) itemView.findViewById(R.id.tv_sender_time);
        }
    }
}
