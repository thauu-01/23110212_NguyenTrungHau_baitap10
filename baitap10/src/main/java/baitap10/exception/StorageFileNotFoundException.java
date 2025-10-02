package baitap10.exception;

public class StorageFileNotFoundException extends StorageException {

    private static final long serialVersionUID = 1L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {  // Add this constructor
        super(message, cause);
    }
}