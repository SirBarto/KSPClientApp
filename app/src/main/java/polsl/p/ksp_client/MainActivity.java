package polsl.p.ksp_client;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.annotations.SerializedName;
import com.jjoe64.graphview.series.DataPoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list;
    private com.github.mikephil.charting.charts.LineChart mchart;

    Button btnConnect, btnDisconect;
    Switch aSwitch;
    EditText textIp, textPort;
    TextView textViewIpAdress;

    @SerializedName("measurement")
    List<Measurement> pomiarList = new ArrayList<>();
    private ArrayAdapter<Measurement> pomiarArrayAdapter;

    RequestQueue queue;
    Retrofit retrofit;
    //adres wprowadzony statycznie
    //String urlForGetRequest = "http://10.0.2.2:8080/test";

    Double temperature, humidity, pressure;
    AdressContector adressContector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = findViewById(R.id.buttonConnect);
        btnDisconect = findViewById(R.id.buttonDisconect);
        btnConnect.setOnClickListener(this);
        btnDisconect.setOnClickListener(this);
        aSwitch = findViewById(R.id.switchStatus);
        aSwitch.setClickable(false);
        list = findViewById(R.id.valuesList);

        textViewIpAdress = findViewById(R.id.textView);
/*lokalny test
        pomiarList.add(new Measurement(1.1, 1.3, 1.2));
        pomiarList.add(new Measurement(2.1, 2.3, 2.2));
        pomiarList.add(new Measurement(3.1, 3.3, 3.2));

        pomiarArrayAdapter = new ArrayAdapter<Measurement>(this, R.layout.listdefine,pomiarList);
        list.setAdapter(pomiarArrayAdapter);
*/
        //other version
 /*       Measurement[] pomiars = {
          new Measurement(1.1,1.2,1.3),
          new Measurement(2.1,2.2,2.3),
          new Measurement(3.1,3.2,3.3),
          new Measurement(4.1,4.2,4.3)
        };

        ArrayAdapter<Measurement> adapter = new ArrayAdapter<>(this,R.layout.listdefine,pomiars);
        list.setAdapter(adapter);
*/


        pomiarArrayAdapter = new ArrayAdapter<Measurement>(this, R.layout.listdefine, pomiarList);

        //Graph static
/*
        GraphView graphView = findViewById(R.id.chart);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPoint());
        graphView.addSeries(series);
*/

    }

    private void getMeasurementRequest(String urlForGetRequest) {
        String urlForGetRequestt = "http://myjson.com/17oorz/";
        retrofit = new Retrofit.Builder()
                .baseUrl(urlForGetRequest)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonMeasurementApi jsonMeasurementApi = retrofit.create(JsonMeasurementApi.class);
        Call<List<Measurement>> call = jsonMeasurementApi.getMeasurement();

        call.enqueue(new Callback<List<Measurement>>() {
            @Override
            public void onResponse(Call<List<Measurement>> call, retrofit2.Response<List<Measurement>> response) {
                if(!response.isSuccessful()){
                    Log.e("Info","Information response: "+response.code());
                    Log.e("Info","Information response body: "+response.body());
                    return;
                }
                List<Measurement> measurements = response.body();

                for(Measurement measurement : measurements){
                    temperature = measurement.getTemperature();
                    humidity = measurement.getHumidity();
                    pressure = measurement.getPressure();

                    list.setAdapter(pomiarArrayAdapter);
                    pomiarList.add(new Measurement(temperature, humidity, pressure));
                   // drawingChart(temperature, humidity, pressure,counter);
                }
            }
            @Override
            public void onFailure(Call<List<Measurement>> call, Throwable t) {
                Log.e("blad","blad json: "+t.getMessage());
                Log.e("blad","blad json: "+t.toString());
            }
        });
                    /*temperature = response.getDouble("temperature");
                    humidity = response.getDouble("humidity");
                    pressure = response.getDouble("pressure");
                    counter++;
                    drawingChart(temperature, humidity, pressure,counter);
                    pomiarList.add(new Measurement(temperature, humidity, pressure));
                    list.setAdapter(pomiarArrayAdapter);*/
       // sendConfirmRequest(urlForGetRequest);
    }

    //Potwierdzenie otrzymania ciągu danych od serwera, wiem można by zrobić lepiej ;)
    public void sendConfirmRequest(String urlForGetRequest) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForGetRequest, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Boolean isOk = true;
                String clientNameTest = "KSP_CLIENT";
                String received = isOk.toString();
                params.put("received", received);
                params.put("client", clientNameTest);
                return params;
            }
        };
       queue.add(stringRequest);
    }

    public String readAdressDestination() {
        textIp = findViewById(R.id.editTextIp);
        textPort = findViewById(R.id.editTextPort);

        String POMIAR_URL = textIp.getText().toString();
        String PORT = textPort.getText().toString();

        adressContector = new AdressContector(POMIAR_URL,PORT);
        textViewIpAdress.setText(adressContector.toString());/*to removed, for test only*/
        return adressContector.toString();
    }

    public void drawingChart(Double temperature, Double humidity, Double pressure, int counter) {
        mchart = (LineChart) findViewById(R.id.lineChart);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(true);

        for (int i = 0; i < counter; i++) {
            ArrayList<Entry> yValuesTemperature = new ArrayList<>();
            yValuesTemperature.add(new Entry(i, temperature.floatValue()));
       /* yValuesTemperature.add(new Entry(1,50f));
        yValuesTemperature.add(new Entry(2,70f));
        yValuesTemperature.add(new Entry(3,30f));
        yValuesTemperature.add(new Entry(4,10f));*/

            ArrayList<Entry> yValuesHumidity = new ArrayList<>();
            yValuesHumidity.add(new Entry(i, humidity.floatValue()));

            ArrayList<Entry> yValuesPressure = new ArrayList<>();
            yValuesPressure.add(new Entry(i, pressure.floatValue()));

            LineDataSet dataSetTemperature = new LineDataSet(yValuesTemperature, "Data Set Temperature");
            dataSetTemperature.setFillAlpha(110);
            dataSetTemperature.setColor(Color.RED);
            dataSetTemperature.setLineWidth(2f);

            LineDataSet dataSetHumidity = new LineDataSet(yValuesHumidity, "Data Set Humidity");
            dataSetHumidity.setFillAlpha(110);
            dataSetHumidity.setColor(Color.GREEN);
            dataSetHumidity.setLineWidth(2f);

            LineDataSet dataSetPressure = new LineDataSet(yValuesPressure, "Data Set Pressure");
            dataSetPressure.setFillAlpha(110);
            dataSetPressure.setColor(Color.BLUE);
            dataSetPressure.setLineWidth(2f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSetTemperature);
            dataSets.add(dataSetHumidity);
            dataSets.add(dataSetPressure);

            LineData data = new LineData(dataSets);
            mchart.setData(data);
        }
    }

    private DataPoint[] getDataPoint() {
        DataPoint[] dp = new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(2, 2),
                new DataPoint(3, 1),
                new DataPoint(5, 4),
                new DataPoint(6, 3),
        };
        return (dp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConnect:
                aSwitch.setChecked(true);
                btnConnect.setEnabled(false);
                btnDisconect.setEnabled(true);
              //getMeasurementRequest(readAdressDestination());
                btnConnect.postDelayed(runnable,3000); /*wywołanie metody co 3s w celu odpytania serwera o nowe dane*/
                textViewIpAdress.setVisibility(TextView.VISIBLE);
                break;
            case R.id.buttonDisconect:
                aSwitch.setChecked(false);
                btnDisconect.setEnabled(false);
                btnConnect.setEnabled(true);
                textViewIpAdress.setVisibility(TextView.INVISIBLE);
                break;
        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btnConnect.postDelayed(this,3000);
            getMeasurementRequest(readAdressDestination());
        }
    };

}
