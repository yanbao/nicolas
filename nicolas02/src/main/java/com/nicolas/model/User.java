package com.nicolas.model;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by 大泥藕 on 2017/5/17.
 */
@Component
@ConfigurationProperties(prefix = "user")
public class User {
    private String name;
    private Integer age;
    private String infos;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getInfos() {
        return infos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }
}
