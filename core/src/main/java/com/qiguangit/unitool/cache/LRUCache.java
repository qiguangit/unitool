package com.qiguangit.unitool.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private int cacheMaxSize;

    public LRUCache(int initialCapacity) {
        super(initialCapacity);
        this.cacheMaxSize = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > cacheMaxSize;
    }
}
