package jaber.hygieneapp;

/**
 * Created by jaber on 12/03/2016.
 * this is my Shop class with all its attributes, setters and getters
 */
public class Shop {
    private String id;
    private String BusinessName;
    private String AddressLine1;
    private String AddressLine2;
    private String AddressLine3;
    private String PostCode;
    private String RatingValue;
    private String RatingDate;
    private String Longitude;
    private String Latitude;
    private String DistanceKM;

    public Shop(){}
    public Shop(String id, String businessName, String addressLine1, String addressLine2, String addressLine3, String postCode, String ratingValue, String ratingDate, String longitude, String latitude, String DistanceKM) {
        this.id = id;
        BusinessName = businessName;
        AddressLine1 = addressLine1;
        AddressLine2 = addressLine2;
        AddressLine3 = addressLine3;
        PostCode = postCode;
        RatingValue = ratingValue;
        RatingDate = ratingDate;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getDistanceKM() {
        return DistanceKM;
    }

    public void setDistanceKM(String distanceKM) {
        DistanceKM = distanceKM;
    }

    public String getAddressLine3() {
        return AddressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        AddressLine3 = addressLine3;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getRatingValue() {
        return RatingValue;
    }

    public void setRatingValue(String ratingValue) {
        RatingValue = ratingValue;
    }

    public String getRatingDate() {
        return RatingDate;
    }

    public void setRatingDate(String ratingDate) {
        RatingDate = ratingDate;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }
}
