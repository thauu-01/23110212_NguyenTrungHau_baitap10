package baitap10.exception;

public class StorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {  // Sửa từ Exception thành Throwable
        super(message, cause);
    }
}