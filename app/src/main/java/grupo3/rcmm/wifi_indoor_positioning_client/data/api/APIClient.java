package grupo3.rcmm.wifi_indoor_positioning_client.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://direccion_api/"; //TODO

    private static APIInterface service;

    public static APIInterface getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            service = retrofit.create(APIInterface.class);
        }
        return service;
    }
}
