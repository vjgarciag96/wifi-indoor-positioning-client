package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.service.FingerprintingService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = "http://192.168.1.130:6969/";

    private static FingerprintingService service;

    public static FingerprintingService getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            service = retrofit.create(FingerprintingService.class);
        }
        return service;
    }
}
