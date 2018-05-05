package grupo3.rcmm.wifi_indoor_positioning_client.data.home.remote;


import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprinting;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FingerprintingService {
    @POST("fingerprint")
    Call<Void> postFingerprint(@Body Fingerprinting fingerprint);
}
