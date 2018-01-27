package org.starstracker;

import org.opencv.core.Core;
import org.starstracker.events.GuiToStarsTrackerEventHandler;
import org.starstracker.events.StarsTrackerToGuiEventHandler;
import org.starstracker.gui.StartTrackerGUI;
import org.starstracker.gui.video.AviVideoCapture;
import org.starstracker.gui.video.OpenCvVideoCapture;

class Main
{
   private StartTrackerGUI starsTrackerGui;
   private StarsTracker    app = new StarsTracker();

   /**
    * Ajout javadoc1
    * @throws StarsTrackerException
    */
   public void track() throws StarsTrackerException
   {
      //this.app.setVideoCapture(new OpenCvVideoCapture());
      this.app.setVideoCapture(new AviVideoCapture("/home/manux/monte.avi"));
      this.app.setModelToGuiEventHandler(new StarsTrackerToGuiEventHandler(this.starsTrackerGui));
      //this.app.start();
      this.app.track();
   }
   
   /**
    * Create the GUI and show it. For thread safety, this method should be
    * invoked from the event dispatch thread.
    * 
    * @throws StarsTrackerException
    */
   private void createAndShowGUI() throws StarsTrackerException
   {
      this.starsTrackerGui = new StartTrackerGUI();
      // Create and set up the window.
      this.starsTrackerGui.setGuiToModelEventHandler(new GuiToStarsTrackerEventHandler(this.app));
      this.starsTrackerGui.show();
      
   }
   
   public static void main(String[] args)
   {
      
      // Load the native library.
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      Main star = new Main();
      try
      {
         star.createAndShowGUI();
         star.track();
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}