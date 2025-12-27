package com.YoutubeSEO20.YoutubeSEO20.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private String id;
    private String channelTitle;
    private String title;
    private List<String> tags;
}