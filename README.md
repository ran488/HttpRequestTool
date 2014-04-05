HttpRequestTool
===============

Simple command line tool to post form data to an HTTP endpoint (e.g. servlet, JSP).

Single Groovy script that sends requests to an HTTP endpoint. It recursively reads a directory looking for "request" files. As it finds them, it created request parameters for each key/value pair in the file and posts the request to the URL.

to generate distributions that can be un-archived on a system...

% gradlew distZip
or
% gradlew distTar

or to simply run....

% gradlew run
