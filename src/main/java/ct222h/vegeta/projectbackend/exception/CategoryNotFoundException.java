package ct222h.vegeta.projectbackend.exception;

/**
 * Exception thrown when a category is not found
 * 
 * @author CT222H Team
 * @version 2.0
 */
public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(String message) {
        super(message);
    }
    
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
