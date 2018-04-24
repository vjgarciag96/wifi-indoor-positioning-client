package grupo3.rcmm.wifi_indoor_positioning_client.api.Listeners;

import grupo3.rcmm.wifi_indoor_positioning_client.data.Fingerprint;
import retrofit2.Call;
import retrofit2.Response;

public interface FingerprintServiceInteractor {
    interface CallBackListener{
        void onResponseFingerprint(Call<Void> call, Response<Void> response);

        void onFailureFingerprint(Call<Void> call, Throwable t);
    }

    void postFingerprint(Fingerprint fingerprint);
}
