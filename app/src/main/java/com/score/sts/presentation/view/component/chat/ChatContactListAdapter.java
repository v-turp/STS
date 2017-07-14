package com.score.sts.presentation.view.component.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Who Dat on 7/9/2017.
 */

public class ChatContactListAdapter extends RecyclerView.Adapter <ChatContactListAdapter.ChatContactViewHolder>{

    @Override
    public ChatContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ChatContactViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ChatContactViewHolder extends RecyclerView.ViewHolder {

        public ChatContactViewHolder(View itemView) {
            super(itemView);
        }
    }
}
