package com.wavemark.scheduler.schedule.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDetailResponse {

	private String description;
	private String bodyParam;
	private String emails;
}
