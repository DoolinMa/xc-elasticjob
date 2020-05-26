package org.xc.elasticjob.config.loader.impl;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.xc.elasticjob.config.JobConfig;
import org.xc.elasticjob.config.loader.IJobLoader;
import org.xc.elasticjob.config.properties.AbstractJobProperties;
import org.xc.elasticjob.config.properties.DataFlowJobProperties;
import org.xc.elasticjob.config.properties.ScriptJobProperties;
import org.xc.elasticjob.config.properties.SimpleJobProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author doolin
 */
public class PropertiesJobLoader implements IJobLoader {
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    private ApplicationContext applicationContext;

    public void load(ZookeeperRegistryCenter zookeeperRegistryCenter,ApplicationContext applicationContext,JobConfig jobConfig) {
        this.applicationContext=applicationContext;
        this.zookeeperRegistryCenter=zookeeperRegistryCenter;
        Optional.ofNullable(jobConfig.getSimpleJob()).ifPresent(this::loadSimpleJobs);
        Optional.ofNullable(jobConfig.getDataFlowJob()).ifPresent(this::loadDataflowJobs);
        Optional.ofNullable(jobConfig.getScriptJob()).ifPresent(this::loadScriptJobs);
    }

    @Override
    public void loadSimpleJobs(List<SimpleJobProperties> jobPropertiesList) {
        for (SimpleJobProperties jobProp : jobPropertiesList) {
            //获取作业类型配置
            JobTypeConfiguration jobTypeConfiguration = new SimpleJobConfiguration(getJobCoreConfiguration(jobProp),jobProp.getJobClass());
            registerJob(jobProp, jobTypeConfiguration);
        }
    }

    @Override
    public void loadDataflowJobs(List<DataFlowJobProperties> jobPropertiesList) {
        for (DataFlowJobProperties jobProp : jobPropertiesList) {
            //获取作业类型配置
            JobTypeConfiguration jobTypeConfiguration = new DataflowJobConfiguration(getJobCoreConfiguration(jobProp),jobProp.getJobClass(),jobProp.isStreamingProcess());
            //获取Lite作业配置
            registerJob(jobProp, jobTypeConfiguration);
        }
    }

    @Override
    public void loadScriptJobs( List<ScriptJobProperties> jobPropertiesList) {

        for (ScriptJobProperties jobProp : jobPropertiesList) {
            //获取作业类型配置
            JobTypeConfiguration jobTypeConfiguration = new ScriptJobConfiguration(getJobCoreConfiguration(jobProp),jobProp.getScriptCommandLine());
            //获取Lite作业配置
            registerJob(jobProp, jobTypeConfiguration);
        }
    }
    //添加腳本任务到zookeeper中
    private void registerJob(ScriptJobProperties jobProp, JobTypeConfiguration jobTypeConfiguration) {
        //获取Lite作业配置
        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobTypeConfiguration, jobProp);

        JobEventConfiguration jobEventConfiguration = jobProp.getJobEventConfigurations();
        //获取作业监听器
        ElasticJobListener[] elasticJobListeners = jobProp.getListeners();
        //注册作业
        if (null == jobEventConfiguration) {
            new SpringJobScheduler(null,zookeeperRegistryCenter, liteJobConfiguration, elasticJobListeners).init();
        } else {
            new SpringJobScheduler(null, zookeeperRegistryCenter, liteJobConfiguration, jobEventConfiguration, elasticJobListeners).init();
        }
    }
    //添加simple和dataflow任务到zookeeper中
    private void registerJob(SimpleJobProperties jobProp, JobTypeConfiguration jobTypeConfiguration) {
        //获取Lite作业配置
        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobTypeConfiguration, jobProp);

        JobEventConfiguration jobEventConfiguration = jobProp.getJobEventConfigurations();
        //获取作业监听器
        ElasticJobListener[] elasticJobListeners = jobProp.getListeners();
        //注册作业
        if (null == jobEventConfiguration) {
            new SpringJobScheduler(registerBean(jobProp.getName(), jobProp.getJobClass(), ElasticJob.class),
                zookeeperRegistryCenter, liteJobConfiguration, elasticJobListeners).init();
        } else {
            new SpringJobScheduler(registerBean(jobProp.getName(), jobProp.getJobClass(), ElasticJob.class),
                zookeeperRegistryCenter, liteJobConfiguration, jobEventConfiguration, elasticJobListeners).init();
        }
    }

    private ElasticJobListener[] creatElasticJobListeners(List<String> jobListenerList) {
        List<ElasticJobListener> elasticJobListenerList = new ArrayList<>();
        for (String jobListener : jobListenerList) {
            try {
                //@TODO 需要看下怎么获取实例
                elasticJobListenerList.add((ElasticJobListener)Class.forName(jobListener).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        ElasticJobListener[] elasticJobListeners = new ElasticJobListener[0];
        elasticJobListenerList.toArray(elasticJobListeners);
        return elasticJobListeners;
    }

    private JobCoreConfiguration getJobCoreConfiguration(AbstractJobProperties jobProperties) {
        return JobCoreConfiguration.newBuilder(jobProperties.getName(), jobProperties.getCron(), jobProperties.getShardingTotalCount())
                .shardingItemParameters(jobProperties.getShardingItemParameters())
                .description(jobProperties.getDescription())
                .failover(jobProperties.isFailover())
                .jobParameter(jobProperties.getJobParameter())
                .misfire(jobProperties.isMisfire())
                .jobProperties(com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobProperties.getJobExceptionHandler())
                .jobProperties(com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), jobProperties.getExecutorServiceHandler())
                .build();
    }

    /**
     * 构建Lite作业
     *
     * @param jobTypeConfiguration 任务类型
     * @param jobProperties     任务配置
     * @return LiteJobConfiguration
     */
    private LiteJobConfiguration getLiteJobConfiguration(JobTypeConfiguration jobTypeConfiguration, AbstractJobProperties jobProperties) {
        //构建Lite作业
        return LiteJobConfiguration.newBuilder(Objects.requireNonNull(jobTypeConfiguration))
                .monitorExecution(jobProperties.isMonitorExecution())
                .monitorPort(jobProperties.getMonitorPort())
                .maxTimeDiffSeconds(jobProperties.getMaxTimeDiffSeconds())
                .jobShardingStrategyClass(jobProperties.getJobShardingStrategyClass())
                .reconcileIntervalMinutes(jobProperties.getReconcileIntervalMinutes())
                .disabled(jobProperties.isDisabled())
                .overwrite(jobProperties.isOverwrite()).build();
    }
    /**
     * 向spring容器中注册bean
     *
     * @param beanName            bean名字
     * @param strClass            类全路径
     * @param tClass              类类型
     * @param constructorArgValue 构造函数参数
     * @param <T>                 泛型
     * @return T
     */
    protected <T> T registerBean(String beanName, String strClass, Class<T> tClass, Object... constructorArgValue) {
        //判断是否配置了监听者
        if (StringUtils.isBlank(strClass)) {
            return null;
        }
        if (StringUtils.isBlank(beanName)) {
            beanName = strClass;
        }
        //判断监听者是否已经在spring容器中存在
        if (applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName, tClass);
        }
        //不存在则创建并注册到Spring容器中
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(strClass);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        //设置参数
        for (Object argValue : constructorArgValue) {
            beanDefinitionBuilder.addConstructorArgValue(argValue);
        }
        getDefaultListableBeanFactory(applicationContext).registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return applicationContext.getBean(beanName, tClass);
    }
    /**
     * 获取beanFactory
     *
     * @return DefaultListableBeanFactory
     * @param applicationContext
     */
    private DefaultListableBeanFactory getDefaultListableBeanFactory(ApplicationContext applicationContext) {
        return (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
    }

}
