package com.nicolas.initialization;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 大泥藕 on 2017/5/17.
 */

@RestController
public class HelloWorld {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){

       return "Hello SpingBoot~";

    }


}
