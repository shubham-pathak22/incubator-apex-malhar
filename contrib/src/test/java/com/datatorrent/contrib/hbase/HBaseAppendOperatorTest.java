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
package com.datatorrent.contrib.hbase;

import junit.framework.Assert;

import org.apache.hadoop.hbase.client.Append;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.contrib.hbase.HBaseAppendOperator;
import com.datatorrent.contrib.hbase.HBaseRowStatePersistence;
import com.datatorrent.contrib.hbase.HBaseStatePersistenceStrategy;
import com.datatorrent.api.DAG;
import com.datatorrent.api.LocalMode;

/**
 *
 */
public class HBaseAppendOperatorTest
{
  private static final Logger logger = LoggerFactory.getLogger(HBasePutOperatorTest.class);

  public HBaseAppendOperatorTest()
  {
  }

  @Test
  public void testAppend()
  {
    try {
      HBaseTestHelper.clearHBase();
      LocalMode lma = LocalMode.newInstance();
      DAG dag = lma.getDAG();

      dag.setAttribute(DAG.APPLICATION_NAME, "HBaseAppendOperatorTest");
      HBaseColTupleGenerator ctg = dag.addOperator("coltuplegenerator", HBaseColTupleGenerator.class);
      TestHBaseAppendOperator thop = dag.addOperator("testhbaseput", TestHBaseAppendOperator.class);
      dag.addStream("ss", ctg.outputPort, thop.inputPort);

      thop.setTableName("table1");
      thop.setZookeeperQuorum("127.0.0.1");
      thop.setZookeeperClientPort(2181);

      final LocalMode.Controller lc = lma.getController();
      lc.setHeartbeatMonitoringEnabled(false);
      lc.run(30000);

      /*
      tuples = new ArrayList<HBaseTuple>();
      TestHBaseScanOperator thop = new TestHBaseScanOperator();
           thop.setTableName("table1");
      thop.setZookeeperQuorum("127.0.0.1");
      thop.setZookeeperClientPort(2822);
      thop.setupConfiguration();

      thop.emitTuples();
      */

      // TODO review the generated test code and remove the default call to fail.
      //fail("The test case is a prototype.");
      // Check total number
      HBaseTuple tuple = HBaseTestHelper.getHBaseTuple("row0", "colfam0", "col-0");
      Assert.assertNotNull("Tuple", tuple);
      Assert.assertEquals("Tuple row", tuple.getRow(), "row0");
      Assert.assertEquals("Tuple column family", tuple.getColFamily(), "colfam0");
      Assert.assertEquals("Tuple column name", tuple.getColName(), "col-0");
      Assert.assertEquals("Tuple column value", tuple.getColValue(), "val-0-0");
      tuple = HBaseTestHelper.getHBaseTuple("row0", "colfam0","col-499");
      Assert.assertNotNull("Tuple", tuple);
      Assert.assertEquals("Tuple row", tuple.getRow(), "row0");
      Assert.assertEquals("Tuple column family", tuple.getColFamily(), "colfam0");
      Assert.assertEquals("Tuple column name", tuple.getColName(), "col-499");
      Assert.assertEquals("Tuple column value", tuple.getColValue(), "val-0-499");
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      assert false;
    }
  }

  public static class TestHBaseAppendOperator extends HBaseAppendOperator<HBaseTuple> {

    @Override
    public Append operationAppend(HBaseTuple t)
    {
      Append append = new Append(t.getRow().getBytes());
      append.add(t.getColFamily().getBytes(), t.getColName().getBytes(), t.getColValue().getBytes());
      return append;
    }

    @Override
    public HBaseStatePersistenceStrategy getPersistenceStrategy() {
      return new HBaseRowStatePersistence();
    }

  }
}
