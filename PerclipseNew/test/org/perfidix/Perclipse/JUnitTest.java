package org.perfidix.Perclipse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnitTest {
    
    @Test
    public void testHelloWorld(){
        String s ="HelloWorld";
        assertEquals("Just a Test", "HelloWorld", s);
    }

}
