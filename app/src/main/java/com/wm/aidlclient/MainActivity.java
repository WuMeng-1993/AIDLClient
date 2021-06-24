package com.wm.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wm.aidlservice.Book;
import com.wm.aidlservice.IBookManager;
import com.wm.aidlservice.IOnNewBookArrivedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuMeng
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WU-MENG";

    private Button btnAddBook, btnGetBookList;

    private IBookManager mRemoteBookManager;

    /**
     * 图书列表
     */
    private List<Book> bookList = new ArrayList<>();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"接收到图书"+newBook.getBookName());
                }
            });
        }
    };

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
                    bookList = mRemoteBookManager.getBookList();
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
                    mRemoteBookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        attemptToBindService();
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "service connected");
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> list = mRemoteBookManager.getBookList();
                Log.d(TAG,"list的长度为" + list.size());

                Book book = new Book("一本新书",3);
                mRemoteBookManager.addBook(book);
                Log.d(TAG,"添加一本新书");

                List<Book> newList = mRemoteBookManager.getBookList();
                Log.d(TAG,"新的List的长度为"+newList.size());

                bookManager.registerListener(listener);
                Log.d(TAG,"开始注册");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.d(TAG,"service disconnected");
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
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.d(TAG,"开始解注册");
                mRemoteBookManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}