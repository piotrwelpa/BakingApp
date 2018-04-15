package com.example.piotr.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListAdapter extends
        RecyclerView.Adapter<MasterListAdapter.CakeViewHolder> {

    List<Cake> cakeList;

    public MasterListAdapter(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

    @NonNull
    @Override
    public CakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cake_item, parent, false);
        CakeViewHolder cakeHolder = new CakeViewHolder(v);
        return cakeHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CakeViewHolder holder, int position) {
        initCakeImage(holder, position);
        initCakeName(holder, position);
    }

    @Override
    public int getItemCount() {
        return cakeList.size();
    }

    private void initCakeImage(CakeViewHolder holder, int position) {
        if (isImagePathAvailable(position)) {
            loadImageFromPath(holder, position);
        } else {
            loadImageFromResource(holder);
        }
    }

    private boolean isImagePathAvailable(int position) {
        String imagePath = cakeList.get(position).getImage();
        return !(imagePath == null || imagePath.equals(""));
    }

    private void loadImageFromPath(CakeViewHolder holder, int position) {
        Picasso.get()
                .load(cakeList.get(position).getImage())
                .placeholder(R.drawable.cake_image_placeholder)
                .error(R.drawable.no_cake)
                .into(holder.cakeImage);
    }

    private void loadImageFromResource(CakeViewHolder holder) {
        Picasso.get()
                .load(R.drawable.no_cake)
                .into(holder.cakeImage);
    }

    private void initCakeName(CakeViewHolder holder, int position) {
        holder.cakeName.setText(cakeList.get(position).getName());
    }

    public static class CakeViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        @BindView(R.id.cake_image_item_cv)
        ImageView cakeImage;
        @BindView(R.id.cake_name_item_cv)
        TextView cakeName;
        @BindView(R.id.cake_item_cv)
        CardView cardItemView;

        public CakeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "test: "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
