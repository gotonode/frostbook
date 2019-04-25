package io.github.gotonode.frostbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
