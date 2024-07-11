package com.wavemark.scheduler.schedule.dto.response.autoorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoOrderConfiguration {

	private boolean includeMissing;
	private boolean includeScannedToCart;
	private boolean orderReplacements30Days;
}
