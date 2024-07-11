package com.wavemark.scheduler.logging.recordlog.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.wavemark.scheduler.logging.recordlog.LogDiffable;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.apache.commons.lang3.builder.DiffResult;

public class RecordLogFactory {


	public static List<RecordLog> getRecordLogInstance(Integer logId, LogDiffable oldLogDiffable, LogDiffable newLogDiffable)
	{
		List<RecordLog> logs = new ArrayList<>();

		if (newLogDiffable != null) {
			DiffResult<?> diffResult = oldLogDiffable.diff(newLogDiffable);
			diffResult.getDiffs()
					.forEach(diff -> {
						RecordLog oneDiff = new RecordLog();
						oneDiff.setTableName(newLogDiffable.getTableName());
						oneDiff.setFieldName(diff.getFieldName());
						oneDiff.setUpdatedDate(Instant.now());
						oneDiff.setUpdatedBy(SecurityUtilsV2.getWebAppUserInfo().getFullName());
						oneDiff.setNewValue(Objects.nonNull(diff.getRight()) ? diff.getRight().toString() : "");
						oneDiff.setOldValue(Objects.nonNull(diff.getLeft()) ? diff.getLeft().toString() : "");
						oneDiff.setLogId(logId);
						logs.add(oneDiff);
					});
		}

		return logs;
	}
}
