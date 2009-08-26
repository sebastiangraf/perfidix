/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.socketadapter;

import java.io.IOException;

/**
 * Exception type for communicating with the socket. Encapsulates instance of
 * IOExceptions.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class SocketViewException extends IOException {

    /** Serial ID */
    private static final long serialVersionUID = 528281733888231478L;

    /**
     * Constructor
     * 
     * @param paramToStore
     *            exception to encapsulate
     */
    public SocketViewException(final IOException paramToStore) {
        super(paramToStore);
    }

}
