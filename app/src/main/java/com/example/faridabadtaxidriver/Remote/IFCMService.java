package com.example.faridabadtaxidriver.Remote;

import com.example.faridabadtaxidriver.Model.FCMResponse;
import com.example.faridabadtaxidriver.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAo39zN0k:APA91bGriUEJJBVUS6GxflR_AksEIlsJcQFRnrOUZ1dWteBWgo39T97MlH2Q7js7IOa6GH-kIAhFVOnV-arwQ26RMGupsS8lIkR15YjSBMzdTOKlketWRinK4GxhrKohRSfiVtLm7xMZ"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);

}
