package grupo3.rcmm.wifi_indoor_positioning_client.api.Services;

import grupo3.rcmm.wifi_indoor_positioning_client.api.API_Client;
import grupo3.rcmm.wifi_indoor_positioning_client.api.API_Interface;
import grupo3.rcmm.wifi_indoor_positioning_client.api.Listeners.LocationServiceInteractor;
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
        API_Interface api_interface = API_Client.getClient();

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
