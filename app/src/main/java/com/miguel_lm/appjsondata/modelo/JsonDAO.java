package com.miguel_lm.appjsondata.modelo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JsonDAO {


    //POSTS

    @Query("SELECT * FROM Post")
    List<Post> getPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post posts);

    @Delete
    void delete(Post posts);

    @Update
    void update(Post posts);

    @Query("SELECT * FROM Post WHERE titulo LIKE '%' || :query || '%'")
    List<Post> searchPosts(String query);

    @Query("DELETE FROM Post")
    void deleteAllPosts();



    //USUARIOS

    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM User WHERE id == :ide")
    User searchUserById(int ide);

    @Query("DELETE FROM User")
    void deleteAllUsers();
}
