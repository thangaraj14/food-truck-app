
# food-truck-app

## Problem Statement

Write a command line program that prints out a list of food trucks that are open at the current date and current time, when the program is being run. So if I run the program at noon on a Friday, I should see a list of all the food trucks that are open then.

## Data

The San Francisco government’s website has a public data source of food trucks

(https://data.sfgov.org/Economy-and-Community/Mobile-Food-Schedule/jjew-r69b). The data can be accessed in a number of forms, including JSON, CSV, and XML. How you access the data is up to you, but you can find some useful information about making an API request to this data source here ([https://dev.socrata.com/foundry/data.sfgov.org/jjew-r69b](https://dev.socrata.com/foundry/data.sfgov.org/jjew-r69b)  [)](https://dev.socrata.com/foundry/data.sfgov.org/jjew-r69b) [](https://dev.socrata.com/foundry/data.sfgov.org/jjew-r69b).


## Resolution

 This application displays the list of food trucks from Socrata which are open at the current date and the current time.Performance has been taken into consideration while designing this application. It is user friendly.Exception handling has been incorporated.

**Steps to run the application**

 1. Unzip the project by executing the following command in the terminal : 
> unzip food-truck-app.zip

 2. Navigate to the project's target location using 
> cd target

 3. Execute the below mentioned command to run the application
>  java -jar food-truck-0.0.1.jar
