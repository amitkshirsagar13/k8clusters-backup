package io.k8clusters.base.modules.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisCacheAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class RedisCacheAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "k8cluster.modules.cache.redis")
    public K8ClusterRedisCacheConfiguration k8ClusterRedisCacheConfiguration() {
        return new K8ClusterRedisCacheConfiguration();
    }


    @Bean
    @ConditionalOnMissingBean
    JedisConnectionFactory jedisConnectionFactory(K8ClusterRedisCacheConfiguration k8ClusterRedisCacheConfiguration) {
//        RedisClusterConfiguration redisConfiguration = new RedisClusterConfiguration(Arrays.asList(k8ClusterRedisCacheConfiguration.getNodes().split(",")));
//        redisConfiguration.setMaxRedirects(k8ClusterRedisCacheConfiguration.getMaxRedirects());
//        redisConfiguration.setPassword(k8ClusterRedisCacheConfiguration.getPassword());

        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(k8ClusterRedisCacheConfiguration.getRedisHost(), k8ClusterRedisCacheConfiguration.getRedisPort());
        redisConfiguration.setPassword(RedisPassword.of(k8ClusterRedisCacheConfiguration.getPassword()));

        final JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisConfiguration);
        return connectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<?, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        final RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
