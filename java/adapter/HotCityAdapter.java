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
import com.mytourguide.ScrollingCityActivity;

import java.util.List;

import model.HotCity;

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.HotCityViewHolder> {

    Context context;
    List<HotCity> hotCityList;

    public HotCityAdapter(Context context, List<HotCity> hotCityList) {
        this.context = context;
        this.hotCityList = hotCityList;
    }

    @NonNull
    @Override
    public HotCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotcity_row ,parent, false);
        return new HotCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotCityViewHolder holder, int position) {
        holder.countryName.setText(hotCityList.get(position).getCountryName());
        holder.provinceName.setText(hotCityList.get(position).getProvinceName());
        holder.cityName.setText(hotCityList.get(position).getCityName());
        holder.cityImage.setImageResource(hotCityList.get(position).getImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent = new Intent(view.getContext(), ScrollingCityActivity.class);
                intent.putExtra("title",holder.cityName.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotCityList.size();
    }

    public static final class HotCityViewHolder extends RecyclerView.ViewHolder {
        ImageView cityImage;
        TextView cityName;
        TextView provinceName;
        TextView countryName;

        public HotCityViewHolder(@NonNull View itemView) {

            super(itemView);
            cityImage = itemView.findViewById(R.id.place_image);
            cityName = itemView.findViewById(R.id.city_name);
            provinceName = itemView.findViewById(R.id.province_name);
            countryName = itemView.findViewById(R.id.country_name);
        }
    }
}
