package Result;

/**
 * Result object with info about the result from clearing the database.
 */
public class ClearResult extends Result {

    /**
     * Constructs a new clearing report.
     * @param message Describes the clear process.
     * @param success Describes the completion status.
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
