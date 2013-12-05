AirVantage Universe
===================

Universe demonstrates how easy it is to create an IoT application using Facebook + Parse (https://www.parse.com) + AirVantage (http://www.sierrawireless.com/en/productsandservices/AirVantage_M2M_Cloud.aspx).
* Facebook is used to:
 * __authenticate end-users__
* AirVantage is used to:
 * __authenticate the objects__
 * __store and consolidate the data__
 * __comunicate with the objects__
* Parse is used to:
 * __associate Facebook users with AirVantage objects__
 * __develop the Android application__ using the Parse SDK

The repository contains 2 sub-projects:
* [universe-parse](/universe-parse): The cloud code to be hosted by Parse and bridging Facebook and AirVantage
* [universe-android](universe-android): The Android application interacting with objects

Screenshots

![Login screen](/screenshots/Screenshot_2013-12-03-17-51-53.jpeg "Login screen")
![Add and List things](/screenshots/Screenshot_2013-12-03-17-51-32.jpeg "Add and List things")
![Details of a Thing](/screenshots/Screenshot_2013-12-03-17-51-44.jpeg "Details of a Thing")
