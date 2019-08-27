# Inhaltsverzeichnis
1. [Cordova Entwicklung - Plugin](#CordovaEntwicklung-Plugin)
    1. [Ordnerstruktur und Dateien anlegen](#Ordnerstruktur und Dateien anlegen)
    2. [plugin.xml erstellen](#plugin.xml erstellen)
    3. [JavaScript erstellen](#JavaScript erstellen)
    4. [Java-Klasse erzeugen](#Java-Klasse erzeugen)
    5. [Plugin bei github hochladen](#Plugin bei github hochladen)
    6. [Plugin der Cordova-App hinzufügen](#Plugin der Cordova-App hinzufügen)
    
# Cordova Entwicklung - Plugin
Dieses Dokument gibt einen kleinen Einblick in die Entwicklung von Cordova-Plugins. Es enthält 
Beispielcode für ein Plugin,mit dem es möglich ist die Lampe eines Android-Smartphone zu steuern.

Hinweis:
Es wurde keine Lösung gefunden bei Android den Status des Flashlight abzurufen.
## Ablauf

### 1) Ordnerstruktur und Dateien anlegen
![](ordnerstruktur.png)
### 2) plugin.xml erstellen
Diese xml enthält die Grundlegenden Eigenschaften wie z.B. den Namen des Plugin fest. Außerdem 
enthält es Informationen über den Speicherort der JS-Datei und Informationen für die verschiedenen 
Plattformen (z.B. Android-Package).

```
<?xml version="1.0" encoding="UTF-8"?>
<plugin xmlns="http://apache.org/cordova/ns/plugins/1.0"
        id="cordova-plugin-Licht" version="0.1.0"
        xmlns:android="http://schemas.android.com/apk/res/android">
    <name>cordova-plugin-licht</name>
    <description>Cordova Licht Plugin</description>
    <license>Apache 2.0</license>
    <keywords>cordova,Licht</keywords>
    <js-module src="www/Licht.js" name="Licht">
        <clobbers target="window.Licht" />
    </js-module>
    <!-- android -->
    <platform name="android">
        <config-file target="config.xml" parent="/*">
            <feature name="Licht">
                <param name="android-package" value="org.apache.cordova.plugin.Licht"/>
                <param name="onload" value="true" />
            </feature>
        </config-file>

        <source-file src="src/android/Licht.java" target-dir="src/org/apache/cordova/plugin" />
    </platform>
</plugin>

```
### 3) JavaScript erstellen
Dieses Script dient zur Bedienung der JavaScript-Schnittstelle von Cordova. Das Script ruft die 
Funktion "cordova.exec()", welche als Schnittstelle zur nativen Plattform dient, auf. Die 
Schnittstelle ermöglicht es über JS Parameter an die native Ebene zu übergeben.
```
window.licht = function(str, callback) {
    cordova.exec(
        callback,
        function(err) {callback('error');},
        "Licht",
        "licht",
        [str]);	}

``` 
### 4) Java-Klasse erzeugen
Diese Java-Klasse enthält nativen Code von Android und übernimmt die Parameter aus dem im
vorherigen Schritt erzeugte Javascript. Die Klasse muss von CordovaPlugin erben und die Methode
"execute()" überschreiben. 
```
package org.apache.cordova.plugin;



import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

// Kamera
import android.content.Context;
import android.hardware.camera2.*;

/**
 *
 */
public class Licht extends CordovaPlugin{

    boolean on = false;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("licht")) {
            String message = args.getString(0);
            try {
                this.licht(message, callbackContext);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void licht(String message, CallbackContext callbackContext) throws CameraAccessException{

        CameraManager manager = (CameraManager) cordova.getActivity().getSystemService(Context.CAMERA_SERVICE);

        if (on == false) {
            manager.setTorchMode("0", true);
            callbackContext.success("Licht aus");
            on=true;
        } else {
            manager.setTorchMode("0", false);
            callbackContext.success("Licht an");
            on=false;
        }
    }
}

```
<a id="Plugin bei github hochladen"><a/>
### 5) Plugin bei github hochladen
Am einfachsten lassen sich plugins in eine Cordova-App installieren wenn diese als repository auf 
GitHub hochgeladen werden.


 ### 6) Plugin der Cordova-App hinzufügen
 ```
 cordova plugin add %Pfad des Plugin repository%
 
 cordova plugin add https://github.com/micbros/cordova-plugin-torch.git
 ```

 ### 7)Plattform der Cordova-App hinzufügen
 Damit die App auch für die gewünschte Plattform erzeugt wird, muss diese der App hinzugefügt werden.
 Hierbei werden wichtige Bestandteile der SDK ergänzt.
 ```
 cordova platform add android
 ```


