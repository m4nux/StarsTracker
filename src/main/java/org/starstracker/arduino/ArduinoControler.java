package org.starstracker.arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.starstracker.StarsTrackerException;

public class ArduinoControler
{
   
   private OutputStreamWriter writer;
   private SerialPort         commPort;
   private SerialReader       reader;
   private OutputStream       out;
   
   public void connect(String port) throws StarsTrackerException
   {
      CommPortIdentifier portIdentifier;
      try
      {
         portIdentifier = CommPortIdentifier.getPortIdentifier(port);
         commPort = (SerialPort) portIdentifier.open("starstracker", 2000);
         commPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                  SerialPort.PARITY_NONE);
         out = commPort.getOutputStream();
         writer = new OutputStreamWriter(out);
         InputStream in = commPort.getInputStream();
         reader = new SerialReader(in);
         (new Thread(reader)).start();
      }
      catch (NoSuchPortException e)
      {
         throw new StarsTrackerException(e);
      }
      catch (PortInUseException e)
      {
         throw new StarsTrackerException(e);
      }
      catch (UnsupportedCommOperationException e)
      {
         throw new StarsTrackerException(e);
      }
      catch (IOException e)
      {
         throw new StarsTrackerException(e);
      }
   }
   
   public void sendCommande(String commande) throws StarsTrackerException
   {
      try
      {
         System.out.println(commande);
         writer.write(commande);
         writer.flush();
         out.flush();
         // Thread.sleep(100);
      }
      catch (IOException e)
      {
         throw new StarsTrackerException(e);
      }
   }
   
   public void disconnect()
   {
      
      commPort.close();
      commPort = null;
      writer = null;
      reader.stop();
      reader = null;
   }
}
