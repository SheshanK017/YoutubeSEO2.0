package com.YoutubeSEO20.YoutubeSEO20.Service;

import com.YoutubeSEO20.YoutubeSEO20.Model.SearchVideos;
import com.YoutubeSEO20.YoutubeSEO20.Model.Video;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeService {

  private final WebClient.Builder webClientBuilder;

  @Value("${youtube.api.key}")
  private String apiKey;

  @Value("${youtube.api.base.url}")
  private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
  private int maRelatedVideos;

    public SearchVideos searchVideos(String videoTitle){
        List<String> videoIds=searchForVideosId(videoTitle);

        if(videoIds.isEmpty()){
            return SearchVideos.builder().primaryVideo(null).relatedVideo(Collections.emptyList()).build();
        }

        String primaryVideoId=videoIds.get(0);
        List<String> relatedVideoIds=videoIds.subList(1,Math.min(videoIds.size(),maRelatedVideos));


        //video ko laane mei from the video clas model
        Video primaryVideo=getVideoById(primaryVideoId);

        List<Video> relatedVideos=new ArrayList<>();
        for(String id : relatedVideoIds){
            Video video=getVideoById(id);

            if(video!=null)relatedVideos.add(video);
        }

        return SearchVideos.builder().primaryVideo(primaryVideo).relatedVideo(relatedVideos).build();


    }

    private List<String> searchForVideosId(String videoTitle){

        //api calling and adding path to the link then retrieving it in the form of dto object
       SearchApiResponse response=webClientBuilder.baseUrl(baseUrl).build()
               .get()
               .uri(uriBuilder -> uriBuilder
                       .path("/search")
                               .queryParam("part","snippet")
                               .queryParam("q",videoTitle)
                               .queryParam("type","video")
                               .queryParam("maxResult",maRelatedVideos)
                               .queryParam("key",apiKey)
                               .build())
               .retrieve()
               .bodyToMono(SearchApiResponse.class)
               .block();

       if(response==null || response.items==null){
           return Collections.emptyList();
       }

       List<String> videoIds=new ArrayList<>();
       for(SearchItem item: response.items){
           videoIds.add(item.id.videoId);
       }
       return videoIds;

    }

    private Video getVideoById(String videoId){
        VideoApiResponse response=webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key",apiKey).build()
                )
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if(response==null || response.items==null){
            return null;
        }

        Snippet snippet=response.items.get(0).snippet;

        return  Video.builder()
                .id(videoId)
                .channelTitle(snippet.ChannelTitle)
                .title(snippet.title)
                .tags(snippet.tags==null ? Collections.emptyList() : snippet.tags)
                .build();
    }


    //Dto

    @Data
    static class SearchApiResponse{
        List<SearchItem> items;
    }

    @Data
    static class SearchItem{
       Id id;
    }

    @Data
    static class Id{
       String videoId;
    }

    @Data
    static class VideoApiResponse{
        List<VideoItem> items;
    }

    @Data
    static class VideoItem{
       Snippet snippet;
    }

    @Data
    static class Snippet{
        String title;
        String description;
        String ChannelTitle;
        String PublishedAt;
        List<String> tags;
        Thumbnails thumbnails;
    }

    @Data
    static class Thumbnails{
       Thumbnail maxres;
        Thumbnail high;
        Thumbnail medium;
        Thumbnail _default;

        String getBestThumbnailUrl(){
            if(maxres!=null)return maxres.url;
            if(high!=null) return high.url;
            if(medium!=null)return medium.url;
            return _default!=null ? _default.url:"";
        }
    }


    @Data
    static class Thumbnail{
        String url;
    }



}
