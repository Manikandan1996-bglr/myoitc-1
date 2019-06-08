package com.velozion.myoitc.db;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    String id;
    String pic;
    String name;
    String username;
    String email;


    public UserProfile() {

    }

    protected UserProfile(Parcel in) {
        id = in.readString();
        pic = in.readString();
        name = in.readString();
        username = in.readString();
        email = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pic);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(email);
    }
}
