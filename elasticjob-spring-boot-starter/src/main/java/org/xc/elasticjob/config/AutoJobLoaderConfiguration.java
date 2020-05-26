package org.xc.elasticjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.xc.elasticjob.config.loader.impl.AnnotationJobLoader;
import org.xc.elasticjob.config.loader.impl.PropertiesJobLoader;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化所有的job
 * @Author doolin
 */
@Data
@Configuration
@AutoConfigureAfter(AutoZookeeperConfiguration.class)
@ConfigurationProperties(prefix = "spring.elastic.job")
public class AutoJobLoaderConfiguration {
    JobConfig config;

    @Bean
    @ConditionalOnBean({ZookeeperRegistryCenter.class,JobConfig.class})
    public PropertiesJobLoader jobLoader(ZookeeperRegistryCenter zookeeperRegistryCenter, ApplicationContext applicationContext) {
        PropertiesJobLoader propertiesJobLoader = new PropertiesJobLoader();
        propertiesJobLoader.load(zookeeperRegistryCenter,applicationContext,config);
        return propertiesJobLoader;
    }

    private void loadDBJobConfig(JobConfig jobConfig){
    }

    @Bean
    @ConditionalOnBean({ZookeeperRegistryCenter.class,JobConfig.class})
    public AnnotationJobLoader annotationJobLoader(ZookeeperRegistryCenter zookeeperRegistryCenter, ApplicationContext applicationContext){
        AnnotationJobLoader annotationJobLoader = new AnnotationJobLoader();
        annotationJobLoader.load(zookeeperRegistryCenter,applicationContext);
        return annotationJobLoader;
    }
}

