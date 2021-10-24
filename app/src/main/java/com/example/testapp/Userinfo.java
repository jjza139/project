package com.example.testapp;

public class Userinfo {
    public String name ,email,phone;
    public long money ;
    public String Car;

    public Userinfo(){}

    public Userinfo(String name,String email, String phone){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.money=0;
        this.Car="";

    }

}
