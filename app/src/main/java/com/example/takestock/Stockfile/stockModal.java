package com.example.takestock.Stockfile;

import android.os.Parcel;
import android.os.Parcelable;

public class stockModal implements Parcelable {
    // creating variables for our different fields.
    private String personsName;
    private String deviceName;
    private String deviceSerial;
    private String date;
    private String stockId;

    // creating an empty constructor.
    public stockModal() {

    }


    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    protected stockModal(Parcel in) {
        personsName = in.readString();
        deviceName = in.readString();
        deviceSerial = in.readString();
        date = in.readString();
        stockId = in.readString();

    }

    public static final Creator<stockModal> CREATOR = new Creator<stockModal>() {
        @Override
        public stockModal createFromParcel(Parcel in) {
            return new stockModal(in);
        }

        @Override
        public stockModal[] newArray(int size) {
            return new stockModal[size];
        }
    };

    // creating getter and setter methods.


    public String getPersonsName() {
        return personsName;
    }

    public void setPersonsName(String personsName) {
        this.personsName = personsName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public stockModal(String stockId, String personsName, String deviceName, String deviceSerial, String date) {
        this.personsName = personsName;
        this.deviceName = deviceName;
        this.deviceSerial = deviceSerial;
        this.date = date;
        this.stockId = stockId;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(personsName);
        dest.writeString(deviceName);
        dest.writeString(deviceSerial);
        dest.writeString(date);
    }
}

