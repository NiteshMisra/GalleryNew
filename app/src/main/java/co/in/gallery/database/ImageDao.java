package co.in.gallery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import co.in.gallery.model.ImageModel;

@Dao
public interface ImageDao {

    @Insert
    void insert(List<ImageModel> imageModel);

    @Query("Delete from ImageModel")
    void deleteAll();

    @Query("Select * from ImageModel")
    List<ImageModel> getImages();

    @Query("Select count(*) from ImageModel")
    int isDataPresent();

}
