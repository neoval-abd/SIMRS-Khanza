/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

<<<<<<< HEAD
// import Modif
import java.awt.Font;

=======
>>>>>>> master
/**
 *
 * @author Owner
 */
public class WarnaTableKasirRalan extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(255,244,244));
            component.setForeground(new Color(50,50,50));
        }else{
            component.setBackground(new Color(255,255,255));
            component.setForeground(new Color(50,50,50));
        } 
        if(table.getValueAt(row,10).toString().equals("Sudah")){
<<<<<<< HEAD
            component.setBackground(new Color(204, 229, 255));
            component.setForeground(new Color(51, 51, 51));
        }else if(table.getValueAt(row,10).toString().equals("Batal")){
            component.setBackground(new Color(255, 204, 204));
            component.setForeground(new Color(51, 51, 51));
        }else if(table.getValueAt(row,10).toString().equals("Dirujuk")||table.getValueAt(row,10).toString().equals("Meninggal")||table.getValueAt(row,10).toString().equals("Pulang Paksa")){
            component.setBackground(new Color(225, 225, 225));
            component.setForeground(new Color(51, 51, 51));
        }else if(table.getValueAt(row,10).toString().equals("Dirawat")){
            component.setBackground(new Color(255, 249, 196));
            component.setForeground(new Color(51, 51, 51));
        }
        if(table.getValueAt(row,15).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(204, 255, 229));
            component.setForeground(new Color(51, 51, 51));
        }
        
        // TAMBAHAN SENDIRI
        if (isSelected) {
            component.setForeground(new Color(255,0,0)); 
            component.setFont(component.getFont().deriveFont(Font.BOLD)); // bold
        } else {
            component.setFont(component.getFont().deriveFont(Font.PLAIN));
=======
            component.setBackground(new Color(200,0,0));
            component.setForeground(new Color(255,230,230));
        }else if(table.getValueAt(row,10).toString().equals("Batal")){
            component.setBackground(new Color(255,243,109));
            component.setForeground(new Color(120,110,50));
        }else if(table.getValueAt(row,10).toString().equals("Dirujuk")||table.getValueAt(row,10).toString().equals("Meninggal")||table.getValueAt(row,10).toString().equals("Pulang Paksa")){
            component.setBackground(new Color(152,152,156));
            component.setForeground(new Color(245,245,255));
        }else if(table.getValueAt(row,10).toString().equals("Dirawat")){
            component.setBackground(new Color(119,221,119));
            component.setForeground(new Color(245,255,245));
        }
        if(table.getValueAt(row,15).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(50,50,50));
            component.setForeground(new Color(255,255,255));
>>>>>>> master
        }
        return component;
    }

}
