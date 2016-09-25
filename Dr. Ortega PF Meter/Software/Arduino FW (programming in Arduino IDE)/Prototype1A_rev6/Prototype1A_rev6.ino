int const Ts = 8;      //Ends up effectively becoming 8.1234 ms during data capture for-loop
int i, cal;
int const L = 741;     //need odd number points/even number segments for integration
int readVal[L];        //initialize for 741 total data points. indices 0 - 740
int calSamp = 50;
int ss = L / 6;        //247 total data points for FEV1. This nice odd int is why we chose L=741.
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
  while(p==1) {
      cal = findCal(calSamp);
      Serial.print("Cal is ");
      Serial.print(cal);
      Serial.println("");
      p = 0;
  }
  
   cal = 509;
   
    //FIRST CHECK
    delay(Ts);
    readVal[1] = analogRead(A0);
    if (readVal[1] > (cal+trig1) || readVal[1]<(cal-trig1))
    {
      delay(Ts);
      readVal[2] = analogRead(A0);

      //SECOND CHECK
      if (readVal[2] > (cal+trig2) || readVal[2]<(cal-trig2))
      {
        int x = 13; //changed
        digitalWrite(12, 0);            //possibly move these digitalWrites() above //FIRST CHECK , seem redundant
        digitalWrite(13, 0);            //actually keep here, b/c we don't want breath status vanishing unless used again
        delay(Ts);

        //GATHER RAW DATA - THIS HAS A TS = 8.1238 ms
        for (i = 3; i < L; i++)
        {
          readVal[i] = analogRead(A0);
          delay(Ts);
        }

        //Spline
        readVal[0] = cal;
        delay(100);
        blinkTimerOFF();
        delay(500);
        blinkTimerON();
        delay(500);
        blinkTimerOFF();

        //PRINT RAW DATA
        Serial.println("");
        for (i = 0; i < L; i++)
        {
          Serial.print(readVal[i]);
          Serial.print(",");
        }

        //DETERMINE PEF - may need to fix to grab 1st max
        float PEF = 0;
        for (i = 0; i < L; i++)
          PEF = max(PEF, readVal[i]);

        //CONVERT PEF
        PEF = bitsToflow(PEF) * 60000;  //[m^3/s * (1000L/m^3)*(60 s/min) ]

        //ANALYZE PEF (Orange(13) -> out of range)
        if (PEF > 600 || PEF < 400)
          digitalWrite(13, 1);
        else
          digitalWrite(12, 1);

        //PRINT PEF
        Serial.println("");
        Serial.print("PEF in LPM is ");
        Serial.print(PEF, 2);
        
        //DETERMINE FEV1/FEV6
        float FEV1_even = 0;  //should these be = 0.0
        float FEV1_odd = 0;
        float FEV_even = 0;
        float FEV_odd = 0;
        float FEV1simp = 0;
        float FEV6simp = 0;
        float FEVtemp = 0;
        float FEV_beg_end[2];
        FEV_beg_end[0] = bitsToflow(readVal[0]);  //start of even indices
        FEV_beg_end[1] = bitsToflow(readVal[L - 1]);    //736, this is really last value in array not readVal[L]

        //CONVERT FEV
        for (i = 2; i <= (L - 3); i = (i + 2))    //2,4,6,...,(L-3)
        {
          FEVtemp = bitsToflow(readVal[i]);
          FEV_even = FEV_even + FEVtemp;
          if(i == (ss-3))
            FEV1_even = FEV_even;
        }
        for (i = 1; i <= (L - 2); i = (i + 2))    //1,3,5,...,737,(L-2)
        {
          FEVtemp = bitsToflow(readVal[i]);
          FEV_odd = FEV_odd + FEVtemp;
          if(i==(ss-2))
            FEV1_odd = FEV_odd;
        }

        //SIMPSON'S 1/3 COMPOSITE INTEGRATION
        FEV6simp = FEV_beg_end[0] + 4*FEV_odd + 2*FEV_even + FEV_beg_end[1];
        FEV6simp = FEV6simp * 2.7079365079; //c = (1000L/m^3)*(b-a)/(3n)  = (1000)*6 sec/(3*741 points) = 1000*Ts/3 = 8.123/3 [ (L*s)/(m^3) ]
        Serial.println("");
        Serial.print("Simpson's 1/3 rule FEV6 in L is ");
        Serial.print(FEV6simp, 4);
        Serial.println("");
        
        //SIMPSON'S 1/3 COMPOSITE INTEGRATION
        FEV_beg_end[1] = bitsToflow(readVal[ss - 1]); //last point of FEV1 dataset, similar to FEV6     
        FEV1simp = FEV_beg_end[0] + 4*FEV1_odd + 2*FEV1_even + FEV_beg_end[1];
        FEV1simp = FEV1simp * 2.7079365079;
        Serial.print("Simpson's 1/3 rule FEV1 in L is ");
        Serial.print(FEV1simp, 4);
        Serial.println("");
        
        //Calculate FVC
        Serial.print("%FVC of user is ");
        Serial.print(100.00*FEV1simp/FEV6simp,4);
        Serial.println("");
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
