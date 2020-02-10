package co.in.gallery.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import co.in.gallery.model.ImageModel;

@Database(entities = {ImageModel.class}, version = 1)
public abstract class ImageDatabase extends RoomDatabase {

    private static ImageDatabase instance;

    public abstract ImageDao imageDao();

    public static synchronized ImageDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),ImageDatabase.class,"Image Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
