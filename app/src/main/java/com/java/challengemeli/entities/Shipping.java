package com.java.challengemeli.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Shipping implements Parcelable {
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
    private Boolean free_shipping;

    public Boolean getFree_shipping() {
        return free_shipping;
    }

    public void setFree_shipping(Boolean free_shipping) {
        this.free_shipping = free_shipping;
    }

    protected Shipping(Parcel in) {
        byte tmpFree_shipping = in.readByte();
        free_shipping = tmpFree_shipping == 0 ? null : tmpFree_shipping == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (free_shipping == null ? 0 : free_shipping ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getShippingFormatter(){
        return free_shipping ? "Envio Gratis" : null;
    }
}
