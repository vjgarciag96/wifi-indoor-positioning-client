package grupo3.rcmm.wifi_indoor_positioning_client.data.api.listeners;

import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Location;
import retrofit2.Call;
import retrofit2.Response;

public interface LocationServiceInteractor {
    interface CallBackListener{
        void onResponseLocation(Call<Void> call, Response<Void> response);

        void onFailureLocation(Call<Void> call, Throwable t);
    }

    void postLocation(Location location);
}
