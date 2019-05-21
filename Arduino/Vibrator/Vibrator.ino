#define TILT 2 
#define LED 13

void setup() {
  Serial.begin(9600);
  Serial.println("Vibrator Sensor Robo");

  pinMode(TILT, INPUT);
  pinMode(LED, OUTPUT);
}

void loop() {

  int vibrat_sensed = digitalRead(TILT);

  if(vibrat_sensed == LOW){
      digitalWrite(LED, HIGH);
      Serial.println("Piiiiii pooooooo Accident Detected");
    
    } else {
              digitalWrite(LED,LOW);
              Serial.println("Driver is Happy No Accident");
      
      }

    delay(400);
 

}
