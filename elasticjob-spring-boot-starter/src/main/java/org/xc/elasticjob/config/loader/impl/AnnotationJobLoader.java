package org.xc.elasticjob.config.loader.impl;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.xc.elasticjob.annotation.DataFlowElasticJob;
import org.xc.elasticjob.annotation.ScriptElasticJob;
import org.xc.elasticjob.annotation.SimpleElasticJob;
import org.xc.elasticjob.config.JobConfig;
import org.xc.elasticjob.config.properties.DataFlowJobProperties;
import org.xc.elasticjob.config.properties.ScriptJobProperties;
import org.xc.elasticjob.config.properties.SimpleJobProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author doolin
 */
@Slf4j
public class AnnotationJobLoader extends PropertiesJobLoader {

    public void load(ZookeeperRegistryCenter zookeeperRegistryCenter, ApplicationContext applicationContext) {
        JobConfig jobConfig = new JobConfig();
        jobConfig.setSimpleJob(transAnnotation2SimpleJobConfig(applicationContext.getBeansWithAnnotation(SimpleElasticJob.class)));
        jobConfig.setDataFlowJob(transAnnotation2DataFlowJobConfig(applicationContext.getBeansWithAnnotation(DataFlowElasticJob.class)));
        jobConfig.setScriptJob(transAnnotation2ScriptJobConfig(applicationContext.getBeansWithAnnotation(ScriptElasticJob.class)));

        super.load(zookeeperRegistryCenter,applicationContext,jobConfig);
    }

    private List<ScriptJobProperties> transAnnotation2ScriptJobConfig(Map<String, Object> beansWithAnnotation) {
        return null;
    }

    private List<DataFlowJobProperties> transAnnotation2DataFlowJobConfig(Map<String, Object> beansWithAnnotation) {
        ArrayList<DataFlowJobProperties> dataFlowJobProperties = new ArrayList<>();
        if(beansWithAnnotation != null && !beansWithAnnotation.isEmpty()){
            beansWithAnnotation.forEach((beanId, beanInstance) -> {
                if(beanInstance instanceof DataflowJob){
                    DataFlowElasticJob annotation = beanInstance.getClass().getAnnotation(DataFlowElasticJob.class);
                    DataFlowJobProperties dataFlowJobPropertie = getDataFlowJobProperties(beanInstance, annotation);
                    dataFlowJobProperties.add(dataFlowJobPropertie);
                }else {
                    log.warn("注解DataFlowElasticJob只能用于DataflowJob类型的JOB");
                }
            });
        }
        return dataFlowJobProperties;
    }



    private List<SimpleJobProperties> transAnnotation2SimpleJobConfig(Map<String, Object> beansWithAnnotation) {
        ArrayList<SimpleJobProperties> simpleJobProperties = new ArrayList<>();
        if(beansWithAnnotation != null && !beansWithAnnotation.isEmpty()){
            beansWithAnnotation.forEach((beanId, beanInstance) -> {
                if(beanInstance instanceof SimpleJob){
                    SimpleElasticJob annotation = beanInstance.getClass().getAnnotation(SimpleElasticJob.class);
                    SimpleJobProperties simpleJobPropertie = getSimpleJobProperties(beanInstance, annotation);
                    simpleJobProperties.add(simpleJobPropertie);
                }else {
                    log.warn("注解SimpleElasticJob只能用于SimpleJob类型的JOB");
                }
            });
        }
        return simpleJobProperties;
    }

    private DataFlowJobProperties getDataFlowJobProperties(Object beanInstance, DataFlowElasticJob annotation) {
        DataFlowJobProperties dataFlowJobPropertie = new DataFlowJobProperties();
        dataFlowJobPropertie.setJobClass(beanInstance.getClass().getCanonicalName());
        dataFlowJobPropertie.setName(annotation.name());
        dataFlowJobPropertie.setCron(annotation.cron());
        dataFlowJobPropertie.setShardingTotalCount(annotation.shardingTotalCount());
        dataFlowJobPropertie.setShardingItemParameters(annotation.shardingItemParameters());
        dataFlowJobPropertie.setJobParameter(annotation.jobParameter());
        dataFlowJobPropertie.setFailover(annotation.failover());
        dataFlowJobPropertie.setMisfire(annotation.misfire());
        dataFlowJobPropertie.setDescription(annotation.description());
        dataFlowJobPropertie.setOverwrite(annotation.overwrite());
        dataFlowJobPropertie.setMonitorExecution(annotation.monitorExecution());
        dataFlowJobPropertie.setMonitorPort(annotation.monitorPort());
        dataFlowJobPropertie.setMaxTimeDiffSeconds(annotation.maxTimeDiffSeconds());
        dataFlowJobPropertie.setJobShardingStrategyClass(annotation.jobShardingStrategyClass());
        dataFlowJobPropertie.setReconcileIntervalMinutes(annotation.reconcileIntervalMinutes());
        dataFlowJobPropertie.setJobEventConfigurations(annotation.jobEventConfigurations());
        dataFlowJobPropertie.setListeners(annotation.listener());
        dataFlowJobPropertie.setDisabled(annotation.disabled());
        dataFlowJobPropertie.setDistributedListener(annotation.distributedListener());
        dataFlowJobPropertie.setStartedTimeoutMilliseconds(annotation.startedTimeoutMilliseconds());
        dataFlowJobPropertie.setCompletedTimeoutMilliseconds(annotation.completedTimeoutMilliseconds());
        dataFlowJobPropertie.setJobExceptionHandler(annotation.jobExceptionHandler());
        dataFlowJobPropertie.setExecutorServiceHandler(annotation.executorServiceHandler());
        dataFlowJobPropertie.setStreamingProcess(annotation.streamingProcess());
        return dataFlowJobPropertie;
    }

    private SimpleJobProperties getSimpleJobProperties(Object beanInstance, SimpleElasticJob annotation) {
        SimpleJobProperties simpleJobPropertie = new SimpleJobProperties();
        simpleJobPropertie.setJobClass(beanInstance.getClass().getCanonicalName());
        simpleJobPropertie.setName(annotation.name());
        simpleJobPropertie.setCron(annotation.cron());
        simpleJobPropertie.setShardingTotalCount(annotation.shardingTotalCount());
        simpleJobPropertie.setShardingItemParameters(annotation.shardingItemParameters());
        simpleJobPropertie.setJobParameter(annotation.jobParameter());
        simpleJobPropertie.setFailover(annotation.failover());
        simpleJobPropertie.setMisfire(annotation.misfire());
        simpleJobPropertie.setDescription(annotation.description());
        simpleJobPropertie.setOverwrite(annotation.overwrite());
        simpleJobPropertie.setMonitorExecution(annotation.monitorExecution());
        simpleJobPropertie.setMonitorPort(annotation.monitorPort());
        simpleJobPropertie.setMaxTimeDiffSeconds(annotation.maxTimeDiffSeconds());
        simpleJobPropertie.setJobShardingStrategyClass(annotation.jobShardingStrategyClass());
        simpleJobPropertie.setReconcileIntervalMinutes(annotation.reconcileIntervalMinutes());
        simpleJobPropertie.setJobEventConfigurations(annotation.jobEventConfigurations());
        simpleJobPropertie.setListeners(annotation.listener());
        simpleJobPropertie.setDisabled(annotation.disabled());
        simpleJobPropertie.setDistributedListener(annotation.distributedListener());
        simpleJobPropertie.setStartedTimeoutMilliseconds(annotation.startedTimeoutMilliseconds());
        simpleJobPropertie.setCompletedTimeoutMilliseconds(annotation.completedTimeoutMilliseconds());
        simpleJobPropertie.setJobExceptionHandler(annotation.jobExceptionHandler());
        simpleJobPropertie.setExecutorServiceHandler(annotation.executorServiceHandler());
        return simpleJobPropertie;
    }
}
