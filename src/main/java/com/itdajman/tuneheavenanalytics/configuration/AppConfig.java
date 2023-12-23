package com.itdajman.tuneheavenanalytics.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public Clock customClock() {
        return Clock.systemUTC();
    }
}
