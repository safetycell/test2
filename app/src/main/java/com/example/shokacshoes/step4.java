package com.example.shokacshoes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import bluetoothcommunicator.DeviceListActivity;
import login.ui.login.LoginActivity;

public class step4 extends AppCompatActivity {
    private static final int REQUEST_ENABLEBLUETOOTH = 1; // Bluetooth機能の有効化要求時の識別コード
    private static final int REQUEST_CONNECTDEVICE_L = 2; // デバイス接続要求時の識別コード左
    private static final int REQUEST_CONNECTDEVICE_R   = 3; // デバイス接続要求時の識別コード右
    private static final int READBUFFERSIZE          = 1024;    // 受信バッファーのサイズ

    private BluetoothAdapter BluetoothAdapter;    // BluetoothAdapter : Bluetooth処理で必要
    private String rightDeviceAddress;
    private String leftDeviceAddress;
    private String rightDeviceName;
    private String leftDeviceName;

    private Button to_login_button;
    private TextView rightLavel;
    private TextView leftLavel;
    private boolean isConnectRight = false;
    private boolean isConnectleft = false;
    private BlueToothDeviceList blueToothDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step4);
        rightLavel = findViewById(R.id.right_lavel);
        leftLavel = findViewById(R.id.left_lavel);

        blueToothDeviceList = new BlueToothDeviceList();

        //ボタン処理
        to_login_button = (Button)findViewById(R.id.to_login);
        to_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                //Intent intent = new Intent(getApplication(), WarmingUp.class);
                Log.d("message","step4へ移動");

//                intent.putExtra("RIGHT", rightDeviceAddress);
//                intent.putExtra("LEFT", leftDeviceAddress);
//                intent.putExtra("DEVICE", rightDeviceName);

                intent.putExtra("BlueToothDeviceList",blueToothDeviceList);

                startActivity(intent);
            }
        });

        Button rightBLE = (Button)findViewById(R.id.right_button);
        rightBLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devicelistactivityIntent2 = new Intent( getApplicationContext(), DeviceListActivity.class );
                startActivityForResult( devicelistactivityIntent2, REQUEST_CONNECTDEVICE_R );
            }
        });
        Button leftBLE = (Button)findViewById(R.id.StartButton);
        leftBLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devicelistactivityIntent = new Intent( getApplicationContext(), DeviceListActivity.class );
                startActivityForResult( devicelistactivityIntent, REQUEST_CONNECTDEVICE_L);
            }
        });

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

    // 初回表示時、および、ポーズからの復帰時
    @Override
    protected void onResume()
    {
        super.onResume();

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
                if( Activity.RESULT_OK == resultCode )
                {
                    // デバイスリストアクティビティからの情報の取得
                    leftDeviceName = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_NAME );
                    leftDeviceAddress = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_ADDRESS );
                    Toast.makeText( this, leftDeviceAddress, Toast.LENGTH_LONG ).show();
                    blueToothDeviceList.setL(leftDeviceAddress,leftDeviceName);
                    Log.d("connect_bt_L",leftDeviceAddress);

                    leftLavel.setText("接続完了");
                    leftLavel.setBackgroundColor(Color.GREEN);
                    isConnectleft = true;

                }
                else
                {
                    leftDeviceName = "接続なし";
                    leftDeviceAddress = "";
                    leftLavel.setText("未接続");
                    leftLavel.setBackgroundColor(0xFF00BCD4);
                    isConnectleft = false;
                }
                break;
            case REQUEST_CONNECTDEVICE_R: // デバイス接続要求
                if( Activity.RESULT_OK == resultCode )
                {
                    // デバイスリストアクティビティからの情報の取得
                    rightDeviceName = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_NAME );
                    rightDeviceAddress = data.getStringExtra( DeviceListActivity.EXTRAS_DEVICE_ADDRESS );
                    Toast.makeText( this, rightDeviceAddress, Toast.LENGTH_LONG ).show();
                    blueToothDeviceList.setR(rightDeviceAddress,rightDeviceName);
                    Log.d("connect_bt_R",rightDeviceAddress);
                    rightLavel.setText("接続完了");
                    rightLavel.setBackgroundColor(Color.GREEN);
                    isConnectRight = true;
                }
                else
                {
                    rightDeviceName = "接続なし";
                    rightDeviceAddress = "";
                    rightLavel.setText("未接続");
                    rightLavel.setBackgroundColor(0xFF00BCD4);
                    isConnectRight = false;

                }
                break;


        }
        if(isConnectRight && isConnectleft) {
            to_login_button.setEnabled(true);
            to_login_button.setBackgroundColor(0xFFFFC107);
        }else{
            to_login_button.setEnabled(false);
            to_login_button.setBackgroundColor(0xFFF3F3F3);

        }

        super.onActivityResult( requestCode, resultCode, data );
    }
}