package com.YoutubeSEO20.YoutubeSEO20.Controller;
//to navigate from one page to another page

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/","home"})
    public String home(){
        return "home";
    }


    @GetMapping("/video-details")
    public String videoDetails(){
        return "video-details";
    }
}

