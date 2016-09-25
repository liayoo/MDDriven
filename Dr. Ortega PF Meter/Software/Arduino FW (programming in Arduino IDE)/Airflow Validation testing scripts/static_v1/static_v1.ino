int const Ts = 8; 
int i, cal;
int const L = 738; //should try 737 b/c simpson's 1/3 requires odd number of data points
int readVal[L];    
int calSamp = 50;
int ss = L / 6;
int secA[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, 1, ss * 5, 1, ss * 3, 1 , ss };
int secB[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, (L - 1), 1, ss * 4, 1, ss * 2, 1 };
int trig1 = 4;
int trig2 = 5;
int p = 1;

void setup() {
  Serial.begin(57600);          //57600 needed for MINI
  Serial.println("Power On");
  pinMode(13, OUTPUT); //Chg*
  pinMode(12, OUTPUT); //
}

void loop() 
{ 
  while(1)
  {
    delay(Ts);
    Serial.print(analogRead(A0));
    Serial.print(",");
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
  if( bits==(cal+1) || bits==(cal-1) || bits==(cal+2) || bits==(cal-2) || bits==(cal+3) || bits==(cal-3) ) {
    bits = cal;
  }
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
