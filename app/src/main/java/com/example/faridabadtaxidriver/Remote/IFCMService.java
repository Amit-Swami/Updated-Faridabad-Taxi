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
            "Authorization:key=Your key"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);

}
