package org.starstracker.utils;

import gnu.io.CommPortIdentifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.starstracker.constants.Constants;

public class Utils implements Constants
{
   
   public static Logger LOGGER;
   
   static
   {
      initLogger();
   }
   
   public static void initLogger()
   {
      LOGGER = Logger.getLogger("StarsTracker");
      LOGGER.setLevel(Level.INFO);
      LOGGER.removeAllAppenders();
      PatternLayout patternLayout = new PatternLayout("%d{dd/MM/yyyy HH:mm:ss:SSS} | %-5p | %m%n");
      try
      {
         LOGGER.addAppender(new DailyRollingFileAppender(patternLayout, "logs"
                  + "/StarsTracker.log", "'.'yyyy-MM-dd"));
         LOGGER.addAppender(new ConsoleAppender(patternLayout));
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
   }
   
   public static BufferedImage convert(Mat image_tmp)
   {
      MatOfByte matOfByte = new MatOfByte();
      
      Highgui.imencode(".jpg", image_tmp, matOfByte);
      
      byte[] byteArray = matOfByte.toArray();
      BufferedImage bufImage = null;
      
      try
      {
         
         InputStream in = new ByteArrayInputStream(byteArray);
         bufImage = ImageIO.read(in);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return bufImage;
   }
   
   public static Point computeShifting(MatOfPoint features1, MatOfPoint features2,
            double minDistance)
   {
      Double returnXShift = null;
      Double returnYShift = null;
      Map<Double, Integer> mapOfXShifts = new HashMap<Double, Integer>();
      Map<Double, Integer> mapOfYShifts = new HashMap<Double, Integer>();
      for (Point p1 : features1.toArray())
      {
         for (Point p2 : features2.toArray())
         {
            if (((Math.abs(p1.x - p2.x)) < (minDistance / 2))
                     && ((Math.abs(p1.y - p2.y)) < (minDistance / 2)))
            {
               /*
                * Si les deux points comparés sont plus proches que la distance
                * seuil/2, dans ce cas, on peut appairé les points
                */
               double xShift = p1.x - p2.x;
               double yShift = p1.y - p2.y;
               LOGGER.debug("Point " + p1.toString() + " match with point " + p2.toString()
                        + " xshift=" + xShift + ", yShift=" + yShift);
               /*
                * On sauve la valeur du décalage entre ces points concordant
                * dans un hash
                */
               mapOfXShifts.put(xShift,
                        (mapOfXShifts.containsKey(xShift) ? mapOfXShifts.get(xShift) + 1 : 1));
               mapOfYShifts.put(yShift,
                        (mapOfYShifts.containsKey(yShift) ? mapOfYShifts.get(yShift) + 1 : 1));
               break;
            }
         }
      }
      
      /*
       * Ces hash contiennent l'ensemble des décalages en X et Y avec pour
       * chacun d'eux le nombre de fois ou ces décalages ont été trouvés, de
       * cette façon, la plus grande valeur (note) contenu dans la clef, nous
       * indique quel est le décalage de l'image le plus probable
       */
      LOGGER.debug("mapOfXShifts results:" + mapOfXShifts.toString());
      LOGGER.debug("mapOfYShifts results:" + mapOfYShifts.toString());
      
      Iterator<Double> itXKey = mapOfXShifts.keySet().iterator();
      int bestXQuote = 0;
      while (itXKey.hasNext())
      {
         Double x = itXKey.next();
         if (mapOfXShifts.get(x) > bestXQuote)
         {
            bestXQuote = mapOfXShifts.get(x);
            returnXShift = x;
         }
      }
      
      Iterator<Double> itYKey = mapOfYShifts.keySet().iterator();
      int bestYQuote = 0;
      while (itYKey.hasNext())
      {
         Double y = itYKey.next();
         if (mapOfYShifts.get(y) > bestYQuote)
         {
            bestYQuote = mapOfYShifts.get(y);
            returnYShift = y;
         }
      }
      if ((returnXShift != null) && (returnYShift != null))
      {
         return new Point(returnXShift, returnYShift);
      }
      else
      {
         return null;
      }
   }
   
   public static MatOfPoint getGoodFeaturesToTrack(String filename, Mat image, int nbCorners, double qualityLevel,
            double minDistance)
   {
      Mat dst = new Mat();
      MatOfPoint features = new MatOfPoint();
      
      // features.clear();
      
      Imgproc.cvtColor(image, dst, Imgproc.COLOR_RGB2GRAY);
      
      Imgproc.goodFeaturesToTrack(dst, features, nbCorners, qualityLevel, minDistance);
      
      Utils.LOGGER.info(features.dump());
      
      for (Point p : features.toArray())
      {
         Core.circle(image, p, 5, new Scalar(255, 0, 0), 1);
      }
      
      // Save the visualized detection.
      //Utils.LOGGER.debug(String.format("Writing %s", filename));
      //Utils.LOGGER.debug(String.format("Size of features w:%f h:%f", features.size().height,
      //         features.size().height));
      //Highgui.imwrite(filename, image);
      
      return features;
   }
   
   public static String[] listSerialPorts()
   {
      
      Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
      List<String> portList = new ArrayList<String>();
      String portArray[] = null;
      while (ports.hasMoreElements())
      {
         CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
         if (port.getPortType() == CommPortIdentifier.PORT_SERIAL)
         {
            portList.add(port.getName());
         }
      }
      portArray = (String[]) portList.toArray(new String[0]);
      return portArray;
   }
   
}
