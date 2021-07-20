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
public abstract class NArray<T>
{
  protected NArray(int[] dimensions)
  {
    this.strides = new int[dimensions.length];
    this.dimensions = new int[dimensions.length];

    this.strides[0] = 1;
    this.dimensions[0] = dimensions[0];
    int s = dimensions[0];
    int ns = 3;
    for(int i=1;i<dimensions.length;++i) {
      this.strides[i] = this.strides[i - 1] * dimensions[i - 1];
      this.dimensions[i] = dimensions[i];
      s *= dimensions[i];
      ns *= 3;
    }
    this.size = s;
    this.neighborhoodSize = ns;
  }

  /**
   * @return Number of dimensions (shortcut to dimensions().length)
   */
  public final int N()
  {
    return dimensions.length;
  }

  /**
   * @return Dimensions of array (do not modify returned array)
   */
  public final int[] dimensions()
  {
    return this.dimensions;
  }

  /**
   * @return Size of a Moore neighborhood in this array (3^N)
   */
  public final int neighborhoodSize()
  {
    return this.neighborhoodSize;
  }

  /**
   * @param coords Coordinates to look up
   * @return Index in raw underlying array corresponding to coordinates
   */
  public final int indexOf(int... coords)
  {
    int idx = coords[0];
    for(int i=1;i<coords.length;++i)
      idx += coords[i] * strides[i];
    return idx;
  }

  /**
   * @return Raw size of array in elements
   */
  public final int size()
  {
    return this.size;
  }

  /**
   * @param idx Index of element in raw underlying array
   * @return Element value
   */
  public abstract T get(int idx);

  /**
   * @param idx Index of element in raw underlying array
   * @param value Element value to set
   */
  public abstract void set(int idx,T value);

  /**
   * @param coords Coordinates of element in n-dimensional array space
   * @return Element value
   */
  public final T get(int... coords)
  {
    return get(indexOf(coords));
  }

  /**
   * @param coords Coordinates of element in n-dimensional array space
   * @param value Element value to set
   */
  public final void set(T value, int ... coords)
  {
    set(indexOf(coords),value);
  }

  /**
   * Fill every element with the specified value
   * 
   * @param value Value to fill
   */
  public final void fill(T value)
  {
    for(int i=0;i<this.size;++i)
      set(i,value);
  }

  private final int[] strides;
  private final int[] dimensions;
  private final int size;
  private final int neighborhoodSize;
}