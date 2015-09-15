package com.datatorrent.contrib.rule;

public class DtRule
{
  
  private int ruleId;
  private String ruleName;
  private String expression;
  
  public DtRule()
  {
    // TODO Auto-generated constructor stub
  }
  
  public DtRule(int ruleId, String ruleName, String expression)
  {
    super();
    this.ruleId = ruleId;
    this.ruleName = ruleName;
    this.expression = expression;
  }

  public String getRuleName()
  {
    return ruleName;
  }
  public void setRuleName(String ruleName)
  {
    this.ruleName = ruleName;
  }
  public int getRuleId()
  {
    return ruleId;
  }
  public void setRuleId(int ruleId)
  {
    this.ruleId = ruleId;
  }
  public String getExpression()
  {
    return expression;
  }
  public void setExpression(String expression)
  {
    this.expression = expression;
  }
  @Override
  public String toString()
  {
    return "DtRule [ruleId=" + ruleId + ", ruleName=" + ruleName + ", expression=" + expression + "]";
  }
}