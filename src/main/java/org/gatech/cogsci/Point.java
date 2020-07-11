package org.gatech.cogsci;
import com.google.maps.model.LatLng;

public class Point{

    private LatLng location;
    private float weight;

    public Point(LatLng location, float weight){
        this.location = location;
        this.weight = weight;
    }

    public LatLng getLocation() { return this.location; }

    public float getWeight(){
        return this.weight;
    }

    public void setLocation(LatLng location){
        this.location = location;
    }

    public void setWeight(float weight){
        this.weight = weight;
    }

    public String toString(){
        return location.toString() + ", " + Float.toString(weight);
    }
}
