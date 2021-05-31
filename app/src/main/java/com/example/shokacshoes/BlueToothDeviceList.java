package com.example.shokacshoes;

import java.io.Serializable;

public class BlueToothDeviceList implements Serializable{
    public String BluetoothDeviceAddress_r;
    public String BluetoothDeviceAddress_l;
    public String BluetoothDeviceName_r;
    public String BluetoothDeviceName_l;
    public BlueToothDeviceList(){
    }
    public BlueToothDeviceList(String r,String l){
        BluetoothDeviceAddress_r = r;
        BluetoothDeviceAddress_l = l;
    }
    public void setR(String address,String name){
        BluetoothDeviceAddress_r = address;
        BluetoothDeviceName_r = name;
    }
    public void setL(String address,String name){
        BluetoothDeviceAddress_l = address;
        BluetoothDeviceName_l = name;
    }
}