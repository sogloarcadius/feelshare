package io.sogloarcadius.feelshare.main;

import androidx.multidex.MultiDexApplication;

public class FeelShareApplication extends MultiDexApplication {

    private String[] moodsNames;
    private Integer[] moodsImages;
    private String[] moodsDesc;
    private Integer[] moodsUID;

    @Override
    public void onCreate() {
        super.onCreate();
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
