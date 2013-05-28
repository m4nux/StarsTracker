package org.starstracker.events;


public interface IGuiToModelEventHandler
{
   public void onStartArduinoSteppers(double speedX, double speedY, double coefStepPixelX,
            double coefStepPixelY);
   public void onStopArduinoSteppers();
   public void onManualXPlus(int val);
   public void onManualXMinus(int val);
   public void onManualYPlus(int val);
   public void onManualYMinus(int val);

   public void onMaxCornersChange(int val);
   public void onQualitLevelChange(double val);
   public void onMinDistanceChange(int val);
   public void onNumWebcamChange(int val);
   public void onStepperSpeedXChange(int parseInt);
   public void onStepperSpeedYChange(int parseInt);
   public void onCoefPixelSpeedXChange(int parseInt);
   public void onCoefPixelSpeedYChange(int parseInt);

   
}
