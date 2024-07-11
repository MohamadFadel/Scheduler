package com.wavemark.scheduler.logging.errorlog.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "ERRORLOG")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLog implements Serializable {

	private String deviceId;
	private String errorCode;
	private Timestamp errorDateTime;
	@Id
	private Long errorId;
	private String message;
	private String severity;
	private String source;
	private byte[] stackMessage;
	private String username;
	private String wmPackage;
}
