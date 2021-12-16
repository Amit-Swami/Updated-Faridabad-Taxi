package com.example.faridabadtaxirider;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.faridabadtaxirider.Callback.IFirebaseFailedListener;
import com.example.faridabadtaxirider.Callback.IFirebaseTripDetailListener;
import com.example.faridabadtaxirider.Common.Common;
import com.example.faridabadtaxirider.Model.EventBus.LoadTripDetailEvent;
import com.example.faridabadtaxirider.Model.TripPlanModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripDetailActivity extends AppCompatActivity implements IFirebaseTripDetailListener, IFirebaseFailedListener {

    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_origin)
    TextView txt_origin;
    @BindView(R.id.txt_destination)
    TextView txt_destination;
    @BindView(R.id.txt_base_fare)
    TextView txt_base_fare;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_distance)
    TextView txt_distance;
    @BindView(R.id.layout_detail)
    LinearLayout layout_detail;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.progress_ring)
    ProgressBar progress_ring;

    IFirebaseTripDetailListener tripDetailListener;
    IFirebaseFailedListener firebaseFailedListener;

    @OnClick(R.id.btn_back)
    void onBtnBackClick()
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        tripDetailListener = this;
        firebaseFailedListener = this;
    }

    @Override
    public void onTripDetailLoadSuccess(TripPlanModel tripPlanModel) {

        //Set Data
        txt_date.setText(tripPlanModel.getTimeText());
        txt_price.setText(new StringBuilder("$").append(tripPlanModel.getTotalFee()));
        txt_origin.setText(tripPlanModel.getOriginString());
        txt_destination.setText(tripPlanModel.getDestinationString());
        txt_base_fare.setText(String.valueOf(Common.BASE_FARE));
        txt_distance.setText(String.valueOf(tripPlanModel.getDistanceText()));
        txt_time.setText(String.valueOf(tripPlanModel.getDurationText()));
        //show layout
        layout_detail.setVisibility(View.VISIBLE);
        progress_ring.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Snackbar.make(main_layout,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {


        if (EventBus.getDefault().hasSubscriberForEvent(LoadTripDetailEvent.class))
            EventBus.getDefault().removeStickyEvent(LoadTripDetailEvent.class);


        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Subscribe(sticky = true,threadMode =  ThreadMode.MAIN)
    public void onLoadTripDetailEvent(LoadTripDetailEvent event)
    {
        FirebaseDatabase.getInstance()
                .getReference(Common.TRIP)
                .child(event.getTripKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            TripPlanModel model = snapshot.getValue(TripPlanModel.class);
                            tripDetailListener.onTripDetailLoadSuccess(model);
                        }
                        else
                            firebaseFailedListener.onFirebaseLoadFailed("Trip key not found!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        firebaseFailedListener.onFirebaseLoadFailed(error.getMessage());
                    }
                });


    }
}