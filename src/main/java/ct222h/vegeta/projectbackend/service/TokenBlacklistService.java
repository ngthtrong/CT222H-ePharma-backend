package ct222h.vegeta.projectbackend.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    
    /**
     * Thêm token vào blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
    
    /**
     * Kiểm tra token có bị blacklist không
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    /**
     * Xóa token khỏi blacklist (cho trường hợp token hết hạn)
     */
    public void removeExpiredToken(String token) {
        blacklistedTokens.remove(token);
    }
    
    /**
     * Lấy số lượng token đang bị blacklist
     */
    public int getBlacklistedTokenCount() {
        return blacklistedTokens.size();
    }
}
