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
package org.perfidix.ouput.graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Graph class.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 */
class IO {
    static BufferedReader br;
    static String line;

    static void readInit(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
    }

    static String readLine() throws Exception {
        return br.readLine();
    }

    static boolean readMore() throws Exception {
        return (line = readLine()) != null;
    }

    static String readNext() throws Exception {
        return line;
    }

    static void readClose() throws Exception {
        br.close();
    }

    static int countLines(String filename) throws Exception {
        readInit(filename);
        int lines = 0;
        while (readMore())
            lines++;
        readClose();
        return lines;
    }
}
