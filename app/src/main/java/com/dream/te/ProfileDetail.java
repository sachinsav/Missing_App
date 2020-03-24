package com.dream.te;

import java.io.Serializable;

public class ProfileDetail {
    public String name,mob,add,email;

    public ProfileDetail(){ }
    public ProfileDetail(String name1,String mob1,String address,String email1){
        name=name1;
        mob=mob1;
        add=address;
        email=email1;
    }

    public String getName() {
        return name;
    }

    public String getMob() {
        return mob;
    }

    public String getAdd() {
        return add;
    }

    public String getEmail() {
        return email;
    }
}
