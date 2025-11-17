# ScalaPrototype example

This is an example app made in Scala 3.3.6. Inspired by https://makingthematrix.wordpress.com/2021/04/07/how-to-build-an-android-app-in-scala-2-13/. 
It fetches random quotes from https://dummyjson.com

## Dependencies
This app has been tested with the following dependencies
* Ubuntu 24.04.2 LTS, some libraries may be needed to install. In my case:
  * libasound2-dev (for pkgConfig alsa)
  * libavcodec-dev (for pkgConfig libavcodec)
  * libavformat-dev (for pkgConfig libavformat)
  * libavutil-dev (for pkgConfig libavutil)
  * libfreetype6-dev (for pkgConfig freetype2)
  * libgl-dev (for pkgConfig gl)
  * libglib2.0-dev (for pkgConfig gmodule-no-export-2.0)
  * libglib2.0-dev (for pkgConfig gthread-2.0)
  * libgtk-3-dev (for pkgConfig gtk+-x11-3.0)
  * libpango1.0-dev (for pkgConfig pangoft2)
  * libxtst-dev (for pkgConfig xtst)
* gcc (Tested with 13.3.0)
* Apache Maven 3.8.7 (https://maven.apache.org/docs/3.8.7/release-notes.html)
* Gluon GraalVM 22.1.0.1.r17-gln. It is currently the latest stable release (https://github.com/gluonhq/graal/releases), 
*  Other GraalVM builds might work also, but only Gluon's own GraalVMs are guaranteed to work with gluon plugins.

## Installation and configuration of Gluon GraalVM
* Install sdkman: https://sdkman.io/install/
* After installation run in command line:
  * sdk list java
* In the output there should be: 
  * Gluon         | >>> | 22.1.0.1.r17 | gln     |      | 22.1.0.1.r17-gln
* Run in command line:
  * sdk install java 22.1.0.1.r17-gln
* In .bashrc add lines:
  * export GRAALVM_HOME=<PATH_TO_SDKMAN>/.sdkman/candidates/java/22.1.0.1.r17-gln
  * export JAVA_HOME=$GRAALVM_HOME
  * export PATH=$GRAALVM_HOME/bin:$PATH


## How to use
* Click the button to get a random quote

## How to compile and run example

### Testing
* To test the application and see how it works: mvn gluonfx:run
### Desktop version
* Build native image: mvn gluonfx:build  gluonfx:nativerun

### Android version
* Build native image: mvn -Pandroid gluonfx:build gluonfx:package
* After previous command is successfully completed there should be APK file in target/gluonfx/aarch64-android/gvm
* To install APK to your Android mobile phone, you need adb. adb can be obtained by installing https://developer.android.com/tools/sdkmanager
* Make sure that you have enabled Developer mode in your Android mobile phone and enabled USB debugging after this
* Connect your Android mobile phone to computer using USB
* There should be a popup on asking for permissions in the Android device
* In command line run command: adb devices, Android device should appear in output
* Install the application by running in the command line: adb install <path to APK>
* adb logcat | grep scalaprototype

### Encountered problems and possible solutions
* Tried to use GraalVM 24 CE, which is not officially supported by Gluon
  * Might be possible to get work, but I would guess it would be easier to wait for official Gluon GraalVM CE Gluon 23+25.1
* Problems using Maven with some dependencies:
  * Eg. [WARNING] Dependency: /home/janne/.m2/repository/org/playframework/play-functional_3/3.0.5/play-functional_3-3.0.5.jar
   - exception: Unable to derive module descriptor for /home/janne/.m2/repository/org/playframework/play-functional_3/3.0.5/play-functional_3-3.0.5.jar
   - cause: play.functional.3: Invalid module name: '3' is not a Java identifier
  * Tested a scala-suffix-maven-plugin plugin for this one, but did not get it to work yet
* Some libraries, which use reflection need further configuration (https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/)
  * Configure Tracing Agent
* Maven 3.9.11 seems not to be compatible with gluonfx.maven.plugin
* Fatal error: java.lang.NullPointerException: Cannot invoke "jdk.internal.platform.CgroupInfo.getMountPoint()" because "anyController" is null, linux kernel problem
