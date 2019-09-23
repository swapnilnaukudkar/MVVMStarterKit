package com.swapnil.mvvmstarterkit.pojo;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageTableDao {
    @Insert
    Long insertSingleValue(ImageTableEntity imageTableEntity);

    @Query("DELETE FROM imageTable where id =:id")
    void deleteImagePath(int id);

    @Query("SELECT * FROM imageTable where `from` = :from")
    List<ImageTableEntity> getAllUnsyncedImagesOfType(String from);
}
