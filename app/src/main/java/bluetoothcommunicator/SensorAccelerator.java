package bluetoothcommunicator;

public class SensorAccelerator{
    private double ax,ay,az,wx,wy,wz;
    private String outputMessage;
    public SensorAccelerator(){
        ax = 0;
        ay = 0;
        az = 0;
        wx = 0;
        wy = 0;
        wz = 0;
    }public SensorAccelerator(int Ax,int Ay,int Az,int Wx,int Wy,int Wz){
        ax = Ax;
        ay = Ay;
        az = Az;
        wx = Wx;
        wy = Wy;
        wz = Wz;
    }public void setAccelerator(String s){
        try{
            String[] AcceleratorData = s.split(",", 7);
            //Log.d("serialbt", AcceleratorData[0]);
            ax = Double.parseDouble(AcceleratorData[1]);
            ay = Double.parseDouble(AcceleratorData[2]);
            az = Double.parseDouble(AcceleratorData[3]);
            wx = Double.parseDouble(AcceleratorData[4]);
            wy = Double.parseDouble(AcceleratorData[5]);
            wz = Double.parseDouble(AcceleratorData[6]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }public double getax(){
        return ax;
    }public double getay(){
        return ay;
    }public double getaz(){
        return az;
    }public double getwx(){
        return wx;
    }public double getwy(){
        return wy;
    }public double getwz(){
        return wz;
    }

    public String getSensorString(){
        outputMessage = "";

        outputMessage+= String.valueOf(ax);
        outputMessage+=" ,";
        outputMessage+= String.valueOf(ay);
        outputMessage+=" ,";
        outputMessage+= String.valueOf(az);
        outputMessage+=" ,";
        outputMessage+= String.valueOf(wx);
        outputMessage+=" ,";
        outputMessage+= String.valueOf(wy);
        outputMessage+=" ,";

        outputMessage+= String.valueOf(wz);

        return outputMessage;
    }
}