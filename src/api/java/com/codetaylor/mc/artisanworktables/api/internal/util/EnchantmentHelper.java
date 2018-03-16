package com.codetaylor.mc.artisanworktables.api.internal.util;

import net.minecraft.entity.player.EntityPlayer;

public class EnchantmentHelper {

  public static int getPlayerExperienceTotal(EntityPlayer player) {

    int experienceFromLevel = EnchantmentHelper.getExperienceFromLevel(player.experienceLevel);
    int experienceBarCapacity = EnchantmentHelper.getExperienceBarCapacity(player.experienceLevel);
    return (int) (experienceFromLevel + (player.experience * experienceBarCapacity));
  }

  public static void adjustPlayerExperience(EntityPlayer player, int amount) {

    int experience = EnchantmentHelper.getPlayerExperienceTotal(player);
    player.experienceTotal = Math.max(0, experience + amount);
    player.experienceLevel = EnchantmentHelper.getLevelFromExperience(player.experienceTotal);
    player.experience = (player.experienceTotal - EnchantmentHelper.getExperienceFromLevel(player.experienceLevel));
    player.experience /= (float) EnchantmentHelper.getExperienceBarCapacity(player.experienceLevel);
  }

  public static int getExperienceBarCapacity(int level) {

    if (level >= 30) {
      return 112 + (level - 30) * 9;
    }

    if (level >= 15) {
      return 37 + (level - 15) * 5;
    }

    return 7 + level * 2;
  }

  private static int sum(int n, int a0, int d) {

    return n * (2 * a0 + (n - 1) * d) / 2;
  }

  public static int getExperienceFromLevel(int level) {

    if (level == 0) {
      return 0;
    }

    if (level <= 15) {
      return EnchantmentHelper.sum(level, 7, 2);
    }

    if (level <= 30) {
      return 315 + EnchantmentHelper.sum(level - 15, 37, 5);
    }

    return 1395 + EnchantmentHelper.sum(level - 30, 112, 9);
  }

  public static int getExperienceToNextLevel(int currentLevel) {

    return EnchantmentHelper.getExperienceToLevel(currentLevel, currentLevel + 1);
  }

  public static int getExperienceToLevel(int startingLevel, int targetLevel) {

    int startingLevelExperience = EnchantmentHelper.getExperienceFromLevel(targetLevel);
    int targetLevelExperience = EnchantmentHelper.getExperienceFromLevel(startingLevel);
    return startingLevelExperience - targetLevelExperience;
  }

  public static int getLevelFromExperience(int experience) {

    int level = 0;

    while (true) {

      int experienceForNextLevel = EnchantmentHelper.getExperienceToNextLevel(level);

      if (experience < experienceForNextLevel) {
        return level;
      }

      level += 1;
      experience -= experienceForNextLevel;
    }
  }

}
