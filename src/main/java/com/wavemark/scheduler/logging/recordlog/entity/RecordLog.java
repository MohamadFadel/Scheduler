package com.wavemark.scheduler.logging.recordlog.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "RECORDLOG")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORDLOGID")
	private Integer recordLogId;
	@Column(name = "TABLENAME")
	private String tableName;
	@Column(name = "FIELDNAME")
	private String fieldName;
	@Column(name = "OLDVALUE")
	private String oldValue;
	@Column(name = "NEWVALUE")
	private String newValue;
	@Column(name = "UPDATEDBY")
	private String updatedBy;
	@Column(name = "UPDATEDDATE")
	private Instant updatedDate;
	@Column(name = "LOGID")
	private Integer logId;
	@Column(name = "WMCOMMENT")
	private String wmComment;
	@Column(name = "STATUS")
	private String status;
}
