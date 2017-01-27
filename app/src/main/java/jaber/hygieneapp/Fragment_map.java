package jaber.hygieneapp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaber on 11/03/2016.
 */
public class Fragment_map extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener{


    private View view;
    Context thiscontext;
    private static GoogleMap mMap;
    static Marker mMarker;
    LocationClient mLocationClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();

        if (container == null) {
            return null;
        }

        //inflate the layout with this fragment
        view = (LinearLayout) inflater.inflate(R.layout.fragment_map, container, false);

        mLocationClient = new LocationClient(thiscontext, this, this);
        mLocationClient.connect();

        setUpMapIfNeeded(); // For setting up the MapFragment

        return view;
    }


    public static void setUpMapIfNeeded() {

        if (mMap == null) {
            mMap = ((MapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.map)).getMap();
        }
    }

    /*To remove the fragment on destroy*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            getActivity().getFragmentManager().beginTransaction()
                    .remove(mapFragment).commit();
    }

    //to drop markers on map when called
    public static void dropMarkersOnMap(List<Shop> allShops){
        ArrayList<Marker> markers = new ArrayList<Marker>();
        String title;
        if(mMarker!=null){
            mMarker.remove();

        }
        double lat= Double.parseDouble(allShops.get(0).getLatitude());
        double lng= Double.parseDouble(allShops.get(0).getLongitude());

        LatLng ll = new LatLng(lat, lng);

        mMap.clear();
        for(int i=0; i<allShops.size(); i++){
            String rating = allShops.get(i).getRatingValue();
            lat= Double.parseDouble(allShops.get(i).getLatitude());
            lng= Double.parseDouble(allShops.get(i).getLongitude());

            //this switch statement decides which pin image to be displayed on the map for
            //each record
            switch(rating){
                case "-1": mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(lat, lng))
                            .title(allShops.get(i).getBusinessName())
                            .snippet(allShops.get(i).getPostCode())
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.awaitingpin)));
                    break;
                case "1":   mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(lat, lng))
                            .title(allShops.get(i).getBusinessName())
                            .snippet(allShops.get(i).getPostCode())
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.onepin)));
                    break;
                case "2":   mMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng))
                        .title(allShops.get(i).getBusinessName())
                        .snippet(allShops.get(i).getPostCode())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.twopin)));
                    break;
                case "3":mMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng))
                        .title(allShops.get(i).getBusinessName())
                        .snippet(allShops.get(i).getPostCode())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.threepin)));
                    break;
                case "4":mMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng))
                        .title(allShops.get(i).getBusinessName())
                        .snippet(allShops.get(i).getPostCode())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fourpin)));
                    break;
                case "5":mMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng))
                        .title(allShops.get(i).getBusinessName())
                        .snippet(allShops.get(i).getPostCode())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fivepin)));
                    break;
                default:mMarker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng))
                        .title(allShops.get(i).getBusinessName())
                        .snippet(allShops.get(i).getPostCode())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.awaitingpin)));
            }


            markers.add(mMarker);
        }

        //to specify the boundaries of the map to be displayed based on the location of the pins
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker i : markers) {
            builder.include(i.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding =0; // sets the offsets from the edges of the map in pixels
        CameraUpdate cupdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMarker.showInfoWindow();
        mMap.animateCamera(cupdate);
    }


    /*-------------------------Implemented Methods---------------------------------*/
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(thiscontext, "connected", Toast.LENGTH_SHORT).show();
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(60000);
        //recommends to redo every 1 minute == 60,000 millisecond!!! to reduce battery consumption
        request.setFastestInterval(60000);

        mLocationClient.requestLocationUpdates(request, this);
        Location currentLocation = mLocationClient.getLastLocation();
        if(currentLocation!=null){//we need to check that the location is not null for the app not to crash
                                  //when connected the current location is found and the map is centered to it
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 14.0f));
        }


    }

    @Override
    public void onDisconnected() {
        Toast.makeText(thiscontext, "Disconected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(thiscontext, "conection Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        String msg="Location: "+ location.getLatitude()+","+location.getLongitude();
        Toast.makeText(thiscontext,msg,Toast.LENGTH_SHORT).show();
    }
}