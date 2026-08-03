/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author novalar
 */
package fungsi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collections;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class WarnaTableRegistrasi extends DefaultTableCellRenderer {

    private Set<String> noRawatMJKN = Collections.emptySet();
    private int kolomNoRawat = 2; // default utk tbPetugas (tab "Registrasi Awal")

    public void setNoRawatMJKN(Set<String> noRawatMJKN) {
        this.noRawatMJKN = (noRawatMJKN == null) ? Collections.emptySet() : noRawatMJKN;
    }

    public void setKolomNoRawat(int kolomNoRawat) {
        this.kolomNoRawat = kolomNoRawat;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // warna default selang-seling (sama seperti WarnaTable biasa)
        if (row % 2 == 1) {
            component.setBackground(new Color(255, 255, 255));
        } else {
            component.setBackground(new Color(255,244,244));
        }
        component.setForeground(new Color(50, 50, 50));

        // cek apakah no_rawat baris ini ada di daftar MJKN
        try {
            Object noRawatObj = table.getValueAt(row, kolomNoRawat);
            if (noRawatObj != null && noRawatMJKN.contains(noRawatObj.toString().trim())) {
                component.setBackground(new Color(198, 239, 206)); // hijau muda ala MJKN
                component.setForeground(new Color(20, 90, 50));
            }
        } catch (Exception e) {
            // kolom belum ada / row kosong, abaikan
        }

        if (isSelected) {
            component.setForeground(new Color(255, 0, 0));
            component.setFont(component.getFont().deriveFont(Font.BOLD));
        } else {
            component.setFont(component.getFont().deriveFont(Font.PLAIN));
        }

        return component;
    }
}
