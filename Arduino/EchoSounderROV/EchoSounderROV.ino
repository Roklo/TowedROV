
// EchoSounderROV.ino

//////////////////////////////////////////////////////////////////////////////////
// NMEA PARESER FOR ROV ONBOARD ECHO SOUNDER
// This appliocations parses the NMEA0183 data for the ROV
// onboard echo sounder and sends the data to the RBPi via
// I2C bus.
#include <nmea.h> // NMEA libarary
#include <SoftwareSerial.h> // Software serial library
//#include <Wire.h> //I2C Library
#include <stdlib.h>
#define arduinoAddress 11 // I2C Address
// Connect Vin (5V) and Gnd
// GPS TX --> Digital 11
// GPS RX --> Digital 10

//-------------------
//Briefcase uno  5 RX pin, 6 TX pin
//ROV nano 2 RX pin, 3 TX pin
//-------------------
SoftwareSerial nmeaSerial(5, 6); // 5 RX pin, 6 TX pin, if
// is unreadable try switching.
NMEA nmeaDecoder(ALL);
// Set true if debugging
static boolean debug = false;
// Variables for NMEA data
float depthBelowTd = 0.00;
float depth = 0.00;
float offsetFromTd = 0.00;
float speedKnots = 0.00;
float speedKm = 0.00;
float temp1 = 0.00;
// Variables for splitting NMEA sentence
char* t0;
char* t1;
char* t2;
char* t3;
char* t4;
char* t5;
char* t6;
char* t7;
char* t8;
char* t9;
boolean LED = false;



void setup() {
  // Serial setup
  Serial.begin(4800);
  nmeaSerial.begin(4800);
  Serial.println("<EchoSounder:0>"); // Sends a setup string to Java.

  pinMode(LED_BUILTIN, OUTPUT);

  // -------------------------------------------------------------------------------- -
  // I2C setup
  // join I2C bus (I2Cdev library doesn't do this automatically)
  // Wire.begin(arduinoAddress);
  // listen on the I2C bus for incoming messages defined by the address
  // if called with data to process go to recieve
  // Wire.onReceive(receiveData); //register events (and name methods)
  // If called to send data send data to the master calling for it.
  // Wire.onRequest(sendData);
  // -------------------------------------------------------------------------------- -
  delay(1000);
  Serial.println("Setup done");
}



// Main loop, collecting data, parsing and sending to RBPi or Surface vessel computer
void loop() {
  if (LED)
  {
    digitalWrite(LED_BUILTIN, HIGH);
    LED = false;
  }
  if (LED)
  {
    digitalWrite(LED_BUILTIN, LOW);
    LED = true;
  }

  //Checks for avilable NMEA sentences
  if (nmeaSerial.available()) {
    if (nmeaDecoder.decode(nmeaSerial.read())) { // if we get a valid NMEA sentence
      if (debug) {
        Serial.println(nmeaDecoder.sentence()); // Pritint NMEA sentence if debuging
      }

      // Decoding the nmea sentence, splitting it up where
      // it is seperated by a comma.
      decodeNmeaSentence();
      // Paring the NMEA sentence, extracting the desiered data
      parseNmeaSentence();
      // Sending updated information to the RBPi.
      convertDataToString();
    }
  }
}
//---------------------------------------------------------------------------------
// NMEA decoder, Splitts the NMEA sentence where it is seperated by commas
void decodeNmeaSentence() {
  t0 = nmeaDecoder.term(0);
  t1 = nmeaDecoder.term(1);
  t2 = nmeaDecoder.term(2);
  t3 = nmeaDecoder.term(3);
  t4 = nmeaDecoder.term(4);
  t5 = nmeaDecoder.term(5);
  t6 = nmeaDecoder.term(6);
  t7 = nmeaDecoder.term(7);
  t8 = nmeaDecoder.term(8);
  t9 = nmeaDecoder.term(9);
}
//---------------------------------------------------------------------------------
// Parses the NMEA sentences and extracts the desiered data
void parseNmeaSentence()
{
  //if t0 is Depth Below Transducer
  if (strcmp(t0, "SDDBT") == 0)
  {
    depthBelowTd = atof(t3); //Extracts field #3 which is depth below transducer.
    if (debug)
    {
      Serial.print("Depth below Transducer: ");
      Serial.println(depthBelowTd);
      Serial.println("");
      Serial.println("");
    }
  }
  //if t0 is Depth of Water
  if (strcmp(t0, "SDDPT") == 0)
  {
    depth = atof(t1); //Extracts field #1 which is depth of water.
    offsetFromTd = atof(t3); //Extracts field #3 which is transducer offset.
    if (debug)
    {
      Serial.print("Depth: ");
      Serial.println(depth);
      Serial.print("Transducer offset: ");
      Serial.println(offsetFromTd);
    }
  }
}

// For converting the parsen NMEA values to string
void convertDataToString()
{

  char str_depth[6]; // Depth 

  char str_depthBelowTd[6]; //Depth Below Transducer


  for (int i = 0; i < 6; i++)
  {
    dtostrf(depth, 4, 2, str_depth);

  }

  for (int i = 0; i < 6; i++)
  {
    dtostrf(depthBelowTd, 4, 2, str_depthBelowTd);
  }


  // Start char, separation char and end char with key
  char* a = "<D:";
  char* b = ":DBT:";
  char* k = ">";
 

  // Creating one big string of <Key:Value> format
  char bigstring[32] = "";  // enough room for all strings together
  strcat(bigstring, a);   // add first string
  strcat(bigstring, str_depth);
  strcat(bigstring, b);
  strcat(bigstring, str_depthBelowTd);
  strcat(bigstring, k);




  // Print the final big string
  Serial.println(bigstring);
  //Serial.write(bigstring); // For serial communication
  // Wire.write(bigstring); // For I2C communication


}
