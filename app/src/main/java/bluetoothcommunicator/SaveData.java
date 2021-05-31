package bluetoothcommunicator;

import android.content.Context;

import java.util.ArrayList;

public class SaveData {
    private int rightI,leftI;
    private int beforeRightIndex,beforeLeftIndex;
    private ArrayList<String> rightData;
    private ArrayList<String> leftData;
    private int saveIndex;
    private boolean permission;
    private Context  saveContext;
    void saveData(){

        permission = false;
    }

    public void Reset(){
        rightData = new ArrayList<>();
        leftData = new ArrayList<>();
        rightI = 0;
        leftI = 0;
        beforeRightIndex = -1;
        beforeLeftIndex = -1;
        permission = true;
        saveIndex = 0;
    }
    public void setRight(int index,String text){
            int temp = Math.abs(index - beforeRightIndex) % 254;

            for (int i = 0; i < (temp - 1); i++) {
                rightData.add("");
            }
            rightData.add(text);

            beforeRightIndex = index;
    }

    public void setLeft(int index,String text){
            int temp = Math.abs(index - beforeLeftIndex) % 254;

            for (int i = 0; i < (temp - 1); i++) {
                leftData.add("");
            }
            leftData.add(text);

            beforeLeftIndex = index;
    }
    public String getData(){

        String temp = leftData.get(saveIndex) + rightData.get(saveIndex);
        saveIndex++;
        return temp;


    }
    public boolean isGetData(){
        if(rightData.size() > saveIndex && leftData.size() > saveIndex){
            return true;
        }else{
            return false;
        }

    }




}
