package org.starstracker;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.starstracker.constants.Constants;
import org.starstracker.events.IModelToGuiEventHandler;
import org.starstracker.events.IHardwareEventHandler;
import org.starstracker.gui.video.IVideoCapture;
import org.starstracker.utils.Utils;

public class StarsTracker
{
   private int                     maxCorners                = 5;
   private double                  minDistanceBetweenCorners = 70;
   private double                  qualityLevel              = 0.1;
   private int                     webcamNumber              = 0;
   
   private IVideoCapture           videoCapture;
   private IHardwareEventHandler   hardwareEventHandler;
   private IModelToGuiEventHandler modelToGuiEventHandler;
   
   private int                     minPixelSensitivity = 2;
   private int                     maxPixelSensitivity = 5;
   
   public StarsTracker()
   {
   }
   
   public IVideoCapture getVideoCapture()
   {
      return videoCapture;
   }
   
   public int getMaxCorners()
   {
      return maxCorners;
   }
   
   public void setMaxCorners(int maxCorners)
   {
      this.maxCorners = maxCorners;
   }
   
   public double getMinDistanceBetweenCorners()
   {
      return minDistanceBetweenCorners;
   }
   
   public void setMinDistanceBetweenCorners(int minDistanceBetweenCorners)
   {
      this.minDistanceBetweenCorners = minDistanceBetweenCorners;
   }
   
   public double getQualityLevel()
   {
      return qualityLevel;
   }
   
   public void setQualityLevel(double qualityLevel)
   {
      this.qualityLevel = qualityLevel;
   }
   
   public int getWebcamNumber()
   {
      return webcamNumber;
   }
   
   public void setWebcamNumber(int webcamNumber)
   {
      this.webcamNumber = webcamNumber;
   }
   
   public void setVideoCapture(IVideoCapture videoCapture)
   {
      this.videoCapture = videoCapture;
   }
   
   public IHardwareEventHandler getHardwareEventHandler()
   {
      return hardwareEventHandler;
   }
   
   public void setHardwareEventHandler(IHardwareEventHandler hardwareEventHandler)
   {
      this.hardwareEventHandler = hardwareEventHandler;
   }
   
   public IModelToGuiEventHandler getModelToGuiEventHandler()
   {
      return modelToGuiEventHandler;
   }
   
   public void setModelToGuiEventHandler(IModelToGuiEventHandler modelToGuiEventHandler)
   {
      this.modelToGuiEventHandler = modelToGuiEventHandler;
   }
   
   public int getMinPixelSensitivity()
   {
      return minPixelSensitivity;
   }
   
   public void setMinPixelSensitivity(int pixelSensitivity)
   {
      this.minPixelSensitivity = pixelSensitivity;
   }
   
   public int getMaxPixelSensitivity()
   {
      return maxPixelSensitivity;
   }
   
   public void setMaxPixelSensitivity(int maxPixelSensitivity)
   {
      this.maxPixelSensitivity = maxPixelSensitivity;
   }
   
   public void initWebCam() throws StarsTrackerException
   {
      if (!this.videoCapture.open(this.webcamNumber))
      {
         throw new StarsTrackerException("Impossible d'initialiser la webcam");
      }
      else
      {
         System.out.println("open webcam " + this.webcamNumber + " succeed");
      }
   }
   
   public void releaseWebCam() throws StarsTrackerException
   {
      if (!this.videoCapture.close())
      {
         throw new StarsTrackerException("Impossible de fermer la webcam");
      }
   }
   
   public void track() throws StarsTrackerException
   {
      Mat imageRef = null;
      Mat imageToCompare = null;
      MatOfPoint featuresRef = null;
      MatOfPoint featuresToCompare = null;
      
      this.initWebCam();
      
      /*
       * MonteVideoRecorder vid = new MonteVideoRecorder(); vid.start();
       */
      
      while (true)
      {
         try
         {
            if (featuresRef == null)
            {
               /* Taking first image */
               imageRef = new Mat();
               this.videoCapture.read(imageRef);
               featuresRef = Utils.getGoodFeaturesToTrack(
                        "/home/manux/starsTracker/featuresRef.png", imageRef, this.maxCorners,
                        this.qualityLevel, this.minDistanceBetweenCorners);
            }
            else
            {
               
               imageToCompare = new Mat();
               this.videoCapture.read(imageToCompare);
               
               featuresToCompare = Utils.getGoodFeaturesToTrack(
                        "/home/manux/starsTracker/featuresToCompare.png", imageToCompare,
                        this.maxCorners, this.qualityLevel, this.minDistanceBetweenCorners);
               
               Point shiftingPoint = Utils.computeShifting(featuresRef, featuresToCompare,
                        this.minDistanceBetweenCorners);
               
               if (shiftingPoint != null)
               {
                  
                  if ((Math.abs(shiftingPoint.x) >= Math.abs(this.minPixelSensitivity) && (Math
                           .abs(shiftingPoint.x) <= Math.abs(this.maxPixelSensitivity)))
                           || ((Math.abs(shiftingPoint.y) >= Math.abs(this.minPixelSensitivity)) && (Math
                                    .abs(shiftingPoint.y) <= Math.abs(this.maxPixelSensitivity))))
                  {
                     /* décalage détecté */
                     Utils.LOGGER.info("Shifting threshold reached : " + shiftingPoint.toString());
                     
                     featuresRef = featuresToCompare;
                     imageRef = imageToCompare;
                     /* On prévient l'interface */
                     modelToGuiEventHandler.onShiftDetected(shiftingPoint);
                     /* On prévient l'arduino */
                     if (hardwareEventHandler != null)
                     {
                        hardwareEventHandler.onShiftDetected(shiftingPoint);
                     }
                     
                  }
               }
               
               final BufferedImage img = Utils.convert(imageToCompare);
               
               /*
                * vid.record(img); if (i==1000) { vid.terminate(); }
                */
               modelToGuiEventHandler.onImageChanged(img);
               
            }
            
            Thread.sleep(Constants.SLEEP);
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      
   }
}
