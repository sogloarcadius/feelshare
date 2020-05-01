package io.sogloarcadius.feelshare.model;


public class SaveMood  {

    private long pk; //current time in ms

    private String userID; //email (uniq)

    private Integer moodUID;

    private String country;


    public Integer getMoodUID() {
        return moodUID;
    }

    public void setMoodUID(Integer moodUID) {
        this.moodUID = moodUID;
    }

    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
