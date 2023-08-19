package com.example.birdsofafeather;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder>{
    private final List<String> sessions;

    public SessionViewAdapter(List<String> sessions){
        super();
        this.sessions = sessions;
    }


    @NonNull
    @Override
    public SessionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.session_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSession(sessions, position);
    }

    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView sessionNameView;
        private List<String> session;

        ViewHolder(View itemView) {
            super(itemView);
            this.sessionNameView = itemView.findViewById(R.id.session_rowname);
            itemView.setOnClickListener(this);
        }

        public void setSession(List<String> session, int index) {
            this.session = session;
            this.sessionNameView.setText(session.get(index));
        }

        @Override
        public void onClick(View view) {

            //implement pulling from sharedpref
            Context context = view.getContext();
            Intent intent = new Intent(context, MainAppActivity.class);
            intent.putExtra("title", sessionNameView.getText().toString());

            context.startActivity(intent);
        }
    }
}
