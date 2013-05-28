package org.starstracker.events;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.opencv.core.Point;
import org.starstracker.gui.StartTrackerGUI;

public class StarsTrackerToGuiEventHandler implements IModelToGuiEventHandler
{
   private StartTrackerGUI starsTrackerGui;
   
   public StarsTrackerToGuiEventHandler(StartTrackerGUI gui)
   {
      this.starsTrackerGui = gui;
   }
   
   @Override
   public void onImageChanged(final Image image)
   {
      
      // final BufferedImage img = Utils.convert(imageRef);
      SwingUtilities.invokeLater(new Runnable()
      {
         
         @Override
         public void run()
         {
            starsTrackerGui.getJpanelImage().setImage(image);
            starsTrackerGui.getJpanelImage().repaint();
         }
      });
   }
   
   @Override
   public void onShiftDetected(Point shiftingPoint)
   {
      try
      {
         SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
         String dateStr = sdf.format(Calendar.getInstance().getTime());
         
         starsTrackerGui
                  .getTextPane()
                  .getDocument()
                  .insertString(0, dateStr + "-shift:" + shiftingPoint.toString() + "\n",
                           starsTrackerGui.getTextPane().getStyledDocument().getStyle("regular"));
      }
      catch (BadLocationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }
   
}
