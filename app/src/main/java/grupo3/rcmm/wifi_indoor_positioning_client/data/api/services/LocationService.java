package grupo3.rcmm.wifi_indoor_positioning_client.data.api.services;

import grupo3.rcmm.wifi_indoor_positioning_client.data.api.APIClient;
import grupo3.rcmm.wifi_indoor_positioning_client.data.api.APIInterface;
import grupo3.rcmm.wifi_indoor_positioning_client.data.api.listeners.LocationServiceInteractor;
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService implements LocationServiceInteractor {
    private CallBackListener callBackListener;

    public LocationService(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public void postLocation(Location location) {
        APIInterface api_interface = APIClient.getClient();

        Call<Void> call = api_interface.postLocation(location);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callBackListener.onResponseLocation(call, response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBackListener.onFailureLocation(call, t);
            }
        });
    }
}
