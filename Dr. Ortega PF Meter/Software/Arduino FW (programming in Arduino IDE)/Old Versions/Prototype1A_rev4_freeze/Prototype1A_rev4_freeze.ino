//copied from frozen rev3. Added more calculations. Put btIndic in rev4

int const Ts = 8; 
int i, cal;
int const L = 738; 
int readVal[L];    
int calSamp = 50;
int ss = L / 6;
int secA[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, 1, ss * 5, 1, ss * 3, 1 , ss };
int secB[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, (L - 1), 1, ss * 4, 1, ss * 2, 1 };
int trig1 = 4;
int trig2 = 5;
int p =1;

void setup() {
  Serial.begin(57600);          //57600 needed for MINI
  Serial.println("Power On");
  pinMode(13, OUTPUT); 
  pinMode(12, OUTPUT); 
}

void loop() { 
  while(p==1) {                        //added 1 time cal 09/25/2015
      cal = findCal(calSamp);          //bug fixed 09/25/2015
      Serial.print("Cal is ");
      Serial.print(cal);
      Serial.println("");
      p = 0;
  }
      
    //FIRST CHECK
    delay(Ts);
    readVal[1] = analogRead(A0);
    if (readVal[1] > (cal+trig1))      //bug fixed09/9/2015
    {
      delay(Ts);
      readVal[2] = analogRead(A0);

      //SECOND CHECK
      if (readVal[2] > (cal+trig2))
      {
        int x = 13; //changed
        digitalWrite(12, 0);            //possibly move these digitalWrites() above //FIRST CHECK , seem redundant
        digitalWrite(13, 0);            //actually keep here, b/c we don't want breath status vanishing unless used again
        delay(Ts);

        //GATHER RAW DATA
        for (i = 3; i < L; i++)
        {
          readVal[i] = analogRead(A0);
          delay(Ts);
          if (i == secA[x]) {        //chg*
            digitalWrite(13, 1);
            digitalWrite(12, 0);
            x = x - 1;
          }
          else if(i == secB[x]) {
            digitalWrite(12, 1);
            digitalWrite(13, 0);          //chg* 09/10/2015
            x = x - 1;
          }
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

        //DETERMINE PEF
        float PEF = 0;
        for (i = 0; i < L; i++)
          PEF = max(PEF, readVal[i]);

        //CONVERT PEF
        PEF = bitsToflow(PEF);
        PEF = PEF * 60000;

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
        float FEVsimp = 0;
        float FEVtemp = 0;
        float FEV_beg_end[4];
        FEV_beg_end[0] = bitsToflow(readVal[0]);  //start of even indices
        FEV_beg_end[1] = bitsToflow(readVal[1]);
        FEV_beg_end[2] = bitsToflow(readVal[L - 2]);
        FEV_beg_end[3] = bitsToflow(readVal[L - 1]);    //this is really last value in array not readVal[L]

        //CONVERT FEV
        for (i = 2; i < (L - 3); i = (i + 2))
        {
          FEVtemp = bitsToflow(readVal[i]);
          FEV_even = FEV_even + FEVtemp;
          if(i == (ss-4))
            FEV1_even = FEV_even;                      // 0 1 2s .... 119, 120, 121, ss-1(122) .... 734, 735, 736, L-1(737)
        }
        for (i = 3; i < (L - 2); i = (i + 2))
        {
          FEVtemp = bitsToflow(readVal[i]);
          FEV_odd = FEV_odd + FEVtemp;
          if(i==(ss-3))
            FEV1_odd = FEV_odd;
        }

        //SIMPSON'S 1/3 COMPOSITE INTEGRATION
        FEVsimp = FEV_beg_end[0] + 4 * (FEV_odd + FEV_beg_end[1]) + 2 * (FEV_even + FEV_beg_end[2]) + FEV_beg_end[3];
        FEVsimp = FEVsimp * 2.7430; //c = (1000L/m^3)*(b-a)/(3n)  = (1000)*6/(3*750) = 1000*TS/3 = 8.229/3
        Serial.println("");
        Serial.print("Simpson's 1/3 rule FEV6 in L is ");
        Serial.print(FEVsimp, 4);
        Serial.println("");
        
        //SIMPSON'S 1/3 COMPOSITE INTEGRATION
        FEV_beg_end[2] = bitsToflow(readVal[ss - 2]); //odd
        FEV_beg_end[3] = bitsToflow(readVal[ss - 1]); //even      
        FEV1simp = FEV_beg_end[0] + 4 * (FEV1_odd + FEV_beg_end[1]) + 2 * (FEV1_even + FEV_beg_end[2]) + FEV_beg_end[3];
        FEV1simp = FEV1simp * 2.7430;
        Serial.print("Simpson's 1/3 rule FEV1 in L is ");
        Serial.print(FEV1simp, 4);
        Serial.println("");
        
        //Calculate FVC
        Serial.print("%FVC of user is ");
        Serial.print(100.00*FEV1simp/FEVsimp,4);
        Serial.println("");
    }
  }
}

int findCal(int calSamp)
{
  int cal = 0;
  for (i = 0; i < calSamp; i++)
    cal = cal + analogRead(A0);

  return cal/calSamp;
}

float bitsToflow(int bits)
{
  if( bits > (cal-4) && bits < (cal+4) )
    bits = cal;
    
  float a = 0.004887585533*(bits - cal);
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
