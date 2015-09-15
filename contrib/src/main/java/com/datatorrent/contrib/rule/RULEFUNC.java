package com.datatorrent.contrib.rule;

import java.text.SimpleDateFormat;
import java.util.List;

public class RULEFUNC
{
  
  public static boolean IN(Object obj,List<Object> list){
    return list == null ? false : list.contains(obj);
  }
  
  public static boolean MATCHES(String str,String regex){
    return str == null ? false : str.matches(regex);
  }
  
  public static boolean VALIDATE_DATE(String date,String pattern){
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setLenient(false);
    try {
        System.out.println(sdf.parse(date));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
   
  public static boolean BETWEEN (Object value,Object leftValue,Object rightValue){
    if (value == null) return false;
    if (((Comparable)value).compareTo((Comparable)leftValue) <= 0) return false;
    if (((Comparable)value).compareTo((Comparable)rightValue) >= 0) return false;
    return true;
  }
  
  public static boolean STR_LENGTH (String str,int length){
    return ( str == null ) ? false : ( str.length() == length );
  }
  
}
