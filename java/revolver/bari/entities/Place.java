package revolver.bari.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;


import revolver.bari.constants.Places;
import revolver.bari.constants.Type;

public class Place implements Parcelable {

    private String name;
    private String title;
    private String contentText;
    private LatLng position;
    private Type type;
    private String[] imageFiles;

    public Place(String name,
                 Type type,
                 LatLng position,
                 String[] imageFiles) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.imageFiles = imageFiles;
    }

    private Place(Parcel src) {
        this.name = src.readString();
        this.title = src.readString();
        this.contentText = src.readString();
        this.position = (LatLng) src.readValue(LatLng.class.getClassLoader());
        this.type = (Type) src.readValue(Type.class.getClassLoader());

        int length = src.readInt();
        this.imageFiles = new String[length];
        src.readStringArray(this.imageFiles);
    }

    public boolean hasPosition() {
        return position != null;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        String title = Places.getTitle(name);
        return title != null ? title : this.name;
    }

    public int getIcon() {
        return Places.getIcon(this);
    }

    public String getContentText() {
        return Places.getText(name);
    }

    public LatLng getPosition() {
        return position;
    }

    public String[] getImageFiles() {
        return imageFiles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(contentText);
        dest.writeValue(position);
        dest.writeValue(type);
        dest.writeInt(imageFiles.length);
        dest.writeStringArray(imageFiles);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
