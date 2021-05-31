package bluetoothcommunicator;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

class BlueToothService
{
    // 定数（Bluetooth UUID）
    private static final UUID UUID_SPP = UUID.fromString( "00001101-0000-1000-8000-00805f9b34fb" );

    private static final int LEFT_LEG = 1;
    private static final int RIGHT_LEG = 2;
    // 定数
    public static final int LEFT_MESSAGE_STATECHANGE    = 1;
    public static final int LEFT_MESSAGE_READ           = 2;
    public static final int LEFT_MESSAGE_WRITTEN        = 3;
    public static final int LEFT_STATE_NONE             = 0;
    public static final int LEFT_STATE_CONNECT_START    = 1;
    public static final int LEFT_STATE_CONNECT_FAILED   = 2;
    public static final int LEFT_STATE_CONNECTED        = 3;
    public static final int LEFT_STATE_CONNECTION_LOST  = 4;
    public static final int LEFT_STATE_DISCONNECT_START = 5;
    public static final int LEFT_STATE_DISCONNECTED     = 6;
    // 定数
    public static final int RIGHT_MESSAGE_STATECHANGE    = 11;
    public static final int RIGHT_MESSAGE_READ           = 12;
    public static final int RIGHT_MESSAGE_WRITTEN        = 13;
    public static final int RIGHT_STATE_NONE             = 10;
    public static final int RIGHT_STATE_CONNECT_START    = 11;
    public static final int RIGHT_STATE_CONNECT_FAILED   = 12;
    public static final int RIGHT_STATE_CONNECTED        = 13;
    public static final int RIGHT_STATE_CONNECTION_LOST  = 14;
    public static final int RIGHT_STATE_DISCONNECT_START = 15;
    public static final int RIGHT_STATE_DISCONNECTED     = 16;


    // メンバー変数
    private int State;
    private ConnectionThread mConnectionThread;
    private Handler mHandler;


    private ConnectionThread rightConnectionThread;
    private int id;


    // コンストラクタ
    public BlueToothService(Context context, Handler handler, BluetoothDevice device , int ID)
    {
        id = ID;

        if(ID == LEFT_LEG) {
            mHandler = handler;
            State = LEFT_STATE_NONE;
        }
        if(ID == RIGHT_LEG) {
            mHandler = handler;
            State = RIGHT_STATE_NONE;
        }
        // 接続時処理用スレッドの作成と開始
        mConnectionThread = new ConnectionThread( device );
        mConnectionThread.start();


    }

    // 接続時処理用のスレッド
    private class ConnectionThread extends Thread
    {
        private BluetoothSocket mBluetoothSocket;
        private InputStream mInput;
        private OutputStream mOutput;

        // コンストラクタ
        public ConnectionThread( BluetoothDevice bluetoothdevice )
        {
            try
            {
                mBluetoothSocket = bluetoothdevice.createRfcommSocketToServiceRecord( UUID_SPP );
                mInput = mBluetoothSocket.getInputStream();
                mOutput = mBluetoothSocket.getOutputStream();
            }
            catch( IOException e )
            {
                Log.e( "BluetoothService", "failed : bluetoothdevice.createRfcommSocketToServiceRecord( UUID_SPP )", e );
            }


        }
        // バイト列送信
        public void write( byte[] buf )
        {
            try
            {
                synchronized( BlueToothService.this )
                {
                    mOutput.write( buf );
                }
                if(id == LEFT_LEG) {
                    mHandler.obtainMessage(LEFT_MESSAGE_WRITTEN).sendToTarget();
                }else if (id == RIGHT_LEG){
                    mHandler.obtainMessage(RIGHT_MESSAGE_WRITTEN).sendToTarget();
                }

            }
            catch( IOException e )
            {
                Log.e( "BluetoothService", "Failed : mBluetoothSocket.close()", e );
            }
        }
        // 処理
        public void run()
        {
            if(id == LEFT_LEG) {
                while (LEFT_STATE_DISCONNECTED != State) {
                    switch (State) {
                        case LEFT_STATE_NONE:
                            break;
                        case LEFT_STATE_CONNECT_START:    // 接続開始
                            try {
                                // BluetoothSocketオブジェクトを用いて、Bluetoothデバイスに接続を試みる。
                                mBluetoothSocket.connect();
                            } catch (IOException e) {    // 接続失敗
                                Log.d("BluetoothService", "Failed : mBluetoothSocket.connect()");
                                setState(LEFT_STATE_CONNECT_FAILED);
                                cancel();    // スレッド終了。
                                return;
                            }
                            // 接続成功
                            setState(LEFT_STATE_CONNECTED);
                            break;
                        case LEFT_STATE_CONNECT_FAILED:        // 接続失敗
                            // 接続失敗時の処理の実体は、cancel()。
                            break;
                        case LEFT_STATE_CONNECTED:        // 接続済み（Bluetoothデバイスから送信されるデータ受信）
                            byte[] buf = new byte[1024];
                            int bytes;
                            try {
                                bytes = mInput.read(buf);
                                mHandler.obtainMessage(LEFT_MESSAGE_READ, bytes, -1, buf).sendToTarget();
                            } catch (IOException e) {
                                setState(LEFT_STATE_CONNECTION_LOST);
                                cancel();    // スレッド終了。
                                break;
                            }
                            break;
                        case LEFT_STATE_CONNECTION_LOST:    // 接続ロスト
                            // 接続ロスト時の処理の実体は、cancel()。
                            break;
                        case LEFT_STATE_DISCONNECT_START:    // 切断開始
                            // 切断開始時の処理の実体は、cancel()。
                            break;
                        case LEFT_STATE_DISCONNECTED:    // 切断完了
                            // whileの条件式により、STATE_DISCONNECTEDの場合は、whileを抜けるので、このケース分岐は無意味。
                            break;
                    }
                }
                synchronized (BlueToothService.this) {    // 親クラスが保持する自スレッドオブジェクトの解放（自分自身の解放）
                    mConnectionThread = null;
                }
            }

            if(id == RIGHT_LEG) {
                while (RIGHT_STATE_DISCONNECTED != State) {
                    switch (State) {
                        case RIGHT_STATE_NONE:
                            break;
                        case RIGHT_STATE_CONNECT_START:    // 接続開始
                            try {
                                // BluetoothSocketオブジェクトを用いて、Bluetoothデバイスに接続を試みる。
                                mBluetoothSocket.connect();
                            } catch (IOException e) {    // 接続失敗
                                Log.d("BluetoothService", "Failed : mBluetoothSocket.connect()");
                                setState(RIGHT_STATE_CONNECT_FAILED);
                                cancel();    // スレッド終了。
                                return;
                            }
                            // 接続成功
                            setState(RIGHT_STATE_CONNECTED);
                            break;
                        case RIGHT_STATE_CONNECT_FAILED:        // 接続失敗
                            // 接続失敗時の処理の実体は、cancel()。
                            break;
                        case RIGHT_STATE_CONNECTED:        // 接続済み（Bluetoothデバイスから送信されるデータ受信）
                            byte[] buf = new byte[1024];
                            int bytes;
                            try {
                                bytes = mInput.read(buf);
                                mHandler.obtainMessage(RIGHT_MESSAGE_READ, bytes, -1, buf).sendToTarget();
                            } catch (IOException e) {
                                setState(RIGHT_STATE_CONNECTION_LOST);
                                cancel();    // スレッド終了。
                                break;
                            }
                            break;
                        case RIGHT_STATE_CONNECTION_LOST:    // 接続ロスト
                            // 接続ロスト時の処理の実体は、cancel()。
                            break;
                        case RIGHT_STATE_DISCONNECT_START:    // 切断開始
                            // 切断開始時の処理の実体は、cancel()。
                            break;
                        case RIGHT_STATE_DISCONNECTED:    // 切断完了
                            // whileの条件式により、STATE_DISCONNECTEDの場合は、whileを抜けるので、このケース分岐は無意味。
                            break;
                    }
                }
                synchronized (BlueToothService.this) {    // 親クラスが保持する自スレッドオブジェクトの解放（自分自身の解放）
                    mConnectionThread = null;
                }
            }
        }

        // キャンセル（接続を終了する。ステータスをSTATE_DISCONNECTEDにすることによってスレッドも終了する）
        public void cancel()
        {
            try
            {
                mBluetoothSocket.close();
            }
            catch( IOException e )
            {
                Log.e( "BluetoothService", "Failed : mBluetoothSocket.close()", e );
            }
            if(id==LEFT_LEG){
                setState( LEFT_STATE_DISCONNECTED );
            }else if(id == RIGHT_LEG){
                setState( RIGHT_STATE_DISCONNECTED );

            }
        }



    }

    // バイト列送信（非同期）
    public void write( byte[] out )
    {
        ConnectionThread connectionThread;
        synchronized( this )
        {
            if( LEFT_STATE_CONNECTED != State && id == LEFT_LEG)
            {
                return;
            }
            if( RIGHT_STATE_CONNECTED != State && id == RIGHT_LEG)
            {
                return;
            }
            connectionThread = mConnectionThread;
        }
        // 非同期送信
        // （送受信で同期（送信と受信を排他処理（≒同期処理））させる実装も可能だが、
        // 　そうすると、mInput.read( buf ) が完了するまで、mOutput.write( buf ) が実施されなくなる。
        // 　mInput.read( buf ) は文字列を受信すると完了するので、文字列を受信しなければいつまでたっても完了しない。
        // 　文字列が頻繁に送信されてくる場合はよいが、文字列がぜんぜん送信されてこない場合は、
        // 　こちらからの送信がいつまでたっても実施されないことになる。なので、受信と送信は非同期。）
        connectionThread.write( out );
    }


    // ステータス設定
    private synchronized void setState( int state )
    {
        State = state;
        if(id == LEFT_LEG) {
            mHandler.obtainMessage(LEFT_MESSAGE_STATECHANGE, state, -1).sendToTarget();
        }else if(id==RIGHT_LEG){
            mHandler.obtainMessage(RIGHT_MESSAGE_STATECHANGE, state, -1).sendToTarget();

        }
    }

    // 接続開始時の処理
    public synchronized void connect()
    {
        if( LEFT_STATE_NONE != State && id == LEFT_LEG)
        {    // １つのBluetoothServiceオブジェクトに対して、connect()は１回だけ呼べる。
            // ２回目以降の呼び出しは、処理しない。
            return;
        }
        if( RIGHT_STATE_NONE != State && id == RIGHT_LEG)
        {    // １つのBluetoothServiceオブジェクトに対して、connect()は１回だけ呼べる。
            // ２回目以降の呼び出しは、処理しない。
            return;
        }

        // ステータス設定
        if(id == LEFT_LEG) {
            setState(LEFT_STATE_CONNECT_START);
        }else if(id == RIGHT_LEG){
            setState(RIGHT_STATE_CONNECT_START);
        }
    }

    // 接続切断時の処理
    public synchronized void disconnect()
    {
        if( LEFT_STATE_CONNECTED != State&& id == LEFT_LEG)
        {    // 接続中以外は、処理しない。
            return;
        }
        if( RIGHT_STATE_CONNECTED != State&& id == RIGHT_LEG)
        {    // 接続中以外は、処理しない。
            return;
        }


        if(id == LEFT_LEG) {
            setState(LEFT_STATE_DISCONNECT_START);
        }else if(id == RIGHT_LEG){
            setState(RIGHT_STATE_DISCONNECT_START);
        }


        mConnectionThread.cancel();
    }
}