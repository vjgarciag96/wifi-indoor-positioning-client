package grupo3.rcmm.wifi_indoor_positioning_client.api.Services;

import grupo3.rcmm.wifi_indoor_positioning_client.api.API_Client;
import grupo3.rcmm.wifi_indoor_positioning_client.api.API_Interface;
import grupo3.rcmm.wifi_indoor_positioning_client.api.Listeners.FingerprintServiceInteractor;
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Fingerprint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FingerprintService implements FingerprintServiceInteractor {
    private CallBackListener callBackListener;

    public FingerprintService(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public void postFingerprint(Fingerprint fingerprint) {
        API_Interface api_interface = API_Client.getClient();

        Call<Void> call = api_interface.postFingerprint(fingerprint);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callBackListener.onResponseFingerprint(call, response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBackListener.onFailureFingerprint(call, t);
            }
        });
    }
}
