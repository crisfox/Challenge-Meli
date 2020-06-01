package com.java.challengemeli.core;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.squareup.picasso.Picasso;

public class Utils {

    public static String getNumber(String monto) {
        String[] decimalAndNumber = monto.replace("$", "$ ").replace(".", "n").split("n");

        return decimalAndNumber[0].replace(",", ".");
    }

    public static String getDecimal(String monto) {
        String[] decimalAndNumber = monto.replace("$", "").replace(".", "n").split("n");
        if (decimalAndNumber.length == 2) {
            return decimalAndNumber[1].equals("00") ? "" : decimalAndNumber[1];
        } else {
            return "";
        }
    }


    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String poserPath) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(view.getContext());
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Picasso.get().load(poserPath).placeholder(circularProgressDrawable).into(view);
    }
}
