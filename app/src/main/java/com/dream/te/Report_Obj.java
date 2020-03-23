package com.dream.te;

public class Report_Obj {
    String name,mob_no,address,image_url;
    public Report_Obj(){ };
    public Report_Obj(String name, String mob_no, String address, String image_url) {
        this.name = name;
        this.mob_no = mob_no;
        this.address = address;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getMob_no() {
        return mob_no;
    }

    public String getAddress() {
        return address;
    }

    public String getImage_url() {
        return image_url;
    }

}
