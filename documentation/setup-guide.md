# Local Server Setup Guide

> **Quick start only.** For the full picture — prerequisites, every command, all URLs, the API
> reference, project layout, database handling and troubleshooting — see [setup.md](setup.md).

This guide provides instructions on how to run the Spring Boot backend server locally for this project.

## Running the Server

You can run the application using the Maven wrapper included in the repository.

### Prerequisites

Ensure you have Java 21 or higher installed on your system (the build targets Java 21). You can
check your version by running:

```powershell
java -version
```

### Option 1: Via the Command Line

1. Open your terminal (e.g., PowerShell, Command Prompt, or bash) in the project root directory.
2. Run the following command using the Maven wrapper:

   **On Windows (PowerShell/CMD):**
   ```powershell
   ./mvnw.cmd spring-boot:run
   ```

   **On macOS/Linux:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. The application will start, by default listening on port `8080`.

### Option 2: Via IDE (VS Code / IntelliJ)

* **VS Code**:
  1. Open the workspace root directory in VS Code.
  2. Locate and open the main entry point class: [ResumeApiApplication.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/ResumeApiApplication.java).
  3. Click **Run** or press `F5` to start debugging.
* **IntelliJ IDEA**:
  1. Open the project by selecting [pom.xml](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/pom.xml).
  2. Run the application configuration or click the play button next to the main class.

## Verification

Once the server has started successfully, you can verify it by opening your browser and visiting:
* [http://localhost:8080](http://localhost:8080)
