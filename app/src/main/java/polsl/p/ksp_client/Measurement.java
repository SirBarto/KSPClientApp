package polsl.p.ksp_client;

public class Measurement {

    private Double temperature;
    private Double humidity;
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
