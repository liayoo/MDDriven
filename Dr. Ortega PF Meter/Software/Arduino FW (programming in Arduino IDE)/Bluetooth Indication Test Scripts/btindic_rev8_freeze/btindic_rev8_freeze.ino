int const Ts = 8; 
int i, cal, p;
int const L = 738; 
int readVal[L];    
int calSamp = 100;
int ss = L / 6;
int secA[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, 1, ss * 5, 1, ss * 3, 1 , ss }; //chg
int secB[14] = { 
  1, 1, 1, 1, 1, 1, 1, 1, (L - 1), 1, ss * 4, 1, ss * 2, 1 }; //chg
int trig1 = 8;
int trig2 = 9;
int gkFlag = 0;

void setup()
{
  Serial.begin(57600);          //57600 needed for MINI
  Serial.println("Power On");
  pinMode(13, OUTPUT); //Chg*
  pinMode(12, OUTPUT); //
  delay(510);            //Roving module require 500ms before $$$
  Serial.print("$$$");      //Roving requires no carriage return
  delay(5);                 //Roving requires 5ms after command
}

void loop() { 
  digitalWrite(12,0);
  digitalWrite(13,0);
  Serial.println("");

  //CONFIRM BT CONNECTION WITH PHONE 
  while(1) {
    Serial.flush();
    Serial.println("GK");

    if(Serial.available() > 0) 
      gkFlag = Serial.parseInt();

    delay(500);
    Serial.print("$$$");

    if( gkFlag == 1) {
      digitalWrite(13,1);
      digitalWrite(12,1);
      Serial.println("");
      Serial.println("BT is connected");
      p=1;
      //delay(1000);
      break;
    }
    digitalWrite(12,0);
    delay(300);
    digitalWrite(12,1);
    delay(250);
  }

  //BEGIN TRIGGER MODE -- needs more work to optimize the delay(100)
  while(1) 
  {
    while(p==1) 
    {
      delay(100);
      Serial.print("---");
      Serial.println("");
      p=0;
    }

    //Main program
    delay(100);
    Serial.println("do work");
    delay(100);
    Serial.println("do more?"); 

    //    Serial.println("");
    //    Serial.println("F,1");
    //    Serial.println("is work happening");
    //    Serial.println("");

    //check disconnection
    delay(510);
    Serial.print("$$$");
    delay(500);
    Serial.print("");
    Serial.flush();
    Serial.println("GK");
    if(Serial.available()) 
    {
      gkFlag = Serial.parseInt();
      if( gkFlag==0 ) 
      { 
        Serial.println("BT is disconnected");
        break; 
      }
    }
    Serial.print("---");
    Serial.println("");



  }
}







