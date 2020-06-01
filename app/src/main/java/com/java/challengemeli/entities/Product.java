package com.java.challengemeli.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.java.challengemeli.core.Utils;

import java.text.NumberFormat;

public class Product implements Parcelable {
    private String id;
    private String title;
    private Double price;
    private Integer available_quantity;
    private Integer sold_quantity;
    private String thumbnail;
    private Boolean accepts_mercadopago;
    private Shipping shipping;
    private Address address;
    private String condition;
    private String permalink;

    public Product(String id, String title, Double price, Integer sold_quantity, String thumbnail, Boolean accepts_mercadopago, String condition, String permalink) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.sold_quantity = sold_quantity;
        this.thumbnail = thumbnail;
        this.accepts_mercadopago = accepts_mercadopago;
        this.condition = condition;
        this.permalink = permalink;
    }

    public String getPermalink() {
        return permalink;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public Address getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getSold_quantity() {
        return sold_quantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String formatterPrice() {
        return Utils.getNumber(NumberFormat.getCurrencyInstance().format(price));
    }

    public String formatterDecimal() {
        return Utils.getDecimal(NumberFormat.getCurrencyInstance().format(price));
    }

    public String formatterCondition() {
        if (condition.equals("new")) {
            return "Nuevo";
        } else {
            return "Usado";
        }
    }

    protected Product(Parcel in) {
        id = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            available_quantity = null;
        } else {
            available_quantity = in.readInt();
        }
        if (in.readByte() == 0) {
            sold_quantity = null;
        } else {
            sold_quantity = in.readInt();
        }
        thumbnail = in.readString();
        byte tmpAccepts_mercadopago = in.readByte();
        accepts_mercadopago = tmpAccepts_mercadopago == 0 ? null : tmpAccepts_mercadopago == 1;
        shipping = in.readParcelable(Shipping.class.getClassLoader());
        address = in.readParcelable(Address.class.getClassLoader());
        condition = in.readString();
        permalink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (available_quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(available_quantity);
        }
        if (sold_quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sold_quantity);
        }
        dest.writeString(thumbnail);
        dest.writeByte((byte) (accepts_mercadopago == null ? 0 : accepts_mercadopago ? 1 : 2));
        dest.writeParcelable(shipping, flags);
        dest.writeParcelable(address, flags);
        dest.writeString(condition);
        dest.writeString(permalink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
