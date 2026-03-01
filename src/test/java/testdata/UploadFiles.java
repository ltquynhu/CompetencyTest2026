package testdata;

import java.nio.file.Path;

import config.FrameworkConstants;

public final class UploadFiles {

    private UploadFiles() {
    }

    public static String doc() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume.doc").toString();
    }

    public static String docx() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume.docx").toString();
    }

    public static String pdf1mb() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume-1mb.pdf").toString();
    }

    public static String pdf2mb() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume-2mb.pdf").toString();
    }

    public static String doc0mb() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume-0mb.doc").toString();
    }

    public static String doc3mb() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume-3mb.doc").toString();
    }

    public static String png() {
        return Path.of(FrameworkConstants.UPLOAD_FILES_PATH, "resume.png").toString();
    }
}
