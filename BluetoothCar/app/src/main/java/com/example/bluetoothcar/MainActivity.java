package com.example.bluetoothcar;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.VideoView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private MaterialCardView ileriCardView, geriCardView,sagCardView,solCardView,kornaCardView,baglanCardView,kameraSagCardView,kameraSolCardView,hizAzaltCardView,hizArttirCardView,lightCardView,hizAyarlaCardView;
    private Handler handler;
    private Runnable sendRunnable;
    private volatile boolean isSending = false;
    private String currentCommand = "";
    private TextView Status,hiz_durum;
    private int hiz_veri;
    private static final int REQUEST_BLUETOOTH_CONNECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Navigasyon çubuğunu ana ekrandan gizleme:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);  // Bu satırı doğru konuma taşıdım
        setContentView(R.layout.activity_main); // Bu satırı da doğru konuma taşıdım
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ileriCardView = findViewById(R.id.ileri);
        geriCardView = findViewById(R.id.geri);
        sagCardView= findViewById(R.id.sag);
        solCardView= findViewById(R.id.sol);
        kornaCardView= findViewById(R.id.cardView7);
        baglanCardView=findViewById(R.id.cardView);
        Status=findViewById(R.id.StatusOfConnect);
        hiz_durum=findViewById(R.id.hiz);
        kameraSagCardView=findViewById(R.id.kamera_arttir);
        kameraSolCardView=findViewById(R.id.kamera_azalt);
        hizArttirCardView=findViewById(R.id.arttir);
        hizAzaltCardView=findViewById(R.id.azalt);
        lightCardView=findViewById(R.id.light);
        hizAyarlaCardView=findViewById(R.id.hiz_ayarla);
        handler = new Handler(Looper.getMainLooper());
        hiz_durum.setText("2");
        hiz_veri=2;

        // Bluetooth iznini kontrol et
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            // İzin verilmemişse, kullanıcıdan izin talep et
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_CONNECT);
        }

        sendRunnable = new Runnable() {
            @Override
            public void run() {
                if (isSending) {
                    sendData(currentCommand);  // currentCommand değerini gönder
                    handler.postDelayed(this, 100);  // 100 milisaniye aralıklarla tekrar çalıştır
                }
            }
        };

        ileriCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("b");  // İleri komutu
                        ileriCardView.setCardBackgroundColor(Color.parseColor("#70FFFFFF"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        stopSendingData();
                        ileriCardView.setCardBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        geriCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("s");  // Geri komutu
                        geriCardView.setCardBackgroundColor(Color.parseColor("#70FFFFFF"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        geriCardView.setCardBackgroundColor(Color.WHITE);
                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        sagCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("d");  // Geri komutu
                        sagCardView.setCardBackgroundColor(Color.parseColor("#70FFFFFF"));

                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        sagCardView.setCardBackgroundColor(Color.WHITE);
                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        solCardView .setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("a");  // sol komutu
                        solCardView.setCardBackgroundColor(Color.parseColor("#70FFFFFF"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        solCardView.setCardBackgroundColor(Color.WHITE);
                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        kornaCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("q");  // İleri komutu
                        kornaCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        kornaCardView.setCardBackgroundColor(Color.BLACK);

                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        kameraSagCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("+");  // İleri komutu
                        kameraSagCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        kameraSagCardView.setCardBackgroundColor(Color.BLACK);
                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        kameraSolCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSendingData("-");  // İleri komutu
                        kameraSolCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
                        break;
                    case MotionEvent.ACTION_UP:
                        sendData("x");  // Dur komutu
                        kameraSolCardView.setCardBackgroundColor(Color.BLACK);
                        stopSendingData();
                        break;
                }
                return true;
            }
        });
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.1.194:8080/video");
    }
    private void startSendingData(String command) {
        if (!isSending) {
            currentCommand = command;
            isSending = true;
            handler.post(sendRunnable);  // Veriyi göndermeye başla
        }
    }

    private void stopSendingData() {
        if (isSending) {
            isSending = false;
            handler.removeCallbacks(sendRunnable);  // Veri göndermeyi durdur
        }
    }
    // Kullanıcı izni yanıtladığında bu yöntem çağrılır
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Kullanıcı izni verdi
                connect(baglanCardView);

            } else {
                // Kullanıcı izni reddetti
                Toast.makeText(this, "Bluetooth bağlantısı için izin gerekli", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void connect(View view) {
        BluetoothDevice arduinoDevice = bluetoothAdapter.getRemoteDevice("58:56:00:01:1E:B6");
        try {
            bluetoothSocket = arduinoDevice.createRfcommSocketToServiceRecord(BT_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            showToast("Arduino'ya bağlandı.");
            Status.setText("BAĞLANTI SAĞLANDI");
            baglanCardView.setCardBackgroundColor(R.color.green);
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Arduino'ya bağlanırken bir hata oluştu.");
            baglanCardView.setCardBackgroundColor(R.color.red2);
        }
    }

    private void sendData(String data) {
        try {
            if (outputStream != null) {
                outputStream.write(data.getBytes());
                //showToast("Veri gönderildi: " + data);
            } else {
                showToast("Bağlı bir Arduino bulunamadı.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Veri gönderilirken bir hata oluştu.");
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void open_light(View view) {
        lightCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
        new Handler().postDelayed(() -> {
            lightCardView.setCardBackgroundColor(Color.BLACK);
        }, 150);
        sendData("z");
    }
    public void hiz_arttir(View view) {
        hizArttirCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
        new Handler().postDelayed(() -> {
            hizArttirCardView.setCardBackgroundColor(Color.BLACK);
        }, 150);
        System.out.println("işik");
        hiz_veri++;
        if(hiz_veri<1){
            hiz_veri=1;
        }
        else if (hiz_veri>4){
            hiz_veri=4;
        }
        System.out.println("a:"+hiz_veri);
        String a=Integer.toString(hiz_veri);
        hiz_durum.setText(a);
    }
    public void hiz_azalt(View view) {
        hizAzaltCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
        new Handler().postDelayed(() -> {
            hizAzaltCardView.setCardBackgroundColor(Color.BLACK);
        }, 150);
        System.out.println("işik");
        System.out.println("a:"+hiz_veri);
        hiz_veri--;
        if(hiz_veri<1){
            hiz_veri=1;
        }
        else if (hiz_veri>4){
            hiz_veri=4;
        }
        String a=Integer.toString(hiz_veri);
        hiz_durum.setText(a);
    }
    public void hiz_gonderme_butonu(View view){
        hizAyarlaCardView.setCardBackgroundColor(Color.parseColor("#70F8F8F8"));
        new Handler().postDelayed(() -> {
            hizAyarlaCardView.setCardBackgroundColor(Color.BLACK);
        }, 150);
        hiz_gonder(hiz_veri);
    }
    public void hiz_gonder(int veri) {
        System.out.println("işik");
        if(veri<1){
            veri=1;
        }
        else if (veri>4){
            veri=4;
        }
        switch (veri) {
            case 1:
                sendData("1");
                break;
            case 2:
                sendData("2");
                break;
            case 3:
                sendData("3");
                break;
            case 4:
                sendData("4");
                break;
        }
        String a=Integer.toString(veri);
        hiz_durum.setText(a);
    }

    public void down(View view) {
        System.out.println("aşağı");
    }

    public void right(View view) {
        System.out.println("sağ");
    }

    public void left(View view) {
        System.out.println("sol");
    }
}
