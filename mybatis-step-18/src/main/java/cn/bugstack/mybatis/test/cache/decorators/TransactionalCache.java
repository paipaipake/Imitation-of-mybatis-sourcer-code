package cn.bugstack.mybatis.test.cache.decorators;

import cn.bugstack.mybatis.cache.Cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 小傅哥，微信：fustack
 * @description The 2nd level cache transactional buffer. 事务缓存
 * @date 2022/7/5
 * @github https://github.com/fuzhengwei/CodeDesignTutorials
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class TransactionalCache implements Cache {

    private Cache delegate;
    // commit 时要不要清缓存
    private boolean clearOnCommit;
    // commit 时要添加的元素
    private Map<Object, Object> entriesToAddOnCommit;
    private Set<Object> entriesMissedInCache;

    public TransactionalCache(Cache delegate) {
        // delegate = FifoCache
        this.delegate = delegate;
        // 默认 commit 时不清缓存
        this.clearOnCommit = false;
        this.entriesToAddOnCommit = new HashMap<>();
        this.entriesMissedInCache = new HashSet<>();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public Object getObject(Object key) {
        // key：CacheKey 拼装后的哈希码
        Object object = delegate.getObject(key);
        if (object == null) {
            entriesMissedInCache.add(key);
        }
        return clearOnCommit ? null : object;
    }

    @Override
    public void putObject(Object key, Object object) {
        entriesToAddOnCommit.put(key, object);
    }

    @Override
    public Object removeObject(Object key) {
        return null;
    }

    @Override
    public void clear() {
        clearOnCommit = true;
        entriesToAddOnCommit.clear();
    }

    public void commit() {
        if (clearOnCommit) {
            delegate.clear();
        }
        flushPendingEntries();
        reset();
    }

    public void rollback() {
        unlockMissedEntries();
        reset();
    }

    private void reset() {
        clearOnCommit = false;
        entriesToAddOnCommit.clear();
        entriesMissedInCache.clear();
    }

    /**
     * 刷新数据到 MappedStatement#Cache 中，也就是把数据填充到 Mapper XML 级别下。
     * flushPendingEntries 方法把事务缓存下的数据，填充到 FifoCache 中。
     */
    private void flushPendingEntries() {
        for (Map.Entry<Object, Object> entry : entriesToAddOnCommit.entrySet()) {
            delegate.putObject(entry.getKey(), entry.getValue());
        }
        for (Object entry : entriesMissedInCache) {
            if (!entriesToAddOnCommit.containsKey(entry)) {
                delegate.putObject(entry, null);
            }
        }
    }

    private void unlockMissedEntries() {
        for (Object entry : entriesMissedInCache) {
            delegate.putObject(entry, null);
        }
    }

}
