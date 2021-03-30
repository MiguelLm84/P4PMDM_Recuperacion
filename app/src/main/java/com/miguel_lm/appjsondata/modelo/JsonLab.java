package com.miguel_lm.appjsondata.modelo;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class JsonLab {

    @SuppressLint("StaticFieldLeak")
    private static JsonLab jsonLab;

    private JsonDAO jsonDao;

    private JsonLab(Context context) {
        Context appContext = context.getApplicationContext();
        DBManagerRoom.AppDatabase database = Room.databaseBuilder(appContext, DBManagerRoom.AppDatabase.class, "JsonData").allowMainThreadQueries().build();
        jsonDao = database.getJsonDao();
    }

    public static JsonLab get(Context context) {
        if (jsonLab == null) {
            jsonLab = new JsonLab(context);
        }
        return jsonLab;
    }

    //POSTS

    public List<Post> getPosts() {
        return jsonDao.getPosts();
    }

    public void insertPosts(Post posts) {
        jsonDao.insert(posts);
    }

    public void updatePosts(Post posts) {
        jsonDao.update(posts);
    }

    public void deletePosts(Post posts) {
        jsonDao.delete(posts);
    }

    public List<Post> searchPosts(String query) {
        return jsonDao.searchPosts(query);
    }

    public void deleteAllPosts() { jsonDao.deleteAllPosts();    }


    // USUARIOS

    public List<User> getUsers() {
        return jsonDao.getUsers();
    }

    public void insertUsers(User user) {
        jsonDao.insert(user);
    }

    public void updateUsers(User user) {
        jsonDao.update(user);
    }

    public void deleteUsers(User user) {
        jsonDao.delete(user);
    }

    public User searchUserById(int id) {
        return jsonDao.searchUserById(id);
    }

    public void deleteAllUsers() { jsonDao.deleteAllUsers();   }

}
