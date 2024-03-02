package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 5,
        @Query("type") type: String = "video",
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): SearchResponse
}