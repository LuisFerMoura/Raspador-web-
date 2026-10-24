package utfpr.tsi.edu.br.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "utfpr.tsi.edu.br.repositories.destino",
        mongoTemplateRef = "destinoMongoTemplate"
)
public class DestinoMongoConfig {

    @Value("${spring.data.mongodb.destino.uri}")
    private String mongoUri;

    @Bean(name = "destinoMongoTemplate")
    public MongoTemplate destinoMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUri));
    }
}