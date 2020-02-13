# MultiThreaded-Dictionary-Service

The project demonstrates JAVA's Multithreading and Socket Programming Applications.

Multiple clients can access the dictionary service for the following services:
1. Query Words in the dictionary (Pre-defined words, such as "Apple")
2. Add New Words and their Meaning
3. Delete an existing Word and its meaning from the Dictionary

## Requirements to execute the project
1. JDK 11.0.3 or above installed in your machine

## Architecture-Server
The server side is implemented with the multi-threading architecture: thread-per-connection.
Everytime a new user connects, a new thread is allocated to the user until the user disconnects.
The dictionary is implemented using the Singleton design pattern to ensure that only one soleInstance of the object
exits. If multiple dictionary object were created, then it would result to more references to the single dictionary object.

## Client-Side
The Client side has a UI built using JavaSwing. The client can connect to server on the IP and Portnumber and access
the services offered in the dictionary.

## Steps to Execute the project
I. The project can be run from an IDE (IntelliJ/ VSCode/ Ecllipse) of your choice. Follow the below steps
  1. Open the client and server projects on two different windows.
  2. Build both the projects.
  3. First run the server project with one arguement indicating "portnumber".
  4. Second run the client by entering the first parameter "localhost" and second the parameter as "portnumber". If the
     server is run on a different host then enter the IP address of that PC instead of the "localhost".
     
II. Alternatively you can execute the JAR executables using the commandline arguments:
  1. Go the directory where the server JAR exist using the command prompt.
  2. Type the following command: javac -jar DictionaryServer.jar *portNumber*
  3. Similary go to the directory for the client JAR and put the command: 
     javac -jar DictionaryClient.jar *localhost/IPAddress* *portNumber*
