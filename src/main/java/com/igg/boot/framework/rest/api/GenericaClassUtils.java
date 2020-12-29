package com.igg.boot.framework.rest.api;

import java.lang.reflect.Type;

import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

public class GenericaClassUtils {
    public static Type createRestType(Class<?>... cls) {
        Assert.notNull(cls, "Class array must not be null");
        if (cls.length <= 1) {
            return ResolvableType.forClassWithGenerics(RestResponse.class, cls).getType();
        } else {
            ResolvableType resolvabletype = ResolvableType.forClassWithGenerics(cls[cls.length - 2],
                    new Class[]{cls[cls.length - 1]});

            for (int i = cls.length - 2; i > 0; --i) {
                resolvabletype = ResolvableType.forClassWithGenerics(cls[i - 1], new ResolvableType[]{resolvabletype});
            }

            return ResolvableType.forClassWithGenerics(RestResponse.class, new ResolvableType[]{resolvabletype})
                    .getType();
        }
    }
}
