package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;

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

        var ext = project.getExtensions().create(EXTENSION_NAME, PdfPrintPluginExtension.class);

        var pdfPrintInfoTaskProvider = project.getTasks().register(
                PRINT_INFO_TASK, PdfPrintInfoTask.class, pdfPrintInfoTask -> {
                    pdfPrintInfoTask.extension = ext;
                });

        var downloadFileTaskProvider = project.getTasks().register(
                DOWNLOAD_FILE_TASK, DownloadFileTask.class, downloadFileTask -> {
                    downloadFileTask.url = ext.templateUrl;
                    downloadFileTask.outputPath = ext.templateOutputPath;
                });
        downloadFileTaskProvider.configure(a -> a.mustRunAfter(pdfPrintInfoTaskProvider));

        var runTaskProvider = project.getTasks().named(RUN_TASK_NAME, JavaExec.class, javaExec ->
                javaExec.doFirst(a -> javaExec.setArgs(createArgs(ext))));
        runTaskProvider.configure(t -> t.mustRunAfter(downloadFileTaskProvider));

        project.getTasks().register(PRINT_TASK_NAME, pdfPrintTask -> {

            pdfPrintTask.dependsOn(pdfPrintInfoTaskProvider);

            if (ext.hasTemplate)
                pdfPrintTask.dependsOn(downloadFileTaskProvider);

            pdfPrintTask.dependsOn(runTaskProvider);

            pdfPrintTask.doLast(a -> System.out.println(
                    "[" + PRINT_TASK_NAME + "] Success! Check printed into " + ext.outputPath));
        });
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
