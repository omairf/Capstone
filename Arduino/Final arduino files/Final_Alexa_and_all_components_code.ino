#include <AsyncPrinter.h>
#include <async_config.h>
#include <DebugPrintMacros.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncTCPbuffer.h>
#include <SyncClient.h>
#include <tcp_axtls.h>
#include <FirebaseESP8266.h>
#include <ESP8266WiFi.h>
#include <fauxmoESP.h>

#define FIREBASE_HOST "https://capstone-3c6f3.firebaseio.com/"
#define FIREBASE_AUTH "XkWKa0Yu4XM5NvzEQYCna9pfDOgm95IXoH413Z63"
const char* ssid = "BELL782"; // Your ssid
const char* password = "9392112F"; // Your Password

FirebaseData firebaseData;
FirebaseData getStatus;
fauxmoESP fauxmo;

const int PIR = 4;
const int led = 5;
const int buzzer = 14;
const int button = 16;
int i = 0;
int j = 0;

void setup(){  
   
  pinMode(PIR,INPUT);
  pinMode(led,OUTPUT);
  pinMode(buzzer,OUTPUT);
  pinMode(button,INPUT);

  Serial.begin(115200);
  delay(100);
  //dht.begin();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi is connected");
  Serial.println(WiFi.macAddress());
  Serial.println(WiFi.localIP());
  delay(1000);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Serial.println("Adding device");
  fauxmo.setPort(80);  
  fauxmo.enable(true);
  fauxmo.addDevice("Security System"); 
}

//void changeStatus(){
//  Serial.println("BUTTON IS PRESSED");
//    if(Firebase.getString(getStatus, "/System")){
//      if(getStatus.stringData() == "Armed" && i == 1){
//         Firebase.set(firebaseData, "/System", "Disarmed");
//         i = 0;
//      }
//      else if(getStatus.stringData() == "Disarmed" && i == 2){
//         Firebase.set(firebaseData, "/System", "Armed");
//         i = 0;
//      }
//    }
//}

void loop(){
  fauxmo.handle();
  byte val = digitalRead(button);
  if(Firebase.getString(getStatus, "/System")){
    if(getStatus.stringData() == "Armed"){
      j = 1;
    }
    else if(getStatus.stringData() == "Disarmed"){
      j = 2;
    }
  }
  if(val == HIGH || i == 1 || i == 2){
    Serial.println("BUTTON IS PRESSED");
    Serial.println(i);
    Serial.println(j);

    //if(Firebase.getString(getStatus, "/System")){
      if(j == 1 && i == 1){
         Firebase.set(firebaseData, "/System", "Disarmed");
         i = 0;
         j = 0;
      }
      else if(j == 2 && i == 2){
         Firebase.set(firebaseData, "/System", "Armed");
         i = 0;
         j = 0;
      }
    //}
  }

  long state = digitalRead(PIR);
  if(state == HIGH){
    digitalWrite(led, HIGH);
    //Serial.println("Motion Detected");
    Firebase.set(firebaseData, "/Motion", "Detected");
     
    if(Firebase.getString(getStatus, "/System")){
      if(getStatus.stringData() == "Armed"){
        digitalWrite(buzzer,HIGH);
        tone(buzzer,1000); //turns on buzzer
        Firebase.set(firebaseData, "/Alarm", "On");
        delay(1000);
      }
      else{
        digitalWrite(buzzer,LOW);
        Firebase.set(firebaseData, "/Alarm", "Off"); 
        delay(1000);   
      }
    }
  
  }
  else{
    digitalWrite(buzzer,LOW);
    Firebase.set(firebaseData, "/Alarm", "Off");    
    digitalWrite(led, LOW);
    //Serial.println("No Motion Detected");
    Firebase.set(firebaseData, "/Motion", "Not Detected");
    delay(100);
  }
  fauxmo.onSetState([](unsigned char device_id, const char * device_name, bool state, unsigned char value) {
//    Serial.print("Device name:");
//    Serial.println(device_name);
//    // Here we handle the command received
//    //changeStatus();
//    Serial.println("Device ID: " + device_id);
    Serial.println("State " + state);
//    Serial.println("Value " + value);
    if(state == 0){
      Serial.println("Turning off system");
      i = 1;
    }
    else if(state == 1){
      Serial.println("Turning on system");
      i = 2;
    }
  
  });
  
}
