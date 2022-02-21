package com.example.elsmaps;

public class Mission {
    String Name;
    String Key;
    double X;
    double Y;

    public String getAdress(){
        return Name;
    }
    public void setAdress(String vehicle){
        this.Name = vehicle;
    }
    public String getKey(){
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public double getX(){
        return X;
    }
    public void moveXY(double x, double y) {this.X=x; this.Y=y;}
    public double getY(){
        return Y;
    }


}
