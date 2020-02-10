package co.in.gallery.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity
public class ImageModel {

    @NonNull
    @PrimaryKey()
    @SerializedName("id")
    private String id;

    @SerializedName("owner")
    private String owner;

    @SerializedName("secret")
    private String secret;

    @SerializedName("server")
    private String server;

    @SerializedName("farm")
    private Integer farm;

    @SerializedName("title")
    private String title;

    @SerializedName("ispublic")
    private Integer isPublic;

    @SerializedName("isfriend")
    private Integer isFriend;

    @SerializedName("isfamily")
    private Integer isFamily;

    @SerializedName("url_s")
    private String url;

    @SerializedName("height_s")
    private Integer height;

    @SerializedName("width_s")
    private Integer width;

    public ImageModel(@NonNull String id, String owner, String secret, String server, Integer farm, String title, Integer isPublic, Integer isFriend, Integer isFamily, String url, Integer height, Integer width) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.isPublic = isPublic;
        this.isFriend = isFriend;
        this.isFamily = isFamily;
        this.url = url;
        this.height = height;
        this.width = width;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public Integer getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public Integer getIsFriend() {
        return isFriend;
    }

    public Integer getIsFamily() {
        return isFamily;
    }

    public String getUrl() {
        return url;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

}
