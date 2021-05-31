package com.example.shokacshoes;

public class Shoes {
    private Sensor6axis leftSensor;
    private Sensor6axis rightSensor;

    public Double LFzAverage;
    public Double RFzAverage;

    public Double LFzStdevp;
    public Double RFzStdevp;
    public Double balance;
    NumericalAnalysis calcModule = new NumericalAnalysis();




    public Shoes(Sensor6axis left,Sensor6axis right){
        leftSensor = left;
        rightSensor = right;


    }

}
