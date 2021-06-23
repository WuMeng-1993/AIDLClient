// IOnNewBookArrivedListener.aidl
package com.wm.aidlservice;

// Declare any non-default types here with import statements
import com.wm.aidlservice.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}