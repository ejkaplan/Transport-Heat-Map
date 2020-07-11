public class Point{

    private float lat;
    private float lon;
    private float weight;

    public Point(float lat, float lon, float weight){
        self.lat = lat;
        self.lon = lon;
        self.weight = weight;
    }

    public float getLat(){
        return self.lat;
    }

    public float getLon(){
        return self.lon;
    }

    public float getWeight(){
        return self.weight;
    }

    public void setLat(float lat){
        self.lat = lat;
    }

    public void setLon(float lon){
        self.lon = lon;
    }

    public void setWeight(float weight){
        self.weight = weight;
    }
}