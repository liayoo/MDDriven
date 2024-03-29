int const Ts = 8; 
int i, cal;
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

    delay(10);
    Serial.print("$$$");
    delay(5);

    if( gkFlag == 1) {
      digitalWrite(13,1);
      Serial.println("BT is connected");
      //Serial.println("---"); //****** maybe get rid of this
      break;
    }
    digitalWrite(12,0);
    delay(300);
    digitalWrite(12,1);
    delay(250);
  }

  //BEGIN TRIGGER MODE
  while(1) 
  {
    digitalWrite(12,1);
    Serial.print("$$$");      //Roving requires no carriage return
    delay(1000);
    Serial.println("");
    Serial.flush();
    Serial.println("GK");
    if(Serial.available()) {
      gkFlag = Serial.parseInt();
      if( gkFlag==0 ) 
      { 
        //Serial.println("---");
        Serial.println("BT is disconnected");
        break; 
      }
    }





  }

}
