package bluetoothcommunicator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shokacshoes.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private static final int LEFT_LEG = 1;
    private static final int RIGHT_LEG = 2;

    // 定数
    private static final int REQUEST_ENABLEBLUETOOTH = 1; // Bluetooth機能の有効化要求時の識別コード
    private static final int REQUEST_CONNECTDEVICE_L = 2; // デバイス接続要求時の識別コード左
    private static final int REQUEST_CONNECTDEVICE_R   = 3; // デバイス接続要求時の識別コード右
    private static final int READBUFFERSIZE          = 1024;    // 受信バッファーのサイズ

    //private FileOutputStream rightSaveData;
    //private FileOutputStream leftSaveData;
    private FileOutputStream textSaveData;
    private String leftFileName = "";
    private String rightFileName = "";
    private String saveFileName = "";

    private boolean isWritenRight = true;
    private boolean isWritenLeft = true;



    // メンバー変数
    private BluetoothAdapter BluetoothAdapter;    // BluetoothAdapter : Bluetooth処理で必要
    private String leftDeviceAddress = "";    // デバイスアドレス
    private String rightDeviceAddress = "";    // デバイスアドレス
    private BlueToothService leftBlueToothService;    // BluetoothService : Bluetoothデバイスとの通信処理を担う
    private BlueToothService rightBlueToothService;    // BluetoothService : Bluetoothデバイスとの通信処理を担う
    private byte[] leftReadBuffer = new byte[READBUFFERSIZE];
    private int leftReadBufferCounter = 0;
    private byte[] rightReadBuffer = new byte[READBUFFERSIZE];
    private int rightReadBufferCounter = 0;


    private sensorGUI sensorBar1;
    private sensorGUI sensorBar2;
    private sensorGUI sensorBar3;
    private sensorGUI sensorBar4;

    //せん断力を色で出力
    private sliceGUI left1rowslice;
    private sliceGUI left1Columnslice;
    private sliceGUI left2rowslice;
    private sliceGUI left2Columnslice;
    private sliceGUI left3rowslice;
    private sliceGUI left3Columnslice;
    private pushGUI left1push;
    private pushGUI left2push;
    private pushGUI left3push;
    private rotationGUI left1rotate;
    private rotationGUI left2rotate;
    private rotationGUI left3rotate;

    private sliceGUI right1rowslice;
    private sliceGUI right1Columnslice;
    private sliceGUI right2rowslice;
    private sliceGUI right2Columnslice;
    private sliceGUI right3rowslice;
    private sliceGUI right3Columnslice;
    private pushGUI right1push;
    private pushGUI right2push;
    private pushGUI right3push;
    private rotationGUI right1rotate;
    private rotationGUI right2rotate;
    private rotationGUI right3rotate;


    private Sensor6axis leftSensor1 = new Sensor6axis();
    private Sensor6axis leftSensor2 = new Sensor6axis();
    private Sensor6axis leftSensor3 = new Sensor6axis();
    private Sensor6axis rightSensor1 = new Sensor6axis();
    private Sensor6axis rightSensor2 = new Sensor6axis();
    private Sensor6axis rightSensor3 = new Sensor6axis();
    private SensorAccelerator leftSensorAccel = new SensorAccelerator();
    private SensorAccelerator rightSensorAccel = new SensorAccelerator();

    private boolean isConnctBT = false;
    private Timer timer;

    private gravityGUI rightGravity;
    private gravityGUI leftGravity;
    private Button button1;

    private SensorShoes sensorShoes;
    private boolean communicationSwith = false;


    SaveData savedata = new SaveData();

    private class rotationGUI{
        private TextView rotatingBody;
        private float rotateX;
        private float rotateY;


        public rotationGUI(int id){
            rotatingBody = findViewById(id);
        }
        public void setRotateX(double i){
            rotateX = (float) i;
            rotatingBody.setRotationX(-rotateX);
            setColor();
        }
        public void setRotateY(double i){
            rotateY = (float) i;
            rotatingBody.setRotationY(-rotateY);
            setColor();
        }
        public void setRotateX(float i){
            rotateX = i;
            rotatingBody.setRotationX(-rotateX);
            setColor();
        }
        public void setRotateY(float i){
            rotateY = i;
            rotatingBody.setRotationY(-rotateY);
            setColor();
        }
        private void setColor(){
            rotatingBody.setTextColor(Color.rgb((int)5, (int)(240-(Math.abs(rotateX)+Math.abs(rotateY))/2*1.6), (int)0));
        }


    }

    private class sliceGUI{
        private TextView sliceBar1,sliceBar2;

        public sliceGUI(int Up_or_Left ,int Down_or_Right){
            sliceBar1 = findViewById(Up_or_Left);
            sliceBar2 = findViewById(Down_or_Right);
            sliceBar1.setTextColor(Color.rgb(0,0,255));
            sliceBar2.setTextColor(Color.rgb(0,0,255));
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setSlicePower(double power){
            if(power>100){
                power = 100;
            }
            if(power<-100){
                power = -100;
            }
            if(power > 50) {
                sliceBar2.setTextColor(Color.rgb(255, (int)(255 - 255 * (power-50) / 50), 0));
            }else if(power > 0) {
                sliceBar2.setTextColor(Color.rgb((int)(255 * power / 50), (int)(255 * power / 50), (int)(255 - 255 * power / 50)));
            }else if(power < -50){
                sliceBar1.setTextColor(Color.rgb(255, (int)(255 - 255 * (-power-50) / 50), 0));
            }else if(power < 0){
                sliceBar1.setTextColor(Color.rgb((int)(255 * -power / 50), (int)(255 * -power / 50), (int)(255 - 255 * -power / 50)));
            }else{
                sliceBar1.setTextColor(Color.rgb(0,0,255));
                sliceBar2.setTextColor(Color.rgb(0,0,255));
            }

        }
    }

    private class gravityGUI{
        private ImageView gravityImage;
        private float sensor1Fz;
        private float sensor2Fz;
        private float sensor3Fz;
        private float sensor1X;
        private float sensor1Y;
        private float sensor2X;
        private float sensor2Y;
        private float sensor3X;
        private float sensor3Y;
        private float centerX;
        private float centerY;
        private float threshold;
        private float i;

        public gravityGUI(int id,int leg){
            gravityImage = findViewById(id);
            if(leg == RIGHT_LEG) {
                sensor1X = 61;
                sensor1Y = 140;
                sensor2X = 125;
                sensor2Y = 165;
                sensor3X = 95;
                sensor3Y = 326;
                centerX = 89+5;
                centerY = 206+5;
            }else{
                sensor1X = 180-61;
                sensor1Y = 140;
                sensor2X = 180-125;
                sensor2Y = 165;
                sensor3X = 180-95;
                sensor3Y = 326;
                centerX = 180-89-5;
                centerY = 206+5;

            }

            threshold = 30;
        }

        private int convertionPx(float DIP){

            final float GESTURE_THRESHOLD_DIP = 16.0f;

            // Convert the dips to pixels
            final float scale = gravityImage.getContext().getResources().getDisplayMetrics().density;
            return (int) (DIP * scale + 0.5f);
        }

        public void setSensor1Fz(int power){
            sensor1Fz = power;
            //Log.d("gravity", "1 : " + String.valueOf(power));





            //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
            gravityImage.setTranslationX(convertionPx(getGravityX()));
            gravityImage.setTranslationY(convertionPx(getGravityY()));
            //Log.d("gravity", String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+" , "+  String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

        }

        public void setSensor2Fz(int power){
            sensor2Fz = power;
            //Log.d("gravity", "2 : " + String.valueOf(power));
            //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
            gravityImage.setTranslationX(convertionPx(getGravityX()));
            gravityImage.setTranslationY(convertionPx(getGravityY()));
            //Log.d("gravity",  String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ " , "+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

        }
        public void setSensor3Fz(int power){
            sensor3Fz = power;
            //Log.d("gravity", "3 : " + String.valueOf(power));
            //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
            gravityImage.setTranslationX(convertionPx(getGravityX()));
            gravityImage.setTranslationY(convertionPx(getGravityY()));
            //Log.d("gravity",  String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+" , "+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

        }
        private float getGravityX(){
            float s1 = sensor1Fz < threshold ? 0 : sensor1Fz;
            float s2 = sensor2Fz < threshold ? 0 : sensor2Fz;
            float s3 = sensor3Fz < threshold ? 0 : sensor3Fz;
            if(s1 == 0 && s2 == 0 && s3 == 0){
                return 0;
            }else {
                return (((centerX - sensor1X) * s1 + (centerX - sensor2X) * s2 + (centerX - sensor3X) * s3) / (s1 + s2 + s3) * -1);
            }
        }
        private float getGravityY(){
            float s1 = sensor1Fz < threshold ? 0 : sensor1Fz;
            float s2 = sensor2Fz < threshold ? 0 : sensor2Fz;
            float s3 = sensor3Fz < threshold ? 0 : sensor3Fz;

            if(s1 == 0 && s2 == 0 && s3 == 0){
                return 0;
            }else{
                return ((centerY-sensor1Y) * s1 + (centerY-sensor2Y) * s2 + (centerY-sensor3Y) * s3) / (s1 + s2 + s3)*-1;
            }
        }

    }

    private class pushGUI{
        private TextView center;

        public pushGUI(int id){
            center = findViewById(id);
            center.setTextColor(Color.rgb(0,0,255));
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setSlicePower(double power){
            if(power>100){
                power = 100;
            }
            if(power<0){
                power = 0;
            }

            if(power > 50) {
                center.setTextColor(Color.rgb(255, (int)(255 - 255 * (power-50) / 50), 0));
            }else if(power > 0) {
                center.setTextColor(Color.rgb((int)(255 * power / 50), (int)(255 * power / 50), (int)(255 - 255 * power / 50)));
            }else{
                center.setTextColor(Color.rgb(0,0, 255));
            }

        }
    }

    private class sensorGUI{
        private final ProgressBar Fxsensor;
        private final ProgressBar Fysensor;
        private final ProgressBar Fzsensor;
        private final ProgressBar Mxsensor;
        private final ProgressBar Mysensor;
        private final ProgressBar Mzsensor;

        public sensorGUI(int fx,int fy,int fz,int mx,int my,int mz){
            Fxsensor = findViewById(fx);
            Fysensor = findViewById(fy);
            Fzsensor = findViewById(fz);
            Mxsensor = findViewById(mx);
            Mysensor = findViewById(my);
            Mzsensor = findViewById(mz);
        }
        public void setProguressBarMax(int fx,int fy,int fz,int mx,int my,int mz){
            Fxsensor.setMax(fx);
            Fysensor.setMax(fy);
            Fzsensor.setMax(fz);
            Mxsensor.setMax(mx);
            Mysensor.setMax(my);
            Mzsensor.setMax(mz);
        }
        public void setProgress(int fx,int fy,int fz,int mx,int my,int mz){
            Fxsensor.setProgress(fx);
            Fysensor.setProgress(fy);
            Fzsensor.setProgress(fz);
            Mxsensor.setProgress(mx);
            Mysensor.setProgress(my);
            Mzsensor.setProgress(mz);
        }public void setProgress(double fx,double fy,double fz,double mx,double my,double mz){
            Fxsensor.setProgress((int) fx);
            Fysensor.setProgress((int) fy);
            Fzsensor.setProgress((int) fz);
            Mxsensor.setProgress((int) mx);
            Mysensor.setProgress((int) my);
            Mzsensor.setProgress((int) mz);
        }
    }


    public class sendo extends TimerTask {
        private Handler handler;

        public sendo() {
            handler = new Handler();
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {


                }
            });
        }

    }






    // Bluetoothサービスから情報を取得するハンドラ
    private final Handler mHandler = new Handler(Looper.getMainLooper())
    {
        // ハンドルメッセージ
        // UIスレッドの処理なので、UI処理について、runOnUiThread対応は、不要。
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage( Message msg )
        {
            switch( msg.what )
            {
                case BlueToothService.LEFT_MESSAGE_STATECHANGE:
                    switch( msg.arg1 )
                    {
                        case BlueToothService.LEFT_STATE_NONE:            // 未接続
                            Log.d("serialBT", "L未接続 ");

                            isConnctBT = false;
                            break;
                        case BlueToothService.LEFT_STATE_CONNECT_START:        // 接続開始
                            Log.d("serialBT", "L接続開始 ");
                            break;
                        case BlueToothService.LEFT_STATE_CONNECT_FAILED:            // 接続失敗
                            Log.d("serialBT", "L接続失敗");
                            Toast.makeText( MainActivity.this, "Failed to connect to the left shoes.", Toast.LENGTH_LONG ).show();
                            break;
                        case BlueToothService.LEFT_STATE_CONNECTED:    // 接続完了
                            // GUIアイテムの有効無効の設定
                            // 切断ボタン、文字列送信ボタンを有効にする
                            Log.d("serialBT", "L接続完了");
                            Toast.makeText( MainActivity.this, "Success Connect to the left shoes", Toast.LENGTH_LONG ).show();

                            isConnctBT = true;
                            write( "r" ,LEFT_LEG);
                            write( "03",LEFT_LEG );
                            write( "01",LEFT_LEG );
                            write( "02" ,LEFT_LEG);
                            write( "03" ,LEFT_LEG);
                            write( "z1" ,LEFT_LEG);
                            write( "z2" ,LEFT_LEG);
                            write( "z3" ,LEFT_LEG);
                            break;
                        case BlueToothService.LEFT_STATE_CONNECTION_LOST:            // 接続ロスト
                            Toast.makeText( MainActivity.this, "Lost connection to the device.", Toast.LENGTH_SHORT ).show();
                            Log.d("serialBT", "L接続ロスト");

                            isConnctBT = false;
                            break;
                        case BlueToothService.LEFT_STATE_DISCONNECT_START:    // 切断開始
                            // GUIアイテムの有効無効の設定
                            // 切断ボタン、文字列送信ボタンを無効にする
                            Log.d("serialBT", "L切断開始");
                            break;
                        case BlueToothService.LEFT_STATE_DISCONNECTED:            // 切断完了
                            Log.d("serialBT", "L切断完了");
                            // GUIアイテムの有効無効の設定
                            leftBlueToothService = null;    // BluetoothServiceオブジェクトの解放
                            isConnctBT = false;
                            break;
                    }
                    break;
                case BlueToothService.RIGHT_MESSAGE_STATECHANGE:
                    switch( msg.arg1 )
                    {
                        case BlueToothService.RIGHT_STATE_NONE:            // 未接続

                            Log.d("serialBT", "R未接続 ");
                            isConnctBT = false;
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECT_START:        // 接続開始
                            Log.d("serialBT", "R接続開始");
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECT_FAILED:            // 接続失敗
                            Log.d("serialBT", "R接続失敗 ");
                            Toast.makeText( MainActivity.this, "Failed to connect to the right shoes.", Toast.LENGTH_LONG ).show();
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECTED:    // 接続完了
                            Log.d("serialBT", "R接続完了 ");
                            Toast.makeText( MainActivity.this, "Success connect to the right shoes.", Toast.LENGTH_LONG ).show();

                            isConnctBT = true;
                            write( "r" ,RIGHT_LEG);
                            write( "03",RIGHT_LEG );
                            write( "01",RIGHT_LEG );
                            write( "02" ,RIGHT_LEG);
                            write( "03" ,RIGHT_LEG);
                            write( "z1" ,RIGHT_LEG);
                            write( "z2" ,RIGHT_LEG);
                            write( "z3" ,RIGHT_LEG);
                            //write( "s" ,RIGHT_LEG);
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECTION_LOST:            // 接続ロスト
                            Toast.makeText( MainActivity.this, "Lost connection to the device.", Toast.LENGTH_LONG).show();
                            Log.d("serialBT", "R接続ロスト ");

                            isConnctBT = false;
                            break;
                        case BlueToothService.RIGHT_STATE_DISCONNECT_START:    // 切断開始
                            Log.d("serialBT", "R接続開始 ");
                            break;
                        case BlueToothService.RIGHT_STATE_DISCONNECTED:            // 切断完了
                            Log.d("serialBT", "R切断完了 ");
                            rightBlueToothService = null;    // BluetoothServiceオブジェクトの解放
                            isConnctBT = false;
                            break;
                    }
                    break;
                case BlueToothService.LEFT_MESSAGE_READ:
                    //Log.d("serialBT", "L受信中... ");
                    byte[] abyteRead = (byte[])msg.obj;
                    int iCountBuf = msg.arg1;
                    for( int i = 0; i < iCountBuf; i++ )
                    {
                        byte c = abyteRead[i];
                        if( '\r' == c )
                        {    // 終端
                            leftReadBuffer[leftReadBufferCounter] = '\0';
                            String receiveReadBT = new String(leftReadBuffer, 0, leftReadBufferCounter);



                            Log.d("serialBT",receiveReadBT);
                            if (receiveReadBT.indexOf("04") == 2) {
                                Log.d("serialBT", "加速度センサの情報です");
                                leftSensorAccel.setAccelerator(receiveReadBT);

                                sensorBar4.setProgress(leftSensorAccel.getax()*100+50, leftSensorAccel.getay()*100+50, leftSensorAccel.getaz()*100+50, leftSensorAccel.getwx()+500, leftSensorAccel.getwy()+500, leftSensorAccel.getwz()+500);

                            }else if (receiveReadBT.indexOf("010e") == 2){
                                //Log.d("serialBT","センサ1の情報です");

                                DateFormat format = new SimpleDateFormat("ss.SSS");
                                String date = format.format(new Date());
                                leftSensor1.setTime(date);
                                leftSensor1.set6axis(receiveReadBT);
                                sensorBar1.setProgress(leftSensor1.getFxDifference()+500, leftSensor1.getFyDifference()+500, leftSensor1.getFzDifference(), leftSensor1.getMxDifference()+500, leftSensor1.getMyDifference()+500, leftSensor1.getMzDifference()+500);
                                left1rowslice.setSlicePower(leftSensor1.getFyDifference()/5);
                                left1Columnslice.setSlicePower(leftSensor1.getFxDifference()/5);
                                left1push.setSlicePower(leftSensor1.getFzDifference()/5);
                                left1rotate.setRotateX(leftSensor1.getMxDifference()/5);
                                left1rotate.setRotateY(leftSensor1.getMyDifference()/5);
                                leftGravity.setSensor1Fz(leftSensor1.getFzDifference());

                            }else if (receiveReadBT.indexOf("020e") == 2){
                                //Log.d("serialBT","センサ2の情報です");
                                leftSensor2.set6axis(receiveReadBT);
                                sensorBar2.setProgress(leftSensor2.getFxDifference()+500, leftSensor2.getFyDifference()+500, leftSensor2.getFzDifference(), leftSensor2.getMxDifference()+500, leftSensor2.getMyDifference()+500, leftSensor2.getMzDifference()+500);
                                left2rowslice.setSlicePower(leftSensor2.getFyDifference()/5);
                                left2Columnslice.setSlicePower(leftSensor2.getFxDifference()/5);
                                left2push.setSlicePower(leftSensor2.getFzDifference()/5);
                                left2rotate.setRotateX(leftSensor2.getMxDifference()/5);
                                left2rotate.setRotateY(leftSensor2.getMyDifference()/5);
                                leftGravity.setSensor2Fz(leftSensor2.getFzDifference());

                            }else if (receiveReadBT.indexOf("030e") == 2){
                                //Log.d("serialBT","センサ3の情報です");
                                leftSensor3.set6axis(receiveReadBT);
                                sensorBar3.setProgress(leftSensor3.getFxDifference()+500, leftSensor3.getFyDifference()+500, leftSensor3.getFzDifference(), leftSensor3.getMxDifference()+500, leftSensor3.getMyDifference()+500, leftSensor3.getMzDifference()+500);
                                left3rowslice.setSlicePower(-leftSensor3.getFyDifference()/5);
                                left3Columnslice.setSlicePower(-leftSensor3.getFxDifference()/5);
                                left3push.setSlicePower(leftSensor3.getFzDifference()/5);
                                left3rotate.setRotateX(leftSensor3.getMxDifference()/5);
                                left3rotate.setRotateY(-leftSensor3.getMyDifference()/5);
                                leftGravity.setSensor3Fz(leftSensor3.getFzDifference());
                                savedata.setLeft(leftSensor3.getID(),sensorShoes.getSensorString(leftSensor1, leftSensor2, leftSensor3, leftSensorAccel));
                                if(savedata.isGetData()){
                                    SaveText(textSaveData,savedata.getData());
                                }
                            }

                            if(communicationSwith) {
                                //SaveText(leftSaveData, sensorShoes.getSensorString(leftSensor1, leftSensor2, leftSensor3) + leftSensorAccel.getSensorString(), LEFT_LEG);
                            }
                            // GUIアイテムへの反映
                            //( (TextView)findViewById( R.id.textview_read ) ).setText( new String( mReadBuffer, 0, mReadBufferCounter ) );
                            leftReadBufferCounter = 0;
                        }
                        else if( '\n' == c )
                        {
                            ;    // 何もしない
                        }
                        else
                        {    // 途中
                            if( ( READBUFFERSIZE - 1 ) > leftReadBufferCounter)
                            {    // mReadBuffer[READBUFFERSIZE - 2] までOK。
                                // mReadBuffer[READBUFFERSIZE - 1] は、バッファー境界内だが、「\0」を入れられなくなるのでNG。
                                leftReadBuffer[leftReadBufferCounter] = c;
                                leftReadBufferCounter++;
                            }
                            else
                            {    // バッファーあふれ。初期化
                                leftReadBufferCounter = 0;
                            }
                        }
                    }
                    break;

                case BlueToothService.RIGHT_MESSAGE_READ:
                    //Log.d("serialBT", "R受信中... ");
                    byte[] bbyteRead = (byte[])msg.obj;

                    int ibCountBuf = msg.arg1;
                    for( int i = 0; i < ibCountBuf; i++ )
                    {
                        byte c = bbyteRead[i];
                        if( '\r' == c )
                        {    // 終端
                            rightReadBuffer[rightReadBufferCounter] = '\0';
                            String receiveReadBT = new String(rightReadBuffer, 0, rightReadBufferCounter);

                            //SaveText(rightSaveData,receiveReadBT,RIGHT_LEG);
                            Log.d("serialBT",receiveReadBT);
                            if (receiveReadBT.indexOf("04") == 2) {
                                //Log.d("serialBT", "加速度センサの情報です");
                                rightSensorAccel.setAccelerator(receiveReadBT);
                                Log.d("accel",receiveReadBT);


                            }else if (receiveReadBT.indexOf("010e") == 2){
                                //Log.d("serialBT","センサ1の情報です");

                                DateFormat format = new SimpleDateFormat("ss.SSS");
                                String date = format.format(new Date());
                                rightSensor1.setTime(date);
                                rightSensor1.set6axis(receiveReadBT);
                                right1rowslice.setSlicePower(rightSensor1.getFyDifference()/5);
                                right1Columnslice.setSlicePower(rightSensor1.getFxDifference()/5);
                                right1push.setSlicePower(rightSensor1.getFzDifference()/5);
                                right1rotate.setRotateX(rightSensor1.getMxDifference()/5);
                                right1rotate.setRotateY(rightSensor1.getMyDifference()/5);
                                rightGravity.setSensor1Fz(rightSensor1.getFzDifference());


                            }else if (receiveReadBT.indexOf("020e") == 2){
                                //Log.d("serialBT","センサ2の情報です");
                                rightSensor2.set6axis(receiveReadBT);
                                //Log.d("serialBT", String.valueOf(rightSensor2.getFxDifference()));
                                right2rowslice.setSlicePower(rightSensor2.getFyDifference()/5);
                                right2Columnslice.setSlicePower(rightSensor2.getFxDifference()/5);
                                right2push.setSlicePower(rightSensor2.getFzDifference()/5);
                                right2rotate.setRotateX(rightSensor2.getMxDifference()/5);
                                right2rotate.setRotateY(rightSensor2.getMyDifference()/5);
                                rightGravity.setSensor2Fz(rightSensor2.getFzDifference());

                            }else if (receiveReadBT.indexOf("030e") == 2){
                                //Log.d("serialBT","センサ3の情報です");
                                rightSensor3.set6axis(receiveReadBT);
                                right3rowslice.setSlicePower(-rightSensor3.getFyDifference()/5);
                                right3Columnslice.setSlicePower(-rightSensor3.getFxDifference()/5);
                                right3push.setSlicePower(rightSensor3.getFzDifference()/5);
                                right3rotate.setRotateX(rightSensor3.getMxDifference()/5);
                                right3rotate.setRotateY(-rightSensor3.getMyDifference()/5);
                                rightGravity.setSensor3Fz(rightSensor3.getFzDifference());
                                int index = Integer.parseInt(receiveReadBT.substring(0,2), 16);
                                savedata.setRight(index,sensorShoes.getSensorString(rightSensor1, rightSensor2, rightSensor3,rightSensorAccel));
                                if(savedata.isGetData()){
                                    SaveText(textSaveData,savedata.getData());
                                }
                            }

                            if(communicationSwith) {
                                //SaveText(rightSaveData, sensorShoes.getSensorString(rightSensor1, rightSensor2, rightSensor3) + rightSensorAccel.getSensorString(), RIGHT_LEG);
                            }
                            // GUIアイテムへの反映
                            //( (TextView)findViewById( R.id.textview_read ) ).setText( new String( mReadBuffer, 0, mReadBufferCounter ) );
                            rightReadBufferCounter = 0;
                        }
                        else if( '\n' == c )
                        {
                            ;    // 何もしない
                        }
                        else
                        {    // 途中
                            if( ( READBUFFERSIZE - 1 ) > rightReadBufferCounter)
                            {    // mReadBuffer[READBUFFERSIZE - 2] までOK。
                                // mReadBuffer[READBUFFERSIZE - 1] は、バッファー境界内だが、「\0」を入れられなくなるのでNG。
                                rightReadBuffer[rightReadBufferCounter] = c;
                                rightReadBufferCounter++;
                            }
                            else
                            {    // バッファーあふれ。初期化
                                rightReadBufferCounter = 0;
                            }
                        }
                    }
                    break;
                case BlueToothService.LEFT_MESSAGE_WRITTEN:
                    // GUIアイテムの有効無効の設定
                    // 文字列送信ボタンを有効にする（連打対策で無効になっているボタンを復帰させる）
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touchence_activity_main);

        // Bluetoothアダプタの取得
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService( Context.BLUETOOTH_SERVICE );
        BluetoothAdapter = bluetoothManager.getAdapter();
        if( null == BluetoothAdapter)
        {    // Android端末がBluetoothをサポートしていない
            Toast.makeText( this, R.string.bluetooth_is_not_supported, Toast.LENGTH_SHORT ).show();
            finish();    // アプリ終了宣言
            return;
        }


        //プログレスバーのMax値の設定
        sensorBar1.setProguressBarMax(1000,1000,500,1000,1000,1000);
        sensorBar2.setProguressBarMax(1000,1000,500,1000,1000,1000);
        sensorBar3.setProguressBarMax(1000,1000,500,1000,1000,1000);
        sensorBar4.setProguressBarMax(100,100,100,1000,1000,1000);

        //タイマーを新規生成
        timer = new Timer();
        TimerTask timerTask = new sendo();
        timer.scheduleAtFixedRate(timerTask, 0, 1);

        //せん断力の取得
        left1rowslice = new sliceGUI(R.id.left1up,R.id.left1down);
        left1Columnslice = new sliceGUI(R.id.left1left,R.id.left1right);
        left2rowslice = new sliceGUI(R.id.left2up,R.id.left2down);
        left2Columnslice = new sliceGUI(R.id.left2left,R.id.left2right);
        left3rowslice = new sliceGUI(R.id.left3up,R.id.left3down);
        left3Columnslice = new sliceGUI(R.id.left3left,R.id.left3right);
        left1push = new pushGUI(R.id.left1center);
        left2push = new pushGUI(R.id.left2center);
        left3push = new pushGUI(R.id.left3center);
        left1rotate = new rotationGUI(R.id.left1circle);
        left2rotate = new rotationGUI(R.id.left2circle);
        left3rotate = new rotationGUI(R.id.left3circle);


        right1rowslice = new sliceGUI(R.id.right1up,R.id.right1down);
        right1Columnslice = new sliceGUI(R.id.right1left,R.id.right1right);
        right2rowslice = new sliceGUI(R.id.right2up,R.id.right2down);
        right2Columnslice = new sliceGUI(R.id.right2left,R.id.right2right);
        right3rowslice = new sliceGUI(R.id.right3up,R.id.right3down);
        right3Columnslice = new sliceGUI(R.id.right3left,R.id.right3right);
        right1push = new pushGUI(R.id.right1center);
        right2push = new pushGUI(R.id.right2center);
        right3push = new pushGUI(R.id.right3center);
        right1rotate = new rotationGUI(R.id.right1circle);
        right2rotate = new rotationGUI(R.id.right2circle);
        right3rotate = new rotationGUI(R.id.right3circle);


        rightGravity = new gravityGUI(R.id.rightGravityView,RIGHT_LEG);
        leftGravity = new gravityGUI(R.id.leftGravityView,LEFT_LEG);


        button1 = findViewById(R.id.StartButton);
        button1.setOnClickListener(buttonClick);

        sensorShoes = new SensorShoes();



    }

    // 初回表示時、および、ポーズからの復帰時
    @Override
    protected void onResume()
    {
        super.onResume();

        // Android端末のBluetooth機能の有効化要求
        requestBluetoothFeature();

    }

    // 別のアクティビティ（か別のアプリ）に移行したことで、バックグラウンドに追いやられた時
    @Override
    protected void onPause()
    {
        super.onPause();

        // 切断
        //disconnect(LEFT_LEG);
        // 切断
        //disconnect(RIGHT_LEG);
    }

    // アクティビティの終了直前
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if( null != leftBlueToothService)
        {
            leftBlueToothService.disconnect();
            leftBlueToothService = null;
        }
        if( null != rightBlueToothService)
        {
            rightBlueToothService.disconnect();
            rightBlueToothService = null;
        }
    }


    // Android端末のBluetooth機能の有効化要求
    private void requestBluetoothFeature()
    {
        if( BluetoothAdapter.isEnabled() )
        {
            return;
        }
        // デバイスのBluetooth機能が有効になっていないときは、有効化要求（ダイアログ表示）
        Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        startActivityForResult( enableBtIntent, REQUEST_ENABLEBLUETOOTH );
    }

    // 機能の有効化ダイアログの操作結果
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch( requestCode )
        {
            case REQUEST_ENABLEBLUETOOTH: // Bluetooth有効化要求
                if( Activity.RESULT_CANCELED == resultCode )
                {    // 有効にされなかった
                    Toast.makeText( this, R.string.bluetooth_is_not_working, Toast.LENGTH_SHORT ).show();
                    finish();    // アプリ終了宣言
                    return;
                }
                break;
            case REQUEST_CONNECTDEVICE_L: // デバイス接続要求
                String strDeviceName;
                if( Activity.RESULT_OK == resultCode )
                {
                    // デバイスリストアクティビティからの情報の取得
                    leftDeviceAddress = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_ADDRESS );
                    connect(RIGHT_LEG);
                    connect(LEFT_LEG);
                }
                else
                {
                    strDeviceName = "接続なし";
                    connect(RIGHT_LEG);
                    disconnect(LEFT_LEG);
                }
                break;
            case REQUEST_CONNECTDEVICE_R: // デバイス接続要求
                if( Activity.RESULT_OK == resultCode )
                {
                    // デバイスリストアクティビティからの情報の取得
                    rightDeviceAddress = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_ADDRESS );
                    connect(RIGHT_LEG);
                    connect(LEFT_LEG);
                }
                else
                {
                    strDeviceName = "接続なし";
                    disconnect(RIGHT_LEG);
                    connect(LEFT_LEG);

                }
                //( (TextView)findViewById( R.id.textview_read ) ).setText( "" );
                break;


        }
        super.onActivityResult( requestCode, resultCode, data );
    }




    // 接続
    private void connect(int id)
    {
        if(id == LEFT_LEG){
            if( leftDeviceAddress.equals( "" ) )
            {    // DeviceAddressが空の場合は処理しない
                return;
            }

            if( null != leftBlueToothService)
            {    // mBluetoothServiceがnullでないなら接続済みか、接続中。
                return;
            }

            // 接続
            BluetoothDevice device = BluetoothAdapter.getRemoteDevice(leftDeviceAddress);
            leftBlueToothService = new BlueToothService(this, mHandler, device, LEFT_LEG);
            leftBlueToothService.connect();
            //Calendar calendar = Calendar.getInstance();
            //Log.d("Calendar",calendar.getTime().toString());
            //leftFileName = "leftLeg  " + calendar.getTime().toString();


        }

        if(id == RIGHT_LEG){
            if( rightDeviceAddress.equals( "" ) )
            {    // DeviceAddressが空の場合は処理しない
                return;
            }

            if( null != rightBlueToothService)
            {    // mBluetoothServiceがnullでないなら接続済みか、接続中。
                return;
            }

            // 接続
            BluetoothDevice device = BluetoothAdapter.getRemoteDevice(rightDeviceAddress);
            rightBlueToothService = new BlueToothService(this, mHandler, device, RIGHT_LEG);
            rightBlueToothService.connect();
            //Calendar calendar = Calendar.getInstance();
            //Log.d("Calendar",calendar.getTime().toString());
            //rightFileName = calendar.getTime().toString();

        }

    }


    // 切断
    private void disconnect(int id)
    {
        Log.d("connect","disconnect");
        //CloseText(leftSaveData);
        if(id == LEFT_LEG) {
            if (null == leftBlueToothService) {    // mBluetoothServiceがnullなら切断済みか、切断中。
                return;
            }
            // 切断
            leftBlueToothService.disconnect();
            leftBlueToothService = null;

        }
        if(id == RIGHT_LEG) {
            write("e",RIGHT_LEG);
            //CloseText(rightSaveData);
            if (null == rightBlueToothService) {    // mBluetoothServiceがnullなら切断済みか、切断中。
                return;
            }
            // 切断
            rightBlueToothService.disconnect();
            rightBlueToothService = null;

        }
    }
    // 文字列送信
    private void write( String string,int id )
    {
        if(id == LEFT_LEG) {
            if (null == leftBlueToothService) {    // mBluetoothServiceがnullなら切断済みか、切断中。
                return;
            }

            // 終端に改行コードを付加
            String stringSend = string + "\r\n";

            // バイト列送信
            leftBlueToothService.write(stringSend.getBytes());

        }
        if(id == RIGHT_LEG) {
            if (null == rightBlueToothService) {    // mBluetoothServiceがnullなら切断済みか、切断中。
                return;
            }

            // 終端に改行コードを付加
            String stringSend = string + "\r\n";

            // バイト列送信
            rightBlueToothService.write(stringSend.getBytes());

        }
    }
    private void SaveModule(){

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void SaveText(FileOutputStream saveFile, String text) {

        String FILE_NAME;
        FILE_NAME = saveFileName + ".csv";

        try {
            saveFile = openFileOutput(FILE_NAME, MODE_PRIVATE|Context.MODE_APPEND);

            //saveFile.write((datetimeText + " ," + text).getBytes());
            saveFile.write((text).getBytes());
            saveFile.write("\n".getBytes());

            //Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.StartButton:
                    if(communicationSwith) {
                        button1.setText("start");
                        write("e",RIGHT_LEG);
                        write("e",LEFT_LEG);
                        communicationSwith = false;
                    }else{
                        savedata.Reset();
                        button1.setText("end");
                        
                        Calendar calendar = Calendar.getInstance();
                        long mill = calendar.getTimeInMillis();
                        Log.d("Calendar",calendar.getTime().toString());
                        saveFileName = calendar.getTime().toString() + String.valueOf(mill);





                        write("s",RIGHT_LEG);
                        write("s",LEFT_LEG);
                        communicationSwith = true;
                    }

                    Log.d("debug","button1, Perform action on click");
                    break;
            }
        }
    };

    private void ClosedButton(){
        button1.setText("start");
        communicationSwith = false;
        write("e",RIGHT_LEG);
        write("e",LEFT_LEG);
        savedata.Reset();
    }


}


