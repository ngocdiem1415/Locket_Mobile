package vn.edu.hcumuaf.locket.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import java.util.Map;

@Configuration
//@PropertySource("classpath:cloudinary.properties")
public class CloudinaryConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);

//    @Value("${cloudinary.cloud_name}")
//    private String cloudName;
//
//    @Value("${cloudinary.api_key}")
//    private String apiKey;
//
//    @Value("${cloudinary.api_secret}")
//    private String apiSecret;

    @Value("${CLOUDINARY_CLOUD_NAME:#{null}}")
    private String cloudName;

    @Value("${CLOUDINARY_API_KEY:#{null}}")
    private String apiKey;

    @Value("${CLOUDINARY_API_SECRET:#{null}}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        if (!StringUtils.hasText(cloudName) || !StringUtils.hasText(apiKey) || !StringUtils.hasText(apiSecret)) {
            logger.error("Cloudinary config is invalid. Please check environment variables or properties.");
            throw new IllegalArgumentException("Missing Cloudinary configuration");
        }

        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );

        logger.info("Initialized Cloudinary with cloud_name: {}", cloudName);
        return new Cloudinary(config);
    }
}