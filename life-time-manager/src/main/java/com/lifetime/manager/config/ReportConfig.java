package com.lifetime.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ReportConfig
 * @Author Liwx
 * @Description
 * @Date 2024/4/30$ 11:25
 */
@Configuration
@ConfigurationProperties("report-bi")
@Data
public class ReportConfig {
    private String biPublicKey;
    private String reportPublicKey;
    private String biUser;
    private String reportUser;
}
