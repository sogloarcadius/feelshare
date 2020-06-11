package io.sogloarcadius.feelshare.model;

public class TopMood {

    private String country;
    private Integer topMoodID;
    private String topMoodName;
    private Integer topMoodImage;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTopMoodID() {
        return topMoodID;
    }

    public void setTopMoodID(Integer topMoodID) {
        this.topMoodID = topMoodID;
    }

    public String getTopMoodName() {
        return topMoodName;
    }

    public void setTopMoodName(String topMoodName) {
        this.topMoodName = topMoodName;
    }

    public Integer getTopMoodImage() {
        return topMoodImage;
    }

    public void setTopMoodImage(Integer topMoodImage) {
        this.topMoodImage = topMoodImage;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "country='" + country + '\'' +
                ", topMoodID=" + topMoodID +
                ", topMoodName='" + topMoodName + '\'' +
                ", topMoodImage=" + topMoodImage +
                '}';
    }
}
