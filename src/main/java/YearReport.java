/**
 * Statistics for the year
 *
 * @author Khomenko Nikita
 * @version 1.0
 * @since 2020-06-16
 */
public class YearReport {
    private final int year;
    private int countReport;
    private long sumValue;

    /**
     * Constructor
     *
     * @param year - year of statistic
     */
    public YearReport(final int year) {
        this.year = year;
    }

    /**
     * Adding statistics in this year
     *
     * @param value value of statistics
     */
    public void addReport(final Long value) {
        this.sumValue += value;
        this.countReport++;
    }

    /**
     * Gives the average value for the year
     *
     * @return average value for the year
     */
    public double getAvgValue() {
        return (double) sumValue / countReport;
    }

    public int getYear() {
        return year;
    }
}
