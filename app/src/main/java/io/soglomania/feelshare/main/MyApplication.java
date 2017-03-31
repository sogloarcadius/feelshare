package io.soglomania.feelshare.main;

/**
 * Created by sogloarcadius on 23/03/17.
 */

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class MyApplication extends Application {

    SyncConfiguration syncConfiguration;


    private String[] moodsNames;
    private Integer[] moodsImages;
    private String[] moodsDesc;
    private Integer[] moodsUID;


    public static String TAG = "FeelShareMainActivity";
    public static String AUTH_URL = "http://ec2-34-248-57-168.eu-west-1.compute.amazonaws.com:9080/auth";
    public static String SERVER_URL = "realm://ec2-34-248-57-168.eu-west-1.compute.amazonaws.com:9080/~/default";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);


        Thread thread_configure_realm = new Thread(new Runnable() {
            @Override
            public void run() {
                SyncCredentials myCredentials = SyncCredentials.usernamePassword("feelshare@soglomania.io", "azerty", false);
                SyncUser user = SyncUser.login(myCredentials, MyApplication.AUTH_URL);
                Log.v(MyApplication.TAG, user.toJson());
                syncConfiguration = new SyncConfiguration.Builder(user, MyApplication.SERVER_URL).build();
                Realm.setDefaultConfiguration(syncConfiguration);
            }
        });

        thread_configure_realm.start();

        /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);*/

    }

    public String[] getMoodsNames() {
        return moodsNames;
    }

    public void setMoodsNames(String[] moodsNames) {
        this.moodsNames = moodsNames;
    }

    public Integer[] getMoodsImages() {
        return moodsImages;
    }

    public void setMoodsImages(Integer[] moodsImages) {
        this.moodsImages = moodsImages;
    }

    public String[] getMoodsDesc() {
        return moodsDesc;
    }

    public void setMoodsDesc(String[] moodsDesc) {
        this.moodsDesc = moodsDesc;
    }

    public Integer[] getMoodsUID() {
        return moodsUID;
    }

    public void setMoodsUID(Integer[] moodsUID) {
        this.moodsUID = moodsUID;
    }
}
