elastic-job的springbootstarter，目前还在更新开发中……
#### 现支持配置文件与注解两种方式来注册elastic-job任务
#### 配置文件方式
```
spring:
  elastic:
    job:
      zookeeper:
        serverLists: localhost:2181
        namespace: test-job
      config:
        simpleJob:
          -
            name: test
            cron: 0/5 * * * * ?
            shardingTotalCount: 3
            jobClass: org.xc.elasticjob.MyJob
            overwrite: true
            listeners:
              - org.xc.elasticjob.listener.MyListener
              - org.xc.elasticjob.listener.MyListener
        dataFlowJob:
          - name: dataflowtest
            cron: 0/5 * * * * ?
            shardingTotalCount: 1
            jobClass: org.xc.elasticjob.MyDataFlowJob
            overwrite: true
            streamingProcess: true
            disabled: true
        scriptJob:
          - name: scripttest
            cron: 0/1 * * * * ?
            shardingTotalCount: 1
            overwrite: true
            scriptCommandLine: C:/Users/xc/Desktop/test.bat
            disabled: true
```
#### 注解方式
```java
@SimpleElasticJob(name = "AnnotationSimpleJob",cron = "0/5 * * * * ?",overwrite = true,listener = {"org.xc.listener.MyListener"})
public class AnnotationSimpleJob implements SimpleJob {
    @Override public void execute(ShardingContext shardingContext) {
        System.out.println("haha");
    }
}



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
```