package org.example;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
public class SecondServiceController {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the second service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request")String header){
        System.out.println(header);
        return "Hello world in Second Service";
    }

    @GetMapping("/check")
    public String check(){
        return "Hi there, this is message from Second Service";
    }



}

