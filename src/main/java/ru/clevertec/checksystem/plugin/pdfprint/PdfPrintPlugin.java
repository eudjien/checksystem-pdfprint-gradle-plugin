package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.tasks.JavaExec;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdfPrintPlugin implements Plugin<Project> {

    private final static String CHILD_CLI_PROJECT_NAME = "checksystem-cli";
    private final static String RUN_TASK_NAME = "run";
    private final static String EXTENSION_NAME = "pdfPrintSettings";
    private final static String PRINT_TASK_NAME = "pdfprint";

    private static void showPrintInfo(PdfPrintPluginExtension extension) {

        System.out.println("PDF print info:");
        if (extension.useTemplate) {
            System.out.println("  Template url:         " + extension.templateUrl);
            System.out.println("  Template output path: " + extension.templateOutput);
            System.out.println("  Template top offset:  " + extension.topOffset);
        }
        System.out.println("  Input file format:    " + extension.inputFileFormat);
        System.out.println("  Input file path:      " + extension.inputFilePath);
        System.out.println("  Output check path:    " + extension.outputPdfPath);
    }

    private static List<String> createArgs(PdfPrintPluginExtension extension) {

        var args = new ArrayList<String>();
        args.add("-mode=file-deserialize");
        args.add("-file-deserialize-format=" + extension.inputFileFormat);
        args.add("-file-deserialize-path=" + extension.inputFilePath);
        args.add("-file-print=1");
        args.add("-file-print-format=pdf");
        args.add("-file-print-path=" + extension.outputPdfPath);

        if (extension.useTemplate) {
            args.add("-file-print-pdf-template=" + extension.templateOutput + "|" + extension.topOffset);
        }

        return args;
    }

    private static void setTemplateOutputOrDefault(PdfPrintPluginExtension extension, Project project) {

        if (extension.useTemplate) {
            if (extension.templateOutput == null || extension.templateOutput.isBlank()) {
                extension.templateOutput =
                        Paths.get(project.getRootDir().toString(), "template.pdf")
                                .toString();
            }
        }
    }

    public void apply(Project project) {

        var extension = project.getExtensions()
                .create(EXTENSION_NAME, PdfPrintPluginExtension.class);

        setTemplateOutputOrDefault(extension, project);
        project.evaluationDependsOnChildren();

        var dlFileTaskProvider = project.getTasks()
                .register(
                        "downloadFile",
                        DownloadFileTask.class,
                        task -> {
                            task.url = extension.templateUrl;
                            task.outputPath = extension.templateOutput;
                        });

        var projectWithRunTask = project.getChildProjects()
                .getOrDefault(CHILD_CLI_PROJECT_NAME, project);

        var runTaskProvider = projectWithRunTask
                .getTasks().named(RUN_TASK_NAME, JavaExec.class, t -> {
                    showPrintInfo(extension);
                    t.setArgs(createArgs(extension));
                });

        project.getTasks().register(PRINT_TASK_NAME, task -> {
            if (extension.useTemplate) {
                task.dependsOn(dlFileTaskProvider);
            }
            task.dependsOn(runTaskProvider);
        });
    }
}
