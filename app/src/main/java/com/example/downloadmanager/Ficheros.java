package com.example.downloadmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ficheros {
    private String id;
    private String descripcion;
    private String fecha;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Ficheros(String id, String descripcion, String fecha, String url) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.url = url;
    }

    public static ArrayList<Ficheros> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<Ficheros> ficheros = new ArrayList<>();
        ArrayList <String> lis = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            JSONObject user=  datos.getJSONObject(i);
            //Log.d("DATOS", user.toString());
            ficheros.add(new Ficheros(user.getString("id"),
                    user.getString("descripcion"),
                    user.getString("fecha"),
                    user.getString("url")));
        }
        return ficheros;
    }
}
