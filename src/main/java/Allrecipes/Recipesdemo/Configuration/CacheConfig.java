package Allrecipes.Recipesdemo.Configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

/**
 * Configuration class for caching.
 *
 * This enables Spring's caching support and configures a simple in-memory cache.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        // You can specify cache names here if you have multiple caches
        return new ConcurrentMapCacheManager("recipes", "ratings", "comments");
    }
}
