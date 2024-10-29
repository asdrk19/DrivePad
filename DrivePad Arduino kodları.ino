#include <SoftwareSerial.h>
#include <Servo.h>  // Servo kütüphanesini dahil et

Servo myServo; 
#define RxD 9  //HC-05  Tx
#define TxD 10  //HC-05  Rx
SoftwareSerial blueToothSerial(RxD,TxD); 
bool light=true;
int hiz_kontrol,servo=90;
void setup() {
   
   Serial.begin(9600);
   blueToothSerial.begin(9600); 
   pinMode(RxD, INPUT);
   pinMode(TxD, OUTPUT);
   pinMode(2, OUTPUT); //Ön farların + pini
   pinMode(3, OUTPUT); //Buzzerın + pini
   pinMode(6, OUTPUT); //l298nin en A pini 
   pinMode(7, OUTPUT); //l298nin in1 girişi
   pinMode(8, OUTPUT); //l298nin in2 girişi
   pinMode(11, OUTPUT); //l298nin en B  girişi 
   pinMode(12, OUTPUT); //l298nin in3 girişi
   pinMode(13, OUTPUT); //l298nin in4 girişi
   digitalWrite(2,HIGH); //Default olarak ışıklar açık ayarlanıyor
   hiz_kontrol=2; //Default olarak hız ayarı 2 ayarlanıyor
   myServo.attach(4); // Servonun sinyal pini
   myServo.write(90); //Default olarak servo ayarı ortada olacak şekilde ayarlanıyor
   
} 
 
void loop() {  


   if (blueToothSerial.available()) { 
      char result2 = blueToothSerial.read();
      Serial.write(result2); // Veri gelen yer (Telefondan buraya geliyor)
      Serial.println(result2);
      if(result2 == 'b'){

        //İLERİ KOMUTU:
        Serial.println("girdi");
           digitalWrite(13, HIGH);
           digitalWrite(12, LOW);
           digitalWrite(7, HIGH);
           digitalWrite(8, LOW);
           if(hiz_kontrol==1){
           analogWrite(6, 68); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 88); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==2){
           analogWrite(6, 105); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 151); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==3){
           analogWrite(6, 171); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 241); // PWM değeri 0-255 arasında olmalı
           }
      }
      else if(result2 == 's'){
        
        //GERİ KOMUTU:
           digitalWrite(13, LOW);
           digitalWrite(12, HIGH);
           digitalWrite(7, LOW);
           digitalWrite(8, HIGH);
           if(hiz_kontrol==1){
           analogWrite(6, 87); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 88); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==2){
           analogWrite(6, 151); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 151); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==3){
           analogWrite(6, 241); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 241); // PWM değeri 0-255 arasında olmalı
           }

      }
      else if(result2=='a'){
        
        //SOL KOMUTU:

           digitalWrite(13, LOW);
           digitalWrite(12, HIGH);
           digitalWrite(7, HIGH);
           digitalWrite(8, LOW);
           if(hiz_kontrol==1){
           analogWrite(6, 87); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 88); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==2){
           analogWrite(6, 151); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 151); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==3){
           analogWrite(6, 241); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 241); // PWM değeri 0-255 arasında olmalı
           }
      }
      else if(result2=='d'){
        
        //SAĞ KOMUTU:
           digitalWrite(13, HIGH);
           digitalWrite(12, LOW);
           digitalWrite(7, LOW);
           digitalWrite(8, HIGH);
           if(hiz_kontrol==1){
           analogWrite(6, 87); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 88); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==2){
           analogWrite(6, 151); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 151); // PWM değeri 0-255 arasında olmalı
           }
           else if(hiz_kontrol==3){
           analogWrite(6, 241); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 241); // PWM değeri 0-255 arasında olmalı
           }
      }
      else if(result2=='z'){
        
        //IŞIK KOMUTU:
        if(light==true){
           digitalWrite(2,LOW);
           light=false;
        }
        else{
            digitalWrite(2,HIGH);
           light=true;
        }}

      else if(result2=='q'){
        
        //KORNA KOMUTU:
           digitalWrite(3,HIGH);
           delay(35);
           digitalWrite(3,LOW);
           if(result2=='x'){
            digitalWrite(3,LOW);
           }
           
      }

         else if(result2=='1'){
          
        //HIZ AYARI 1 KOMUTU:
        hiz_kontrol=1;
      }
       else if(result2=='2'){
         
        //HIZ AYARI 1 KOMUTU:
        hiz_kontrol=2;
      }
      else if(result2=='3'){
         
        //HIZ AYARI 1 KOMUTU:
        hiz_kontrol=3;
      }

       else if(result2=='-'){
         
        //KAMERA SERVO AYARI KOMUTU:

        servo+=13;
        if(servo>180){
          servo=180;
          myServo.write(servo);
        }
        else{
          myServo.write(servo);
        }

      }

         else if(result2=='+'){
          
         //KAMERA SERVO AYARI KOMUTU:  

        servo-=13;
        if(servo<0){
          servo=0;
          myServo.write(servo);
        }
        else{
          myServo.write(servo);
        }

      }

      else{

        //EĞER GELEN VERİ YOKSA DURDURMA KOMUTU:
           analogWrite(6, 0); // PWM değeri 0-255 arasında olmalı
           analogWrite(11, 0); // PWM değeri 0-255 arasında olmalı
      }
   }
   
   else{

    Serial.println("bağlantı yok");
   }}
 
   

  /* if (Serial.available()) { 
      char result = Serial.read();
      blueToothSerial.print(result);
         if(result==' '){

        analogWrite(6, 0); // PWM değeri 0-255 arasında olmalı
   analogWrite(11, 0); // PWM değeri 0-255 arasında olmalı
      }
      
      if(result == 'b'){
    digitalWrite(13, HIGH);
   digitalWrite(12, LOW);
   digitalWrite(7, HIGH);
   digitalWrite(8, LOW);
   analogWrite(6, 255); // PWM değeri 0-255 arasında olmalı
   analogWrite(11, 255); // PWM değeri 0-255 arasında olmalı

      }
        if(result == 's'){
    digitalWrite(13, LOW);
   digitalWrite(12, HIGH);
   digitalWrite(7, LOW);
   digitalWrite(8, HIGH);
   analogWrite(6, 255); // PWM değeri 0-255 arasında olmalı
   analogWrite(11, 255); // PWM değeri 0-255 arasında olmalı

      }
   
   }*/
