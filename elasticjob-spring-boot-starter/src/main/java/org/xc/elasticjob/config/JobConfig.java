package org.xc.elasticjob.config;

import org.xc.elasticjob.config.properties.DataFlowJobProperties;
import org.xc.elasticjob.config.properties.ScriptJobProperties;
import org.xc.elasticjob.config.properties.SimpleJobProperties;
import lombok.Data;

import java.util.List;

@Data
public class JobConfig {
    List<SimpleJobProperties> simpleJob;
    List<ScriptJobProperties> scriptJob;
    List<DataFlowJobProperties> dataFlowJob;
}
