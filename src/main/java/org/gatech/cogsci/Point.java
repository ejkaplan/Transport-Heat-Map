package org.gatech.cogsci;
import com.google.maps.model.LatLng;

public class Point{

    private LatLng location;
    private long timeInSeconds;
    private double weight;

    public Point(LatLng location, double weight, long timeInSeconds){
        this.location = location;
        this.weight = weight;
        this.timeInSeconds = timeInSeconds;
    }

    public LatLng getLocation() { return this.location; }

    public double getWeight(){
        return this.weight;
    }

    public void setLocation(LatLng location){
        this.location = location;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public String toString(){
        return location.toString() + ", " + weight;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }
}
