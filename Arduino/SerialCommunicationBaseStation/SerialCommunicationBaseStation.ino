
#include <Adafruit_GPS.h> // Adafruit GPS library
#include <SoftwareSerial.h> // Software serial library



SoftwareSerial mySerial(3, 2); // GPS connection

//HardwareSerial mySerial = Serial1;


Adafruit_GPS GPS(&mySerial);

#define GPSECHO false
#define NUM_SAMPLES 10

int sum = 0;                    // sum of samples taken
unsigned char sample_count = 0; // current sample number
float voltage = 0.0;            // calculated voltage
float calibratedVoltage = 0.0;

boolean usingInterrupt = false;
void useInterrupt(boolean); // Func prototype keeps Arduino 0023 happy
String data[14];


void setup()
{

  Serial.begin(115200);
  Serial.println("<GPS:0>");

  GPS.begin(9600);

  GPS.sendCommand(PMTK_SET_NMEA_OUTPUT_RMCGGA);

  // Set the update rate
  GPS.sendCommand(PMTK_SET_NMEA_UPDATE_1HZ);   // 1 Hz update rate
  GPS.sendCommand(PGCMD_ANTENNA);

  useInterrupt(true);

  delay(1000);
  mySerial.println(PMTK_Q_RELEASE);
}


SIGNAL(TIMER0_COMPA_vect) {
  char c = GPS.read();

#ifdef UDR0
  if (GPSECHO)
    if (c) UDR0 = c;
#endif
}

void useInterrupt(boolean v) {
  if (v) {

    OCR0A = 0xAF;
    TIMSK0 |= _BV(OCIE0A);
    usingInterrupt = true;
  } else {
    TIMSK0 &= ~_BV(OCIE0A);
    usingInterrupt = false;
  }
}


void sendDataOverSerial(String data[14])
{
  String dataString = "";
  dataString = String("<");



  for (byte i = 0; i < 14; i++)
  {
    dataString = String(dataString + data[i]);
    if (i < 14 - 1)
    {
      dataString = String(dataString + ":");
    }
  }
  dataString = String(dataString + ">");
  Serial.println(dataString);
  delay(1000);
}

void setToByteArray()
{
  data[0] = "Satellites";
  data[1] = String(GPS.satellites);
  data[2] = "Altitude";
  data[3] = String(GPS.altitude);
  data[4] = "Angle";
  data[5] = String(GPS.angle);
  data[6] = "Speed";
  data[7] = String(GPS.speed);
  data[8] = "Latitude";
  data[9] = String(GPS.latitudeDegrees , 5);
  data[10] = "Longitude";
  data[11] = String(GPS.longitudeDegrees, 5);
  data[12] = "Voltage";
  data[13] = String(calibratedVoltage);
}

uint32_t timer = millis();
void loop()                     // run over and over again
{
  if (! usingInterrupt)
  {
    char c = GPS.read();
    if (GPSECHO)
      if (c) Serial.print(c);
  }
  if (GPS.newNMEAreceived())
  {
    if (!GPS.parse(GPS.lastNMEA()))
      return;
  }
  if (timer > millis())  timer = millis();

  if (millis() - timer > 500)
  {
    timer = millis(); // reset the timer
    setToByteArray();
    sendDataOverSerial(data);
  }

  // take a number of analog samples and add them up
  while (sample_count < NUM_SAMPLES) {
    sum += analogRead(A0);    
    sample_count++;
    delay(10);
  }
  // calculate the voltage
  // use 5.0 for a 5.0V ADC reference voltage
  // 5.015V is the calibrated reference voltage
  voltage = ((float)sum / (float)NUM_SAMPLES * 4.6) / 1024.0;
  calibratedVoltage = voltage * (11.53);
  // send voltage for display on Serial Monitor
  // voltage multiplied by 11 when using voltage divider that
  // divides by 11. 11.132 is the calibrated voltage divide
  // value
  // Serial.print(voltage * 11.132);
  // Serial.println (" V");
  sample_count = 0;
  sum = 0;

}

