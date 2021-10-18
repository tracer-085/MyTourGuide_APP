package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytourguide.R;
import com.mytourguide.ScrollingPlaceActivity;

import java.util.List;

import model.HotPlace;

public class HotPlaceAdapter extends RecyclerView.Adapter<HotPlaceAdapter.HotPlaceViewHolder> {

    Context context;
    List<HotPlace> hotPlaceList;

    public HotPlaceAdapter(Context context, List<HotPlace> hotPlaceList) {
        this.context = context;
        this.hotPlaceList = hotPlaceList;
    }

    @NonNull
    @Override
    public HotPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotplace_row ,parent, false);
        return new HotPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotPlaceViewHolder holder, int position) {
        holder.placeName.setText(hotPlaceList.get(position).getPlaceName());
        holder.countryName.setText(hotPlaceList.get(position).getCountryName());
        holder.provinceName.setText(hotPlaceList.get(position).getProvinceName());
        holder.cityName.setText(hotPlaceList.get(position).getCityName());
        holder.cityImage.setImageResource(hotPlaceList.get(position).getImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent = new Intent(view.getContext(), ScrollingPlaceActivity.class);
                intent.putExtra("title",holder.placeName.getText().toString());
                intent.putExtra("cityName",holder.cityName.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotPlaceList.size();
    }

    public static final class HotPlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView cityImage;
        TextView cityName;
        TextView provinceName;
        TextView countryName;
        TextView placeName;

        public HotPlaceViewHolder(@NonNull View itemView) {

            super(itemView);
            cityImage = itemView.findViewById(R.id.place_image);
            cityName = itemView.findViewById(R.id.city_name);
            provinceName = itemView.findViewById(R.id.province_name);
            countryName = itemView.findViewById(R.id.country_name);
            placeName = itemView.findViewById(R.id.palce_name);
        }
    }
}
