package com.velozion.myoitc;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {


    private static Dialog dialog;

    public static String appName = "Myoitc";
    public static String LoginAPI = "http://www.myoitc.com/beta1/?option=com_ajax&group=cmajax&plugin=cmmap&type=login&format=json&=employer&=demo&ignoreMessages=0";
    public static String CheckinAPI = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=loc_update&format=json&ignoreMessages=0";
    public static String CheckOutAPI = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=loc_update&format=json&ignoreMessages=0";
    public static String HistoryAPI = "https://api.myjson.com/bins/8a64q";
    public static String ProfileAPI = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=getProfile&format=json";
//    public static String TaskListApi = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1s8a9XLIMB-jChvQIGhli5579HGme_WMKR_enz08rce0&sheet=TaskEvents";

    public static String ClientListAPI = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1s8a9XLIMB-jChvQIGhli5579HGme_WMKR_enz08rce0&sheet=Clients";
    public static String ServicesListAPI = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1s8a9XLIMB-jChvQIGhli5579HGme_WMKR_enz08rce0&sheet=Services";


    public static String ClientChartListAPI = "https://api.myjson.com/bins/ior0a";
    public static String EmployeeListAPI = "https://api.myjson.com/bins/wckt6";
    public static String RequestListAPI = "https://api.myjson.com/bins/i5toi";
    public static String EmployeeFolderAPI = "https://api.myjson.com/bins/10uwgu";
    public static String TimeSheetAPI = "https://api.myjson.com/bins/18u1r2";
    public static String ChoosePackagesAPI = "https://api.myjson.com/bins/1gutqc";
    public static String TaskManagementAPI = "https://api.myjson.com/bins/1bypoy";
    public static String AbsentTrackerAPI = "https://api.myjson.com/bins/wckt6";
    public static String travelMapAPI = "https://api.myjson.com/bins/7hav4";
    public static String ClientServiceAcknowledgementAPI = "https://api.myjson.com/bins/ixlrk";
    public static String ProviderReferralAPI = "https://api.myjson.com/bins/dk1bo";
    public static String TreatmentCarePlanAPI = "https://api.myjson.com/bins/f3z7o";

    public static String Spinner_Referral_source = "https://api.myjson.com/bins/1g4itq";
    public static String Spinner_Medicaid_API = "https://api.myjson.com/bins/pe5ri";
    public static String Spinner_Physician_API = " https://api.myjson.com/bins/16pvgu";
    public static String Spinner_Primary_Diagnosis_API = "https://api.myjson.com/bins/q1qj2";
    public static String Spinner_Secondary_Diagnosis_API = "https://api.myjson.com/bins/6akoe";
    public static String Spinner_other_Diagnosis_API = "https://api.myjson.com/bins/11b9am";
    public static String Spinner_Service_requestedAPI = "https://api.myjson.com/bins/1aekh2";

    public static String Spinner_CustomerServiceRequestedApi = "https://api.myjson.com/bins/ukpcm";
    public static String Spinner_CustomerServiceLocationedApi = "https://api.myjson.com/bins/13qv5i";


    static DisplayImageOptions options;
    static ImageLoaderConfiguration imgconfig;
    static LocationManager locationManager;


    public static void displayCustomDailog(Context context) {

        dialog = new Dialog( context, android.R.style.Theme_Black );
        View view2 = LayoutInflater.from( context ).inflate( R.layout.progressbar_bg, null );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.getWindow().setBackgroundDrawableResource( R.color.transparent2 );
        dialog.setContentView( view2 );
        dialog.show();

    }

    public static void dismissCustomDailog() {

        dialog.dismiss();

    }

    public static void ImageLoaderInitialization(Context context) {

        imgconfig = new ImageLoaderConfiguration.Builder( context )
                .build();
        ImageLoader.getInstance().init( imgconfig );

    }

    public static void LoadImage(String url, ImageView imageView) {

        options = new DisplayImageOptions.Builder()
                .showImageOnFail( R.drawable.image_placeholder )
                .showImageForEmptyUri( R.drawable.image_placeholder )
                .cacheInMemory( true )
                .cacheOnDisk( true )
                .considerExifParams( true )
                .displayer( new SimpleBitmapDisplayer() )
                .imageScaleType( ImageScaleType.NONE )
                .build();

        ImageLoader.getInstance().displayImage( url, imageView, options );
    }

    public static boolean checkLocationEnabled(Context context) {
        locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        //not enabled
        return locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );

    }

    public static String getMonthName(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy", Locale.US );
        Date d = dateFormat.parse( date );

        SimpleDateFormat monthformat = new SimpleDateFormat( "MM", Locale.US );
        int month = Integer.parseInt( monthformat.format( d.getTime() ) );


        return convertNumToMonth( month );
    }

    public static int getMonthNum(String date) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy", Locale.US );
        Date d = dateFormat.parse( date );
        SimpleDateFormat monthformat = new SimpleDateFormat( "MM", Locale.US );
        int month = Integer.parseInt( monthformat.format( d.getTime() ) );
        month--;
        return month;
    }


    public static String getDay(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy", Locale.US );
        Date d = dateFormat.parse( date );
        SimpleDateFormat dayformat = new SimpleDateFormat( "dd", Locale.US );
        return dayformat.format( d.getTime() );
    }

    public static String getyear(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy", Locale.US );
        Date d = dateFormat.parse( date );
        SimpleDateFormat yearformat = new SimpleDateFormat( "yyyy", Locale.US );
        return yearformat.format( d.getTime() );
    }


    private static String convertNumToMonth(int month) {
        String value = null;

        switch (month) {
            case 1:
                value = "Jan";
                break;

            case 2:
                value = "Feb";
                break;
            case 3:
                value = "Mar";
                break;
            case 4:
                value = "Apr";
                break;

            case 5:
                value = "May";
                break;

            case 6:
                value = "Jun";
                break;

            case 7:
                value = "Jul";
                break;
            case 8:
                value = "Aug";
                break;
            case 9:
                value = "Sep";
                break;
            case 10:
                value = "Oct";
                break;
            case 11:
                value = "Nov";
                break;
            case 12:
                value = "Dec";
                break;


        }

        return value;
    }

    public static String getTodayDate(Context context) {
        String date;
        Calendar calendar = Calendar.getInstance();
        date = calendar.get( Calendar.DAY_OF_MONTH ) + "-" + calendar.get( Calendar.MONTH ) + "-" + calendar.get( Calendar.YEAR );
        return date;
    }


    public static String getCurrentTiming(Context context) {
        String time;
        Calendar calendar = Calendar.getInstance();
        time = calendar.get( Calendar.HOUR ) + ":" + calendar.get( Calendar.MINUTE );
        return time;
    }


    public static String getWelcomeMsg() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime( dt );
        int hours = c.get( Calendar.HOUR_OF_DAY );

        String greeting = "Good Night";
        if (hours >= 1 && hours <= 11) {
            greeting = "Good Morning";
        } else if (hours <= 15) {
            greeting = "Good Afternoon";
        } else if (hours <= 20) {
            greeting = "Good Evening";
        }
        return greeting;
    }


}
