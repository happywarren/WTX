package com.lt.adapter;

import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cnmcs on 2017/9/6.
 */
public class Main {

    public static void main(String [] args){
        String imageUrl = "{imageFile2=http://cos.speedycloud.org/cainiu-lt/20170912104346.png,imageFile1=http://cos.speedycloud.org/cainiu-lt/20170912104346.png,imageFile4=http://cos.speedycloud.org/cainiu-lt/20170912104347.jpg,imageFile3=http://cos.speedycloud.org/cainiu-lt/20170912104347.png}";
        StringBuilder stringBuilder = new StringBuilder();
        if (StringTools.isNotEmpty(imageUrl)) {
            imageUrl = imageUrl.replace("{","").replace("}","");
            String[] imageArray = imageUrl.split(",");
            for (String url : imageArray) {
                String[] urlArray = url.split("=");
                if (urlArray.length > 1) {
                    stringBuilder.append(",").append(urlArray[1]);
                }
            }
            System.out.println(stringBuilder.substring(1));
        }
    }

}
