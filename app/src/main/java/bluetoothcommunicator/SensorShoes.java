package bluetoothcommunicator;

public class SensorShoes {
    private Sensor6axis a,b,c;
    private String outputMessage;
    public SensorShoes(){

    }
    public String getSensorString(Sensor6axis a,Sensor6axis b,Sensor6axis c,SensorAccelerator acc){
        outputMessage = "";

        outputMessage += String.valueOf(a.getID());
        outputMessage+=" ,";

        outputMessage+= String.valueOf(a.getFx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getFy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getFz());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getMx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getMy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getMz());
        outputMessage+=" ,";

        outputMessage+= String.valueOf(b.getFx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(b.getFy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(b.getFz());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(b.getMx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(b.getMy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(b.getMz());
        outputMessage+=" ,";

        outputMessage+= String.valueOf(c.getFx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(c.getFy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(c.getFz());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(c.getMx());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(c.getMy());
        outputMessage+=" ,";
        outputMessage+= String.valueOf(c.getMz());
        outputMessage+=" ,";
        outputMessage+=acc.getSensorString();
        outputMessage+=" ,";
        outputMessage+= String.valueOf(a.getTime());
        outputMessage+=" ,";

        return outputMessage;
    }

}



