package com.example.todolist10;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String name;
    private String description;
    private float type;
    private String imgUri;

    public Task(String name, String description, float type, String imgUri) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.imgUri = imgUri;
    }

    protected Task(Parcel in) {
        name = in.readString();
        description = in.readString();
        type = in.readFloat();
        imgUri = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(type);
        dest.writeString(imgUri);
    }
}
