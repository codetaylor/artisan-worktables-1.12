package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputReplacements {

  public static InputReplacements NO_OP = new InputReplacements() {

    @Override
    public void add(IIngredient toReplace, IIngredient replacement) {

      throw new UnsupportedOperationException();
    }
  };

  private List<InputReplacementEntry> inputReplacementEntryList;

  private int width;
  private int height;

  private Map<GridPosition, IIngredient> replacementMap;

  public InputReplacements() {

    this.inputReplacementEntryList = new ArrayList<>(9);
    this.replacementMap = new HashMap<>(9);
  }

  public void add(@Nullable IIngredient toReplace, @Nullable IIngredient replacement) {

    this.inputReplacementEntryList.add(new InputReplacementEntry(toReplace, replacement));
  }

  public void add(int col, int row, IIngredient replacement) {

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

  public Ingredient replace(@Nullable Ingredient ingredient) {

    for (InputReplacementEntry entry : this.inputReplacementEntryList) {

      if (entry.matches(ingredient)) {
        ingredient = CTInputHelper.toIngredient(entry.getReplacement());
        break;
      }
    }

    return ingredient;
  }

  public Ingredient replace(int col, int row, @Nullable Ingredient ingredient) {

    GridPosition gridPosition = new GridPosition(col, row);

    if (this.replacementMap.containsKey(gridPosition)) {
      IIngredient replacement = this.replacementMap.get(gridPosition);

      if (replacement != null) {
        return CTInputHelper.toIngredient(replacement);
      }

      return Ingredient.EMPTY;
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
