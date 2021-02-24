package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskProvider;

import java.util.ArrayList;
import java.util.List;

public class PdfPrintPlugin implements Plugin<Project> {

    private final static String RUN_TASK_NAME = "bootRun";
    private final static String EXTENSION_NAME = "pdfPrint";
    private final static String PRINT_TASK_NAME = "pdfPrint";
    private final static String DOWNLOAD_FILE_TASK = "downloadPdfTemplate";
    private final static String PRINT_INFO_TASK = "pdfPrintInfo";

    @Override
    public void apply(Project project) {

        project.getPlugins().apply("application");

        var extension = project.getExtensions()
                .create(EXTENSION_NAME, PdfPrintPluginExtension.class);

        var runTask = project.getTasks().getByName(RUN_TASK_NAME);

        var infoProvider = registerInfoTask(project, extension);
        var downloadProvider = registerDownloadTask(project, extension, infoProvider);

        registerPrintTask(project, extension, runTask, infoProvider, downloadProvider);
    }

    private static void registerPrintTask(
            Project project,
            PdfPrintPluginExtension extension,
            Task runTask,
            TaskProvider<PdfPrintInfoTask> infoProvider,
            TaskProvider<DownloadFileTask> downloadProvider) {

        project.getTasks().register(PRINT_TASK_NAME, t -> {

            if (extension.showInfo)
                t.dependsOn(infoProvider);

            if (extension.hasTemplate)
                t.dependsOn(downloadProvider);

            t.dependsOn(configureRunTask(runTask, extension, downloadProvider));
            t.doLast(a -> System.out.println(successPrintMessage(extension)));
        });
    }

    private static TaskProvider<PdfPrintInfoTask> registerInfoTask(
            Project project, PdfPrintPluginExtension extension) {

        return project.getTasks().register(
                PRINT_INFO_TASK, PdfPrintInfoTask.class, pdfPrintInfoTask -> pdfPrintInfoTask.extension = extension);
    }

    private static TaskProvider<DownloadFileTask> registerDownloadTask(
            Project project, PdfPrintPluginExtension extension, TaskProvider<? extends Task> beforeTask) {

        var provider = project.getTasks().register(
                DOWNLOAD_FILE_TASK, DownloadFileTask.class, downloadFileTask -> {
                    downloadFileTask.url = extension.templateUrl;
                    downloadFileTask.outputPath = extension.templateOutputPath;
                });

        if (beforeTask != null)
            provider.configure(a -> a.mustRunAfter(beforeTask));

        return provider;
    }

    private static Task configureRunTask(
            Task runTask, PdfPrintPluginExtension extension, TaskProvider<? extends Task> beforeTask) {

        return runTask.doFirst(t -> ((JavaExec) t).setArgs(createArgs(extension)))
                .mustRunAfter(beforeTask);
    }

    private static String successPrintMessage(PdfPrintPluginExtension extension) {
        return "[" + PRINT_TASK_NAME + "] Success! Check printed into " + extension.outputPath;
    }

    private static List<String> createArgs(PdfPrintPluginExtension extension) {

        var args = new ArrayList<String>();
        args.add("-i=deserialize");
        args.add("-deserialize-format=" + extension.inputFormat);
        args.add("-deserialize-path=" + extension.inputPath);
        args.add("-print=true");
        args.add("-print-format=pdf");
        args.add("-print-path=" + extension.outputPath);

        if (extension.hasTemplate) {
            args.add("-print-pdf-template=1");
            args.add("-print-pdf-template-path=" + extension.templateOutputPath);
            args.add("-print-pdf-template-offset=" + extension.templateTopOffset);
        }

        return args;
    }
}
