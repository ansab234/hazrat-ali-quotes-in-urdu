package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ansab.hazrataliquotesinurdu.R;
import com.facebook.ads.NativeAdLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import activities.ads.AdsConstant;
import activities.ads.AdsManager;
import activities.full_image_Activity;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ImageViewHolder>{

    private Context mContext;
    private List<imageClass> mimageClass;

    public imageAdapter(Context mContext, List<imageClass> mimageClass) {
        this.mContext = mContext;
        this.mimageClass = mimageClass;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_images,parent,false);
       return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
         imageClass mclass = mimageClass.get(position);
        Picasso.get().load(mclass.getUrl())
             //   .placeholder(R.layout.default_pic)
                .fit().centerCrop().into(holder.imageView);
        if(position!=0 &&position % AdsConstant.nativeDisplay ==0){
            AdsManager.loadNative((Activity) mContext, holder.nativeAdLayout);
        }else{
            holder.nativeAdLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mimageClass.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {

        public ImageView imageView;
        public NativeAdLayout nativeAdLayout;
        CardView cardView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= itemView.findViewById(R.id.item);
            nativeAdLayout= itemView.findViewById(R.id.nativeAd);
            cardView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image_view_upload);


        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, full_image_Activity.class);
            intent.putExtra("images", mimageClass.get(getAdapterPosition()).url);
            mContext.startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }
}
