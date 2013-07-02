package org.starstracker.events;

import org.opencv.core.Point;
import org.starstracker.StarsTrackerException;
import org.starstracker.arduino.ArduinoControler;
import org.starstracker.constants.Constants;
import org.starstracker.utils.Utils;

public class ArduinoEventHandler implements IHardwareEventHandler
{
   private ArduinoControler controler;
   
   private double           vitesseDeclinaison;
   private double           sommeErreurs     = 0;
   private double           erreurPrecedente = 0;
   private double           variationErreur  = 0;
   private double           ajustement       = 0;
   
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
         Utils.LOGGER.info("decalage " + "X:" + shiftingPoint.x + "-Y:" + shiftingPoint.y);
         Utils.LOGGER.info("Vitesse actuelle declinaison : " + this.vitesseDeclinaison);
         
         /* Décalage sur ascension droite */
         Utils.LOGGER.info("sending -> SHIFT Y " + shiftingPoint.y);
         this.controler.sendCommande("SHIFT Y " + shiftingPoint.y);
         
         /* Decalage en déclinaison donc ajustement de la vitesse */
         
         /* Calcul PID */
         double erreur = shiftingPoint.x;
         this.sommeErreurs += erreur;
         
         this.variationErreur = erreur - erreurPrecedente;
         
         this.ajustement = (Constants.Kp * erreur) + (Constants.Ki * sommeErreurs)
                  + (Constants.Kd * variationErreur);
         Utils.LOGGER.info("this.ajustement(" + this.ajustement + ") = (Constants.Kp("
                  + Constants.Kp + ") * erreur(" + erreur + ")) + (Constants.Ki(" + Constants.Ki
                  + ") * sommeErreurs(" + sommeErreurs + ")) + (Constants.Kd(" + Constants.Kd
                  + ") * variationErreur(" + variationErreur + "))");
         
         erreurPrecedente = erreur;
         
         this.vitesseDeclinaison += ajustement;
         
         long newSpeed = Math.round(this.vitesseDeclinaison);
         Utils.LOGGER.info("sending -> SPEED D " + newSpeed);
         this.controler.sendCommande("SPEED D " + newSpeed);
         
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
      
      Utils.LOGGER.info("MOVE " + axe + " " + deplacement);
      this.controler.sendCommande("MOVE " + axe + " " + deplacement);
      
   }
   
   public void setSpeed(String axe, double speed) throws StarsTrackerException
   {
      Utils.LOGGER.info("SPEED " + axe + " " + speed);
      this.controler.sendCommande("SPEED " + axe + " " + speed);
   }
   
   public void setCoefPixelStep(String axe, double coef) throws StarsTrackerException
   {
      Utils.LOGGER.info("coefPixelStep " + axe + " " + coef);
      // this.controler.sendCommande("4,coefPixelStep" + axe + ":" + coef +
      // ";\n");
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
