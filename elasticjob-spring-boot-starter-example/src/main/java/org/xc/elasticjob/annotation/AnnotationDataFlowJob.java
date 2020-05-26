package org.xc.elasticjob.annotation;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author THTF
 */
@DataFlowElasticJob(name = "AnnotationDataFlowJob",cron = "0/5 * * * * ?",overwrite = true)
public class AnnotationDataFlowJob implements DataflowJob {
    @Override public List fetchData(ShardingContext shardingContext) {
        System.out.println("AnnotationDataFlowJob-fetchData");
        return new ArrayList();
    }

    @Override public void processData(ShardingContext shardingContext, List list) {

        System.out.println("AnnotationDataFlowJob-processData");
    }
}
