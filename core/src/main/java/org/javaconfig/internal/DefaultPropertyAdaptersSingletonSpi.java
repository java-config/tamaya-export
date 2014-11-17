package org.javaconfig.internal;

import org.javaconfig.ConfigException;
import org.javaconfig.PropertyAdapter;
import org.javaconfig.annot.WithPropertyAdapter;
import org.javaconfig.spi.PropertyAdaptersSingletonSpi;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anatole on 30.09.2014.
 */
public class DefaultPropertyAdaptersSingletonSpi implements PropertyAdaptersSingletonSpi{

    private Map<Class,PropertyAdapter> adapters = new ConcurrentHashMap<>();

    public DefaultPropertyAdaptersSingletonSpi(){
        // Add default adapters
        register(char.class, (s) -> s.charAt(0));
        register(int.class, Integer::parseInt);
        register(byte.class, Byte::parseByte);
        register(short.class, Short::parseShort);
        register(boolean.class, Boolean::parseBoolean);
        register(float.class, Float::parseFloat);
        register(double.class, Double::parseDouble);

        register(Character.class, (s) -> s.charAt(0));
        register(Integer.class, Integer::parseInt);
        register(Byte.class, Byte::parseByte);
        register(Short.class, Short::parseShort);
        register(Boolean.class, Boolean::parseBoolean);
        register(Float.class, Float::parseFloat);
        register(Double.class, Double::parseDouble);
        register(BigDecimal.class, BigDecimal::new);
        register(BigInteger.class, BigInteger::new);

        register(Currency.class, (s) -> Currency.getInstance(s));

        register(LocalDate.class, LocalDate::parse);
        register(LocalTime.class, LocalTime::parse);
        register(LocalDateTime.class, LocalDateTime::parse);
        register(ZoneId.class, ZoneId::of);
    }

    @Override
    public <T> PropertyAdapter<T> register(Class<T> targetType, PropertyAdapter<T> adapter){
        return adapters.put(targetType, adapter);
    }

    @Override
    public <T> PropertyAdapter<T> getAdapter(Class<T> targetType, WithPropertyAdapter adapterAnnot){
        PropertyAdapter adapter = null;
        Class<? extends PropertyAdapter> configuredAdapter = null;
        if(adapterAnnot != null){
            configuredAdapter = adapterAnnot.value();
            if(!configuredAdapter.equals(PropertyAdapter.class)){
                try{
                    adapter = configuredAdapter.newInstance();
                }
                catch(Exception e){
                    throw new ConfigException("Invalid adapter configured.", e);
                }
            }
        }
        if(adapter == null){
            adapter = adapters.get(targetType);
        }
        return adapter;
    }

    @Override
    public boolean isTargetTypeSupported(Class<?> targetType){
        return adapters.containsKey(targetType);
    }
}
