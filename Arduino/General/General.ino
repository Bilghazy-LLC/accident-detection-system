 #include<Wire.h>
 #include<LiquidCrystal_I2C.h>

  // lcd configurations
  LiquidCrystal_I2C lcd(0x27, 2,1,0,4,5,6,7,3, POSITIVE);

  
#define TILT 2 
#define LED 13

void setup() {
    // put your setup code here, to run once:
  lcd.begin(16,2);
  lcd.clear();
  lcd.println("Accident Detection System");

  delay(1000);

  // vibrator sensor module
  Serial.begin(9600);
  Serial.println("Vibrator Sensor Robo");

  pinMode(TILT, INPUT);
  pinMode(LED, OUTPUT);
}

void loop() {

  // vibration Sensor codes
  int vibrat_sensed = digitalRead(TILT);

  if(vibrat_sensed == LOW){
      digitalWrite(LED, HIGH);
      Serial.println("Piiiiii pooooooo Accident Detected");
    
    } else {
              digitalWrite(LED,LOW);
              Serial.println("Driver is Happy No Accident");
      
      }

    delay(400);


 
  // LCD Codes 

  // scroll 13 positions (string length) to the left
  // to move it offscreen left:
  for (int positionCounter = 0; positionCounter < 13; positionCounter++) {
    // scroll one position left:
    lcd.scrollDisplayLeft();
    // wait a bit:
    delay(300);
  }

  // scroll 29 positions (string length + display length) to the right
  // to move it offscreen right:
  for (int positionCounter = 0; positionCounter < 29; positionCounter++) {
    // scroll one position right:
    lcd.scrollDisplayRight();
    // wait a bit:
    delay(300);
  }

  // scroll 16 positions (display length + string length) to the left
  // to move it back to center:
  for (int positionCounter = 0; positionCounter < 16; positionCounter++) {
    // scroll one position left:
    lcd.scrollDisplayLeft();
    // wait a bit:
    delay(300);
  }

  // delay at the end of the full loop:
  delay(1000);




  
  

}
