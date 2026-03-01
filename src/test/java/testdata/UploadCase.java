package testdata;

public class UploadCase {
    private final String filePath;
    private final boolean valid;
    private final String caseName;
    private final String expectedMessage;

    public UploadCase(String filePath, boolean valid, String caseName, String expectedMessage) {
        this.filePath = filePath;
        this.valid = valid;
        this.caseName = caseName;
        this.expectedMessage = expectedMessage;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isValid() {
        return valid;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getExpectedMessage() {
        return expectedMessage;
    }
}
