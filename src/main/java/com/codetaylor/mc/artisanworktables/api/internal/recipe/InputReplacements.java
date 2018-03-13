package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputReplacements {

  public static InputReplacements NO_OP = new InputReplacements() {

    @Override
    public void add(IArtisanIngredient toReplace, IArtisanIngredient replacement) {

      throw new UnsupportedOperationException();
    }
  };

  private List<InputReplacementEntry> inputReplacementEntryList;

  private int width;
  private int height;

  private Map<GridPosition, IArtisanIngredient> replacementMap;

  public InputReplacements() {

    this.inputReplacementEntryList = new ArrayList<>(9);
    this.replacementMap = new HashMap<>(9);
  }

  public void add(@Nullable IArtisanIngredient toReplace, @Nullable IArtisanIngredient replacement) {

    this.inputReplacementEntryList.add(new InputReplacementEntry(toReplace, replacement));
  }

  public void add(int col, int row, IArtisanIngredient replacement) {

    this.width = Math.max(this.width, col + 1);
    this.height = Math.max(this.height, row + 1);

    this.replacementMap.put(new GridPosition(col, row), replacement);
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }

  public IArtisanIngredient replace(@Nullable IArtisanIngredient ingredient) {

    for (InputReplacementEntry entry : this.inputReplacementEntryList) {

      if (entry.matches(ingredient)) {
        ingredient = entry.getReplacement();
        break;
      }
    }

    return ingredient;
  }

  public IArtisanIngredient replace(int col, int row, @Nullable IArtisanIngredient ingredient) {

    GridPosition gridPosition = new GridPosition(col, row);

    if (this.replacementMap.containsKey(gridPosition)) {
      IArtisanIngredient replacement = this.replacementMap.get(gridPosition);

      if (replacement != null) {
        return replacement;
      }

      return ArtisanIngredient.EMPTY;
    }

    return this.replace(ingredient);
  }

  private static class GridPosition {

    final int col;
    final int row;

    public GridPosition(int col, int row) {

      this.col = col;
      this.row = row;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      GridPosition that = (GridPosition) o;

      if (col != that.col) {
        return false;
      }
      return row == that.row;
    }

    @Override
    public int hashCode() {

      int result = col;
      result = 31 * result + row;
      return result;
    }
  }
}
