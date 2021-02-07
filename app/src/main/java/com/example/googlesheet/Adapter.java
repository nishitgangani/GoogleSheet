package com.example.googlesheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> data;
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private removeItem removeItem;

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }

    public Adapter(Context context, ArrayList<Object> data,removeItem removeItem) {
        this.context = context;
        this.data = data;
        this.removeItem = removeItem;
    }




    public class AdViewHolder extends RecyclerView.ViewHolder {

        private TemplateView myTemplate;

        public AdViewHolder(View view) {
            super(view);

            myTemplate = view.findViewById(R.id.my_template);
        }
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = data.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View nativeAdView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_native_ad, parent, false);

                return new Adapter.AdViewHolder(nativeAdView);
            case MENU_ITEM_VIEW_TYPE:
                // Fall through.
            default:
                View view = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);

                return new MyViewholder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        int viewType = getItemViewType(position);

        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                final AdViewHolder adViewHolder = (AdViewHolder)holder;

                AdLoader.Builder adbuilder = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110");

                adbuilder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        adViewHolder.myTemplate.setNativeAd(unifiedNativeAd);
                    }
                });

                AdLoader adLoader = adbuilder.build();
                adLoader.loadAd(new AdRequest.Builder().build());

                break;
            case MENU_ITEM_VIEW_TYPE:
                // fall through
            default:
                MyViewholder myViewHolder = (MyViewholder) holder;
                final Sheet1 user = (Sheet1) data.get(position);
                ((MyViewholder) holder).id.setText(user.getName());
                ((MyViewholder) holder).name.setText(user.getCountry());

                ((MyViewholder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        removeItem.removeItemfromArray(position);
                        return true;
                    }
                });
        }



    }

/*    public void onBindViewHolder(@NonNull Adapter.MyViewholder holder, int i) {

        Sheet1 user = data.get(i);

            holder.id.setText(user.getName());
            holder.name.setText(user.getCountry());

    }*/

    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface removeItem{

        void removeItemfromArray(int position);
    }

}
