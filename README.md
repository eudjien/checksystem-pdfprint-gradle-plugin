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
    id 'ru.clevertec.checksystem.plugin.pdfprint' version "1.1.7"
}

// With template or not
def withTemplate = true;

pdfprint {

    // Input
    pdfprint.inputFilePath = Paths.get("$rootDir", "examples", "checks_serialized.json")
    pdfprint.inputFileFormat = "json"
    
    // Output
    pdfprint.outputPdfPath = Paths.get("$rootDir", "CHECKS_" + (useTemplate ? "WITH" : "WITHOUT") + "_TEMPLATE.pdf")
    
    // Template
    pdfprint.templateIsUsed = withTemplate;
    pdfprint.templateUrl = "https://github.com/stebadmitriy/files/raw/main/Clevertec_Template.pdf"
    pdfprint.topOffset = 94; // На сколько чек по отношению к шаблону будет смещен вниз
    pdfprint.templateOutput = Paths.get("$rootDir", "resources", "templates", "Clevertec_Template.pdf")
}
```
