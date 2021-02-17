### checksystem-pdfprint-plugin

> Это gradle плагин для [checksystem](https://github.com/evgnpn/checksystem), который печатает чеки в формате PDF

**Run**

```
gradlew pdfprint
```

**Example 'gradle.build'**

```groovy
plugins {
    // ...
    id 'ru.clevertec.checksystem.plugin.pdfprint' version "1.2.0"
}

def hasPdfTemplate = true

pdfPrint {
    inputPath = "$rootDir\\..\\examples\\serialized.json"
    inputFormat = "json"
    outputPath = "$rootDir\\resources\\checks.pdf"

    hasTemplate = hasPdfTemplate
    templateUrl = "https://github.com/stebadmitriy/files/raw/main/Clevertec_Template.pdf"
    templateTopOffset = 94
    templateOutputPath = "$rootDir\\resources\\templates\\Clevertec_Template.pdf"
}

```
