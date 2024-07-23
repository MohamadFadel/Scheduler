package com.wavemark.scheduler.fire.httpinvoker.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HttpResponse implements Serializable {

	private boolean success;
	private String url;
	private int code;
	private String series;
	private String phrase;
	private String message;
	private String cause;
}