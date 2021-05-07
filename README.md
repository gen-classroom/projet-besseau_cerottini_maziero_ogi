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

![Init_structure](README/images/Init_structure.png)

-  `statique build PATH` 

Convert the different markdown file present in the path into hmtl files with the site and file metadata specified in the header.

- `statique clean PATH`

Remove the build folder inside the path

- `statique serve PATH`

Not yet implemented

## Adding content to your site

### Reserved folder

`build` and `template` folders inside your site folder are special folder and any md file inside will be ignored. The `build` folder is use as the output for the conversion for the file and the `template` folder is used to stored template for your site.

### Template usage

You can use [handlebars template](https://handlebarsjs.com/) to describe a template for your page.

A default template is automatically applied to any file with no template specified. The default template is located in `template/default.html`

#### Using a custom template

You can define your own template by adding it inside the `template` folder. The file must be a `.html` file.

### Inserting content inside a template

To specify where to insert content inside your template, you must use `{{INSERT}}`.

Inside the curly brackets, you can specify which properties to display.

For example, if you want to access to your global meta-data (defined in `config.json`) you can use `{{site:member}}` where member is the property you want to access. Otherwise if you want to access your file meta-data you can use `{{page:member}}`. Finally, to display the content of your page, use `{{md content}}`.

### Link to another file

The statique generator supports links to other file. Furthermore link to other md files will be transformed to link to html file. Example:

`[Another page](./test/page.md)` will be tranformed into `<a href="./test/page.html">Another page</a>`

### Limitations

There must be no spaces between the curly brackets. Example: `{{ site.content }}` will not work

## Build the project

```bash
mvn clean install 
```

There should be a `statique.jar` in the `target` folder.

## Others

If you use an IDE, you can add `-Dpicocli.ansi=TRUE` in the vm option in order to enable the colouring in your terminal.

