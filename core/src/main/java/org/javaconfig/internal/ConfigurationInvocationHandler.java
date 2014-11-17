package org.javaconfig.internal;

import org.javaconfig.Configuration;
import org.javaconfig.internal.inject.ConfiguredType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by Anatole on 17.10.2014.
 */
class ConfigurationInvocationHandler implements InvocationHandler {

    private Configuration config;
    private ConfiguredType type;

    public ConfigurationInvocationHandler(Class<?> type, Configuration config) {
        this.config = Objects.requireNonNull(config);
        this.type = new ConfiguredType(Objects.requireNonNull(type));
        if(!type.isInterface()){
            throw new IllegalArgumentException("Can only proxy interfaces as configuration templates.");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("toString".equals(method.getName())){
            return "Configured Proxy -> " + this.type.getType().getName();
        }
        return this.type.getConfiguredValue(method, args);
    }
}
