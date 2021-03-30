package com.miguel_lm.appjsondata.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.miguel_lm.appjsondata.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Post")
public class Post implements Serializable {

    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE)
    private int userId;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name="titulo")
    private String titulo;

    @ColumnInfo(name="cuerpo")
    private String cuerpo;

    public Post(int userId, int id, String titulo, String cuerpo){
        this.userId = userId;
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    @Ignore
    public Post(int userId,  String titulo, String cuerpo){
        this.userId = userId;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int  getId() {
        return id;
    }

    public void setId(int  id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public void modificar(int userId,  String titulo, String cuerpo){
        this.userId = userId;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public JSONObject generarJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", String.valueOf(userId));
            json.put("title", String.valueOf(titulo));
            json.put("body", String.valueOf(cuerpo));
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static Post parsearPost(JSONObject response, boolean mantenerIdDescargado) {

        try {
            int userId = response.getInt("userId");
            int id = 0;
            if (mantenerIdDescargado)
                id = response.getInt("id");
            String titulo = response.getString("title");
            String cuerpo = response.getString("body");

            if (mantenerIdDescargado)
                return new Post(userId, id, titulo, cuerpo);
            else
                return new Post(userId, titulo, cuerpo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
