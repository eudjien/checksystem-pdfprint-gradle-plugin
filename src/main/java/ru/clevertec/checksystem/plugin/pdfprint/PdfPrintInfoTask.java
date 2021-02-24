package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class PdfPrintInfoTask extends DefaultTask {

    public PdfPrintPluginExtension extension;

    @TaskAction
    public void run() {
        showPrintInfo(extension);
    }

    private static void showPrintInfo(PdfPrintPluginExtension extension) {
        System.out.println("PDF print info:");
        System.out.println("-------------------------");
        if (extension.hasTemplate) {
            System.out.println("Template url:         " + extension.templateUrl);
            System.out.println("Template output path: " + extension.templateOutputPath);
            System.out.println("Template top offset:  " + extension.templateTopOffset);
        }
        System.out.println("Input file format:    " + extension.inputFormat);
        System.out.println("Input file path:      " + extension.inputPath);
        System.out.println("Output check path:    " + extension.outputPath);
        System.out.println("-------------------------");
    }
}
