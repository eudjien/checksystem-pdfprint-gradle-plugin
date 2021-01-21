package ru.clevertec.checksystem.plugin.pdfprint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadFileTask extends DefaultTask {

    public String url;
    public String outputPath;

    @TaskAction
    public void run() {
        System.out.println("Downloading file...");

        try {
            downloadFile(outputPath);
            System.out.println("Download completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String destinationPath) throws IOException {
        var fileUrl = new URL(url);
        try (var is = fileUrl.openStream()) {
            Files.createDirectories(Paths.get(destinationPath).getParent());
            Files.copy(is, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
