package io.soglomania.appfeelshare.model;

/**
 * Created by sogloarcadius on 13/03/17.
 */

public class Moods {

    private String desc;
    private Integer img;
    private Integer uid;
    private String name;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public Integer getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}

