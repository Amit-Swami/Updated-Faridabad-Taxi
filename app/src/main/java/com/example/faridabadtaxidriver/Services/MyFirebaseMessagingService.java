package com.example.faridabadtaxidriver.Services;

import android.graphics.Color;

import com.example.faridabadtaxidriver.Common;
import com.example.faridabadtaxidriver.Model.EventBus.DriverRequestReceived;
import com.example.faridabadtaxidriver.Utils.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            UserUtils.updateToken(this,s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> dataRecv = remoteMessage.getData();
        if (dataRecv != null)
        {
            if (dataRecv.get(Common.NOTI_TITLE).equals(Common.REQUEST_DRIVER_TITLE))
            {
                DriverRequestReceived driverRequestReceived = new DriverRequestReceived();
                driverRequestReceived.setKey(dataRecv.get(Common.RIDER_KEY));
                driverRequestReceived.setPickupLocation(dataRecv.get(Common.RIDER_PICKUP_LOCATION));
                driverRequestReceived.setDestinationLocationString(dataRecv.get(Common.RIDER_PICKUP_LOCATION_STRING));
                driverRequestReceived.setDestinationLocation(dataRecv.get(Common.RIDER_DESTINATION));
                driverRequestReceived.setDestinationLocationString(dataRecv.get(Common.RIDER_DESTINATION_STRING));

                //Value and fee
                driverRequestReceived.setDistanceValue(Integer.parseInt(dataRecv.get(Common.RIDER_DISTANCE_VALUE)));
                driverRequestReceived.setDurationValue(Integer.parseInt(dataRecv.get(Common.RIDER_TIME_VALUE)));
                driverRequestReceived.setTotalFee(Double.parseDouble(dataRecv.get(Common.RIDER_TOTAL_FEE)));
                driverRequestReceived.setDistanceText(dataRecv.get(Common.RIDER_DISTANCE_TEXT));
                driverRequestReceived.setDurationText(dataRecv.get(Common.RIDER_TIME_TEXT));

                EventBus.getDefault().postSticky(driverRequestReceived);
            }
            else {
                Common.showNotification(this, new Random().nextInt(),
                        dataRecv.get(Common.NOTI_TITLE),
                        dataRecv.get(Common.NOTI_CONTENT),
                        null);
            }
        }
    }
}
