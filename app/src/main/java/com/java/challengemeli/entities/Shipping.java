package com.java.challengemeli.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Shipping implements Parcelable {

    private Boolean free_shipping;

    private Shipping(Parcel in) {
        byte tmpFree_shipping = in.readByte();
        free_shipping = tmpFree_shipping == 0 ? null : tmpFree_shipping == 1;
    }

    public String getShippingFormatter() {
        return free_shipping ? "Envio Gratis" : null;
    }

    public static final Creator<Shipping> CREATOR = new Creator<Shipping>() {
        @Override
        public Shipping createFromParcel(Parcel in) {
            return new Shipping(in);
        }

        @Override
        public Shipping[] newArray(int size) {
            return new Shipping[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (free_shipping == null ? 0 : free_shipping ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
