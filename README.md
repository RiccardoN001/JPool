## Execution Guide

For Windows users it is possible to run JPool launching batch file `JPool.bat` in `dist/Windows` repository.\

For Linux and MacOS users (or if you have problems running the batch file in a Windows environment) you need to follow the execution guide below.

### Prerequisites

* JDK 11 or later (https://www.oracle.com/java/technologies/downloads/)
* SDK (https://gluonhq.com/products/javafx/)

### Instructions

1. Set JAVA_HOME environment variable to JDK installation path (https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux)
2. Run following command in Command Prompt (Windows) or Terminal (Linux, MacOS)
```shell
java --module-path /path/to/sdk/lib --add-modules javafx.controls,javafx.fxml --enable-preview -jar /path/to/jar
```
* `/path/to/sdk/lib` is the path to the lib folder (contained in the SDK) on your PC
* `/path/to/jar` is the path to the jar file on your PC
