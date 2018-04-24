package grupo3.rcmm.wifi_indoor_positioning_client.api;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API_Client {
    private static final String BASE_URL = "https://direccion_api/"; //TODO

    private static API_Interface service;

    public static API_Interface getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            service = retrofit.create(API_Interface.class);
        }
        return service;
    }
}
