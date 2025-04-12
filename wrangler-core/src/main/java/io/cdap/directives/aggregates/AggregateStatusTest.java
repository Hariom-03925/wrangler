package io.cdap.wrangler;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.DirectiveParseException;
import io.cdap.wrangler.api.parser.TokenGroup;
import io.cdap.wrangler.executor.RecipePipelineExecutor;
import io.cdap.wrangler.executor.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for AggregateStats directive using ByteSize and TimeDuration parsing.
 */
public class AggregateStatsTest {

    @Test
    public void testAggregateStatsTotalSizeAndTime() throws Exception {
        // Prepare sample input rows
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("data_transfer_size", "1MB").add("response_time", "500ms"));
        rows.add(new Row("data_transfer_size", "512KB").add("response_time", "1500ms"));
        rows.add(new Row("data_transfer_size", "2MB").add("response_time", "2000ms"));

        // Prepare recipe
        String[] recipe = new String[] {
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        // Execute the recipe
        List<Row> results = TestingRig.execute(recipe, rows);

        // Validate results
        Assert.assertEquals(1, results.size());

        Row resultRow = results.get(0);

        // Calculate expected results
        double expectedTotalBytes = (1 * 1024 * 1024) + (512 * 1024) + (2 * 1024 * 1024); // 1MB + 512KB + 2MB
        double expectedTotalMB = expectedTotalBytes / (1024.0 * 1024.0); // Convert bytes to MB

        long totalMillis = 500 + 1500 + 2000;
        double expectedTotalSec = totalMillis / 1000.0; // Convert ms to seconds

        Assert.assertEquals(expectedTotalMB, (Double) resultRow.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedTotalSec, (Double) resultRow.getValue("total_time_sec"), 0.001);
    }
}
