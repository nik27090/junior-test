import java.io.IOException;

interface DataConnection {
    void loadData() throws Exception;

    void saveData() throws IOException;
}