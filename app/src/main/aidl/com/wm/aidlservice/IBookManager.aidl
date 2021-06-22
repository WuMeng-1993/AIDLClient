// IBookManager.aidl
package com.wm.aidlservice;

// Declare any non-default types here with import statements
import com.wm.aidlservice.Book;

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

}