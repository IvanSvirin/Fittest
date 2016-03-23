package com.example.ivansv.fittest.controller;

import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivansv.fittest.R;
import com.example.ivansv.fittest.model.Datum;
import com.example.ivansv.fittest.view.ListFragment;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Datum> datums;
    private final ListFragment.OnListFragmentInteractionListener interactionListener;

    public RecyclerViewAdapter(List<Datum> datums, ListFragment.OnListFragmentInteractionListener interactionListener) {
        this.datums = datums;
        this.interactionListener = interactionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = datums.get(position);
        holder.titleTextView.setText(datums.get(position).getTitle());
        holder.textTextView.setText(datums.get(position).getText());
        holder.previewImageView.setImageDrawable(BitmapDrawable.createFromPath(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + datums.get(position).getV() + ".jpg"));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != interactionListener) {
                    interactionListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTextView;
        public final TextView textTextView;
        public final ImageView previewImageView;
        public Datum item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleTextView = (TextView) view.findViewById(R.id.title);
            textTextView = (TextView) view.findViewById(R.id.text);
            previewImageView = (ImageView) view.findViewById(R.id.preview);
        }
    }
}
