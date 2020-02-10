package co.in.gallery.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("stat")
    private String stat;

    @SerializedName("photos")
    private ImageResponse photos;

    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    public ApiResponse(String stat, ImageResponse photos, Integer code, String message) {
        this.stat = stat;
        this.photos = photos;
        this.code = code;
        this.message = message;
    }

    public String getStat() {
        return stat;
    }

    public ImageResponse getPhotos() {
        return photos;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
