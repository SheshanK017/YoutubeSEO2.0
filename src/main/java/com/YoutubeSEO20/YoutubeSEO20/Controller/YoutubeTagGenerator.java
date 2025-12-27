package com.YoutubeSEO20.YoutubeSEO20.Controller;


import com.YoutubeSEO20.YoutubeSEO20.Model.SearchVideos;
import com.YoutubeSEO20.YoutubeSEO20.Service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class YoutubeTagGenerator {

    @Autowired
    private YoutubeService youtubeService;


    @Value("${youtube.api.key}")//this will add value to apiKey
    private String apiKey;



    private boolean checkApiKey(){
        if(apiKey==null || apiKey.isEmpty())return false;
        return true;
    }

    public String videoTags(@RequestParam ("videoTitle") String videoTitle, Model model){


        if(!checkApiKey()){
            model.addAttribute("error", "Api key is not correctly configured");
            return "home";
        }


        if(videoTitle ==null || videoTitle.isEmpty()){
            model.addAttribute("error","please add input for video");
            return "home";
        }

        try{
            SearchVideos result=YoutubeService.searchVideos(videoTitle);;

            model.addAttribute("primaryVideos",result.getPrimaryVideo());
            model.addAttribute("relatedVideos",result.getRelatedVideo());
            return "home";

        } catch (Exception e) {
           model.addAttribute("error",e.getMessage());
           return "home";
        }

        return null;
    }

}
