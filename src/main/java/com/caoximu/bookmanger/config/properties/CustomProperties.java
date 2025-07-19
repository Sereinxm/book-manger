package com.caoximu.bookmanger.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cao32
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class CustomProperties {

    private RasKey signatureKey;
    @Setter
    @Getter
    public static class RasKey {
        /**
         * privateKey
         */
        private String privateKey;

        /**
         * publicKey
         */
        private String publicKey;
    }

}