package testdata;

import java.util.List;

public class UploadFileDataProvider {

    public static List<UploadCase> uploadCases() {
        return List.of(
                new UploadCase(UploadFiles.doc(), true, "DOC file < 2Mb is accepted", ""),
                new UploadCase(UploadFiles.docx(), true, "DOCX file < 2Mb is accepted", ""),
                new UploadCase(UploadFiles.pdf1mb(), true, "PDF file < 2Mb is accepted", ""),
                new UploadCase(UploadFiles.pdf2mb(), true, "PDF file = 2Mb is accepted", ""),
                new UploadCase(UploadFiles.doc0mb(), true, "DOC file = 0 is accepted",""),
                new UploadCase(UploadFiles.doc3mb(), false, "DOC file > 3Mb is not accepted", "Please upload a smaller file (2 MB or less). Change file"),
                new UploadCase(UploadFiles.png(), false, "PNG file is not accepted", "Please upload an acceptable document format (DOC, DOCX, PDF). Change file"));

    }
}
