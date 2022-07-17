package com.viettelpost.core.cfgs;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;

//@Configuration
public class MongoCfg {

    @Autowired
    Environment env;

//    public MongoClientOptions mongoClientOptions() {
//        MongoClientOptions options = MongoClientOptions.builder()
//                .maxWaitTime(10000)
//                .connectionsPerHost(50)
//                .maxConnectionIdleTime(10000)
//                .maxConnectionLifeTime(120000)
//                .serverSelectionTimeout(2000)
//                .build();
//        return options;
//    }

    MongoClientSettings mongoClientSettings() {
        String password = env.getRequiredProperty("db.mongo.password");
        MongoCredential credential = MongoCredential.createCredential("customer_user", "CUSTOMER", password.toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(false))
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("10.60.117.88", 27017))))
                .build();
        return settings;
    }

    @Bean(name = {"mg28"})
    @Primary
    public com.mongodb.client.MongoClient mongo28() {
        return MongoClients.create(mongoClientSettings());
    }


    @Bean(name = {"mongo28"})
    @Primary
    public MongoTemplate cacheTemplate(@Qualifier("mg28") com.mongodb.client.MongoClient mog) throws Exception {
        return new MongoTemplate(mog, "CUSTOMER");
    }
}
