package uapi.task.internal;

/**
 * A buffer which can be only read item
 * 
 * @author min
 */
public interface IReadableBuffer<T> {

    /**
     * Read an item from buffer, it will return null if the buffer
     * is empty or the buffer is locked for writing item.
     * 
     * @return  An item or null
     */
    T read();

    /**
     * Read an item from buffer, it will be blocked if the buffer
     * is empty or the buffer is locked for writing item when the
     * isWait is set to true, its behavior is not different as read()
     * method if the isWait is set false.
     * 
     * @param   isWait
     *          Wait or do not wait if no item in the buffer or the buffer
     *          is locked for writing
     * @return  The item
     * @throws  InterruptedException
     *          When it is wait for available item or waiting for the buffer
     *          can be read
     */
    T read(boolean isWait) throws InterruptedException;
}
