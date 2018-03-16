package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class WeightedPicker<T> {

  private NavigableMap<Integer, ResultWrapper<T>> map;
  private int total = 0;
  private Random random;

  private static class ResultWrapper<T> {

    int weight;
    T result;

    ResultWrapper(int weight, T result) {

      this.weight = weight;
      this.result = result;
    }
  }

  public WeightedPicker() {

    this(new Random());
  }

  public WeightedPicker(Random random) {

    this.random = random;
    this.map = new TreeMap();
  }

  public void add(int weight, T result) {

    if (weight <= 0) {
      return;
    }
    this.total += weight;
    this.map.put(this.total, new ResultWrapper<>(weight, result));
  }

  public T get() {

    int index = this.random.nextInt(this.total) + 1;
    return this.map.ceilingEntry(index).getValue().result;
  }

  public T getAndRemove() {

    T result = this.get();
    this.remove(result);
    return result;
  }

  public int getTotal() {

    return this.total;
  }

  public int getSize() {

    return this.map.size();
  }

  private void remove(T value) {

    NavigableMap<Integer, ResultWrapper<T>> oldMap = this.map;
    this.map = new TreeMap<>();
    this.total = 0;

    for (ResultWrapper<T> resultWrapper : oldMap.values()) {

      if (resultWrapper.result != value) {
        this.total += resultWrapper.weight;
        this.map.put(this.total, resultWrapper);
      }
    }
  }

  @Override
  public String toString() {

    return "WeightedPicker [map=" + map + ", total=" + total + "]";
  }

}
