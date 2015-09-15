package com.datatorrent.contrib.rule;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;

import com.datatorrent.api.Context.OperatorContext;
import com.datatorrent.lib.util.KeyValPair;

public class JaninoRuleOperator extends RuleOperator
{

  protected String[] parameterNames;
  protected Class<?>[] parameterTypes;
  protected Object[] arguments;
  protected Class pojoClass;
  protected transient ExpressionEvaluator expressionEvaluator;
  
  @Override
  public void setup(OperatorContext context)
  {
    super.setup(context);
    try {
      expressionEvaluator = new ExpressionEvaluator();
      expressionEvaluator.setExpressionType(Boolean.class);
      expressionEvaluator.setDefaultImports(new String[] {
          "com.datatorrent.contrib.rule.RULEFUNC"
      });
      parameterNames = ArrayUtils.add(parameterNames, "pojo");
      parameterTypes = ArrayUtils.add(parameterTypes, pojoClass);
      expressionEvaluator.setParameters(parameterNames, parameterTypes);
      expressionEvaluator.cook(rule.getExpression());
    } catch (CompileException e) {
      throw new RuntimeException("Janino CompileException " + e);
    }
  }
  
  @Override
  public void process(Object pojo)
  {
    try {
      if( (boolean) expressionEvaluator.evaluate(ArrayUtils.addAll(arguments, pojo))){
        outPassed.emit(new KeyValPair<>(rule, pojo));
      }else{
        outFailed.emit(new KeyValPair<>(rule, pojo));
      }
    } catch (InvocationTargetException e) {
      throw new RuntimeException("InvocationTargetException in process method " + e);
    }
  }
  
  public String[] getParameterNames()
  {
    return parameterNames;
  }
  public void setParameterNames(String[] parameterNames)
  {
    this.parameterNames = parameterNames;
  }
  public Class<?>[] getParameterTypes()
  {
    return parameterTypes;
  }
  public void setParameterTypes(Class<?>[] parameterTypes)
  {
    this.parameterTypes = parameterTypes;
  }
  public Object[] getArguments()
  {
    return arguments;
  }
  public void setArguments(Object[] arguments)
  {
    this.arguments = arguments;
  }

  public Class getPojoClass()
  {
    return pojoClass;
  }

  public void setPojoClass(Class pojoClass)
  {
    this.pojoClass = pojoClass;
  }

}
