// This example demonstrates CmdMessenger's callback & attach methods
// For Arduino Uno and Arduino Duemilanove board (may work with other)

// Download these into your Sketches/libraries/ folder...

// CmdMessenger library available from https://github.com/dreamcat4/cmdmessenger
#include <CmdMessenger.h>
#include <Streaming.h>
#include <Stepper.h>

#define STEPSX 48
#define STEPSY 200

// Mustnt conflict / collide with our message payload data. Fine if we use base64 library ^^ above
char field_separator = ',';
char command_separator = ';';

Stepper stepperX(STEPSX, 4, 5, 6, 7);
Stepper stepperY(STEPSY, 10, 11, 12, 13);

int coefPixelStepX=0;
int coefPixelStepY=0;

int speedX=0;
int speedY=0;

// Attach a new CmdMessenger object to the default Serial port
CmdMessenger cmdMessenger = CmdMessenger(Serial, field_separator,
command_separator);

// ------------------ S E R I A L M O N I T O R -----------------------------
//
// Try typing these command messages in the serial monitor!
//
// 4,hi,heh,ho!;
// 5;
// 5,dGhlIGJlYXJzIGFyZSBhbGxyaWdodA==;
// 5,dGhvc2UgbmFzdHkgY29udHJvbCA7OyBjaGFyYWN0ZXJzICws==;
// 2;
// 6;
//
//
// Expected output:
//
// 1,Arduino ready;
// 1,bens cmd recieved;
// 1,hi;
// 1,heh;
// 1,ho!;
// 1,jerrys cmd recieved;
// 1,"the bears are allright" encoded in base64...;
// 1,dGhlIGJlYXJzIGFyZSBhbGxyaWdodA==;
// 1,jerrys cmd recieved;
// 1,what you send me, decoded base64...;
// 1,the bears are allright;
// 1,jerrys cmd recieved;
// 1,what you send me, decoded base64...;
// 1,those nasty control ;; characters ,,;
// 1,Arduino ready;
// 3,Unknown command;
//

// ------------------ C M D L I S T I N G ( T X / R X ) ---------------------

// We can define up to a default of 50 cmds total, including both directions (send + recieve)
// and including also the first 4 default command codes for the generic error handling.
// If you run out of message slots, then just increase the value of MAXCALLBACKS in CmdMessenger.h

// Commands we send from the Arduino to be received on the PC
enum {
  kCOMM_ERROR = 000, // Lets Arduino report serial port comm error back to the PC (only works for some comm errors)
  kACK = 001, // Arduino acknowledges cmd was received
  kARDUINO_READY = 002, // After opening the comm port, send this cmd 02 from PC to check arduino is ready
  kERR = 003, // Arduino reports badly formatted cmd, or cmd not recognised

  // Now we can define many more 'send' commands, coming from the arduino -> the PC, eg
  // kICE_CREAM_READY,
  // kICE_CREAM_PRICE,
  // For the above commands, we just call cmdMessenger.sendCmd() anywhere we want in our Arduino program.

  kSEND_CMDS_END, // Mustnt delete this line
};

// Commands we send from the PC and want to recieve on the Arduino.
// We must define a callback function in our Arduino program for each entry in the list below vv.
// They start at the address kSEND_CMDS_END defined ^^ above as 004
messengerCallbackFunction messengerCallbacks[] = { 
  bens_msg, // 004 in this example
  NULL };
// Its also possible (above ^^) to implement some symetric commands, when both the Arduino and
// PC / host are using each other's same command numbers. However we recommend only to do this if you
// really have the exact same messages going in both directions. Then specify the integers (with '=')

// ----------------FONCTION RELATIVES AUX STEPPERS ----------------

void majSpeed(String commande, int val)
{
  if (commande.equalsIgnoreCase("speedX"))
  {
    speedX=val;
    stepperX.setSpeed(val);
  }
  else if (commande.equalsIgnoreCase("speedY"))
  {
    speedY=val;
    stepperY.setSpeed(val);
  } else
  {
    String mesg = "Commande non reconnue:" + commande;
    cmdMessenger.sendCmd(kERR, getCharFromString(mesg));
  }
}


void majCoefPixelStep(String commande, int val)
{
  if (commande.equals("coefPixelStepX"))
  {
    coefPixelStepX=val;
  }
  else if (commande.equals("coefPixelStepY"))
  {
    coefPixelStepY=val;
  } else
  {
    String mesg = "Commande non reconnue:" + commande;
    cmdMessenger.sendCmd(kERR, getCharFromString(mesg));
  }
}

void moveStepper(String axe, int val)
{
  if (coefPixelStepX==0)
  {
    cmdMessenger.sendCmd(kERR, "Le coefficient pixel/step moteur en X n'est pas defini (commande coefPixelStepX:50)");
  }

  if (coefPixelStepY==0)
  {
    cmdMessenger.sendCmd(kERR, "Le coefficient pixel/step moteur en Y n'est pas defini (commande coefPixelStepY:50)");
  }

  if (speedX==0)
  {
    cmdMessenger.sendCmd(kERR, "La vitesse du moteur en X doit etre definie (commande speedX:50)");
  }

  if (speedY==0)
  {
    cmdMessenger.sendCmd(kERR, "La vitesse du moteur en Y doit etre definie (commande speedY:50)");
  }

  double nbPasX = val*coefPixelStepX;
  double nbPasY = val*coefPixelStepY;

  if (axe.equalsIgnoreCase("X"))
  {
    stepperX.step(nbPasX);
  }
  else if (axe.equalsIgnoreCase("Y"))
  {
    stepperY.step(nbPasY);
  }
}

// ----------------- Utils --------------------

char * getCharFromString(String chaineString)
{
  char chaineChar[200];
  for (int a=0;a<=chaineString.length();a++)
  {
    chaineChar[a]=chaineString[a];
  }
  return chaineChar;
}

// ------------------ C A L L B A C K M E T H O D S -------------------------

void bens_msg() {
  String thisString;
  // Message data is any ASCII bytes (0-255 value). But can't contain the field
  // separator, command separator chars you decide (eg ',' and ';')
  cmdMessenger.sendCmd(kACK, "bens cmd recieved");
  while (cmdMessenger.available()) {
    char buf[350] = { 
      '\0'                                         };
    cmdMessenger.copyString(buf, 350);
    if (buf[0])
    {
      cmdMessenger.sendCmd(kACK, buf);
      thisString = String(buf);
    }
  }

  int val = thisString.substring(thisString.indexOf(":")+1).toInt();
  String commande = thisString.substring(0, thisString.indexOf(":"));

  if (commande.length()>0)
  {
    if ((commande.equalsIgnoreCase("X")) || (commande.equalsIgnoreCase("Y")))
    {
      moveStepper(commande, val);
    } 
    else if (commande.startsWith("speed"))
    {
      String param = "Maj vitesse-" + commande + ":" + val;
      cmdMessenger.sendCmd(kACK, getCharFromString(param));
      majSpeed(commande, val);
      //speedX=100;
      //speedY=100;
    } 
    else if (commande.startsWith("coefPixelStep"))
    {
      String param = "Maj coef-" + commande + ":" + val;
      cmdMessenger.sendCmd(kACK, getCharFromString(param));
      majCoefPixelStep(commande, val);
    } 
    else
    {
      cmdMessenger.sendCmd(kERR, "Commande non reconnue");
    }
  } 
  else
  {
    cmdMessenger.sendCmd(kERR, "La commande doit etre de la forme cmd:valeur");
  }


}

// ------------------ D E F A U L T C A L L B A C K S -----------------------

void arduino_ready() {
  // In response to ping. We just send a throw-away Acknowledgement to say "im alive"
  cmdMessenger.sendCmd(kACK, "Arduino ready");
}

void unknownCmd() {
  // Default response for unknown commands and corrupt messages
  cmdMessenger.sendCmd(kERR, "Unknown command");
}

// ------------------ E N D C A L L B A C K M E T H O D S ------------------

// ------------------ S E T U P ----------------------------------------------

void attach_callbacks(messengerCallbackFunction* callbacks) {
  int i = 0;
  int offset = kSEND_CMDS_END;
  while (callbacks[i]) {
    cmdMessenger.attach(offset + i, callbacks[i]);
    i++;
  }
}

void setup() {
  // Listen on serial connection for messages from the pc
  // Serial.begin(57600); // Arduino Duemilanove, FTDI Serial
  Serial.begin(115200); // Arduino Uno, Mega, with AT8u2 USB

  // cmdMessenger.discard_LF_CR(); // Useful if your terminal appends CR/LF, and you wish to remove them
  cmdMessenger.print_LF_CR(); // Make output more readable whilst debugging in Arduino Serial Monitor

  // Attach default / generic callback methods
  cmdMessenger.attach(kARDUINO_READY, arduino_ready);
  cmdMessenger.attach(unknownCmd);

  // Attach my application's user-defined callback methods
  attach_callbacks(messengerCallbacks);

  arduino_ready();

  // blink
  pinMode(13, OUTPUT);
}

// ------------------ M A I N ( ) --------------------------------------------

// Timeout handling
long timeoutInterval = 2000; // 2 seconds
long previousMillis = 0;
int counter = 0;

void timeout() {
  // blink
  if (counter % 2)
    digitalWrite(13, HIGH);
  else
    digitalWrite(13, LOW);
  counter++;
}

void loop() {
  // Process incoming serial data, if any
  cmdMessenger.feedinSerialData();

  // handle timeout function, if any
  if (millis() - previousMillis > timeoutInterval) {
    timeout();
    previousMillis = millis();
  }

  // La vitesse en declinaison de base
  //stepperX.setSpeed(100);
  //stepperX.step(2);

  // Loop.
}










