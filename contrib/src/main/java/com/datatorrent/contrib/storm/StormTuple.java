package com.datatorrent.contrib.storm;

import java.io.Serializable;
import java.util.List;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.MessageId;
import backtype.storm.tuple.Values;

public class StormTuple implements backtype.storm.tuple.Tuple, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Values stormTuple;

  public StormTuple()
  {
    this.stormTuple = null;
  }

  public StormTuple(final Values tuple)
  {
    this.stormTuple = tuple;
  }

  @Override
  public GlobalStreamId getSourceGlobalStreamid()
  {
    return new GlobalStreamId();
  }

  @Override
  public String getSourceComponent()
  {
    return null;
  }

  @Override
  public int getSourceTask()
  {
    return 0;
  }

  @Override
  public String getSourceStreamId()
  {
    return null;
  }

  @Override
  public MessageId getMessageId()
  {
    return null;
  }

  @Override
  public int size()
  {
    return 0;
  }

  @Override
  public boolean contains(String s)
  {
    return false;
  }

  @Override
  public Fields getFields()
  {
    return null;
  }

  @Override
  public int fieldIndex(String s)
  {
    return 0;
  }

  @Override
  public List<Object> select(Fields fields)
  {
    return null;
  }

  @Override
  public Object getValue(int i)
  {
    return this.stormTuple;
  }

  @Override
  public String getString(int i)
  {
    return (String)this.stormTuple.get(i);
  }

  @Override
  public Integer getInteger(final int i)
  {
    return (Integer)this.stormTuple.get(i);
  }

  @Override
  public Long getLong(final int i)
  {
    return (Long)this.stormTuple.get(i);
  }

  @Override
  public Boolean getBoolean(final int i)
  {
    return (Boolean)this.stormTuple.get(i);
  }

  @Override
  public Short getShort(final int i)
  {
    return (Short)this.stormTuple.get(i);
  }

  @Override
  public Byte getByte(final int i)
  {
    return (Byte)this.stormTuple.get(i);
  }

  @Override
  public Double getDouble(final int i)
  {
    return (Double)this.stormTuple.get(i);
  }

  @Override
  public Float getFloat(final int i)
  {
    return (Float)this.stormTuple.get(i);
  }

  @Override
  public byte[] getBinary(final int i)
  {
    return (byte[])this.stormTuple.get(i);
  }

  @Override
  public Object getValueByField(String s)
  {
    return null;
  }

  @Override
  public String getStringByField(String s)
  {
    return null;
  }

  @Override
  public Integer getIntegerByField(String s)
  {
    return null;
  }

  @Override
  public Long getLongByField(String s)
  {
    return null;
  }

  @Override
  public Boolean getBooleanByField(String s)
  {
    return null;
  }

  @Override
  public Short getShortByField(String s)
  {
    return null;
  }

  @Override
  public Byte getByteByField(String s)
  {
    return null;
  }

  @Override
  public Double getDoubleByField(String s)
  {
    return null;
  }

  @Override
  public Float getFloatByField(String s)
  {
    return null;
  }

  @Override
  public byte[] getBinaryByField(String s)
  {
    return new byte[0];
  }

  @Override
  public List<Object> getValues()
  {
    return this.stormTuple;
  }
}
