/*
 *  Copyright (c) 2012 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.lib.logs;

import com.malhartech.annotation.InjectConfig;
import com.malhartech.annotation.InputPortFieldAnnotation;
import com.malhartech.annotation.OutputPortFieldAnnotation;
import com.malhartech.api.BaseOperator;
import com.malhartech.api.DefaultInputPort;
import com.malhartech.api.DefaultOutputPort;

/**
 *
 * Base class for line split operators. Takes in one stream via input port "data". Lines are split into tokens and tokens are processed<p>
 * processToken, and processSubToken are called for each token. Users should override calls backs to intercept at any level.<br>
 * This module is a pass through<br>
 * <br>
 * Ideal for applications like word count
 * Ports:<br>
 * <b>data</b>: expects String<br>
 *
 * @author amol<br>
 *
 */
public class BaseLineTokenizer extends BaseOperator
{
  @InputPortFieldAnnotation(name = "data")
  public final transient DefaultInputPort<String> data = new DefaultInputPort<String>(this)
  {
    @Override
    public void process(String tuple)
    {
      if (!validTuple(tuple)) { // emit error token?
        return;
      }
      beginProcessTokens();
      processTokens(tuple.split(splitBy));
      endProcessTokens();
    }
  };
  @InjectConfig(key = "splitby")
  String splitBy = ";\t";
  @InjectConfig(key = "splittokenby")
  String splitTokenBy = "";

  public String getSplitBy()
  {
    return splitBy;
  }

  public String getSplitTokenBy()
  {
    return splitTokenBy;
  }

  public void setSplitBy(String str)
  {
    splitBy = str;
  }

  public void setSplitTokenBy(String str)
  {
    splitTokenBy = str;
  }

  public void beginProcessTokens()
  {
  }

  public void endProcessTokens()
  {
  }

  public void beginProcessSubTokens()
  {
  }

  public void endProcessSubTokens()
  {
  }

  public void processTokens(String[] tokens)
  {
    if (tokens == null) {
      return;
    }
    for (String tok: tokens) {
      if (validToken(tok)) {
        processToken(tok);
      }
    }
  }

  public void processToken(String tok)
  {
    if (tok.isEmpty()) {
      return;
    }
    beginProcessSubTokens();
    if (splitTokenBy.isEmpty()) {
        processSubToken(tok);
    }
    else {
      String[] subtoks = tok.split(splitTokenBy);
      int i = 0;
      for (String subtok: subtoks) {
        if ((i ==0) && !validSubTokenKey(subtok)) { // first subtoken is the key
          break;
        }
        processSubToken(subtok);
        i++;
      }
    }
    endProcessSubTokens();
  }

  public void processSubToken(String subtok)
  {
  }

  public boolean validTuple(String tuple)
  {
    return !tuple.isEmpty();
  }

  public boolean validToken(String tok)
  {
    return !tok.isEmpty();
  }

  public boolean validSubTokenKey(String subtok)
  {
    return !subtok.isEmpty();
  }
}
