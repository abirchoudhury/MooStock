package com.moo.moostockm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CowAdapter extends ListAdapter<Cow, CowAdapter.CowHolder> {
    private OnItemClickListener listener;
    private String defaultImage = Environment.getExternalStorageDirectory().getAbsolutePath() +"/img_cow_basic.png";

    public CowAdapter() {
        super(DIFF_CALLBACK);
    }

    //Code to handle changes to the current cow
    private static final DiffUtil.ItemCallback<Cow> DIFF_CALLBACK = new DiffUtil.ItemCallback<Cow>() {
        @Override
        public boolean areItemsTheSame(Cow oldItem, Cow newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Cow oldItem, Cow newItem) {
            return oldItem.getId() == newItem.getId() &&
                    oldItem.getBirthweight().equals(newItem.getBirthweight()) &&
                    oldItem.getColor().equals(newItem.getBirthweight()) &&
                    oldItem.getDob().equals(newItem.getBirthweight()) &&
                    oldItem.getGender() == newItem.getGender() &&
                    oldItem.getImgurl().equals(newItem.getImgurl()) &&
                    oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public CowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cow_item, viewGroup, false);
        return new CowHolder(itemView);
    }

    //Binding the cow data to the cow_item.xml layout
    @Override
    public void onBindViewHolder(@NonNull CowHolder cowHolder, int i) {
        String defaultPhoto = defaultImage;
        Cow currentCow = getItem(i);
        cowHolder.textViewName.setText(currentCow.getName());
        File imgFile = new File(currentCow.getImgurl());
        String path = imgFile.getAbsolutePath();
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp != null){
            cowHolder.imageView.setImageBitmap(bmp);
        }
        else {
            imgFile = new File(defaultPhoto);
            Drawable dr = Drawable.createFromPath(imgFile.getAbsolutePath());
            cowHolder.imageView.setImageDrawable(dr);
        }

    }

    public Cow getCowAt(int position){
        return getItem(position);
    }

    //a holder to hold the cow_item.xml layout
    class CowHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private ImageView imageView;

        public CowHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.singleTxtView);
            imageView = itemView.findViewById(R.id.singleImgView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Cow cow);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
