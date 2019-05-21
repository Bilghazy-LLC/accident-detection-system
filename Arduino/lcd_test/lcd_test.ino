#include <LiquidCrystal.h>

LiquidCrystal lcd(0x27,12, 11, 5, 4, 3, 2);

void setup() {
  lcd.begin(16,2);
  lcd.clear();
  lcd.print("hello, world!");
}

void loop() {
  
}
