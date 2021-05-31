package com.example.shokacshoes;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;


public class WarmingUp extends AppCompatActivity {
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
    private android.bluetooth.BluetoothAdapter BluetoothAdapter;    // BluetoothAdapter : Bluetooth処理で必要
    private String leftDeviceAddress = "E8:68:E7:05:26:DA";    // デバイスアドレス
    private String rightDeviceAddress = "E8:68:E7:05:28:2E";    // デバイスアドレス
    private BlueToothService leftBlueToothService;    // BluetoothService : Bluetoothデバイスとの通信処理を担う
    private BlueToothService rightBlueToothService;    // BluetoothService : Bluetoothデバイスとの通信処理を担う
    private byte[] leftReadBuffer = new byte[READBUFFERSIZE];
    private int leftReadBufferCounter = 0;
    private byte[] rightReadBuffer = new byte[READBUFFERSIZE];
    private int rightReadBufferCounter = 0;

    private boolean isConnctBT_right = false;
    private boolean isConnctBT_left = false;
    private String rightBTName;
    private String leftBTName;
    private Timer connectTimer;
    private Timer progressTimer;
    private Calibration leftCalibration;
    private Calibration rightCalibration;
    private CorrectionCoefficient multdata;
    private int count;

    private TextView otukareText;
    private com.example.shokacshoes.Sensor6axis leftSensor;
    private com.example.shokacshoes.Sensor6axis rightSensor;

    private boolean isReceive_left = false;

    private boolean isReceive_right = false;

    private boolean isConnectLeft;
    private boolean isConnectRight;
    private boolean softWearStart;
    private Button startMeasurement_button;
    private TimerTask upLeg;



    public void InitSensor(){


        leftSensor = new com.example.shokacshoes.Sensor6axis("left");
        rightSensor = new com.example.shokacshoes.Sensor6axis("right");

        count = 0;
    }

    public void timerStop(){
        if(progressTimer != null) {

            progressTimer.cancel();
            progressTimer.purge();
            progressTimer = null;

        }
        if(connectTimer != null){

            connectTimer.cancel();
            connectTimer.purge();
            connectTimer = null;
        }
    }


    public class UpLeg extends TimerTask {
        private Handler handler;

        ProgressBar rightBar = (ProgressBar)findViewById(R.id.progressBarRight);
        ProgressBar leftBar = (ProgressBar)findViewById(R.id.progressBarLeft);

        //処理開始前の時刻
        long start_point;
        //途中の時刻
        long end_point;
        int time;
        public UpLeg() {
            //処理開始前の時刻
            start_point = System.currentTimeMillis();


            handler = new Handler();

            rightBar.setMax(4000); // 水平プログレスバーの最大値を設定
            rightBar.setProgress(0); // 水平プログレスバーの値を設定
            leftBar.setMax(4000); // 水平プログレスバーの最大値を設定
            leftBar.setProgress(0); // 水平プログレスバーの値を設定
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    end_point = System.currentTimeMillis();
                    //msになっている
                    time = (int) (end_point - start_point);



                    rightBar.setProgress(time); // 水平プログレスバーの値を設定
                    leftBar.setProgress(time-6000); // 水平プログレスバーの値を設定

                    //10s間計測する
                    if(time>=10000){


                        Calibration leftCalibration = new Calibration(48,leftSensor);
                        Calibration rightCalibration = new Calibration(48,rightSensor);

                        //2~3秒目は右足を上げている　
                        ShoesOffset rightOffset = new ShoesOffset(rightSensor,2,3);
                        //7~8秒目は左足を上げている
                        ShoesOffset leftOffset = new ShoesOffset(leftSensor,7,8);;

                        //2~3秒目は右足を上げているので左足に体重がかかっている。つまり、左足の2~3秒目のデータが必要になる
                        leftCalibration.Calc(rightOffset,2,3);
                        //7~8秒目は左足を上げているので右足に体重がかかっている。つまり、右足の7~8秒目のデータが必要になる
                        rightCalibration.Calc(leftOffset,7,8);



                        //2~3秒目は右足を上げている　7~8秒目は左足を上げている
                        multdata = new CorrectionCoefficient(leftOffset,leftCalibration,rightOffset,rightCalibration);

                        timerStop();
                        otukareText.setVisibility(View.VISIBLE);
                        Log.d("serialBT","通信終了_通信切断開始_両足");
                        disconnect(LEFT_LEG);
                        disconnect(RIGHT_LEG);

                        Intent intent = new Intent(getApplication(), DisplayShoes.class);
                        intent.putExtra("metadata",multdata);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext() ,"画面遷移に失敗", Toast.LENGTH_SHORT ).show();
                            e.printStackTrace();
                        }




                    }



                }
            });
        }

    }



    public class SendStartCommand extends TimerTask {
        private Handler handler;

        public SendStartCommand() {
            handler = new Handler();
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("BT_Send","スタートコマンド同時送信開始");
                    write( "s" ,LEFT_LEG);
                    write( "s" ,RIGHT_LEG);
                    upLeg = new UpLeg();
                    //10秒ごとに一度更新をかける
                    progressTimer.schedule(upLeg, 0,10);

                }
            });
        }

    }



    public class ConnectModule extends TimerTask {
        private Handler handler;

        private boolean isPreparationRight = false;
        private boolean isPreparationLeft = false;
        private boolean isStart = false;
        public ConnectModule() {
            handler = new Handler();
        }


        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    //右足左足の応答が帰ってきたらスタートする
                    if(isReceive_right && isReceive_left && isPreparationLeft && isPreparationRight && !isStart){
                        Log.d("BT_Send","スタートコマンド同時送信準備");

                        startMeasurement_button.setEnabled(true);


                        isStart = true;

                    //右足がつながっていて、通信開始していないなら接続命令を出す
                    }else if(!isStart){
                        if(isconnect(LEFT_LEG)&& !isReceive_left) {

                            Log.d("BT_left_Send", "リセットコマンド送信");
                            //必ず止めるコマンドを送っておく
                            write("e", LEFT_LEG);
                            write("r", LEFT_LEG);
                            write("03", LEFT_LEG);
                            write("01", LEFT_LEG);
                            write("02", LEFT_LEG);
                            write("03", LEFT_LEG);
                            write("z1", LEFT_LEG);
                            write("z2", LEFT_LEG);
                            write("z3", LEFT_LEG);
                            isPreparationLeft = true;
                        }
                        if(isconnect(RIGHT_LEG)&& !isReceive_right) {
                            Log.d("BT_right_Send","リセットコマンド送信");
                            //必ず止めるコマンドを送っておく
                            write("e", RIGHT_LEG);
                            write("r", RIGHT_LEG);
                            write("03", RIGHT_LEG);
                            write("01", RIGHT_LEG);
                            write("02", RIGHT_LEG);
                            write("03", RIGHT_LEG);
                            write("z1", RIGHT_LEG);
                            write("z2", RIGHT_LEG);
                            write("z3", RIGHT_LEG);
                            isPreparationRight = true;
                        }


                    }if(!isconnect(RIGHT_LEG)) {
                        connect(RIGHT_LEG);
                        isPreparationRight = false;
                        isStart = false;
                    }
                    if(!isconnect(LEFT_LEG)) {
                        connect(LEFT_LEG);
                        isPreparationLeft = false;
                        isStart = false;
                    }

                }
            });
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warming_up);
        InitSensor();
        //Intent intent = new Intent(getApplication(), MainActivity.class);
        //startActivity(intent);
        //Intent intent = getIntent();
        //rightBTName = intent.getStringExtra("RIGHT");
        //leftBTName = intent.getStringExtra("LEFT");


        startMeasurement_button = (Button)findViewById(R.id.StartMeasurement);
        startMeasurement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimerTask sendStart = new SendStartCommand();
                progressTimer.schedule(sendStart, 1000);
                startMeasurement_button.setEnabled(false);

            }
        });


        softWearStart = false;
        otukareText = findViewById(R.id.otukare);

        //タイマーを新規生成
        connectTimer = new Timer();
        TimerTask timerTask = new ConnectModule();
        connectTimer.scheduleAtFixedRate(timerTask, 0, 500);


        //タイマーを新規生成
        progressTimer = new Timer();



        // Bluetoothアダプタの取得
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService( Context.BLUETOOTH_SERVICE );
        BluetoothAdapter = bluetoothManager.getAdapter();
        if( null == BluetoothAdapter)
        {    // Android端末がBluetoothをサポートしていない
            Toast.makeText( this, R.string.bluetooth_is_not_supported, Toast.LENGTH_SHORT ).show();
            finish();    // アプリ終了宣言
            return;
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
                            isReceive_left = false;
                            isConnctBT_left = false;
                            break;
                        case BlueToothService.LEFT_STATE_CONNECT_START:        // 接続開始
                            Log.d("serialBT", "L接続開始 ");
                            break;
                        case BlueToothService.LEFT_STATE_CONNECT_FAILED:            // 接続失敗
                            Log.d("serialBT", "L接続失敗");
                            isReceive_left = false;
                            Toast.makeText( getApplicationContext(), "左足の接続失敗", Toast.LENGTH_LONG ).show();
                            break;
                        case BlueToothService.LEFT_STATE_CONNECTED:    // 接続完了
                            // GUIアイテムの有効無効の設定
                            // 切断ボタン、文字列送信ボタンを有効にする
                            Log.d("serialBT", "L接続完了");
                            Toast.makeText( getApplicationContext(), "左足の接続完了", Toast.LENGTH_LONG ).show();


                            isConnctBT_left = true;
                            break;
                        case BlueToothService.LEFT_STATE_CONNECTION_LOST:            // 接続ロスト
                            Toast.makeText( getApplicationContext(), "左足との接続が途切れました.", Toast.LENGTH_SHORT ).show();
                            Log.d("serialBT", "L接続ロスト");
                            InitSensor();
                            //timerStop();
                            isReceive_left = false;

                            isConnctBT_left = false;
                            break;
                        case BlueToothService.LEFT_STATE_DISCONNECT_START:    // 切断開始
                            // GUIアイテムの有効無効の設定
                            // 切断ボタン、文字列送信ボタンを無効にする
                            isReceive_left = false;
                            Log.d("serialBT", "L切断開始");
                            break;
                        case BlueToothService.LEFT_STATE_DISCONNECTED:            // 切断完了
                            Log.d("serialBT", "L切断完了");
                            // GUIアイテムの有効無効の設定
                            leftBlueToothService = null;    // BluetoothServiceオブジェクトの解放
                            isConnctBT_left = false;
                            isReceive_left = false;
                            break;
                    }
                    break;
                case BlueToothService.RIGHT_MESSAGE_STATECHANGE:
                    switch( msg.arg1 )
                    {
                        case BlueToothService.RIGHT_STATE_NONE:            // 未接続

                            Log.d("serialBT", "R未接続 ");
                            isConnctBT_right = false;
                            isReceive_right = false;
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECT_START:        // 接続開始
                            Log.d("serialBT", "R接続開始");
                            isReceive_right = false;
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECT_FAILED:            // 接続失敗
                            Log.d("serialBT", "R接続失敗 ");
                            isReceive_right = false;
                            Toast.makeText( getApplicationContext(), "右足の接続失敗", Toast.LENGTH_LONG ).show();
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECTED:    // 接続完了
                            Log.d("serialBT", "R接続完了 ");
                            Toast.makeText( getApplicationContext(), "右足の接続完了", Toast.LENGTH_LONG ).show();
                            isConnctBT_right = true;
                            break;
                        case BlueToothService.RIGHT_STATE_CONNECTION_LOST:            // 接続ロスト
                            Toast.makeText( getApplicationContext(), "右足との接続が途切れました.", Toast.LENGTH_LONG).show();
                            InitSensor();
                            Log.d("serialBT", "R接続ロスト ");
                            //timerStop();
                            isReceive_right = false;

                            isConnctBT_right = false;
                            break;
                        case BlueToothService.RIGHT_STATE_DISCONNECT_START:    // 切断開始
                            Log.d("serialBT", "R接続開始 ");
                            break;
                        case BlueToothService.RIGHT_STATE_DISCONNECTED:            // 切断完了
                            Log.d("serialBT", "R切断完了 ");
                            rightBlueToothService = null;    // BluetoothServiceオブジェクトの解放
                            isConnctBT_right = false;
                            isReceive_right = false;
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

                            Log.d("BT_left_Receive",receiveReadBT);

                            if(receiveReadBT.indexOf("U:") == 0)
                                isReceive_left = true;

                            leftSensor.Set6axis(receiveReadBT);




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
                            Log.d("BT_right_Receive",receiveReadBT);
                            if(receiveReadBT.indexOf("U:") == 0)
                                isReceive_right = true;
                            rightSensor.Set6axis(receiveReadBT);

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

    @Override
    public void onBackPressed(){
        // 切断
        disconnect(LEFT_LEG);
        // 切断
        disconnect(RIGHT_LEG);


        timerStop();
        finish();
        // 行いたい処理
    }

    // アクティビティの終了直前
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // 切断
        disconnect(LEFT_LEG);
        // 切断
        disconnect(RIGHT_LEG);


        timerStop();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // 切断
        disconnect(LEFT_LEG);
        // 切断
        disconnect(RIGHT_LEG);


        timerStop();
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

    private boolean isconnect(int id){
        if(id == LEFT_LEG){

            if( null != leftBlueToothService)
            {    // mBluetoothServiceがnullでないなら接続済みか、接続中。
                return true;
            }else{
                return false;
            }

        }

        if(id == RIGHT_LEG){

            if( null != rightBlueToothService)
            {    // mBluetoothServiceがnullでないなら接続済みか、接続中。
                return true;
            }else{
                return false;
            }
        }
        else{
            return false;
        }
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




    // 初回表示時、および、ポーズからの復帰時
    @Override
    protected void onResume()
    {
        super.onResume();
        InitSensor();

        // Android端末のBluetooth機能の有効化要求
        requestBluetoothFeature();

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




}