package io.cdap.wrangler.steps.aggregate;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.UsageDefinition;

import java.util.Collections;
import java.util.List;

public class AggregateStats implements Directive {
  private String sizeColumn;
  private String timeColumn;
  private String targetSizeColumn;
  private String targetTimeColumn;

  @Override
  public UsageDefinition define() {
    return UsageDefinition.builder()
      .define("sizeColumn", TokenType.COLUMN_NAME)
      .define("timeColumn", TokenType.COLUMN_NAME)
      .define("targetSizeColumn", TokenType.COLUMN_NAME)
      .define("targetTimeColumn", TokenType.COLUMN_NAME)
      .build();
  }

  @Override
  public void initialize(Arguments arguments) {
    sizeColumn = ((ColumnName) arguments.value("sizeColumn")).value();
    timeColumn = ((ColumnName) arguments.value("timeColumn")).value();
    targetSizeColumn = ((ColumnName) arguments.value("targetSizeColumn")).value();
    targetTimeColumn = ((ColumnName) arguments.value("targetTimeColumn")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
    long totalBytes = 0;
    long totalMillis = 0;

    for (Row row : rows) {
      Object sizeObj = row.getValue(sizeColumn);
      Object timeObj = row.getValue(timeColumn);

      if (sizeObj != null && timeObj != null) {
        ByteSize byteSize = new ByteSize(sizeObj.toString());
        TimeDuration timeDuration = new TimeDuration(timeObj.toString());
        totalBytes += byteSize.getBytes();
        totalMillis += timeDuration.getMillis();
      }
    }

    double totalSizeMB = totalBytes / (1024.0 * 1024.0);
    double totalTimeSec = totalMillis / 1000.0;

    Row output = new Row(targetSizeColumn, totalSizeMB)
                    .add(targetTimeColumn, totalTimeSec);

    return Collections.singletonList(output);
  }
}
