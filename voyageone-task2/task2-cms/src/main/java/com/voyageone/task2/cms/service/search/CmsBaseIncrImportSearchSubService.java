package com.voyageone.task2.cms.service.search;


import com.voyageone.common.logger.VOAbsIssueLoggable;
import org.bson.Document;

/**
 * Cms Base Increment Import to Search of Sub Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
abstract class CmsBaseIncrImportSearchSubService extends VOAbsIssueLoggable {

    /**
     * handleInsert
     */
    protected abstract boolean handleInsert(Document document);

    /**
     * handleInsert
     */
    protected abstract boolean handleUpdate(Document document);

    /**
     * handleInsert
     */
    protected abstract boolean handleDelete(Document document);
}
