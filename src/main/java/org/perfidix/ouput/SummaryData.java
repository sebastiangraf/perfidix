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
package org.perfidix.ouput;

import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * This class write summaries of results to different streams.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class SummaryData extends ResultVisitor {

    protected SummaryData() {
    }

    /** {@inheritDoc} */
    @Override
    protected void handleBenchmarkResult(final BenchmarkResult benchRes) {

    }

    /** {@inheritDoc} */
    @Override
    protected void handleClassResult(ClassResult classRes) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    protected void handleMethodResult(MethodResult methRes) {
        // TODO Auto-generated method stub

    }

}
