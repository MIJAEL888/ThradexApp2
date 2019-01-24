package org.thradex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thradex.rrhh.service.ShiftCronServiceImpl;

@Configuration
@EnableScheduling
public class ScheduleConfig {

	@Bean
    public ShiftCronServiceImpl bean() {
        return new ShiftCronServiceImpl();
    }
}
