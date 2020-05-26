package org.xc.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MyJob implements SimpleJob {

    @Override
    public void execute(ShardingContext context) {
        switch (context.getShardingItem()) {
            case 0:
                System.out.println(0);
                // do something by sharding item 0
                break;
            case 1:
                System.out.println(1);
                // do something by sharding item 1
                break;
            case 2:
                System.out.println(2);
                // do something by sharding item 2
                break;
            // case n: ...
        }
    }
}
