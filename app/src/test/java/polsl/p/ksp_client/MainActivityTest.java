package polsl.p.ksp_client;

import com.android.volley.toolbox.JsonArrayRequest;
import static org.junit.Assert.assertEquals;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class MainActivityTest {

    public static String toJSon(Measurement measurement){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("temperature",measurement.getTemperature());
            jsonObject.put("humidity",measurement.getHumidity());
            jsonObject.put("pressure",measurement.getPressure());
            System.out.println(jsonObject.toString());
            return jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public  void contructorMeasurement(){
       Measurement measurement = new Measurement(1.1,2.2,3.3);

        assertEquals("1.1", measurement.getTemperature().toString());
        assertEquals("2.2", measurement.getHumidity().toString());
        assertEquals("3.3", measurement.getPressure().toString());

    }

    String urlForGetRequest = "http://10.0.2.2:8080/test";
    String actual = "{\"temperature\":1.1,\"humidity\":1.2,\"pressure\":3.2}";


    @Test
    public void onCreate() {
    }

    @Test
    public void getMeasurementRequest() {

    }

    @Test
    public void sendConfirmRequest() {
    }

    @Test
    public void readAdressDestination() {
    }

    @Test
    public void drawingChart() {
    }

    @Test
    public void onClick() {
    }
}