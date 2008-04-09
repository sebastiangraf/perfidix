/*
 * Copyright 2007 University of Konstanz
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
 * $Id: IRandomizer.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.perfidix.exceptions.ProbabilityInvalidException;

/**
 * a randomizer for the benchmark object.
 * 
 * @author axo
 * @since 18.11.2005
 */
public interface IRandomizer {

    /**
     * returns true if a method should be run at the current iteration.
     * 
     * @param m
     *                the method to ask for.
     * @return boolean whether it should be run or not.
     */
    public boolean shouldRun(Method m);

    /**
     * the default randomizer returns always true, which is the default
     * behavior.
     */
    public class DefaultRandomizer implements IRandomizer {

        /**
         * tells whether the method m should be run or not.
         * 
         * @return whether the method m should be run or not.
         * @param m
         *                the method to ask for.
         */
        public boolean shouldRun(final Method m) {
            return true;
        }

    }

    /**
     * the Randomizer allows using probabilities computing whether to run a
     * certain method or not.
     */
    public class Randomizer extends IRandomizer.DefaultRandomizer {

        private Hashtable<Method, Double> randomHashes;

        /**
         * @param hashes
         *                the hashtable to look up.
         */
        public Randomizer(final Hashtable<Method, Double> hashes) {

            this.randomHashes = hashes;
        }

        /**
         * default constructor.
         */
        public Randomizer() {

            this(new Hashtable<Method, Double>());
        }

        /**
         * tells the randomizer to ignore the given method.
         * 
         * @param m
         *                the method to ignore.
         */
        public void doIgnoreMethod(final Method m) {
            try {
                setTreshold(m, 0.0);
            } catch (Exception e) {
                // should never reach here, since i'm giving valid input.
                assert (false);
            }
        }

        /**
         * @param m
         *                the method.
         */
        public void doInvokeMethod(final Method m) {
            try {
                setTreshold(m, 1.0);
            } catch (Exception e) {
                // should never reach here, since i'm giving valid input.
                assert (false);
            }
        }

        /**
         * sets the treshold for a method m.
         * 
         * @param m
         *                the method
         * @param probability
         *                the probability to use this method.
         * @throws ProbabilityInvalidException
         *                 on invalid data.
         * @throws NullPointerException
         *                 if the method is null.
         */
        public void setTreshold(final Method m, final double probability)
                throws ProbabilityInvalidException, NullPointerException {

            if (m == null) {
                throw new NullPointerException();
            }
            if (probability < 0.0 || probability > 1.0) {
                throw new ProbabilityInvalidException();
            }
            randomHashes.put(m, new Double(probability));
        }

        /**
         * checks whether the method should be run or not.
         * 
         * @param m
         *                the method
         * @return boolean whether to run or not.
         */
        public boolean shouldRun(final Method m) {

            double treshold = getTreshold(m);
            if (treshold == 1.0) {
                // System.out.println("should run " + m.getName());
                return true;
            }
            if (treshold == 0.0) {
                // System.out.println("should not run " + m.getName());
                return false;
            }
            boolean result = Math.random() <= getTreshold(m);
            // System.out.println("should " + (result ? "" : " not ") + " run "
            // + m.getName());
            return result;
        }

        /**
         * returns the treshold for the given method m.
         * 
         * @param m
         *                the method
         * @return the treshold
         */
        public double getTreshold(final Method m) {

            Double r = randomHashes.get(m);
            if (r == null) {
                return 1.0;
            }
            double v = r.doubleValue();
            if (v < 0.0 || v > 1.0) {
                return 1.0;
            }
            return v;
        }

    }

    /**
     * this is a rather simple randomizer. it returns true every second call,
     * starting with true, then false, then true.
     * 
     * @author axo
     * @since 02.2006
     */

    public class Switcher implements IRandomizer {

        private boolean theSwitch = false;

        /**
         * tells whether to run the method or not. for each shouldRun() call, it
         * will return a row of true, false, true, false, true, false.
         * 
         * @return whether to run or not.
         * @param m
         *                the method in question.
         */
        public boolean shouldRun(final Method m) {
            theSwitch = !theSwitch;
            return theSwitch;
        }

    }

}
