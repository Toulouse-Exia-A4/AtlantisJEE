import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SampleClassTest
{
    @Test
    public void multiplyingByZeroShouldReturnZero()
    {
        SampleClass sample = new SampleClass();
        assertEquals("10 x 0 must equal 0", sample.multiply(10, 0), 0);
    }
}