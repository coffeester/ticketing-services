package com.coffeester.ticketing.testconfigs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;

/**
 * Created by amitsehgal on 1/30/16.
 */
@EnableAutoConfiguration
@Component
public class TestSeatAvailabilityConfig {

    @Autowired
    Environment env;

    //@Bean(destroyMethod = "shutdown")
    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName(env.getProperty("test.db.name")).
                addScript("test-schema.sql").
                addScript(env.getProperty("test.db.sql")).
                build();
    }
}
