# Spring Boot Setup Guide for Resume Project

This guide provides step-by-step instructions to set up a Spring Boot application within your workspace. Since your workspace is currently empty, we will initialize a new Spring Boot project.

---

## 1. Prerequisites

Before starting, ensure you have the following installed on your system:
* **Java Development Kit (JDK)**: JDK 17 or JDK 21 is highly recommended for modern Spring Boot applications.
  * Check your version by running: `java -version`
* **Build Tool**: Maven (recommended for beginners) or Gradle.
* **IDE / Text Editor**: 
  * **VS Code** with the **Spring Boot Extension Pack** and **Extension Pack for Java** installed.
  * *Or* **IntelliJ IDEA** (Community or Ultimate edition).

---

## 2. Generating the Spring Boot Project

The easiest way to initialize a Spring Boot project is using **Spring Initializr**.

1. Go to [start.spring.io](https://start.spring.io/).
2. Configure the metadata:
   * **Project**: Maven (or Gradle if preferred)
   * **Language**: Java
   * **Spring Boot**: Choose the latest stable version (e.g., `3.x.x`, avoid `SNAPSHOT` or `M` versions).
   * **Group**: `com.renzmapa`
   * **Artifact**: `resume-api` (or `resume`)
   * **Name**: `resume-api`
   * **Description**: Resume backend API using Spring Boot
   * **Package name**: `com.renzmapa.resume`
   * **Packaging**: Jar
   * **Java**: `17` or `21` (matching your installed JDK)
3. **Dependencies**: Add the following initial dependencies:
   * **Spring Web**: Builds RESTful APIs using Spring MVC.
   * **Spring Boot DevTools**: Provides fast application restarts and LiveReload.
   * **Lombok** (Optional): Reduces boilerplate code (e.g., getters, setters, constructors).
4. Click **Generate** to download the project as a `.zip` file.
5. Extract the `.zip` contents directly into your workspace:
   `c:\Users\Renz\Personal Projects\Renz Mapa - Resume\`

---

## 3. Directory Structure

Once extracted, your workspace should look like this:

```text
Renz Mapa - Resume/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── renzmapa/
│   │   │           └── resume/
│   │   │               └── ResumeApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## 4. Building and Running the Application

You can build and run the application using the Maven wrapper included in the project.

### Via Command Line
Open a terminal in the project root directory and run:

* **To compile and run**:
  ```powershell
  ./mvnw spring-boot:run
  ```
* **To package the application (creates a JAR in the `target` folder)**:
  ```powershell
  ./mvnw clean package
  ```

### Via VS Code
1. Open the project folder in VS Code.
2. If the Java extensions are installed, they will import the Maven project automatically.
3. Locate `ResumeApplication.java` under `src/main/java/com/renzmapa/resume/`.
4. Click the **Run** button above the `main` method, or press `F5` to start debugging.

The server should start on port `8080` by default. You can verify it by opening `http://localhost:8080` in your browser (you will see a default 404 page, which is normal since no endpoints are defined yet).

---

## 5. Creating a Simple Test Controller

To verify that the setup is working correctly, let's create a simple REST controller.

1. Create a new file under `src/main/java/com/renzmapa/resume/controller/HelloController.java`.
2. Add the following Java code:

```java
package com.renzmapa.resume.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String sayHello() {
        return "Hello, Renz Mapa! Your Spring Boot Resume backend is running successfully!";
    }
}
```

3. Restart the Spring Boot application.
4. Navigate to `http://localhost:8080/api/hello` in your browser. You should see the welcome message.
