package in.alertmeu.a4a.models;

public class SendNotificationModelDAO {
    String id="";
    String name="";
    String imagepath="";
    String country_code="";
    String latitude="";
    String longitude="";
    String fcm_id="";
    String checked_status;
    private boolean isSelected = false;
    public SendNotificationModelDAO() {

    }

    public SendNotificationModelDAO(String id, String name, String imagepath, String country_code, String latitude, String longitude, String fcm_id) {
        this.id = id;
        this.name = name;
        this.imagepath = imagepath;
        this.country_code = country_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fcm_id = fcm_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getChecked_status() {
        return checked_status;
    }

    public void setChecked_status(String checked_status) {
        this.checked_status = checked_status;
    }
}
