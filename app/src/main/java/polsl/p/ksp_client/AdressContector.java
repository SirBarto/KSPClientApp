package polsl.p.ksp_client;

public class AdressContector {

    private String POMIAR_URL;
    private String PORT;

    public AdressContector(String POMIAR_URL, String PORT) {
        this.POMIAR_URL = POMIAR_URL;
        this.PORT = PORT;
    }

    public String getPOMIAR_URL(String POMIAR_URL) {
        return this.POMIAR_URL;
    }

    public String getPORT(String PORT) {
        return this.PORT;
    }

    public void setPOMIAR_URL(String POMIAR_URL) {
        this.POMIAR_URL = POMIAR_URL;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    @Override
    public String toString() {
        return "http://" + this.POMIAR_URL + ":" + this.PORT + "/test";
    }
}
