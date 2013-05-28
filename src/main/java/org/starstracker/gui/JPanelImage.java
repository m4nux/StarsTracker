package org.starstracker.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class JPanelImage extends JPanel
{
   
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Image image;
   
   public JPanelImage(Image image)
   {
      super();
      this.image = image;
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paintComponent(g);
      if (image != null)
         g.drawImage(image, 0, 0, this);
      
   }
   
   public void setImage(Image convert)
   {
      this.image = convert;
      
   }
   
}