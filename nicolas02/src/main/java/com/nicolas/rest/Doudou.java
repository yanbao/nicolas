package com.nicolas.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 大泥藕 on 2017/5/17.
 */


@RestController
public class Doudou {

    @Value("${name}")
    private String name;
    @Value("${age}")
    private Integer age;
    @Value("${infos}")
    private String infos;
    @RequestMapping(value = "/model",method = RequestMethod.GET)
    public String say(){

       return infos;

    }


}
