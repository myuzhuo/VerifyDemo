package com.example.record.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by myuzh on 2019/2/22.
 */
public class UserInfo implements Parcelable {

    public UserInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    protected UserInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
