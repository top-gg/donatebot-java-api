# Donate Bot Java API
A Donate Bot API client for Java

[![](https://jitpack.io/v/top-gg/donatebot-java-api.svg)](https://jitpack.io/#top-gg/donatebot-java-api)

## Installation
Replace `VERSION` with the latest version or commit hash. The latest version can be found under [releases](https://github.com/top-gg/donatebot-java-api/releases). You can also add the JAR from releases into your Java project.
#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.top-gg</groupId>
        <artifactId>donatebot-java-api</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```
#### Gradle 
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
```gradle
dependencies {
        implementation 'com.github.top-gg:donatebot-java-api:VERSION'
}
```

## Documentation
[Click here to read the documentation for this API client.](https://developers.donatebot.io/api-libraries/java)

## Usage

```java
import io.donatebot.api.*;

public static void main(String[] args)  {
    DBClient dbClient = new DBClient("My Discord Server ID", "My API Key");
}
```
