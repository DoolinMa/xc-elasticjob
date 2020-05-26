package org.xc.elasticjob.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用来存储所有job配置信息
 * @Author doolin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleJobProperties extends AbstractJobProperties {
    private String jobClass;
}
