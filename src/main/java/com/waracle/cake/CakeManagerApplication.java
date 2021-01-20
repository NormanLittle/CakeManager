package com.waracle.cake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.waracle.cake.data.CakeEntity;
import com.waracle.cake.data.CakeEntityFactory;
import com.waracle.cake.data.CakeEntityRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.waracle.cake.data")
@EntityScan(basePackages = "com.waracle.cake.data")
public class CakeManagerApplication implements ApplicationRunner {

    private final CakeEntityFactory cakeEntityFactory;
    private final CakeEntityRepository cakeEntityRepository;

    public CakeManagerApplication(CakeEntityFactory cakeEntityFactory,
                                  CakeEntityRepository cakeEntityRepository) {
        this.cakeEntityFactory = cakeEntityFactory;
        this.cakeEntityRepository = cakeEntityRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CakeManagerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws JsonProcessingException {
        List<CakeEntity> cakeEntities = cakeEntityFactory.createAll();
        cakeEntities.forEach(cakeEntityRepository::save);
    }
}
