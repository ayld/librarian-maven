Librarian
=========

Tools to track/update your Maven dependencies.

### Overview

This is just a Maven plugin for now. It traverses some repositories and sites to find out whether you're using
the latest version of your dependecies and updates them for you if you want to.


### Usage

#### Info Mojo

Tells you what percentage of your library is outdated (with the Maven logs) and creates a JSON file in your project root dir
containing the info.

Used like this:

`mvn librarian.plugin:librarian-maven-plugin:info`

I know its too long to type, will be shorter soon.

#### Update Mojo

Comming Soon™.
