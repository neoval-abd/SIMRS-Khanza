package fungsi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Utility untuk memasang kotak pencarian (search-as-you-type) pada tabel
 * di panel tombol bawah (panelBtm). Pencarian mencocokkan teks ke SEMUA
 * kolom tabel secara case-insensitive.
 *
 * @author novalar
 */
public class UtilCari {

    /**
     * Memasang komponen pencarian ke panelBtm yang memfilter isi tb.
     * @param panelBtm      panel tempat search box ditaruh (FlowLayout)
     * @param tb            JTable yang mau difilter
     * @param tabMode       model tabel (DefaultTableModel)
     * @param tab           JTabbedPane pemilik tabel (untuk konteks tab aktif)
     * @param noRawatAktif  supplier no_rawat pasien yang sedang aktif di form
     */
    public static void pasang(widget.panelisi panelBtm, JTable tb, DefaultTableModel tabMode,
                               javax.swing.JTabbedPane tab, Supplier<String> noRawatAktif) {

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tabMode);
        tb.setRowSorter(sorter);

        JLabel lbl = new JLabel("Cari :");
        JTextField txtCari = new JTextField();
        txtCari.setPreferredSize(new java.awt.Dimension(160, 25));

        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kata = txtCari.getText().trim();
                if (kata.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(kata)));
                    } catch (Exception ex) {
                        sorter.setRowFilter(null);
                    }
                }
            }
        });

        panelBtm.add(lbl);
        panelBtm.add(txtCari);
    }
}