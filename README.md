### checksystem-pdfprint-plugin

> Это gradle плагин для [checksystem](https://github.com/evgnpn/checksystem), который печатает чеки в формате PDF

**Tasks**

```
gradlew pdfPrint // скачать шаблон затем произвести печать
gradlew pdfPrintInfo // вывести конфигурацию плагина (путь входного или выходного файла, url шаблона - если он есть, и т.п.)
gradlew downloadPdfTemplate // скачать шаблон (без печати)
```

**Example 'gradle.build'**

```groovy
plugins {
    // ...
    id 'ru.clevertec.checksystem.plugin.pdfprint' version "1.3.0"
}

def hasPdfTemplate = true

pdfPrint {
    
    showInfo = true
    
    inputPath = "$rootDir\\..\\examples\\serialized.json"
    inputFormat = "json"
    outputPath = "$rootDir\\resources\\checks.pdf"

    hasTemplate = hasPdfTemplate
    templateUrl = "https://github.com/stebadmitriy/files/raw/main/Clevertec_Template.pdf"
    templateTopOffset = 94
    templateOutputPath = "$rootDir\\resources\\templates\\Clevertec_Template.pdf"
}

```
