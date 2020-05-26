package org.xc.elasticjob.config.properties;

import lombok.Data;

/**
 * 用来存储所有job配置信息
 * @Author doolin
 */
@Data
public class ScriptJobProperties extends AbstractJobProperties {

    /*********************ScriptJobConfiguration START********************/
    /**
     * 脚本型作业执行命令行
     */
    private String scriptCommandLine;

    /*********************ScriptJobConfiguration END********************/

}
