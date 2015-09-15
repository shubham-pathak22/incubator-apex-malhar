package com.datatorrent.contrib.rule;

import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.annotation.InputPortFieldAnnotation;
import com.datatorrent.api.annotation.OutputPortFieldAnnotation;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.lib.util.KeyValPair;

public abstract class RuleOperator extends BaseOperator
{

  protected DtRule rule;
  
  @InputPortFieldAnnotation
  public final transient DefaultInputPort<Object> in = new DefaultInputPort<Object>()
  {
    @Override
    public void process(Object tuple)
    {
      RuleOperator.this.process(tuple);
    }
  };
  
  @OutputPortFieldAnnotation
  public final transient DefaultOutputPort<KeyValPair<DtRule, Object>> outPassed = new DefaultOutputPort<KeyValPair<DtRule, Object>>();
  
  @OutputPortFieldAnnotation(optional = true)
  public final transient DefaultOutputPort<KeyValPair<DtRule, Object>> outFailed = new DefaultOutputPort<KeyValPair<DtRule, Object>>();
 
  public DtRule getRule()
  {
    return rule;
  }

  public void setRule(DtRule rule)
  {
    this.rule = rule;
  }
  
  public abstract void process(Object tuple);
  
}
