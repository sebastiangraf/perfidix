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
 * This is a class that tests the {@link FastIntStack} class with all its methods
 */
public class FastIntStackTest
{

    /**
     * This member variable contains a FastIntStack object.
     */
    private FastIntStack stack;
    /**
     * Create constant to determine size of test data
     */
    private static final int SIZE = 5;
    /**
     * This integer array contains five random values to test FastIntStack methods
     */
    private static int[] r = new int[SIZE];
    /**
     * Fill integer array with raandom values
     */
    @BeforeClass
    public static void initR()
    {
        java.util.Random random = new java.util.Random();
        for(int i=0;i<SIZE;i++)
        {
            r[i] = random.nextInt();
        }
    }
    /**
     * @throws java.lang.Exception
     */
    /**
     * initialize stack
     * @throws Exception
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
        for(int i=0;i<SIZE;i++)
        {
            stack.push(r[i]);
        }
        assertEquals(new StringBuilder("Size of stack is ").append(SIZE).toString(), SIZE, stack.size());
        for(int i=SIZE-1;i>=0;i--)
        {
            assertEquals(new StringBuilder("value of position ").append(i).append(" is returned").toString(), r[i], stack.get(i));
            assertEquals(new StringBuilder("Stack element contains").append(r[i]).toString(), r[i], stack.peek());
            assertEquals("Element r[i] is removed from stack", r[i], stack.pop());
        }
    }

        /**
     * Test method for {@link org.perfidix.example.FastIntStack#clear()}.
     */
    @Test
    public void testClear()
    {
        for(int i=0;i<SIZE;i++)
        {
            stack.push(r[i]);
        }
        assertEquals("Size of stack is "+ SIZE, SIZE, stack.size());
        stack.clear();
    }

}
