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

import java.util.Random;

import org.perfidix.annotation.Bench;

/**
 * This class simulates a bench class that throws exceptions.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchWithException {

    /**
     * A method that throws an exception.
     */
    @Bench(runs = 10)
    public void benchMe() {
        // Index out of array
        final String[] array = { "A", "B", "C" };
        String concat = "The alphabet: ";
        final Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            if (random.nextInt() < 0) {
                concat = concat.concat(array[i + 1]);
            }
        }
    }
}
