# datetime-extensions
[![deprecated](https://img.shields.io/badge/deprecated-too%20stale%20to%20maintain-red.svg)](https://img.shields.io/badge/deprecated-too%20stale%20to%20maintain-red.svg)

[Joda Time](http://www.joda.org/joda-time/) based Date and Time handler.

## Getting Start
Add the following snippet to any project's pom that depends on your project
```xml
<repositories>
  ...
  <repository>
    <id>commons-wrap</id>
    <url>https://raw.github.com/furplag/datetime-extensions/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>jp.furplag.sandbox.java.util</groupId>
    <artifactId>datetime-extensions</artifactId>
    <version>[1.0,)</version>
  </dependency>
</dependencies>
```

## License
Code is under the [Apache Licence v2](LICENCE).
