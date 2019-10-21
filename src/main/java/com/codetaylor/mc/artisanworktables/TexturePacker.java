package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.athenaeum.packer.GsonPackDataSupplier;
import com.codetaylor.mc.athenaeum.packer.ImageCollector;
import com.codetaylor.mc.athenaeum.packer.PackDataPathResolver;
import com.codetaylor.mc.athenaeum.packer.Packer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TexturePacker {

  public static void main(String[] args) throws IOException {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String pathname = "assets/atlas/pack.json";

    Packer packer = new Packer(
        gson,
        new GsonPackDataSupplier(gson, new FileReader(new File(pathname))),
        new PackDataPathResolver(),
        new ImageCollector(gson)
    );

    packer.run();
  }
}
