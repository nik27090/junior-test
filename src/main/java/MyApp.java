import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MyApp implements DataConnection {
    private static final String SUFFIX = ".txt";
    private final String name;
    private final int startYear;
    private final int endYear;
    private HashMap<Integer, YearReport> yearReports;

    /**
     * Constructor
     *
     * @param name application name
     * @param startYear starting year
     * @param endYear ending year
     */
    public MyApp(final String name, final int startYear, final int endYear) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    /**
     * Downloads statistics app of specified period of years.
     *
     * @throws IOException if file no such
     */
    public void loadData() throws IOException {
        this.yearReports = new HashMap<>();
        final File file = new File("data/data" + SUFFIX);
        try (final FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            long position = 0;

            final ByteBuffer countRowBB = ByteBuffer.allocate(Long.BYTES);
            channel.read(countRowBB, position);
            position += countRowBB.limit();
            final long countRow = countRowBB.rewind().getLong();

            for (int i = 0; i < countRow; i++) {
                final ByteBuffer bytesOfName = ByteBuffer.allocate(Integer.BYTES);
                channel.read(bytesOfName, position);
                position += Integer.BYTES;
                final int countBytesOfName = bytesOfName.rewind().limit();

                final ByteBuffer nameBB = ByteBuffer.allocate(countBytesOfName);
                channel.read(nameBB, position);
                position += countBytesOfName;
                final String name = StandardCharsets.UTF_8.decode(nameBB.rewind()).toString();

                if (!name.equals(this.name)) {
                    position += Integer.BYTES + Long.BYTES;
                    continue;
                }

                final ByteBuffer yearBB = ByteBuffer.allocate(Integer.BYTES);
                channel.read(yearBB, position);
                position += Integer.BYTES;
                final int year = yearBB.rewind().getInt();

                if (year < startYear || year > endYear) {
                    position += Long.BYTES;
                    continue;
                }

                final ByteBuffer valueBB = ByteBuffer.allocate(Long.BYTES);
                channel.read(valueBB, position);
                position += Long.BYTES;
                final long value = valueBB.rewind().getLong();

                YearReport yearReport = yearReports.get(year);
                if (yearReport == null) {
                    yearReport = new YearReport(year);
                    yearReport.addReport(value);
                    yearReports.put(year, yearReport);
                } else {
                    yearReport.addReport(value);
                }
            }
        }
    }

    /**
     * Saving downloaded statistics to a file .txt
     *
     * @throws IOException If a file could not be created
     */
    public void saveData() throws IOException {
        if (yearReports == null) {
            throw new NoSuchElementException("Before saving, you need to download data.");
        }

        final FileOutputStream fos = new FileOutputStream("data/statistics" + SUFFIX, true);
        fos.write((name + " " + startYear + " - " + endYear + "\n").getBytes());

        if (yearReports.isEmpty()) {
            fos.write("No statistics for the indicated years\n\n".getBytes());
            return;
        }

        int id = 0;
        for (Map.Entry<Integer, YearReport> entry : yearReports.entrySet()) {
            final YearReport yearReport = entry.getValue();
            final int year = yearReport.getYear();
            final double avgValue = yearReport.getAvgValue();
            final String line = id + " " + year + " " + avgValue + "\n";
            fos.write(line.getBytes());
            id++;
        }
        fos.write("\n".getBytes());
    }
}
