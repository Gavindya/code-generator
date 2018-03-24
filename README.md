# MSstack (A full stack micro services framework for Java)
[![Build Status](https://www.travis-ci.org/msstack/code-generator.svg?branch=master)](https://www.travis-ci.org/msstack/code-generator)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/92ae77e01ce244ff963cdfa87100d165)](https://www.codacy.com/app/grydtech/code-generator?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=msstack/code-generator&amp;utm_campaign=Badge_Grade)

## About this project
This is the code generator of msstack  
Generate business model into a code base which can be used to deployed in multiple micro services

>Note: For further informaions about MSstack [msstack.grydtech.com](http://msstack.grydtech.com)

## Build from sources

### Install maven
1. You can download maven from [here](https://maven.apache.org/download.cgi)
2. You can find installation instruction [here](https://maven.apache.org/install.html)

### Clone and install dependencies
Clone repository to your local machine (assume you already installed git if not please install)
```bash
git clone https://github.com/msstack/code-generator.git
mvn clean install
```

## Run
1. Create business model according to json schema
2. Run jar executable giving parameters
```bash
java -jar codegenerator-1.0.jar "<model.json>" "<output folder>" "<group id>" "<version>"
```
