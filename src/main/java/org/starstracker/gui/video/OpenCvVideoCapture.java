package org.starstracker.gui.video;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class OpenCvVideoCapture implements IVideoCapture
{
   private VideoCapture    webcam;
   
   public OpenCvVideoCapture()
   {
      this.webcam = new VideoCapture();
   }
   
   @Override
   public boolean open(int camNumber)
   {
      // TODO Auto-generated method stub
      return this.webcam.open(camNumber);
   }
   
   @Override
   public boolean read(Mat image)
   {
      // TODO Auto-generated method stub
      return this.webcam.read(image);
   }

   @Override
   public boolean close()
   {
      webcam.release();
      return !webcam.isOpened(); 
   }
   
}
