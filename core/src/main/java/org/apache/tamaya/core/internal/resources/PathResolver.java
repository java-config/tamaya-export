/*
 * Copyright (c) 2014.
 * Anatole Tresch IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 *
 * Specification: Java Configuration ("Specification")
 *
 * Copyright (c) 2012-2014, Anatole Tresch All rights reserved.
 */
package org.apache.tamaya.core.internal.resources;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by Anatole on 16.06.2014.
 */
public interface PathResolver{

    /**
     * Get the (unique) resolver prefix.
     *
     * @return the resolver prefix, never null.
     */
    public String getResolverId();

    /**
     * Resolve the given expression.
     *
     * @param expressions expressions, never null.
     * @return the resolved URIs, never null.
     */
    public Collection<URI> resolve(ClassLoader classLoader, Stream<String> expressions);

}
