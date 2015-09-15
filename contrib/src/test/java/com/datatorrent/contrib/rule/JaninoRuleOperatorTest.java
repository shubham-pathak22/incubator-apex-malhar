package com.datatorrent.contrib.rule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.datatorrent.lib.testbench.CollectorTestSink;
import com.datatorrent.lib.util.KeyValPair;
import com.datatorrent.lib.util.TestUtils;

public class JaninoRuleOperatorTest
{

  JaninoRuleOperator operator;
  CollectorTestSink<KeyValPair<DtRule,Object>> validDataSink;
  CollectorTestSink<KeyValPair<DtRule,Object>> invalidDataSink;
  
  @Rule 
  public Watcher watcher = new Watcher();
  
  public class Watcher extends TestWatcher {
    
    @Override
    protected void starting(Description description)
    {
      super.starting(description);
      operator = new JaninoRuleOperator();
      operator.setPojoClass(StockBean.class);
      validDataSink = new CollectorTestSink<KeyValPair<DtRule,Object>>();
      invalidDataSink = new CollectorTestSink<KeyValPair<DtRule,Object>>();
      TestUtils.setSink(operator.outPassed, validDataSink);
      TestUtils.setSink(operator.outFailed, invalidDataSink);
    }
    
    @Override
    protected void finished(Description description)
    {
      super.finished(description);
      operator.teardown();
    }
}
  
  @Test
  public void testSimpleJavaRulePositiveCase(){
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("opening stock price check");
    rule.setExpression("pojo.getoPrice() == 1");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setup(null);
    operator.process(stockBean);
    
    StockBean tuple = (StockBean) validDataSink.collectedTuples.get(0).getValue();
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(1.0, tuple.getoPrice(),0);
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testSimpleJavaRuleNegativeCase(){
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("opening stock price check");
    rule.setExpression("pojo.getoPrice() == 1");
    
    StockBean stockBean = new StockBean(2,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setup(null);
    operator.process(stockBean);
    
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    StockBean tuple = (StockBean) invalidDataSink.collectedTuples.get(0).getValue();
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(2.0, tuple.getoPrice(),0);
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testINopertaionPositiveCase(){
    List<Object> stockList = new ArrayList<Object>();
    stockList.add("MSFT");
    stockList.add("APPL");
    
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("check stock IN list");
    rule.setExpression("RULEFUNC.IN(pojo.getSymbol(),stockList)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"stockList"});
    operator.setParameterTypes(new Class[]{List.class});
    operator.setArguments(new Object[]{stockList});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testINopertaionNegativeCase(){
    List<Object> stockList = new ArrayList<Object>();
    stockList.add("MSFT");
    stockList.add("GOOGL");
    
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("check stock IN list");
    rule.setExpression("RULEFUNC.IN(pojo.getSymbol(),stockList)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"stockList"});
    operator.setParameterTypes(new Class[]{List.class});
    operator.setArguments(new Object[]{stockList});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testMATCHESPositiveCase(){

    String REGEX = ".*AP.*"; 
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocknames containing AP ");
    rule.setExpression("RULEFUNC.MATCHES(pojo.getSymbol(),REGEX)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"REGEX"});
    operator.setParameterTypes(new Class[]{String.class});
    operator.setArguments(new Object[]{REGEX});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testMATCHESNegativeCase(){

    String REGEX = ".*MS.*"; 
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocknames containing AP ");
    rule.setExpression("RULEFUNC.MATCHES(pojo.getSymbol(),REGEX)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"REGEX"});
    operator.setParameterTypes(new Class[]{String.class});
    operator.setArguments(new Object[]{REGEX});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testCombinedRulePositiveCase(){

    List<Object> stockList = new ArrayList<Object>();
    stockList.add("MSFT");
    stockList.add("APPL");
   
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocks from a given list with opening price as 1 , closing price at 2 ");
    rule.setExpression("( pojo.getoPrice() == 1 ) && "
        + "(pojo.getcPrice() == 2) && "
        + "RULEFUNC.IN(pojo.getSymbol(),stockList)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"stockList"});
    operator.setParameterTypes(new Class[]{List.class});
    operator.setArguments(new Object[]{stockList});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testCombinedRuleNegativeCase(){

    List<Object> stockList = new ArrayList<Object>();
    stockList.add("MSFT");
   
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocks from a given list with opening price as 1 , closing price at 2 ");
    rule.setExpression("( pojo.getoPrice() == 1 ) && "
        + "(pojo.getcPrice() == 2) && "
        + "RULEFUNC.IN(pojo.getSymbol(),stockList)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"stockList"});
    operator.setParameterTypes(new Class[]{List.class});
    operator.setArguments(new Object[]{stockList});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  
  @Test
  public void testVALID_DATEPositiveCase(){

    String  datePattern = "dd/MM/yyyy";
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Check whether date is valid");
    rule.setExpression("RULEFUNC.VALIDATE_DATE(pojo.getDate(),datePattern)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"datePattern"});
    operator.setParameterTypes(new Class[]{String.class});
    operator.setArguments(new Object[]{datePattern});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testVALID_DATENegativeCase(){

    String  datePattern = "dd/MM/yyyy";
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Check whether date is valid");
    rule.setExpression("RULEFUNC.VALIDATE_DATE(pojo.getDate(),datePattern)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "08/24/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"datePattern"});
    operator.setParameterTypes(new Class[]{String.class});
    operator.setArguments(new Object[]{datePattern});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testBETWEENPositiveCase(){

    double upperLimit = 3.0;
    double lowerlimit = 0.0;
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocks within given range");
    rule.setExpression("RULEFUNC.BETWEEN(pojo.getoPrice(),lowerlimit,upperLimit)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"lowerlimit","upperLimit"});
    operator.setParameterTypes(new Class[]{Double.class,Double.class});
    operator.setArguments(new Object[]{lowerlimit,upperLimit});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) validDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, validDataSink.collectedTuples.size());
    Assert.assertEquals(0, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
  
  @Test
  public void testBETWEENNegativeCase(){

    double upperLimit = 10.0;
    double lowerlimit = 5.0;
    DtRule rule = new DtRule();
    rule.setRuleId(1);
    rule.setRuleName("Filter stocks within given range");
    rule.setExpression("RULEFUNC.BETWEEN(pojo.getoPrice(),lowerlimit,upperLimit)");
    
    StockBean stockBean = new StockBean(1,2, "APPL", "24/08/2015");
    
    operator.setRule(rule);
    operator.setParameterNames(new String[]{"lowerlimit","upperLimit"});
    operator.setParameterTypes(new Class[]{Double.class,Double.class});
    operator.setArguments(new Object[]{lowerlimit,upperLimit});
    
    operator.setup(null);
    operator.process(stockBean);
    
    DtRule emittedRule = (DtRule) invalidDataSink.collectedTuples.get(0).getKey();
    
    Assert.assertEquals(1, invalidDataSink.collectedTuples.size());
    Assert.assertEquals(0, validDataSink.collectedTuples.size());
    Assert.assertEquals(emittedRule.getExpression(), rule.getExpression());
  }
}
