package com.esp.note;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    private List<Item> itemList;
    private Context context;
    private AlertDialog.Builder builder;


    public ItemAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<Item>();
        builder = new AlertDialog.Builder(context);


    }

    public void add(Item item) {
        itemList.add(item);
    }

    public void remove(Item item) {
        itemList.remove(item);
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getContent());
        holder.frame.setBackgroundResource(item.getBackgroundResId());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        public int position;
        public TextView title;
        public TextView description;
        public LinearLayout frame;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_item);
            description = (TextView) itemView.findViewById(R.id.description_item);
            frame = (LinearLayout) itemView.findViewById(R.id.frame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditActivity.class);
                    Item item = itemList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", item.getTitle());
                    bundle.putString("desc", item.getContent());
                    bundle.putInt("bg",item.getBackgroundResId());
                    bundle.putInt("pos", position);
                    bundle.putInt("id",item.getId());
                    intent.putExtra("item", bundle);
                    ((Activity)context).startActivityForResult(intent, MainActivity.REQUEST_CODE);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //show dialog
                    final AlertDialog dialog;
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
                    Button cancel = (Button) dialogView.findViewById(R.id.delete_cancel_button);
                    Button confirm = (Button) dialogView.findViewById(R.id.delete_confirm_button);
                    builder.setView(dialogView);
                    dialog = builder.create();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemList.remove(position);
                            notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
    }
}
