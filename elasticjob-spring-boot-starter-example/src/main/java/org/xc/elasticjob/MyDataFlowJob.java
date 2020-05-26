package org.xc.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author doolin
 */
public class MyDataFlowJob implements DataflowJob {
    @Override public List fetchData(ShardingContext shardingContext) {
        System.out.println("fetchData");
        return new ArrayList();
    }

    @Override public void processData(ShardingContext shardingContext, List list) {

        System.out.println("processData");
    }
}
