package com.ilexiconn.llibrary.server.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * Generic implementation of com.sun.jna.WeakIdentityHashMap
 *
 * @author pau101
 */
public class WeakIdentityHashMap<K, V> implements Map<K, V> {
    private final ReferenceQueue<K> queue = new ReferenceQueue<>();

    private final Map<IdentityWeakReference, V> delegate;

    public WeakIdentityHashMap(int expectedMaxSize, float loadFactor) {
        this.delegate = new HashMap<>(expectedMaxSize, loadFactor);
    }

    public WeakIdentityHashMap(int expectedMaxSize) {
        this.delegate = new HashMap<>(expectedMaxSize);
    }

    public WeakIdentityHashMap() {
        this.delegate = new HashMap<>();
    }

    @Override
    public int size() {
        this.reap();
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        this.reap();
        return this.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        this.reap();
        return this.delegate.containsKey(new IdentityWeakReference((K) key));
    }

    @Override
    public boolean containsValue(Object value) {
        this.reap();
        return this.delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        this.reap();
        return this.delegate.get(new IdentityWeakReference((K) key));
    }

    @Override
    public V put(K key, V value) {
        return this.delegate.put(new IdentityWeakReference(key), value);
    }

    @Override
    public V remove(Object key) {
        this.reap();
        return this.delegate.remove(new IdentityWeakReference((K) key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        this.delegate.clear();
        this.reap();
    }

    @Override
    public Set<K> keySet() {
        this.reap();
        return new AbstractSet<K>() {
            @Override
            public int size() {
                WeakIdentityHashMap.this.reap();
                return WeakIdentityHashMap.this.delegate.size();
            }

            @Override
            public Iterator<K> iterator() {
                WeakIdentityHashMap.this.reap();
                Iterator<IdentityWeakReference> iter = WeakIdentityHashMap.this.delegate.keySet().iterator();
                return new Iterator<K>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public K next() {
                        return iter.next().get();
                    }

                    @Override
                    public void remove() {
                        iter.remove();
                    }
                };
            }

            @Override
            public void clear() {
                WeakIdentityHashMap.this.delegate.clear();
                WeakIdentityHashMap.this.reap();
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public int size() {
                WeakIdentityHashMap.this.reap();
                return WeakIdentityHashMap.this.delegate.size();
            }

            @Override
            public boolean contains(Object o) {
                WeakIdentityHashMap.this.reap();
                return WeakIdentityHashMap.this.containsValue(o);
            }

            @Override
            public Iterator<V> iterator() {
                WeakIdentityHashMap.this.reap();
                return WeakIdentityHashMap.this.delegate.values().iterator();
            }

            @Override
            public void clear() {
                WeakIdentityHashMap.this.delegate.clear();
                WeakIdentityHashMap.this.reap();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        this.reap();
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public int size() {
                WeakIdentityHashMap.this.reap();
                return WeakIdentityHashMap.this.delegate.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                WeakIdentityHashMap.this.reap();
                Iterator<Entry<IdentityWeakReference, V>> iter = WeakIdentityHashMap.this.delegate.entrySet().iterator();
                return new Iterator<Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        Entry<IdentityWeakReference, V> entry = iter.next();
                        return new Entry<K, V>() {
                            @Override
                            public K getKey() {
                                return entry.getKey().get();
                            }

                            @Override
                            public V getValue() {
                                return entry.getValue();
                            }

                            @Override
                            public V setValue(V value) {
                                return WeakIdentityHashMap.this.delegate.put(entry.getKey(), value);
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        iter.remove();
                    }
                };
            }

            @Override
            public void clear() {
                WeakIdentityHashMap.this.delegate.clear();
                WeakIdentityHashMap.this.reap();
            }
        };
    }

    @Override
    public String toString() {
        this.reap();
        return this.delegate.toString();
    }

    private synchronized void reap() {
        Reference<? extends K> zombie = this.queue.poll();
        while (zombie != null) {
            IdentityWeakReference victim = (IdentityWeakReference) zombie;
            this.delegate.remove(victim);
            zombie = this.queue.poll();
        }
    }

    private class IdentityWeakReference extends WeakReference<K> {
        private final int hash;

        public IdentityWeakReference(K obj) {
            super(obj, WeakIdentityHashMap.this.queue);
            this.hash = System.identityHashCode(obj);
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || ((IdentityWeakReference) obj).get() == this.get();
        }

        @Override
        public String toString() {
            return String.valueOf(this.get());
        }
    }
}
