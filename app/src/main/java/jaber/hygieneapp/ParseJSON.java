package jaber.hygieneapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseJSON {

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "BusinessName";
    public static final String KEY_Line1 = "AddressLine1";
    public static final String KEY_Line2 = "AddressLine2";
    public static final String KEY_Line3 = "AddressLine3";
    public static final String KEY_PC = "PostCode";
    public static final String KEY_RV = "RatingValue";
    public static final String KEY_RD = "RatingDate";
    public static final String KEY_Lng = "Longitude";
    public static final String KEY_Lat = "Latitude";
    public static final String KEY_Dis = "DistanceKM";



    private String json;

    Shop shop = null;

    public ParseJSON(String json){
        this.json = json;
    }

    private JSONArray shops = null;


    //generates a list of Shop objects
    public List<Shop> getAllShopsforDis(int tag){
        List<Shop>allShops = new ArrayList<Shop>();

        try {
            //an instance of JSONArray shops is instantiated with the json string
            shops = new JSONArray(json);
            JSONObject jo;//an instance of JSONObject is instantiated
            for(int i=0; i<shops.length();i++){
                //the loop iterates through all the JSONArray's elements
                //and assigns each element to the instantiated object from my Shop class (shop)
                jo = shops.getJSONObject(i);
                shop=new Shop();
                //in each iteration an object of Shop class is created again to be filled with a new object
                shop.setId(jo.getString(KEY_NAME));
                shop.setBusinessName(jo.getString(KEY_NAME));
                shop.setAddressLine1(jo.getString(KEY_Line1));
                shop.setAddressLine2(jo.getString(KEY_Line2));
                shop.setAddressLine3(jo.getString(KEY_Line3));
                shop.setPostCode(jo.getString(KEY_PC));
                shop.setRatingValue(jo.getString(KEY_RV));
                shop.setRatingDate(jo.getString(KEY_RD));
                shop.setLongitude(jo.getString(KEY_Lng));
                shop.setLatitude(jo.getString(KEY_Lat));
                if(tag==1)//if tag == 1 all keys must be added to the object
                shop.setDistanceKM(jo.getString(KEY_Dis));//else if tag == 0 key_dis may not be included
                allShops.add(shop);
                /*when the latitude and longitude are passed to the url as the parameters,
                * the search result contain some duplicated values, I investigated and figured
                * the problem is in the server side since i iterated through the JSONArray to
                * see the same problem*/
            }



            return allShops;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}