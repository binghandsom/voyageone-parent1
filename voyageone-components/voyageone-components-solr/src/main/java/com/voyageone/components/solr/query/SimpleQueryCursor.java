package com.voyageone.components.solr.query;

import org.springframework.data.solr.core.query.result.Cursor;

import java.io.IOException;
import java.io.Serializable;

/**
 * SimpleQueryBean
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
public class SimpleQueryCursor<T> implements Cursor<T> {

    private Cursor<T> _cursor;

    public SimpleQueryCursor(Cursor<T> cursor) {
        _cursor = cursor;
    }

    @Override
    public Serializable getCursorMark() {
        return _cursor.getCursorMark();
    }

    @Override
    public Cursor<T> open() {
        return _cursor.open();
    }

    @Override
    public long getPosition() {
        return _cursor.getPosition();
    }

    @Override
    public boolean isOpen() {
        return _cursor.isOpen();
    }

    @Override
    public boolean isClosed() {
        return _cursor.isClosed();
    }

    @Override
    public void close() throws IOException {
        _cursor.close();
    }

    @Override
    public boolean hasNext() {
        return _cursor.hasNext();
    }

    @Override
    public T next() {
        return _cursor.next();
    }
}
