package org.xc.elasticjob.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * @author doolin
 */
@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleElasticJob {
    String name();
    String cron();
    int shardingTotalCount() default 1;
    String shardingItemParameters() default "";
    String jobParameter() default "";
    boolean failover() default false;
    boolean misfire() default false;
    String description() default "";
    boolean overwrite() default false;
    boolean monitorExecution() default true;
    int monitorPort() default -1;
    int maxTimeDiffSeconds() default -1;
    String jobShardingStrategyClass() default "";
    int reconcileIntervalMinutes() default 10;
    String eventTraceRdbDataSource() default "";
    String[] listener() default {};
    boolean disabled() default false;
    String distributedListener() default "";
    long startedTimeoutMilliseconds() default Long.MAX_VALUE;
    long completedTimeoutMilliseconds() default Long.MAX_VALUE;
    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";
    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
    String jobEventConfigurations() default "";
}
