package com.example.faridabadtaxirider.Services;

import com.example.faridabadtaxirider.Common.Common;
import com.example.faridabadtaxirider.Model.EventBus.DeclineRequestAndRemoveTripFromDriver;
import com.example.faridabadtaxirider.Model.EventBus.DeclineRequestFromDriver;
import com.example.faridabadtaxirider.Model.EventBus.DriverAcceptTripEvent;
import com.example.faridabadtaxirider.Model.EventBus.DriverCompleteTripEvent;
import com.example.faridabadtaxirider.Utils.UserUtils;
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
            UserUtils.updateToken(this, s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> dataRecv = remoteMessage.getData();
        if (dataRecv != null) {
            if (dataRecv.get(Common.NOTI_TITLE) != null) {

                if (dataRecv.get(Common.NOTI_TITLE).equals(Common.REQUEST_DRIVER_DECLINE)) {

                    EventBus.getDefault().postSticky(new DeclineRequestFromDriver());

                }
                else if (dataRecv.get(Common.NOTI_TITLE).equals(Common.REQUEST_DRIVER_DECLINE_AND_REMOVE_TRIP)) {

                    EventBus.getDefault().postSticky(new DeclineRequestAndRemoveTripFromDriver());

                }

                else if (dataRecv.get(Common.NOTI_TITLE).equals(Common.REQUEST_DRIVER_ACCEPT)) {

                    String tripKey = dataRecv.get(Common.TRIP_KEY);
                    EventBus.getDefault().postSticky(new DriverAcceptTripEvent(tripKey));

                }

                else if (dataRecv.get(Common.NOTI_TITLE).equals(Common.RIDER_COMPLETE_TRIP)) {

                    String tripKey = dataRecv.get(Common.TRIP_KEY);
                    EventBus.getDefault().postSticky(new DriverCompleteTripEvent(tripKey));

                }

                else
                    Common.showNotification(this, new Random().nextInt(),
                            dataRecv.get(Common.NOTI_TITLE),
                            dataRecv.get(Common.NOTI_CONTENT),
                            null);
            }
        }
    }
}
