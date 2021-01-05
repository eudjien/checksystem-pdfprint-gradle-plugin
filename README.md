### Build & run

> Это gradle плагин для [checksystem](https://github.com/evgnpn/checksystem), который печатает чеки в формате PDF

**Publish to maven local**

```
gradlew build 
gradlew publishToMavenLocal
```

**Add plugin to 'gradle.build' file**

```
plugins {
    ...
    id 'ru.clevertec.checksystem.plugin.pdfprint' version "1.0"
}
```

**Settings example**

```
// With template or not
def useTemplate = true;

// Input setup
pdfPrintSettings.inputFilePath = Paths.get("$rootDir", "examples", "checks_serialized.json")
pdfPrintSettings.inputFileFormat = "json"

// Template setup
if (useTemplate) {
    pdfPrintSettings.useTemplate = useTemplate;
    pdfPrintSettings.templateUrl = "https://github.com/stebadmitriy/files/raw/main/Clevertec_Template.pdf"
    pdfPrintSettings.topOffset = 94; // На сколько чек по отношению к шаблону будет смещен вниз
    pdfPrintSettings.templateOutput = Paths.get("$rootDir", "resources", "templates", "Clevertec_Template.pdf")
}

// Output path
pdfPrintSettings.outputPdfPath = Paths.get("$rootDir", "CHECKS_" + (useTemplate ? "WITH" : "WITHOUT") + "_TEMPLATE.pdf")

```

**Run**

```
gradlew pdfprint
```
	