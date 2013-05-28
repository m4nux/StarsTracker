package org.starstracker.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class TestSwing extends JPanel implements ActionListener
{
   public TestSwing() {
      setLayout(new GridLayout(0, 1, 0, 0));
      
      JSplitPane splitPane = new JSplitPane();
      add(splitPane);
   }
   {
      
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      // TODO Auto-generated method stub
      
   }
}