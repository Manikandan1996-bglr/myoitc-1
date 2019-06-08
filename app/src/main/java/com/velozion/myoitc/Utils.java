package com.velozion.myoitc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by JAGADISH on 8/20/2018.
 */

public class Utils {

    static ProgressDialog progressDialog;
    static Dialog dialog;

    public static String appName = "Myoitc";
    public static String LoginApi = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=login&format=json&=employer&=demo&ignoreMessages=0";
    public static String CheckinApi = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=loc_update&format=json&ignoreMessages=0";
    public static String CheckOutApi = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=loc_update&format=json&ignoreMessages=0";
    public static String HistoryApi = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=getmylocation&format=json&ignoreMessages=0";
    public static String ProfileApi = "http://www.myoitc.com/beta/?option=com_ajax&group=cmajax&plugin=cmmap&type=getProfile&format=json";
    public static String DocterListApi = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1s8a9XLIMB-jChvQIGhli5579HGme_WMKR_enz08rce0&sheet=Sheet1";
    //"https://api.myjson.com/bins/pdbsb";
    //"https://api.myjson.com/bins/fmufb";
    Context context;
    static DisplayImageOptions options;
    static ImageLoaderConfiguration imgconfig;

    public static void displayProgressDailog(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading..");
        progressDialog.show();

    }

    public static void dismissProgressDailog() {

        progressDialog.dismiss();

    }


    public static void displayCustomDailog(Context context) {

        dialog = new Dialog(context, android.R.style.Theme_Black);
        View view2 = LayoutInflater.from(context).inflate(R.layout.progressbar_bg, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent2);
        dialog.setContentView(view2);
        dialog.show();

    }

    public static void dismissCustomDailog() {

        dialog.dismiss();

    }

    public static void ImageLoaderInitialization(Context context) {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_profile_pic)
                .showImageForEmptyUri(R.drawable.icon_profile_pic)
                .showImageOnFail(R.drawable.icon_profile_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.NONE)
                .build();

        imgconfig = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(imgconfig);

    }

    public static void LoadImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }


    public static void displayFullImage(Context context, String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_img);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        ImageLoader.getInstance().displayImage(image, iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}
