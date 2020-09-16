# maven-plugin for Adoclist

## 目的

mavenからadocの属性を解析し、一覧化するpluginです。

## 使い方

pom.xmlのpluginsに以下を追加します。

```xml
<plugin>
    <groupId>com.example</groupId>
    <artifactId>adoclist-maven-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <configuration>
    </configuration>
    <executions>
        <execution>
            <phase>generate-resources</phase>
            <goals>
                <goal>adoclist</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
