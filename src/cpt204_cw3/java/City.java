package cpt204_cw3.java;
public class City {

    private String cityName;
    private String Interest;

    public City(String cityName, String Interest) {
        this.cityName = cityName;
        this.Interest = Interest;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setInterest(String Interest) {
        this.Interest = Interest;
    }

    public String getCityName() {
        return cityName;
    }

    public String getInterest() {
        return Interest;
    }

    @Override
    public String toString() {
        return cityName; // return cityName
    }
}
