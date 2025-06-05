@ECHO OFF
SET DIR=%~dp0
SET GRADLE_JAR=%DIR%\gradle\wrapper\gradle-wrapper.jar
IF NOT EXIST "%GRADLE_JAR%" (
  ECHO Gradle wrapper JAR not found. Please install gradle.
  EXIT /B 1
)
java -jar "%GRADLE_JAR%" %*
