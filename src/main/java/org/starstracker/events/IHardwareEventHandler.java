package org.starstracker.events;

import org.opencv.core.Point;
import org.starstracker.StarsTrackerException;

public interface IHardwareEventHandler
{
   public void calibrate();
   public void move(String axe, double deplacement) throws StarsTrackerException;
   public void onShiftDetected(Point shiftingPoint);
   public void onNoDetection();
}
