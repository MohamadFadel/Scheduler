package com.wavemark.scheduler.fire.httpinvoker.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpProperty {

    private String taskName;
    private String endpointName;
    private String url;
    private String bodyParam;
}
