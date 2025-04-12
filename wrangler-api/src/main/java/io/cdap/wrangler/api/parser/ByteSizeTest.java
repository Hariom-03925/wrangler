package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {

  @Test
  public void testKB() {
    ByteSize b = new ByteSize("1KB");
    Assert.assertEquals(1024, b.getBytes());
  }

  @Test
  public void testMB() {
    ByteSize b = new ByteSize("2.5MB");
    Assert.assertEquals((long)(2.5 * 1024 * 1024), b.getBytes());
  }

  @Test
  public void testGB() {
    ByteSize b = new ByteSize("1GB");
    Assert.assertEquals(1024L * 1024 * 1024, b.getBytes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidUnit() {
    new ByteSize("100XY");
  }
}
