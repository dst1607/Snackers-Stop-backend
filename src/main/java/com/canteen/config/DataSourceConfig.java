package com.canteen.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        String explicitUrl = System.getenv("SPRING_DATASOURCE_URL");

        // Prefer explicitUrl if provided, else dbUrl
        String urlToUse = (explicitUrl != null && !explicitUrl.trim().isEmpty()) ? explicitUrl : dbUrl;

        // If the URL is provided in the postgres(ql):// format (Render default or pasted incorrectly)
        if (urlToUse != null && (urlToUse.startsWith("postgres://") || urlToUse.startsWith("postgresql://"))) {
            try {
                // Determine prefix length
                int prefixLen = urlToUse.startsWith("postgresql://") ? 13 : 11;
                String cleanUrl = urlToUse.substring(prefixLen); 
                
                String[] userInfoAndHost = cleanUrl.split("@");
                if (userInfoAndHost.length == 2) {
                    // We have credentials in the URL (user:pass@host/db)
                    String[] credentials = userInfoAndHost[0].split(":");
                    String username = credentials[0];
                    String password = credentials.length > 1 ? credentials[1] : "";
                    String hostAndDb = userInfoAndHost[1];
                    
                    String jdbcUrl = "jdbc:postgresql://" + hostAndDb;
                    
                    return DataSourceBuilder.create()
                            .driverClassName("org.postgresql.Driver")
                            .url(jdbcUrl)
                            .username(username)
                            .password(password)
                            .build();
                } else {
                    // No credentials in URL, just host
                     String jdbcUrl = "jdbc:postgresql://" + cleanUrl;
                     return buildFallback(jdbcUrl);
                }
            } catch (Exception e) {
                System.err.println("Failed to parse URL: " + urlToUse);
            }
        }
        
        // If it starts with jdbc: do normal fallback
        if (urlToUse != null && urlToUse.startsWith("jdbc:")) {
             return buildFallback(urlToUse);
        }

        // Complete fallback for localhost local dev
        return buildFallback("jdbc:postgresql://localhost:5432/canteen_backend");
    }

    private DataSource buildFallback(String jdbcUrl) {
        String explicitUser = System.getenv("SPRING_DATASOURCE_USERNAME");
        if (explicitUser == null || explicitUser.isEmpty()) {
            explicitUser = "postgres";  // local fallback
        }
        
        String explicitPass = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if (explicitPass == null) {
            explicitPass = "postgres";  // local fallback
        }
        
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(jdbcUrl)
                .username(explicitUser)
                .password(explicitPass)
                .build();
    }
}
