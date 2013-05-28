package org.starstracker.arduino;

import java.io.IOException;
import java.io.InputStream;

public class SerialReader implements Runnable
{
   
   InputStream in;
   boolean     stopped = false;
   
   public SerialReader(InputStream in)
   {
      this.in = in;
   }
   
   @Override
   public void run()
   {
      byte[] buffer = new byte[1024];
      int len = -1;
      try
      {
         while ((len = this.in.read(buffer)) > -1 && !stopped)
         {
            System.out.print(new String(buffer, 0, len));
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   void stop()
   {
      stopped = true;
   }
}
