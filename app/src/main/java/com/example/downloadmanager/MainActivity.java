package com.example.downloadmanager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txtUser;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String URL = "https://my-json-server.typicode.com/DianaAvilesCh/JsonDL/db";
    ArrayList<Ficheros> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        ArrayList<String> permisos = new ArrayList<String>();
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        getPermission(permisos);
        stringRequest();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public void stringRequest() {
        txtUser = (TextView) findViewById(R.id.txtLsUser);
        StringRequest request = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray JSONlista = object.getJSONArray("ficheros");

                            lista = Ficheros.JsonObjectsBuild(JSONlista);

                            Adapter adapter = new Adapter(getApplicationContext(), lista);

                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                    resId);
                            recyclerView.setLayoutAnimation(animation);

                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtUser.setVisibility(View.VISIBLE);
                            txtUser.setKeyListener(null);
                            txtUser.setText("HOLA " + e.toString() + "\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
                    public void onErrorResponse(final VolleyError error) {
                        txtUser.setKeyListener(null);
                        if (error.networkResponse == null
                                && error instanceof NoConnectionError
                                && error.getMessage().contains("javax.net.ssl.SSLHandshakeException")) {
                            txtUser.setVisibility(View.VISIBLE);
                            txtUser.setText(error.toString());
                            // Se ha producido un error con el certificado SSL y la conexión ha sido
                            // rechazada
                        }
                    }
                }
        );
        requestQueue.add(request);
    }



    public void getPermission(ArrayList<String> permisosSolicitados){

        ArrayList<String> listPermisosNOAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNOAprob.size()>0)
            if (Build.VERSION.SDK_INT >= 23)
                requestPermissions(listPermisosNOAprob.toArray(new String[listPermisosNOAprob.size()]), 1);

    }


    public ArrayList<String> getPermisosNoAprobados(ArrayList<String>  listaPermisos) {
        ArrayList<String> list = new ArrayList<String>();
        for(String permiso: listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                if(checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                    list.add(permiso);

        }
        return list;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s="";
        if(requestCode==1)    {
            for(int i =0; i<permissions.length;i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    s=s + "OK " + permissions[i] + "\n";
                else
                    s=s + "NO  " + permissions[i] + "\n";
            }
            Toast.makeText(this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }


    public void BajarDoc(Context context, String file, String url){
        DownloadManager downloadManager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,file+".pdf");

        downloadManager.enqueue(request);
    }

}
