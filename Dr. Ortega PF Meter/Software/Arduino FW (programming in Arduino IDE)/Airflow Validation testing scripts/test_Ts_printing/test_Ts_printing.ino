//zero flow when bits = 509 or 510, make small math improvements at "//*CHANGE"
int const Ts = 2; //true Ts will be at least 8.123 ms
int i, cal;
int const L = 738; //Need odd number data points and even number of segments. L=738 -> 739 points
int readVal[L];    
int calSamp = 50;
int trig1 = 4;
int trig2 = 5;
int p = 1;

void setup() {
  Serial.begin(57600);          
  Serial.println("Power On");
  pinMode(13, OUTPUT); //Chg*
  pinMode(12, OUTPUT); //
}

void loop() { 
  while(p==1) {                        
      cal = findCal(calSamp);        
      Serial.print("Cal is ");
      Serial.print(cal);
      Serial.println("");
      p = 0;
  }
      
    //FIRST CHECK
    delay(Ts);
    readVal[1] = analogRead(A0);
    if (readVal[1] > (cal+trig1))
    {
      Serial.print(readVal[1]);
      Serial.print(",");
      delay(Ts);
      readVal[2] = analogRead(A0);

      //SECOND CHECK
      if (readVal[2] > (cal+trig2))
      {
        Serial.print(readVal[2]);
        Serial.print(",");
        delay(Ts);
        
        //GATHER RAW DATA THIS HAS A TS = 8.31393 ms
        //with oversampling true Ts = 2.31393
        for (i = 3; i <= L; i++)
        {
          Serial.print(analogRead(A0));
          Serial.print(",");
          delay(Ts);
        }
        for (i = 0; i <= L; i++)
        {
          Serial.print(analogRead(A0));
          Serial.print(",");
          delay(Ts);
        }
        for (i = 0; i <= L; i++)
        {
          Serial.print(analogRead(A0));
          Serial.print(",");
          delay(Ts);
        }
        for (i = 0; i <= L; i++)
        {
          Serial.print(analogRead(A0));
          Serial.print(",");
          delay(Ts);
        }
      
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
  if( bits==(cal+1) ) {
    bits = cal;
  }
  float a = 0.0048828125*(bits - cal); //*CHANGE*
  if(a < 0) {                          
    return -sqrt(-a) * .0079066947; //[m^3/s]
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
