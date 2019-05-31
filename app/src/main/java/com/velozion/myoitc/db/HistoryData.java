package com.velozion.myoitc.db;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class HistoryData implements Parcelable {


    Context context;

    String checkinloc;
    String checkintime;
    String checkoutloc;
    String checkouttime;
    String checkinLat;
    String checkinLang;
    String checkoutLan;
    String checkoutLang;


    public HistoryData(Context context) {
        this.context = context;
    }

    public HistoryData(Parcel in) {
        checkinloc = in.readString();
        checkintime = in.readString();
        checkoutloc = in.readString();
        checkouttime = in.readString();
        checkinLat = in.readString();
        checkinLang = in.readString();
        checkoutLan = in.readString();
        checkoutLang = in.readString();
    }

    public static final Creator<HistoryData> CREATOR = new Creator<HistoryData>() {
        @Override
        public HistoryData createFromParcel(Parcel in) {
            return new HistoryData(in);
        }

        @Override
        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };

    public String getCheckinloc() {
        return checkinloc;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public String getCheckoutloc() {
        return checkoutloc;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public String getCheckinLat() {
        return checkinLat;
    }

    public String getCheckinLang() {
        return checkinLang;
    }

    public String getCheckoutLan() {
        return checkoutLan;
    }

    public String getCheckoutLang() {
        return checkoutLang;
    }

    public void setCheckinloc(String checkinloc) {
        this.checkinloc = checkinloc;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public void setCheckoutloc(String checkoutloc) {
        this.checkoutloc = checkoutloc;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public void setCheckinLat(String checkinLat) {
        this.checkinLat = checkinLat;
    }

    public void setCheckinLang(String checkinLang) {
        this.checkinLang = checkinLang;
    }

    public void setCheckoutLan(String checkoutLan) {
        this.checkoutLan = checkoutLan;
    }

    public void setCheckoutLang(String checkoutLang) {
        this.checkoutLang = checkoutLang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(checkinloc);
        dest.writeString(checkintime);
        dest.writeString(checkoutloc);
        dest.writeString(checkouttime);
        dest.writeString(checkinLat);
        dest.writeString(checkinLang);
        dest.writeString(checkoutLan);
        dest.writeString(checkoutLang);
    }
}
