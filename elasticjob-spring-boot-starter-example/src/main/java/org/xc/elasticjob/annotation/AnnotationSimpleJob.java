package org.xc.elasticjob.annotation;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Author doolin
 */
@SimpleElasticJob(name = "AnnotationSimpleJob",cron = "0/5 * * * * ?",overwrite = true)
public class AnnotationSimpleJob implements SimpleJob {
    @Override public void execute(ShardingContext shardingContext) {

        System.out.println("haha");
    }
}
