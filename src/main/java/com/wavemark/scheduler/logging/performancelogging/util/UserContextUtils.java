package com.wavemark.scheduler.logging.performancelogging.util;

import java.util.List;
import java.util.stream.Collectors;

import com.cardinalhealth.service.support.models.TypedValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContextUtils
{
    public static final String SECURITY_ID = "SecurityId";
    public static final String PRICING_KEY = "PricingKey";
    public static final String PRICE_IV = "Price_IV";

    public static List<TypedValue> excludePricing(List<TypedValue> userContextList) {
        return userContextList.stream()
                .filter(UserContextUtils::shouldInclude)
                .collect(Collectors.toList());
    }

    private static boolean shouldInclude(TypedValue userContext) {
        return !userContext.getName().equals(PRICING_KEY) &&
                !userContext.getName().equals(PRICE_IV) &&
                !userContext.getName().equals(SECURITY_ID);
    }
}
