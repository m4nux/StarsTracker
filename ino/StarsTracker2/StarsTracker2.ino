/*
 * StarTracker
 */

#include <SerialCommand.h>
#include "Stepper2.h"

// change this to the number of steps on your motor
#define STEPSA 200
#define STEPSD 48

// Stepper default speed
float speedA = 200;
float speedD = 40;

#define arduinoLED 13 // Arduino LED on board

#define PID_CONSTANT -0.015 // Arduino LED on board

// create an instance of the stepper class, specifying
// the number of steps of the motor and the pins it's
// attached to

// Le Stepper pour l'ascension droite
Stepper stepperD(STEPSD, 4, 5, 6, 7);

// Le Stepper pour la declinaison
Stepper stepperA(STEPSA, 10, 11,12,13);

SerialCommand SCmd;

void move()
{
  
  char *arg;

  Serial.print("MOVE ");
  arg = SCmd.next();
  while (arg != NULL) {
    char numStepper=arg[0];
    arg = SCmd.next();
    if (arg!=NULL) {
      String valeur(arg); 
      int aNumber=valeur.toInt();
      
      switch (numStepper) {
        case 'A':
          Serial.print("MOVE_A ");
          Serial.print(aNumber);
          Serial.print(" ");
          stepperA.step(aNumber);
          break;
        case 'D':
          Serial.print("MOVE_D ");
          Serial.print(aNumber);
          Serial.print(" ");
          stepperD.step(aNumber);
          break;
      }
    }
  }
  
  Serial.println(" ");
  SCmd.clearBuffer();
}

void changeSpeed()
{
  char *arg;

  Serial.print("SPEED ");
  arg = SCmd.next();
  while (arg != NULL) {
    char numStepper=arg[0];
    arg = SCmd.next();
    if (arg!=NULL) {
      String valeur(arg); 
      int aNumber=valeur.toInt();
      
      switch (numStepper) {
        case 'A':
          Serial.print("SPEED_A ");
          Serial.print(aNumber);
          Serial.print(" ");
          stepperA.step(aNumber);
          break;
        case 'D':
          // Pour la declinaison, on ajuste la vitesse et on fait tourner le moteur par defaut (principe du suivi)
          Serial.print("SPEED_D ");
          Serial.print(aNumber);
          Serial.print(" ");
          stepperD.setSpeed(aNumber);
          stepperD.step(100000);
          break;
      }
    }
  }
  
  Serial.println(" ");
  SCmd.clearBuffer();
}


void shift()
{
  char *arg;

  Serial.print("SHIFT ");
  arg = SCmd.next();
  while (arg != NULL) {
    char numStepper=arg[0];
    arg = SCmd.next();
    if (arg!=NULL) {
      String valeur(arg); 
      int aNumber=valeur.toInt();
      
      switch (numStepper) {
        case 'Y':
          Serial.print("SHIFTY ");
          Serial.print(aNumber);
          Serial.print(" ");
          stepperA.step(aNumber);
          break;
        case 'X':
          // Pour la declinaison, on ajuste la vitesse et on fait tourner le moteur par defaut (principe du suivi)
          Serial.print("SHIFTX ");
          
          speedD = speedD+(aNumber*PID_CONSTANT);
          Serial.print("aNumber*PID_CONSTANT");
          Serial.print(aNumber*PID_CONSTANT);
          Serial.print(" ");
          Serial.print("NEWSPEED ");
          Serial.print(speedD);
          stepperD.setSpeed(speedD);
          //stepperD.step(100000);
          break;
      }
    }
  }
  
  Serial.println(" ");
  SCmd.clearBuffer();
}

void LED_on()
{
  Serial.println("LED on");
  digitalWrite(arduinoLED,HIGH);
}

void LED_off()
{
  Serial.println("LED off");
  digitalWrite(arduinoLED,LOW);
}

void unrecognized() {
  Serial.println("Commandant Mitchel !");
}

// the previous reading from the analog input
int previous = 0;

void setup()
{
  Serial.begin(115200);
    // Setup callbacks for SerialCommand commands
  SCmd.addCommand("ON",LED_on); 
  SCmd.addCommand("OFF",LED_off); 
  SCmd.addCommand("MOVE", move);
  SCmd.addCommand("SPEED", changeSpeed);
  SCmd.addCommand("SHIFT", shift);
  SCmd.addDefaultHandler(unrecognized); // Handler for command that isn't matched (says "Commandant Mitchel?")

  // set the speed of the motors
  stepperA.setSpeed(speedA);
  stepperD.setSpeed(speedD);
  
  // On fait tourner le stepper de declinaison par defaut pour le suivi
  // Si correction a apporter, on change la vitesse c'est tout
  //stepperA.step(100000);
  stepperD.step(1000000);
  
  Serial.println("Ready"); 
    
}

void waitAll() {
  stepperA.doMove();
  stepperD.doMove();
}

void loop()
{
  SCmd.readSerial();
  waitAll();
/*   stepper.step(170); waitAll();
   stepper2.step(170);
   waitAll();
   delay(1000);
   stepper.step(-170); waitAll();
   stepper2.step(-170);
   waitAll();

   delay(1000);*/
}


