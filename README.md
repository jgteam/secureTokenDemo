# SecureStore Demo App

![Maven Build](https://github.com/jgteam/secureTokenDemo/actions/workflows/maven.yml/badge.svg)

For the full setup and walkthrough, please watch the following video:

[Setup and Demo Video](https://www.youtube.com/watch?v=YQw1_svw4Eo)

This is a demo app to show how to use the "SecureStore" capabilities of different platforms and will be a Proof of Concept for storing sensitive OpenID Connect tokens securely when used with native desktop applications.

This Demo is part of a Bachelor's thesis project by Jannis Günsche from the University of Applied Sciences in Darmstadt, Germany.

## Disclaimer

Disclaimer: This app is not intended for production use and is only a proof of concept. It shows the secure storage of sensitive data in a native desktop application but does not handle all edge cases and errors that might occur in a real-world application. It also does not handle the token inside the app in a secure way, as this demo is only a proof of concept.

## Repo Structure

```
.
├── sources-cli
│   └── src
│       └── main
│           └── java
│               └── credStoreCLI
│                   └── CredStoreCLI.java
├── sources-demoApp
│   └── src
│       └── main
│           └── java
│               ├── logger
│               │   └── Logger.java
│               └── secureTokenDemo
│                   ├── App.java
│                   ├── AppLogic.java
│                   ├── AppShell.java
│                   ├── CopyCommandDialog.java
│                   ├── CredentialTracker.java
│                   ├── LogDialog.java
│                   ├── SecureStorageHandler.java
│                   └── TokenViewerDialog.java
├── target-credStoreCLI
│   └── pom.xml
├── target-platform-linux-gtk-x86_64
│   └── pom.xml
├── target-platform-macos-aarch64
│   └── pom.xml
├── target-platform-windows-x86_64
│   └── pom.xml
├── README.md
└── pom.xml
```

## Content

The Demo consists of two parts:
 1. A GUI-App written in Java which is build for Windows, macOS and Linux separately. 
 2. A CLI-App (named: `credStoreCLI`) also written in Java which is multiplatform and can be used on Windows, macOS and Linux.

The GUI-App is used to show how the data can be stored and retrieved securely.

The CLI-App is used to simulate 'a different' application that stored sensitive data securely. The GUI-App will be able to retrieve the not owned data from the CLI-App. This shows that saved credentials can be shared between different applications.

## Getting Started

This project is build with Maven.

The GUI-App as well as the CLI-App are automatically build by GitHub Actions and the artifacts are available for download in the 'Actions' tab of this repository. To be able to download the artifacts, you need to be logged in to GitHub.

To build the Artifacts yourself, you can use the following commands after cloning the repository:

```shell
maven clean install
```

The jar files will be located in the target folders of the different targets (E. g. `target-platform-linux-gtk-x86_64/target/target-platform-linux-gtk-x86_64-1.0.jar`).

To run the jar-files, you can use the following command:

```shell
java -jar <your-jar-filename>.jar
```

In some cases of the GUI-App, you need to provide additional Java VM Flags to run the app correctly because of the GUI SWT-Library. You can do this by executing the following command:

```shell
java -XstartOnFirstThread -jar <your-gui-jar-filename>.jar
```

To pass `credential-key` and `token` to the CLI-App, you can use the following command:

```shell
java -jar <your-cli-jar-filename>.jar <your-credential-key> <your-token>
```


## Measuring time

To measure the time you can press the separate measure button in the App. This will measure retrieving the token 100 times and log the time simultaneously. The time is measured in milliseconds and can be seen in the time measurement dialog. To open the time measurement dialog, you can press the "View Time Measurements" button in the App.
