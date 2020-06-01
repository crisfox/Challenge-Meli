package com.java.challengemeli.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String state_name;
    private String city_name;

    public String getAddressFormatter() {
        if (!state_name.equals(city_name)) {
            return city_name + ", " + state_name;
        } else {
            return city_name;
        }
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel in) {
        state_name = in.readString();
        city_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state_name);
        dest.writeString(city_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
