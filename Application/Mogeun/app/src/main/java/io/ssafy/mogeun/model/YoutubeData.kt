package io.ssafy.mogeun.model

data class VideoItem(val id: VideoId, val snippet: Snippet)
data class VideoId(val videoId: String)
data class Snippet(val title: String, val thumbnails: Thumbnails)
data class Thumbnails(val medium: ThumbnailInfo)
data class ThumbnailInfo(val url: String)
data class SearchResponse(val items: List<VideoItem>)