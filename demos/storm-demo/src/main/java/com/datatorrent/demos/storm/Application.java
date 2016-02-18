/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.demos.storm;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context.PortContext;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.contrib.storm.BoltWrapper;
import com.datatorrent.contrib.storm.SpoutWrapper;
import com.datatorrent.contrib.storm.StormTupleStreamCodec;

@ApplicationAnnotation(name = "storm-demo")
public class Application implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    SpoutWrapper input = dag.addOperator("input", new SpoutWrapper(new WordSpout(), "sentence"));
    BoltWrapper tokens = dag.addOperator("tokenizer", new BoltWrapper(new BoltTokenizer(), "tokens"));
    BoltWrapper counter = dag.addOperator("counter", new BoltWrapper(new BoltCounter(), "counter"));
    BoltWrapper sink = dag.addOperator("sink",
        new BoltWrapper(new BoltPrintSink(new TupleOutputFormatter()), "output"));
    dag.setInputPortAttribute(counter.input, PortContext.STREAM_CODEC, new StormTupleStreamCodec(new int[] { 0 }));
    dag.addStream("input", input.output, tokens.input);
    dag.addStream("word-count", tokens.output, counter.input);
    dag.addStream("output", counter.output, sink.input);

  }

}
