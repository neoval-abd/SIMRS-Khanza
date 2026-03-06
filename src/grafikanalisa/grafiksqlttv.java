/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafikanalisa;

/**
 *
 * @author Via
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

// TAMBAHAN FITUR UNTUK ESC
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Via
 */
public class grafiksqlttv extends JDialog {

    /**
     * Creates a dataset.
     *
     * @return A dataset.
     */

    public static CategoryDataset createDataset1(String query, String kolom, String kolom2, String kolom3, String kolom4) { //data grafik nilai K dan D

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        String series1 = "Suhu";
//          String series2 = "Nadi";
//          String series3 = "Respirasi";
//          String series4 = "SpO2";

        try {
            Statement stat = koneksiDB.condb().createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String tksbr = rs.getString(1) + " " + rs.getString(2);
                double field1 = rs.getDouble(3);
//                    double field2=rs.getDouble(4);
//                    double field3=rs.getDouble(5);
//                    double field4=rs.getDouble(6);

                //result.addValue(field1, series1,tksbr);
                result.addValue(field1, series1, tksbr);
//                    result.addValue(field2, series2,tksbr);
//                    result.addValue(field3, series3,tksbr);
//                    result.addValue(field4, series4,tksbr);
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        return result;
    }

    public static CategoryDataset createDataset2(String query, String kolom, String kolom2, String kolom3, String kolom4) { //data grafik nilai K dan D

        DefaultCategoryDataset result = new DefaultCategoryDataset();
//          String series1 = "Suhu";
        String series2 = "Nadi";
//          String series3 = "Respirasi";
//          String series4 = "SpO2";

        try {
            Statement stat = koneksiDB.condb().createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String tksbr = rs.getString(1) + " " + rs.getString(2);
//                    double field1=rs.getDouble(3);
                double field2 = rs.getDouble(4);
//                    double field3=rs.getDouble(5);
//                    double field4=rs.getDouble(6);

                //result.addValue(field1, series1,tksbr);
//                    result.addValue(field1, series1,tksbr);
                result.addValue(field2, series2, tksbr);
//                    result.addValue(field3, series3,tksbr);
//                    result.addValue(field4, series4,tksbr);
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        return result;
    }

    public static CategoryDataset createDataset3(String query, String kolom, String kolom2, String kolom3, String kolom4) { //data grafik nilai K dan D

        DefaultCategoryDataset result = new DefaultCategoryDataset();
//          String series1 = "Suhu";
//          String series2 = "Nadi";
        String series3 = "Respirasi";
//          String series4 = "SpO2";

        try {
            Statement stat = koneksiDB.condb().createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String tksbr = rs.getString(1) + " " + rs.getString(2);
//                    double field1=rs.getDouble(3);
//                    double field2=rs.getDouble(4);
                double field3 = rs.getDouble(5);
//                    double field4=rs.getDouble(6);

                //result.addValue(field1, series1,tksbr);
//                    result.addValue(field1, series1,tksbr);
//                    result.addValue(field2, series2,tksbr);
                result.addValue(field3, series3, tksbr);
//                    result.addValue(field4, series4,tksbr);
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        return result;
    }

    public static CategoryDataset createDataset4(String query, String kolom, String kolom2, String kolom3, String kolom4) { //data grafik nilai K dan D

        DefaultCategoryDataset result = new DefaultCategoryDataset();
//          String series1 = "Suhu";
//          String series2 = "Nadi";
//          String series3 = "Respirasi";
        String series4 = "SpO2";

        try {
            Statement stat = koneksiDB.condb().createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String tksbr = rs.getString(1) + " " + rs.getString(2);
//                    double field1=rs.getDouble(3);
//                    double field2=rs.getDouble(4);
//                    double field3=rs.getDouble(5);
                double field4 = rs.getDouble(6);

                //result.addValue(field1, series1,tksbr);
//                    result.addValue(field1, series1,tksbr);
//                    result.addValue(field2, series2,tksbr);
//                    result.addValue(field3, series3,tksbr);
                result.addValue(field4, series4, tksbr);
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        return result;
    }

    /**
     * Creates a chart.
     *
     * @return A chart.
     */
    private static JFreeChart createChart(String query, String kolom, String kolom2, String kolom3, String kolom4) {

        CategoryDataset dataset1 = createDataset1(query, kolom, kolom2, kolom3, kolom4);
        NumberAxis rangeAxis1 = new NumberAxis("Suhu(ᵒC)");
        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1,
                renderer1);
        subplot1.setDomainGridlinesVisible(true);

        CategoryDataset dataset2 = createDataset2(query, kolom, kolom2, kolom3, kolom4);
        NumberAxis rangeAxis2 = new NumberAxis("Nadi(x/menit)");
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        CategoryPlot subplot2 = new CategoryPlot(dataset2, null, rangeAxis2,
                renderer2);
        subplot2.setDomainGridlinesVisible(true);

        CategoryDataset dataset3 = createDataset3(query, kolom, kolom2, kolom3, kolom4);
        NumberAxis rangeAxis3 = new NumberAxis("RR(x/menit)");
        rangeAxis3.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LineAndShapeRenderer renderer3 = new LineAndShapeRenderer();
        renderer3.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        CategoryPlot subplot3 = new CategoryPlot(dataset3, null, rangeAxis3,
                renderer3);
        subplot3.setDomainGridlinesVisible(true);

        CategoryDataset dataset4 = createDataset4(query, kolom, kolom2, kolom3, kolom4);
        NumberAxis rangeAxis4 = new NumberAxis("SpO2(%)");
        rangeAxis4.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LineAndShapeRenderer renderer4 = new LineAndShapeRenderer();
        renderer4.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        renderer4.setSeriesPaint(0, Color.BLACK);
        CategoryPlot subplot4 = new CategoryPlot(dataset4, null, rangeAxis4,
                renderer4);
        subplot4.setDomainGridlinesVisible(true);

        CategoryAxis domainAxis = new CategoryAxis("TTV");
        CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
//             CombinedCategoryPlot plot = new CombinedCategoryPlot(
//                     domainAxis, new NumberAxis("Range"));
        plot.add(subplot1, 1);
        plot.add(subplot2, 1);
        plot.add(subplot3, 1);
        plot.add(subplot4, 1);

        return new JFreeChart(
                "",
                new Font("Tahoma", Font.PLAIN, 5), plot, true);

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel(String query, String kolom, String kolom2, String kolom3, String kolom4) {
        JFreeChart chart = createChart(query, kolom, kolom2, kolom3, kolom4);
        return new ChartPanel(chart);
    }
    sekuel Sequel = new sekuel();
    validasi Valid = new validasi();
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public grafiksqlttv(String title, String query, String kolom, String kolom2, String kolom3, String kolom4) {
        // super(title);
        setTitle(title);
        JPanel chartPanel = createDemoPanel(query, kolom, kolom2, kolom3, kolom4);

        chartPanel.setSize(screen.width, screen.height);
        setContentPane(chartPanel);

        // TAMBAHAN FITUR UNTUK ESC
        this.getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CloseTTV");

        this.getRootPane().getActionMap().put("CloseTTV", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // menutup grafik
            }
        });

        //setSize(screen.width,screen.height);
        setModal(true);
        //setUndecorated(true);
        setIconImage(new ImageIcon(super.getClass().getResource("/picture/addressbook-edit24.png")).getImage());
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        chartPanel.setBackground(Color.BLACK);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
}
