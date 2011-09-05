/**
 * 
 */
package org.perfidix.example;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Nuray Guerler, University of Konstanz
 *
 */
public class FastIntStackTest
{

    private FastIntStack stack;
    private static int[] r = new int[5];
    /**
     * Create array with raandom integer values
     */
    @BeforeClass
    public static void initR()
    {
        java.util.Random random = new java.util.Random();
        for(int i=0;i<r.length;i++)
        {
            r[i] = random.nextInt();
        }
    }
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        stack = new FastIntStack();
    }

    /**
     * Test method for {@link org.perfidix.example.FastIntStack#push(int)}., {@link org.perfidix.example.FastIntStack#get(int)}., {@link org.perfidix.example.FastIntStack#peek()}. and {@link org.perfidix.example.FastIntStack#pop()}.
     */
    @Test
    public void testPushSizeGetPeekPop()
    {
        assertEquals("Stack should be empty", 0, stack.size());
        for(int i=0;i<r.length;i++)
        {
            stack.push(r[i]);
        }
        assertEquals("Size of stack is r.length", r.length, stack.size());
        for(int i=r.length-1;i>=0;i--)
        {
            assertEquals("element i from stack is returned", r[i]
                    , stack.get(i));
            assertEquals("Stack element contains r[i]", r[i], stack.peek());
            assertEquals("Element r[i] is removed from stack", r[i], stack.pop());
        }
    }

        /**
     * Test method for {@link org.perfidix.example.FastIntStack#clear()}.
     */
    @Test
    public void testClear()
    {
        for(int i=0;i<r.length;i++)
        {
            stack.push(r[i]);
        }
        assertEquals("Size of stack is r.length", r.length, stack.size());
        stack.clear();
    }

}
