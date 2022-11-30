## Guida per l'esecuzione

### Prerequisiti

* JDK 11 o superiore (https://www.oracle.com/java/technologies/downloads/)
* SDK (https://gluonhq.com/products/javafx/)

### Istruzioni

1. Impostare il valore della variabile d'ambiente JAVA_HOME al percorso di installazione del JDK (https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux)
2. Eseguire il segente comando:
```console
java --module-path /path/to/sdk/lib --add-modules javafx.controls,javafx.fxml 
--enable-preview -jar /path/to/jar
```
