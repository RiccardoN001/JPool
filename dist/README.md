## Guida per l'esecuzione

Per utenti Windows è possibile eseguire JPool tramite il file JPool.bat presente nella repository dist.
Per utenti Linux e MacOS (oppure in caso di problemi con l'esecuzione del file batch in ambiente Windows) è necessario seguire la guida di esecuzione seguente.

### Prerequisiti

* JDK 11 o superiore (https://www.oracle.com/java/technologies/downloads/)
* SDK (https://gluonhq.com/products/javafx/)

### Istruzioni

1. Impostare il valore della variabile d'ambiente JAVA_HOME al percorso di installazione del JDK nel proprio PC (https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux)
2. Eseguire il seguente comando nel Prompt dei Comandi (Windows) o nel Terminale (Linux, MacOS)
```shell
java --module-path /path/to/sdk/lib --add-modules javafx.controls,javafx.fxml --enable-preview -jar /path/to/jar
```
* /path/to/sdk/lib è il percorso alla cartella lib (contenuta nel SDK) nel proprio PC
* /path/to/jar è il percorso al file jar nel proprio PC

**N.B.** Su Windows il /path/to/sdk/lib deve essere tra virgolette

GitHub: https://github.com/RiccardoN001/JPool
