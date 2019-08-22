package polsl.p.ksp_client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonMeasurementApi {

    @GET("/")
    Call<List<Measurement>> getMeasurement();
}
