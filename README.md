# Dijkstra-Java [![Build Status](https://travis-ci.org/vchatela/Dijkstra-Java.svg?branch=master)](https://travis-ci.org/vchatela/Dijkstra-Java)
A java open-source implementation of Dijkstra with .maps and .path files

![Dijkstra](http://img15.hostingpics.net/pics/275768Dijkstra.png)

## Features
* Allow **GPS** functionalities from a source to a destination (in distance or in time) based either on
  * On **Dijkstra** standard
  * Or on **Dijkstra A-Star** (Artifical Intelligence Algorithm)
* Allow **carpol** functionalitie, from a pedestrian source, a driver source to the *same* destination 
  * *With/Without* restriction on walking duration of the pedestrian
  * To **optimize** the way duration for both
* Implements **Connexity algorithm**
* *Generate path* from a source to a dest (ability to export this path)
* With an imported path file ability to *draw* and *calculate* time and duration of the way

**All** of these functionalities are supported by a *friendly* UI.

## Tests
###Unit Tests
* 11 tests 
  * from the **instancation** of the graph
  * to the **assertion of** the time of the execution, based on **real information** mesured
  * passing by **control information** tests

###Functionnal Tests
* TODO : 
  * UI tests
