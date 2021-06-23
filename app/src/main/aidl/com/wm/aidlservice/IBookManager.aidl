// IBookManager.aidl
package com.wm.aidlservice;

// Declare any non-default types here with import statements
import com.wm.aidlservice.Book;
import com.wm.aidlservice.IOnNewBookArrivedListener;

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

    void registerListener(IOnNewBookArrivedListener listener);

    void unregisterListener(IOnNewBookArrivedListener listener);

}