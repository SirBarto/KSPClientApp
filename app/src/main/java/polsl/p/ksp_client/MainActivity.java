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

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.series.DataPoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list;
    private com.github.mikephil.charting.charts.LineChart mchart;

    private Button btnConnect, btnDisconect;
    private Switch aSwitch;
    public EditText textIp, textPort;
    private TextView textViewIpAdress;

    List<Measurement> surveyList = new ArrayList<>();
    private ArrayAdapter<Measurement> surveyArrayAdapter;

    ArrayList<ILineDataSet> dataSets;
    ArrayList<String> arrayCounter;

    private ArrayList<Entry> yValuesTemperature;
    private ArrayList<Entry> yValuesHumidity;
    private ArrayList<Entry> yValuesPressure;

    ValueFormatter formatter;
    XAxis xAxis;
    public Legend legend;

    RequestQueue queue;
    Retrofit retrofit;
    //adres wprowadzony statycznie
    //String urlForGetRequest = "http://10.0.2.2:8080/test";

    private Double temperature, humidity, pressure;
    AdressContector adressContector;
    int counter = 0;

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

        surveyArrayAdapter = new ArrayAdapter<Measurement>(this, R.layout.listdefine, surveyList);

        formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                //    return arrayCounter.get(((int)value)-1);
                return arrayCounter.get(0);
            }
        };

        dataSets = new ArrayList<>();
        yValuesTemperature = new ArrayList<>();
        yValuesPressure = new ArrayList<>();
        yValuesHumidity = new ArrayList<>();
        arrayCounter = new ArrayList<String>();
        mchart = findViewById(R.id.lineChart);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(true);
        xAxis = mchart.getXAxis();
    }

    //TODO GET JSON with Retrofit
    private void getMeasurementRequest(String urlForGetRequest) {
        String urlForGetRequestt = "http://myjson.com/ofirj/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(urlForGetRequest)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final JsonMeasurementApi jsonMeasurementApi = retrofit.create(JsonMeasurementApi.class);
        Call<List<Measurement>> call = jsonMeasurementApi.getMeasurement();

        Log.w("call", call.toString());
        call.enqueue(new Callback<List<Measurement>>() {
            @Override
            public void onResponse(Call<List<Measurement>> call, retrofit2.Response<List<Measurement>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Info", "Information response: " + response.code());
                    Log.i("Info", "Information response body: " + response.body());
                    return;
                }
                List<Measurement> measurements = response.body();

                for (Measurement measurement : measurements) {
                    temperature = measurement.getTemperature();
                    humidity = measurement.getHumidity();
                    pressure = measurement.getPressure();

                    list.setAdapter(surveyArrayAdapter);
                    if(surveyList.size()==11)
                        surveyList.clear();

                    surveyList.add(new Measurement(temperature, humidity, pressure));

                    counter++;
                    arrayCounter.add(String.valueOf(counter));

                    Log.i("info about list", list.toString());
                    Log.i("info about surveyList", surveyList.toString());
                    drawingChart(temperature, humidity, pressure, counter);
                }
            }

            @Override
            public void onFailure(Call<List<Measurement>> call, Throwable t) {
                Log.e("blad", "blad json: " + t.getMessage());
                Log.e("blad", "blad json: " + t.toString());
            }
        });
    }

    public String readAdressDestination() {
        textIp = findViewById(R.id.editTextIp);
        textPort = findViewById(R.id.editTextPort);

        String POMIAR_URL = textIp.getText().toString();
        String PORT = textPort.getText().toString();

        adressContector = new AdressContector(POMIAR_URL, PORT);
        textViewIpAdress.setText(adressContector.toString());/*to removed, for test only*/
        return adressContector.toString();
    }

    public void drawingChart(Double temperature, Double humidity, Double pressure, int counter) {

     /*   yValuesTemperature.add(new Entry(1,50f));
        yValuesTemperature.add(new Entry(2,70f));
        yValuesTemperature.add(new Entry(3,30f));
        yValuesTemperature.add(new Entry(4,10f));
*/
        //TODO dla kazdego i jest przypisywana ta sama pierwsza pobrana temperatura

        yValuesTemperature.add(new Entry(counter, temperature.floatValue()));
        yValuesHumidity.add(new Entry(counter, humidity.floatValue()));
        yValuesPressure.add(new Entry(counter, (pressure.floatValue()) / 1000));

        LineDataSet dataSetTemperature = new LineDataSet(yValuesTemperature, "Temperature");
        dataSetTemperature.setFillAlpha(110);
        dataSetTemperature.setColor(Color.RED);
        dataSetTemperature.setLineWidth(2f);

        LineDataSet dataSetHumidity = new LineDataSet(yValuesHumidity, "Humidity");
        dataSetHumidity.setFillAlpha(110);
        dataSetHumidity.setColor(Color.GREEN);
        dataSetHumidity.setLineWidth(2f);

        LineDataSet dataSetPressure = new LineDataSet(yValuesPressure, "Pressure kPa  ");
        dataSetPressure.setFillAlpha(110);
        dataSetPressure.setColor(Color.BLUE);
        dataSetPressure.setLineWidth(2f);

        dataSets.add(dataSetTemperature);
        dataSets.add(dataSetHumidity);
        dataSets.add(dataSetPressure);

        legend = mchart.getLegend();
        legend.setYOffset(40);
        legend.setForm(Legend.LegendForm.DEFAULT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        LineData data = new LineData(dataSets);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        mchart.setData(data);
        mchart.invalidate();
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
                btnConnect.postDelayed(runnable, 3000); /*wywołanie metody po 3s raz zapyta serwer o zestaw danych*/
                textViewIpAdress.setVisibility(TextView.VISIBLE);
                break;
            case R.id.buttonDisconect:
                aSwitch.setChecked(false);
                btnDisconect.setEnabled(false);
                btnConnect.setEnabled(true);
                textViewIpAdress.setVisibility(TextView.INVISIBLE);
                btnDisconect.removeCallbacks(runnable); //zatrzymanie pobierania danych z serwera
                break;
        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btnConnect.postDelayed(this, 3000);//co 3 sekundy pyta serwer o dane
            getMeasurementRequest(readAdressDestination());
        }
    };


}
