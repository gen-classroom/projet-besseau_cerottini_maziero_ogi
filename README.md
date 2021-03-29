# GEN Project | Command line static website generator

**Authors**

> Cerottini Alexandra
>
> Besseau Léonard
>
> Ogi Nicolas
>
> Maziero Marco

## About the project

The goal of this project is to train the software engineering skills of the team and create a simple but powerful command line tool that allows anyone to generate a simple static website with only a couple of markdown files and configuration files.

## Installation

Download the latest release and lanch the .jar. with `java -jar`

Executing it should produce the following result:

 ![Usage](./images/Usage.png)

## Usage

- `statique init PATH`

Initialise the static website to the given path. Make sure you have the permission to do so. For now, it creates a config file (`config.json`) and an index.md. In another iteration we might allow you to directly specifies those information.

-  `statique build PATH` 

Convert the different markdown file present in the path into hmtl files with the site and file metadata specified in the header.

- `statique clean PATH`

Remove the build folder inside the path

- `statique serve PATH`

Not yet implemented

## Build the project

```bash
mvn clean install 
```

There should be a `statique-VERSION.jar` in the `target` folder.

## Others

If you use an IDE you can add `-Dpicocli.ansi=TRUE` in the vm option in order to enable the colouring.

