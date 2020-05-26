package org.xc.elasticjob.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用来存储所有job配置信息
 * @Author doolin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataFlowJobProperties extends SimpleJobProperties {

    /*********************DataflowJobConfiguration START********************/

    /**
     * 是否流式处理数据
     * <p>如果流式处理数据, 则fetchData不返回空结果将持续执行作业<p>
     * <p>如果非流式处理数据, 则处理数据完成后作业结束<p>
     */
    private boolean streamingProcess;

    /*********************DataflowJobConfiguration END********************/
}
