package com.alquimia.infrastructure.config;

import com.alquimia.application.port.out.DeviceRepositoryPort;
import com.alquimia.application.usecase.AlquimiaService;
import com.alquimia.infrastructure.adapter.InMemoryDeviceRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Único lugar donde se conectan puertos con adaptadores; dominio y aplicación quedan libres de Spring. */
@Configuration
public class BeanConfig {

    @Bean
    DeviceRepositoryPort deviceRepository() { return new InMemoryDeviceRepositoryAdapter(); }

    @Bean
    AlquimiaService alquimiaService(DeviceRepositoryPort repository) { return new AlquimiaService(repository); }
}
