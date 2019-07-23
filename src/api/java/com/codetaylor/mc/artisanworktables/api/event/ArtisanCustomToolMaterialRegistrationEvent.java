package com.codetaylor.mc.artisanworktables.api.event;

import com.codetaylor.mc.artisanworktables.api.tool.CustomToolMaterialRegistrationEntry;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class ArtisanCustomToolMaterialRegistrationEvent
    extends Event {

  private List<CustomToolMaterialRegistrationEntry> materialList = new ArrayList<>();

  public List<CustomToolMaterialRegistrationEntry> getMaterialList() {

    return this.materialList;
  }

  @Override
  public boolean isCancelable() {

    return false;
  }
}
