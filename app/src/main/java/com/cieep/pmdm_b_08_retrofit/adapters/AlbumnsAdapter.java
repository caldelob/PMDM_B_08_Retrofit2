package com.cieep.pmdm_b_08_retrofit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cieep.pmdm_b_08_retrofit.PhotosActivity;
import com.cieep.pmdm_b_08_retrofit.R;
import com.cieep.pmdm_b_08_retrofit.conexiones.ApiConexiones;
import com.cieep.pmdm_b_08_retrofit.conexiones.RetrofitObject;
import com.cieep.pmdm_b_08_retrofit.modelos.Album;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlbumnsAdapter extends RecyclerView.Adapter<AlbumnsAdapter.AlbumVH> {


    private List<Album> objects;
    private Context context;
    private int resource;

    public AlbumnsAdapter(List<Album> objects, Context context, int resource) {
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public AlbumVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View albumView = LayoutInflater.from(context).inflate(resource, null);
        albumView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new AlbumVH(albumView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumVH holder, int position) {
        Album a = objects.get(position);
        holder.lblTitulo.setText(a.getTitulo());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID_ALBUM", String.valueOf(a.getId()));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminaAlbum(a, holder.getAdapterPosition());
            }
        });


    }

    private void eliminaAlbum(Album a, int adapterPosition) {
        Retrofit retrofit = RetrofitObject.getConnection();
        ApiConexiones api = retrofit.create(ApiConexiones.class);
        Call<Album> doDeleteAlbum = api.deleteAlbum(String.valueOf(a.getId()));

        doDeleteAlbum.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.code()== HttpsURLConnection.HTTP_OK){
                    objects.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }else{
                    Toast.makeText(context, "ERROR AL ELIMINAR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Toast.makeText(context, "ERROR AL CONECTAR", Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class AlbumVH extends RecyclerView.ViewHolder{


        TextView lblTitulo;
        ImageButton btnEliminar;

        public AlbumVH(@NonNull View itemView){
            super(itemView);
            lblTitulo= itemView.findViewById(R.id.lblTituloAlbumVH);
            btnEliminar = itemView.findViewById(R.id.btnEliminarAlbumCard);

        }
    }
}
