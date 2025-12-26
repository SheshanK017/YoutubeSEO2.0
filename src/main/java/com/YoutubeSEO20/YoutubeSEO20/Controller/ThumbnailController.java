package com.YoutubeSEO20.YoutubeSEO20.Controller;

import com.YoutubeSEO20.YoutubeSEO20.Service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ThumbnailController {

    @Autowired
    ThumbnailService service;


    @GetMapping("/thumbnail")
    public String getThumbnail(){
        return "thumbnails";
    }

    @PostMapping("/get-thumbnail")
    public String showThumbnail(@RequestParam ("videoUrlOrId") String videoUrlOrID, Model model){
        String videoId=service.ectractVideoID(videoUrlOrID);
        if(videoId==null){
            model.addAttribute("error","Invalid Youtube Url");
            return "thumbnails";
        }

        String thumbnailUrl="http://img.youtube.com/vi/"+videoId+"/maxresdefault.jpg";
        model.addAttribute("thumbnailUrl",thumbnailUrl);
        return "thumbnails";
    }
}
