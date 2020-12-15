package com.gksoftwaresolutions.comicviewer.data.remote

import com.gksoftwaresolutions.comicviewer.model.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MarvelClient {

    @GET("/v1/public/comics")
    fun comics(@QueryMap params: Map<String, String>): Observable<ResponseGenericData<List<Comic>>>

    @GET("/v1/public/comics/{comicId}")
    fun comic(
        @Path("comicId") comicId: Int,
        @QueryMap params: Map<String, String>
    ): Single<ResponseGenericData<List<Comic>>>

    @GET("/v1/public/comics/{comicId}/characters")
    fun comicWithCharacters(
        @Path("comicId") comicId: Int,
        @QueryMap params: Map<String, String>
    ): Flowable<ResponseGenericData<List<Character>>>

    @GET("/v1/public/characters")
    fun characters(@QueryMap params: Map<String, String>): Observable<ResponseGenericData<List<Character>>>

    @GET("/v1/public/characters/{characterId}")
    fun character(
        @Path("characterId") characterId: Int,
        @QueryMap params: Map<String, String>
    ): Single<ResponseGenericData<List<Character>>>

    @GET("/v1/public/characters/{characterId}/comics")
    fun characterWithComics(
        @Path("characterId") characterId: Int,
        @QueryMap params: Map<String, String>
    ): Flowable<ResponseGenericData<List<Comic>>>

    @GET("/v1/public/creators")
    fun creators(@QueryMap params: Map<String, String>): Observable<ResponseGenericData<List<Creator>>>

    @GET("/v1/public/creators/{creatorId}")
    fun creator(
        @Path("creatorId") creatorId: Int,
        @QueryMap params: Map<String, String>
    ): Single<ResponseGenericData<List<Creator>>>

    @GET("/v1/public/creators/{creatorId}/comics")
    fun creatorWithComics(
        @Path("creatorId") creatorId: Int,
        @QueryMap params: Map<String, String>
    ): Flowable<ResponseGenericData<List<Comic>>>

    @GET("/v1/public/events")
    fun events(@QueryMap params: Map<String, String>): Observable<ResponseGenericData<List<Event>>>

    @GET("/v1/public/events/{eventId}")
    fun event(
        @Path("eventId") eventId: Int,
        @QueryMap params: Map<String, String>
    ): Single<ResponseGenericData<List<Event>>>

    @GET("/v1/public/events/{eventId}/comics")
    fun eventWithComics(
        @Path("eventId") eventId: Int,
        @QueryMap params: Map<String, String>
    ): Flowable<ResponseGenericData<List<Comic>>>

    @GET("/v1/public/series")
    fun series(@QueryMap params: Map<String, String>): Observable<ResponseGenericData<List<Serie>>>

    @GET("/v1/public/series/{serieId}")
    fun serie(
        @Path("serieId") serieId: Int,
        @QueryMap params: Map<String, String>
    ): Single<ResponseGenericData<List<Serie>>>

    @GET("/v1/public/series/{serieId}/characters")
    fun serieWithCharacters(
        @Path("serieId") serieId: Int,
        @QueryMap params: Map<String, String>
    ): Flowable<ResponseGenericData<List<Character>>>
}