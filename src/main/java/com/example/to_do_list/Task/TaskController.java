package com.example.to_do_list.Task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/task"))
public class TaskController {

    @GetMapping
    public String hello(){
        return "hello";
    }
}

