package com.mytest.controller;

import com.mytest.domian.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author qinxiang
 * @description: TODO
 * @date 2019/12/6 14:34
 */
@RestController
@RequestMapping("/api")
public class ExampleController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${local.url}")
    private String url;

    @GetMapping("/exmaple/{id}")
    public Example getById(@PathVariable("id") Long id){
        Example example = restTemplate.getForObject(url + id, Example.class);
        return example;
    }
}
