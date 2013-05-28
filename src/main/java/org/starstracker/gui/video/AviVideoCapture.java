package org.starstracker.gui.video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import org.monte.media.Buffer;
import org.monte.media.BufferFlag;
import org.monte.media.Codec;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.MovieReader;
import org.monte.media.Registry;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.VideoFormatKeys.DataClassKey;
import static org.monte.media.VideoFormatKeys.ENCODING_BUFFERED_IMAGE;

public class AviVideoCapture implements IVideoCapture
{
   private String       filename;
   private MovieReader  reader;
   private int          track;
   private Buffer       inbuf;
   private Buffer       codecbuf;
   private Codec        codec;
   
   public AviVideoCapture(String filename)
   {
      super();
      this.filename = filename;
   }
   
   @Override
   public boolean open(int i)
   {
      reader = Registry.getInstance().getReader(new File(filename));
      Format format = new Format(DataClassKey, BufferedImage.class, EncodingKey,
               ENCODING_BUFFERED_IMAGE);
      try
      {
         track = reader.findTrack(0, new Format(MediaTypeKey, MediaType.VIDEO));
         codec = Registry.getInstance().getCodec(reader.getFormat(track), format);
         inbuf = new Buffer();
         codecbuf = new Buffer();
         return true;
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   @Override
   public boolean read(Mat image)
   {
      
      try
      {
         reader.read(track, inbuf);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      codec.process(inbuf, codecbuf);
      
      if (!codecbuf.isFlag(BufferFlag.DISCARD))
      {
         Mat matOfByte = convert((BufferedImage) codecbuf.data);
         matOfByte.copyTo(image);
      }
      return true;
      
   }
   
   public static Mat convert(BufferedImage image_tmp)
   {
      Mat mat = new Mat(image_tmp.getHeight(), image_tmp.getWidth(), CvType.CV_8UC3);
      mat.put(0, 0, ((DataBufferByte) image_tmp.getRaster().getDataBuffer()).getData());
      return mat;
   }

   @Override
   public boolean close()
   {
      // TODO Auto-generated method stub
      return false;
   }
   
   
}