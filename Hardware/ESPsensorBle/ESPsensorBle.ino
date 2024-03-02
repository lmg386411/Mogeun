/*
    Video: https://www.youtube.com/watch?v=oCMOYS71NIU
    Based on Neil Kolban example for IDF: https://github.com/nkolban/esp32-snippets/blob/master/cpp_utils/tests/BLE%20Tests/SampleNotify.cpp
    Ported to Arduino ESP32 by Evandro Copercini

   Create a BLE server that, once we receive a connection, will send periodic notifications.
   The service advertises itself as: 6E400001-B5A3-F393-E0A9-E50E24DCCA9E
   Has a characteristic of: 6E400002-B5A3-F393-E0A9-E50E24DCCA9E - used for receiving data with "WRITE" 
   Has a characteristic of: 6E400003-B5A3-F393-E0A9-E50E24DCCA9E - used to send data with  "NOTIFY"

   The design of creating the BLE server is:
   1. Create a BLE Server
   2. Create a BLE Service
   3. Create a BLE Characteristic on the Service
   4. Create a BLE Descriptor on the characteristic
   5. Start the service.
   6. Start advertising.

   In this example rxValue is the data received (only accessible inside that function).
   And txValue is the data to be sent, in this example just a byte incremented every second. 
*/
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>

const int sensorInputPin = A4;
const int ledPin = LED_BUILTIN;
const String device_name = "Movice_Sub";

BLEServer *pServer = NULL;
BLECharacteristic * pTxCharacteristic;

bool deviceConnected = false;
bool oldDeviceConnected = false;
bool calculated = false;

double tempSum = 0;
int addCnt = 0;
int ref = 0;

bool ledState = false;
int loopCnt = 0;

#define SERVICE_UUID           "6E400001-B5A3-F393-E0A9-E50E24DCCA9E" // UART service UUID
#define CHARACTERISTIC_UUID_RX "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
#define CHARACTERISTIC_UUID_TX "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"


class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
      deviceConnected = true;
    };

    void onDisconnect(BLEServer* pServer) {
      deviceConnected = false;
    }
};

class MyCallbacks: public BLECharacteristicCallbacks {
    void onWrite(BLECharacteristic *pCharacteristic) {
      std::string rxValue = pCharacteristic->getValue();

      if (rxValue.length() > 0) {
        Serial.println("*********");
        Serial.print("Received Value: ");
        for (int i = 0; i < rxValue.length(); i++)
          Serial.print(rxValue[i]);

        Serial.println();
        Serial.println("*********");

        char cmd = rxValue[0];
        Serial.println(cmd);
      }
    }
};

void sendData(int i) {
  std::string numString = String(i).c_str();
  pTxCharacteristic->setValue(numString);
  pTxCharacteristic->notify();
}


void setup() {
  Serial.begin(115200);

  BLEDevice::init(device_name.c_str());  // 장치명

  pServer = BLEDevice::createServer();            // 서버생성
  pServer->setCallbacks(new MyServerCallbacks()); // 연결 상태 확인 콜백 등록

  BLEService *pService = pServer->createService(SERVICE_UUID);  // 서비스 추가

  // Create a BLE Characteristic
  pTxCharacteristic = pService->createCharacteristic(           // 송신 서비스 정의
										CHARACTERISTIC_UUID_TX,
										BLECharacteristic::PROPERTY_NOTIFY
									);
                      
  pTxCharacteristic->addDescriptor(new BLE2902());

  BLECharacteristic * pRxCharacteristic = pService->createCharacteristic(
											 CHARACTERISTIC_UUID_RX,
											BLECharacteristic::PROPERTY_WRITE         // 수신 서비스 정의
										);

  pRxCharacteristic->setCallbacks(new MyCallbacks());           // 읽기함수 콜백

  // Start the service
  pService->start();

  // Start advertising
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");

  
  pinMode(ledPin, OUTPUT);
}

void loop() {
    // disconnecting
    if (!deviceConnected && oldDeviceConnected) {
        delay(500);
        pServer->startAdvertising();
        Serial.println("start advertising");
        oldDeviceConnected = deviceConnected;
    }
    
    // connecting
    if (deviceConnected && !oldDeviceConnected) {
        oldDeviceConnected = deviceConnected;
    }

    if(loopCnt % 100 == 0) {
        if(ledState) digitalWrite(ledPin, HIGH);
        else digitalWrite(ledPin, LOW);

        if(!deviceConnected) {
            ledState = !ledState;
        }
    }

    if(loopCnt % 20 == 0) {
        if(deviceConnected) {
            ledState = false;

            int ret = analogRead(sensorInputPin);

            // Serial.println(ret);

            if(!calculated) {
              if(addCnt < 200) {
                tempSum += ret;
                addCnt++;
              } else {
                ref = tempSum / 200;
                calculated = true;
                tempSum = 0;
                addCnt = 0;
              }
            } else {
              int sendValue = ret - ref;
              Serial.println(sendValue);
              sendData(sendValue);
            }
        }
    }

    loopCnt++;
    if(loopCnt == 4000) loopCnt = 0;
    delay(1);
}
