package org.starstracker.events;

import org.starstracker.StarsTracker;
import org.starstracker.StarsTrackerException;
import org.starstracker.utils.Utils;

public class GuiToStarsTrackerEventHandler implements IGuiToModelEventHandler
{
   private StarsTracker        app;
   private ArduinoEventHandler arduinoEvent = null;
   
   public GuiToStarsTrackerEventHandler(StarsTracker app)
   {
      this.app = app;
      try
      {
         this.arduinoEvent = new ArduinoEventHandler();
      }
      catch (StarsTrackerException ste)
      {
         Utils.LOGGER.warn("Impossible d'établir une liaison avec le controlleur Hardware");
         ste.printStackTrace();
      }
   }
   
   @Override
   public void onStartArduinoSteppers(double speedX, double speedY, double coefStepPixelX,
            double coefStepPixelY)
   {
      try
      {
         this.arduinoEvent.setSpeed("X", speedX);
         this.arduinoEvent.setSpeed("Y", speedY);
         this.arduinoEvent.setCoefPixelStep("X", coefStepPixelX);
         this.arduinoEvent.setCoefPixelStep("Y", coefStepPixelY);
         this.app.setHardwareEventHandler(this.arduinoEvent);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }
   
   @Override
   public void onStopArduinoSteppers()
   {
      // TODO Auto-generated method stub
      this.app.setHardwareEventHandler(null);
   }
   
   @Override
   public void onManualXPlus(int val)
   {
      try
      {
         this.arduinoEvent.move("X", val);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onManualXMinus(int val)
   {
      try
      {
         this.arduinoEvent.move("X", -val);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onManualYPlus(int val)
   {
      try
      {
         this.arduinoEvent.move("Y", val);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onManualYMinus(int val)
   {
      try
      {
         this.arduinoEvent.move("Y", -val);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onMaxCornersChange(int val)
   {
      this.app.setMaxCorners(val);
   }
   
   @Override
   public void onQualitLevelChange(double val)
   {
      this.app.setQualityLevel(val);
   }
   
   @Override
   public void onMinDistanceChange(int val)
   {
      this.app.setMinDistanceBetweenCorners(val);
   }
   
   @Override
   public void onNumWebcamChange(int val)
   {
      this.app.setWebcamNumber(val);
      try
      {
         this.app.initWebCam();
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onStepperSpeedXChange(int parseInt)
   {
      try
      {
         this.arduinoEvent.setSpeed("X", parseInt);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onStepperSpeedYChange(int parseInt)
   {
      try
      {
         this.arduinoEvent.setSpeed("Y", parseInt);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onCoefPixelSpeedXChange(int parseInt)
   {
      try
      {
         this.arduinoEvent.setCoefPixelStep("X", parseInt);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onCoefPixelSpeedYChange(int parseInt)
   {
      try
      {
         this.arduinoEvent.setCoefPixelStep("Y", parseInt);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Override
   public void onMinPixelSensitivityChange(int parseInt)
   {
      this.app.setMinPixelSensitivity(parseInt);
   }

   @Override
   public void onMaxPixelSensitivityChange(int parseInt)
   {
      this.app.setMaxPixelSensitivity(parseInt);
   }
   
}
