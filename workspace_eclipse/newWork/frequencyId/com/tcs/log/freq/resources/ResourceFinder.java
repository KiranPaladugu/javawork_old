package com.tcs.log.freq.resources;

import com.logService.Logger;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

public class ResourceFinder
{
  public static URL getResource()
  {
    return getResource();
  }
  public static URL getResource(String name) {
    try {
      return ResourceFinder.class.getResource(name);
    } catch (Exception e) {
      Logger.log("Unable to Load resource:" + name);
    }return null;
  }

  public static File getResourceFile(String name) {
    return new File(ResourceFinder.class.getResource(name).getFile());
  }

  public static ImageIcon getImage(String path) {
    ImageIcon icon = null;
    try {
      icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
    } catch (Exception e) {
      icon = new ImageIcon();
      Logger.log("Cannot load Image :" + path);
    }
    return icon;
  }

  public static ImageIcon getImageWithName(String name)
  {
    ImageIcon icon = null;
    try {
      URL imgurl = getResource(name);
      icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(imgurl));
    } catch (Exception e) {
      icon = new ImageIcon();
      Logger.log("Cannot load Image :" + name);
    }
    return icon;
  }
  public static InputStream getResouceAsAStream(String name) {
    return ResourceFinder.class.getResourceAsStream(name);
  }
}