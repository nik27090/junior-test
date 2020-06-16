import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class BaseTest {
    /**
     * file structure: {numbers of row} ... {[row]}
     * row structure: {numbers of name bytes(int)} ... {name} ... {year(int)} ... {value(long)}
     */
    @Test
    public void creatorData() {
        createData();
    }

    private void createData() {
        File file = new File("data/data.txt");
        try (FileChannel fileChannel = FileChannel.open(file.toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            long countRow = 15;
            ByteBuffer countRowBB = ByteBuffer.allocate(Long.BYTES).putLong(countRow).rewind();
            fileChannel.write(countRowBB);

            fillData(fileChannel, countRow / 3, 0, 0);
            fillData(fileChannel, countRow / 3, 1, 1);
            fillData(fileChannel, countRow / 3, 1, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillData(FileChannel fileChannel, long countRow, int shiftYear, long shiftValue) throws IOException {
        for (int i = 0; i < countRow; i++) {
            String nameApp = "app" + i;

            ByteBuffer nameAppBB = ByteBuffer.wrap(nameApp.getBytes()).rewind();
            int bytesOfName = nameAppBB.limit();
            ByteBuffer bytesOfNameBB = ByteBuffer.allocate(bytesOfName).rewind();
            fileChannel.write(bytesOfNameBB);
            fileChannel.write(nameAppBB);

            int year = 2014 + i + shiftYear;
            ByteBuffer yearBB = ByteBuffer.allocate(Integer.BYTES).putInt(year).rewind();
            fileChannel.write(yearBB);

            long value = 10 + i + shiftValue;
            ByteBuffer valueBB = ByteBuffer.allocate(Long.BYTES).putLong(value).rewind();
            fileChannel.write(valueBB);
        }
    }
}
