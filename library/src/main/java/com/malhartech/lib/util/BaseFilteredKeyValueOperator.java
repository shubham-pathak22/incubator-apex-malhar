/*
 *  Copyright (c) 2012 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.lib.util;

import java.util.HashMap;

/**
 * Base class for operators that enables filtering of of keys to be processed<p>
 * By default no filtering would be done as inverse is set to true and filterBy would be empty unless set<br>
 * <br>
 * <b>Ports</b>: None<br>
 * <br>
 * <b>Properties</b>:<br>
 * <b>inverse</b>: if set to true the key in the filter will block tuple. Default is true<br>
 * <b>filterBy</b>: List of keys to filter on. Default is an empty filter<br>
 * <br>
 * <b>Specific compile time checks</b>: None<br>
 * <b>Specific run time checks</b>: None<br>
 * <br>
 * <b>Benchmarks</b>: Not done as there are no ports on this operator<br>
 * <br>
 * @author Amol Kekre (amol@malhar-inc.com)<br>
 * <br>
 *
 */
public class BaseFilteredKeyValueOperator<K, V> extends BaseKeyValueOperator<K, V>
{
  private HashMap<K, Object> filterBy = new HashMap<K, Object>(4);
  private boolean inverse = true;

  /**
   * getter function for inverse
   * @return the value of inverse
   */
  public boolean getInverse()
  {
    return inverse;
  }

  /**
   * Setter function for inverse. The filter is a negative filter is inverse is set to true
   * @param i value of inverse
   */
  public void setInverse(boolean i)
  {
    inverse = i;
  }

  /**
   * setter function for filterBy
   * @param list list of keys for subtoken filters
   */
  public void setFilterBy(K [] list)
  {
    if (list != null) {
      for (K s: list) {
        filterBy.put(s, null);
      }
    }
  }

  public boolean doprocessKey(K key)
  {
    boolean fcontains = filterBy.containsKey(key);
    return (!inverse && fcontains) || (inverse && !fcontains);
  }
}
