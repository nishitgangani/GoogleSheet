package com.example.googlesheet;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.removeItem {

    private RecyclerView rview;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=159--pHYbfd56xjdLpNau0Db58Qz8L2r8kGf-WY8l8Ww&sheet=Sheet1";
    private ArrayList<Object> sheet1List = new ArrayList<>();

    private Adapter adapter;
    private RequestQueue requestQueue;

    private AdLoader adLoader;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public static final int NUMBER_OF_ADS = 5;
    private ModelMain modelMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rview = findViewById(R.id.rview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        mNativeAds.clear();



        loadNativeAds();

        rview.setHasFixedSize(true);
        adapter = new Adapter(this,sheet1List,this);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapter);
        JsonRequest();

//        https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=159--pHYbfd56xjdLpNau0Db58Qz8L2r8kGf-WY8l8Ww&sheet=Sheet1
        rview.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                mNativeAds.clear();
                onRefreshRecycle();

                loadNativeAds();

            }
        });
    }



    private void JsonRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.serializeNulls().create();

                 modelMain = gson.fromJson(response,ModelMain.class);

                sheet1List.clear();

                sheet1List.addAll(modelMain.getSheet1());

                adapter.notifyDataSetChanged();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        loadNativeAds();

    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110");
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), sheet1List.size());
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        //int offset = (videoList.size() / mNativeAds.size()) + 1;
        //int offset = 3;
        int index = 3;
        /*for (UnifiedNativeAd ad : mNativeAds) {
            videoList.add(index, ad);
            index = index + offset;
        }*/

        for (int i = 0; i <= mNativeAds.size(); i++) {
            if (index < sheet1List.size()) {
                sheet1List.add(index, mNativeAds.get(i));
                index = index + 4;
            } else {
                break;
            }
        }
    }

    @Override
    public void removeItemfromArray(int position) {

        sheet1List.remove(position);
        adapter.notifyDataSetChanged();


    }

    private void onRefreshRecycle() {
        sheet1List.clear();

        sheet1List.addAll(modelMain.getSheet1());

        adapter.notifyDataSetChanged();
    }


}