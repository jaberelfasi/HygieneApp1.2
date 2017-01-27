package jaber.hygieneapp;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener{

    //variable declarations
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int GPS_ERROR_REQUEST = 9001;
    static LocationClient mLocationClient;
    public static android.app.FragmentManager fragmentManager;
    String lastNameSearched="";
    String lastPostCodeSearched="";
    String lastCitySearched="";

    //http request declarations
    String op;
    String param;
    String url;
    RadioGroup rg;
    RadioButton rb;
    TextView tv;
    int tag;//to determine whether the search includes distance or not 1 or 0





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servesesOk()){
            setContentView(R.layout.activity_main);
            // initialising the object of the map FragmentManager.
            fragmentManager = getFragmentManager();
            try{
                mLocationClient = new LocationClient(this, this, this);
                mLocationClient.connect();
            }catch(Exception e){
                Toast.makeText(this, "Map is not available", Toast.LENGTH_SHORT).show();
            }

        }else{
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Service is not ok", Toast.LENGTH_SHORT).show();
            //if service is not available this toast message will get displayed.
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);//placing the views in the tabs

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //prevents keyboard from showing on the startup of the app


    }

    //adds the map fragment and the list fragment to the tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_list(), "List");
        adapter.addFragment(new Fragment_map(), "Map");
        viewPager.setAdapter(adapter);
    }

    //the Viewer Adapter class is being declared
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*/http request code:------------------------------------------------------------*/

    //when users type their search term in the text edit of the map view
    public void getBySearchTermMap(View v) {
        hideSoftKeyboard(v);
        rg = (RadioGroup) findViewById(R.id.RadioButtonsMap);
        int selected = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(selected);
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        String searchTerm = tv.getText().toString();
        String strRb = rb.getText().toString();
        switch (strRb) {
            case "Name":
                getByName(searchTerm);
                break;
            case "Post Code":
                getByPostCode(searchTerm);
                break;
            case "City":
                getByCity(searchTerm);
                break;
            case "GPS":
                getByLocation();
                break;
            case "Recent":getRecent();
                break;
            default:
                Toast.makeText(this, "Something wrong with selection Radio buttons", Toast.LENGTH_SHORT).show();

        }
    }

    //when users type their search term in the text edit of the list view
    public void getBySearchTermList(View v) {
        hideSoftKeyboard(v);
        rg = (RadioGroup) findViewById(R.id.RadioButtonsList);
        int selected = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(selected);
        tv = (TextView) findViewById(R.id.etSearchTermList);
        String searchTerm = tv.getText().toString();
        String strRb = rb.getText().toString();
        switch (strRb) {
            case "Name":
                getByName(searchTerm);
                break;
            case "Post Code":
                getByPostCode(searchTerm);
                break;
            case "City":
                getByCity(searchTerm);
                break;
            case "GPS":
                getByLocation();
                break;
            case "Recent":getRecent();
                break;
            default:
                Toast.makeText(this, "Something wrong with selection Radio buttons", Toast.LENGTH_SHORT).show();

        }
    }

    //sends request url to the method that does the http request
    public void getByName(String name) {
        lastNameSearched=name;//validation functionality: keeps a record of the last searched name
        fillOtherSide(lastNameSearched);//validation functionality: fills text edit on radio button
                                        //change
        //checks for validation and if passes url request gets passed to the method that
        //does the http request
        if (name.equals("") || name.equals("Your Search Goes Here")) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        } else if(name.length()<3){
            Toast.makeText(this, "Please Type 3 Or More Characters", Toast.LENGTH_SHORT).show();
        } else {
            param = "&name=" + name;
            op = "?op=s_name";
            url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php" + op + param;
            tag=0;
            sendRequest(url);
        }

    }

    //gets results based on postcode parameter, the same way the previous method works
    public void getByPostCode(String postcode) {
        lastPostCodeSearched=postcode;
        fillOtherSide(lastPostCodeSearched);
        if (postcode.equals("") || postcode.equals("Your Search Goes Here")) {
            Toast.makeText(this, "Please enter a PostCode", Toast.LENGTH_SHORT).show();
        } else if(postcode.length()<3){
            Toast.makeText(this, "Please Type 3 Or More Characters", Toast.LENGTH_SHORT).show();
        } else {
            param = "&postcode=" + postcode;
            op = "?op=s_postcode";
            url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php" + op + param;
            tag=0;
            sendRequest(url);
        }
    }

    //gets results by finding the city's lat and lng
    public void getByCity(String city) {
        lastCitySearched=city;
        fillOtherSide(lastCitySearched);
        if (city.equals("") || city.equals("Your Search Goes Here")) {
            Toast.makeText(this, "Please enter a City Name", Toast.LENGTH_SHORT).show();
        } else if(city.length()<3){
            Toast.makeText(this, "Please Type 3 Or More Characters", Toast.LENGTH_SHORT).show();
        } else {
            //finds the city's lat and lng
            Geocoder gc = new Geocoder(this);
            try {
                List<Address> list = gc.getFromLocationName(city, 1);
                Address add = list.get(0);
                String localily = add.getLocality();

                Toast.makeText(this, localily, Toast.LENGTH_SHORT).show();

                double lat = add.getLatitude();
                double lng = add.getLongitude();

                getByLatLng(lat, lng);//generates url and sends it to http request method

            } catch (Exception c) {
                Toast.makeText(this, "something wrong :(", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //finds the lat and lng of the current location, and sends them to be processed for the http request
    public void getByLocation() {
        fillOtherSide("");
        Location currentLocation = mLocationClient.getLastLocation();
        if(currentLocation==null){
            Toast.makeText(this,"current location not available", Toast.LENGTH_SHORT).show();
        }else{
            double lat = lat = currentLocation.getLatitude();
            double lng = lng = currentLocation.getLongitude();
            getByLatLng(lat, lng);//generates url and sends it to http request method
        }


    }


    /*when passing the Lat and Lng to the url the returned set of shops will most likely have
    * the same lat and lng thus they will be displayed in the exact location on the map.
    * also the distance returned does not measure the queried (lat and lng) with respect to the
    * current location since there is no passed parameter for the current location, thus the
    * returned distance is not actual. for example when sending Lat and Lng of London the returned
    * distance from the current location is less than a Kilometer*/
    public void getByLatLng(double lat, double lng) {
        url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php" + "?op=s_loc&lat=" + lat + "&long=" + lng;
        tag=1;
        sendRequest(url);
    }

    //passes the recent parameter in the url to the http request method.
    public void getRecent(){
        fillOtherSide("");
        url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=s_recent";
        tag=0;
        sendRequest(url);
    }

    //sending http request to the server using Volley library
    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },//json string is passed to the showJSON method to be parsed
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        //Concurrency: RequestQueue Makes the Request Concurrent
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*--------------------Json Manipulation-----------------------------------------*/
    private void showJSON(String json) {
        ParseJSON pj = new ParseJSON(json);//instantiate an instance (pj) from my ParseJSON class
        List<Shop> allShops = new ArrayList<Shop>();//instantiate a list of objects (allShops) of my class Shop
        allShops = pj.getAllShopsforDis(tag);//tag variable is to determine whether the search includes distance or not 1 or 0
                                             //my allShops list of objects gets assigned the list of the returned shops
        if(allShops.size()>0){//to insure that allShops has actually got assigned at least one record
            ShopList shopsAdapter = new ShopList(this, allShops, tag);//adapter gets instantiated
            ListView listView = (ListView)findViewById(R.id.listView);//the listview gets instantiated

            listView.setAdapter(shopsAdapter);//adaper of list shop gets allocated to the listview

            Fragment_map.dropMarkersOnMap(allShops);//allShops list of objects is sent to a method in
                                                    //my Fragment_map class, the method drops the pins on the map
        }else{
            Toast.makeText(this, "0 Records were found", Toast.LENGTH_SHORT).show();
        }
    }
    /*-----------------------some validation ---------------------------------------------------*/
    //these methods are to control the behaviour of the various buttons when clicked in my views
    public void eraseSearchTermList(View v) {
        EditText et = (EditText) findViewById(R.id.etSearchTermList);
        et.setText("");
    }

    public void eraseSearchTermMap(View v) {
        EditText et = (EditText) findViewById(R.id.etSearchTermMap);
        et.setText("");
    }

    public void recentClicked(View v) {
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setEnabled(false);
        tv.setText("");
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setEnabled(false);
        tv.setText("");
        rb = (RadioButton)findViewById(R.id.rbByRecentList);
        rb.setChecked(true);
        rb = (RadioButton)findViewById(R.id.rbByRecentMap);
        rb.setChecked(true);
    }
    public void gpsClicked(View v) {
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setEnabled(false);
        tv.setText("");
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setEnabled(false);
        tv.setText("");
        rb = (RadioButton)findViewById(R.id.rbByGpsList);
        rb.setChecked(true);
        rb = (RadioButton)findViewById(R.id.rbByGpsMap);
        rb.setChecked(true);
    }

    public void cityClicked(View v){
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setEnabled(true);
        tv.setText(lastCitySearched);
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setEnabled(true);
        tv.setText(lastCitySearched);
        rb = (RadioButton)findViewById(R.id.rbByCityList);
        rb.setChecked(true);
        rb = (RadioButton)findViewById(R.id.rbByCityMap);
        rb.setChecked(true);
    }
    public void postCodeClicked(View v){
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setEnabled(true);
        tv.setText(lastPostCodeSearched);
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setEnabled(true);
        tv.setText(lastPostCodeSearched);
        rb = (RadioButton)findViewById(R.id.rbByPostCodeList);
        rb.setChecked(true);
        rb = (RadioButton)findViewById(R.id.rbByPostCodeMap);
        rb.setChecked(true);
    }
    public void nameClicked(View v){
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setEnabled(true);
        tv.setText(lastNameSearched);
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setEnabled(true);
        tv.setText(lastNameSearched);
        rb = (RadioButton)findViewById(R.id.rbByNameList);
        rb.setChecked(true);
        rb = (RadioButton)findViewById(R.id.rbByNameMap);
        rb.setChecked(true);
    }

    public void fillOtherSide(String term){
        tv = (TextView) findViewById(R.id.etSearchTermList);
        tv.setText(term);
        tv = (TextView) findViewById(R.id.etSearchTermMap);
        tv.setText(term);
    }


    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

   /*----------------------------Maps------------------------------------*/

    //checks whether google play services is available which is required for the map to load
    public boolean servesesOk() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERROR_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "can not connect to google play services", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    /*-------------------------------------Implemented Methods---------------------------------------*/
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}