package ct222h.vegeta.projectbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.bson.Document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public String testConnection() {
        try {
            // Test the connection by running a simple ping command
            mongoTemplate.execute(db -> {
                Document ping = new Document("ping", 1);
                return db.runCommand(ping);
            });
            return "Successfully connected to MongoDB Atlas!";
        } catch (Exception e) {
            return "Failed to connect to MongoDB Atlas: " + e.getMessage();
        }
    }

    @GetMapping("/test-detailed")
    public String testConnectionDetailed() {
        try {
            // Get database stats for more detailed testing
            Document stats = mongoTemplate.execute(db -> {
                return db.runCommand(new Document("dbStats", 1));
            });
            return "Successfully connected to MongoDB Atlas! Database: " + stats.getString("db") +
                   ", Collections: " + stats.getInteger("collections", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to connect to MongoDB Atlas: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    @PostMapping("/test-insert")
    public String testInsert() {
        try {
            // Tạo một document test đơn giản
            Document testDoc = new Document()
                    .append("message", "Test insert from API")
                    .append("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .append("status", "success")
                    .append("testNumber", Math.random() * 1000);
            
            // Insert vào collection "test_data"
            mongoTemplate.insert(testDoc, "test_data");
            
            return "Successfully inserted test data into MongoDB! Document ID: " + testDoc.getObjectId("_id");
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to insert test data: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    @GetMapping("/test-count")
    public String testCount() {
        try {
            long count = mongoTemplate.getCollection("test_data").countDocuments();
            return "Total test documents in database: " + count;
        } catch (Exception e) {
            return "Failed to count test data: " + e.getMessage();
        }
    }
}
