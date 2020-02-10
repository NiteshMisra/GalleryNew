package co.in.gallery.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import co.in.gallery.model.ImageModel;

public class ImageResponse {

    @SerializedName("page")
    private Integer page;

    @SerializedName("pages")
    private Integer pages;

    @SerializedName("perpage")
    private Integer perPages;

    @SerializedName("total")
    private Integer total;

    @SerializedName("photo")
    private List<ImageModel> imageList;

    public ImageResponse(Integer page, Integer pages, Integer perPages, Integer total, List<ImageModel> imageList) {
        this.page = page;
        this.pages = pages;
        this.perPages = perPages;
        this.total = total;
        this.imageList = imageList;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getPerPages() {
        return perPages;
    }

    public Integer getTotal() {
        return total;
    }

    public List<ImageModel> getImageList() {
        return imageList;
    }
}
