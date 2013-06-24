/*
 * Copyright (c) 2013 Malhar Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */
package com.datatorrent.contrib.summit.ads;

import com.datatorrent.api.Context.OperatorContext;
import com.datatorrent.api.Context.PortContext;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.contrib.redis.RedisNumberAggregateOutputOperator;
import java.util.Map;
import org.apache.commons.lang.mutable.MutableDouble;
import org.apache.hadoop.conf.Configuration;

/**
 *
 */
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    dag.setAttribute(DAG.APPLICATION_NAME, "AdsDimension");

    InputGenerator input = dag.addOperator("input", InputGenerator.class);
    dag.setOutputPortAttribute(input.outputPort, PortContext.QUEUE_CAPACITY, 32 * 1024);
    dag.setAttribute(input, OperatorContext.INITIAL_PARTITION_COUNT, 9);
    BucketOperator bucket = dag.addOperator("bucket", BucketOperator.class);
    dag.setInputPortAttribute(bucket.inputPort, PortContext.PARTITION_PARALLEL, true);

    dag.setInputPortAttribute(bucket.inputPort, PortContext.QUEUE_CAPACITY, 32 * 1024);
    dag.setOutputPortAttribute(bucket.outputPort, PortContext.QUEUE_CAPACITY, 32 * 1024);
    dag.setAttribute(bucket, OperatorContext.APPLICATION_WINDOW_COUNT, 10);

    RedisNumberAggregateOutputOperator<AggrKey, Map<String, MutableDouble>> redis = dag.addOperator("redis", RedisNumberAggregateOutputOperator.class);
    dag.setInputPortAttribute(redis.input, PortContext.QUEUE_CAPACITY, 32 * 1024);
    dag.setAttribute(redis, OperatorContext.INITIAL_PARTITION_COUNT, 2);

    dag.addStream("ingen", input.outputPort, bucket.inputPort).setInline(true);
    dag.addStream("store", bucket.outputPort, redis.inputInd);
  }


}
