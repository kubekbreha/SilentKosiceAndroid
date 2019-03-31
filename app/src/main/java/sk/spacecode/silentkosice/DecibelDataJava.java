package sk.spacecode.silentkosice;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DecibelDataJava {

    public String timestamp;
    public String decibel;
    public String userNumber;
    public String lat;
    public String lon;


    public DecibelDataJava() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DecibelDataJava(String timestamp, String decibel, String userNumber, String lat, String lon) {
        this.timestamp = timestamp;
        this.decibel = decibel;
        this.userNumber = userNumber;
        this.lat = lat;
        this.lon = lon;
    }

}
