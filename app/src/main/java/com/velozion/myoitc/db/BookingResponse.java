package com.velozion.myoitc.db;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingResponse implements Parcelable {


   String booking_id;
   String personanme;
   String date;
   String time;


    public BookingResponse(String booking_id, String personanme, String date, String time) {
        this.booking_id = booking_id;
        this.personanme = personanme;
        this.date = date;
        this.time = time;
    }

    protected BookingResponse(Parcel in) {
        booking_id = in.readString();
        personanme = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<BookingResponse> CREATOR = new Creator<BookingResponse>() {
        @Override
        public BookingResponse createFromParcel(Parcel in) {
            return new BookingResponse(in);
        }

        @Override
        public BookingResponse[] newArray(int size) {
            return new BookingResponse[size];
        }
    };

    public String getBooking_id() {
        return booking_id;
    }

    public String getPersonanme() {
        return personanme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public void setPersonanme(String personanme) {
        this.personanme = personanme;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(booking_id);
        dest.writeString(personanme);
        dest.writeString(date);
        dest.writeString(time);
    }
}
