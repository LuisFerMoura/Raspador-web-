package utfpr.tsi.edu.br.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "utfpr.tsi.edu.br.repositories.origem",
        mongoTemplateRef = "origemMongoTemplate"
)
public class OrigemMongoConfig {

    @Value("${spring.data.mongodb.origem.uri}")
    private String mongoUri;

    @Primary
    @Bean(name = "origemMongoTemplate")
    public MongoTemplate origemMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUri));
    }
}