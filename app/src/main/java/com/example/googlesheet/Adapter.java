package com.example.googlesheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewholder> {

    private Context context;
    private List<Sheet1> data;

    public Adapter(Context context, List<Sheet1> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public Adapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewholder holder, int i) {

        Sheet1 user = data.get(i);

        holder.id.setText(user.getName());
        holder.name.setText(user.getCountry());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }


}
