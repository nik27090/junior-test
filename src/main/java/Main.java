import java.io.IOException;

/**
 * This is test assignment.
 *
 * @author Khomenko Nikita
 * @version 1.0
 * @since 2020-06-16
 */
public class Main {

    /**
     * Loads statistics about the specified applications in specified period of years from the database
     * and writes to file .txt.
     *
     * @param args args[0] - starting year, args[1] - ending year,  args[3] - name app, args[4] - name app, etc
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Error! Invalid number of arguments.");
            System.exit(1);
        }

        int startYear = 0;
        int endYear = 0;
        try {
            startYear = Integer.parseInt(args[0]);
            endYear = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.print("Error! Incorrect date.");
            e.printStackTrace();
        }

        for (int i = 2; i < args.length; i++) {
            final AppStatistic app = new AppStatisticFactory().create(args[i], startYear, endYear);

            try {
                app.loadData();
                app.saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
