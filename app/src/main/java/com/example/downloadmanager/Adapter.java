package com.example.downloadmanager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolderDatos> {

    List<Ficheros> lista;
    Context Ctx;
    MainActivity mainActivity = new MainActivity();

    public Adapter(Context mCtx, List<Ficheros> lista) {
        this.lista = lista;
        Ctx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        Ficheros usuario = lista.get(position);

        holder.tvId.setText(usuario.getId());
        holder.tvNombre.setText(usuario.getDescripcion());
        holder.tvFecha.setText(usuario.getFecha());
        holder.btndes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.BajarDoc(holder.tvNombre.getContext(),usuario.getDescripcion(),usuario.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView tvNombre, tvFecha, tvId;
        Button btndes;

        public ViewHolderDatos(View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.txtNombre);
            tvId = itemView.findViewById(R.id.txtId);
            tvFecha = itemView.findViewById(R.id.txtFecha);
            btndes = itemView.findViewById(R.id.btnDes);
        }

    }

//    public void downloadPdf ( Context context, String file, String fileExten, String destinationDirecto,String url)
//    {
//        DownloadManager downloadManager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context,destinationDirecto,file+fileExten);
//
//        downloadManager.enqueue(request);
//    }
}
