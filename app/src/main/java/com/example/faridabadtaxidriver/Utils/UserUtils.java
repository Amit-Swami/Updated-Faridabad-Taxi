package com.example.faridabadtaxidriver.Utils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.faridabadtaxidriver.Common;
import com.example.faridabadtaxidriver.Model.EventBus.NotifyToRiderEvent;
import com.example.faridabadtaxidriver.Model.FCMSendData;
import com.example.faridabadtaxidriver.Model.TokenModel;
import com.example.faridabadtaxidriver.R;
import com.example.faridabadtaxidriver.Remote.IFCMService;
import com.example.faridabadtaxidriver.Remote.RetrofitFCMClient;
import com.example.faridabadtaxidriver.Services.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserUtils {

    public static void updateUser(View view, Map<String, Object> updateData) {
        FirebaseDatabase.getInstance()
                .getReference(Common.DRIVER_INFO_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Snackbar.make(view,e.getMessage(),Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> Snackbar.make(view,"Update information successfully!",Snackbar.LENGTH_SHORT).show());
    }

    public static void updateToken(Context context, String token) {
        TokenModel tokenModel = new TokenModel(token);

        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(tokenModel)
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show()).addOnSuccessListener(aVoid -> {

                });

    }

    public static void sendDeclineRequest(View view, Context context, String key) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        //Get Token
        FirebaseDatabase
                .getInstance()
                .getReference(Common.TOKEN_REFERENCE)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);

                            Map<String,String> notificationData = new HashMap<>();
                            notificationData.put(Common.NOTI_TITLE,Common.REQUEST_DRIVER_DECLINE);
                            notificationData.put(Common.NOTI_CONTENT,"This message represent for action driver decline");
                            notificationData.put(Common.DRIVER_KEY,FirebaseAuth.getInstance().getCurrentUser().getUid());


                            FCMSendData fcmSendData = new FCMSendData(tokenModel.getToken(),notificationData);

                            compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(fcmResponse -> {
                                        if (fcmResponse.getSuccess() == 0)
                                        {
                                            compositeDisposable.clear();
                                            Snackbar.make(view,context.getString(R.string.decline_failed),Snackbar.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Snackbar.make(view,context.getString(R.string.decline_success),Snackbar.LENGTH_LONG).show();
                                        }
                                    }, throwable -> {
                                        compositeDisposable.clear();
                                        Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_LONG).show();
                                    }));
                        }
                        else
                        {
                            compositeDisposable.clear();
                            Snackbar.make(view,context.getString(R.string.token_not_found),Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        compositeDisposable.clear();
                        Snackbar.make(view,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    public static void sendAcceptRequestToRider(View view, Context context, String key, String tripNumberId) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        FirebaseDatabase
                .getInstance()
                .getReference(Common.TOKEN_REFERENCE)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);

                            Map<String,String> notificationData = new HashMap<>();
                            notificationData.put(Common.NOTI_TITLE,Common.REQUEST_DRIVER_ACCEPT);
                            notificationData.put(Common.NOTI_CONTENT,"This message represent for action driver accept");
                            notificationData.put(Common.DRIVER_KEY,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notificationData.put(Common.TRIP_KEY,tripNumberId);




                            FCMSendData fcmSendData = new FCMSendData(tokenModel.getToken(),notificationData);

                            compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(fcmResponse -> {
                                        if (fcmResponse.getSuccess() == 0)
                                        {
                                            compositeDisposable.clear();
                                            Snackbar.make(view,context.getString(R.string.accept_failed),Snackbar.LENGTH_LONG).show();
                                        }

                                    }, throwable -> {
                                        compositeDisposable.clear();
                                        Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_LONG).show();
                                    }));
                        }
                        else
                        {
                            compositeDisposable.clear();
                            Snackbar.make(view,context.getString(R.string.token_not_found),Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        compositeDisposable.clear();
                        Snackbar.make(view,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    public static void sendNotifyToRider(Context context, View view, String key) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        FirebaseDatabase
                .getInstance()
                .getReference(Common.TOKEN_REFERENCE)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);

                            Map<String,String> notificationData = new HashMap<>();
                            notificationData.put(Common.NOTI_TITLE,context.getString(R.string.driver_arrived));
                            notificationData.put(Common.NOTI_CONTENT,context.getString(R.string.your_driver_arrived));
                            notificationData.put(Common.DRIVER_KEY,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notificationData.put(Common.RIDER_KEY,key);




                            FCMSendData fcmSendData = new FCMSendData(tokenModel.getToken(),notificationData);

                            compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(fcmResponse -> {
                                        if (fcmResponse.getSuccess() == 0)
                                        {
                                            compositeDisposable.clear();
                                            Snackbar.make(view,context.getString(R.string.accept_failed),Snackbar.LENGTH_LONG).show();
                                        }
                                        else
                                            EventBus.getDefault().postSticky(new NotifyToRiderEvent());

                                    }, throwable -> {
                                        compositeDisposable.clear();
                                        Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_LONG).show();
                                    }));
                        }
                        else
                        {
                            compositeDisposable.clear();
                            Snackbar.make(view,context.getString(R.string.token_not_found),Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        compositeDisposable.clear();
                        Snackbar.make(view,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    public static void sendDeclineAndRemoveTripRequest(View view, Context context, String key, String tripNumberId) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        //First, remove trip from database
        FirebaseDatabase.getInstance()
                .getReference(Common.Trip)
                .child(tripNumberId)
                .removeValue()
                .addOnFailureListener(e -> {
                    Snackbar.make(view,e.getMessage(),Snackbar.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(aVoid -> {
                    //Delete success, send notification to rider app
                    FirebaseDatabase
                            .getInstance()
                            .getReference(Common.TOKEN_REFERENCE)
                            .child(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists())
                                    {
                                        TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);

                                        Map<String,String> notificationData = new HashMap<>();
                                        notificationData.put(Common.NOTI_TITLE,Common.REQUEST_DRIVER_DECLINE_AND_REMOVE_TRIP);
                                        notificationData.put(Common.NOTI_CONTENT,"This message represent for action driver decline");
                                        notificationData.put(Common.DRIVER_KEY,FirebaseAuth.getInstance().getCurrentUser().getUid());


                                        FCMSendData fcmSendData = new FCMSendData(tokenModel.getToken(),notificationData);

                                        compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(fcmResponse -> {
                                                    if (fcmResponse.getSuccess() == 0)
                                                    {
                                                        compositeDisposable.clear();
                                                        Snackbar.make(view,context.getString(R.string.decline_failed),Snackbar.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Snackbar.make(view,context.getString(R.string.decline_success),Snackbar.LENGTH_LONG).show();
                                                    }
                                                }, throwable -> {
                                                    compositeDisposable.clear();
                                                    Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_LONG).show();
                                                }));
                                    }
                                    else
                                    {
                                        compositeDisposable.clear();
                                        Snackbar.make(view,context.getString(R.string.token_not_found),Snackbar.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    compositeDisposable.clear();
                                    Snackbar.make(view,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                                }
                            });
                });


    }

    public static void sendCompleteTripToRider(View view, Context context, String key, String tripNumberId) {

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);


                    //update success, send notification to rider app
                    FirebaseDatabase
                            .getInstance()
                            .getReference(Common.TOKEN_REFERENCE)
                            .child(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists())
                                    {
                                        TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);

                                        Map<String,String> notificationData = new HashMap<>();
                                        notificationData.put(Common.NOTI_TITLE,Common.RIDER_COMPLETE_TRIP);
                                        notificationData.put(Common.NOTI_CONTENT,"This message represent for action driver complete trip");
                                        notificationData.put(Common.TRIP_KEY,tripNumberId);


                                        FCMSendData fcmSendData = new FCMSendData(tokenModel.getToken(),notificationData);

                                        compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(fcmResponse -> {
                                                    if (fcmResponse.getSuccess() == 0)
                                                    {
                                                        compositeDisposable.clear();
                                                        Snackbar.make(view,context.getString(R.string.complete_trip_failed),Snackbar.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Snackbar.make(view,context.getString(R.string.complete_trip_success),Snackbar.LENGTH_LONG).show();
                                                    }
                                                }, throwable -> {
                                                    compositeDisposable.clear();
                                                    Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_LONG).show();
                                                }));
                                    }
                                    else
                                    {
                                        compositeDisposable.clear();
                                        Snackbar.make(view,context.getString(R.string.token_not_found),Snackbar.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    compositeDisposable.clear();
                                    Snackbar.make(view,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                                }
                            });

    }
}