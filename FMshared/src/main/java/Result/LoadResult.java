package Result;

/**
 * Result of loading request.
 */
public class LoadResult extends Result {

    /**
     * Constructs a new Result object to describe the load completion.
     *
     * @param success Completion status of the load.
     * @param message Message describing the completion of the load.
     */
    public LoadResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
