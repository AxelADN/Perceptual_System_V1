/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;


/**
 *
 * @author Laptop
 */
public class NArrayObject<T> extends NArray<T>
{
  public NArrayObject(int ... dimensions)
  {
    super(dimensions);
    this.data = new Object[super.size()];
  }

  @SuppressWarnings("unchecked")
  @Override
  public final T get(int idx)
  {
    return (T)data[idx];
  }

  @Override
  public final void set(int idx,T value)
  {
    data[idx] = value;
  }

  public final Object[] data;
}
