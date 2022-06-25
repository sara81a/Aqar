package com.myapp.aqar.adapter;

import static com.myapp.aqar.constants.AppConstants.MANAGER;
import static com.myapp.aqar.constants.AppConstants.USER;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.aqar.R;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    List<House> houses;
    Activity activity;
    String comingFor;
    View.OnClickListener subscribe;
    private OnItemClickListener mClickListener;


    public HouseAdapter(List<House> houses, Activity activity, String comingFor, View.OnClickListener subscribe) {
        this.houses = houses;
        this.activity = activity;
        this.comingFor = comingFor;
        this.subscribe = subscribe;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subscribed, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {

        if(comingFor.equals(USER)){
            holder.subscribeCard.setVisibility(View.VISIBLE);
        }else if(comingFor.equals(MANAGER)){
            holder.subscribeCard.setVisibility(View.GONE);
        }
        House house = houses.get(position);

        Bitmap bmp = BitmapFactory.decodeByteArray(house.getImage(), 0, house.getImage().length);
        holder.image.setImageBitmap(bmp);

        holder.location.setText(Utils.getLocationText(activity,house.getLatitude(),house.getLongitude()));
        holder.title.setText(house.getTitle());

        holder.subscribe.setOnClickListener(v -> {
            v.setTag(houses.get(position));
            subscribe.onClick(v);
        });
    }

    public void setmClickListener(OnItemClickListener mClickListener){
        this.mClickListener = mClickListener;
    }
    @Override
    public int getItemCount() {
        return houses.size();
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.home_img)
        ImageView image;
        @BindView(R.id.home_title)
        TextView title;
        @BindView(R.id.home_location)
        TextView location;

        @BindView(R.id.tv_subscribe)
        TextView subscribe;

        @BindView(R.id.subscribe_card)
        CardView subscribeCard;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener != null){
                mClickListener.onSubscribeClick(v,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onSubscribeClick(View view, int position);
    }
}
