package com.example.gilberto.googlemapsrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.gilberto.googlemapsrecyclerview.adapter.PlaceListAdapter;
import com.example.gilberto.googlemapsrecyclerview.retrofit.PlaceService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceListActivity extends AppCompatActivity implements ListClickElement, Callback<Places> {
    RecyclerView mPlaceListRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PlaceListAdapter mAdapter;
    private List<Place> mPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);


        generatePlaceList();

    }

    private void setPlacesList() {
        mPlaceListRecyclerView  = (RecyclerView) findViewById(R.id.Recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mPlaceListRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PlaceListAdapter(mPlaceList, this);
        mPlaceListRecyclerView.setAdapter(mAdapter);
    }

    private void generatePlaceList() {
//        mPlaceList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://demo2355296.mockable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlaceService service = retrofit.create(PlaceService.class);
        Call<Places> call = service.getPlaces();
        call.enqueue(this);
    }

    @Override
    public void onItemClick(Place place) {
        DetailPlaceActivity.start(this, place);
    }

    @Override
    public void onResponse(Call<Places> call, Response<Places> response) {
        mPlaceList = response.body().places;
        setPlacesList();
    }

    @Override
    public void onFailure(Call<Places> call, Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
