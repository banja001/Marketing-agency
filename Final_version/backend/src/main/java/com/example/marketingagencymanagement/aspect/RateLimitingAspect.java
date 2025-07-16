package com.example.marketingagencymanagement.aspect;

import com.example.marketingagencymanagement.model.ServicePackageType;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimitingAspect {

    @Autowired
    private RateLimiter basicRateLimiter;

    @Autowired
    private RateLimiter standardRateLimiter;

    @Autowired
    private RateLimiter goldenRateLimiter;

    @Before("execution(* com.example.marketingagencymanagement.controller.CommercialController.*(..)) && args(packageType, ..)")
    public void enforceRateLimit(String packageType) {
        RateLimiter rateLimiter = getRateLimiterForPackage(packageType);
        rateLimiter.acquire();
    }

    private RateLimiter getRateLimiterForPackage(String packageType) {
        ServicePackageType enumPackageType = ServicePackageType.valueOf(packageType.toUpperCase());
        switch (enumPackageType) {
            case BASIC:
                return basicRateLimiter;
            case STANDARD:
                return standardRateLimiter;
            case GOLD:
                return goldenRateLimiter;
            default:
                throw new IllegalArgumentException("Unknown package type: " + packageType);
        }
    }
}


