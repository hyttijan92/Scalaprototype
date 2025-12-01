# ScalaPrototype example

This is an example app made in Scala 3.3.6. Inspired by https://makingthematrix.wordpress.com/2021/04/07/how-to-build-an-android-app-in-scala-2-13/. 
It fetches random quotes from https://dummyjson.com

## Dependencies
This app has been tested with the following dependencies
* gcc (Tested with 13.3.0)
* Apache Maven 3.8.7 (https://maven.apache.org/docs/3.8.7/release-notes.html)
* Gluon GraalVM 22.1.0.1.r17-gln. It is currently the latest stable release (https://github.com/gluonhq/graal/releases), 
* Other GraalVM builds might work also, but only Gluon's own GraalVMs are guaranteed to work with gluon plugins.

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
* Application consists of two views: main view and saved quotes view
* In main view user can fetch a random quote, save it and go to saved quotes view
* Saved quotes view shows list of saved quotes, allows to delete saved quotes and to return to main view

## How to compile and run example

### Testing
* To test the application and see how it works: mvn gluonfx:run

### Pre native image building
* Run application, using command: mvn gluonfx:runagent
* This command collects the necessary metadata, while the application is running

### Desktop version
* Build native image and run it: mvn gluonfx:build  gluonfx:nativerun

### Android version
* Build native image and package it as APK: mvn -Pandroid gluonfx:build gluonfx:package
* After previous command is successfully completed there should be APK file in target/gluonfx/aarch64-android/gvm
* To install APK to your Android mobile phone, you need adb. adb can be obtained by installing https://developer.android.com/tools/sdkmanager
* Make sure that you have enabled Developer mode in your Android mobile phone and enabled USB debugging after this
* Connect your Android mobile phone to computer using USB
* There should be a popup on asking for permissions in the Android device
* In command line run command: adb devices, Android device should appear in output
* Install the application by running in the command line: adb install <path to APK>
* adb logcat | grep scalaprototype

### Encountered problems and possible solutions
* GraalVM version problems:
  * Gluon only supports officially Gluon's own GraalVM forks
  * Other GraalVMs might work, but I would guess it would be easier to wait for official Gluon GraalVM CE Gluon 23+25.1
* Maven 3.9.11 seems not to be compatible with gluonfx.maven.plugin
* Problems using Maven with some dependencies:
  * Maven has problems handling Scala dependencies: 
    * Eg. [WARNING] There are 1 pathException(s). The related dependencies will be ignored.
      [WARNING] Dependency: /home/janne/.m2/repository/com/softwaremill/sttp/client4/core_3/4.0.13/core_3-4.0.13.jar
    - exception: Unable to derive module descriptor for /home/janne/.m2/repository/com/softwaremill/sttp/client4/core_3/4.0.13/core_3-4.0.13.jar
    - cause: core.3: Invalid module name: '3' is not a Java identifier
    * To fix these kind of issues use the forked version of the scala-suffix-maven-plugin (https://github.com/hyttijan92/scala-suffix). The forked version is able to handle transitive Scala dependencies.
* During native image building, the following errors might occur:
  * Some libraries, which use reflection need further configuration (https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/):
    * To collect necessary metadata for the application and to generate metadata JSONs, before generating native image: run mvn gluonfx:runagent
  * Linux kernel issues:
    * Fatal error: java.lang.NullPointerException: Cannot invoke "jdk.internal.platform.CgroupInfo.getMountPoint()" because "anyController" is null
  * Build might fail with error 137, this indicates that the computer has run out of RAM during image building. To fix this issue close all other applications or if this does not solve the problem add more RAM to your computer.
    
