# Java 6 Cinema

This repository hosts a student Java project, by the group n°6 in the advanced programming class. 

# Application 

This app uses several Google Maps APIs and HTML Parsing of google movies service to help a bored user. 
From the user's location the app can give him a list of movies being shown in the nearby cinemas.
**Update :** Google movies has ended their service right before the end of our project. We had to make a quick fix and now we generate our own HTML. 

# Language version

This project uses Java 8 and Maven dependencies.

# Build

First ensure you have the appropriate version of Java. Then you need to have Maven installed on your computer to quickly generate the jar file.
The build process is then pretty straightforward.

First clone the git repository :
```
git clone https://github.com/paulden/java6cinema.git
```
Then get into the "java6cinema" directory and run :
```     
mvn package
```

This creates an executable .jar file with all dependencies.
It is located in the "target" directory and is named java6cinema-0.0.1-SNAPSHOT-jar-with-dependencies.jar
You can now run this .jar file like any other : for example if you like command line, you can use 
```
java -jar target/java6cinema-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

# Testing 

The "tests" package contains a couple of test classes to test the different parts of the app.
- TestClosestCinemas gets the closest cinemas in a 10km radius
- TestHTMLParser tests the Google movies html parsing **WARNING: Doesn't work anymore because Google Movies stopped working.**
- TestTimeToCinema tests the program that calculates the time it takes to go from a place to a cinema
- TestCinemaFinder is a more global program that tests the program's ability to suggest a close cinema
- TestGUI was meant to test the graphical interface but has actually become the main method. 

# Authors

- Renaud Dahl - renaud.dahl@student.ecp.fr
- Paul De Nonancourt - paul.de-nonancourt@student.ecp.fr
- Kévin Descamps - kevin.descamps@student.ecp.fr



