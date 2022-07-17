package com.viettelpost.core;

import co.elastic.apm.attach.ElasticApmAttacher;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import com.viettelpost.core.utils.Utils;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class
        , MongoRepositoriesAutoConfiguration.class
        , MongoDataAutoConfiguration.class
        , KafkaAutoConfiguration.class})
@EnableEncryptableProperties
@PropertySource("cfg.properties")
public class CrmV2ApiApplication {

    static final String defaultPassword = "123456a#";

    public static void main(String[] args) {
        String password = System.getenv("jasypt_erpapi_password");
        System.setProperty("jasypt.encryptor.password", Utils.isNullOrEmpty(password) ? defaultPassword : password);
        System.out.println("----");
        System.out.println("#loading private key " + password);
        SpringApplication.run(CrmV2ApiApplication.class, args);
        ElasticApmAttacher.attach();
    }
}

