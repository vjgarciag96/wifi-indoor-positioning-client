package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.service.FingerprintingService;
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.service.LocationService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = "http://192.168.1.130:6969/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static FingerprintingService getFingerprintingService() {
        return getClient().create(FingerprintingService.class);
    }

    public static LocationService getLocationService() {
        return getClient().create(LocationService.class);
    }
}
