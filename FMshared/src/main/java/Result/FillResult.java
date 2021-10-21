package Result;

/**
 * Result object with info about filling the database with ancestral data.
 */
public class FillResult extends Result {

    /**
     * Constructs new Fill completion report.
     * @param message Describes the completion of the fill request.
     */
    public FillResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
