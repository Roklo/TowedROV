

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


//const int CHANNEL1_PIN = A0;
//const int CHANNEL2_PIN = A1;
//const int CHANNEL3_PIN = A2;
//const int CHANNEL4_PIN = A3;
//const int CHANNEL5_PIN = 10;

//const int CHANNEL6_PIN = 11;
//const int CHANNEL7_PIN = 12;
//const int CHANNEL8_PIN = 13;
//float Ach1 = 0.00;
//float Ach2 = 0.00;
//float Ach3 = 0.00;
//float Ach4 = 0.00;
//float Dch5 = 0.00;
//float Dch6 = 0.00;
//boolean Dch7 = false;
//boolean Dch8 = false;
boolean LED = false;



void setup() {
  // Serial setup
  Serial.begin(4800);
  nmeaSerial.begin(4800);
  Serial.println("<EchoSounder:0>");

 // pinMode(CHANNEL5_PIN, INPUT);
 // pinMode(CHANNEL6_PIN, INPUT);
 // pinMode(CHANNEL7_PIN, INPUT);
 // pinMode(CHANNEL8_PIN, INPUT);
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



// Main loop, collecting data, parsing and sending to RBPi
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

  //Checks input
 // Ach1 = analogRead(CHANNEL1_PIN);
 // Ach2 = analogRead(CHANNEL2_PIN);
 // Ach3 = analogRead(CHANNEL3_PIN);
 // Ach4 = analogRead(CHANNEL4_PIN);
 /*
  if (digitalRead(CHANNEL5_PIN) == HIGH)
  {
    Dch5 = 1.00;
  }
  else
  {
    Dch5 = 0.00;
  }
  if (digitalRead(CHANNEL6_PIN) == HIGH)
  {
    Dch6 = 1.00;
  }
  else
  {
    Dch6 = 0.00;
  }
  Dch7 = digitalRead(CHANNEL7_PIN);
  Dch8 = digitalRead(CHANNEL8_PIN);
  */

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
      //sendSerialNMEAData();
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
    /*
      }
      //if t0 is Water speed and heading
      if (strcmp(t0, "VWVHW") == 0) {
      // speedKnots = atof(t5); //Extracts field #5 which is speed in knots.
      // speedKm = atof(t7); //Extracts field #7 which is speed in km/h.
      if (debug) {
      Serial.print("Speed in Knots: ");
      Serial.println(speedKnots);
      Serial.print("Speed in Km/h: ");
      Serial.println(speedKm);
      }
      }
      //if t0 is Mean temp1erature of Water
      if (strcmp(t0, "YXMTW") == 0) {
      temp1 = atof(t1); //Extracts field #1 which is mean temp1erature.
      if (debug) {
      Serial.print("temp1erature in celcius: ");
      Serial.println(temp1);
      }
      }
    */
  }
}

void convertDataToString()
{

  char str_depth[6];

  char str_depthBelowTd[6];

/*
  char str_Ach1[6];
  char str_Ach2[6];
  char str_Ach3[6];
  char str_Ach4[6];
  char str_Dch5[6];
  char str_Dch6[6];
  // char str_Dch7[6];
  //t char str_Dch8[6];
  */

  // char str_speed[6];


  for (int i = 0; i < 6; i++)
  {
    dtostrf(depth, 4, 2, str_depth);

  }

  for (int i = 0; i < 6; i++)
  {
    dtostrf(depthBelowTd, 4, 2, str_depthBelowTd);
  }
  /*

  for (int i = 0; i < 6; i++)
  {
    dtostrf(Ach1, 4, 2, str_Ach1);
  }

  for (int i = 0; i < 6; i++)
  {
    dtostrf(Ach2, 4, 2, str_Ach2);
  }


  for (int i = 0; i < 6; i++)
  {
    dtostrf(Ach3, 4, 2, str_Ach3);
  }



  for (int i = 0; i < 6; i++)
  {
    dtostrf(Ach4, 4, 2, str_Ach4);
  }



  for (int i = 0; i < 6; i++)
  {
    dtostrf(Dch5, 4, 2, str_Dch5);
  }


  for (int i = 0; i < 6; i++)
  {
    dtostrf(Dch6, 4, 2, str_Dch6);
  }
  */

  /*

    for (int i = 0; i < 6; i++)
    {
    dtostrf(Dch7, 4, 2, str_Dch7);
    }

    for (int i = 0; i < 6; i++)
    {
    dtostrf(Dch8, 4, 2, str_Dch8);
    }
  */
  /*

    for (int i = 0; i < 6; i++)
    {
    dtostrf(speedKnots, 4, 2, str_speed);
    }
  */


  char* a = "<D:";
  char* b = ":DBT:";
  //char* c = ":Ach1:";
  //char* d = ":Ach2:";
  //char* e = ":Ach3:";
  //char* f = ":Ach4:";
  //char* g = ":Dch5:";
  //char* h = ":Dch6:";
  //char* i = ":Dch7:";
  //  char* j = ":Dch8:";
  char* k = ">";
  //strcat(str_depth, a);

  char bigstring[32] = "";  // enough room for all strings together
  //bigstring[0] = <;          // start with a null string:
  strcat(bigstring, a);   // add first string
  strcat(bigstring, str_depth);
  strcat(bigstring, b);
  strcat(bigstring, str_depthBelowTd);
  //strcat(bigstring, c);
  //strcat(bigstring, str_Ach1);
  //strcat(bigstring, d);
  //strcat(bigstring, str_Ach2);
 //strcat(bigstring, e);
  //strcat(bigstring, str_Ach3);
  //strcat(bigstring, f);
  //strcat(bigstring, str_Ach4);
  //strcat(bigstring, g);
  //strcat(bigstring, str_Dch5);
  //strcat(bigstring, h);
 // strcat(bigstring, str_Dch6);
  //  strcat(bigstring, i);
  //  strcat(bigstring, str_Dch7);
  //  strcat(bigstring, j);
  //  strcat(bigstring, str_Dch8);
  strcat(bigstring, k);




  // strcat(bigstring, str_speed);
  // strcat(bigstring, d);

  Serial.println(bigstring);
  //Serial.write(bigstring);
  // Wire.write(bigstring);


  //Serial.println(final_depth);
  //Serial.println(final_temp);
}



void sendDataOverSerial(String data[4])
{
  String dataString = "";
  dataString = String("<");

  for (byte i = 0; i < 4; i++)
  {
    dataString = String(dataString + data[i]);
    if (i < 4 - 1)
    {
      dataString = String(dataString + ":");
    }
  }
  dataString = String(dataString + ">");
  Serial.print(dataString);
  delay(1000);
}
//---------------------------------------------------------------------------------
// Sends data to the GUI over serial line.
void sendSerialNMEAData() {
  //  Create byte array of sensor values
  byte data[6] = {(depthBelowTd), (depth), (offsetFromTd),
                  (speedKnots), (speedKm), (temp1)
                 };
  byte newData[2] = {(depth), (temp1)};

  //  Write array "data" with 6 byte.
  Serial.write(data, sizeof(data));



  if (debug) {
    Serial.println("Sending information");
  }
}

//---------------------------------------------------------------------------------
// Sends data to RBPi via Wire.Write();
void sendNMEAData() {
  //Create byte array of sensor values
  //byte data[6] = {(depthBelowTd), (depth), (offsetFromTd),
  //(speedKnots), (speedKm), (temp1)
  //};
  //Write array "data" with 6 byte.
  //  Wire.write(data, sizeof(data));
}
//---------------------------------------------------------------------------------
//Recives data from computer
void receiveData(int b) {
  //recieves connection from Pi
  Serial.println("Event Recived");
}
