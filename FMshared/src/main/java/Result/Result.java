package Result;

public class Result {
    /**
     * Message describing result of the service.
     */
    protected String message;

    /**
     * Completion status of the service.
     */
    protected boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
