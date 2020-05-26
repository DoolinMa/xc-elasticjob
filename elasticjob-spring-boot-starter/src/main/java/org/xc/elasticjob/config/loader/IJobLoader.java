package org.xc.elasticjob.config.loader;

import org.xc.elasticjob.config.properties.DataFlowJobProperties;
import org.xc.elasticjob.config.properties.ScriptJobProperties;
import org.xc.elasticjob.config.properties.SimpleJobProperties;

import java.util.List;

/**
 * @author doolin
 */
public interface IJobLoader {

    void loadSimpleJobs(List<SimpleJobProperties> jobPropertiesList);

    void loadDataflowJobs(List<DataFlowJobProperties> jobPropertiesList);

    void loadScriptJobs(List<ScriptJobProperties> jobPropertiesList);
}
