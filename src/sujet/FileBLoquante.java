package sujet;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FileBLoquante extends LinkedBlockingQueue<CustomNode> {

    private Set<CustomNode> set = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Add only element, that is not already enqueued.
     * The method is synchronized, so that the duplicate elements can't get in during race condition.
     * @param url object to put in
     * @return true, if the queue was changed, false otherwise
     */
    @Override
    public synchronized boolean add(CustomNode url) {
        if (set.contains(url)) {
            return false;
        } else {
            set.add(url);
            return super.add(url);
        }
    }

    /**
     * Takes the element from the queue.
     * Note that no synchronization with {@link #add(Object)} is here, as we don't care about the element staying in the set longer needed.
     * @return taken element
     * @throws InterruptedException
     */
    @Override
    public CustomNode take() throws InterruptedException {
        CustomNode url = super.take();
        set.remove(url);
        return url;
    }
}