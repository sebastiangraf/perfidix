/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.result;

import java.util.Set;

import org.perfidix.meter.AbstractMeter;

/**
 * @author axo
 */
public class BenchmarkResult extends ResultContainer<ClassResult> {

    private final String name;

    /**
     * default constructor.
     */
    public BenchmarkResult(
            final Set<AbstractMeter> meters, final String paramName) {
        super(meters);
        this.name = paramName;
    }

    public BenchmarkResult(final Set<AbstractMeter> meters) {
        this(meters, "");
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

}
