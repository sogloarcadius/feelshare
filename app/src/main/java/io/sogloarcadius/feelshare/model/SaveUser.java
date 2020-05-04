package io.sogloarcadius.feelshare.model;

public class SaveUser {

    private String userName;
    private String userEmail;
    private String userID;
    private Boolean userEnableNewsLetter;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Boolean getUserEnableNewsLetter() {
        return userEnableNewsLetter;
    }

    public void setUserEnableNewsLetter(Boolean userEnableNewsLetter) {
        this.userEnableNewsLetter = userEnableNewsLetter;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
