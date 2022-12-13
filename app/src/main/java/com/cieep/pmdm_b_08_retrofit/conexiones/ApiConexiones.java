package com.cieep.pmdm_b_08_retrofit.conexiones;

import com.cieep.pmdm_b_08_retrofit.modelos.Album;
import com.cieep.pmdm_b_08_retrofit.modelos.Photo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {

    // Todos los endpoint y retornos de la API

    @GET("/albums")
    Call<ArrayList<Album>> getAlbums();

    @GET("/albums/{idAlbum}")
    Call<Album> getAlbum(@Path("idAlbum") String idAlbum);


    // /photos?albumId=2

    @GET("/photos?")

    Call<ArrayList<Photo>> getPhotosAlbum(@Query("albumId") String idAlbum);

    @GET("/albums/{albumId}/photos")
    Call<ArrayList<Photo>> getPostAlbumPath(@Path("albumId") String idAlbum);


    @POST("/albums")
    Call<Album> postAlbum(@Body Album album);


    @PUT("/albums")
    Call<Album> putAlbum(@Body Album album);

    @DELETE("/albums/{idAlbum}")
    Call<Album> deleteAlbum(@Path("idAlbum")String id);

}
