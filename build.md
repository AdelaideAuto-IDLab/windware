# Using WINDWare #

We have extend fosstrak server and TDT engine to support passive sensor enabled RFID tags.

To build WINDWare please follow the instructions on how dependencies on [fosstrak How to Buld page](https://code.google.com/p/fosstrak/wiki/AleDevGuideHowToBuild).

Checkout modified TDT engine from [tdt](https://windware.googlecode.com/svn/trunk/tdt/) directory.
Using install the modified TDT engine in the local maven repository.

```
mvn clean install
```


Then checkout the source in the [windware](https://windware.googlecode.com/svn/trunk/windware/) directory.
Then run the maven package goal to build the deployable applications.
```
mvn clean package
```