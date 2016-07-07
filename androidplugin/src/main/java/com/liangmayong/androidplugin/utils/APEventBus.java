package com.liangmayong.androidplugin.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.AbstractSequentialList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * APEventBus
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APEventBus {

    private static Map<String, APEventBus> eventMap = new HashMap<String, APEventBus>();
    private static String DEFAULT_EVENT = "ANDROID_PLUGIN_EVENT_BUS";

    /**
     * get default event bus
     *
     * @return APEventBus
     */
    public static APEventBus getDefault() {
        return getEvent(DEFAULT_EVENT);
    }

    /**
     * get event bus
     *
     * @param eventName eventName
     * @return APEventBus
     */
    public static APEventBus getEvent(String eventName) {
        if (eventMap.containsKey(eventName)) {
            return eventMap.get(eventName);
        }
        APEventBus event = new APEventBus();
        eventMap.put(eventName, event);
        return event;
    }

    private APEventBus() {
    }

    private final int WHAT = 0x01;
    private WeakList<Object> objects = new WeakList<Object>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                EventObj obj = (EventObj) msg.obj;
                switch (msg.what) {
                    case WHAT:
                        for (Object observer : objects) {
                            try {
                                methoder(observer, "doEvent", int.class, String.class, Object.class).invoke(obj.getId(),
                                        obj.getAction(), obj.getObj());
                            } catch (Exception e) {
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }
    };

    /**
     * unregister event
     *
     * @param subscriber subscriber
     */
    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        if (objects.contains(subscriber)) {
            objects.remove(subscriber);
        }
    }

    /**
     * register event
     *
     * @param subscriber subscriber
     */
    public void register(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        if (!objects.contains(subscriber)) {
            objects.add(subscriber);
        }
    }

    /**
     * post event
     *
     * @param id     id
     * @param action action
     * @param obj    obj
     */
    public void post(int id, String action, Object obj) {
        handler.obtainMessage(WHAT, new EventObj(id, action, obj)).sendToTarget();
    }

    /**
     * postDelayed
     *
     * @param id          id
     * @param action      action
     * @param obj         obj
     * @param delayMillis delayMillis
     */
    public void postDelayed(final int id, final String action, final Object obj, int delayMillis) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                post(id, action, obj);
            }
        }, delayMillis);
    }

    /**
     * postAtTime
     *
     * @param id           id
     * @param action       action
     * @param obj          obj
     * @param uptimeMillis uptimeMillis
     */
    public void postAtTime(final int id, final String action, final Object obj, int uptimeMillis) {
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                post(id, action, obj);
            }
        }, uptimeMillis);
    }

    private static class EventObj {
        private int id;
        private String action;
        private Object obj;

        public EventObj(int id, String action, Object obj) {
            this.id = id;
            this.action = action;
            this.obj = obj;
        }

        public int getId() {
            return id;
        }

        public String getAction() {
            return action;
        }

        public Object getObj() {
            return obj;
        }

    }

    /**
     * IEvent
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static interface IEvent {
        void doEvent(int id, String action, Object obj);
    }

    private Methoder methoder(Object object, String method, Class<?>... parameterTypes) {
        return new Methoder(object.getClass(), object, method, parameterTypes);
    }

    /**
     * Methoder
     *
     * @author LiangMaYong
     * @version 1.0
     */
    private static final class Methoder {

        private Method method;
        private Object object;

        private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
            Method method = null;
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    method = clazz.getDeclaredMethod(name, parameterTypes);
                    return method;
                } catch (Exception e) {
                }
            }
            return null;
        }

        private Methoder(Class<?> cls, Object object, String method, Class<?>... parameterTypes) {
            try {
                this.object = object;
                this.method = getDeclaredMethod(cls, method, parameterTypes);
            } catch (Exception e) {
            }
        }

        public Object invoke(Object... args) throws Exception {
            if (method != null) {
                method.setAccessible(true);
                Object object = method.invoke(this.object, args);
                method = null;
                this.object = null;
                return object;
            }
            return null;
        }
    }

    /**
     * weak list
     *
     * @param <E> e
     * @author LiangMaYong
     * @version 1.0
     */
    public static class WeakList<E> extends AbstractSequentialList<E> implements Queue<E> {

        private ReferenceQueue<E> queue = new ReferenceQueue<E>();

        private Entry root = new Entry();

        private int size = 0;

        private class Entry extends WeakReference<E> {

            private Entry prev, next;

            private Entry() {
                super(null, null);
                this.prev = this;
                this.next = this;
            }

            private Entry(E referent, Entry next, Entry prev) {
                super(referent, queue);
                prev.next = this;
                this.prev = prev;
                this.next = next;
                next.prev = this;
            }

            private void remove() {
                prev.next = this.next;
                next.prev = this.prev;

                this.prev = null;
                this.next = null;
            }
        }

        private Entry addBefore(E e, Entry entry) {
            Entry newEntry = new Entry(e, entry, entry.prev);
            size++;
            modCount++;
            return newEntry;
        }

        private E remove(Entry e) {
            if (e == root) {
                throw new NoSuchElementException();
            }

            E result = e.get();
            e.remove();
            size--;
            modCount++;
            return result;
        }

        @SuppressWarnings("unchecked")
        private void expungeStaleEntries() {
            if (size == 0) {
                return;
            }
            for (; ; ) {
                Entry e = (Entry) queue.poll();
                if (e == null) {
                    break;
                }
                remove(e);
            }
        }

        private Entry find(E element) {
            for (Entry e = root.next; e != root; e = e.next) {
                if (element.equals(e.get()))
                    return e;
            }
            return null;
        }

        /* private */Entry find(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            Entry e = root;
            if (index < size / 2) {
                for (int i = 0; i <= index; i++)
                    e = e.next;
            } else {
                for (int i = size; i > index; i--)
                    e = e.prev;
            }
            return e;
        }

        //
        //
        //

        public void addFirst(E e) {
            if (e == null) {
                throw new IllegalArgumentException();
            }
            expungeStaleEntries();
            addBefore(e, root.next);
        }

        public void addLast(E e) {
            if (e == null) {
                throw new IllegalArgumentException();
            }
            expungeStaleEntries();
            addBefore(e, root);
        }

        public E getFirst() {
            expungeStaleEntries();
            if (size == 0) {
                throw new NoSuchElementException();
            }
            return root.next.get();
        }

        public E getLast() {
            expungeStaleEntries();
            if (size == 0) {
                throw new NoSuchElementException();
            }
            return root.prev.get();
        }

        public E removeFirst() {
            expungeStaleEntries();
            return remove(root.next);
        }

        public E removeLast() {
            expungeStaleEntries();
            return remove(root.prev);
        }

        @Override
        public boolean contains(Object o) {
            if (o == null) {
                return false;
            }
            expungeStaleEntries();
            @SuppressWarnings("unchecked")
            E e = (E) o;
            return find(e) != null;
        }

        public int size() {
            expungeStaleEntries();
            return size;
        }

        @Override
        public boolean add(E e) {
            if (e == null) {
                throw new IllegalArgumentException();
            }
            expungeStaleEntries();
            addBefore(e, root);
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (o != null) {
                expungeStaleEntries();

                @SuppressWarnings("unchecked")
                Entry e = find((E) o);
                if (e != null) {
                    remove(e);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void clear() {
            for (Entry e = root.next; e != root; ) {
                Entry next = e.next;
                remove(e);
                e = next;
            }
            root.next = root.prev = root;
            size = 0;
            modCount++;
        }

        //
        //
        //

        @Override
        public Iterator<E> iterator() {
            expungeStaleEntries();
            return new Itr();
        }

        private class Itr implements Iterator<E> {

            private Entry current = root.next, lastRet = null;

            private int expectedModCount = modCount;

            public boolean hasNext() {
                return current != root;
            }

            public E next() {
                checkForComodification();
                lastRet = current;
                current = current.next;
                return lastRet.get();
            }

            public void remove() {
                if (lastRet == null) {
                    throw new IllegalStateException();
                }
                checkForComodification();
                WeakList.this.remove(lastRet);
                lastRet = null;
                expectedModCount = modCount;
            }

            final void checkForComodification() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
            }
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return new ListItr(index);
        }

        private class ListItr implements ListIterator<E> {
            private Entry lastReturned = root;

            private Entry next;

            private int nextIndex;

            private int expectedModCount = modCount;

            ListItr(int index) {
                if (index < 0 || index > size) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
                }
                if (index < size / 2) {
                    next = root.next;
                    for (nextIndex = 0; nextIndex < index; nextIndex++)
                        next = next.next;
                } else {
                    next = root;
                    for (nextIndex = size; nextIndex > index; nextIndex--)
                        next = next.prev;
                }
            }

            public boolean hasNext() {
                return nextIndex != size;
            }

            public E next() {
                checkForComodification();
                if (nextIndex == size)
                    throw new NoSuchElementException();

                lastReturned = next;
                next = next.next;
                nextIndex++;
                return lastReturned.get();
            }

            public boolean hasPrevious() {
                return nextIndex != 0;
            }

            public E previous() {
                if (nextIndex == 0)
                    throw new NoSuchElementException();

                lastReturned = next = next.prev;
                nextIndex--;
                checkForComodification();
                return lastReturned.get();
            }

            public int nextIndex() {
                return nextIndex;
            }

            public int previousIndex() {
                return nextIndex - 1;
            }

            public void remove() {
                checkForComodification();
                Entry lastNext = lastReturned.next;
                try {
                    WeakList.this.remove(lastReturned);
                } catch (NoSuchElementException e) {
                    throw new IllegalStateException();
                }
                if (next == lastReturned)
                    next = lastNext;
                else
                    nextIndex--;
                lastReturned = root;
                expectedModCount++;
            }

            public void set(E e) {
                throw new UnsupportedOperationException();
            }

            public void add(E e) {
                checkForComodification();
                lastReturned = root;
                addBefore(e, next);
                nextIndex++;
                expectedModCount++;
            }

            final void checkForComodification() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
            }
        }

        //
        // Queue
        //

        public E peek() {
            expungeStaleEntries();
            if (size == 0) {
                return null;
            }
            return root.next.get();
        }

        public E element() {
            return getFirst();
        }

        public E poll() {
            expungeStaleEntries();
            if (size == 0)
                return null;
            return remove(root.next);
        }

        public E remove() {
            return removeFirst();
        }

        public boolean offer(E e) {
            return add(e);
        }

    }
}
