package polsl.p.ksp_client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Measurement implements Serializable {

    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("pressure")
    @Expose
    private Double pressure;

    public Measurement(Double temperature, Double humidity, Double preassure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = preassure;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "T: " + this.temperature + " H: " + this.humidity + " P: " + this.pressure;
    }
}
