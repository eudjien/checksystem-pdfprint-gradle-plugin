package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;

import java.util.ArrayList;
import java.util.List;

public class PdfPrintPlugin implements Plugin<Project> {

    private final static String EXTENSION_NAME = "pdfprint";
    private final static String PRINT_TASK_NAME = "pdfprint";
    private final static String DOWNLOAD_FILE_TASK = "downloadTemplate";

    @Override
    public void apply(Project project) {

        project.getPlugins().apply("application");

        var ext = project.getExtensions().create(EXTENSION_NAME, PdfPrintPluginExtension.class);

        var downloadFileTaskTaskProvider
                = project.getTasks().register(DOWNLOAD_FILE_TASK, DownloadFileTask.class, task -> {
            task.doFirst(a -> {
                task.url = ext.templateUrl;
                task.outputPath = ext.templateOutput;
            });
        });

        var runTask = project.getTasks().named(ext.runTaskName, JavaExec.class, javaExec ->
                javaExec.doFirst(a -> javaExec.setArgs(createArgs(ext))));

        project.getTasks().register(PRINT_TASK_NAME, task -> {

            showPrintInfo(ext);

            if (ext.withTemplate) {
                task.dependsOn(downloadFileTaskTaskProvider);
            }

            task.dependsOn(runTask);

            task.doLast(a ->
                    System.out.println("[" + PRINT_TASK_NAME + "] Success! Check printed into " + ext.outputPdfPath));
        });
    }

    private static void showPrintInfo(PdfPrintPluginExtension extension) {

        System.out.println("PDF print info:");
        System.out.println("-------------------------");
        if (extension.withTemplate) {
            System.out.println("Template url:         " + extension.templateUrl);
            System.out.println("Template output path: " + extension.templateOutput);
            System.out.println("Template top offset:  " + extension.topOffset);
        }
        System.out.println("Input file format:    " + extension.inputFileFormat);
        System.out.println("Input file path:      " + extension.inputFilePath);
        System.out.println("Output check path:    " + extension.outputPdfPath);
        System.out.println("-------------------------");
    }

    private static List<String> createArgs(PdfPrintPluginExtension extension) {

        var args = new ArrayList<String>();
        args.add("-mode=file-deserialize");
        args.add("-file-deserialize-format=" + extension.inputFileFormat);
        args.add("-file-deserialize-path=" + extension.inputFilePath);
        args.add("-file-print=1");
        args.add("-file-print-format=pdf");
        args.add("-file-print-path=" + extension.outputPdfPath);

        if (extension.withTemplate) {
            args.add("-file-print-pdf-template=1");
            args.add("-file-print-pdf-template-path=" + extension.templateOutput);
            args.add("-file-print-pdf-template-offset=" + extension.topOffset);
        }

        return args;
    }
}
