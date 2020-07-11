import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;

public class Start{

    public static void main(String args[]){
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();

        LatLng location = new LatLng(33.781556,-84.407480);
        String query = "";

        TextSearchRequest request = PlacesApi.textSearchQuery(context, query, location);
        System.out.println(request);
    }
}
