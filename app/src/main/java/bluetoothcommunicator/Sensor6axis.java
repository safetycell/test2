package bluetoothcommunicator;

public class Sensor6axis{
    private int fx,fy,fz,mx,my,mz;
    private int kiss;
    private int init_fx,init_fy,init_fz,init_mx,init_my,init_mz = 0;
    private boolean virgin;
    private int offset;
    private int id;
    private boolean j;
    private String time;

    public Sensor6axis(){
        fx = 0;
        fy = 0;
        fz = 0;
        mx = 0;
        my = 0;
        mz = 0;
        virgin = true;
        kiss = 0;
        offset = 2;
        id = 0;
    }
    public Sensor6axis(int Fx,int Fy,int Fz,int Mx,int My,int Mz) {
        fx = Fx;
        fy = Fy;
        fz = Fz;
        mx = Mx;
        my = My;
        mz = Mz;
        virgin = true;
        kiss = 0;
        offset = 6;
        id=0;
    }
    public void setTime(String s){
        time = s;
    }

    public String getTime(){
        return time;
    }

    public void set6axis(String s){
        //Log.d("serialBT",s.substring(8,12));
        //Log.d("serialBT",String.valueOf(Integer.parseInt(s.substring(8,12), 16)));

        id = Integer.parseInt(s.substring(0,2), 16);

        fx = Integer.parseInt(s.substring(offset+4,offset+8), 16);
        fy = Integer.parseInt(s.substring(offset+8,offset+12), 16);
        fz = Integer.parseInt(s.substring(offset+12,offset+16), 16);
        mx = Integer.parseInt(s.substring(offset+16,offset+20), 16);
        my = Integer.parseInt(s.substring(offset+20,offset+24), 16);
        mz = Integer.parseInt(s.substring(offset+24,offset+28), 16);

        if (virgin && kiss > 1){
            init_fx = fx;
            init_fy = fy;
            init_fz = fz;
            init_mx = mx;
            init_my = my;
            init_mz = mz;
            virgin = false;
        }
        kiss++;
        //Log.d("serialBT",String.valueOf(fx));
    }


    public int getFx(){
        return fx-2048;
    }public int getFy(){
        return fy-2048;
    }public int getFz(){
        return fz-2048;
    }public int getMx(){
        return mx-2048;
    }public int getMy(){
        return my-2048;
    }public int getMz(){
        return mz-2048;
    }
    public int getFxDifference(){
        return init_fx - fx;
    }
    public int getFyDifference(){
        return init_fy - fy;
    }
    public int getFzDifference(){
        return -(init_fz - fz);
    }
    public int getMxDifference(){
        return init_mx - mx;
    }
    public int getMyDifference(){
        return init_my - my;
    }
    public int getMzDifference(){
        return init_mz - mz;
    }
    public int getID(){return id;};

}
