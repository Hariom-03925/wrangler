package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {

  @Test
  public void testMilliseconds() {
    TimeDuration t = new TimeDuration("500ms");
    Assert.assertEquals(500, t.getMilliseconds());
  }

  @Test
  public void testSeconds() {
    TimeDuration t = new TimeDuration("2.5s");
    Assert.assertEquals((long)(2.5 * 1000), t.getMilliseconds());
  }

  @Test
  public void testMinutes() {
    TimeDuration t = new TimeDuration("1m");
    Assert.assertEquals(60000, t.getMilliseconds());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFormat() {
    new TimeDuration("4xy");
  }
}
