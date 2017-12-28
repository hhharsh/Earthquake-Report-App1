package com.example.android.quakereport;

/**
 * Created by hhharsh on 24/10/17.
 */

public class Earth {

    private double mag;
    private String place;
    private String date;
    private String time;
    private String url;

    Earth(double m,String p,String t,String t2,String url){
        mag=m;
        place=p;
        date=t;
        time=t2;
        this.url=url;
    }

    public void set_mag(double m){
        mag=m;
    }

    public void set_place(String s){
        place=s;
    }

    public void set_date(String s){
        date=s;
    }

    public void set_time(String s){
        time=s;
    }

    public double get_mag(){
        return mag;
    }
    public String get_place(){
        return place;
    }
    public String get_date(){
        return date;
    }
    public String get_time(){
        return time;
    }
    public String get_url(){
        return url;
    }


}
