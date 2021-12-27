package com.example.testapp;

public class Userinfo {
    public String name ,email,phone,plate;
    public long money ;

    public Userinfo(){}

    public String getPlate() { return plate; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public long getMoney() {
        return money;
    }

    public Userinfo(String name, String email, String phone,String plate){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.plate=plate;
        this.money=0;
        

    }

}
