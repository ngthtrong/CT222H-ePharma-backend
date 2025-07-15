package ct222h.vegeta.projectbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.Index;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class MongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        // Tạo unique index cho email trong collection users
        IndexOperations indexOps = mongoTemplate.indexOps("users");
        IndexDefinition emailIndex = new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique();
        
        // Sử dụng method mới thay cho ensureIndex deprecated
        if (!indexOps.getIndexInfo().stream().anyMatch(index -> 
            index.getIndexFields().stream().anyMatch(field -> "email".equals(field.getKey())))) {
            indexOps.ensureIndex(emailIndex);
        }
    }
}
