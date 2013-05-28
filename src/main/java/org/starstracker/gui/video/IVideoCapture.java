package org.starstracker.gui.video;

import org.opencv.core.Mat;

public interface IVideoCapture
{
   public boolean open(int camNumber);
   public boolean read(Mat image);
   public boolean close();
}
