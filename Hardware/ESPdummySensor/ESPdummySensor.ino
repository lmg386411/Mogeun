#include "BluetoothSerial.h"
#define USE_PIN // Uncomment this to use PIN during pairing. The pin is specified on the line below

const char *pin = "1234"; // Change this to more secure PIN.

const int sensorInputPin = 34;
const int ledPin = 2;
int deviceId = 3 ;

String device_name = "ESP32-BT-Test";

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

#if !defined(CONFIG_BT_SPP_ENABLED)
#error Serial Bluetooth not available or not enabled. It is only available for the ESP32 chip.
#endif

BluetoothSerial SerialBT;

bool isConnected = false;
bool ledState = true;
int loopCnt = 0;

int ref = 0;
bool calculated = false;

void setup() {
  Serial.begin(115200);
  SerialBT.begin(device_name); //Bluetooth device name
  Serial.printf("The device with name \"%s\" is started.\nNow you can pair it with Bluetooth!\n", device_name);
  //Serial.printf("The device with name \"%s\" and MAC address %s is started.\nNow you can pair it with Bluetooth!\n", device_name.c_str(), SerialBT.getMacString()); // Use this after the MAC method is implemented
  #ifdef USE_PIN
    SerialBT.setPin(pin);
    Serial.println("Using PIN");
  #endif

  pinMode(ledPin, OUTPUT);
}

void sendData(int i) {
  char buf[8];
  sprintf(buf, "%d#%05d", deviceId, i);
  Serial.println(buf);
  SerialBT.print(buf);
}

int num = 0;

void loop() {

  if(loopCnt % 100 == 0) {
    if(ledState) digitalWrite(ledPin, HIGH);
    else digitalWrite(ledPin, LOW);
  
    if(!isConnected) {
        ledState = !ledState;
    }
  }

  if(loopCnt % 1000 == 0) {
//    if (Serial.available()) {
//      String buf = Serial.readStringUntil('\n');
//      int num = buf.toInt();
//      sendData(num);
//    }
    if (SerialBT.available()) {
      String msg = SerialBT.readStringUntil('\n');
      int msgLength = msg.length();
      int msgIdx = msg.indexOf('#');
  
      String mobile = msg.substring(0, msgIdx);
      String cmd = msg.substring(msgIdx + 1, msgLength);
  
//      Serial.println("보낸사람 : ");
//      Serial.println(mobile);
//      Serial.println("명령 : ");
//      Serial.println(cmd);
  
      if(cmd.toInt() == 0) {
        deviceId = 0;
        isConnected = true;
        ledState = true;
      } else if(cmd.toInt() == 1) {
        deviceId = 1;
        isConnected = true;
        ledState = true;
      } else if(cmd.toInt() == 2) {
        deviceId = 2;
        isConnected = true;
        ledState = true;
      } else if(cmd.toInt() == 3) {
        deviceId = 3;
        isConnected = true;
        ledState = true;
      } else {
        isConnected = false;
      }
    }  
  }

  if(loopCnt % 20 == 0) {
    if(isConnected) {
      num++;

      if(num > 440) num = 0;

      sendData(num);
    }
  }

  loopCnt++;
  if(loopCnt == 4000) loopCnt = 0;
  delay(1);
}
