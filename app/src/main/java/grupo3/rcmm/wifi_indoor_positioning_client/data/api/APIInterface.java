package grupo3.rcmm.wifi_indoor_positioning_client.data.api;

import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Fingerprint;
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Location;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("fingerprint")
    Call<Void> postFingerprint(@Body Fingerprint fingerprint);

    @POST("location")
    Call<Void> postLocation(@Body Location location);
}
