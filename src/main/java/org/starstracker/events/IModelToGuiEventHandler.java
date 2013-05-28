package org.starstracker.events;

import java.awt.Image;

import org.opencv.core.Point;

public interface IModelToGuiEventHandler
{
   public void onImageChanged(final Image image);
   public void onShiftDetected(final Point shiftingPoint);
   
}
