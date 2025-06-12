package vn.edu.hcumuaf.locket.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary configKey(){
        Map<String,String> config = new HashMap();
        config.put("cloud_name", "dwjztnzgv");
        config.put("api_key", "191659792121745");
        config.put("api_secret", "APplbFE3u0xbYmIegZu0SC7PIoY");
        return new Cloudinary(config);
    }
}
