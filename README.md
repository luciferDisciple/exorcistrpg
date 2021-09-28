# Exorcist RPG

A simple Java dungeon crawling game with a text-based user interface (TUI)
implemented with [lanterna](https://github.com/mabe02/lanterna) library.

## Requirements

- JDK 16

## Building

To create a standalone JAR execute:

```
$ mvn clean package
```

If it succeedes, maven should create a file named
`target/ExorcistRPG-${version}-jar-with-dependencies.jar`.

## Running the application

```
$ java -jar target/ExorcistRPG-${version}-jar-with-dependencies.jar
```
