

//////////////////////////////////////////////////////////////////////////////////
// NMEA PARESER FOR ROV ONBOARD ECHO SOUNDER
// This appliocations parses the NMEA0183 data for the ROV
// onboard echo sounder and sends the data to the RBPi via
// I2C bus.
#include <nmea.h> // NMEA libarary
#include <SoftwareSerial.h> // Software serial library
#include <Wire.h> //I2C Library
#include <stdlib.h>
#define arduinoAddress 11 // I2C Address
// Connect Vin (5V) and Gnd
// GPS TX --> Digital 11
// GPS RX --> Digital 10
SoftwareSerial nmeaSerial(2, 3); // 5 RX pin, 6 TX pin, if
// is unreadable try switching.
NMEA nmeaDecoder(ALL);
// Set true if debugging
boolean debug = false;
// Variables for NMEA data
float depthBelowTd = 0.00;
float depth = 0.00;
float offsetFromTd = 0.00;
float speedKnots = 0.00;
float speedKm = 0.00;
float temp1 = 0.00;
// VAriables for splitting NMEA sentence
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


void setup() {
  // Serial setup
  Serial.begin(4800);
  nmeaSerial.begin(4800);
  Serial.println("<ArduinoIO:0>");
  // -------------------------------------------------------------------------------- -
  // I2C setup
  // join I2C bus (I2Cdev library doesn't do this automatically)
  Wire.begin(arduinoAddress);
  // listen on the I2C bus for incoming messages defined by the address
  // if called with data to process go to recieve
  // Wire.onReceive(receiveData); //register events (and name methods)
  // If called to send data send data to the master calling for it.
  // Wire.onRequest(sendData);
  // -------------------------------------------------------------------------------- -
  delay(1000);
}



// Main loop, collecting data, parsing and sending to RBPi
void loop() {
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
      //Convert used NMEA sentence data to String.
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
void parseNmeaSentence() {
  //if t0 is Depth Below Transducer
  if (strcmp(t0, "SDDBT") == 0) {
    depthBelowTd = atof(t3); //Extracts field #3 which is depth below transducer.
    if (debug) {
      Serial.print("Depth below Transducer: ");
      Serial.println(depthBelowTd);
      Serial.println("");
      Serial.println("");
    }
  }
  //if t0 is Depth of Water
  if (strcmp(t0, "SDDPT") == 0) {
    depth = atof(t1); //Extracts field #1 which is depth of water.
    offsetFromTd = atof(t3); //Extracts field #3 which is transducer offset.
    if (debug) {
      Serial.print("Depth: ");
      Serial.println(depth);
      Serial.print("Transducer offset: ");
      Serial.println(offsetFromTd);
    }
  }
  //if t0 is Water speed and heading
  if (strcmp(t0, "VWVHW") == 0) {
    speedKnots = atof(t5); //Extracts field #5 which is speed in knots.
    speedKm = atof(t7); //Extracts field #7 which is speed in km/h.
    if (debug) {
      Serial.print("Speed in Knots: ");
      Serial.println(speedKnots);
      Serial.print("Speed in Km/h: ");
      Serial.println(speedKm);
    }
  }
  //if t0 is Mean temp1erature of Water
  if (strcmp(t0, "YXMTW") == 0) {
    temp1 = atof(t1); //Extracts field #1 which is mean temperature.
    if (debug) {
      Serial.print("temp1erature in celcius: ");
      Serial.println(temp1);
    }
  }
}

// Converting NMEA data to string 
void convertDataToString()
{

  char str_depth[6];

  char str_temp[6];

  // for loop to convert the value to a string
  for (int i = 0; i < 6; i++)
  {
    dtostrf(depth, 4, 2, str_depth);

  }
  // for loop to convert the value to a string
  for (int i = 0; i < 6; i++)
  {
    dtostrf(temp1, 4, 2, str_temp);
  }

  //Star char separations char and ending char, this way the string will be <key:value:key:value>
  char* a = "<Depth:";
  char* b = ":Temp:";
  char* c = ">";
  //strcat(str_depth, a);

  char bigstring[40] = "";  // enough room for all strings together
  //bigstring[0] = <;          // start with a null string:
  strcat(bigstring, a);   // add first string
  strcat(bigstring, str_depth);
  strcat(bigstring, b);
  strcat(bigstring, str_temp);
  strcat(bigstring, c);
  Serial.println(bigstring);
  //Serial.write(bigstring);
  // Wire.write(bigstring);

  // For reading values directly from serial monitor.
  //Serial.println(final_depth);
  //Serial.println(final_temp);
}
