package org.starstracker.events;

import org.opencv.core.Point;
import org.starstracker.StarsTrackerException;
import org.starstracker.arduino.ArduinoControler;
import org.starstracker.utils.Utils;

public class ArduinoEventHandler implements IHardwareEventHandler
{
   private ArduinoControler controler;
   
   public ArduinoEventHandler() throws StarsTrackerException
   {
      this.controler = new ArduinoControler();
      this.controler.connect("/dev/ttyACM0");
   }
   
   @Override
   public void onShiftDetected(Point shiftingPoint)
   {
      try
      {
         Utils.LOGGER.info("Envoi du decalage " + "X:" + shiftingPoint.x + "-Y:" + shiftingPoint.y);
         this.move("X", shiftingPoint.x);
         this.move("Y", shiftingPoint.y);
      }
      catch (StarsTrackerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   @Override
   public void onNoDetection()
   {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void move(String axe, double deplacement) throws StarsTrackerException
   {
      
      Utils.LOGGER.info("4," + axe + ":" + deplacement + ";");
      this.controler.sendCommande("4," + axe + ":" + deplacement + ";\n");
      
   }

   public void setSpeed(String axe, double speed) throws StarsTrackerException
   {
      Utils.LOGGER.info("4,speed" + axe + ":" + speed + ";");
      this.controler.sendCommande("4,speed" + axe + ":" + speed + ";\n"); 
   }

   public void setCoefPixelStep(String axe, double coef) throws StarsTrackerException
   {
      Utils.LOGGER.info("4,coefPixelStep" + axe + ":" + coef + ";");
      this.controler.sendCommande("4,coefPixelStep" + axe + ":" + coef + ";\n"); 
   }
   
   @Override
   public void calibrate()
   {
      // TODO Auto-generated method stub
      
   }
   
   public void release()
   {
      this.controler.disconnect();
   }
   
}
