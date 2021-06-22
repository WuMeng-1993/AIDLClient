package com.wm.aidlservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author WuMeng
 * @date 2021/6/21
 * desc:
 */
public class Book implements Parcelable {

    private String BookName;

    private int BookId;

    public Book(String bookName, int bookId) {
        BookName = bookName;
        BookId = bookId;
    }

    public Book() {
    }

    protected Book(Parcel in) {
        BookName = in.readString();
        BookId = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BookName);
        dest.writeInt(BookId);
    }

}
