package com.cieep.pmdm_b_08_retrofit;

import android.content.DialogInterface;
import android.os.Bundle;

import com.cieep.pmdm_b_08_retrofit.adapters.AlbumnsAdapter;
import com.cieep.pmdm_b_08_retrofit.conexiones.ApiConexiones;
import com.cieep.pmdm_b_08_retrofit.conexiones.RetrofitObject;
import com.cieep.pmdm_b_08_retrofit.modelos.Album;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;


import com.cieep.pmdm_b_08_retrofit.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<Album> albums;
    private AlbumnsAdapter adapter;
    private RecyclerView.LayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        albums = new ArrayList<>();
        adapter= new AlbumnsAdapter(albums, this, R.layout.album_view_holder);
        lm = new LinearLayoutManager(this);
        binding.contentMain.contenedorAlbumns.setAdapter(adapter);
        binding.contentMain.contenedorAlbumns.setLayoutManager(lm);

        //RETROFIT:
        doGetAlbums();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlbum().show();
            }
        });
    }

    private AlertDialog createAlbum() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Create Album");
        EditText txtTitulo = new EditText(this);
        builder.setView(txtTitulo);
        builder.setCancelable(false);
        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("INSERTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             if(!txtTitulo.getText().toString().isEmpty())   {
                 Album album = new Album();
                 album.setUserId(1);
                 album.setTitulo(txtTitulo.getText().toString());

                 postAlbum(album);
             }
            }
        });
        return builder.create();
    }

    private void postAlbum(Album album) {
        Retrofit retrofit = RetrofitObject.getConnection();
        ApiConexiones api = retrofit.create(ApiConexiones.class);
        Call<Album> doPostAlbum = api.postAlbum(album);

        doPostAlbum.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    albums.add(0, response.body());
                    adapter.notifyItemInserted(0);

                }else{
                    Toast.makeText(MainActivity.this, "ERROR AL CREAR EL ALBUM", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doGetAlbums() {

        Retrofit retrofit = RetrofitObject.getConnection();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        Call<ArrayList<Album>> getAlbums = api.getAlbums();
        getAlbums.enqueue(new Callback<ArrayList<Album>>() {
            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {

                if(response.code()== HttpURLConnection.HTTP_OK){
                    ArrayList<Album> temp = response.body(); //aqui esta el arrayList de objetos de tipo Album
                    albums.addAll(temp);
                    adapter.notifyItemRangeInserted(0, temp.size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR DE CONECCIÓN", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.getLocalizedMessage());
            }
        });
        }
    }
