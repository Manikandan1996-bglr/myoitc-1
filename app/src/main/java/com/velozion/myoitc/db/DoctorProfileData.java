package com.velozion.myoitc.db;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.velozion.myoitc.Utils;

public class DoctorProfileData implements Parcelable {

    Context context;

    String name;
    String pic;
    String qualification;
    String specialist;
    String experience;
    String about;
    String hospital_name;
    String area;
    String fees;
    String mobile;
    String email;
    String rating;
    String lat;
    String lang;
    String avaliable_timings;

    public DoctorProfileData(Context context) {
        this.context = context;
        Utils.ImageLoaderInitialization(context);
    }

    protected DoctorProfileData(Parcel in) {
        name = in.readString();
        pic = in.readString();
        qualification = in.readString();
        specialist = in.readString();
        experience = in.readString();
        about = in.readString();
        hospital_name = in.readString();
        area = in.readString();
        fees = in.readString();
        mobile = in.readString();
        email = in.readString();
        rating = in.readString();
        lat = in.readString();
        lang = in.readString();
        avaliable_timings = in.readString();
    }

    public static final Creator<DoctorProfileData> CREATOR = new Creator<DoctorProfileData>() {
        @Override
        public DoctorProfileData createFromParcel(Parcel in) {
            return new DoctorProfileData(in);
        }

        @Override
        public DoctorProfileData[] newArray(int size) {
            return new DoctorProfileData[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSpecialist() {
        return specialist;
    }

    public String getExperience() {
        return experience;
    }

    public String getAbout() {
        return about;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getArea() {
        return area;
    }

    public String getFees() {
        return fees;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getRating() {
        return rating;
    }

    public String getLat() {
        return lat;
    }

    public String getLang() {
        return lang;

    }

    public String getAvaliable_timings() {
        return avaliable_timings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setAvaliable_timings(String avaliable_timings) {
        this.avaliable_timings = avaliable_timings;
    }

    @BindingAdapter({"android:profileImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        Utils.LoadImage(imageUrl,view);

    }

    @BindingAdapter("android:rating")
    public static void setRating(RatingBar view, String rating) {

        if (view!=null)
        {

            float rate= Float.parseFloat(rating);

            view.setRating(rate);

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pic);
        dest.writeString(qualification);
        dest.writeString(specialist);
        dest.writeString(experience);
        dest.writeString(about);
        dest.writeString(hospital_name);
        dest.writeString(area);
        dest.writeString(fees);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(rating);
        dest.writeString(lat);
        dest.writeString(lang);
        dest.writeString(avaliable_timings);
    }
}
