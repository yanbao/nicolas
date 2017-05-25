package com.nicolas.rest;

import com.mongodb.BasicDBObject;
import com.nicolas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import com.nicolas.util.MongoUtil;
/**
 * Created by 大泥藕 on 2017/5/17.
 */

@RestController
public class UserInfos {
    @Autowired
    private User user;
    @Autowired
    private MongoUtil mongoUtil;
    @GetMapping(value = "/getUserInfos")
    public List getUserInfos(@RequestParam(value = "name",required = true,defaultValue = "0") String name){
        String str =  user.getInfos();
        List fl = new ArrayList<>();
        fl.add(str);
        fl.add(name);

        //----------------------------------------------------------
        List nl = new ArrayList<>();
        nl.add(fl);
        BasicDBObject fQueryObject = new BasicDBObject("account","haoxing");
        BasicDBObject sQueryObject = new BasicDBObject();
        List<BasicDBObject> fObj = mongoUtil.find("reef", sQueryObject);
        if(!MongoUtil.isEmpty(fObj)){


            List sl = new ArrayList();
            for(BasicDBObject obj:fObj){
                Map smap = new HashMap();
                String pwd = obj.getString("password");
                smap.put("pwd", pwd);
                sl.add(smap);
            }

            nl.add(sl);
        }
        //----------------------------------------------------------
       return nl;

    }


}
