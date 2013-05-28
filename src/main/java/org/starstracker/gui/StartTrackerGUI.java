package org.starstracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.starstracker.events.IGuiToModelEventHandler;

public class StartTrackerGUI extends JPanel implements ActionListener
{
   
   private static final long       serialVersionUID = 1L;
   private String                  newline          = "\n";
   private static final String     stepperSpeedX    = "Stepper speed X";
   private static final String     stepperSpeedY    = "Stepper speed Y";
   private static final String     coefStepPixelX   = "Coef step/pixel X";
   private static final String     coefStepPixelY   = "Coef step/pixel Y";
   private static final String     minDistance      = "Min distance in Pixel";
   private static final String     maxCorners       = "Max corners";
   private static final String     qualityLevel     = "Quality level";
   private static final String     numWebcam        = "WebCam number";
   
   private JLabel                  actionLabel;
   
   private JButton                 startTrackButton;
   private JButton                 stopTrackButton;
   private JButton                 buttonxminus;
   private JButton                 buttonyminus;
   private JButton                 buttonxplus;
   private JButton                 buttonyplus;
   
   private JButton                 buttonmajcorners;
   
   private JPanelImage             jpanelImage      = new JPanelImage(null);
   private JTextPane               textPane;
   
   private JFormattedTextField     stepperSpeedXField;
   private JFormattedTextField     stepperSpeedYField;
   private JFormattedTextField     coefPixelSpeedXField;
   private JFormattedTextField     coefPixelSpeedYField;
   private JFormattedTextField     maxCornersField;
   private JFormattedTextField     minDistanceField;
   private JTextField     qualityLevelField;
   private JFormattedTextField     numWebcamField;
   
   private JFormattedTextField     textFieldxminus;
   private JFormattedTextField     textFieldyminus;
   private JFormattedTextField     textFieldxplus;
   private JFormattedTextField     textFieldyplus;
   
   private JButton                 stepperSpeedXButton;
   private JButton                 stepperSpeedYButton;
   private JButton                 coefPixelSpeedXButton;
   private JButton                 coefPixelSpeedYButton;
   private JButton                 maxCornersButton;
   private JButton                 minDistanceButton;
   private JButton                 qualityLevelButton;
   private JButton                 numWebcamButton;
   
   private IGuiToModelEventHandler guiToModelEventHandler;
   
   public JPanelImage getJpanelImage()
   {
      return this.jpanelImage;
   }
   
   public void setJpanelImage(JPanelImage jpanelImage)
   {
      this.jpanelImage = jpanelImage;
   }
   
   public JTextPane getTextPane()
   {
      return textPane;
   }
   
   public void setTextPane(JTextPane textPane)
   {
      this.textPane = textPane;
   }
   
   public IGuiToModelEventHandler getGuiToModelEventHandler()
   {
      return guiToModelEventHandler;
   }
   
   public void setGuiToModelEventHandler(IGuiToModelEventHandler guiToModelEventHandler)
   {
      this.guiToModelEventHandler = guiToModelEventHandler;
   }
   
   public StartTrackerGUI()
   {
      setLayout(new BorderLayout());
      
      // Create a regular text field.
      this.stepperSpeedXField = new JFormattedTextField(100);
      this.stepperSpeedXField.setActionCommand(stepperSpeedX);
      this.stepperSpeedXField.addActionListener(this);
      
      // Create a password field.
      this.stepperSpeedYField = new JFormattedTextField(100);
      this.stepperSpeedYField.setActionCommand(stepperSpeedY);
      this.stepperSpeedYField.addActionListener(this);
      
      // Create a formatted text field.
      this.coefPixelSpeedXField = new JFormattedTextField(10);
      this.coefPixelSpeedXField.setActionCommand(coefStepPixelX);
      this.coefPixelSpeedXField.addActionListener(this);
      
      // Create a formatted text field.
      this.coefPixelSpeedYField = new JFormattedTextField(10);
      this.coefPixelSpeedYField.setActionCommand(coefStepPixelY);
      this.coefPixelSpeedYField.addActionListener(this);
      
      // Create a formatted text field.
      this.maxCornersField = new JFormattedTextField(5);
      this.maxCornersField.setActionCommand(maxCorners);
      this.maxCornersField.addActionListener(this);
      
      // Create a formatted text field.
      this.minDistanceField = new JFormattedTextField(50);
      this.minDistanceField.setActionCommand(minDistance);
      this.minDistanceField.addActionListener(this);
      
      // Create a formatted text field.
      this.qualityLevelField = new JTextField("0.1");
      this.qualityLevelField.setActionCommand(qualityLevel);
      this.qualityLevelField.addActionListener(this);
      
      // Create a formatted text field.
      this.numWebcamField = new JFormattedTextField(0);
      this.numWebcamField.setActionCommand(numWebcam);
      this.numWebcamField.addActionListener(this);
      
      this.stepperSpeedXButton = new JButton("Ok");
      this.stepperSpeedYButton = new JButton("Ok");
      this.coefPixelSpeedXButton = new JButton("Ok");
      this.coefPixelSpeedYButton = new JButton("Ok");
      this.maxCornersButton = new JButton("Ok");
      this.minDistanceButton = new JButton("Ok");
      this.qualityLevelButton = new JButton("Ok");
      this.numWebcamButton = new JButton("Ok");
      
      // Create some labels for the fields.
      JLabel stepperSpeedXLabel = new JLabel(stepperSpeedX + ": ");
      stepperSpeedXLabel.setLabelFor(stepperSpeedXField);
      JLabel stepperSpeedYLabel = new JLabel(stepperSpeedY + ": ");
      stepperSpeedYLabel.setLabelFor(stepperSpeedYField);
      JLabel coefStepPixelXLabel = new JLabel(coefStepPixelX + ": ");
      coefStepPixelXLabel.setLabelFor(coefPixelSpeedXField);
      JLabel coefStepPixelYLabel = new JLabel(coefStepPixelY + ": ");
      coefStepPixelYLabel.setLabelFor(coefPixelSpeedXField);
      JLabel maxCornersLabel = new JLabel(maxCorners + ": ");
      maxCornersLabel.setLabelFor(maxCornersField);
      JLabel minDistanceLabel = new JLabel(minDistance + ": ");
      minDistanceLabel.setLabelFor(minDistanceField);
      JLabel qualityLevelLabel = new JLabel(qualityLevel + ": ");
      qualityLevelLabel.setLabelFor(qualityLevelField);
      JLabel numWebcamLabel = new JLabel(numWebcam + ": ");
      numWebcamLabel.setLabelFor(numWebcamField);
      
      // Create a text area.
      JTextArea textArea = new JTextArea("This is an editable JTextArea. "
               + "A text area is a \"plain\" text component, "
               + "which means that although it can display text "
               + "in any font, all of the text is in the same font.");
      textArea.setFont(new Font("Serif", Font.ITALIC, 16));
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      
      this.jpanelImage = new JPanelImage(null);
      //this.jpanelImage.setBackground(Color.TRANSLUCENT);
      
      JScrollPane areaScrollPane = new JScrollPane(this.jpanelImage);
      areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      areaScrollPane.setPreferredSize(new Dimension(640, 480));
      areaScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
               .createCompoundBorder(BorderFactory.createTitledBorder("Video"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)), areaScrollPane.getBorder()));
      
      // ICIICI
      
      // Lay out the text controls and the labels.
      JPanel textControlsPane = new JPanel();
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      
      textControlsPane.setLayout(gridbag);
      
      JLabel[] labels =
      { stepperSpeedXLabel, stepperSpeedYLabel, coefStepPixelXLabel, coefStepPixelYLabel,
               maxCornersLabel, minDistanceLabel, qualityLevelLabel, numWebcamLabel };
      JTextField[] textFields =
      { stepperSpeedXField, stepperSpeedYField, coefPixelSpeedXField, coefPixelSpeedYField,
               maxCornersField, minDistanceField, qualityLevelField, numWebcamField };
      JButton[] majButtons =
      { stepperSpeedXButton, stepperSpeedYButton, coefPixelSpeedXButton, coefPixelSpeedYButton,
               maxCornersButton, minDistanceButton, qualityLevelButton, numWebcamButton };
      addLabelTextRows(labels, textFields, majButtons, gridbag, textControlsPane);
      
      // Create a label to put messages during an action event.
      startTrackButton = new JButton("Start arduino's steppers");
      startTrackButton.setName("start");
      stopTrackButton = new JButton("Stop arduino's steppers");
      stopTrackButton.setName("stop");
      
      actionLabel = new JLabel("No informations");
      actionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
      
      textControlsPane.add(startTrackButton);
      textControlsPane.add(stopTrackButton);
      textControlsPane.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createTitledBorder("Text Fields"),
               BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      
      // Create an editor pane.
      /*
       * JEditorPane editorPane = createEditorPane(); JScrollPane
       * editorScrollPane = new JScrollPane(editorPane);
       * editorScrollPane.setVerticalScrollBarPolicy
       * (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       * editorScrollPane.setPreferredSize(new Dimension(250, 145));
       * editorScrollPane.setMinimumSize(new Dimension(10, 10));
       */
      
      // Create a text pane.
      this.textPane = createTextPane();
      JScrollPane paneScrollPane = new JScrollPane(textPane);
      paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      paneScrollPane.setPreferredSize(new Dimension(250, 255));
      paneScrollPane.setMinimumSize(new Dimension(10, 10));
      
      // Put the editor pane and the text pane in a split pane.
      // JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
      // editorScrollPane,
      // paneScrollPane);
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textControlsPane,
               paneScrollPane);
      splitPane.setOneTouchExpandable(true);
      splitPane.setResizeWeight(0.5);
      JPanel rightPane = new JPanel(new GridLayout(2, 0));
      rightPane.add(splitPane);
      
      JPanel panelmanualcontrol = new JPanel();
      panelmanualcontrol.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createTitledBorder("Manual control"),
               BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      
      rightPane.add(panelmanualcontrol);
      panelmanualcontrol.setLayout(null);
      
      this.buttonxminus = new JButton("-x");
      this.buttonxminus.setBounds(12, 66, 59, 25);
      panelmanualcontrol.add(this.buttonxminus);
      
      this.textFieldxminus = new JFormattedTextField();
      this.textFieldxminus.setHorizontalAlignment(SwingConstants.RIGHT);
      this.textFieldxminus.setValue(1);
      this.textFieldxminus.setBounds(23, 95, 38, 25);
      panelmanualcontrol.add(this.textFieldxminus);
      this.textFieldxminus.setColumns(10);
      
      this.buttonyplus = new JButton("+y");
      this.buttonyplus.setBounds(93, 12, 59, 25);
      panelmanualcontrol.add(this.buttonyplus);
      
      this.textFieldyplus = new JFormattedTextField();
      this.textFieldyplus.setValue(1);
      this.textFieldyplus.setHorizontalAlignment(SwingConstants.RIGHT);
      this.textFieldyplus.setColumns(10);
      this.textFieldyplus.setBounds(104, 41, 38, 25);
      panelmanualcontrol.add(this.textFieldyplus);
      
      this.buttonyminus = new JButton("-y");
      this.buttonyminus.setBounds(94, 102, 59, 25);
      panelmanualcontrol.add(this.buttonyminus);
      
      this.textFieldyminus = new JFormattedTextField();
      this.textFieldyminus.setValue(1);
      this.textFieldyminus.setHorizontalAlignment(SwingConstants.RIGHT);
      this.textFieldyminus.setColumns(10);
      this.textFieldyminus.setBounds(105, 131, 38, 25);
      panelmanualcontrol.add(this.textFieldyminus);
      
      this.buttonxplus = new JButton("+x");
      this.buttonxplus.setBounds(181, 66, 59, 25);
      panelmanualcontrol.add(this.buttonxplus);
      
      this.textFieldxplus = new JFormattedTextField();
      this.textFieldxplus.setValue(1);
      this.textFieldxplus.setHorizontalAlignment(SwingConstants.RIGHT);
      this.textFieldxplus.setColumns(10);
      this.textFieldxplus.setBounds(192, 95, 38, 25);
      panelmanualcontrol.add(this.textFieldxplus);
      
      rightPane.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createTitledBorder("Styled Text"),
               BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      
      // Put everything together.
      JPanel leftPane = new JPanel(new BorderLayout());
      // leftPane.add(textControlsPane, BorderLayout.PAGE_START);
      leftPane.add(areaScrollPane, BorderLayout.CENTER);
      // leftPane.add(this.jpanelImage, BorderLayout.CENTER);
      
      add(leftPane, BorderLayout.LINE_START);
      add(rightPane, BorderLayout.LINE_END);
      
      this.createEventListeners();
   }
   
   private void addLabelTextRows(JLabel[] labels, JTextField[] textFields, JButton[] majButtons,
            GridBagLayout gridbag, Container container)
   {
      GridBagConstraints c = new GridBagConstraints();
      c.anchor = GridBagConstraints.EAST;
      int numLabels = labels.length;
      
      for (int i = 0; i < numLabels; i++)
      {
         c.gridwidth = GridBagConstraints.LINE_START; // next-to-last
         c.fill = GridBagConstraints.HORIZONTAL; // reset to default
         c.weightx = 0.0; // reset to default
         container.add(labels[i], c);
         
         c.gridwidth = GridBagConstraints.RELATIVE; // end row
         c.fill = GridBagConstraints.HORIZONTAL;
         c.weightx = 1.0;
         container.add(textFields[i], c);
         
         c.gridwidth = GridBagConstraints.REMAINDER; // end row
         c.fill = GridBagConstraints.HORIZONTAL;
         c.weightx = 0.0;
         container.add(majButtons[i], c);
      }
   }
   
   private JEditorPane createEditorPane()
   {
      JEditorPane editorPane = new JEditorPane();
      editorPane.setEditable(false);
      java.net.URL helpURL = StartTrackerGUI.class.getResource("TextSamplerDemoHelp.html");
      if (helpURL != null)
      {
         try
         {
            editorPane.setPage(helpURL);
         }
         catch (IOException e)
         {
            System.err.println("Attempted to read a bad URL: " + helpURL);
         }
      }
      else
      {
         System.err.println("Couldn't find file: TextSampleDemoHelp.html");
      }
      
      return editorPane;
   }
   
   private JTextPane createTextPane()
   {
      String[] initString =
      { "First capture " + Calendar.getInstance().getTime() };
      
      String[] initStyles =
      { "regular", "italic", "bold", "small", "large", "regular", "button", "regular", "icon",
               "regular" };
      
      JTextPane textPane = new JTextPane();
      StyledDocument doc = textPane.getStyledDocument();
      
      try
      {
         for (int i = 0; i < initString.length; i++)
         {
            doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
         }
      }
      catch (BadLocationException ble)
      {
         System.err.println("Couldn't insert initial text into text pane.");
      }
      
      return textPane;
   }
   
   public void show()
   {
      JFrame frame = new JFrame("StartsTracker");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Add content to the window.
      frame.getContentPane().add(this);
      
      // Display the window.
      frame.pack();
      frame.setVisible(true);
   }
   
   private void createEventListeners()
   {
      this.startTrackButton.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            double speedX = new Double(stepperSpeedXField.getText());
            double speedY = new Double(stepperSpeedYField.getText());
            double coefPixelStepX = new Double(coefPixelSpeedXField.getText());
            double coefPixelStepY = new Double(coefPixelSpeedYField.getText());
            guiToModelEventHandler.onStartArduinoSteppers(speedX, speedY, coefPixelStepX,
                     coefPixelStepY);
         }
      });
      this.stopTrackButton.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            guiToModelEventHandler.onStopArduinoSteppers();
         }
      });
      
      /* Manual control */
      this.buttonxplus.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            guiToModelEventHandler.onManualXPlus(Integer.parseInt(textFieldxplus.getText()));
         }
      });
      this.buttonxminus.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            guiToModelEventHandler.onManualXMinus((Integer.parseInt(textFieldxminus.getText())));
         }
      });
      this.buttonyplus.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            guiToModelEventHandler.onManualYPlus((Integer.parseInt(textFieldyplus.getText())));
         }
      });
      this.buttonyminus.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Action performed " + ((JButton) e.getSource()).getName());
            guiToModelEventHandler.onManualYMinus((Integer.parseInt(textFieldyminus.getText())));
         }
      });
      this.stepperSpeedXButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onStepperSpeedXChange(Integer.parseInt(stepperSpeedXField
                     .getText()));
         }
      });
      this.stepperSpeedYButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onStepperSpeedYChange(Integer.parseInt(stepperSpeedYField
                     .getText()));
         }
      });
      this.coefPixelSpeedXButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onCoefPixelSpeedXChange(Integer.parseInt(coefPixelSpeedXField.getText()));
         }
      });
      this.coefPixelSpeedYButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onCoefPixelSpeedYChange(Integer.parseInt(coefPixelSpeedYField.getText()));
         }
      });
      this.maxCornersButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onMaxCornersChange(Integer.parseInt(maxCornersField.getText()));
         }
      });
      this.minDistanceButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onMinDistanceChange(Integer.parseInt(minDistanceField.getText()));
         }
      });
      this.qualityLevelButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onQualitLevelChange(Float.parseFloat(qualityLevelField.getText()));
         }
      });
      this.numWebcamButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            guiToModelEventHandler.onNumWebcamChange(Integer.parseInt(numWebcamField.getText()));
         }
      });
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      // TODO Auto-generated method stub
      System.out.println("Action performed " + e.getSource().toString());
   }
   
}
