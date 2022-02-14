package com.atguigu.springcloud;

public class demoa{

    public demoa(){
        System.out.println(" demoa constructed");
    }
    {
        System.out.println("demoa block call");
    }
    static {
        System.out.println("demoa static block call");
    }

}
