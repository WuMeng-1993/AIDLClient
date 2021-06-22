package com.wm.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wm.aidlservice.Book;
import com.wm.aidlservice.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuMeng
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WU-MENG";

    private Button btnAddBook, btnGetBookList;

    /**
     * 是否连接Service
     */
    private boolean isConnect = false;

    private IBookManager bookManager;

    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();

    }

    /**
     * 初始化View
     */
    private void initView() {
        btnAddBook = findViewById(R.id.btn_addBook);
        btnGetBookList = findViewById(R.id.btn_getBookList);
    }

    /**
     * 点击事件
     */
    private void initClick() {
        // getBookList
        btnGetBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookList = bookManager.getBookList();
                    Log.d(TAG, "bookList is " + bookList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        // addBook
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book("第二行代码",90909);
                try {
                    bookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnect) {
            attemptToBindService();
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "service connected");
            isConnect = true;
            bookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"service disconnected");
            isConnect = false;
        }
    };

    /**
     * 尝试去绑定Service
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.wm.aidlservice.BookManagerService");
        intent.setPackage("com.wm.aidlservice");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnect) {
            unbindService(serviceConnection);
            isConnect = false;
        }
    }
}