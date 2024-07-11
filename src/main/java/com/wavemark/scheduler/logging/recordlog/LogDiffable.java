package com.wavemark.scheduler.logging.recordlog;

import org.apache.commons.lang3.builder.Diffable;

public interface LogDiffable<T> extends Diffable<T> {

	String getTableName();
}
