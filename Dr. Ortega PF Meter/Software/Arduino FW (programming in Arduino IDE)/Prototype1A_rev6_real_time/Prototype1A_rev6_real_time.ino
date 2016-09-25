int const Ts = 8;      //Ends up effectively becoming 8.1234 ms during data capture for-loop
int i, cal;
int const L = 741;     //need odd number points/even number segments for integration
int readVal[L];        //initialize for 741 total data points. indices 0 - 740
int calSamp = 50;
//int ss = L / 6;        //247 total data points for FEV1. This nice odd int is why we chose L=741.
int trig1 = 3;
int trig2 = 3;
int p = 1;

void setup() {
  Serial.begin(57600);
  Serial.println("Power On");
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT); 
}

void loop() { 
  while(p==1) 
  {
      cal = findCal(calSamp);
      Serial.print("Cal is ");
      Serial.print(cal);
      Serial.println("");
      cal = 509;
      p = 0;
  }
   
   delay(4);
   
    //FIRST CHECK
    readVal[1] = analogRead(A0);
    if (readVal[1] > (cal+trig1) || readVal[1]<(cal-trig1))
    {
      delay(Ts);
      readVal[2] = analogRead(A0);

      //SECOND CHECK
      if (readVal[2] > (cal+trig2) || readVal[2]<(cal-trig2))
      {
        Serial.println("");
        digitalWrite(12, 0);            //possibly move these digitalWrites() above //FIRST CHECK , seem redundant
        digitalWrite(13, 0);            //actually keep here, b/c we don't want breath status vanishing unless used again
        delay(Ts);

        //GATHER RAW DATA - THIS HAS ACTUAL TS = 8.3143631436 ms
        for (i = 3; i < L; i++)
        {
          Serial.print(analogRead(A0));
          Serial.print(",");
          delay(Ts);
        }

        delay(100);
        blinkTimerON();
        delay(500);
        blinkTimerOFF();
        delay(500);
        blinkTimerON();

        //SEND CORRECTING DATA AND  SPLINE
        Serial.println("END. Append this data to beginning:");
        Serial.print(cal);
        Serial.print(",");
        Serial.print(readVal[1]);
        Serial.print(",");
        Serial.print(readVal[2]);

    }
  }
}

int findCal(int calSamp)
{
  int cal = 0;
  for (i = 0; i <= calSamp; i++)
    cal = cal + analogRead(A0);

  return cal/calSamp;
}

float bitsToflow(int bits)
{
  if( bits==(cal+1) )
    bits = cal;
    
  float a = 0.0048828125*(bits - cal);
  if(a < 0) {                          
    return -sqrt(-a) * .0079066947;
  }
  return sqrt(a) * .0079066947;
}

void blinkTimerOFF()
{
  digitalWrite(13, 0);
  digitalWrite(12, 0);
}

void blinkTimerON()
{
  digitalWrite(13, 1);
  digitalWrite(12, 1);
}
