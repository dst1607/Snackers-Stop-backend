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
        
        // Render typically provides the URL in this format: postgres://user:password@host/dbname
        if (dbUrl != null && dbUrl.startsWith("postgres://")) {
            try {
                String cleanUrl = dbUrl.substring(11); // remove "postgres://"
                String[] userInfoAndHost = cleanUrl.split("@");
                String[] credentials = userInfoAndHost[0].split(":");
                String username = credentials[0];
                String password = credentials.length > 1 ? credentials[1] : "";
                String hostAndDb = userInfoAndHost[1];
                
                // Add query params to ensure SSL mode if required by Render instances (sometimes causes issues without)
                String jdbcUrl = "jdbc:postgresql://" + hostAndDb;
                
                return DataSourceBuilder.create()
                        .driverClassName("org.postgresql.Driver")
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .build();
            } catch (Exception e) {
                System.err.println("Failed to parse DATABASE_URL: " + dbUrl);
            }
        }
        
        // Fallback to explicit variables or localhost
        String explicitUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (explicitUrl == null || explicitUrl.isEmpty()) {
            explicitUrl = "jdbc:postgresql://localhost:5432/canteen_backend";
        }
        
        String explicitUser = System.getenv("SPRING_DATASOURCE_USERNAME");
        if (explicitUser == null || explicitUser.isEmpty()) {
            explicitUser = "postgres";
        }
        
        String explicitPass = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if (explicitPass == null) {
            explicitPass = "postgres";
        }
        
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(explicitUrl)
                .username(explicitUser)
                .password(explicitPass)
                .build();
    }
}
