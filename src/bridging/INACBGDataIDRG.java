/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPasienMati.java
 *
 * Created on Aug 30, 2010, 7:46:01 AM
 */

package bridging;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import fungsi.CacheLabelTB;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.SwingUtilities;
import kepegawaian.DlgCariDokter;
import org.json.JSONArray;
import org.json.JSONObject;
import simrskhanza.DlgPasien;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.print.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import widget.TextBox;

/**
 *
 * @author dosen3
 */
public class INACBGDataIDRG extends javax.swing.JDialog {
    private final DefaultTableModel tabModeDiagnosa,tabModeProsedur,tabModeDiagnosa2,tabModeProsedur2;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgPasien pasien=new DlgPasien(null,false);
    private PreparedStatement ps,pskoders,psprosedur, psdiagnosa;
    private ResultSet rs,rskode,rs2,rs3,rsdiagnosa, rsprosedur,rsgrouper;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String tgl, finger="", lama="",payor_id="",payor_cd="",coder="",dokterkonsul="",tgl_masuk_ranap="",tgl_keluar_ranap="",dpjp_ranap="",inacbg_diagnosa="",prosedur_inacbg="",no_rawat_bayi="",
                    icd9="",icd10="",icd10INA="",icd9INA="";
    public  ApiINACBG inacbg = new ApiINACBG();
    private javax.swing.Timer searchTimer;
    private int total_biaya = 0,i=0,j=0,jml=0,index=0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter_tanggal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean isLoading = false;
    
    private String[] kode,nama,kode2,panjang,acc,asterisk,validcode;
    private boolean[] pilih;
    
    JTextField[] kd_diag;
    JTextField[] kd_diag2;
    JTextField[] nm_diag;
    JTextField[] nm_diag2;
    JTextField[] multiple;
    JTextField[] kd_psdr;
    JTextField[] kd_psdr2;
    JTextField[] nm_psdr;
    JTextField[] nm_psdr2;
    JCheckBox[] chk_diag;
    JCheckBox[] chk_diag2;
    JButton[] h_diag;
    JButton[] h_diag2;
    JButton[] h_psdr;
    JButton[] h_psdr2;
    
    /** Creates new form DlgPasienMati
     * @param parent
     * @param modal */
    public INACBGDataIDRG(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        pnb.setDocument(new batasInput((byte)10).getOnlyAngka(pnb));
        rad.setDocument(new batasInput((byte)10).getOnlyAngka(rad));
        rad.setDocument(new batasInput((byte)10).getOnlyAngka(rad));
        rehab.setDocument(new batasInput((byte)10).getOnlyAngka(rehab));
        obat.setDocument(new batasInput((byte)10).getOnlyAngka(obat));
        alkes.setDocument(new batasInput((byte)10).getOnlyAngka(alkes));
        pb.setDocument(new batasInput((byte)10).getOnlyAngka(pb));
        kpr.setDocument(new batasInput((byte)10).getOnlyAngka(kpr));
        lab.setDocument(new batasInput((byte)10).getOnlyAngka(lab));
        akomodasi.setDocument(new batasInput((byte)10).getOnlyAngka(akomodasi));
        kronis.setDocument(new batasInput((byte)10).getOnlyAngka(kronis));
        bmhp.setDocument(new batasInput((byte)10).getOnlyAngka(bmhp));
        ksl.setDocument(new batasInput((byte)10).getOnlyAngka(ksl));
        pnj.setDocument(new batasInput((byte)10).getOnlyAngka(pnj));
        pl_darah.setDocument(new batasInput((byte)10).getOnlyAngka(pl_darah));
        ri.setDocument(new batasInput((byte)10).getOnlyAngka(ri));
        obat_kemo.setDocument(new batasInput((byte)10).getOnlyAngka(obat_kemo));
        sewa_alat.setDocument(new batasInput((byte)10).getOnlyAngka(sewa_alat));
        multiple1.setDocument(new batasInput((byte)1).getOnlyAngka(multiple1));
        multiple2.setDocument(new batasInput((byte)1).getOnlyAngka(multiple2));
        multiple3.setDocument(new batasInput((byte)1).getOnlyAngka(multiple3));
        multiple4.setDocument(new batasInput((byte)1).getOnlyAngka(multiple4));
        multiple5.setDocument(new batasInput((byte)1).getOnlyAngka(multiple5));
        multiple6.setDocument(new batasInput((byte)1).getOnlyAngka(multiple6));
        multiple7.setDocument(new batasInput((byte)1).getOnlyAngka(multiple7));
        multiple8.setDocument(new batasInput((byte)1).getOnlyAngka(multiple8));
        multiple9.setDocument(new batasInput((byte)1).getOnlyAngka(multiple9));
        multiple10.setDocument(new batasInput((byte)1).getOnlyAngka(multiple10));
        multiple11.setDocument(new batasInput((byte)1).getOnlyAngka(multiple11));
        multiple12.setDocument(new batasInput((byte)1).getOnlyAngka(multiple12));
        multiple13.setDocument(new batasInput((byte)1).getOnlyAngka(multiple13));
        multiple14.setDocument(new batasInput((byte)1).getOnlyAngka(multiple14));
        multiple15.setDocument(new batasInput((byte)1).getOnlyAngka(multiple15));
        multiple16.setDocument(new batasInput((byte)1).getOnlyAngka(multiple16));
        multiple17.setDocument(new batasInput((byte)1).getOnlyAngka(multiple17));
        multiple18.setDocument(new batasInput((byte)1).getOnlyAngka(multiple18));
        multiple19.setDocument(new batasInput((byte)1).getOnlyAngka(multiple19));
        multiple20.setDocument(new batasInput((byte)1).getOnlyAngka(multiple20));
        
        h_diag = new JButton[] {
            batal_1, batal_2, batal_3, batal_4, batal_5,
            batal_6, batal_7, batal_8, batal_9, batal_10,
            batal_21, batal_22, batal_23, batal_24, batal_25,
            batal_26, batal_27, batal_28, batal_29, batal_30,
            batal_41
        };
        
        h_diag2 = new JButton[] {
            batal_11, batal_12, batal_13, batal_14, batal_15,
            batal_16, batal_17, batal_18, batal_19, batal_20,
            batal_31, batal_32, batal_33, batal_34, batal_35,
            batal_36, batal_37, batal_38, batal_39, batal_40,
            batal_42
        };
        
        h_psdr = new JButton[] {
            batal_psdr_1, batal_psdr_2, batal_psdr_3, batal_psdr_4, batal_psdr_5,
            batal_psdr_6, batal_psdr_7, batal_psdr_8, batal_psdr_9, batal_psdr_10,
            batal_psdr_21, batal_psdr_22, batal_psdr_23, batal_psdr_24, batal_psdr_25,
            batal_psdr_26, batal_psdr_27, batal_psdr_28, batal_psdr_29, batal_psdr_30,
            batal_psdr_31
        };
        
        h_psdr2 = new JButton[] {
            batal_psdr_11, batal_psdr_12, batal_psdr_13, batal_psdr_14, batal_psdr_15,
            batal_psdr_16, batal_psdr_17, batal_psdr_18, batal_psdr_19, batal_psdr_20,
            batal_psdr_36, batal_psdr_37, batal_psdr_38, batal_psdr_39, batal_psdr_40,
            batal_psdr_41, batal_psdr_42, batal_psdr_43, batal_psdr_44, batal_psdr_45,
            batal_psdr_46
        };

        kd_diag = new JTextField[]{
            kd_diag_1, kd_diag_2, kd_diag_3, kd_diag_4, kd_diag_5,
            kd_diag_6, kd_diag_7, kd_diag_8, kd_diag_9, kd_diag_10,
            kd_diag_21, kd_diag_22, kd_diag_23, kd_diag_24, kd_diag_25,
            kd_diag_26, kd_diag_27, kd_diag_28, kd_diag_29, kd_diag_30,
            kd_diag_41
        };
        
        kd_diag2 = new JTextField[] {
            kd_diag_11, kd_diag_12, kd_diag_13, kd_diag_14, kd_diag_15,
            kd_diag_16, kd_diag_17, kd_diag_18, kd_diag_19, kd_diag_20,
            kd_diag_31, kd_diag_32, kd_diag_33, kd_diag_34, kd_diag_35,
            kd_diag_36, kd_diag_37, kd_diag_38, kd_diag_39, kd_diag_40,
            kd_diag_42
        };

        nm_diag = new JTextField[]{
            nm_diag_1, nm_diag_2, nm_diag_3, nm_diag_4, nm_diag_5,
            nm_diag_6, nm_diag_7, nm_diag_8, nm_diag_9, nm_diag_10,
            nm_diag_21, nm_diag_22, nm_diag_23, nm_diag_24, nm_diag_25,
            nm_diag_26, nm_diag_27, nm_diag_28, nm_diag_29, nm_diag_30,
            nm_diag_41
        };
        
        nm_diag2 = new JTextField[] {
            nm_diag_11, nm_diag_12, nm_diag_13, nm_diag_14, nm_diag_15,
            nm_diag_16, nm_diag_17, nm_diag_18, nm_diag_19, nm_diag_20,
            nm_diag_31, nm_diag_32, nm_diag_33, nm_diag_34, nm_diag_35,
            nm_diag_36, nm_diag_37, nm_diag_38, nm_diag_39, nm_diag_40,
            nm_diag_42
        };

        chk_diag = new JCheckBox[] {
            chk_diag_1, chk_diag_2, chk_diag_3, chk_diag_4, chk_diag_5,
            chk_diag_6, chk_diag_7, chk_diag_8, chk_diag_9, chk_diag_10,
            chk_diag_21, chk_diag_22, chk_diag_23, chk_diag_24, chk_diag_25,
            chk_diag_26, chk_diag_27, chk_diag_28, chk_diag_29, chk_diag_30,
            chk_diag_41
        };
        
        chk_diag2 = new JCheckBox[] {
            chk_diag_11, chk_diag_12, chk_diag_13, chk_diag_14, chk_diag_15,
            chk_diag_16, chk_diag_17, chk_diag_18, chk_diag_19, chk_diag_20,
            chk_diag_31, chk_diag_32, chk_diag_33, chk_diag_34, chk_diag_35,
            chk_diag_36, chk_diag_37, chk_diag_38, chk_diag_39, chk_diag_40,
            chk_diag_42
        };

        kd_psdr = new JTextField[] {
            kd_psdr_1, kd_psdr_2, kd_psdr_3, kd_psdr_4, kd_psdr_5,
            kd_psdr_6, kd_psdr_7, kd_psdr_8, kd_psdr_9, kd_psdr_10,
            kd_psdr_21, kd_psdr_22, kd_psdr_23, kd_psdr_24, kd_psdr_25,
            kd_psdr_26, kd_psdr_27, kd_psdr_28, kd_psdr_29, kd_psdr_30,
            kd_psdr_31 
        };
        
        kd_psdr2 = new JTextField[] {
            kd_psdr_11, kd_psdr_12, kd_psdr_13, kd_psdr_14, kd_psdr_15,
            kd_psdr_16, kd_psdr_17, kd_psdr_18, kd_psdr_19, kd_psdr_20,
            kd_psdr_36, kd_psdr_37, kd_psdr_38, kd_psdr_39, kd_psdr_40,
            kd_psdr_41, kd_psdr_42, kd_psdr_43, kd_psdr_44, kd_psdr_45,
            kd_psdr_46
        };

        nm_psdr = new JTextField[] {
            nm_psdr_1, nm_psdr_2, nm_psdr_3, nm_psdr_4, nm_psdr_5,
            nm_psdr_6, nm_psdr_7, nm_psdr_8, nm_psdr_9, nm_psdr_10,
            nm_psdr_21, nm_psdr_22, nm_psdr_23, nm_psdr_24, nm_psdr_25,
            nm_psdr_26, nm_psdr_27, nm_psdr_28, nm_psdr_29, nm_psdr_30,
            nm_psdr_31
                
        };

        nm_psdr2 = new JTextField[] {
            nm_psdr_11, nm_psdr_12, nm_psdr_13, nm_psdr_14, nm_psdr_15,
            nm_psdr_16, nm_psdr_17, nm_psdr_18, nm_psdr_19, nm_psdr_20,
            nm_psdr_36, nm_psdr_37, nm_psdr_38, nm_psdr_39, nm_psdr_40,
            nm_psdr_41, nm_psdr_42, nm_psdr_43, nm_psdr_44, nm_psdr_45,
            nm_psdr_46
        };
        
        multiple = new JTextField[] {
            multiple1, multiple2, multiple3, multiple4, multiple5,
            multiple6, multiple7, multiple8, multiple9, multiple10,
            multiple11, multiple12, multiple13, multiple14, multiple15,
            multiple16, multiple17, multiple18, multiple19, multiple20,
            multiple21
        };
        
        for (int i = 0; i < chk_diag.length; i++) {
            final int index = i;
            chk_diag[i].addActionListener(e -> {
                for (int j = 0; j < chk_diag.length; j++) {
                    if (j != index) {
                        chk_diag[j].setSelected(false);
                    }
                }
                
//                if (chk_diag[index].isSelected()) {
//                    cekDiagnosaUtama(index);
//                }
            });
        }
        
        for (int i = 0; i < chk_diag2.length; i++) {
            final int index = i;
            chk_diag2[i].addActionListener(e -> {
                for (int j = 0; j < chk_diag2.length; j++) {
                    if (j != index) {
                        chk_diag2[j].setSelected(false);
                    }
                }
            });
        }
        
        tabModeDiagnosa=new DefaultTableModel(null,new Object[]{
            "P","Kode","Nama Penyakit","ACCPDX","ASTERISK","VALIDCODE"}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbDiagnosa.setModel(tabModeDiagnosa);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosa.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDiagnosa.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 6; i++) {
            TableColumn column = tbDiagnosa.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(350);
            }else if(i==3){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==4){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbDiagnosa.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDiagnosa2=new DefaultTableModel(null,new Object[]{
            "P","Kode","Nama Penyakit"}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbDiagnosa2.setModel(tabModeDiagnosa2);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosa2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDiagnosa2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 3; i++) {
            TableColumn column = tbDiagnosa2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbDiagnosa2.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeProsedur=new DefaultTableModel(null,new Object[]{
            "P","Kode","Deskripsi Panjang","ACCPDX","ASTERISK","VALIDCODE"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbProsedur.setModel(tabModeProsedur);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedur.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbProsedur.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 6; i++) {
            TableColumn column = tbProsedur.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(50);
            }else if(i==2){
                column.setPreferredWidth(350);
            }else if(i==3){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==4){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbProsedur.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeProsedur2=new DefaultTableModel(null,new Object[]{
            "P","Kode","Deskripsi Panjang"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbProsedur2.setModel(tabModeProsedur2);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedur2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbProsedur2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbProsedur2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(50);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbProsedur2.setDefaultRenderer(Object.class, new WarnaTable());
        
        setupAutoCalculation();
        
    }
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        no_rawat = new widget.TextBox();
        Scroll9 = new widget.ScrollPane();
        FormDATAKLAIM = new widget.panelisi();
        Panel1 = new javax.swing.JPanel();
        TPasien = new widget.TextBox();
        tgl_masuk = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel49 = new widget.Label();
        no_kartu = new widget.TextBox();
        cara_masuk = new widget.ComboBox();
        sistole = new widget.TextBox();
        jenis_rawat = new widget.ComboBox();
        cob = new widget.ComboBox();
        jaminan = new widget.ComboBox();
        jLabel9 = new widget.Label();
        jLabel58 = new widget.Label();
        jLabel61 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel68 = new widget.Label();
        hak_kelas = new widget.ComboBox();
        jLabel88 = new widget.Label();
        jam_masuk = new widget.ComboBox();
        menit_masuk = new widget.ComboBox();
        tgl_keluar = new widget.Tanggal();
        jLabel89 = new widget.Label();
        jam_keluar = new widget.ComboBox();
        menit_keluar = new widget.ComboBox();
        rawat_intensif = new javax.swing.JCheckBox();
        jLabel90 = new widget.Label();
        ventilator = new javax.swing.JCheckBox();
        jLabel91 = new widget.Label();
        naik_turun_kelas = new javax.swing.JCheckBox();
        naik_kelas_pelayanan = new widget.ComboBox();
        jLabel216 = new widget.Label();
        lama_naik_kelas = new widget.TextBox();
        jLabel217 = new widget.Label();
        jLabel218 = new widget.Label();
        jLabel219 = new widget.Label();
        chronic = new widget.TextBox();
        sub_acute = new widget.TextBox();
        jLabel220 = new widget.Label();
        berat_bayi = new widget.TextBox();
        jLabel221 = new widget.Label();
        dpjp = new widget.TextBox();
        jLabel222 = new widget.Label();
        validasi_tb = new widget.Button();
        status_pulang = new widget.ComboBox();
        jLabel223 = new widget.Label();
        kelas_eksekutif = new javax.swing.JCheckBox();
        tgl_intubasi = new widget.Tanggal();
        jam_intubasi = new widget.ComboBox();
        menit_intubasi = new widget.ComboBox();
        menit_ekstubasi = new widget.ComboBox();
        jam_ekstubasi = new widget.ComboBox();
        tgl_ekstubasi = new widget.Tanggal();
        jLabel224 = new widget.Label();
        hari_intensif = new widget.TextBox();
        jLabel225 = new widget.Label();
        jLabel226 = new widget.Label();
        distole = new widget.TextBox();
        tarif_eksekutif = new widget.TextBox();
        jLabel227 = new widget.Label();
        tgl_lahir = new widget.TextBox();
        jLabel228 = new widget.Label();
        jenis_kelamin = new widget.TextBox();
        jLabel229 = new widget.Label();
        jam_ventilator = new widget.TextBox();
        jLabel230 = new widget.Label();
        Tsep = new widget.TextBox();
        pasien_tb = new widget.TextBox();
        batal_validasi_tb = new widget.Button();
        pasien_hd = new widget.ComboBox();
        jLabel233 = new widget.Label();
        kt_darah = new widget.TextBox();
        jLabel234 = new widget.Label();
        check_hd = new javax.swing.JCheckBox();
        check_tb = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        Scroll4 = new widget.ScrollPane();
        FormIDRG1 = new widget.panelisi();
        pnb = new widget.TextBox();
        ahli = new widget.TextBox();
        rehab = new widget.TextBox();
        rad = new widget.TextBox();
        alkes = new widget.TextBox();
        obat = new widget.TextBox();
        pb = new widget.TextBox();
        kpr = new widget.TextBox();
        akomodasi = new widget.TextBox();
        lab = new widget.TextBox();
        bmhp = new widget.TextBox();
        kronis = new widget.TextBox();
        ksl = new widget.TextBox();
        pnj = new widget.TextBox();
        ri = new widget.TextBox();
        pl_darah = new widget.TextBox();
        sewa_alat = new widget.TextBox();
        obat_kemo = new widget.TextBox();
        jLabel71 = new widget.Label();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        jLabel78 = new widget.Label();
        jLabel80 = new widget.Label();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        jLabel86 = new widget.Label();
        jLabel87 = new widget.Label();
        jLabel231 = new widget.Label();
        jLabel232 = new widget.Label();
        tarifRS = new widget.Label();
        lbltarifrs = new widget.Label();
        jLabel235 = new widget.Label();
        jLabel237 = new widget.Label();
        Appear1 = new widget.TextBox();
        Pulse1 = new widget.TextBox();
        Grimace1 = new widget.TextBox();
        Activity1 = new widget.TextBox();
        Respiration1 = new widget.TextBox();
        jLabel238 = new widget.Label();
        jLabel239 = new widget.Label();
        jLabel240 = new widget.Label();
        jLabel241 = new widget.Label();
        jLabel242 = new widget.Label();
        label1 = new widget.Label();
        label3 = new widget.Label();
        lamaharirawat = new widget.TextBox();
        lamajamrawat = new widget.TextBox();
        label4 = new widget.Label();
        label2 = new widget.Label();
        Appear5 = new widget.TextBox();
        Pulse5 = new widget.TextBox();
        Grimace5 = new widget.TextBox();
        Activity5 = new widget.TextBox();
        Respiration5 = new widget.TextBox();
        Apgar = new javax.swing.JCheckBox();
        Kehamilan = new javax.swing.JCheckBox();
        Abortus = new widget.TextBox();
        label7 = new widget.Label();
        label8 = new widget.Label();
        UsiaKehamilan = new widget.TextBox();
        Gravida = new widget.TextBox();
        Partus = new widget.TextBox();
        label10 = new widget.Label();
        label11 = new widget.Label();
        label12 = new widget.Label();
        Kontraksi = new widget.ComboBox();
        label13 = new widget.Label();
        label14 = new widget.Label();
        label5 = new widget.Label();
        lblStatusEncounter = new widget.Label();
        lblStatusKlaim = new widget.Label();
        TabRawat = new javax.swing.JTabbedPane();
        FormIDRG = new widget.panelisi();
        jLabel111 = new widget.Label();
        nm_diag_1 = new widget.TextBox();
        jLabel121 = new widget.Label();
        kd_diag_1 = new widget.TextBox();
        nm_diag_2 = new widget.TextBox();
        kd_diag_2 = new widget.TextBox();
        nm_diag_3 = new widget.TextBox();
        kd_diag_3 = new widget.TextBox();
        nm_diag_4 = new widget.TextBox();
        kd_diag_4 = new widget.TextBox();
        nm_diag_5 = new widget.TextBox();
        kd_diag_5 = new widget.TextBox();
        nm_psdr_1 = new widget.TextBox();
        kd_psdr_1 = new widget.TextBox();
        nm_psdr_2 = new widget.TextBox();
        kd_psdr_2 = new widget.TextBox();
        nm_psdr_3 = new widget.TextBox();
        kd_psdr_3 = new widget.TextBox();
        nm_psdr_4 = new widget.TextBox();
        kd_psdr_4 = new widget.TextBox();
        nm_psdr_5 = new widget.TextBox();
        kd_psdr_5 = new widget.TextBox();
        chk_diag_1 = new javax.swing.JCheckBox();
        chk_diag_2 = new javax.swing.JCheckBox();
        chk_diag_3 = new javax.swing.JCheckBox();
        chk_diag_4 = new javax.swing.JCheckBox();
        chk_diag_5 = new javax.swing.JCheckBox();
        nm_diag_6 = new widget.TextBox();
        kd_diag_6 = new widget.TextBox();
        nm_diag_7 = new widget.TextBox();
        kd_diag_7 = new widget.TextBox();
        nm_diag_8 = new widget.TextBox();
        kd_diag_8 = new widget.TextBox();
        nm_diag_9 = new widget.TextBox();
        kd_diag_9 = new widget.TextBox();
        nm_diag_10 = new widget.TextBox();
        kd_diag_10 = new widget.TextBox();
        chk_diag_6 = new javax.swing.JCheckBox();
        chk_diag_7 = new javax.swing.JCheckBox();
        chk_diag_8 = new javax.swing.JCheckBox();
        chk_diag_9 = new javax.swing.JCheckBox();
        chk_diag_10 = new javax.swing.JCheckBox();
        nm_psdr_6 = new widget.TextBox();
        kd_psdr_6 = new widget.TextBox();
        nm_psdr_7 = new widget.TextBox();
        kd_psdr_7 = new widget.TextBox();
        nm_psdr_8 = new widget.TextBox();
        kd_psdr_8 = new widget.TextBox();
        nm_psdr_9 = new widget.TextBox();
        kd_psdr_9 = new widget.TextBox();
        nm_psdr_10 = new widget.TextBox();
        kd_psdr_10 = new widget.TextBox();
        multiple1 = new widget.TextBox();
        multiple2 = new widget.TextBox();
        multiple3 = new widget.TextBox();
        multiple4 = new widget.TextBox();
        multiple5 = new widget.TextBox();
        multiple6 = new widget.TextBox();
        multiple7 = new widget.TextBox();
        multiple8 = new widget.TextBox();
        multiple9 = new widget.TextBox();
        multiple10 = new widget.TextBox();
        batal_1 = new widget.Button();
        batal_2 = new widget.Button();
        batal_3 = new widget.Button();
        batal_4 = new widget.Button();
        batal_5 = new widget.Button();
        batal_6 = new widget.Button();
        batal_7 = new widget.Button();
        batal_8 = new widget.Button();
        batal_9 = new widget.Button();
        batal_10 = new widget.Button();
        batal_psdr_1 = new widget.Button();
        batal_psdr_2 = new widget.Button();
        batal_psdr_3 = new widget.Button();
        batal_psdr_4 = new widget.Button();
        batal_psdr_5 = new widget.Button();
        batal_psdr_6 = new widget.Button();
        batal_psdr_7 = new widget.Button();
        batal_psdr_8 = new widget.Button();
        batal_psdr_9 = new widget.Button();
        batal_psdr_10 = new widget.Button();
        jLabel92 = new widget.Label();
        Diagnosa = new widget.TextBox();
        idrgPenyakit = new widget.Button();
        idrgPenyakittambah = new widget.Button();
        Scroll5 = new widget.ScrollPane();
        tbDiagnosa = new widget.Table();
        Scroll6 = new widget.ScrollPane();
        tbProsedur = new widget.Table();
        jLabel93 = new widget.Label();
        Prosedur = new widget.TextBox();
        idrgProsedur = new widget.Button();
        idrgProseduttambah = new widget.Button();
        responIDRG = new widget.Label();
        jLabel127 = new widget.Label();
        jLabel129 = new widget.Label();
        kd_psdr_21 = new widget.TextBox();
        nm_psdr_21 = new widget.TextBox();
        multiple11 = new widget.TextBox();
        batal_psdr_21 = new widget.Button();
        kd_psdr_22 = new widget.TextBox();
        nm_psdr_22 = new widget.TextBox();
        multiple12 = new widget.TextBox();
        batal_psdr_22 = new widget.Button();
        kd_psdr_23 = new widget.TextBox();
        nm_psdr_23 = new widget.TextBox();
        multiple13 = new widget.TextBox();
        batal_psdr_23 = new widget.Button();
        kd_psdr_24 = new widget.TextBox();
        nm_psdr_24 = new widget.TextBox();
        multiple14 = new widget.TextBox();
        batal_psdr_24 = new widget.Button();
        kd_psdr_25 = new widget.TextBox();
        nm_psdr_25 = new widget.TextBox();
        multiple15 = new widget.TextBox();
        batal_psdr_25 = new widget.Button();
        kd_psdr_26 = new widget.TextBox();
        nm_psdr_26 = new widget.TextBox();
        multiple16 = new widget.TextBox();
        batal_psdr_26 = new widget.Button();
        kd_psdr_27 = new widget.TextBox();
        nm_psdr_27 = new widget.TextBox();
        multiple17 = new widget.TextBox();
        batal_psdr_27 = new widget.Button();
        kd_psdr_28 = new widget.TextBox();
        nm_psdr_28 = new widget.TextBox();
        multiple18 = new widget.TextBox();
        batal_psdr_28 = new widget.Button();
        kd_psdr_29 = new widget.TextBox();
        nm_psdr_29 = new widget.TextBox();
        multiple19 = new widget.TextBox();
        batal_psdr_29 = new widget.Button();
        kd_psdr_30 = new widget.TextBox();
        nm_psdr_30 = new widget.TextBox();
        multiple20 = new widget.TextBox();
        batal_psdr_30 = new widget.Button();
        nm_diag_21 = new widget.TextBox();
        kd_diag_21 = new widget.TextBox();
        nm_diag_22 = new widget.TextBox();
        kd_diag_22 = new widget.TextBox();
        nm_diag_23 = new widget.TextBox();
        kd_diag_23 = new widget.TextBox();
        nm_diag_24 = new widget.TextBox();
        kd_diag_24 = new widget.TextBox();
        nm_diag_25 = new widget.TextBox();
        kd_diag_25 = new widget.TextBox();
        chk_diag_21 = new javax.swing.JCheckBox();
        chk_diag_22 = new javax.swing.JCheckBox();
        chk_diag_23 = new javax.swing.JCheckBox();
        chk_diag_24 = new javax.swing.JCheckBox();
        chk_diag_25 = new javax.swing.JCheckBox();
        nm_diag_26 = new widget.TextBox();
        kd_diag_26 = new widget.TextBox();
        nm_diag_27 = new widget.TextBox();
        kd_diag_27 = new widget.TextBox();
        nm_diag_28 = new widget.TextBox();
        kd_diag_28 = new widget.TextBox();
        nm_diag_29 = new widget.TextBox();
        kd_diag_29 = new widget.TextBox();
        nm_diag_30 = new widget.TextBox();
        kd_diag_30 = new widget.TextBox();
        chk_diag_26 = new javax.swing.JCheckBox();
        chk_diag_27 = new javax.swing.JCheckBox();
        chk_diag_28 = new javax.swing.JCheckBox();
        chk_diag_29 = new javax.swing.JCheckBox();
        chk_diag_30 = new javax.swing.JCheckBox();
        batal_21 = new widget.Button();
        batal_22 = new widget.Button();
        batal_23 = new widget.Button();
        batal_24 = new widget.Button();
        batal_25 = new widget.Button();
        batal_26 = new widget.Button();
        batal_27 = new widget.Button();
        batal_28 = new widget.Button();
        batal_29 = new widget.Button();
        batal_30 = new widget.Button();
        kd_diag_41 = new widget.TextBox();
        nm_diag_41 = new widget.TextBox();
        chk_diag_41 = new javax.swing.JCheckBox();
        batal_41 = new widget.Button();
        kd_psdr_31 = new widget.TextBox();
        nm_psdr_31 = new widget.TextBox();
        multiple21 = new widget.TextBox();
        batal_psdr_31 = new widget.Button();
        FormINACBG = new widget.panelisi();
        jLabel94 = new widget.Label();
        DiagnosaINACBG = new widget.TextBox();
        INAdiagnosa = new widget.Button();
        INAdiagnosatambah = new widget.Button();
        Scroll7 = new widget.ScrollPane();
        tbDiagnosa2 = new widget.Table();
        Scroll8 = new widget.ScrollPane();
        tbProsedur2 = new widget.Table();
        jLabel95 = new widget.Label();
        ProsedurINACBG = new widget.TextBox();
        INAprosedur = new widget.Button();
        INAprosedurtambah = new widget.Button();
        jLabel183 = new widget.Label();
        jLabel113 = new widget.Label();
        nm_diag_11 = new widget.TextBox();
        jLabel125 = new widget.Label();
        kd_diag_11 = new widget.TextBox();
        nm_diag_12 = new widget.TextBox();
        kd_diag_12 = new widget.TextBox();
        nm_diag_13 = new widget.TextBox();
        kd_diag_13 = new widget.TextBox();
        nm_diag_14 = new widget.TextBox();
        kd_diag_14 = new widget.TextBox();
        nm_diag_15 = new widget.TextBox();
        kd_diag_15 = new widget.TextBox();
        nm_psdr_11 = new widget.TextBox();
        kd_psdr_11 = new widget.TextBox();
        nm_psdr_12 = new widget.TextBox();
        kd_psdr_12 = new widget.TextBox();
        nm_psdr_13 = new widget.TextBox();
        kd_psdr_13 = new widget.TextBox();
        nm_psdr_14 = new widget.TextBox();
        kd_psdr_14 = new widget.TextBox();
        nm_psdr_15 = new widget.TextBox();
        kd_psdr_15 = new widget.TextBox();
        chk_diag_11 = new javax.swing.JCheckBox();
        chk_diag_12 = new javax.swing.JCheckBox();
        chk_diag_13 = new javax.swing.JCheckBox();
        chk_diag_14 = new javax.swing.JCheckBox();
        chk_diag_15 = new javax.swing.JCheckBox();
        jLabel126 = new widget.Label();
        nm_diag_16 = new widget.TextBox();
        kd_diag_16 = new widget.TextBox();
        nm_diag_17 = new widget.TextBox();
        kd_diag_17 = new widget.TextBox();
        nm_diag_18 = new widget.TextBox();
        kd_diag_18 = new widget.TextBox();
        nm_diag_19 = new widget.TextBox();
        kd_diag_19 = new widget.TextBox();
        nm_diag_20 = new widget.TextBox();
        kd_diag_20 = new widget.TextBox();
        chk_diag_16 = new javax.swing.JCheckBox();
        chk_diag_17 = new javax.swing.JCheckBox();
        chk_diag_18 = new javax.swing.JCheckBox();
        chk_diag_19 = new javax.swing.JCheckBox();
        chk_diag_20 = new javax.swing.JCheckBox();
        nm_psdr_16 = new widget.TextBox();
        kd_psdr_16 = new widget.TextBox();
        nm_psdr_17 = new widget.TextBox();
        kd_psdr_17 = new widget.TextBox();
        nm_psdr_18 = new widget.TextBox();
        kd_psdr_18 = new widget.TextBox();
        nm_psdr_19 = new widget.TextBox();
        kd_psdr_19 = new widget.TextBox();
        nm_psdr_20 = new widget.TextBox();
        kd_psdr_20 = new widget.TextBox();
        batal_11 = new widget.Button();
        batal_12 = new widget.Button();
        batal_13 = new widget.Button();
        batal_14 = new widget.Button();
        batal_15 = new widget.Button();
        batal_16 = new widget.Button();
        batal_17 = new widget.Button();
        batal_18 = new widget.Button();
        batal_19 = new widget.Button();
        batal_20 = new widget.Button();
        batal_psdr_11 = new widget.Button();
        batal_psdr_12 = new widget.Button();
        batal_psdr_13 = new widget.Button();
        batal_psdr_14 = new widget.Button();
        batal_psdr_15 = new widget.Button();
        batal_psdr_16 = new widget.Button();
        batal_psdr_17 = new widget.Button();
        batal_psdr_18 = new widget.Button();
        batal_psdr_19 = new widget.Button();
        batal_psdr_20 = new widget.Button();
        Import = new widget.Button();
        scrollRespon1 = new widget.ScrollPane();
        panelGlass9 = new widget.panelisi();
        cmbDrugs = new widget.ComboBox();
        jLabel96 = new widget.Label();
        jLabel97 = new widget.Label();
        jLabel98 = new widget.Label();
        jLabel99 = new widget.Label();
        jLabel100 = new widget.Label();
        jLabel101 = new widget.Label();
        jLabel102 = new widget.Label();
        jLabel103 = new widget.Label();
        jLabel104 = new widget.Label();
        lChronic = new widget.Label();
        jLabel105 = new widget.Label();
        cmbProc = new widget.ComboBox();
        cmbPros = new widget.ComboBox();
        cmbInv = new widget.ComboBox();
        lSubacute = new widget.Label();
        jLabel107 = new widget.Label();
        jLabel108 = new widget.Label();
        jLabel109 = new widget.Label();
        jLabel110 = new widget.Label();
        jLabel112 = new widget.Label();
        jLabel114 = new widget.Label();
        jLabel115 = new widget.Label();
        lblTarifKronis = new widget.Label();
        lblTarifGrup = new widget.Label();
        lblTarifAcute = new widget.Label();
        lbltarifProc = new widget.Label();
        lblTarifPros = new widget.Label();
        lblTarifDrugs = new widget.Label();
        lblTarifInv = new widget.Label();
        lblTarifTotal = new widget.Label();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jSeparator22 = new javax.swing.JSeparator();
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        grupCode = new widget.Label();
        jSeparator26 = new javax.swing.JSeparator();
        jLabel117 = new widget.Label();
        lblTarifRS = new widget.Label();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        lblBaseTariff = new widget.Label();
        jSeparator27 = new javax.swing.JSeparator();
        scrollPane1 = new widget.ScrollPane();
        Txtgroup = new widget.TextArea();
        jLabel120 = new widget.Label();
        jLabel122 = new widget.Label();
        lblSelisih = new widget.Label();
        jSeparator28 = new javax.swing.JSeparator();
        lJenis1 = new widget.Label();
        cmbSelisih = new widget.ComboBox();
        jLabel116 = new widget.Label();
        lblCodeDrugs = new widget.Label();
        lblCodeProc = new widget.Label();
        lblCodePros = new widget.Label();
        lblCodeInv = new widget.Label();
        nm_psdr_36 = new widget.TextBox();
        kd_psdr_36 = new widget.TextBox();
        nm_psdr_37 = new widget.TextBox();
        kd_psdr_37 = new widget.TextBox();
        nm_psdr_38 = new widget.TextBox();
        kd_psdr_38 = new widget.TextBox();
        nm_psdr_39 = new widget.TextBox();
        kd_psdr_39 = new widget.TextBox();
        nm_psdr_40 = new widget.TextBox();
        kd_psdr_40 = new widget.TextBox();
        nm_psdr_41 = new widget.TextBox();
        kd_psdr_41 = new widget.TextBox();
        nm_psdr_42 = new widget.TextBox();
        kd_psdr_42 = new widget.TextBox();
        nm_psdr_43 = new widget.TextBox();
        kd_psdr_43 = new widget.TextBox();
        nm_psdr_44 = new widget.TextBox();
        kd_psdr_44 = new widget.TextBox();
        nm_psdr_45 = new widget.TextBox();
        kd_psdr_45 = new widget.TextBox();
        batal_psdr_36 = new widget.Button();
        batal_psdr_37 = new widget.Button();
        batal_psdr_38 = new widget.Button();
        batal_psdr_39 = new widget.Button();
        batal_psdr_40 = new widget.Button();
        batal_psdr_41 = new widget.Button();
        batal_psdr_42 = new widget.Button();
        batal_psdr_43 = new widget.Button();
        batal_psdr_44 = new widget.Button();
        batal_psdr_45 = new widget.Button();
        nm_diag_31 = new widget.TextBox();
        kd_diag_31 = new widget.TextBox();
        nm_diag_32 = new widget.TextBox();
        kd_diag_32 = new widget.TextBox();
        nm_diag_33 = new widget.TextBox();
        kd_diag_33 = new widget.TextBox();
        nm_diag_34 = new widget.TextBox();
        kd_diag_34 = new widget.TextBox();
        nm_diag_35 = new widget.TextBox();
        kd_diag_35 = new widget.TextBox();
        chk_diag_31 = new javax.swing.JCheckBox();
        chk_diag_32 = new javax.swing.JCheckBox();
        chk_diag_33 = new javax.swing.JCheckBox();
        chk_diag_34 = new javax.swing.JCheckBox();
        chk_diag_35 = new javax.swing.JCheckBox();
        nm_diag_36 = new widget.TextBox();
        kd_diag_36 = new widget.TextBox();
        nm_diag_37 = new widget.TextBox();
        kd_diag_37 = new widget.TextBox();
        nm_diag_38 = new widget.TextBox();
        kd_diag_38 = new widget.TextBox();
        nm_diag_39 = new widget.TextBox();
        kd_diag_39 = new widget.TextBox();
        nm_diag_40 = new widget.TextBox();
        kd_diag_40 = new widget.TextBox();
        chk_diag_36 = new javax.swing.JCheckBox();
        chk_diag_37 = new javax.swing.JCheckBox();
        chk_diag_38 = new javax.swing.JCheckBox();
        chk_diag_39 = new javax.swing.JCheckBox();
        chk_diag_40 = new javax.swing.JCheckBox();
        batal_31 = new widget.Button();
        batal_32 = new widget.Button();
        batal_33 = new widget.Button();
        batal_34 = new widget.Button();
        batal_35 = new widget.Button();
        batal_36 = new widget.Button();
        batal_37 = new widget.Button();
        batal_38 = new widget.Button();
        batal_39 = new widget.Button();
        batal_40 = new widget.Button();
        kd_diag_42 = new widget.TextBox();
        nm_diag_42 = new widget.TextBox();
        chk_diag_42 = new javax.swing.JCheckBox();
        batal_42 = new widget.Button();
        kd_psdr_46 = new widget.TextBox();
        nm_psdr_46 = new widget.TextBox();
        batal_psdr_46 = new widget.Button();
        panelBawah = new widget.panelisi();
        hapusKlaim4 = new widget.Button();
        BtnGrouperIDRG = new widget.Button();
        finalklaimIDRG = new widget.Button();
        editKlaimIDRG = new widget.Button();
        BtnGrouperINA = new widget.Button();
        finalINA = new widget.Button();
        editINA = new widget.Button();
        finalklaim = new widget.Button();
        editKlaim = new widget.Button();
        kirimonline = new widget.Button();
        CetakKlaim = new widget.Button();
        BtnKeluar4 = new widget.Button();

        no_rawat.setHighlighter(null);
        no_rawat.setName("no_rawat"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);
        Scroll9.setPreferredSize(new java.awt.Dimension(500, 770));

        FormDATAKLAIM.setName("FormDATAKLAIM"); // NOI18N
        FormDATAKLAIM.setPreferredSize(new java.awt.Dimension(1000, 1600));
        FormDATAKLAIM.setLayout(new java.awt.BorderLayout());

        Panel1.setBackground(new java.awt.Color(255, 255, 255));
        Panel1.setName("Panel1"); // NOI18N
        Panel1.setPreferredSize(new java.awt.Dimension(1300, 483));
        Panel1.setLayout(null);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        Panel1.add(TPasien);
        TPasien.setBounds(220, 10, 230, 23);

        tgl_masuk.setEditable(false);
        tgl_masuk.setForeground(new java.awt.Color(50, 70, 50));
        tgl_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-06-24" }));
        tgl_masuk.setDisplayFormat("yyyy-MM-dd");
        tgl_masuk.setName("tgl_masuk"); // NOI18N
        tgl_masuk.setOpaque(false);
        Panel1.add(tgl_masuk);
        tgl_masuk.setBounds(120, 100, 100, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        Panel1.add(TNoRM);
        TNoRM.setBounds(120, 10, 90, 23);

        jLabel25.setText("Distole : ");
        jLabel25.setName("jLabel25"); // NOI18N
        Panel1.add(jLabel25);
        jLabel25.setBounds(110, 390, 60, 23);

        jLabel49.setText("Intubasi :");
        jLabel49.setName("jLabel49"); // NOI18N
        Panel1.add(jLabel49);
        jLabel49.setBounds(190, 130, 70, 23);

        no_kartu.setEditable(false);
        no_kartu.setHighlighter(null);
        no_kartu.setName("no_kartu"); // NOI18N
        Panel1.add(no_kartu);
        no_kartu.setBounds(490, 10, 120, 23);

        cara_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "gp : Rujukan FKTP", "hosp-trans : Rujukan FKRTL", "mp : Rujukan Spesialis", "outp : Dari Rawat Jalan", "inp : Dari Rawat Inap", "emd : Dari Rawat Darurat", "born : Lahir di RS", "nursing : Rujukan Panti Jompo", "psych : Rujukan dari RS Jiwa", "rehab : Rujukan Fasilitas Rehab", "other : Lain-lain" }));
        cara_masuk.setName("cara_masuk"); // NOI18N
        Panel1.add(cara_masuk);
        cara_masuk.setBounds(740, 40, 270, 23);

        sistole.setHighlighter(null);
        sistole.setName("sistole"); // NOI18N
        sistole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sistoleActionPerformed(evt);
            }
        });
        Panel1.add(sistole);
        sistole.setBounds(170, 360, 50, 23);

        jenis_rawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : RAWAT INAP", "2 : RAWAT JALAN", "3 : IGD" }));
        jenis_rawat.setSelectedIndex(1);
        jenis_rawat.setName("jenis_rawat"); // NOI18N
        jenis_rawat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenis_rawatItemStateChanged(evt);
            }
        });
        Panel1.add(jenis_rawat);
        jenis_rawat.setBounds(120, 70, 150, 23);

        cob.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "# : TIDAK COB", "0001 : MANDIRI INHEALTH", "0005 : ASURANSI SINAR MAS", "0006 : ASURANSI TUGU MANDIRI", "0007 : ASURANSI MITRA MAPARYA", "0008 : ASURANSI AXA MANDIRI FINANSIAL SERVICE", "0009 : ASURANSI AXA FINANSIAL INDONESIA", "0010 : LIPPO GENERAL INSURANCE", "0011 : ARTHAGRAHA GENERAL INSURANSE", "0012 : TUGU PRATAMA INDONESIA", "0013 : ASURANSI BINA DANA ARTA", "0014 : ASURANSI JIWA SINAR MAS MSIG", "0015 : AVRIST ASSURANCE", "0016 : ASURANSI JIWA SRAYA", "0017 : ASURANSI JIWA CENTRAL ASIA RAYA", "0018 : ASURANSI TAKAFUL KELUARGA", "0019 : ASURANSI JIWA GENERALI INDONESIA", "0020 : ASURANSI ASTRA BUANA", "0021 : ASURANSI UMUM MEGA", "0022 : ASURANSI MULTI ARTHA GUNA", "0023 : ASURANSI AIA INDONESIA", "0024 : ASURANSI JIWA EQUITY LIFE INDONESIA", "0025 : ASURANSI JIWA RECAPITAL", "0026 : GREAT EASTERN LIFE INDONESIA", "0027 : ASURANSI ADISARANA WANAARTHA", "0028 : ASURANSI JIWA BRINGIN JIWA SEJAHTERA", "0029 : BOSOWA ASURANSI", "0030 : MNC LIFE ASSURANCE", "0031 : ASURANSI AVIVA INDONESIA", "0032 : ASURANSI CENTRAL ASIA RAYA", "0033 : ASURANSI ALLIANZ LIFE INDONESIA", "0034 : ASURANSI BINTANG", "0035 : TOKIO MARINE LIFE INSURANCE INDONESIA", "0036 : MALACCA TRUST WUWUNGAN", "0037 : ASURANSI JASA INDONESIA", "0038 : ASURANSI JIWA MANULIFE INDONESIA", "0039 : ASURANSI BANGUN ASKRIDA", "0040 : ASURANSI JIWA SEQUIS FINANCIAL", "0041 : ASURANSI AXA INDONESIA", "0042 : BNI LIFE", "0043 : ACE LIFE INSURANCE", "0044 : CITRA INTERNATIONAL UNDERWRITERS", "0045 : ASURANSI RELIANCE INDONESIA", "0046 : HANWHA LIFE INSURANCE INDONESIA", "0047 : ASURANSI DAYIN MITRA", "0048 : ASURANSI ADIRA DINAMIKA", "0049 : PAN PASIFIC INSURANCE", "0050 : ASURANSI SAMSUNG TUGU", "0051 : ASURANSI UMUM BUMI PUTERA MUDA 1967", "0052 : ASURANSI JIWA KRESNA", "0053 : ASURANSI RAMAYANA", "0054 : VICTORIA INSURANCE", "0055 : ASURANSI JIWA BERSAMA BUMIPUTERA 1912", "0056 : FWD LIFE INDONESIA", "0057 : ASURANSI TAKAFUL KELUARGA", "0058 : ASURANSI TUGU KRESNA PRATAMA", "0059 : SOMPO INSURANCE" }));
        cob.setName("cob"); // NOI18N
        Panel1.add(cob);
        cob.setBounds(740, 10, 270, 23);

        jaminan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 : JKN ", "71 : JAMINAN COVID-19", "72 : JAMINAN KIPI", "73 : JAMINAN BAYI BARU LAHIR", "74 : JAMINAN PERPANJANGAN MASA RAWAT", "75 : JAMINAN CO-INSIDENSE", "76 : JAMPERSAL", "77 : JAMINAN PEMULIHAN KESEHATAN PRIORITAS" }));
        jaminan.setName("jaminan"); // NOI18N
        Panel1.add(jaminan);
        jaminan.setBounds(120, 40, 200, 23);

        jLabel9.setText("No.Rekam Medik :");
        jLabel9.setName("jLabel9"); // NOI18N
        Panel1.add(jLabel9);
        jLabel9.setBounds(10, 10, 100, 23);

        jLabel58.setText("Jaminan :");
        jLabel58.setName("jLabel58"); // NOI18N
        Panel1.add(jLabel58);
        jLabel58.setBounds(10, 40, 100, 23);

        jLabel61.setText("COB :");
        jLabel61.setName("jLabel61"); // NOI18N
        Panel1.add(jLabel61);
        jLabel61.setBounds(700, 10, 30, 23);

        jLabel66.setText("Kartu : ");
        jLabel66.setName("jLabel66"); // NOI18N
        Panel1.add(jLabel66);
        jLabel66.setBounds(400, 10, 90, 23);

        jLabel68.setText("Tgl Masuk :");
        jLabel68.setName("jLabel68"); // NOI18N
        Panel1.add(jLabel68);
        jLabel68.setBounds(10, 100, 100, 23);

        hak_kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kelas 1", "Kelas 2", "Kelas 3" }));
        hak_kelas.setSelectedIndex(2);
        hak_kelas.setName("hak_kelas"); // NOI18N
        Panel1.add(hak_kelas);
        hak_kelas.setBounds(880, 70, 130, 23);

        jLabel88.setText("Jenis Rawat :");
        jLabel88.setName("jLabel88"); // NOI18N
        Panel1.add(jLabel88);
        jLabel88.setBounds(10, 70, 100, 23);

        jam_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_masuk.setName("jam_masuk"); // NOI18N
        Panel1.add(jam_masuk);
        jam_masuk.setBounds(230, 100, 62, 23);

        menit_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_masuk.setName("menit_masuk"); // NOI18N
        Panel1.add(menit_masuk);
        menit_masuk.setBounds(290, 100, 62, 23);

        tgl_keluar.setEditable(false);
        tgl_keluar.setForeground(new java.awt.Color(50, 70, 50));
        tgl_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-06-24" }));
        tgl_keluar.setDisplayFormat("yyyy-MM-dd");
        tgl_keluar.setName("tgl_keluar"); // NOI18N
        tgl_keluar.setOpaque(false);
        Panel1.add(tgl_keluar);
        tgl_keluar.setBounds(420, 100, 100, 23);

        jLabel89.setText("Tgl Keluar :");
        jLabel89.setName("jLabel89"); // NOI18N
        Panel1.add(jLabel89);
        jLabel89.setBounds(300, 100, 115, 23);

        jam_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_keluar.setName("jam_keluar"); // NOI18N
        Panel1.add(jam_keluar);
        jam_keluar.setBounds(530, 100, 62, 23);

        menit_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_keluar.setName("menit_keluar"); // NOI18N
        Panel1.add(menit_keluar);
        menit_keluar.setBounds(590, 100, 62, 23);

        rawat_intensif.setBackground(new java.awt.Color(255, 255, 255));
        rawat_intensif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        rawat_intensif.setText("Ada Rawat Intensif");
        rawat_intensif.setName("rawat_intensif"); // NOI18N
        rawat_intensif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rawat_intensifItemStateChanged(evt);
            }
        });
        Panel1.add(rawat_intensif);
        rawat_intensif.setBounds(550, 70, 140, 22);

        jLabel90.setText("ADL Score :");
        jLabel90.setName("jLabel90"); // NOI18N
        Panel1.add(jLabel90);
        jLabel90.setBounds(0, 220, 110, 23);

        ventilator.setBackground(new java.awt.Color(255, 255, 255));
        ventilator.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        ventilator.setText("Ventilator");
        ventilator.setName("ventilator"); // NOI18N
        ventilator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ventilatorItemStateChanged(evt);
            }
        });
        Panel1.add(ventilator);
        ventilator.setBounds(120, 130, 90, 22);

        jLabel91.setText("SEP : ");
        jLabel91.setName("jLabel91"); // NOI18N
        Panel1.add(jLabel91);
        jLabel91.setBounds(290, 40, 70, 23);

        naik_turun_kelas.setBackground(new java.awt.Color(255, 255, 255));
        naik_turun_kelas.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        naik_turun_kelas.setText("Naik / Turun Kelas");
        naik_turun_kelas.setName("naik_turun_kelas"); // NOI18N
        naik_turun_kelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                naik_turun_kelasItemStateChanged(evt);
            }
        });
        Panel1.add(naik_turun_kelas);
        naik_turun_kelas.setBounds(410, 70, 130, 22);

        naik_kelas_pelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "kelas_3 : Kelas 3", "kelas_2 : Kelas 2", "kelas_1 : Kelas 1", "vip : Diatas Kelas 1" }));
        naik_kelas_pelayanan.setName("naik_kelas_pelayanan"); // NOI18N
        Panel1.add(naik_kelas_pelayanan);
        naik_kelas_pelayanan.setBounds(760, 100, 120, 23);

        jLabel216.setText("Kelas Pelayanan : ");
        jLabel216.setName("jLabel216"); // NOI18N
        Panel1.add(jLabel216);
        jLabel216.setBounds(660, 100, 100, 23);

        lama_naik_kelas.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lama_naik_kelas.setHighlighter(null);
        lama_naik_kelas.setName("lama_naik_kelas"); // NOI18N
        Panel1.add(lama_naik_kelas);
        lama_naik_kelas.setBounds(960, 100, 50, 23);

        jLabel217.setText("Lama (hari) : ");
        jLabel217.setName("jLabel217"); // NOI18N
        Panel1.add(jLabel217);
        jLabel217.setBounds(890, 100, 70, 23);

        jLabel218.setText("Chronic : ");
        jLabel218.setName("jLabel218"); // NOI18N
        Panel1.add(jLabel218);
        jLabel218.setBounds(220, 220, 90, 23);

        jLabel219.setText("Gram");
        jLabel219.setName("jLabel219"); // NOI18N
        Panel1.add(jLabel219);
        jLabel219.setBounds(860, 160, 30, 23);

        chronic.setText("-");
        chronic.setHighlighter(null);
        chronic.setName("chronic"); // NOI18N
        Panel1.add(chronic);
        chronic.setBounds(310, 220, 60, 23);

        sub_acute.setText("-");
        sub_acute.setHighlighter(null);
        sub_acute.setName("sub_acute"); // NOI18N
        Panel1.add(sub_acute);
        sub_acute.setBounds(180, 220, 60, 23);

        jLabel220.setText("Sub Acute : ");
        jLabel220.setName("jLabel220"); // NOI18N
        Panel1.add(jLabel220);
        jLabel220.setBounds(90, 220, 90, 23);

        berat_bayi.setHighlighter(null);
        berat_bayi.setName("berat_bayi"); // NOI18N
        Panel1.add(berat_bayi);
        berat_bayi.setBounds(760, 160, 90, 23);

        jLabel221.setText("Berat Lahir Bayi : ");
        jLabel221.setName("jLabel221"); // NOI18N
        Panel1.add(jLabel221);
        jLabel221.setBounds(666, 160, 90, 23);

        dpjp.setHighlighter(null);
        dpjp.setName("dpjp"); // NOI18N
        Panel1.add(dpjp);
        dpjp.setBounds(120, 250, 510, 23);

        jLabel222.setText("DPJP : ");
        jLabel222.setName("jLabel222"); // NOI18N
        jLabel222.setPreferredSize(new java.awt.Dimension(60, 16));
        Panel1.add(jLabel222);
        jLabel222.setBounds(10, 250, 100, 23);

        validasi_tb.setBackground(new java.awt.Color(0, 153, 102));
        validasi_tb.setText("Validasi");
        validasi_tb.setToolTipText("");
        validasi_tb.setGlassColor(new java.awt.Color(255, 255, 255));
        validasi_tb.setName("validasi_tb"); // NOI18N
        validasi_tb.setPreferredSize(new java.awt.Dimension(100, 30));
        validasi_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validasi_tbActionPerformed(evt);
            }
        });
        Panel1.add(validasi_tb);
        validasi_tb.setBounds(400, 280, 70, 22);

        status_pulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : Atas persetujuan dokter", "2 : Dirujuk", "3 : Atas permintaan sendiri", "4 : Meninggal", "5 : Lain-lain" }));
        status_pulang.setName("status_pulang"); // NOI18N
        Panel1.add(status_pulang);
        status_pulang.setBounds(720, 190, 170, 23);

        jLabel223.setText("Pulang : ");
        jLabel223.setName("jLabel223"); // NOI18N
        Panel1.add(jLabel223);
        jLabel223.setBounds(652, 190, 60, 23);

        kelas_eksekutif.setBackground(new java.awt.Color(255, 255, 255));
        kelas_eksekutif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        kelas_eksekutif.setText("Kelas Eksekutif");
        kelas_eksekutif.setName("kelas_eksekutif"); // NOI18N
        kelas_eksekutif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kelas_eksekutifItemStateChanged(evt);
            }
        });
        Panel1.add(kelas_eksekutif);
        kelas_eksekutif.setBounds(280, 70, 120, 22);

        tgl_intubasi.setEditable(false);
        tgl_intubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-06-24" }));
        tgl_intubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_intubasi.setName("tgl_intubasi"); // NOI18N
        tgl_intubasi.setOpaque(false);
        Panel1.add(tgl_intubasi);
        tgl_intubasi.setBounds(270, 130, 100, 23);

        jam_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_intubasi.setName("jam_intubasi"); // NOI18N
        Panel1.add(jam_intubasi);
        jam_intubasi.setBounds(380, 130, 62, 23);

        menit_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_intubasi.setName("menit_intubasi"); // NOI18N
        Panel1.add(menit_intubasi);
        menit_intubasi.setBounds(440, 130, 62, 23);

        menit_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_ekstubasi.setName("menit_ekstubasi"); // NOI18N
        Panel1.add(menit_ekstubasi);
        menit_ekstubasi.setBounds(440, 160, 62, 23);

        jam_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_ekstubasi.setName("jam_ekstubasi"); // NOI18N
        Panel1.add(jam_ekstubasi);
        jam_ekstubasi.setBounds(380, 160, 62, 23);

        tgl_ekstubasi.setEditable(false);
        tgl_ekstubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-06-24" }));
        tgl_ekstubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_ekstubasi.setName("tgl_ekstubasi"); // NOI18N
        tgl_ekstubasi.setOpaque(false);
        Panel1.add(tgl_ekstubasi);
        tgl_ekstubasi.setBounds(270, 160, 100, 23);

        jLabel224.setText("Ekstubasi :");
        jLabel224.setName("jLabel224"); // NOI18N
        Panel1.add(jLabel224);
        jLabel224.setBounds(190, 160, 70, 23);

        hari_intensif.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        hari_intensif.setHighlighter(null);
        hari_intensif.setName("hari_intensif"); // NOI18N
        Panel1.add(hari_intensif);
        hari_intensif.setBounds(780, 130, 70, 23);

        jLabel225.setText(" Rawat Intensif (hari) : ");
        jLabel225.setName("jLabel225"); // NOI18N
        Panel1.add(jLabel225);
        jLabel225.setBounds(660, 130, 120, 23);

        jLabel226.setText("Sistole : ");
        jLabel226.setName("jLabel226"); // NOI18N
        Panel1.add(jLabel226);
        jLabel226.setBounds(120, 360, 50, 23);

        distole.setHighlighter(null);
        distole.setName("distole"); // NOI18N
        Panel1.add(distole);
        distole.setBounds(170, 390, 50, 23);

        tarif_eksekutif.setHighlighter(null);
        tarif_eksekutif.setName("tarif_eksekutif"); // NOI18N
        Panel1.add(tarif_eksekutif);
        tarif_eksekutif.setBounds(750, 220, 140, 23);

        jLabel227.setText("Tarif Eksekutif : ");
        jLabel227.setName("jLabel227"); // NOI18N
        Panel1.add(jLabel227);
        jLabel227.setBounds(658, 220, 90, 23);

        tgl_lahir.setEditable(false);
        tgl_lahir.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        tgl_lahir.setEnabled(false);
        tgl_lahir.setHighlighter(null);
        tgl_lahir.setName("tgl_lahir"); // NOI18N
        Panel1.add(tgl_lahir);
        tgl_lahir.setBounds(620, 10, 80, 23);

        jLabel228.setText("Gender : ");
        jLabel228.setName("jLabel228"); // NOI18N
        Panel1.add(jLabel228);
        jLabel228.setBounds(520, 40, 70, 23);

        jenis_kelamin.setEditable(false);
        jenis_kelamin.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jenis_kelamin.setEnabled(false);
        jenis_kelamin.setHighlighter(null);
        jenis_kelamin.setName("jenis_kelamin"); // NOI18N
        Panel1.add(jenis_kelamin);
        jenis_kelamin.setBounds(590, 40, 50, 23);

        jLabel229.setText("Cara Masuk :");
        jLabel229.setName("jLabel229"); // NOI18N
        Panel1.add(jLabel229);
        jLabel229.setBounds(660, 40, 70, 23);

        jam_ventilator.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jam_ventilator.setHighlighter(null);
        jam_ventilator.setName("jam_ventilator"); // NOI18N
        Panel1.add(jam_ventilator);
        jam_ventilator.setBounds(580, 160, 50, 23);

        jLabel230.setText("Berapa Jam  : ");
        jLabel230.setName("jLabel230"); // NOI18N
        Panel1.add(jLabel230);
        jLabel230.setBounds(500, 160, 80, 23);

        Tsep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tsep.setHighlighter(null);
        Tsep.setName("Tsep"); // NOI18N
        Tsep.setOpaque(true);
        Panel1.add(Tsep);
        Tsep.setBounds(360, 40, 180, 23);

        pasien_tb.setHighlighter(null);
        pasien_tb.setName("pasien_tb"); // NOI18N
        Panel1.add(pasien_tb);
        pasien_tb.setBounds(210, 280, 190, 23);

        batal_validasi_tb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_validasi_tb.setToolTipText("");
        batal_validasi_tb.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_validasi_tb.setName("batal_validasi_tb"); // NOI18N
        batal_validasi_tb.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_validasi_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_validasi_tbActionPerformed(evt);
            }
        });
        Panel1.add(batal_validasi_tb);
        batal_validasi_tb.setBounds(470, 280, 40, 22);

        pasien_hd.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MULTIPLE USE", "SINGLE USE" }));
        pasien_hd.setSelectedIndex(1);
        pasien_hd.setName("pasien_hd"); // NOI18N
        Panel1.add(pasien_hd);
        pasien_hd.setBounds(260, 310, 110, 23);

        jLabel233.setText("Dializer : ");
        jLabel233.setName("jLabel233"); // NOI18N
        Panel1.add(jLabel233);
        jLabel233.setBounds(210, 310, 50, 23);

        kt_darah.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kt_darah.setHighlighter(null);
        kt_darah.setName("kt_darah"); // NOI18N
        Panel1.add(kt_darah);
        kt_darah.setBounds(460, 310, 40, 23);

        jLabel234.setText("Kantong Darah : ");
        jLabel234.setName("jLabel234"); // NOI18N
        Panel1.add(jLabel234);
        jLabel234.setBounds(370, 310, 90, 23);

        check_hd.setBackground(new java.awt.Color(255, 255, 255));
        check_hd.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        check_hd.setText("Pasien HD");
        check_hd.setName("check_hd"); // NOI18N
        check_hd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                check_hdItemStateChanged(evt);
            }
        });
        Panel1.add(check_hd);
        check_hd.setBounds(120, 310, 90, 22);

        check_tb.setBackground(new java.awt.Color(255, 255, 255));
        check_tb.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        check_tb.setText("Pasien TB");
        check_tb.setName("check_tb"); // NOI18N
        check_tb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                check_tbItemStateChanged(evt);
            }
        });
        Panel1.add(check_tb);
        check_tb.setBounds(120, 280, 80, 22);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel1.setLayout(null);

        Scroll4.setToolTipText("");
        Scroll4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);
        Scroll4.setPreferredSize(new java.awt.Dimension(300, 170));

        FormIDRG1.setName("FormIDRG1"); // NOI18N
        FormIDRG1.setPreferredSize(new java.awt.Dimension(300, 612));
        FormIDRG1.setLayout(null);

        pnb.setHighlighter(null);
        pnb.setName("pnb"); // NOI18N
        pnb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnbKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pnbKeyTyped(evt);
            }
        });
        FormIDRG1.add(pnb);
        pnb.setBounds(130, 50, 154, 23);

        ahli.setHighlighter(null);
        ahli.setName("ahli"); // NOI18N
        ahli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ahliKeyReleased(evt);
            }
        });
        FormIDRG1.add(ahli);
        ahli.setBounds(130, 80, 154, 23);

        rehab.setHighlighter(null);
        rehab.setName("rehab"); // NOI18N
        rehab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rehabKeyReleased(evt);
            }
        });
        FormIDRG1.add(rehab);
        rehab.setBounds(130, 140, 154, 23);

        rad.setHighlighter(null);
        rad.setName("rad"); // NOI18N
        rad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                radKeyReleased(evt);
            }
        });
        FormIDRG1.add(rad);
        rad.setBounds(130, 110, 154, 23);

        alkes.setHighlighter(null);
        alkes.setName("alkes"); // NOI18N
        alkes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                alkesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alkesKeyTyped(evt);
            }
        });
        FormIDRG1.add(alkes);
        alkes.setBounds(130, 200, 154, 23);

        obat.setHighlighter(null);
        obat.setName("obat"); // NOI18N
        obat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                obatKeyReleased(evt);
            }
        });
        FormIDRG1.add(obat);
        obat.setBounds(130, 170, 154, 23);

        pb.setHighlighter(null);
        pb.setName("pb"); // NOI18N
        pb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pbKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pbKeyReleased(evt);
            }
        });
        FormIDRG1.add(pb);
        pb.setBounds(130, 230, 154, 23);

        kpr.setHighlighter(null);
        kpr.setName("kpr"); // NOI18N
        kpr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kprKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kprKeyReleased(evt);
            }
        });
        FormIDRG1.add(kpr);
        kpr.setBounds(130, 260, 154, 23);

        akomodasi.setHighlighter(null);
        akomodasi.setName("akomodasi"); // NOI18N
        akomodasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                akomodasiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                akomodasiKeyReleased(evt);
            }
        });
        FormIDRG1.add(akomodasi);
        akomodasi.setBounds(130, 320, 154, 23);

        lab.setHighlighter(null);
        lab.setName("lab"); // NOI18N
        lab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                labKeyReleased(evt);
            }
        });
        FormIDRG1.add(lab);
        lab.setBounds(130, 290, 154, 23);

        bmhp.setHighlighter(null);
        bmhp.setName("bmhp"); // NOI18N
        bmhp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bmhpKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bmhpKeyReleased(evt);
            }
        });
        FormIDRG1.add(bmhp);
        bmhp.setBounds(130, 380, 154, 23);

        kronis.setHighlighter(null);
        kronis.setName("kronis"); // NOI18N
        kronis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kronisKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kronisKeyReleased(evt);
            }
        });
        FormIDRG1.add(kronis);
        kronis.setBounds(130, 350, 154, 23);

        ksl.setHighlighter(null);
        ksl.setName("ksl"); // NOI18N
        ksl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kslKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kslKeyReleased(evt);
            }
        });
        FormIDRG1.add(ksl);
        ksl.setBounds(130, 410, 154, 23);

        pnj.setHighlighter(null);
        pnj.setName("pnj"); // NOI18N
        pnj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnjKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnjKeyReleased(evt);
            }
        });
        FormIDRG1.add(pnj);
        pnj.setBounds(130, 440, 154, 23);

        ri.setHighlighter(null);
        ri.setName("ri"); // NOI18N
        ri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                riKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                riKeyReleased(evt);
            }
        });
        FormIDRG1.add(ri);
        ri.setBounds(130, 500, 154, 23);

        pl_darah.setHighlighter(null);
        pl_darah.setName("pl_darah"); // NOI18N
        pl_darah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pl_darahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pl_darahKeyReleased(evt);
            }
        });
        FormIDRG1.add(pl_darah);
        pl_darah.setBounds(130, 470, 154, 23);

        sewa_alat.setHighlighter(null);
        sewa_alat.setName("sewa_alat"); // NOI18N
        sewa_alat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sewa_alatKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sewa_alatKeyReleased(evt);
            }
        });
        FormIDRG1.add(sewa_alat);
        sewa_alat.setBounds(130, 560, 154, 23);

        obat_kemo.setHighlighter(null);
        obat_kemo.setName("obat_kemo"); // NOI18N
        obat_kemo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                obat_kemoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                obat_kemoKeyReleased(evt);
            }
        });
        FormIDRG1.add(obat_kemo);
        obat_kemo.setBounds(130, 530, 154, 23);

        jLabel71.setText("BMHP :");
        jLabel71.setName("jLabel71"); // NOI18N
        FormIDRG1.add(jLabel71);
        jLabel71.setBounds(10, 380, 115, 23);

        jLabel72.setText("Prosedur Non Bedah :");
        jLabel72.setName("jLabel72"); // NOI18N
        FormIDRG1.add(jLabel72);
        jLabel72.setBounds(10, 50, 115, 23);

        jLabel73.setText("Tenaga Ahli :");
        jLabel73.setName("jLabel73"); // NOI18N
        FormIDRG1.add(jLabel73);
        jLabel73.setBounds(10, 80, 115, 23);

        jLabel74.setText("Radiologi :");
        jLabel74.setName("jLabel74"); // NOI18N
        FormIDRG1.add(jLabel74);
        jLabel74.setBounds(10, 110, 115, 23);

        jLabel75.setText("Rehabilitasi :");
        jLabel75.setName("jLabel75"); // NOI18N
        FormIDRG1.add(jLabel75);
        jLabel75.setBounds(10, 140, 115, 23);

        jLabel76.setText("Obat :");
        jLabel76.setName("jLabel76"); // NOI18N
        FormIDRG1.add(jLabel76);
        jLabel76.setBounds(10, 170, 115, 23);

        jLabel77.setText("Prosedur Bedah :");
        jLabel77.setName("jLabel77"); // NOI18N
        FormIDRG1.add(jLabel77);
        jLabel77.setBounds(10, 230, 115, 23);

        jLabel78.setText("Keperawatan :");
        jLabel78.setName("jLabel78"); // NOI18N
        FormIDRG1.add(jLabel78);
        jLabel78.setBounds(10, 260, 115, 23);

        jLabel80.setText("Labotatorium :");
        jLabel80.setName("jLabel80"); // NOI18N
        FormIDRG1.add(jLabel80);
        jLabel80.setBounds(10, 290, 115, 23);

        jLabel81.setText("Kamar / Akomodasi :");
        jLabel81.setName("jLabel81"); // NOI18N
        FormIDRG1.add(jLabel81);
        jLabel81.setBounds(10, 320, 115, 23);

        jLabel82.setText("Obat Kronis :");
        jLabel82.setName("jLabel82"); // NOI18N
        FormIDRG1.add(jLabel82);
        jLabel82.setBounds(10, 350, 115, 23);

        jLabel83.setText("Konsultasi :");
        jLabel83.setName("jLabel83"); // NOI18N
        FormIDRG1.add(jLabel83);
        jLabel83.setBounds(10, 410, 115, 23);

        jLabel84.setText("Penunjang :");
        jLabel84.setName("jLabel84"); // NOI18N
        FormIDRG1.add(jLabel84);
        jLabel84.setBounds(10, 440, 115, 23);

        jLabel85.setText("Pelayanan Darah :");
        jLabel85.setName("jLabel85"); // NOI18N
        FormIDRG1.add(jLabel85);
        jLabel85.setBounds(10, 470, 115, 23);

        jLabel86.setText("Rawat Intensif :");
        jLabel86.setName("jLabel86"); // NOI18N
        FormIDRG1.add(jLabel86);
        jLabel86.setBounds(10, 500, 115, 23);

        jLabel87.setText("Obat Kemoterapi :");
        jLabel87.setName("jLabel87"); // NOI18N
        FormIDRG1.add(jLabel87);
        jLabel87.setBounds(10, 530, 115, 23);

        jLabel231.setText("Alkes :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormIDRG1.add(jLabel231);
        jLabel231.setBounds(10, 200, 115, 23);

        jLabel232.setText("Sewa Alat :");
        jLabel232.setName("jLabel232"); // NOI18N
        FormIDRG1.add(jLabel232);
        jLabel232.setBounds(10, 560, 115, 23);

        tarifRS.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tarifRS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tarifRS.setName("tarifRS"); // NOI18N
        FormIDRG1.add(tarifRS);
        tarifRS.setBounds(150, 20, 220, 20);

        lbltarifrs.setText("TARIF RS : Rp. ");
        lbltarifrs.setToolTipText("");
        lbltarifrs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbltarifrs.setName("lbltarifrs"); // NOI18N
        FormIDRG1.add(lbltarifrs);
        lbltarifrs.setBounds(10, 20, 140, 20);

        Scroll4.setViewportView(FormIDRG1);

        jPanel1.add(Scroll4);
        Scroll4.setBounds(-6, 5, 320, 310);

        Panel1.add(jPanel1);
        jPanel1.setBounds(1020, 0, 350, 330);

        jLabel235.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel235.setText("Pulse");
        jLabel235.setName("jLabel235"); // NOI18N
        Panel1.add(jLabel235);
        jLabel235.setBounds(460, 340, 60, 20);

        jLabel237.setText("1 Menit :");
        jLabel237.setName("jLabel237"); // NOI18N
        Panel1.add(jLabel237);
        jLabel237.setBounds(330, 360, 50, 23);

        Appear1.setName("Appear1"); // NOI18N
        Panel1.add(Appear1);
        Appear1.setBounds(390, 360, 62, 16);

        Pulse1.setName("Pulse1"); // NOI18N
        Panel1.add(Pulse1);
        Pulse1.setBounds(460, 360, 62, 16);

        Grimace1.setName("Grimace1"); // NOI18N
        Panel1.add(Grimace1);
        Grimace1.setBounds(530, 360, 62, 16);

        Activity1.setName("Activity1"); // NOI18N
        Panel1.add(Activity1);
        Activity1.setBounds(600, 360, 62, 16);

        Respiration1.setName("Respiration1"); // NOI18N
        Panel1.add(Respiration1);
        Respiration1.setBounds(670, 360, 62, 16);

        jLabel238.setText("5 Menit :");
        jLabel238.setName("jLabel238"); // NOI18N
        Panel1.add(jLabel238);
        jLabel238.setBounds(330, 390, 50, 23);

        jLabel239.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel239.setText("Appear");
        jLabel239.setName("jLabel239"); // NOI18N
        Panel1.add(jLabel239);
        jLabel239.setBounds(390, 340, 60, 20);

        jLabel240.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel240.setText("Respiration");
        jLabel240.setName("jLabel240"); // NOI18N
        Panel1.add(jLabel240);
        jLabel240.setBounds(670, 340, 60, 20);

        jLabel241.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel241.setText("Grimace");
        jLabel241.setName("jLabel241"); // NOI18N
        Panel1.add(jLabel241);
        jLabel241.setBounds(530, 340, 60, 20);

        jLabel242.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel242.setText("Activity");
        jLabel242.setName("jLabel242"); // NOI18N
        Panel1.add(jLabel242);
        jLabel242.setBounds(600, 340, 60, 20);

        label1.setText("LOS :");
        label1.setName("label1"); // NOI18N
        Panel1.add(label1);
        label1.setBounds(11, 190, 100, 20);

        label3.setText("Hari");
        label3.setName("label3"); // NOI18N
        Panel1.add(label3);
        label3.setBounds(190, 190, 30, 20);

        lamaharirawat.setName("lamaharirawat"); // NOI18N
        lamaharirawat.setPreferredSize(new java.awt.Dimension(21, 26));
        Panel1.add(lamaharirawat);
        lamaharirawat.setBounds(120, 190, 70, 23);

        lamajamrawat.setName("lamajamrawat"); // NOI18N
        lamajamrawat.setPreferredSize(new java.awt.Dimension(21, 26));
        Panel1.add(lamajamrawat);
        lamajamrawat.setBounds(230, 190, 70, 23);

        label4.setText("Jam");
        label4.setName("label4"); // NOI18N
        Panel1.add(label4);
        label4.setBounds(300, 190, 30, 20);

        label2.setText("Data Klinis :");
        label2.setName("label2"); // NOI18N
        Panel1.add(label2);
        label2.setBounds(30, 360, 80, 20);

        Appear5.setName("Appear5"); // NOI18N
        Panel1.add(Appear5);
        Appear5.setBounds(390, 390, 62, 16);

        Pulse5.setName("Pulse5"); // NOI18N
        Panel1.add(Pulse5);
        Pulse5.setBounds(460, 390, 62, 16);

        Grimace5.setName("Grimace5"); // NOI18N
        Panel1.add(Grimace5);
        Grimace5.setBounds(530, 390, 62, 16);

        Activity5.setName("Activity5"); // NOI18N
        Panel1.add(Activity5);
        Activity5.setBounds(600, 390, 62, 16);

        Respiration5.setName("Respiration5"); // NOI18N
        Panel1.add(Respiration5);
        Respiration5.setBounds(670, 390, 62, 16);

        Apgar.setBackground(new java.awt.Color(255, 255, 255));
        Apgar.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        Apgar.setText("Apgar Score");
        Apgar.setName("Apgar"); // NOI18N
        Apgar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ApgarItemStateChanged(evt);
            }
        });
        Panel1.add(Apgar);
        Apgar.setBounds(230, 360, 100, 22);

        Kehamilan.setBackground(new java.awt.Color(255, 255, 255));
        Kehamilan.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        Kehamilan.setText("Riwayat Kehamilan");
        Kehamilan.setName("Kehamilan"); // NOI18N
        Kehamilan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                KehamilanItemStateChanged(evt);
            }
        });
        Panel1.add(Kehamilan);
        Kehamilan.setBounds(230, 420, 150, 22);

        Abortus.setName("Abortus"); // NOI18N
        Panel1.add(Abortus);
        Abortus.setBounds(880, 440, 60, 16);

        label7.setText("Riwayat Kehamilan Sebelumnya :");
        label7.setName("label7"); // NOI18N
        Panel1.add(label7);
        label7.setBounds(540, 440, 190, 20);

        label8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label8.setText("Onset Kontraksi");
        label8.setName("label8"); // NOI18N
        Panel1.add(label8);
        label8.setBounds(960, 420, 200, 20);

        UsiaKehamilan.setName("UsiaKehamilan"); // NOI18N
        Panel1.add(UsiaKehamilan);
        UsiaKehamilan.setBounds(390, 440, 60, 16);

        Gravida.setName("Gravida"); // NOI18N
        Panel1.add(Gravida);
        Gravida.setBounds(740, 440, 60, 16);

        Partus.setName("Partus"); // NOI18N
        Panel1.add(Partus);
        Partus.setBounds(810, 440, 60, 16);

        label10.setText("Minggu");
        label10.setName("label10"); // NOI18N
        Panel1.add(label10);
        label10.setBounds(450, 440, 50, 20);

        label11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label11.setText("Gravida");
        label11.setName("label11"); // NOI18N
        Panel1.add(label11);
        label11.setBounds(740, 420, 60, 20);

        label12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label12.setText("Partus");
        label12.setName("label12"); // NOI18N
        Panel1.add(label12);
        label12.setBounds(810, 420, 60, 20);

        Kontraksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Timbul Spontan", "Dengan Induksi", "SC Tanpa Kontraksi/Induksi" }));
        Kontraksi.setName("Kontraksi"); // NOI18N
        Panel1.add(Kontraksi);
        Kontraksi.setBounds(960, 440, 200, 24);

        label13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label13.setText("Abortus");
        label13.setName("label13"); // NOI18N
        Panel1.add(label13);
        label13.setBounds(880, 420, 60, 20);

        label14.setText("Usia Kehamilan :");
        label14.setName("label14"); // NOI18N
        Panel1.add(label14);
        label14.setBounds(290, 436, 90, 30);

        label5.setText("Kelas Hak :");
        label5.setName("label5"); // NOI18N
        Panel1.add(label5);
        label5.setBounds(760, 70, 110, 20);

        lblStatusEncounter.setText("-");
        lblStatusEncounter.setToolTipText("");
        lblStatusEncounter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatusEncounter.setName("lblStatusEncounter"); // NOI18N
        Panel1.add(lblStatusEncounter);
        lblStatusEncounter.setBounds(920, 360, 410, 20);

        lblStatusKlaim.setText("-");
        lblStatusKlaim.setToolTipText("");
        lblStatusKlaim.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatusKlaim.setName("lblStatusKlaim"); // NOI18N
        Panel1.add(lblStatusKlaim);
        lblStatusKlaim.setBounds(920, 330, 410, 20);

        FormDATAKLAIM.add(Panel1, java.awt.BorderLayout.PAGE_START);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFocusCycleRoot(true);
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.setPreferredSize(new java.awt.Dimension(494, 1000));
        TabRawat.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabRawatStateChanged(evt);
            }
        });
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        FormIDRG.setName("FormIDRG"); // NOI18N
        FormIDRG.setPreferredSize(new java.awt.Dimension(490, 842));
        FormIDRG.setLayout(null);

        jLabel111.setText("Check Diagnosa Utama");
        jLabel111.setName("jLabel111"); // NOI18N
        FormIDRG.add(jLabel111);
        jLabel111.setBounds(270, 190, 150, 23);

        nm_diag_1.setEditable(false);
        nm_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_1.setEnabled(false);
        nm_diag_1.setHighlighter(null);
        nm_diag_1.setName("nm_diag_1"); // NOI18N
        FormIDRG.add(nm_diag_1);
        nm_diag_1.setBounds(110, 210, 250, 23);

        jLabel121.setText("DIAGNOSA PASIEN");
        jLabel121.setName("jLabel121"); // NOI18N
        FormIDRG.add(jLabel121);
        jLabel121.setBounds(25, 190, 110, 23);

        kd_diag_1.setEditable(false);
        kd_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_1.setEnabled(false);
        kd_diag_1.setHighlighter(null);
        kd_diag_1.setName("kd_diag_1"); // NOI18N
        FormIDRG.add(kd_diag_1);
        kd_diag_1.setBounds(40, 210, 70, 23);

        nm_diag_2.setEditable(false);
        nm_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_2.setEnabled(false);
        nm_diag_2.setHighlighter(null);
        nm_diag_2.setName("nm_diag_2"); // NOI18N
        FormIDRG.add(nm_diag_2);
        nm_diag_2.setBounds(110, 240, 250, 23);

        kd_diag_2.setEditable(false);
        kd_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_2.setEnabled(false);
        kd_diag_2.setHighlighter(null);
        kd_diag_2.setName("kd_diag_2"); // NOI18N
        FormIDRG.add(kd_diag_2);
        kd_diag_2.setBounds(40, 240, 70, 23);

        nm_diag_3.setEditable(false);
        nm_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_3.setEnabled(false);
        nm_diag_3.setHighlighter(null);
        nm_diag_3.setName("nm_diag_3"); // NOI18N
        FormIDRG.add(nm_diag_3);
        nm_diag_3.setBounds(110, 270, 250, 23);

        kd_diag_3.setEditable(false);
        kd_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_3.setEnabled(false);
        kd_diag_3.setHighlighter(null);
        kd_diag_3.setName("kd_diag_3"); // NOI18N
        FormIDRG.add(kd_diag_3);
        kd_diag_3.setBounds(40, 270, 70, 23);

        nm_diag_4.setEditable(false);
        nm_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_4.setEnabled(false);
        nm_diag_4.setHighlighter(null);
        nm_diag_4.setName("nm_diag_4"); // NOI18N
        FormIDRG.add(nm_diag_4);
        nm_diag_4.setBounds(110, 300, 250, 23);

        kd_diag_4.setEditable(false);
        kd_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_4.setEnabled(false);
        kd_diag_4.setHighlighter(null);
        kd_diag_4.setName("kd_diag_4"); // NOI18N
        FormIDRG.add(kd_diag_4);
        kd_diag_4.setBounds(40, 300, 70, 23);

        nm_diag_5.setEditable(false);
        nm_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_5.setEnabled(false);
        nm_diag_5.setHighlighter(null);
        nm_diag_5.setName("nm_diag_5"); // NOI18N
        nm_diag_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_diag_5ActionPerformed(evt);
            }
        });
        FormIDRG.add(nm_diag_5);
        nm_diag_5.setBounds(110, 330, 250, 23);

        kd_diag_5.setEditable(false);
        kd_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_5.setEnabled(false);
        kd_diag_5.setHighlighter(null);
        kd_diag_5.setName("kd_diag_5"); // NOI18N
        FormIDRG.add(kd_diag_5);
        kd_diag_5.setBounds(40, 330, 70, 23);

        nm_psdr_1.setEditable(false);
        nm_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_1.setEnabled(false);
        nm_psdr_1.setHighlighter(null);
        nm_psdr_1.setName("nm_psdr_1"); // NOI18N
        FormIDRG.add(nm_psdr_1);
        nm_psdr_1.setBounds(590, 210, 260, 23);

        kd_psdr_1.setEditable(false);
        kd_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_1.setEnabled(false);
        kd_psdr_1.setHighlighter(null);
        kd_psdr_1.setName("kd_psdr_1"); // NOI18N
        FormIDRG.add(kd_psdr_1);
        kd_psdr_1.setBounds(520, 210, 70, 23);

        nm_psdr_2.setEditable(false);
        nm_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_2.setEnabled(false);
        nm_psdr_2.setHighlighter(null);
        nm_psdr_2.setName("nm_psdr_2"); // NOI18N
        FormIDRG.add(nm_psdr_2);
        nm_psdr_2.setBounds(590, 240, 260, 23);

        kd_psdr_2.setEditable(false);
        kd_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_2.setEnabled(false);
        kd_psdr_2.setHighlighter(null);
        kd_psdr_2.setName("kd_psdr_2"); // NOI18N
        FormIDRG.add(kd_psdr_2);
        kd_psdr_2.setBounds(520, 240, 70, 23);

        nm_psdr_3.setEditable(false);
        nm_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_3.setEnabled(false);
        nm_psdr_3.setHighlighter(null);
        nm_psdr_3.setName("nm_psdr_3"); // NOI18N
        FormIDRG.add(nm_psdr_3);
        nm_psdr_3.setBounds(590, 270, 260, 23);

        kd_psdr_3.setEditable(false);
        kd_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_3.setEnabled(false);
        kd_psdr_3.setHighlighter(null);
        kd_psdr_3.setName("kd_psdr_3"); // NOI18N
        FormIDRG.add(kd_psdr_3);
        kd_psdr_3.setBounds(520, 270, 70, 23);

        nm_psdr_4.setEditable(false);
        nm_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_4.setEnabled(false);
        nm_psdr_4.setHighlighter(null);
        nm_psdr_4.setName("nm_psdr_4"); // NOI18N
        FormIDRG.add(nm_psdr_4);
        nm_psdr_4.setBounds(590, 300, 260, 23);

        kd_psdr_4.setEditable(false);
        kd_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_4.setEnabled(false);
        kd_psdr_4.setHighlighter(null);
        kd_psdr_4.setName("kd_psdr_4"); // NOI18N
        FormIDRG.add(kd_psdr_4);
        kd_psdr_4.setBounds(520, 300, 70, 23);

        nm_psdr_5.setEditable(false);
        nm_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_5.setEnabled(false);
        nm_psdr_5.setHighlighter(null);
        nm_psdr_5.setName("nm_psdr_5"); // NOI18N
        FormIDRG.add(nm_psdr_5);
        nm_psdr_5.setBounds(590, 330, 260, 23);

        kd_psdr_5.setEditable(false);
        kd_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_5.setEnabled(false);
        kd_psdr_5.setHighlighter(null);
        kd_psdr_5.setName("kd_psdr_5"); // NOI18N
        FormIDRG.add(kd_psdr_5);
        kd_psdr_5.setBounds(520, 330, 70, 23);

        chk_diag_1.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_1.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_1.setName("chk_diag_1"); // NOI18N
        chk_diag_1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_1ItemStateChanged(evt);
            }
        });
        chk_diag_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_diag_1ActionPerformed(evt);
            }
        });
        FormIDRG.add(chk_diag_1);
        chk_diag_1.setBounds(360, 210, 30, 22);

        chk_diag_2.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_2.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_2.setName("chk_diag_2"); // NOI18N
        chk_diag_2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_2ItemStateChanged(evt);
            }
        });
        chk_diag_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_diag_2ActionPerformed(evt);
            }
        });
        FormIDRG.add(chk_diag_2);
        chk_diag_2.setBounds(360, 240, 30, 22);

        chk_diag_3.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_3.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_3.setName("chk_diag_3"); // NOI18N
        chk_diag_3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_3ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_3);
        chk_diag_3.setBounds(360, 270, 30, 22);

        chk_diag_4.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_4.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_4.setName("chk_diag_4"); // NOI18N
        chk_diag_4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_4ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_4);
        chk_diag_4.setBounds(360, 300, 30, 22);

        chk_diag_5.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_5.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_5.setName("chk_diag_5"); // NOI18N
        chk_diag_5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_5ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_5);
        chk_diag_5.setBounds(360, 330, 30, 22);

        nm_diag_6.setEditable(false);
        nm_diag_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_6.setEnabled(false);
        nm_diag_6.setHighlighter(null);
        nm_diag_6.setName("nm_diag_6"); // NOI18N
        FormIDRG.add(nm_diag_6);
        nm_diag_6.setBounds(110, 360, 250, 23);

        kd_diag_6.setEditable(false);
        kd_diag_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_6.setEnabled(false);
        kd_diag_6.setHighlighter(null);
        kd_diag_6.setName("kd_diag_6"); // NOI18N
        FormIDRG.add(kd_diag_6);
        kd_diag_6.setBounds(40, 360, 70, 23);

        nm_diag_7.setEditable(false);
        nm_diag_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_7.setEnabled(false);
        nm_diag_7.setHighlighter(null);
        nm_diag_7.setName("nm_diag_7"); // NOI18N
        FormIDRG.add(nm_diag_7);
        nm_diag_7.setBounds(110, 390, 250, 23);

        kd_diag_7.setEditable(false);
        kd_diag_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_7.setEnabled(false);
        kd_diag_7.setHighlighter(null);
        kd_diag_7.setName("kd_diag_7"); // NOI18N
        FormIDRG.add(kd_diag_7);
        kd_diag_7.setBounds(40, 390, 70, 23);

        nm_diag_8.setEditable(false);
        nm_diag_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_8.setEnabled(false);
        nm_diag_8.setHighlighter(null);
        nm_diag_8.setName("nm_diag_8"); // NOI18N
        FormIDRG.add(nm_diag_8);
        nm_diag_8.setBounds(110, 420, 250, 23);

        kd_diag_8.setEditable(false);
        kd_diag_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_8.setEnabled(false);
        kd_diag_8.setHighlighter(null);
        kd_diag_8.setName("kd_diag_8"); // NOI18N
        FormIDRG.add(kd_diag_8);
        kd_diag_8.setBounds(40, 420, 70, 23);

        nm_diag_9.setEditable(false);
        nm_diag_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_9.setEnabled(false);
        nm_diag_9.setHighlighter(null);
        nm_diag_9.setName("nm_diag_9"); // NOI18N
        FormIDRG.add(nm_diag_9);
        nm_diag_9.setBounds(110, 450, 250, 23);

        kd_diag_9.setEditable(false);
        kd_diag_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_9.setEnabled(false);
        kd_diag_9.setHighlighter(null);
        kd_diag_9.setName("kd_diag_9"); // NOI18N
        FormIDRG.add(kd_diag_9);
        kd_diag_9.setBounds(40, 450, 70, 23);

        nm_diag_10.setEditable(false);
        nm_diag_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_10.setEnabled(false);
        nm_diag_10.setHighlighter(null);
        nm_diag_10.setName("nm_diag_10"); // NOI18N
        FormIDRG.add(nm_diag_10);
        nm_diag_10.setBounds(110, 480, 250, 23);

        kd_diag_10.setEditable(false);
        kd_diag_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_10.setEnabled(false);
        kd_diag_10.setHighlighter(null);
        kd_diag_10.setName("kd_diag_10"); // NOI18N
        FormIDRG.add(kd_diag_10);
        kd_diag_10.setBounds(40, 480, 70, 23);

        chk_diag_6.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_6.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_6.setName("chk_diag_6"); // NOI18N
        chk_diag_6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_6ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_6);
        chk_diag_6.setBounds(360, 360, 30, 22);

        chk_diag_7.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_7.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_7.setName("chk_diag_7"); // NOI18N
        chk_diag_7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_7ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_7);
        chk_diag_7.setBounds(360, 390, 30, 22);

        chk_diag_8.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_8.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_8.setName("chk_diag_8"); // NOI18N
        chk_diag_8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_8ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_8);
        chk_diag_8.setBounds(360, 420, 30, 22);

        chk_diag_9.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_9.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_9.setName("chk_diag_9"); // NOI18N
        chk_diag_9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_9ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_9);
        chk_diag_9.setBounds(360, 450, 30, 22);

        chk_diag_10.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_10.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_10.setName("chk_diag_10"); // NOI18N
        chk_diag_10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_10ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_10);
        chk_diag_10.setBounds(360, 480, 30, 22);

        nm_psdr_6.setEditable(false);
        nm_psdr_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_6.setEnabled(false);
        nm_psdr_6.setHighlighter(null);
        nm_psdr_6.setName("nm_psdr_6"); // NOI18N
        FormIDRG.add(nm_psdr_6);
        nm_psdr_6.setBounds(590, 360, 260, 23);

        kd_psdr_6.setEditable(false);
        kd_psdr_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_6.setEnabled(false);
        kd_psdr_6.setHighlighter(null);
        kd_psdr_6.setName("kd_psdr_6"); // NOI18N
        FormIDRG.add(kd_psdr_6);
        kd_psdr_6.setBounds(520, 360, 70, 23);

        nm_psdr_7.setEditable(false);
        nm_psdr_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_7.setEnabled(false);
        nm_psdr_7.setHighlighter(null);
        nm_psdr_7.setName("nm_psdr_7"); // NOI18N
        FormIDRG.add(nm_psdr_7);
        nm_psdr_7.setBounds(590, 390, 260, 23);

        kd_psdr_7.setEditable(false);
        kd_psdr_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_7.setEnabled(false);
        kd_psdr_7.setHighlighter(null);
        kd_psdr_7.setName("kd_psdr_7"); // NOI18N
        FormIDRG.add(kd_psdr_7);
        kd_psdr_7.setBounds(520, 390, 70, 23);

        nm_psdr_8.setEditable(false);
        nm_psdr_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_8.setEnabled(false);
        nm_psdr_8.setHighlighter(null);
        nm_psdr_8.setName("nm_psdr_8"); // NOI18N
        FormIDRG.add(nm_psdr_8);
        nm_psdr_8.setBounds(590, 420, 260, 23);

        kd_psdr_8.setEditable(false);
        kd_psdr_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_8.setEnabled(false);
        kd_psdr_8.setHighlighter(null);
        kd_psdr_8.setName("kd_psdr_8"); // NOI18N
        FormIDRG.add(kd_psdr_8);
        kd_psdr_8.setBounds(520, 420, 70, 23);

        nm_psdr_9.setEditable(false);
        nm_psdr_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_9.setEnabled(false);
        nm_psdr_9.setHighlighter(null);
        nm_psdr_9.setName("nm_psdr_9"); // NOI18N
        FormIDRG.add(nm_psdr_9);
        nm_psdr_9.setBounds(590, 450, 260, 23);

        kd_psdr_9.setEditable(false);
        kd_psdr_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_9.setEnabled(false);
        kd_psdr_9.setHighlighter(null);
        kd_psdr_9.setName("kd_psdr_9"); // NOI18N
        FormIDRG.add(kd_psdr_9);
        kd_psdr_9.setBounds(520, 450, 70, 23);

        nm_psdr_10.setEditable(false);
        nm_psdr_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_10.setEnabled(false);
        nm_psdr_10.setHighlighter(null);
        nm_psdr_10.setName("nm_psdr_10"); // NOI18N
        FormIDRG.add(nm_psdr_10);
        nm_psdr_10.setBounds(590, 480, 260, 23);

        kd_psdr_10.setEditable(false);
        kd_psdr_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_10.setEnabled(false);
        kd_psdr_10.setHighlighter(null);
        kd_psdr_10.setName("kd_psdr_10"); // NOI18N
        FormIDRG.add(kd_psdr_10);
        kd_psdr_10.setBounds(520, 480, 70, 23);

        multiple1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple1.setHighlighter(null);
        multiple1.setName("multiple1"); // NOI18N
        FormIDRG.add(multiple1);
        multiple1.setBounds(850, 210, 50, 23);

        multiple2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple2.setHighlighter(null);
        multiple2.setName("multiple2"); // NOI18N
        FormIDRG.add(multiple2);
        multiple2.setBounds(850, 240, 50, 23);

        multiple3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple3.setHighlighter(null);
        multiple3.setName("multiple3"); // NOI18N
        FormIDRG.add(multiple3);
        multiple3.setBounds(850, 270, 50, 23);

        multiple4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple4.setHighlighter(null);
        multiple4.setName("multiple4"); // NOI18N
        FormIDRG.add(multiple4);
        multiple4.setBounds(850, 300, 50, 23);

        multiple5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple5.setHighlighter(null);
        multiple5.setName("multiple5"); // NOI18N
        FormIDRG.add(multiple5);
        multiple5.setBounds(850, 330, 50, 23);

        multiple6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple6.setHighlighter(null);
        multiple6.setName("multiple6"); // NOI18N
        FormIDRG.add(multiple6);
        multiple6.setBounds(850, 360, 50, 23);

        multiple7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple7.setHighlighter(null);
        multiple7.setName("multiple7"); // NOI18N
        FormIDRG.add(multiple7);
        multiple7.setBounds(850, 390, 50, 23);

        multiple8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple8.setHighlighter(null);
        multiple8.setName("multiple8"); // NOI18N
        FormIDRG.add(multiple8);
        multiple8.setBounds(850, 420, 50, 23);

        multiple9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple9.setHighlighter(null);
        multiple9.setName("multiple9"); // NOI18N
        FormIDRG.add(multiple9);
        multiple9.setBounds(850, 450, 50, 23);

        multiple10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple10.setHighlighter(null);
        multiple10.setName("multiple10"); // NOI18N
        FormIDRG.add(multiple10);
        multiple10.setBounds(850, 480, 50, 23);

        batal_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_1.setToolTipText("");
        batal_1.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_1.setName("batal_1"); // NOI18N
        batal_1.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_1ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_1);
        batal_1.setBounds(390, 210, 40, 22);

        batal_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_2.setToolTipText("");
        batal_2.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_2.setName("batal_2"); // NOI18N
        batal_2.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_2ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_2);
        batal_2.setBounds(390, 240, 40, 22);

        batal_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_3.setToolTipText("");
        batal_3.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_3.setName("batal_3"); // NOI18N
        batal_3.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_3ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_3);
        batal_3.setBounds(390, 270, 40, 22);

        batal_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_4.setToolTipText("");
        batal_4.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_4.setName("batal_4"); // NOI18N
        batal_4.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_4ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_4);
        batal_4.setBounds(390, 300, 40, 22);

        batal_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_5.setToolTipText("");
        batal_5.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_5.setName("batal_5"); // NOI18N
        batal_5.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_5ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_5);
        batal_5.setBounds(390, 330, 40, 22);

        batal_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_6.setToolTipText("");
        batal_6.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_6.setName("batal_6"); // NOI18N
        batal_6.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_6ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_6);
        batal_6.setBounds(390, 360, 40, 22);

        batal_7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_7.setToolTipText("");
        batal_7.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_7.setName("batal_7"); // NOI18N
        batal_7.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_7ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_7);
        batal_7.setBounds(390, 390, 40, 22);

        batal_8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_8.setToolTipText("");
        batal_8.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_8.setName("batal_8"); // NOI18N
        batal_8.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_8ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_8);
        batal_8.setBounds(390, 420, 40, 22);

        batal_9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_9.setToolTipText("");
        batal_9.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_9.setName("batal_9"); // NOI18N
        batal_9.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_9ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_9);
        batal_9.setBounds(390, 450, 40, 22);

        batal_10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_10.setToolTipText("");
        batal_10.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_10.setName("batal_10"); // NOI18N
        batal_10.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_10ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_10);
        batal_10.setBounds(390, 480, 40, 22);

        batal_psdr_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_1.setToolTipText("");
        batal_psdr_1.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_1.setName("batal_psdr_1"); // NOI18N
        batal_psdr_1.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_1ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_1);
        batal_psdr_1.setBounds(910, 210, 40, 22);

        batal_psdr_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_2.setToolTipText("");
        batal_psdr_2.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_2.setName("batal_psdr_2"); // NOI18N
        batal_psdr_2.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_2ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_2);
        batal_psdr_2.setBounds(910, 240, 40, 22);

        batal_psdr_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_3.setToolTipText("");
        batal_psdr_3.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_3.setName("batal_psdr_3"); // NOI18N
        batal_psdr_3.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_3ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_3);
        batal_psdr_3.setBounds(910, 270, 40, 22);

        batal_psdr_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_4.setToolTipText("");
        batal_psdr_4.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_4.setName("batal_psdr_4"); // NOI18N
        batal_psdr_4.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_4ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_4);
        batal_psdr_4.setBounds(910, 300, 40, 22);

        batal_psdr_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_5.setToolTipText("");
        batal_psdr_5.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_5.setName("batal_psdr_5"); // NOI18N
        batal_psdr_5.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_5ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_5);
        batal_psdr_5.setBounds(910, 330, 40, 22);

        batal_psdr_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_6.setToolTipText("");
        batal_psdr_6.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_6.setName("batal_psdr_6"); // NOI18N
        batal_psdr_6.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_6ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_6);
        batal_psdr_6.setBounds(910, 360, 40, 22);

        batal_psdr_7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_7.setToolTipText("");
        batal_psdr_7.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_7.setName("batal_psdr_7"); // NOI18N
        batal_psdr_7.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_7ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_7);
        batal_psdr_7.setBounds(910, 390, 40, 22);

        batal_psdr_8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_8.setToolTipText("");
        batal_psdr_8.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_8.setName("batal_psdr_8"); // NOI18N
        batal_psdr_8.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_8ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_8);
        batal_psdr_8.setBounds(910, 420, 40, 22);

        batal_psdr_9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_9.setToolTipText("");
        batal_psdr_9.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_9.setName("batal_psdr_9"); // NOI18N
        batal_psdr_9.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_9ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_9);
        batal_psdr_9.setBounds(910, 450, 40, 22);

        batal_psdr_10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_10.setToolTipText("");
        batal_psdr_10.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_10.setName("batal_psdr_10"); // NOI18N
        batal_psdr_10.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_10ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_10);
        batal_psdr_10.setBounds(910, 480, 40, 22);

        jLabel92.setText("Diagnosa :");
        jLabel92.setName("jLabel92"); // NOI18N
        FormIDRG.add(jLabel92);
        jLabel92.setBounds(0, 10, 68, 23);

        Diagnosa.setHighlighter(null);
        Diagnosa.setName("Diagnosa"); // NOI18N
        Diagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DiagnosaKeyReleased(evt);
            }
        });
        FormIDRG.add(Diagnosa);
        Diagnosa.setBounds(70, 10, 370, 23);

        idrgPenyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        idrgPenyakit.setMnemonic('1');
        idrgPenyakit.setToolTipText("Alt+1");
        idrgPenyakit.setName("idrgPenyakit"); // NOI18N
        idrgPenyakit.setPreferredSize(new java.awt.Dimension(28, 23));
        idrgPenyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idrgPenyakitActionPerformed(evt);
            }
        });
        FormIDRG.add(idrgPenyakit);
        idrgPenyakit.setBounds(440, 10, 28, 23);

        idrgPenyakittambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        idrgPenyakittambah.setMnemonic('2');
        idrgPenyakittambah.setToolTipText("Alt+2");
        idrgPenyakittambah.setName("idrgPenyakittambah"); // NOI18N
        idrgPenyakittambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idrgPenyakittambahActionPerformed(evt);
            }
        });
        FormIDRG.add(idrgPenyakittambah);
        idrgPenyakittambah.setBounds(10, 40, 28, 23);

        Scroll5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)));
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tbDiagnosa.setName("tbDiagnosa"); // NOI18N
        tbDiagnosa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDiagnosaMouseClicked(evt);
            }
        });
        Scroll5.setViewportView(tbDiagnosa);

        FormIDRG.add(Scroll5);
        Scroll5.setBounds(40, 40, 400, 150);

        Scroll6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)));
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbProsedur.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbProsedur.setName("tbProsedur"); // NOI18N
        tbProsedur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProsedurMouseClicked(evt);
            }
        });
        Scroll6.setViewportView(tbProsedur);

        FormIDRG.add(Scroll6);
        Scroll6.setBounds(520, 40, 430, 150);

        jLabel93.setText("Prosedur :");
        jLabel93.setName("jLabel93"); // NOI18N
        FormIDRG.add(jLabel93);
        jLabel93.setBounds(480, 10, 68, 23);

        Prosedur.setHighlighter(null);
        Prosedur.setName("Prosedur"); // NOI18N
        Prosedur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProsedurKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProsedurKeyReleased(evt);
            }
        });
        FormIDRG.add(Prosedur);
        Prosedur.setBounds(550, 10, 400, 23);

        idrgProsedur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        idrgProsedur.setMnemonic('1');
        idrgProsedur.setToolTipText("Alt+1");
        idrgProsedur.setName("idrgProsedur"); // NOI18N
        idrgProsedur.setPreferredSize(new java.awt.Dimension(28, 23));
        idrgProsedur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idrgProsedurActionPerformed(evt);
            }
        });
        FormIDRG.add(idrgProsedur);
        idrgProsedur.setBounds(950, 10, 28, 23);

        idrgProseduttambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        idrgProseduttambah.setMnemonic('2');
        idrgProseduttambah.setToolTipText("Alt+2");
        idrgProseduttambah.setName("idrgProseduttambah"); // NOI18N
        idrgProseduttambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idrgProseduttambahActionPerformed(evt);
            }
        });
        FormIDRG.add(idrgProseduttambah);
        idrgProseduttambah.setBounds(490, 40, 28, 23);

        responIDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        responIDRG.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        responIDRG.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        responIDRG.setName("responIDRG"); // NOI18N
        FormIDRG.add(responIDRG);
        responIDRG.setBounds(960, 70, 360, 380);

        jLabel127.setText("PROSEDUR PASIEN");
        jLabel127.setName("jLabel127"); // NOI18N
        FormIDRG.add(jLabel127);
        jLabel127.setBounds(500, 190, 115, 23);

        jLabel129.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel129.setText(" HASIL GROUPING iDRG");
        jLabel129.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel129.setName("jLabel129"); // NOI18N
        FormIDRG.add(jLabel129);
        jLabel129.setBounds(960, 40, 200, 23);

        kd_psdr_21.setEditable(false);
        kd_psdr_21.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_21.setEnabled(false);
        kd_psdr_21.setHighlighter(null);
        kd_psdr_21.setName("kd_psdr_21"); // NOI18N
        FormIDRG.add(kd_psdr_21);
        kd_psdr_21.setBounds(520, 510, 70, 23);

        nm_psdr_21.setEditable(false);
        nm_psdr_21.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_21.setEnabled(false);
        nm_psdr_21.setHighlighter(null);
        nm_psdr_21.setName("nm_psdr_21"); // NOI18N
        FormIDRG.add(nm_psdr_21);
        nm_psdr_21.setBounds(590, 510, 260, 23);

        multiple11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple11.setHighlighter(null);
        multiple11.setName("multiple11"); // NOI18N
        FormIDRG.add(multiple11);
        multiple11.setBounds(850, 510, 50, 23);

        batal_psdr_21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_21.setToolTipText("");
        batal_psdr_21.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_21.setName("batal_psdr_21"); // NOI18N
        batal_psdr_21.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_21ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_21);
        batal_psdr_21.setBounds(910, 510, 40, 22);

        kd_psdr_22.setEditable(false);
        kd_psdr_22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_22.setEnabled(false);
        kd_psdr_22.setHighlighter(null);
        kd_psdr_22.setName("kd_psdr_22"); // NOI18N
        FormIDRG.add(kd_psdr_22);
        kd_psdr_22.setBounds(520, 540, 70, 23);

        nm_psdr_22.setEditable(false);
        nm_psdr_22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_22.setEnabled(false);
        nm_psdr_22.setHighlighter(null);
        nm_psdr_22.setName("nm_psdr_22"); // NOI18N
        FormIDRG.add(nm_psdr_22);
        nm_psdr_22.setBounds(590, 540, 260, 23);

        multiple12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple12.setHighlighter(null);
        multiple12.setName("multiple12"); // NOI18N
        FormIDRG.add(multiple12);
        multiple12.setBounds(850, 540, 50, 23);

        batal_psdr_22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_22.setToolTipText("");
        batal_psdr_22.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_22.setName("batal_psdr_22"); // NOI18N
        batal_psdr_22.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_22ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_22);
        batal_psdr_22.setBounds(910, 540, 40, 22);

        kd_psdr_23.setEditable(false);
        kd_psdr_23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_23.setEnabled(false);
        kd_psdr_23.setHighlighter(null);
        kd_psdr_23.setName("kd_psdr_23"); // NOI18N
        FormIDRG.add(kd_psdr_23);
        kd_psdr_23.setBounds(520, 570, 70, 23);

        nm_psdr_23.setEditable(false);
        nm_psdr_23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_23.setEnabled(false);
        nm_psdr_23.setHighlighter(null);
        nm_psdr_23.setName("nm_psdr_23"); // NOI18N
        FormIDRG.add(nm_psdr_23);
        nm_psdr_23.setBounds(590, 570, 260, 23);

        multiple13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple13.setHighlighter(null);
        multiple13.setName("multiple13"); // NOI18N
        FormIDRG.add(multiple13);
        multiple13.setBounds(850, 570, 50, 23);

        batal_psdr_23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_23.setToolTipText("");
        batal_psdr_23.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_23.setName("batal_psdr_23"); // NOI18N
        batal_psdr_23.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_23ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_23);
        batal_psdr_23.setBounds(910, 570, 40, 22);

        kd_psdr_24.setEditable(false);
        kd_psdr_24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_24.setEnabled(false);
        kd_psdr_24.setHighlighter(null);
        kd_psdr_24.setName("kd_psdr_24"); // NOI18N
        FormIDRG.add(kd_psdr_24);
        kd_psdr_24.setBounds(520, 600, 70, 23);

        nm_psdr_24.setEditable(false);
        nm_psdr_24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_24.setEnabled(false);
        nm_psdr_24.setHighlighter(null);
        nm_psdr_24.setName("nm_psdr_24"); // NOI18N
        FormIDRG.add(nm_psdr_24);
        nm_psdr_24.setBounds(590, 600, 260, 23);

        multiple14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple14.setHighlighter(null);
        multiple14.setName("multiple14"); // NOI18N
        FormIDRG.add(multiple14);
        multiple14.setBounds(850, 600, 50, 23);

        batal_psdr_24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_24.setToolTipText("");
        batal_psdr_24.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_24.setName("batal_psdr_24"); // NOI18N
        batal_psdr_24.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_24ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_24);
        batal_psdr_24.setBounds(910, 600, 40, 22);

        kd_psdr_25.setEditable(false);
        kd_psdr_25.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_25.setEnabled(false);
        kd_psdr_25.setHighlighter(null);
        kd_psdr_25.setName("kd_psdr_25"); // NOI18N
        FormIDRG.add(kd_psdr_25);
        kd_psdr_25.setBounds(520, 630, 70, 23);

        nm_psdr_25.setEditable(false);
        nm_psdr_25.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_25.setEnabled(false);
        nm_psdr_25.setHighlighter(null);
        nm_psdr_25.setName("nm_psdr_25"); // NOI18N
        FormIDRG.add(nm_psdr_25);
        nm_psdr_25.setBounds(590, 630, 260, 23);

        multiple15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple15.setHighlighter(null);
        multiple15.setName("multiple15"); // NOI18N
        FormIDRG.add(multiple15);
        multiple15.setBounds(850, 630, 50, 23);

        batal_psdr_25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_25.setToolTipText("");
        batal_psdr_25.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_25.setName("batal_psdr_25"); // NOI18N
        batal_psdr_25.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_25ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_25);
        batal_psdr_25.setBounds(910, 630, 40, 22);

        kd_psdr_26.setEditable(false);
        kd_psdr_26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_26.setEnabled(false);
        kd_psdr_26.setHighlighter(null);
        kd_psdr_26.setName("kd_psdr_26"); // NOI18N
        FormIDRG.add(kd_psdr_26);
        kd_psdr_26.setBounds(520, 660, 70, 23);

        nm_psdr_26.setEditable(false);
        nm_psdr_26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_26.setEnabled(false);
        nm_psdr_26.setHighlighter(null);
        nm_psdr_26.setName("nm_psdr_26"); // NOI18N
        FormIDRG.add(nm_psdr_26);
        nm_psdr_26.setBounds(590, 660, 260, 23);

        multiple16.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple16.setHighlighter(null);
        multiple16.setName("multiple16"); // NOI18N
        FormIDRG.add(multiple16);
        multiple16.setBounds(850, 660, 50, 23);

        batal_psdr_26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_26.setToolTipText("");
        batal_psdr_26.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_26.setName("batal_psdr_26"); // NOI18N
        batal_psdr_26.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_26ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_26);
        batal_psdr_26.setBounds(910, 660, 40, 22);

        kd_psdr_27.setEditable(false);
        kd_psdr_27.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_27.setEnabled(false);
        kd_psdr_27.setHighlighter(null);
        kd_psdr_27.setName("kd_psdr_27"); // NOI18N
        FormIDRG.add(kd_psdr_27);
        kd_psdr_27.setBounds(520, 690, 70, 23);

        nm_psdr_27.setEditable(false);
        nm_psdr_27.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_27.setEnabled(false);
        nm_psdr_27.setHighlighter(null);
        nm_psdr_27.setName("nm_psdr_27"); // NOI18N
        FormIDRG.add(nm_psdr_27);
        nm_psdr_27.setBounds(590, 690, 260, 23);

        multiple17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple17.setHighlighter(null);
        multiple17.setName("multiple17"); // NOI18N
        FormIDRG.add(multiple17);
        multiple17.setBounds(850, 690, 50, 23);

        batal_psdr_27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_27.setToolTipText("");
        batal_psdr_27.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_27.setName("batal_psdr_27"); // NOI18N
        batal_psdr_27.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_27ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_27);
        batal_psdr_27.setBounds(910, 690, 40, 22);

        kd_psdr_28.setEditable(false);
        kd_psdr_28.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_28.setEnabled(false);
        kd_psdr_28.setHighlighter(null);
        kd_psdr_28.setName("kd_psdr_28"); // NOI18N
        FormIDRG.add(kd_psdr_28);
        kd_psdr_28.setBounds(520, 720, 70, 23);

        nm_psdr_28.setEditable(false);
        nm_psdr_28.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_28.setEnabled(false);
        nm_psdr_28.setHighlighter(null);
        nm_psdr_28.setName("nm_psdr_28"); // NOI18N
        FormIDRG.add(nm_psdr_28);
        nm_psdr_28.setBounds(590, 720, 260, 23);

        multiple18.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple18.setHighlighter(null);
        multiple18.setName("multiple18"); // NOI18N
        FormIDRG.add(multiple18);
        multiple18.setBounds(850, 720, 50, 23);

        batal_psdr_28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_28.setToolTipText("");
        batal_psdr_28.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_28.setName("batal_psdr_28"); // NOI18N
        batal_psdr_28.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_28ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_28);
        batal_psdr_28.setBounds(910, 720, 40, 22);

        kd_psdr_29.setEditable(false);
        kd_psdr_29.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_29.setEnabled(false);
        kd_psdr_29.setHighlighter(null);
        kd_psdr_29.setName("kd_psdr_29"); // NOI18N
        FormIDRG.add(kd_psdr_29);
        kd_psdr_29.setBounds(520, 750, 70, 23);

        nm_psdr_29.setEditable(false);
        nm_psdr_29.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_29.setEnabled(false);
        nm_psdr_29.setHighlighter(null);
        nm_psdr_29.setName("nm_psdr_29"); // NOI18N
        FormIDRG.add(nm_psdr_29);
        nm_psdr_29.setBounds(590, 750, 260, 23);

        multiple19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple19.setHighlighter(null);
        multiple19.setName("multiple19"); // NOI18N
        FormIDRG.add(multiple19);
        multiple19.setBounds(850, 750, 50, 23);

        batal_psdr_29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_29.setToolTipText("");
        batal_psdr_29.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_29.setName("batal_psdr_29"); // NOI18N
        batal_psdr_29.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_29ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_29);
        batal_psdr_29.setBounds(910, 750, 40, 22);

        kd_psdr_30.setEditable(false);
        kd_psdr_30.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_30.setEnabled(false);
        kd_psdr_30.setHighlighter(null);
        kd_psdr_30.setName("kd_psdr_30"); // NOI18N
        FormIDRG.add(kd_psdr_30);
        kd_psdr_30.setBounds(520, 780, 70, 23);

        nm_psdr_30.setEditable(false);
        nm_psdr_30.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_30.setEnabled(false);
        nm_psdr_30.setHighlighter(null);
        nm_psdr_30.setName("nm_psdr_30"); // NOI18N
        FormIDRG.add(nm_psdr_30);
        nm_psdr_30.setBounds(590, 780, 260, 23);

        multiple20.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple20.setHighlighter(null);
        multiple20.setName("multiple20"); // NOI18N
        FormIDRG.add(multiple20);
        multiple20.setBounds(850, 780, 50, 23);

        batal_psdr_30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_30.setToolTipText("");
        batal_psdr_30.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_30.setName("batal_psdr_30"); // NOI18N
        batal_psdr_30.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_30ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_30);
        batal_psdr_30.setBounds(910, 780, 40, 22);

        nm_diag_21.setEditable(false);
        nm_diag_21.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_21.setEnabled(false);
        nm_diag_21.setHighlighter(null);
        nm_diag_21.setName("nm_diag_21"); // NOI18N
        FormIDRG.add(nm_diag_21);
        nm_diag_21.setBounds(110, 510, 250, 23);

        kd_diag_21.setEditable(false);
        kd_diag_21.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_21.setEnabled(false);
        kd_diag_21.setHighlighter(null);
        kd_diag_21.setName("kd_diag_21"); // NOI18N
        FormIDRG.add(kd_diag_21);
        kd_diag_21.setBounds(40, 510, 70, 23);

        nm_diag_22.setEditable(false);
        nm_diag_22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_22.setEnabled(false);
        nm_diag_22.setHighlighter(null);
        nm_diag_22.setName("nm_diag_22"); // NOI18N
        FormIDRG.add(nm_diag_22);
        nm_diag_22.setBounds(110, 540, 250, 23);

        kd_diag_22.setEditable(false);
        kd_diag_22.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_22.setEnabled(false);
        kd_diag_22.setHighlighter(null);
        kd_diag_22.setName("kd_diag_22"); // NOI18N
        FormIDRG.add(kd_diag_22);
        kd_diag_22.setBounds(40, 540, 70, 23);

        nm_diag_23.setEditable(false);
        nm_diag_23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_23.setEnabled(false);
        nm_diag_23.setHighlighter(null);
        nm_diag_23.setName("nm_diag_23"); // NOI18N
        FormIDRG.add(nm_diag_23);
        nm_diag_23.setBounds(110, 570, 250, 23);

        kd_diag_23.setEditable(false);
        kd_diag_23.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_23.setEnabled(false);
        kd_diag_23.setHighlighter(null);
        kd_diag_23.setName("kd_diag_23"); // NOI18N
        FormIDRG.add(kd_diag_23);
        kd_diag_23.setBounds(40, 570, 70, 23);

        nm_diag_24.setEditable(false);
        nm_diag_24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_24.setEnabled(false);
        nm_diag_24.setHighlighter(null);
        nm_diag_24.setName("nm_diag_24"); // NOI18N
        FormIDRG.add(nm_diag_24);
        nm_diag_24.setBounds(110, 600, 250, 23);

        kd_diag_24.setEditable(false);
        kd_diag_24.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_24.setEnabled(false);
        kd_diag_24.setHighlighter(null);
        kd_diag_24.setName("kd_diag_24"); // NOI18N
        FormIDRG.add(kd_diag_24);
        kd_diag_24.setBounds(40, 600, 70, 23);

        nm_diag_25.setEditable(false);
        nm_diag_25.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_25.setEnabled(false);
        nm_diag_25.setHighlighter(null);
        nm_diag_25.setName("nm_diag_25"); // NOI18N
        nm_diag_25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_diag_25ActionPerformed(evt);
            }
        });
        FormIDRG.add(nm_diag_25);
        nm_diag_25.setBounds(110, 630, 250, 23);

        kd_diag_25.setEditable(false);
        kd_diag_25.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_25.setEnabled(false);
        kd_diag_25.setHighlighter(null);
        kd_diag_25.setName("kd_diag_25"); // NOI18N
        FormIDRG.add(kd_diag_25);
        kd_diag_25.setBounds(40, 630, 70, 23);

        chk_diag_21.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_21.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_21.setName("chk_diag_21"); // NOI18N
        chk_diag_21.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_21ItemStateChanged(evt);
            }
        });
        chk_diag_21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_diag_21ActionPerformed(evt);
            }
        });
        FormIDRG.add(chk_diag_21);
        chk_diag_21.setBounds(360, 510, 30, 22);

        chk_diag_22.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_22.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_22.setName("chk_diag_22"); // NOI18N
        chk_diag_22.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_22ItemStateChanged(evt);
            }
        });
        chk_diag_22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_diag_22ActionPerformed(evt);
            }
        });
        FormIDRG.add(chk_diag_22);
        chk_diag_22.setBounds(360, 540, 30, 22);

        chk_diag_23.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_23.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_23.setName("chk_diag_23"); // NOI18N
        chk_diag_23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_23ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_23);
        chk_diag_23.setBounds(360, 570, 30, 22);

        chk_diag_24.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_24.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_24.setName("chk_diag_24"); // NOI18N
        chk_diag_24.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_24ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_24);
        chk_diag_24.setBounds(360, 600, 30, 22);

        chk_diag_25.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_25.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_25.setName("chk_diag_25"); // NOI18N
        chk_diag_25.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_25ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_25);
        chk_diag_25.setBounds(360, 630, 30, 22);

        nm_diag_26.setEditable(false);
        nm_diag_26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_26.setEnabled(false);
        nm_diag_26.setHighlighter(null);
        nm_diag_26.setName("nm_diag_26"); // NOI18N
        FormIDRG.add(nm_diag_26);
        nm_diag_26.setBounds(110, 660, 250, 23);

        kd_diag_26.setEditable(false);
        kd_diag_26.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_26.setEnabled(false);
        kd_diag_26.setHighlighter(null);
        kd_diag_26.setName("kd_diag_26"); // NOI18N
        FormIDRG.add(kd_diag_26);
        kd_diag_26.setBounds(40, 660, 70, 23);

        nm_diag_27.setEditable(false);
        nm_diag_27.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_27.setEnabled(false);
        nm_diag_27.setHighlighter(null);
        nm_diag_27.setName("nm_diag_27"); // NOI18N
        FormIDRG.add(nm_diag_27);
        nm_diag_27.setBounds(110, 690, 250, 23);

        kd_diag_27.setEditable(false);
        kd_diag_27.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_27.setEnabled(false);
        kd_diag_27.setHighlighter(null);
        kd_diag_27.setName("kd_diag_27"); // NOI18N
        FormIDRG.add(kd_diag_27);
        kd_diag_27.setBounds(40, 690, 70, 23);

        nm_diag_28.setEditable(false);
        nm_diag_28.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_28.setEnabled(false);
        nm_diag_28.setHighlighter(null);
        nm_diag_28.setName("nm_diag_28"); // NOI18N
        FormIDRG.add(nm_diag_28);
        nm_diag_28.setBounds(110, 720, 250, 23);

        kd_diag_28.setEditable(false);
        kd_diag_28.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_28.setEnabled(false);
        kd_diag_28.setHighlighter(null);
        kd_diag_28.setName("kd_diag_28"); // NOI18N
        FormIDRG.add(kd_diag_28);
        kd_diag_28.setBounds(40, 720, 70, 23);

        nm_diag_29.setEditable(false);
        nm_diag_29.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_29.setEnabled(false);
        nm_diag_29.setHighlighter(null);
        nm_diag_29.setName("nm_diag_29"); // NOI18N
        FormIDRG.add(nm_diag_29);
        nm_diag_29.setBounds(110, 750, 250, 23);

        kd_diag_29.setEditable(false);
        kd_diag_29.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_29.setEnabled(false);
        kd_diag_29.setHighlighter(null);
        kd_diag_29.setName("kd_diag_29"); // NOI18N
        FormIDRG.add(kd_diag_29);
        kd_diag_29.setBounds(40, 750, 70, 23);

        nm_diag_30.setEditable(false);
        nm_diag_30.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_30.setEnabled(false);
        nm_diag_30.setHighlighter(null);
        nm_diag_30.setName("nm_diag_30"); // NOI18N
        FormIDRG.add(nm_diag_30);
        nm_diag_30.setBounds(110, 780, 250, 23);

        kd_diag_30.setEditable(false);
        kd_diag_30.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_30.setEnabled(false);
        kd_diag_30.setHighlighter(null);
        kd_diag_30.setName("kd_diag_30"); // NOI18N
        FormIDRG.add(kd_diag_30);
        kd_diag_30.setBounds(40, 780, 70, 23);

        chk_diag_26.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_26.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_26.setName("chk_diag_26"); // NOI18N
        chk_diag_26.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_26ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_26);
        chk_diag_26.setBounds(360, 660, 30, 22);

        chk_diag_27.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_27.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_27.setName("chk_diag_27"); // NOI18N
        chk_diag_27.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_27ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_27);
        chk_diag_27.setBounds(360, 690, 30, 22);

        chk_diag_28.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_28.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_28.setName("chk_diag_28"); // NOI18N
        chk_diag_28.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_28ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_28);
        chk_diag_28.setBounds(360, 720, 30, 22);

        chk_diag_29.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_29.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_29.setName("chk_diag_29"); // NOI18N
        chk_diag_29.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_29ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_29);
        chk_diag_29.setBounds(360, 750, 30, 22);

        chk_diag_30.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_30.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_30.setName("chk_diag_30"); // NOI18N
        chk_diag_30.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_30ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_30);
        chk_diag_30.setBounds(360, 780, 30, 22);

        batal_21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_21.setToolTipText("");
        batal_21.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_21.setName("batal_21"); // NOI18N
        batal_21.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_21ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_21);
        batal_21.setBounds(390, 510, 40, 22);

        batal_22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_22.setToolTipText("");
        batal_22.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_22.setName("batal_22"); // NOI18N
        batal_22.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_22ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_22);
        batal_22.setBounds(390, 540, 40, 22);

        batal_23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_23.setToolTipText("");
        batal_23.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_23.setName("batal_23"); // NOI18N
        batal_23.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_23ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_23);
        batal_23.setBounds(390, 570, 40, 22);

        batal_24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_24.setToolTipText("");
        batal_24.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_24.setName("batal_24"); // NOI18N
        batal_24.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_24ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_24);
        batal_24.setBounds(390, 600, 40, 22);

        batal_25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_25.setToolTipText("");
        batal_25.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_25.setName("batal_25"); // NOI18N
        batal_25.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_25ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_25);
        batal_25.setBounds(390, 630, 40, 22);

        batal_26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_26.setToolTipText("");
        batal_26.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_26.setName("batal_26"); // NOI18N
        batal_26.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_26ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_26);
        batal_26.setBounds(390, 660, 40, 22);

        batal_27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_27.setToolTipText("");
        batal_27.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_27.setName("batal_27"); // NOI18N
        batal_27.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_27ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_27);
        batal_27.setBounds(390, 690, 40, 22);

        batal_28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_28.setToolTipText("");
        batal_28.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_28.setName("batal_28"); // NOI18N
        batal_28.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_28ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_28);
        batal_28.setBounds(390, 720, 40, 22);

        batal_29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_29.setToolTipText("");
        batal_29.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_29.setName("batal_29"); // NOI18N
        batal_29.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_29ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_29);
        batal_29.setBounds(390, 750, 40, 22);

        batal_30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_30.setToolTipText("");
        batal_30.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_30.setName("batal_30"); // NOI18N
        batal_30.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_30ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_30);
        batal_30.setBounds(390, 780, 40, 22);

        kd_diag_41.setEditable(false);
        kd_diag_41.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_41.setEnabled(false);
        kd_diag_41.setHighlighter(null);
        kd_diag_41.setName("kd_diag_41"); // NOI18N
        FormIDRG.add(kd_diag_41);
        kd_diag_41.setBounds(40, 810, 70, 23);

        nm_diag_41.setEditable(false);
        nm_diag_41.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_41.setEnabled(false);
        nm_diag_41.setHighlighter(null);
        nm_diag_41.setName("nm_diag_41"); // NOI18N
        FormIDRG.add(nm_diag_41);
        nm_diag_41.setBounds(110, 810, 250, 23);

        chk_diag_41.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_41.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_41.setName("chk_diag_41"); // NOI18N
        chk_diag_41.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_41ItemStateChanged(evt);
            }
        });
        FormIDRG.add(chk_diag_41);
        chk_diag_41.setBounds(360, 810, 30, 22);

        batal_41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_41.setToolTipText("");
        batal_41.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_41.setName("batal_41"); // NOI18N
        batal_41.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_41ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_41);
        batal_41.setBounds(390, 810, 40, 22);

        kd_psdr_31.setEditable(false);
        kd_psdr_31.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_31.setEnabled(false);
        kd_psdr_31.setHighlighter(null);
        kd_psdr_31.setName("kd_psdr_31"); // NOI18N
        FormIDRG.add(kd_psdr_31);
        kd_psdr_31.setBounds(520, 810, 70, 23);

        nm_psdr_31.setEditable(false);
        nm_psdr_31.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_31.setEnabled(false);
        nm_psdr_31.setHighlighter(null);
        nm_psdr_31.setName("nm_psdr_31"); // NOI18N
        FormIDRG.add(nm_psdr_31);
        nm_psdr_31.setBounds(590, 810, 260, 23);

        multiple21.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple21.setHighlighter(null);
        multiple21.setName("multiple21"); // NOI18N
        FormIDRG.add(multiple21);
        multiple21.setBounds(850, 810, 50, 23);

        batal_psdr_31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_31.setToolTipText("");
        batal_psdr_31.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_31.setName("batal_psdr_31"); // NOI18N
        batal_psdr_31.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_31ActionPerformed(evt);
            }
        });
        FormIDRG.add(batal_psdr_31);
        batal_psdr_31.setBounds(910, 810, 40, 22);

        TabRawat.addTab("IDRG", FormIDRG);

        FormINACBG.setName("FormINACBG"); // NOI18N
        FormINACBG.setPreferredSize(new java.awt.Dimension(490, 842));
        FormINACBG.setLayout(null);

        jLabel94.setText("Diagnosa :");
        jLabel94.setName("jLabel94"); // NOI18N
        FormINACBG.add(jLabel94);
        jLabel94.setBounds(0, 10, 68, 23);

        DiagnosaINACBG.setHighlighter(null);
        DiagnosaINACBG.setName("DiagnosaINACBG"); // NOI18N
        DiagnosaINACBG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaINACBGKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DiagnosaINACBGKeyReleased(evt);
            }
        });
        FormINACBG.add(DiagnosaINACBG);
        DiagnosaINACBG.setBounds(70, 10, 370, 23);

        INAdiagnosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        INAdiagnosa.setMnemonic('1');
        INAdiagnosa.setToolTipText("Alt+1");
        INAdiagnosa.setName("INAdiagnosa"); // NOI18N
        INAdiagnosa.setPreferredSize(new java.awt.Dimension(28, 23));
        INAdiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INAdiagnosaActionPerformed(evt);
            }
        });
        FormINACBG.add(INAdiagnosa);
        INAdiagnosa.setBounds(440, 10, 28, 23);

        INAdiagnosatambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        INAdiagnosatambah.setMnemonic('2');
        INAdiagnosatambah.setToolTipText("Alt+2");
        INAdiagnosatambah.setName("INAdiagnosatambah"); // NOI18N
        INAdiagnosatambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INAdiagnosatambahActionPerformed(evt);
            }
        });
        FormINACBG.add(INAdiagnosatambah);
        INAdiagnosatambah.setBounds(10, 40, 28, 23);

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbDiagnosa2.setName("tbDiagnosa2"); // NOI18N
        tbDiagnosa2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDiagnosa2MouseClicked(evt);
            }
        });
        Scroll7.setViewportView(tbDiagnosa2);

        FormINACBG.add(Scroll7);
        Scroll7.setBounds(40, 40, 400, 150);

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbProsedur2.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbProsedur2.setName("tbProsedur2"); // NOI18N
        tbProsedur2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProsedur2MouseClicked(evt);
            }
        });
        Scroll8.setViewportView(tbProsedur2);

        FormINACBG.add(Scroll8);
        Scroll8.setBounds(520, 40, 430, 150);

        jLabel95.setText("Prosedur :");
        jLabel95.setName("jLabel95"); // NOI18N
        FormINACBG.add(jLabel95);
        jLabel95.setBounds(480, 10, 68, 23);

        ProsedurINACBG.setHighlighter(null);
        ProsedurINACBG.setName("ProsedurINACBG"); // NOI18N
        ProsedurINACBG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProsedurINACBGKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProsedurINACBGKeyReleased(evt);
            }
        });
        FormINACBG.add(ProsedurINACBG);
        ProsedurINACBG.setBounds(550, 10, 400, 23);

        INAprosedur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        INAprosedur.setMnemonic('1');
        INAprosedur.setToolTipText("Alt+1");
        INAprosedur.setName("INAprosedur"); // NOI18N
        INAprosedur.setPreferredSize(new java.awt.Dimension(28, 23));
        INAprosedur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INAprosedurActionPerformed(evt);
            }
        });
        FormINACBG.add(INAprosedur);
        INAprosedur.setBounds(950, 10, 28, 23);

        INAprosedurtambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        INAprosedurtambah.setMnemonic('2');
        INAprosedurtambah.setToolTipText("Alt+2");
        INAprosedurtambah.setName("INAprosedurtambah"); // NOI18N
        INAprosedurtambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INAprosedurtambahActionPerformed(evt);
            }
        });
        FormINACBG.add(INAprosedurtambah);
        INAprosedurtambah.setBounds(490, 40, 28, 23);

        jLabel183.setName("jLabel183"); // NOI18N
        jLabel183.setPreferredSize(new java.awt.Dimension(150, 14));
        FormINACBG.add(jLabel183);
        jLabel183.setBounds(980, 160, 150, 14);

        jLabel113.setText("Check Diagnosa Utama");
        jLabel113.setName("jLabel113"); // NOI18N
        FormINACBG.add(jLabel113);
        jLabel113.setBounds(270, 190, 115, 23);

        nm_diag_11.setEditable(false);
        nm_diag_11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_11.setEnabled(false);
        nm_diag_11.setHighlighter(null);
        nm_diag_11.setName("nm_diag_11"); // NOI18N
        FormINACBG.add(nm_diag_11);
        nm_diag_11.setBounds(110, 210, 250, 23);

        jLabel125.setText("DIAGNOSA PASIEN");
        jLabel125.setName("jLabel125"); // NOI18N
        FormINACBG.add(jLabel125);
        jLabel125.setBounds(20, 190, 115, 23);

        kd_diag_11.setEditable(false);
        kd_diag_11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_11.setEnabled(false);
        kd_diag_11.setHighlighter(null);
        kd_diag_11.setName("kd_diag_11"); // NOI18N
        FormINACBG.add(kd_diag_11);
        kd_diag_11.setBounds(40, 210, 70, 23);

        nm_diag_12.setEditable(false);
        nm_diag_12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_12.setEnabled(false);
        nm_diag_12.setHighlighter(null);
        nm_diag_12.setName("nm_diag_12"); // NOI18N
        FormINACBG.add(nm_diag_12);
        nm_diag_12.setBounds(110, 240, 250, 23);

        kd_diag_12.setEditable(false);
        kd_diag_12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_12.setEnabled(false);
        kd_diag_12.setHighlighter(null);
        kd_diag_12.setName("kd_diag_12"); // NOI18N
        FormINACBG.add(kd_diag_12);
        kd_diag_12.setBounds(40, 240, 70, 23);

        nm_diag_13.setEditable(false);
        nm_diag_13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_13.setEnabled(false);
        nm_diag_13.setHighlighter(null);
        nm_diag_13.setName("nm_diag_13"); // NOI18N
        FormINACBG.add(nm_diag_13);
        nm_diag_13.setBounds(110, 270, 250, 23);

        kd_diag_13.setEditable(false);
        kd_diag_13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_13.setEnabled(false);
        kd_diag_13.setHighlighter(null);
        kd_diag_13.setName("kd_diag_13"); // NOI18N
        FormINACBG.add(kd_diag_13);
        kd_diag_13.setBounds(40, 270, 70, 23);

        nm_diag_14.setEditable(false);
        nm_diag_14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_14.setEnabled(false);
        nm_diag_14.setHighlighter(null);
        nm_diag_14.setName("nm_diag_14"); // NOI18N
        FormINACBG.add(nm_diag_14);
        nm_diag_14.setBounds(110, 300, 250, 23);

        kd_diag_14.setEditable(false);
        kd_diag_14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_14.setEnabled(false);
        kd_diag_14.setHighlighter(null);
        kd_diag_14.setName("kd_diag_14"); // NOI18N
        FormINACBG.add(kd_diag_14);
        kd_diag_14.setBounds(40, 300, 70, 23);

        nm_diag_15.setEditable(false);
        nm_diag_15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_15.setEnabled(false);
        nm_diag_15.setHighlighter(null);
        nm_diag_15.setName("nm_diag_15"); // NOI18N
        nm_diag_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_diag_15ActionPerformed(evt);
            }
        });
        FormINACBG.add(nm_diag_15);
        nm_diag_15.setBounds(110, 330, 250, 23);

        kd_diag_15.setEditable(false);
        kd_diag_15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_15.setEnabled(false);
        kd_diag_15.setHighlighter(null);
        kd_diag_15.setName("kd_diag_15"); // NOI18N
        FormINACBG.add(kd_diag_15);
        kd_diag_15.setBounds(40, 330, 70, 23);

        nm_psdr_11.setEditable(false);
        nm_psdr_11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_11.setEnabled(false);
        nm_psdr_11.setHighlighter(null);
        nm_psdr_11.setName("nm_psdr_11"); // NOI18N
        nm_psdr_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_psdr_11ActionPerformed(evt);
            }
        });
        FormINACBG.add(nm_psdr_11);
        nm_psdr_11.setBounds(590, 210, 310, 23);

        kd_psdr_11.setEditable(false);
        kd_psdr_11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_11.setEnabled(false);
        kd_psdr_11.setHighlighter(null);
        kd_psdr_11.setName("kd_psdr_11"); // NOI18N
        FormINACBG.add(kd_psdr_11);
        kd_psdr_11.setBounds(520, 210, 70, 23);

        nm_psdr_12.setEditable(false);
        nm_psdr_12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_12.setEnabled(false);
        nm_psdr_12.setHighlighter(null);
        nm_psdr_12.setName("nm_psdr_12"); // NOI18N
        FormINACBG.add(nm_psdr_12);
        nm_psdr_12.setBounds(590, 240, 310, 23);

        kd_psdr_12.setEditable(false);
        kd_psdr_12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_12.setEnabled(false);
        kd_psdr_12.setHighlighter(null);
        kd_psdr_12.setName("kd_psdr_12"); // NOI18N
        FormINACBG.add(kd_psdr_12);
        kd_psdr_12.setBounds(520, 240, 70, 23);

        nm_psdr_13.setEditable(false);
        nm_psdr_13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_13.setEnabled(false);
        nm_psdr_13.setHighlighter(null);
        nm_psdr_13.setName("nm_psdr_13"); // NOI18N
        FormINACBG.add(nm_psdr_13);
        nm_psdr_13.setBounds(590, 270, 310, 23);

        kd_psdr_13.setEditable(false);
        kd_psdr_13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_13.setEnabled(false);
        kd_psdr_13.setHighlighter(null);
        kd_psdr_13.setName("kd_psdr_13"); // NOI18N
        FormINACBG.add(kd_psdr_13);
        kd_psdr_13.setBounds(520, 270, 70, 23);

        nm_psdr_14.setEditable(false);
        nm_psdr_14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_14.setEnabled(false);
        nm_psdr_14.setHighlighter(null);
        nm_psdr_14.setName("nm_psdr_14"); // NOI18N
        FormINACBG.add(nm_psdr_14);
        nm_psdr_14.setBounds(590, 300, 310, 23);

        kd_psdr_14.setEditable(false);
        kd_psdr_14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_14.setEnabled(false);
        kd_psdr_14.setHighlighter(null);
        kd_psdr_14.setName("kd_psdr_14"); // NOI18N
        FormINACBG.add(kd_psdr_14);
        kd_psdr_14.setBounds(520, 300, 70, 23);

        nm_psdr_15.setEditable(false);
        nm_psdr_15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_15.setEnabled(false);
        nm_psdr_15.setHighlighter(null);
        nm_psdr_15.setName("nm_psdr_15"); // NOI18N
        FormINACBG.add(nm_psdr_15);
        nm_psdr_15.setBounds(590, 330, 310, 23);

        kd_psdr_15.setEditable(false);
        kd_psdr_15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_15.setEnabled(false);
        kd_psdr_15.setHighlighter(null);
        kd_psdr_15.setName("kd_psdr_15"); // NOI18N
        FormINACBG.add(kd_psdr_15);
        kd_psdr_15.setBounds(520, 330, 70, 23);

        chk_diag_11.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_11.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_11.setName("chk_diag_11"); // NOI18N
        chk_diag_11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_11ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_11);
        chk_diag_11.setBounds(360, 210, 30, 22);

        chk_diag_12.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_12.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_12.setName("chk_diag_12"); // NOI18N
        chk_diag_12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_12ItemStateChanged(evt);
            }
        });
        chk_diag_12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chk_diag_12MouseClicked(evt);
            }
        });
        FormINACBG.add(chk_diag_12);
        chk_diag_12.setBounds(360, 240, 30, 22);

        chk_diag_13.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_13.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_13.setName("chk_diag_13"); // NOI18N
        chk_diag_13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_13ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_13);
        chk_diag_13.setBounds(360, 270, 30, 22);

        chk_diag_14.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_14.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_14.setName("chk_diag_14"); // NOI18N
        chk_diag_14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_14ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_14);
        chk_diag_14.setBounds(360, 300, 30, 22);

        chk_diag_15.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_15.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_15.setName("chk_diag_15"); // NOI18N
        chk_diag_15.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_15ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_15);
        chk_diag_15.setBounds(360, 330, 30, 22);

        jLabel126.setText("PROSEDUR PASIEN");
        jLabel126.setName("jLabel126"); // NOI18N
        FormINACBG.add(jLabel126);
        jLabel126.setBounds(500, 190, 115, 23);

        nm_diag_16.setEditable(false);
        nm_diag_16.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_16.setEnabled(false);
        nm_diag_16.setHighlighter(null);
        nm_diag_16.setName("nm_diag_16"); // NOI18N
        FormINACBG.add(nm_diag_16);
        nm_diag_16.setBounds(110, 360, 250, 23);

        kd_diag_16.setEditable(false);
        kd_diag_16.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_16.setEnabled(false);
        kd_diag_16.setHighlighter(null);
        kd_diag_16.setName("kd_diag_16"); // NOI18N
        FormINACBG.add(kd_diag_16);
        kd_diag_16.setBounds(40, 360, 70, 23);

        nm_diag_17.setEditable(false);
        nm_diag_17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_17.setEnabled(false);
        nm_diag_17.setHighlighter(null);
        nm_diag_17.setName("nm_diag_17"); // NOI18N
        FormINACBG.add(nm_diag_17);
        nm_diag_17.setBounds(110, 390, 250, 23);

        kd_diag_17.setEditable(false);
        kd_diag_17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_17.setEnabled(false);
        kd_diag_17.setHighlighter(null);
        kd_diag_17.setName("kd_diag_17"); // NOI18N
        FormINACBG.add(kd_diag_17);
        kd_diag_17.setBounds(40, 390, 70, 23);

        nm_diag_18.setEditable(false);
        nm_diag_18.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_18.setEnabled(false);
        nm_diag_18.setHighlighter(null);
        nm_diag_18.setName("nm_diag_18"); // NOI18N
        FormINACBG.add(nm_diag_18);
        nm_diag_18.setBounds(110, 420, 250, 23);

        kd_diag_18.setEditable(false);
        kd_diag_18.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_18.setEnabled(false);
        kd_diag_18.setHighlighter(null);
        kd_diag_18.setName("kd_diag_18"); // NOI18N
        FormINACBG.add(kd_diag_18);
        kd_diag_18.setBounds(40, 420, 70, 23);

        nm_diag_19.setEditable(false);
        nm_diag_19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_19.setEnabled(false);
        nm_diag_19.setHighlighter(null);
        nm_diag_19.setName("nm_diag_19"); // NOI18N
        FormINACBG.add(nm_diag_19);
        nm_diag_19.setBounds(110, 450, 250, 23);

        kd_diag_19.setEditable(false);
        kd_diag_19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_19.setEnabled(false);
        kd_diag_19.setHighlighter(null);
        kd_diag_19.setName("kd_diag_19"); // NOI18N
        FormINACBG.add(kd_diag_19);
        kd_diag_19.setBounds(40, 450, 70, 23);

        nm_diag_20.setEditable(false);
        nm_diag_20.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_20.setEnabled(false);
        nm_diag_20.setHighlighter(null);
        nm_diag_20.setName("nm_diag_20"); // NOI18N
        FormINACBG.add(nm_diag_20);
        nm_diag_20.setBounds(110, 480, 250, 23);

        kd_diag_20.setEditable(false);
        kd_diag_20.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_20.setEnabled(false);
        kd_diag_20.setHighlighter(null);
        kd_diag_20.setName("kd_diag_20"); // NOI18N
        FormINACBG.add(kd_diag_20);
        kd_diag_20.setBounds(40, 480, 70, 23);

        chk_diag_16.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_16.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_16.setName("chk_diag_16"); // NOI18N
        chk_diag_16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_16ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_16);
        chk_diag_16.setBounds(360, 360, 30, 22);

        chk_diag_17.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_17.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_17.setName("chk_diag_17"); // NOI18N
        chk_diag_17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_17ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_17);
        chk_diag_17.setBounds(360, 390, 30, 22);

        chk_diag_18.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_18.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_18.setName("chk_diag_18"); // NOI18N
        chk_diag_18.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_18ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_18);
        chk_diag_18.setBounds(360, 420, 30, 22);

        chk_diag_19.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_19.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_19.setName("chk_diag_19"); // NOI18N
        chk_diag_19.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_19ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_19);
        chk_diag_19.setBounds(360, 450, 30, 22);

        chk_diag_20.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_20.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_20.setName("chk_diag_20"); // NOI18N
        chk_diag_20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_20ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_20);
        chk_diag_20.setBounds(360, 480, 30, 22);

        nm_psdr_16.setEditable(false);
        nm_psdr_16.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_16.setEnabled(false);
        nm_psdr_16.setHighlighter(null);
        nm_psdr_16.setName("nm_psdr_16"); // NOI18N
        FormINACBG.add(nm_psdr_16);
        nm_psdr_16.setBounds(590, 360, 310, 23);

        kd_psdr_16.setEditable(false);
        kd_psdr_16.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_16.setEnabled(false);
        kd_psdr_16.setHighlighter(null);
        kd_psdr_16.setName("kd_psdr_16"); // NOI18N
        FormINACBG.add(kd_psdr_16);
        kd_psdr_16.setBounds(520, 360, 70, 23);

        nm_psdr_17.setEditable(false);
        nm_psdr_17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_17.setEnabled(false);
        nm_psdr_17.setHighlighter(null);
        nm_psdr_17.setName("nm_psdr_17"); // NOI18N
        FormINACBG.add(nm_psdr_17);
        nm_psdr_17.setBounds(590, 390, 310, 23);

        kd_psdr_17.setEditable(false);
        kd_psdr_17.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_17.setEnabled(false);
        kd_psdr_17.setHighlighter(null);
        kd_psdr_17.setName("kd_psdr_17"); // NOI18N
        FormINACBG.add(kd_psdr_17);
        kd_psdr_17.setBounds(520, 390, 70, 23);

        nm_psdr_18.setEditable(false);
        nm_psdr_18.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_18.setEnabled(false);
        nm_psdr_18.setHighlighter(null);
        nm_psdr_18.setName("nm_psdr_18"); // NOI18N
        FormINACBG.add(nm_psdr_18);
        nm_psdr_18.setBounds(590, 420, 310, 23);

        kd_psdr_18.setEditable(false);
        kd_psdr_18.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_18.setEnabled(false);
        kd_psdr_18.setHighlighter(null);
        kd_psdr_18.setName("kd_psdr_18"); // NOI18N
        FormINACBG.add(kd_psdr_18);
        kd_psdr_18.setBounds(520, 420, 70, 23);

        nm_psdr_19.setEditable(false);
        nm_psdr_19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_19.setEnabled(false);
        nm_psdr_19.setHighlighter(null);
        nm_psdr_19.setName("nm_psdr_19"); // NOI18N
        FormINACBG.add(nm_psdr_19);
        nm_psdr_19.setBounds(590, 450, 310, 23);

        kd_psdr_19.setEditable(false);
        kd_psdr_19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_19.setEnabled(false);
        kd_psdr_19.setHighlighter(null);
        kd_psdr_19.setName("kd_psdr_19"); // NOI18N
        FormINACBG.add(kd_psdr_19);
        kd_psdr_19.setBounds(520, 450, 70, 23);

        nm_psdr_20.setEditable(false);
        nm_psdr_20.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_20.setEnabled(false);
        nm_psdr_20.setHighlighter(null);
        nm_psdr_20.setName("nm_psdr_20"); // NOI18N
        FormINACBG.add(nm_psdr_20);
        nm_psdr_20.setBounds(590, 480, 310, 23);

        kd_psdr_20.setEditable(false);
        kd_psdr_20.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_20.setEnabled(false);
        kd_psdr_20.setHighlighter(null);
        kd_psdr_20.setName("kd_psdr_20"); // NOI18N
        FormINACBG.add(kd_psdr_20);
        kd_psdr_20.setBounds(520, 480, 70, 23);

        batal_11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_11.setToolTipText("");
        batal_11.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_11.setName("batal_11"); // NOI18N
        batal_11.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_11ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_11);
        batal_11.setBounds(390, 210, 40, 22);

        batal_12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_12.setToolTipText("");
        batal_12.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_12.setName("batal_12"); // NOI18N
        batal_12.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_12ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_12);
        batal_12.setBounds(390, 240, 40, 22);

        batal_13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_13.setToolTipText("");
        batal_13.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_13.setName("batal_13"); // NOI18N
        batal_13.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_13ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_13);
        batal_13.setBounds(390, 270, 40, 22);

        batal_14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_14.setToolTipText("");
        batal_14.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_14.setName("batal_14"); // NOI18N
        batal_14.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_14ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_14);
        batal_14.setBounds(390, 300, 40, 22);

        batal_15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_15.setToolTipText("");
        batal_15.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_15.setName("batal_15"); // NOI18N
        batal_15.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_15ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_15);
        batal_15.setBounds(390, 330, 40, 22);

        batal_16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_16.setToolTipText("");
        batal_16.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_16.setName("batal_16"); // NOI18N
        batal_16.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_16ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_16);
        batal_16.setBounds(390, 360, 40, 22);

        batal_17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_17.setToolTipText("");
        batal_17.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_17.setName("batal_17"); // NOI18N
        batal_17.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_17ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_17);
        batal_17.setBounds(390, 390, 40, 22);

        batal_18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_18.setToolTipText("");
        batal_18.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_18.setName("batal_18"); // NOI18N
        batal_18.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_18ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_18);
        batal_18.setBounds(390, 420, 40, 22);

        batal_19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_19.setToolTipText("");
        batal_19.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_19.setName("batal_19"); // NOI18N
        batal_19.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_19ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_19);
        batal_19.setBounds(390, 450, 40, 22);

        batal_20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_20.setToolTipText("");
        batal_20.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_20.setName("batal_20"); // NOI18N
        batal_20.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_20ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_20);
        batal_20.setBounds(390, 480, 40, 22);

        batal_psdr_11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_11.setToolTipText("");
        batal_psdr_11.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_11.setName("batal_psdr_11"); // NOI18N
        batal_psdr_11.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_11ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_11);
        batal_psdr_11.setBounds(910, 210, 40, 22);

        batal_psdr_12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_12.setToolTipText("");
        batal_psdr_12.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_12.setName("batal_psdr_12"); // NOI18N
        batal_psdr_12.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_12ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_12);
        batal_psdr_12.setBounds(910, 240, 40, 22);

        batal_psdr_13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_13.setToolTipText("");
        batal_psdr_13.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_13.setName("batal_psdr_13"); // NOI18N
        batal_psdr_13.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_13ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_13);
        batal_psdr_13.setBounds(910, 270, 40, 22);

        batal_psdr_14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_14.setToolTipText("");
        batal_psdr_14.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_14.setName("batal_psdr_14"); // NOI18N
        batal_psdr_14.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_14ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_14);
        batal_psdr_14.setBounds(910, 300, 40, 22);

        batal_psdr_15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_15.setToolTipText("");
        batal_psdr_15.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_15.setName("batal_psdr_15"); // NOI18N
        batal_psdr_15.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_15ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_15);
        batal_psdr_15.setBounds(910, 330, 40, 22);

        batal_psdr_16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_16.setToolTipText("");
        batal_psdr_16.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_16.setName("batal_psdr_16"); // NOI18N
        batal_psdr_16.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_16ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_16);
        batal_psdr_16.setBounds(910, 360, 40, 22);

        batal_psdr_17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_17.setToolTipText("");
        batal_psdr_17.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_17.setName("batal_psdr_17"); // NOI18N
        batal_psdr_17.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_17ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_17);
        batal_psdr_17.setBounds(910, 390, 40, 22);

        batal_psdr_18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_18.setToolTipText("");
        batal_psdr_18.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_18.setName("batal_psdr_18"); // NOI18N
        batal_psdr_18.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_18ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_18);
        batal_psdr_18.setBounds(910, 420, 40, 22);

        batal_psdr_19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_19.setToolTipText("");
        batal_psdr_19.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_19.setName("batal_psdr_19"); // NOI18N
        batal_psdr_19.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_19ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_19);
        batal_psdr_19.setBounds(910, 450, 40, 22);

        batal_psdr_20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_20.setToolTipText("");
        batal_psdr_20.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_20.setName("batal_psdr_20"); // NOI18N
        batal_psdr_20.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_20ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_20);
        batal_psdr_20.setBounds(910, 480, 40, 22);

        Import.setBackground(new java.awt.Color(0, 153, 102));
        Import.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        Import.setText("Import Coding");
        Import.setToolTipText("Alt+S");
        Import.setGlassColor(new java.awt.Color(255, 255, 255));
        Import.setName("Import"); // NOI18N
        Import.setPreferredSize(new java.awt.Dimension(120, 30));
        Import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportActionPerformed(evt);
            }
        });
        FormINACBG.add(Import);
        Import.setBounds(990, 10, 150, 30);

        scrollRespon1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        scrollRespon1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollRespon1.setName("scrollRespon1"); // NOI18N
        scrollRespon1.setPreferredSize(new java.awt.Dimension(370, 450));

        panelGlass9.setBackground(new java.awt.Color(187, 187, 187));
        panelGlass9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(556, 500));
        panelGlass9.setLayout(null);

        cmbDrugs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbDrugs.setName("cmbDrugs"); // NOI18N
        cmbDrugs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDrugsItemStateChanged(evt);
            }
        });
        panelGlass9.add(cmbDrugs);
        cmbDrugs.setBounds(120, 260, 160, 23);

        jLabel96.setText("Sub Acute :");
        jLabel96.setName("jLabel96"); // NOI18N
        panelGlass9.add(jLabel96);
        jLabel96.setBounds(10, 110, 100, 23);

        jLabel97.setText("Pembayar Selisih :");
        jLabel97.setName("jLabel97"); // NOI18N
        panelGlass9.add(jLabel97);
        jLabel97.setBounds(10, 410, 100, 23);

        jLabel98.setText("Group :");
        jLabel98.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel98.setName("jLabel98"); // NOI18N
        panelGlass9.add(jLabel98);
        jLabel98.setBounds(10, 290, 100, 23);

        jLabel99.setText("Chronic");
        jLabel99.setName("jLabel99"); // NOI18N
        panelGlass9.add(jLabel99);
        jLabel99.setBounds(10, 140, 100, 23);

        jLabel100.setText("Spec. Procedure :");
        jLabel100.setName("jLabel100"); // NOI18N
        panelGlass9.add(jLabel100);
        jLabel100.setBounds(10, 170, 100, 23);

        jLabel101.setText("Spec. Prosthesis :");
        jLabel101.setName("jLabel101"); // NOI18N
        panelGlass9.add(jLabel101);
        jLabel101.setBounds(10, 200, 100, 23);

        jLabel102.setText("TOTAL KLAIM :");
        jLabel102.setName("jLabel102"); // NOI18N
        panelGlass9.add(jLabel102);
        jLabel102.setBounds(10, 350, 100, 23);

        jLabel103.setText("Spec. Investigation :");
        jLabel103.setName("jLabel103"); // NOI18N
        panelGlass9.add(jLabel103);
        jLabel103.setBounds(10, 230, 100, 23);

        jLabel104.setText("Spec.Drugs :");
        jLabel104.setName("jLabel104"); // NOI18N
        panelGlass9.add(jLabel104);
        jLabel104.setBounds(10, 260, 100, 23);

        lChronic.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lChronic.setText("-");
        lChronic.setName("lChronic"); // NOI18N
        panelGlass9.add(lChronic);
        lChronic.setBounds(120, 140, 80, 23);

        jLabel105.setText("Rp. ");
        jLabel105.setName("jLabel105"); // NOI18N
        panelGlass9.add(jLabel105);
        jLabel105.setBounds(410, 280, 30, 23);

        cmbProc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbProc.setName("cmbProc"); // NOI18N
        cmbProc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProcItemStateChanged(evt);
            }
        });
        panelGlass9.add(cmbProc);
        cmbProc.setBounds(120, 170, 160, 23);

        cmbPros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbPros.setName("cmbPros"); // NOI18N
        cmbPros.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProsItemStateChanged(evt);
            }
        });
        panelGlass9.add(cmbPros);
        cmbPros.setBounds(120, 200, 160, 23);

        cmbInv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbInv.setName("cmbInv"); // NOI18N
        cmbInv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbInvItemStateChanged(evt);
            }
        });
        panelGlass9.add(cmbInv);
        cmbInv.setBounds(120, 230, 160, 23);

        lSubacute.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lSubacute.setText("-");
        lSubacute.setName("lSubacute"); // NOI18N
        panelGlass9.add(lSubacute);
        lSubacute.setBounds(120, 110, 100, 23);

        jLabel107.setText("Rp. ");
        jLabel107.setName("jLabel107"); // NOI18N
        panelGlass9.add(jLabel107);
        jLabel107.setBounds(410, 130, 30, 23);

        jLabel108.setText("Rp. ");
        jLabel108.setName("jLabel108"); // NOI18N
        panelGlass9.add(jLabel108);
        jLabel108.setBounds(410, 100, 30, 23);

        jLabel109.setText("Rp. ");
        jLabel109.setName("jLabel109"); // NOI18N
        panelGlass9.add(jLabel109);
        jLabel109.setBounds(410, 190, 30, 23);

        jLabel110.setText("Rp. ");
        jLabel110.setName("jLabel110"); // NOI18N
        panelGlass9.add(jLabel110);
        jLabel110.setBounds(410, 160, 30, 23);

        jLabel112.setText("Rp. ");
        jLabel112.setName("jLabel112"); // NOI18N
        panelGlass9.add(jLabel112);
        jLabel112.setBounds(410, 250, 30, 23);

        jLabel114.setText("Rp. ");
        jLabel114.setName("jLabel114"); // NOI18N
        panelGlass9.add(jLabel114);
        jLabel114.setBounds(410, 220, 30, 23);

        jLabel115.setText("Rp. ");
        jLabel115.setName("jLabel115"); // NOI18N
        panelGlass9.add(jLabel115);
        jLabel115.setBounds(410, 340, 30, 23);

        lblTarifKronis.setText("0");
        lblTarifKronis.setName("lblTarifKronis"); // NOI18N
        panelGlass9.add(lblTarifKronis);
        lblTarifKronis.setBounds(440, 130, 100, 23);

        lblTarifGrup.setText("0");
        lblTarifGrup.setName("lblTarifGrup"); // NOI18N
        panelGlass9.add(lblTarifGrup);
        lblTarifGrup.setBounds(440, 280, 100, 23);

        lblTarifAcute.setText("0");
        lblTarifAcute.setName("lblTarifAcute"); // NOI18N
        panelGlass9.add(lblTarifAcute);
        lblTarifAcute.setBounds(440, 100, 100, 23);

        lbltarifProc.setText("0");
        lbltarifProc.setName("lbltarifProc"); // NOI18N
        panelGlass9.add(lbltarifProc);
        lbltarifProc.setBounds(440, 160, 100, 23);

        lblTarifPros.setText("0");
        lblTarifPros.setName("lblTarifPros"); // NOI18N
        panelGlass9.add(lblTarifPros);
        lblTarifPros.setBounds(440, 190, 100, 23);

        lblTarifDrugs.setText("0");
        lblTarifDrugs.setName("lblTarifDrugs"); // NOI18N
        panelGlass9.add(lblTarifDrugs);
        lblTarifDrugs.setBounds(440, 250, 100, 23);

        lblTarifInv.setText("0");
        lblTarifInv.setName("lblTarifInv"); // NOI18N
        panelGlass9.add(lblTarifInv);
        lblTarifInv.setBounds(440, 220, 100, 23);

        lblTarifTotal.setText("0");
        lblTarifTotal.setName("lblTarifTotal"); // NOI18N
        panelGlass9.add(lblTarifTotal);
        lblTarifTotal.setBounds(440, 340, 100, 23);

        jSeparator16.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator16.setName("jSeparator16"); // NOI18N
        panelGlass9.add(jSeparator16);
        jSeparator16.setBounds(10, 400, 540, 10);

        jSeparator17.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator17.setName("jSeparator17"); // NOI18N
        panelGlass9.add(jSeparator17);
        jSeparator17.setBounds(10, 100, 540, 10);

        jSeparator18.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator18.setName("jSeparator18"); // NOI18N
        panelGlass9.add(jSeparator18);
        jSeparator18.setBounds(10, 370, 540, 10);

        jSeparator19.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator19.setName("jSeparator19"); // NOI18N
        panelGlass9.add(jSeparator19);
        jSeparator19.setBounds(10, 130, 540, 10);

        jSeparator20.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator20.setName("jSeparator20"); // NOI18N
        panelGlass9.add(jSeparator20);
        jSeparator20.setBounds(10, 160, 540, 10);

        jSeparator21.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator21.setName("jSeparator21"); // NOI18N
        panelGlass9.add(jSeparator21);
        jSeparator21.setBounds(10, 190, 540, 10);

        jSeparator22.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator22.setName("jSeparator22"); // NOI18N
        panelGlass9.add(jSeparator22);
        jSeparator22.setBounds(10, 220, 540, 10);

        jSeparator23.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator23.setName("jSeparator23"); // NOI18N
        panelGlass9.add(jSeparator23);
        jSeparator23.setBounds(10, 250, 540, 10);

        jSeparator24.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator24.setName("jSeparator24"); // NOI18N
        panelGlass9.add(jSeparator24);
        jSeparator24.setBounds(10, 280, 540, 10);

        jSeparator25.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator25.setName("jSeparator25"); // NOI18N
        panelGlass9.add(jSeparator25);
        jSeparator25.setBounds(20, 340, 530, 10);

        grupCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grupCode.setName("grupCode"); // NOI18N
        panelGlass9.add(grupCode);
        grupCode.setBounds(300, 450, 60, 23);

        jSeparator26.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator26.setName("jSeparator26"); // NOI18N
        panelGlass9.add(jSeparator26);
        jSeparator26.setBounds(10, 70, 540, 10);

        jLabel117.setText("Rp. ");
        jLabel117.setName("jLabel117"); // NOI18N
        panelGlass9.add(jLabel117);
        jLabel117.setBounds(410, 40, 30, 23);

        lblTarifRS.setText("0");
        lblTarifRS.setName("lblTarifRS"); // NOI18N
        panelGlass9.add(lblTarifRS);
        lblTarifRS.setBounds(440, 40, 100, 23);

        jLabel118.setText("Base Tarif INACBG :");
        jLabel118.setName("jLabel118"); // NOI18N
        panelGlass9.add(jLabel118);
        jLabel118.setBounds(10, 80, 100, 23);

        jLabel119.setText("Rp. ");
        jLabel119.setName("jLabel119"); // NOI18N
        panelGlass9.add(jLabel119);
        jLabel119.setBounds(410, 70, 30, 23);

        lblBaseTariff.setText("0");
        lblBaseTariff.setName("lblBaseTariff"); // NOI18N
        panelGlass9.add(lblBaseTariff);
        lblBaseTariff.setBounds(440, 70, 100, 23);

        jSeparator27.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator27.setName("jSeparator27"); // NOI18N
        panelGlass9.add(jSeparator27);
        jSeparator27.setBounds(10, 40, 540, 10);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        Txtgroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Txtgroup.setColumns(20);
        Txtgroup.setRows(5);
        Txtgroup.setName("Txtgroup"); // NOI18N
        Txtgroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtgroupKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(Txtgroup);

        panelGlass9.add(scrollPane1);
        scrollPane1.setBounds(120, 290, 170, 50);

        jLabel120.setText("Selisih :");
        jLabel120.setName("jLabel120"); // NOI18N
        panelGlass9.add(jLabel120);
        jLabel120.setBounds(10, 380, 100, 23);

        jLabel122.setText("Rp. ");
        jLabel122.setName("jLabel122"); // NOI18N
        panelGlass9.add(jLabel122);
        jLabel122.setBounds(410, 370, 30, 23);

        lblSelisih.setText("0");
        lblSelisih.setName("lblSelisih"); // NOI18N
        panelGlass9.add(lblSelisih);
        lblSelisih.setBounds(440, 370, 100, 23);

        jSeparator28.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator28.setName("jSeparator28"); // NOI18N
        panelGlass9.add(jSeparator28);
        jSeparator28.setBounds(10, 430, 540, 10);

        lJenis1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lJenis1.setText("-");
        lJenis1.setName("lJenis1"); // NOI18N
        panelGlass9.add(lJenis1);
        lJenis1.setBounds(120, 80, 180, 23);

        cmbSelisih.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Peserta", "Pemberi Kerja", "Asuransi Tambahan" }));
        cmbSelisih.setName("cmbSelisih"); // NOI18N
        cmbSelisih.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSelisihItemStateChanged(evt);
            }
        });
        panelGlass9.add(cmbSelisih);
        cmbSelisih.setBounds(120, 410, 160, 23);

        jLabel116.setText("BILLING RS :");
        jLabel116.setName("jLabel116"); // NOI18N
        panelGlass9.add(jLabel116);
        jLabel116.setBounds(10, 50, 100, 23);

        lblCodeDrugs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCodeDrugs.setText("-");
        lblCodeDrugs.setName("lblCodeDrugs"); // NOI18N
        panelGlass9.add(lblCodeDrugs);
        lblCodeDrugs.setBounds(300, 260, 110, 14);

        lblCodeProc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCodeProc.setText("-");
        lblCodeProc.setName("lblCodeProc"); // NOI18N
        panelGlass9.add(lblCodeProc);
        lblCodeProc.setBounds(300, 170, 110, 14);

        lblCodePros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCodePros.setText("-");
        lblCodePros.setName("lblCodePros"); // NOI18N
        panelGlass9.add(lblCodePros);
        lblCodePros.setBounds(300, 200, 110, 14);

        lblCodeInv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCodeInv.setText("-");
        lblCodeInv.setName("lblCodeInv"); // NOI18N
        panelGlass9.add(lblCodeInv);
        lblCodeInv.setBounds(300, 230, 110, 14);

        scrollRespon1.setViewportView(panelGlass9);

        FormINACBG.add(scrollRespon1);
        scrollRespon1.setBounds(970, 50, 370, 450);

        nm_psdr_36.setEditable(false);
        nm_psdr_36.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_36.setEnabled(false);
        nm_psdr_36.setHighlighter(null);
        nm_psdr_36.setName("nm_psdr_36"); // NOI18N
        FormINACBG.add(nm_psdr_36);
        nm_psdr_36.setBounds(590, 510, 310, 23);

        kd_psdr_36.setEditable(false);
        kd_psdr_36.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_36.setEnabled(false);
        kd_psdr_36.setHighlighter(null);
        kd_psdr_36.setName("kd_psdr_36"); // NOI18N
        FormINACBG.add(kd_psdr_36);
        kd_psdr_36.setBounds(520, 510, 70, 23);

        nm_psdr_37.setEditable(false);
        nm_psdr_37.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_37.setEnabled(false);
        nm_psdr_37.setHighlighter(null);
        nm_psdr_37.setName("nm_psdr_37"); // NOI18N
        FormINACBG.add(nm_psdr_37);
        nm_psdr_37.setBounds(590, 540, 310, 23);

        kd_psdr_37.setEditable(false);
        kd_psdr_37.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_37.setEnabled(false);
        kd_psdr_37.setHighlighter(null);
        kd_psdr_37.setName("kd_psdr_37"); // NOI18N
        FormINACBG.add(kd_psdr_37);
        kd_psdr_37.setBounds(520, 540, 70, 23);

        nm_psdr_38.setEditable(false);
        nm_psdr_38.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_38.setEnabled(false);
        nm_psdr_38.setHighlighter(null);
        nm_psdr_38.setName("nm_psdr_38"); // NOI18N
        FormINACBG.add(nm_psdr_38);
        nm_psdr_38.setBounds(590, 570, 310, 23);

        kd_psdr_38.setEditable(false);
        kd_psdr_38.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_38.setEnabled(false);
        kd_psdr_38.setHighlighter(null);
        kd_psdr_38.setName("kd_psdr_38"); // NOI18N
        FormINACBG.add(kd_psdr_38);
        kd_psdr_38.setBounds(520, 570, 70, 23);

        nm_psdr_39.setEditable(false);
        nm_psdr_39.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_39.setEnabled(false);
        nm_psdr_39.setHighlighter(null);
        nm_psdr_39.setName("nm_psdr_39"); // NOI18N
        FormINACBG.add(nm_psdr_39);
        nm_psdr_39.setBounds(590, 600, 310, 23);

        kd_psdr_39.setEditable(false);
        kd_psdr_39.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_39.setEnabled(false);
        kd_psdr_39.setHighlighter(null);
        kd_psdr_39.setName("kd_psdr_39"); // NOI18N
        FormINACBG.add(kd_psdr_39);
        kd_psdr_39.setBounds(520, 600, 70, 23);

        nm_psdr_40.setEditable(false);
        nm_psdr_40.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_40.setEnabled(false);
        nm_psdr_40.setHighlighter(null);
        nm_psdr_40.setName("nm_psdr_40"); // NOI18N
        FormINACBG.add(nm_psdr_40);
        nm_psdr_40.setBounds(590, 630, 310, 23);

        kd_psdr_40.setEditable(false);
        kd_psdr_40.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_40.setEnabled(false);
        kd_psdr_40.setHighlighter(null);
        kd_psdr_40.setName("kd_psdr_40"); // NOI18N
        FormINACBG.add(kd_psdr_40);
        kd_psdr_40.setBounds(520, 630, 70, 23);

        nm_psdr_41.setEditable(false);
        nm_psdr_41.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_41.setEnabled(false);
        nm_psdr_41.setHighlighter(null);
        nm_psdr_41.setName("nm_psdr_41"); // NOI18N
        FormINACBG.add(nm_psdr_41);
        nm_psdr_41.setBounds(590, 660, 310, 23);

        kd_psdr_41.setEditable(false);
        kd_psdr_41.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_41.setEnabled(false);
        kd_psdr_41.setHighlighter(null);
        kd_psdr_41.setName("kd_psdr_41"); // NOI18N
        FormINACBG.add(kd_psdr_41);
        kd_psdr_41.setBounds(520, 660, 70, 23);

        nm_psdr_42.setEditable(false);
        nm_psdr_42.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_42.setEnabled(false);
        nm_psdr_42.setHighlighter(null);
        nm_psdr_42.setName("nm_psdr_42"); // NOI18N
        FormINACBG.add(nm_psdr_42);
        nm_psdr_42.setBounds(590, 690, 310, 23);

        kd_psdr_42.setEditable(false);
        kd_psdr_42.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_42.setEnabled(false);
        kd_psdr_42.setHighlighter(null);
        kd_psdr_42.setName("kd_psdr_42"); // NOI18N
        FormINACBG.add(kd_psdr_42);
        kd_psdr_42.setBounds(520, 690, 70, 23);

        nm_psdr_43.setEditable(false);
        nm_psdr_43.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_43.setEnabled(false);
        nm_psdr_43.setHighlighter(null);
        nm_psdr_43.setName("nm_psdr_43"); // NOI18N
        FormINACBG.add(nm_psdr_43);
        nm_psdr_43.setBounds(590, 720, 310, 23);

        kd_psdr_43.setEditable(false);
        kd_psdr_43.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_43.setEnabled(false);
        kd_psdr_43.setHighlighter(null);
        kd_psdr_43.setName("kd_psdr_43"); // NOI18N
        FormINACBG.add(kd_psdr_43);
        kd_psdr_43.setBounds(520, 720, 70, 23);

        nm_psdr_44.setEditable(false);
        nm_psdr_44.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_44.setEnabled(false);
        nm_psdr_44.setHighlighter(null);
        nm_psdr_44.setName("nm_psdr_44"); // NOI18N
        FormINACBG.add(nm_psdr_44);
        nm_psdr_44.setBounds(590, 750, 310, 23);

        kd_psdr_44.setEditable(false);
        kd_psdr_44.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_44.setEnabled(false);
        kd_psdr_44.setHighlighter(null);
        kd_psdr_44.setName("kd_psdr_44"); // NOI18N
        FormINACBG.add(kd_psdr_44);
        kd_psdr_44.setBounds(520, 750, 70, 23);

        nm_psdr_45.setEditable(false);
        nm_psdr_45.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_45.setEnabled(false);
        nm_psdr_45.setHighlighter(null);
        nm_psdr_45.setName("nm_psdr_45"); // NOI18N
        FormINACBG.add(nm_psdr_45);
        nm_psdr_45.setBounds(590, 780, 310, 23);

        kd_psdr_45.setEditable(false);
        kd_psdr_45.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_45.setEnabled(false);
        kd_psdr_45.setHighlighter(null);
        kd_psdr_45.setName("kd_psdr_45"); // NOI18N
        FormINACBG.add(kd_psdr_45);
        kd_psdr_45.setBounds(520, 780, 70, 23);

        batal_psdr_36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_36.setToolTipText("");
        batal_psdr_36.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_36.setName("batal_psdr_36"); // NOI18N
        batal_psdr_36.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_36ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_36);
        batal_psdr_36.setBounds(910, 510, 40, 22);

        batal_psdr_37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_37.setToolTipText("");
        batal_psdr_37.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_37.setName("batal_psdr_37"); // NOI18N
        batal_psdr_37.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_37ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_37);
        batal_psdr_37.setBounds(910, 540, 40, 22);

        batal_psdr_38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_38.setToolTipText("");
        batal_psdr_38.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_38.setName("batal_psdr_38"); // NOI18N
        batal_psdr_38.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_38ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_38);
        batal_psdr_38.setBounds(910, 570, 40, 22);

        batal_psdr_39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_39.setToolTipText("");
        batal_psdr_39.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_39.setName("batal_psdr_39"); // NOI18N
        batal_psdr_39.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_39ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_39);
        batal_psdr_39.setBounds(910, 600, 40, 22);

        batal_psdr_40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_40.setToolTipText("");
        batal_psdr_40.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_40.setName("batal_psdr_40"); // NOI18N
        batal_psdr_40.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_40ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_40);
        batal_psdr_40.setBounds(910, 630, 40, 22);

        batal_psdr_41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_41.setToolTipText("");
        batal_psdr_41.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_41.setName("batal_psdr_41"); // NOI18N
        batal_psdr_41.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_41ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_41);
        batal_psdr_41.setBounds(910, 660, 40, 22);

        batal_psdr_42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_42.setToolTipText("");
        batal_psdr_42.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_42.setName("batal_psdr_42"); // NOI18N
        batal_psdr_42.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_42ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_42);
        batal_psdr_42.setBounds(910, 690, 40, 22);

        batal_psdr_43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_43.setToolTipText("");
        batal_psdr_43.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_43.setName("batal_psdr_43"); // NOI18N
        batal_psdr_43.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_43ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_43);
        batal_psdr_43.setBounds(910, 720, 40, 22);

        batal_psdr_44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_44.setToolTipText("");
        batal_psdr_44.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_44.setName("batal_psdr_44"); // NOI18N
        batal_psdr_44.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_44ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_44);
        batal_psdr_44.setBounds(910, 750, 40, 22);

        batal_psdr_45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_45.setToolTipText("");
        batal_psdr_45.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_45.setName("batal_psdr_45"); // NOI18N
        batal_psdr_45.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_45ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_45);
        batal_psdr_45.setBounds(910, 780, 40, 22);

        nm_diag_31.setEditable(false);
        nm_diag_31.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_31.setEnabled(false);
        nm_diag_31.setHighlighter(null);
        nm_diag_31.setName("nm_diag_31"); // NOI18N
        FormINACBG.add(nm_diag_31);
        nm_diag_31.setBounds(110, 510, 250, 23);

        kd_diag_31.setEditable(false);
        kd_diag_31.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_31.setEnabled(false);
        kd_diag_31.setHighlighter(null);
        kd_diag_31.setName("kd_diag_31"); // NOI18N
        FormINACBG.add(kd_diag_31);
        kd_diag_31.setBounds(40, 510, 70, 23);

        nm_diag_32.setEditable(false);
        nm_diag_32.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_32.setEnabled(false);
        nm_diag_32.setHighlighter(null);
        nm_diag_32.setName("nm_diag_32"); // NOI18N
        FormINACBG.add(nm_diag_32);
        nm_diag_32.setBounds(110, 540, 250, 23);

        kd_diag_32.setEditable(false);
        kd_diag_32.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_32.setEnabled(false);
        kd_diag_32.setHighlighter(null);
        kd_diag_32.setName("kd_diag_32"); // NOI18N
        FormINACBG.add(kd_diag_32);
        kd_diag_32.setBounds(40, 540, 70, 23);

        nm_diag_33.setEditable(false);
        nm_diag_33.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_33.setEnabled(false);
        nm_diag_33.setHighlighter(null);
        nm_diag_33.setName("nm_diag_33"); // NOI18N
        FormINACBG.add(nm_diag_33);
        nm_diag_33.setBounds(110, 570, 250, 23);

        kd_diag_33.setEditable(false);
        kd_diag_33.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_33.setEnabled(false);
        kd_diag_33.setHighlighter(null);
        kd_diag_33.setName("kd_diag_33"); // NOI18N
        FormINACBG.add(kd_diag_33);
        kd_diag_33.setBounds(40, 570, 70, 23);

        nm_diag_34.setEditable(false);
        nm_diag_34.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_34.setEnabled(false);
        nm_diag_34.setHighlighter(null);
        nm_diag_34.setName("nm_diag_34"); // NOI18N
        FormINACBG.add(nm_diag_34);
        nm_diag_34.setBounds(110, 600, 250, 23);

        kd_diag_34.setEditable(false);
        kd_diag_34.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_34.setEnabled(false);
        kd_diag_34.setHighlighter(null);
        kd_diag_34.setName("kd_diag_34"); // NOI18N
        FormINACBG.add(kd_diag_34);
        kd_diag_34.setBounds(40, 600, 70, 23);

        nm_diag_35.setEditable(false);
        nm_diag_35.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_35.setEnabled(false);
        nm_diag_35.setHighlighter(null);
        nm_diag_35.setName("nm_diag_35"); // NOI18N
        nm_diag_35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_diag_35ActionPerformed(evt);
            }
        });
        FormINACBG.add(nm_diag_35);
        nm_diag_35.setBounds(110, 630, 250, 23);

        kd_diag_35.setEditable(false);
        kd_diag_35.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_35.setEnabled(false);
        kd_diag_35.setHighlighter(null);
        kd_diag_35.setName("kd_diag_35"); // NOI18N
        FormINACBG.add(kd_diag_35);
        kd_diag_35.setBounds(40, 630, 70, 23);

        chk_diag_31.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_31.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_31.setName("chk_diag_31"); // NOI18N
        chk_diag_31.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_31ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_31);
        chk_diag_31.setBounds(360, 510, 30, 22);

        chk_diag_32.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_32.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_32.setName("chk_diag_32"); // NOI18N
        chk_diag_32.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_32ItemStateChanged(evt);
            }
        });
        chk_diag_32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chk_diag_32MouseClicked(evt);
            }
        });
        FormINACBG.add(chk_diag_32);
        chk_diag_32.setBounds(360, 540, 30, 22);

        chk_diag_33.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_33.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_33.setName("chk_diag_33"); // NOI18N
        chk_diag_33.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_33ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_33);
        chk_diag_33.setBounds(360, 570, 30, 22);

        chk_diag_34.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_34.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_34.setName("chk_diag_34"); // NOI18N
        chk_diag_34.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_34ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_34);
        chk_diag_34.setBounds(360, 600, 30, 22);

        chk_diag_35.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_35.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_35.setName("chk_diag_35"); // NOI18N
        chk_diag_35.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_35ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_35);
        chk_diag_35.setBounds(360, 630, 30, 22);

        nm_diag_36.setEditable(false);
        nm_diag_36.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_36.setEnabled(false);
        nm_diag_36.setHighlighter(null);
        nm_diag_36.setName("nm_diag_36"); // NOI18N
        FormINACBG.add(nm_diag_36);
        nm_diag_36.setBounds(110, 660, 250, 23);

        kd_diag_36.setEditable(false);
        kd_diag_36.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_36.setEnabled(false);
        kd_diag_36.setHighlighter(null);
        kd_diag_36.setName("kd_diag_36"); // NOI18N
        FormINACBG.add(kd_diag_36);
        kd_diag_36.setBounds(40, 660, 70, 23);

        nm_diag_37.setEditable(false);
        nm_diag_37.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_37.setEnabled(false);
        nm_diag_37.setHighlighter(null);
        nm_diag_37.setName("nm_diag_37"); // NOI18N
        FormINACBG.add(nm_diag_37);
        nm_diag_37.setBounds(110, 690, 250, 23);

        kd_diag_37.setEditable(false);
        kd_diag_37.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_37.setEnabled(false);
        kd_diag_37.setHighlighter(null);
        kd_diag_37.setName("kd_diag_37"); // NOI18N
        FormINACBG.add(kd_diag_37);
        kd_diag_37.setBounds(40, 690, 70, 23);

        nm_diag_38.setEditable(false);
        nm_diag_38.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_38.setEnabled(false);
        nm_diag_38.setHighlighter(null);
        nm_diag_38.setName("nm_diag_38"); // NOI18N
        FormINACBG.add(nm_diag_38);
        nm_diag_38.setBounds(110, 720, 250, 23);

        kd_diag_38.setEditable(false);
        kd_diag_38.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_38.setEnabled(false);
        kd_diag_38.setHighlighter(null);
        kd_diag_38.setName("kd_diag_38"); // NOI18N
        FormINACBG.add(kd_diag_38);
        kd_diag_38.setBounds(40, 720, 70, 23);

        nm_diag_39.setEditable(false);
        nm_diag_39.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_39.setEnabled(false);
        nm_diag_39.setHighlighter(null);
        nm_diag_39.setName("nm_diag_39"); // NOI18N
        FormINACBG.add(nm_diag_39);
        nm_diag_39.setBounds(110, 750, 250, 23);

        kd_diag_39.setEditable(false);
        kd_diag_39.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_39.setEnabled(false);
        kd_diag_39.setHighlighter(null);
        kd_diag_39.setName("kd_diag_39"); // NOI18N
        FormINACBG.add(kd_diag_39);
        kd_diag_39.setBounds(40, 750, 70, 23);

        nm_diag_40.setEditable(false);
        nm_diag_40.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_40.setEnabled(false);
        nm_diag_40.setHighlighter(null);
        nm_diag_40.setName("nm_diag_40"); // NOI18N
        FormINACBG.add(nm_diag_40);
        nm_diag_40.setBounds(110, 780, 250, 23);

        kd_diag_40.setEditable(false);
        kd_diag_40.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_40.setEnabled(false);
        kd_diag_40.setHighlighter(null);
        kd_diag_40.setName("kd_diag_40"); // NOI18N
        FormINACBG.add(kd_diag_40);
        kd_diag_40.setBounds(40, 780, 70, 23);

        chk_diag_36.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_36.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_36.setName("chk_diag_36"); // NOI18N
        chk_diag_36.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_36ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_36);
        chk_diag_36.setBounds(360, 660, 30, 22);

        chk_diag_37.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_37.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_37.setName("chk_diag_37"); // NOI18N
        chk_diag_37.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_37ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_37);
        chk_diag_37.setBounds(360, 690, 30, 22);

        chk_diag_38.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_38.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_38.setName("chk_diag_38"); // NOI18N
        chk_diag_38.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_38ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_38);
        chk_diag_38.setBounds(360, 720, 30, 22);

        chk_diag_39.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_39.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_39.setName("chk_diag_39"); // NOI18N
        chk_diag_39.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_39ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_39);
        chk_diag_39.setBounds(360, 750, 30, 22);

        chk_diag_40.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_40.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_40.setName("chk_diag_40"); // NOI18N
        chk_diag_40.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_40ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_40);
        chk_diag_40.setBounds(360, 780, 30, 22);

        batal_31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_31.setToolTipText("");
        batal_31.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_31.setName("batal_31"); // NOI18N
        batal_31.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_31ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_31);
        batal_31.setBounds(390, 510, 40, 22);

        batal_32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_32.setToolTipText("");
        batal_32.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_32.setName("batal_32"); // NOI18N
        batal_32.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_32ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_32);
        batal_32.setBounds(390, 540, 40, 22);

        batal_33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_33.setToolTipText("");
        batal_33.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_33.setName("batal_33"); // NOI18N
        batal_33.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_33ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_33);
        batal_33.setBounds(390, 570, 40, 22);

        batal_34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_34.setToolTipText("");
        batal_34.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_34.setName("batal_34"); // NOI18N
        batal_34.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_34ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_34);
        batal_34.setBounds(390, 600, 40, 22);

        batal_35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_35.setToolTipText("");
        batal_35.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_35.setName("batal_35"); // NOI18N
        batal_35.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_35ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_35);
        batal_35.setBounds(390, 630, 40, 22);

        batal_36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_36.setToolTipText("");
        batal_36.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_36.setName("batal_36"); // NOI18N
        batal_36.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_36ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_36);
        batal_36.setBounds(390, 660, 40, 22);

        batal_37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_37.setToolTipText("");
        batal_37.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_37.setName("batal_37"); // NOI18N
        batal_37.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_37ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_37);
        batal_37.setBounds(390, 690, 40, 22);

        batal_38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_38.setToolTipText("");
        batal_38.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_38.setName("batal_38"); // NOI18N
        batal_38.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_38ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_38);
        batal_38.setBounds(390, 720, 40, 22);

        batal_39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_39.setToolTipText("");
        batal_39.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_39.setName("batal_39"); // NOI18N
        batal_39.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_39ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_39);
        batal_39.setBounds(390, 750, 40, 22);

        batal_40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_40.setToolTipText("");
        batal_40.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_40.setName("batal_40"); // NOI18N
        batal_40.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_40ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_40);
        batal_40.setBounds(390, 780, 40, 22);

        kd_diag_42.setEditable(false);
        kd_diag_42.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_42.setEnabled(false);
        kd_diag_42.setHighlighter(null);
        kd_diag_42.setName("kd_diag_42"); // NOI18N
        FormINACBG.add(kd_diag_42);
        kd_diag_42.setBounds(40, 810, 70, 23);

        nm_diag_42.setEditable(false);
        nm_diag_42.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_42.setEnabled(false);
        nm_diag_42.setHighlighter(null);
        nm_diag_42.setName("nm_diag_42"); // NOI18N
        FormINACBG.add(nm_diag_42);
        nm_diag_42.setBounds(110, 810, 250, 23);

        chk_diag_42.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_42.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_42.setName("chk_diag_42"); // NOI18N
        chk_diag_42.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_42ItemStateChanged(evt);
            }
        });
        FormINACBG.add(chk_diag_42);
        chk_diag_42.setBounds(360, 810, 30, 22);

        batal_42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_42.setToolTipText("");
        batal_42.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_42.setName("batal_42"); // NOI18N
        batal_42.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_42ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_42);
        batal_42.setBounds(390, 810, 40, 22);

        kd_psdr_46.setEditable(false);
        kd_psdr_46.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_46.setEnabled(false);
        kd_psdr_46.setHighlighter(null);
        kd_psdr_46.setName("kd_psdr_46"); // NOI18N
        FormINACBG.add(kd_psdr_46);
        kd_psdr_46.setBounds(520, 810, 70, 23);

        nm_psdr_46.setEditable(false);
        nm_psdr_46.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_46.setEnabled(false);
        nm_psdr_46.setHighlighter(null);
        nm_psdr_46.setName("nm_psdr_46"); // NOI18N
        FormINACBG.add(nm_psdr_46);
        nm_psdr_46.setBounds(590, 810, 310, 23);

        batal_psdr_46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_psdr_46.setToolTipText("");
        batal_psdr_46.setGlassColor(new java.awt.Color(255, 255, 255));
        batal_psdr_46.setName("batal_psdr_46"); // NOI18N
        batal_psdr_46.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_psdr_46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_psdr_46ActionPerformed(evt);
            }
        });
        FormINACBG.add(batal_psdr_46);
        batal_psdr_46.setBounds(910, 810, 40, 22);

        TabRawat.addTab("INACBG", FormINACBG);

        FormDATAKLAIM.add(TabRawat, java.awt.BorderLayout.CENTER);

        Scroll9.setViewportView(FormDATAKLAIM);

        getContentPane().add(Scroll9, java.awt.BorderLayout.CENTER);

        panelBawah.setName("panelBawah"); // NOI18N
        panelBawah.setPreferredSize(new java.awt.Dimension(44, 54));
        panelBawah.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        hapusKlaim4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/delete_folder.png"))); // NOI18N
        hapusKlaim4.setMnemonic('K');
        hapusKlaim4.setText("Hapus Claim");
        hapusKlaim4.setToolTipText("Alt+K");
        hapusKlaim4.setEnabled(false);
        hapusKlaim4.setGlassColor(new java.awt.Color(255, 255, 255));
        hapusKlaim4.setName("hapusKlaim4"); // NOI18N
        hapusKlaim4.setPreferredSize(new java.awt.Dimension(130, 30));
        hapusKlaim4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusKlaim4ActionPerformed(evt);
            }
        });
        hapusKlaim4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusKlaim4KeyPressed(evt);
            }
        });
        panelBawah.add(hapusKlaim4);

        BtnGrouperIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/users.png"))); // NOI18N
        BtnGrouperIDRG.setMnemonic('B');
        BtnGrouperIDRG.setText("Grouping IDRG");
        BtnGrouperIDRG.setToolTipText("Alt+B");
        BtnGrouperIDRG.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnGrouperIDRG.setName("BtnGrouperIDRG"); // NOI18N
        BtnGrouperIDRG.setPreferredSize(new java.awt.Dimension(150, 30));
        BtnGrouperIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrouperIDRGActionPerformed(evt);
            }
        });
        panelBawah.add(BtnGrouperIDRG);

        finalklaimIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaimIDRG.setText("Final IDRG");
        finalklaimIDRG.setToolTipText("Alt+S");
        finalklaimIDRG.setGlassColor(new java.awt.Color(255, 255, 255));
        finalklaimIDRG.setName("finalklaimIDRG"); // NOI18N
        finalklaimIDRG.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaimIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaimIDRGActionPerformed(evt);
            }
        });
        panelBawah.add(finalklaimIDRG);

        editKlaimIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaimIDRG.setText("Edit IDRG");
        editKlaimIDRG.setToolTipText("Alt+S");
        editKlaimIDRG.setGlassColor(new java.awt.Color(255, 255, 255));
        editKlaimIDRG.setName("editKlaimIDRG"); // NOI18N
        editKlaimIDRG.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaimIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaimIDRGActionPerformed(evt);
            }
        });
        panelBawah.add(editKlaimIDRG);

        BtnGrouperINA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/users.png"))); // NOI18N
        BtnGrouperINA.setMnemonic('B');
        BtnGrouperINA.setText("Grouping INACBG");
        BtnGrouperINA.setToolTipText("Alt+B");
        BtnGrouperINA.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnGrouperINA.setName("BtnGrouperINA"); // NOI18N
        BtnGrouperINA.setPreferredSize(new java.awt.Dimension(150, 30));
        BtnGrouperINA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrouperINAActionPerformed(evt);
            }
        });
        BtnGrouperINA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnGrouperINAKeyPressed(evt);
            }
        });
        panelBawah.add(BtnGrouperINA);

        finalINA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalINA.setText("Final INACBG");
        finalINA.setToolTipText("Alt+S");
        finalINA.setGlassColor(new java.awt.Color(255, 255, 255));
        finalINA.setName("finalINA"); // NOI18N
        finalINA.setPreferredSize(new java.awt.Dimension(120, 30));
        finalINA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalINAActionPerformed(evt);
            }
        });
        panelBawah.add(finalINA);

        editINA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editINA.setText("Edit INACBG");
        editINA.setToolTipText("Alt+S");
        editINA.setGlassColor(new java.awt.Color(255, 255, 255));
        editINA.setName("editINA"); // NOI18N
        editINA.setPreferredSize(new java.awt.Dimension(120, 30));
        editINA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editINAActionPerformed(evt);
            }
        });
        panelBawah.add(editINA);

        finalklaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaim.setText("Final Klaim");
        finalklaim.setToolTipText("Alt+S");
        finalklaim.setGlassColor(new java.awt.Color(255, 255, 255));
        finalklaim.setName("finalklaim"); // NOI18N
        finalklaim.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaimActionPerformed(evt);
            }
        });
        panelBawah.add(finalklaim);

        editKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaim.setText("Edit Klaim");
        editKlaim.setToolTipText("Alt+S");
        editKlaim.setGlassColor(new java.awt.Color(255, 255, 255));
        editKlaim.setName("editKlaim"); // NOI18N
        editKlaim.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaimActionPerformed(evt);
            }
        });
        panelBawah.add(editKlaim);

        kirimonline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline.setText("Kirim Online");
        kirimonline.setToolTipText("Alt+S");
        kirimonline.setGlassColor(new java.awt.Color(255, 255, 255));
        kirimonline.setName("kirimonline"); // NOI18N
        kirimonline.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonlineActionPerformed(evt);
            }
        });
        panelBawah.add(kirimonline);

        CetakKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        CetakKlaim.setText("Cetak Klaim");
        CetakKlaim.setToolTipText("Alt+S");
        CetakKlaim.setGlassColor(new java.awt.Color(255, 255, 255));
        CetakKlaim.setName("CetakKlaim"); // NOI18N
        CetakKlaim.setPreferredSize(new java.awt.Dimension(120, 30));
        CetakKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakKlaimActionPerformed(evt);
            }
        });
        panelBawah.add(CetakKlaim);

        BtnKeluar4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar4.setMnemonic('K');
        BtnKeluar4.setText("Keluar");
        BtnKeluar4.setToolTipText("Alt+K");
        BtnKeluar4.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnKeluar4.setName("BtnKeluar4"); // NOI18N
        BtnKeluar4.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar4ActionPerformed(evt);
            }
        });
        BtnKeluar4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar4KeyPressed(evt);
            }
        });
        panelBawah.add(BtnKeluar4);

        getContentPane().add(panelBawah, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       Scroll9.getVerticalScrollBar().setUnitIncrement(20);                                 
       Scroll4.getVerticalScrollBar().setUnitIncrement(20);
       TabRawatMouseClicked(null);       
       scrollRespon1.getHorizontalScrollBar().setUnitIncrement(16);
       scrollRespon1.getVerticalScrollBar().setUnitIncrement(16);
       
        // Cek encounter di background agar tidak freeze UI
        new Thread(() -> {
            loadStatusKlaim();  // ← tambah
            satusehatEncounterSet();  // langsung cek, hasil ke lblStatusEncounter
        }).start();
    }//GEN-LAST:event_formWindowOpened

    private void klaimbaru(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson = "{\n"
                                   + " \"metadata\": {\n"
                                   + " \"method\": \"new_claim\"\n"
                                   + "},\n"
                                   + " \"data\": {\n"+
                                                    "\"nomor_kartu\": \""+no_kartu.getText().trim()+"\",\n" +
                                                    "\"nomor_sep\": \""+Tsep.getText().trim()+"\",\n" +
                                                    "\"nomor_rm\": \""+TNoRM.getText().trim()+"\",\n" +
                                                    "\"nama_pasien\": \""+TPasien.getText().trim()+"\",\n" +
                                                    "\"tgl_lahir\": \""+tgl_lahir.getText().trim()+" 00:00:00\",\n" +
                                                    "\"gender\": \""+(jenis_kelamin.getText().trim().equals("L") ? 1 : 2)+"\""
                                            + " }\n"
                                   + "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                
                System.out.println("respon klaimbaru : "+response);

                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response");
                        if (data.has("patient_id")) {
                            if(Sequel.menyimpantf2("inacbg_klaim_baru","?,?,?,?","EKLAIM",4,new String[]{
                               Tsep.getText(),data.getInt("patient_id")+"",data.getInt("admission_id")+"",data.getInt("hospital_admission_id")+""
                            })==false){
                                Sequel.mengedit("inacbg_klaim_baru", "no_sep='"+Tsep.getText()+"'", "patient_id='"+data.getInt("patient_id")+"',admission_id='"+data.getInt("admission_id")+"',hospital_admission_id='"+data.getInt("hospital_admission_id")+"'");
                            }
                        }
                        System.out.println("RESPON : "+code.getString("message"));
                    }else{
                        if (!code.getString("message").contains("Duplikasi")) {
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void grouper1(){   
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"1\",\n" +
                                            "\"grouper\":\"inacbg\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response grouper1 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        Sequel.meghapus("inacbg_grouping_stage1", "no_sep", Tsep.getText());
                        Sequel.meghapus("inacbg_grouping_stage2", "no_sep", Tsep.getText());
                        Sequel.meghapus("inacbg_prosedur", "no_sep", Tsep.getText());
                        Sequel.meghapus("inacbg_diagnosa", "no_sep", Tsep.getText());

                        JSONObject data = response.getJSONObject("response_inacbg");
                        JSONObject trf = data.getJSONObject("cbg");
                        if (data.has("tariff")) {
                            Sequel.menyimpantf2("inacbg_grouping_stage1","?,?,?,?","GROUPER EKLAIM",4,new String[]{
                               Tsep.getText(),trf.getString("code"),trf.getString("description"),data.getDouble("tariff")+""
                            });
                            
                            for (i = 0; i < kd_diag2.length ; i++) {
                                if (!kd_diag2[i].getText().trim().equals("")) {
                                    Sequel.menyimpantf2("inacbg_diagnosa","?,?,?,?","INA EKLAIM",4,new String[]{
                                        Tsep.getText(),kd_diag2[i].getText(),nm_diag2[i].getText(),(chk_diag2[i].isSelected() == true ? "1" : "0")
                                    });
                                }
                            }
                            
                            for (i = 0; i < kd_psdr2.length ; i++) {
                                if (!kd_psdr2[i].getText().trim().equals("")) {
                                    Sequel.menyimpantf2("inacbg_prosedur","?,?,?","INA PSDR EKLAIM",3,new String[]{
                                        Tsep.getText(),kd_psdr2[i].getText(),nm_psdr2[i].getText()
                                    });
                                }
                            }
                            
                            lblTarifRS.setText(tarifRS.getText());
                            lblBaseTariff.setText(Valid.SetAngka(data.getDouble("tariff")));
                            lblTarifTotal.setText(Valid.SetAngka(data.getDouble("tariff")));// ← tambahan
                            Txtgroup.setText(trf.getString("description"));
                            lJenis1.setText(trf.getString("code"));
                            lblSelisih.setText(""+Sequel.cariIsi("select biaya from inacbg_selisihbayar where no_sep='"+Tsep.getText().trim()+"'"));
                            
                            JOptionPane.showMessageDialog(null,"RESPON : GROUPER BERHASIL");
                        }else{
                            lblBaseTariff.setText("0");
                            Txtgroup.setText(trf.getString("description"));
                            lJenis1.setText(trf.getString("code"));
                            JOptionPane.showMessageDialog(null,"RESPON : GROUPER GAGAL");
                        }
                        
                        if (data.has("add_payment_amt") && data.optDouble("add_payment_amt", 0.0) > 0) {
                            cmbSelisih.setEnabled(true);
                            lblSelisih.setText(Valid.SetAngka(data.getDouble("add_payment_amt")));
                        } else {
                            cmbSelisih.setEnabled(false);
                            lblSelisih.setText("0");
                        }
                        
                        if(response.has("special_cmg_option")){
                            isLoading = true;
                            
                            cmbProc.setEnabled(true);
                            cmbDrugs.setEnabled(true);
                            cmbPros.setEnabled(true);
                            cmbInv.setEnabled(true);
                            
                            cmbProc.removeAllItems();
                            cmbPros.removeAllItems();
                            cmbInv.removeAllItems();
                            cmbDrugs.removeAllItems();

                            // tambah default
                            cmbProc.addItem("none : None");
                            cmbPros.addItem("none : None");
                            cmbInv.addItem("none : None");
                            cmbDrugs.addItem("none : None");

                            isLoading = false;

                            System.out.println("spesial : "+response.getJSONArray("special_cmg_option"));
                            JSONArray spesial = response.getJSONArray("special_cmg_option");

                            // Kumpulkan tipe yang ada
                            List<String> tipeList = new ArrayList<>();
                            boolean hasDrug = false, hasProcedure = false, hasInvestigation = false, hasProsthesis = false;

                            for (int i = 0; i < spesial.length(); i++) {
                                JSONObject cmg = spesial.getJSONObject(i);
                                String type = cmg.getString("type");
                                String itemText = cmg.getString("code") + " : " + cmg.getString("description");

                                if ("Special Prosthesis".equals(type)) {
                                    cmbPros.addItem(itemText);
                                    if (!hasProsthesis) { tipeList.add("Special Prosthesis"); hasProsthesis = true; }
                                }
                                if ("Special Procedure".equals(type)) {
                                    cmbProc.addItem(itemText);
                                    if (!hasProcedure) { tipeList.add("Special Procedure"); hasProcedure = true; }
                                }
                                if ("Special Investigation".equals(type)) {
                                    cmbInv.addItem(itemText);
                                    if (!hasInvestigation) { tipeList.add("Special Investigation"); hasInvestigation = true; }
                                }
                                if ("Special Drug".equals(type)) {
                                    cmbDrugs.addItem(itemText);
                                    if (!hasDrug) { tipeList.add("Special Drug"); hasDrug = true; }
                                }
                            }
                    // Tampilkan dialog sesuai tipe yang ada
                    String pesanTipe = String.join(", ", tipeList);
                    JOptionPane.showMessageDialog(null, "PASIEN INI MEMILIKI : " + pesanTipe);
                }
                }else{
                    JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }
                           
            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private String getCodeOnly(JComboBox combo) {
        if (combo.getSelectedItem() == null) {
            return "";
        }
        String val = combo.getSelectedItem().toString();
        if (val.toLowerCase().contains("none")) {
            return "";
        }
        String[] parts = val.split(":", 2); // ambil sebelum tanda ":"
        return parts[0].trim();
    }

    private String buildSpecialCmg() {
        StringJoiner joiner = new StringJoiner("#");

        String code1 = getCodeOnly(cmbProc);
        String code2 = getCodeOnly(cmbPros);
        String code3 = getCodeOnly(cmbInv);
        String code4 = getCodeOnly(cmbDrugs);

        if (!code1.isEmpty()) joiner.add(code1);
        if (!code2.isEmpty()) joiner.add(code2);
        if (!code3.isEmpty()) joiner.add(code3);
        if (!code4.isEmpty()) joiner.add(code4);

        return joiner.toString();
    }
    
    private void grouper2(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"2\",\n" +
                                            "\"grouper\":\"inacbg\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\",\n" +
                                            "\"special_cmg\": \""+
                                                buildSpecialCmg()
                                            +"\"\n" +
                                        "}\n" +
                                    "}";

                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject meta = response.getJSONObject("metadata"); // ← ganti "code" jadi "meta"
                System.out.println("response grouper2 : "+response);

                if (response.has("metadata")) {
                    if (meta.getInt("code") == 200) {
                        JSONObject data = response.getJSONObject("response_inacbg");

                        JOptionPane.showMessageDialog(null, "GROUPER SPESIAL BERHASIL");

                        Sequel.meghapus("inacbg_grouping_stage2", "no_sep", Tsep.getText());

                        if (data.has("special_cmg")) {
                            JSONArray trf = data.getJSONArray("special_cmg");
                            
                            // ↓ TAMBAHKAN DI SINI ↓
                            System.out.println("=== DEBUG ===");
                            System.out.println("code1 proc  : [" + getCodeOnly(cmbProc) + "]");
                            System.out.println("code2 pros  : [" + getCodeOnly(cmbPros) + "]");
                            System.out.println("code3 inv   : [" + getCodeOnly(cmbInv) + "]");
                            System.out.println("code4 drugs : [" + getCodeOnly(cmbDrugs) + "]");
                            System.out.println("trf length  : " + trf.length());
                            for (int x = 0; x < trf.length(); x++) {
                                System.out.println("  trf[" + x + "] = " + trf.getJSONObject(x).toString());
                            }
                            // ↑ SAMPAI SINI ↑

                            List<String[]> activeCombo = new ArrayList<>();

                            String code1 = getCodeOnly(cmbProc);
                            String code2 = getCodeOnly(cmbPros);
                            String code3 = getCodeOnly(cmbInv);
                            String code4 = getCodeOnly(cmbDrugs);

                            if (!code1.isEmpty()) activeCombo.add(new String[]{"proc", cmbProc.getSelectedItem().toString()});
                            if (!code2.isEmpty()) activeCombo.add(new String[]{"pros", cmbPros.getSelectedItem().toString()});
                            if (!code3.isEmpty()) activeCombo.add(new String[]{"inv",  cmbInv.getSelectedItem().toString()});
                            if (!code4.isEmpty()) activeCombo.add(new String[]{"drugs",cmbDrugs.getSelectedItem().toString()});

                            lbltarifProc.setText("0");
                            lblCodeProc.setText("-");
                            lblTarifPros.setText("0");
                            lblCodePros.setText("-");
                            lblTarifInv.setText("0");
                            lblCodeInv.setText("-");
                            lblTarifDrugs.setText("0");
                            lblCodeDrugs.setText("-");

                            for (int i = 0; i < trf.length(); i++) {
                                if (i >= activeCombo.size()) break;

                                JSONObject obj = trf.getJSONObject(i);
                                double tarif = obj.getDouble("tariff");
                                String type = obj.getString("type");
                                String cmgCode = obj.optString("code", ""); // ← ganti "code" jadi "cmgCode"
                                String comboLabel = activeCombo.get(i)[0];
                                String comboText  = activeCombo.get(i)[1];

                                switch (comboLabel) {
                                    case "proc":
                                        lbltarifProc.setText(Valid.SetAngka(tarif));
                                        lblCodeProc.setText(cmgCode);
                                        Sequel.menyimpantf2("inacbg_grouping_stage2", "?,?,?,?,?", "GROUPER EKLAIM", 5,
                                            new String[]{Tsep.getText(), cmgCode, comboText, tarif + "", type});
                                        break;
                                    case "pros":
                                        lblTarifPros.setText(Valid.SetAngka(tarif));
                                        lblCodePros.setText(cmgCode);
                                        Sequel.menyimpantf2("inacbg_grouping_stage2", "?,?,?,?,?", "GROUPER EKLAIM", 5,
                                            new String[]{Tsep.getText(), cmgCode, comboText, tarif + "", type});
                                        break;
                                    case "inv":
                                        lblTarifInv.setText(Valid.SetAngka(tarif));
                                        lblCodeInv.setText(cmgCode);
                                        Sequel.menyimpantf2("inacbg_grouping_stage2", "?,?,?,?,?", "GROUPER EKLAIM", 5,
                                            new String[]{Tsep.getText(), cmgCode, comboText, tarif + "", type});
                                        break;
                                    case "drugs":
                                        lblTarifDrugs.setText(Valid.SetAngka(tarif));
                                        lblCodeDrugs.setText(cmgCode);
                                        Sequel.menyimpantf2("inacbg_grouping_stage2", "?,?,?,?,?", "GROUPER EKLAIM", 5,
                                            new String[]{Tsep.getText(), cmgCode, comboText, tarif + "", type});
                                        break;
                                }
                            }
                        }

                        if (data.has("base_tariff")) {
                            lblBaseTariff.setText(Valid.SetAngka(data.getDouble("base_tariff")));
                        }
                        if (data.has("tariff")) {
                            lblTarifTotal.setText(Valid.SetAngka(data.getDouble("tariff")));
                        }

                    } else {
                        System.out.println("response : " + response);
                    }
                } else {
                    System.out.println("response : " + response);
                    System.err.println("\n The 'response_inacbg' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void setdataklaim(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {            
            switch (jaminan.getSelectedIndex()) {
                case 0:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
                case 1:
                    payor_id = 71+"";
                    payor_cd = "COVID-19";
                    break;
                case 2:
                    payor_id = 72+"";
                    payor_cd = "KIPI";
                    break;
                case 3:
                    payor_id = 73+"";
                    payor_cd = "BBL";
                    break;
                case 4:
                    payor_id = 74+"";
                    payor_cd = "PMR";
                    break;
                case 5:
                    payor_id = 75+"";
                    payor_cd = "CO-INS";
                    break;
                case 6:
                    payor_id = 76+"";
                    payor_cd = "JPS";
                    break;
                case 7:
                    payor_id = 77+"";
                    payor_cd = "JPKP";
                    break;
                default:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
            }
            try {
//                
                String requestJson =   "{\n"+
                                        "  \"metadata\": {\n" +
                                        "        \"method\": \"set_claim_data\",\n" +
                                        "        \"nomor_sep\": \""+Tsep.getText()+"\"\n" +
                                        "    },\n" +
                                        "    \"data\": {\n" +
                                        "        \"nomor_sep\": \""+Tsep.getText()+"\",\n" +
                                        "        \"nomor_kartu\": \""+no_kartu.getText()+"\",\n" +
                                        "        \"tgl_masuk\": \""+tgl_masuk.getSelectedItem()+" "+jam_masuk.getSelectedItem()+":"+menit_masuk.getSelectedItem()+":00\",\n" +
                                        "        \"tgl_pulang\": \""+tgl_keluar.getSelectedItem()+" "+jam_keluar.getSelectedItem()+":"+menit_keluar.getSelectedItem()+":00\",\n" +
                                        "        \"cara_masuk\": \""+cara_masuk.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"jenis_rawat\": \""+jenis_rawat.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"kelas_rawat\": \""+hak_kelas.getSelectedItem().toString().split(" ")[1].trim()+"\",\n" +
                                        "        \"adl_sub_acute\": \""+sub_acute.getText()+"\",\n" +
                                        "        \"adl_chronic\": \""+chronic.getText()+"\",\n" +
                                        "        \"icu_indikator\": \""+(rawat_intensif.isSelected() == true ? 1 : 0)+"\",\n" +
                                                 
                                        (rawat_intensif.isSelected() == true ? 
                                        "        \"icu_los\": \""+hari_intensif.getText()+"\",\n" +
                                        "        \"ventilator_hour\": \""+jam_ventilator.getText()+"\",\n" +
                                        "        \"ventilator\": {\n" +
                                        "            \"use_ind\": \""+(ventilator.isSelected()==true ? 1 : 0)+"\",\n" +
                                        "            \"start_dttm\": \""+tgl_intubasi.getSelectedItem()+" "+jam_intubasi.getSelectedItem()+":"+menit_intubasi.getSelectedItem()+":00\",\n" +
                                        "            \"stop_dttm\": \""+tgl_ekstubasi.getSelectedItem()+" "+jam_ekstubasi.getSelectedItem()+":"+menit_ekstubasi.getSelectedItem()+":00\"\n" +
                                        "        },\n"  
                                        : "") + 
                        
                        
                                        "        \"upgrade_class_ind\": \""+(naik_turun_kelas.isSelected()==true ? 1 : 0)+"\",\n" +
                        
                                        (naik_turun_kelas.isSelected()==true ? 
                                        "        \"upgrade_class_class\": \""+naik_kelas_pelayanan.getSelectedItem().toString().split(":")[0].trim()+"\",\n" + 
                                        "        \"upgrade_class_los\": \""+lama_naik_kelas.getText().trim()+"\",\n" +
                                        "        \"upgrade_class_payor\": \"peserta\",\n" +
                                        "        \"add_payment_pct\": \"35\",\n" 
                                        : "") +
                                        
                                        "        \"sistole\": \""+sistole.getText()+"\",\n" +
                                        "        \"diastole\": \""+distole.getText()+"\",\n" +
                                        "        \"discharge_status\": \""+status_pulang.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        
                                        (check_hd.isSelected()==true ? 
                                        "        \"dializer_single_use\": \""+pasien_hd.getSelectedIndex()+"\",\n" : "") +
                                        "        \"kantong_darah\": \""+kt_darah.getText().trim()+"\",\n" +                                        
                        
                                        "        \"birth_weight\": \""+berat_bayi.getText()+"\",\n" +
//                                        "        \"diagnosa\": \""+icd10.getText()+"\",\n" +
//                                        "        \"procedure\": \""+icd9.getText()+"\",\n" +
//                                        "        \"diagnosa_inagrouper\": \""+icd10.getText()+"\",\n" +
//                                        "        \"procedure_inagrouper\": \""+icd9.getText()+"\",\n" +
                                        "        \"tarif_rs\": {\n" +
                                        "            \"prosedur_non_bedah\": \""+pnb.getText()+"\",\n" +
                                        "            \"prosedur_bedah\": \""+pb.getText()+"\",\n" +
                                        "            \"konsultasi\": \""+ksl.getText()+"\",\n" +
                                        "            \"tenaga_ahli\": \""+ahli.getText()+"\",\n" +
                                        "            \"keperawatan\": \""+kpr.getText()+"\",\n" +
                                        "            \"penunjang\": \""+pnj.getText()+"\",\n" +
                                        "            \"radiologi\": \""+rad.getText()+"\",\n" +
                                        "            \"laboratorium\": \""+lab.getText()+"\",\n" +
                                        "            \"pelayanan_darah\": \""+pl_darah.getText()+"\",\n" +
                                        "            \"rehabilitasi\": \""+rehab.getText()+"\",\n" +
                                        "            \"kamar\": \""+akomodasi.getText()+"\",\n" +
                                        "            \"rawat_intensif\": \""+ri.getText()+"\",\n" +
                                        "            \"obat\": \""+obat.getText()+"\",\n" +
                                        "            \"obat_kronis\": \""+kronis.getText()+"\",\n" +
                                        "            \"obat_kemoterapi\": \""+obat_kemo.getText()+"\",\n" +
                                        "            \"alkes\": \""+alkes.getText()+"\",\n" +
                                        "            \"bmhp\": \""+bmhp.getText()+"\",\n" +
                                        "            \"sewa_alat\": \""+sewa_alat.getText()+"\"\n" +
                                        "        },\n" +
//                                        "        \"pemulasaraan_jenazah\": \"1\",\n" +
//                                        "        \"kantong_jenazah\": \"1\",\n" +
//                                        "        \"peti_jenazah\": \"1\",\n" +
//                                        "        \"plastik_erat\": \"1\",\n" +
//                                        "        \"desinfektan_jenazah\": \"1\",\n" +
//                                        "        \"mobil_jenazah\": \"0\",\n" +
//                                        "        \"desinfektan_mobil_jenazah\": \"0\",\n" +
//                                        "        \"covid19_status_cd\": \"1\",\n" +
//                                        "        \"nomor_kartu_t\": \"nik\",\n" +
//                                        "        \"episodes\": \"1;12#2;3#6;5\",\n" +
//                                        "        \"covid19_cc_ind\": \"1\",\n" +
//                                        "        \"covid19_rs_darurat_ind\": \"1\",\n" +
//                                        "        \"covid19_co_insidense_ind\": \"1\",\n" +
//                                        "        \"covid19_penunjang_pengurang\": {\n" +
//                                        "            \"lab_asam_laktat\": \"1\",\n" +
//                                        "            \"lab_procalcitonin\": \"1\",\n" +
//                                        "            \"lab_crp\": \"1\",\n" +
//                                        "            \"lab_kultur\": \"1\",\n" +
//                                        "            \"lab_d_dimer\": \"1\",\n" +
//                                        "            \"lab_pt\": \"1\",\n" +
//                                        "            \"lab_aptt\": \"1\",\n" +
//                                        "            \"lab_waktu_pendarahan\": \"1\",\n" +
//                                        "            \"lab_anti_hiv\": \"1\",\n" +
//                                        "            \"lab_analisa_gas\": \"1\",\n" +
//                                        "            \"lab_albumin\": \"1\",\n" +
//                                        "            \"rad_thorax_ap_pa\": \"0\"\n" +
//                                        "        },\n" +
//                                        "        \"terapi_konvalesen\": \"1000000\",\n" +
//                                        "        \"akses_naat\": \"C\",\n" +
//                                        "        \"isoman_ind\": \"0\",\n" +
//                                        "        \"bayi_lahir_status_cd\": 1,\n" +
//                                        "        \"alteplase_ind\": 0,\n" +
                                        (Apgar.isSelected()==true ?
                                        "        \"apgar\": {\n" +
                                        "            \"menit_1\": {\n" +
                                        "                \"appearance\": "+(Appear1.getText().isEmpty() ? "0" : Appear1.getText())+",\n" +
                                        "                \"pulse\": "+(Pulse1.getText().isEmpty() ? "0" : Pulse1.getText())+",\n" +
                                        "                \"grimace\": "+(Grimace1.getText().isEmpty() ? "0" : Grimace1.getText())+",\n" +
                                        "                \"activity\": "+(Activity1.getText().isEmpty() ? "0" : Activity1.getText())+",\n" +
                                        "                \"respiration\": "+(Respiration1.getText().isEmpty() ? "0" : Respiration1.getText())+"\n" +
                                        "            },\n" +
                                        "            \"menit_5\": {\n" +
                                        "                \"appearance\": "+(Appear5.getText().isEmpty() ? "0" : Appear5.getText())+",\n" +
                                        "                \"pulse\": "+(Pulse5.getText().isEmpty() ? "0" : Pulse5.getText())+",\n" +
                                        "                \"grimace\": "+(Grimace5.getText().isEmpty() ? "0" : Grimace5.getText())+",\n" +
                                        "                \"activity\": "+(Activity5.getText().isEmpty() ? "0" : Activity5.getText())+",\n" +
                                        "                \"respiration\": "+(Respiration5.getText().isEmpty() ? "0" : Respiration5.getText())+"\n" +
                                        "            }\n" +
                                        "        },\n"
                                        : "") +
                                        (Kehamilan.isSelected()==true ?
                                        "        \"persalinan\": {\n" +
                                        "            \"usia_kehamilan\": \""+(UsiaKehamilan.getText().isEmpty() ? "" : UsiaKehamilan.getText())+"\",\n" +
                                        "            \"gravida\": \""+(Gravida.getText().isEmpty() ? "" : Gravida.getText())+"\",\n" +
                                        "            \"partus\": \""+(Partus.getText().isEmpty() ? "" : Partus.getText())+"\",\n" +
                                        "            \"abortus\": \""+(Abortus.getText().isEmpty() ? "" : Abortus.getText())+"\",\n" +
                                        "            \"onset_kontraksi\": \""+(Kontraksi.getSelectedItem() != null ? mapOnsetKontraksi(Kontraksi.getSelectedItem().toString()) : "")+"\"\n" +
//                                        "            \"delivery\": [\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"1\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:01:33\",\n" +
//                                        "                    \"letak_janin\": \"kepala\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"1\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"ya\",\n" +
//                                        "                    \"shk_lokasi\": \"tumit\",\n" +
//                                        "                    \"shk_spesimen_dttm\": \"2023-01-21 18:11:33\"\n" +
//                                        "                },\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"2\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:03:49\",\n" +
//                                        "                    \"letak_janin\": \"lintang\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"0\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"tidak\",\n" +
//                                        "                    \"shk_alasan\": \"akses-sulit\"\n" +
//                                        "                }\n" +
//                                        "            ]\n" +
                                        "        },\n"
                                        : "") +
                                        "        \"tarif_poli_eks\": \""+(tarif_eksekutif.getText().equals(0) ? 0 : tarif_eksekutif.getText())+"\",\n" +
                                        "        \"nama_dokter\": \""+dpjp.getText()+"\",\n" +
                                        "        \"kode_tarif\": \""+koneksiDB.KELASRSEKLAIM()+"\",\n" +
                                        "        \"payor_id\": \""+payor_id+"\",\n" +
                                        "        \"payor_cd\": \""+payor_cd+"\",\n" +
                                        "        \"cob_cd\": \""+cob.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"coder_nik\": \""+coder+"\"\n" +
                                        "    \n}"
                                    + " }\n";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                System.out.println("response data klaim : "+response); 
                
                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Sequel.meghapus("inacbg_data_terkirim", "no_sep", Tsep.getText());
                        Sequel.menyimpantf2("inacbg_data_terkirim","?,?","DATA EKLAIM",2,new String[]{
                               Tsep.getText(),coder
                        });
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }
                
            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRMKeyPressed

    private void pnbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pnbKeyReleased

    private void pnbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pnbKeyTyped

    private void ahliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ahliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ahliKeyReleased

    private void jenis_rawatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenis_rawatItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jenis_rawatItemStateChanged

    private void rehabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rehabKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_rehabKeyReleased

    private void radKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_radKeyReleased

    private void alkesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alkesKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_alkesKeyReleased

    private void alkesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alkesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_alkesKeyTyped

    private void obatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obatKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_obatKeyReleased

    private void pbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pbKeyPressed

    private void pbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pbKeyReleased

    private void kprKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kprKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kprKeyPressed

    private void kprKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kprKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_kprKeyReleased

    private void akomodasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akomodasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_akomodasiKeyPressed

    private void akomodasiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akomodasiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_akomodasiKeyReleased

    private void labKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_labKeyPressed

    private void labKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_labKeyReleased

    private void bmhpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bmhpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_bmhpKeyPressed

    private void bmhpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bmhpKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_bmhpKeyReleased

    private void kronisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kronisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kronisKeyPressed

    private void kronisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kronisKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_kronisKeyReleased

    private void kslKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kslKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kslKeyPressed

    private void kslKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kslKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_kslKeyReleased

    private void pnjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnjKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnjKeyPressed

    private void pnjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnjKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pnjKeyReleased

    private void riKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_riKeyPressed

    private void riKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_riKeyReleased

    private void pl_darahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pl_darahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pl_darahKeyPressed

    private void pl_darahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pl_darahKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pl_darahKeyReleased

    private void sewa_alatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sewa_alatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sewa_alatKeyPressed

    private void sewa_alatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sewa_alatKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_sewa_alatKeyReleased

    private void obat_kemoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obat_kemoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_obat_kemoKeyPressed

    private void obat_kemoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obat_kemoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_obat_kemoKeyReleased

    private void rawat_intensifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rawat_intensifItemStateChanged
        // TODO add your handling code here:
        if (rawat_intensif.isSelected() == true) {
            ventilator.setVisible(true);
            jLabel225.setVisible(true);
            hari_intensif.setVisible(true);
            ventilatorItemStateChanged(null);
        } else {
            ventilator.setVisible(false);
            jLabel225.setVisible(false);
            hari_intensif.setVisible(false);
            ventilatorItemStateChanged(null);
        }
    }//GEN-LAST:event_rawat_intensifItemStateChanged

    private void ventilatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ventilatorItemStateChanged
        // TODO add your handling code here:
        if (ventilator.isSelected() == true) {
            tgl_intubasi.setVisible(true);
            jam_intubasi.setVisible(true);
            menit_intubasi.setVisible(true);
            tgl_ekstubasi.setVisible(true);
            jam_ekstubasi.setVisible(true);
            menit_ekstubasi.setVisible(true);
            jLabel49.setVisible(true);
            jLabel224.setVisible(true);
            jLabel230.setVisible(true);
            jam_ventilator.setVisible(true);
        } else {
            tgl_intubasi.setVisible(false);
            jam_intubasi.setVisible(false);
            menit_intubasi.setVisible(false);
            tgl_ekstubasi.setVisible(false);
            jam_ekstubasi.setVisible(false);
            menit_ekstubasi.setVisible(false);
            jLabel49.setVisible(false);
            jLabel224.setVisible(false);
            jLabel230.setVisible(false);
            jam_ventilator.setVisible(false);
        }
    }//GEN-LAST:event_ventilatorItemStateChanged

    private void naik_turun_kelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_naik_turun_kelasItemStateChanged
        // TODO add your handling code here:
        if (naik_turun_kelas.isSelected() == true) {
           jLabel216.setVisible(true);
           naik_kelas_pelayanan.setVisible(true);
           jLabel217.setVisible(true);
           lama_naik_kelas.setVisible(true);
        } else {
           jLabel216.setVisible(false);
           naik_kelas_pelayanan.setVisible(false);
           jLabel217.setVisible(false);
           lama_naik_kelas.setVisible(false); 
        }
    }//GEN-LAST:event_naik_turun_kelasItemStateChanged

    private void validasi_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validasi_tbActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"YAKIN VALIDASI SITB PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (pasien_tb.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Silahkan isi nomor sitb pasien terlebih dahulu..!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson = "{\n"
                                       + " \"metadata\": {\n"
                                       + " \"method\": \"sitb_validate\"\n"
                                       + "},\n"
                                       + " \"data\": {\n"
                                       + " \"nomor_sep\": \""+Tsep.getText()+"\",\n"
                                       + " \"nomor_register_sitb\": \"" + pasien_tb.getText().trim() + "\"\n"
                                       + " }\n"
                                       + "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);

                    JSONObject response = inacbg.request(request);
                    System.out.println("response data klaim : "+response); 

                    if(response.has("metadata")){
                        JSONObject code = response.getJSONObject("metadata");
                        if (code.getInt("code") == 200) {
                            JSONObject res = response.getJSONObject("response");
                            if(res.getString("status").equals("VALID")){
                                JOptionPane.showMessageDialog(null,"RESPON : STATUS PASIEN "+TPasien.getText().trim()+" ( "+res.getString("status")+" )");
                                Sequel.menyimpan("inacbg_validasi_tb","'"+TNoRM.getText()+"','"+pasien_tb.getText().trim()+"'");
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+res.getString("detail"));
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_validasi_tbActionPerformed

    private void kelas_eksekutifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kelas_eksekutifItemStateChanged
        // TODO add your handling code here:
        if (kelas_eksekutif.isSelected() == true) {
           jLabel227.setVisible(true);
           tarif_eksekutif.setVisible(true);
        } else {
           jLabel227.setVisible(false);
           tarif_eksekutif.setVisible(false);
        }
    }//GEN-LAST:event_kelas_eksekutifItemStateChanged

    private void batal_validasi_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_validasi_tbActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"YAKIN MAU BATAL REGISTER SITB PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (pasien_tb.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Silahkan isi nomor sitb pasien terlebih dahulu..!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson = "{\n"
                                       + " \"metadata\": {\n"
                                       + " \"method\": \"sitb_invalidate\"\n"
                                       + "},\n"
                                       + " \"data\": {\n"
                                       + " \"nomor_sep\": \""+Tsep.getText()+"\"\n"
                                       + " }\n"
                                       + "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);

                    JSONObject response = inacbg.request(request);
                    System.out.println("response data klaim : "+response); 

                    if(response.has("metadata")){
                        JSONObject code = response.getJSONObject("metadata");
                        JSONObject res = response.getJSONObject("metadata");
                        if(code.getInt("code")==200){
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Sequel.meghapus("inacbg_validasi_tb","no_rkm_medis", TNoRM.getText());
                        } else {
                           JOptionPane.showMessageDialog(null,"RESPON : "+res.getString("message")); 
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_batal_validasi_tbActionPerformed

    private void check_hdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_check_hdItemStateChanged
        // TODO add your handling code here:
        if (check_hd.isSelected() == true) {
            jLabel233.setVisible(true);                    
            //jLabel234.setVisible(true);
            pasien_hd.setVisible(true);
            //kt_darah.setVisible(true);
        }else{
            jLabel233.setVisible(false);                    
            //jLabel234.setVisible(false);
            pasien_hd.setVisible(false);
            //kt_darah.setVisible(false); 
        }
    }//GEN-LAST:event_check_hdItemStateChanged

    private void chk_diag_1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_1ItemStateChanged

    private void chk_diag_2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_2ItemStateChanged

    private void chk_diag_3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_3ItemStateChanged

    private void chk_diag_4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_4ItemStateChanged

    private void chk_diag_5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_5ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_5ItemStateChanged

    private void chk_diag_6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_6ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_6ItemStateChanged

    private void chk_diag_7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_7ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_7ItemStateChanged

    private void chk_diag_8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_8ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_8ItemStateChanged

    private void chk_diag_9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_9ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_9ItemStateChanged

    private void chk_diag_10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_10ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_10ItemStateChanged

    private void batal_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_10ActionPerformed
        // TODO add your handling code here:
        kd_diag_10.setText("");
        nm_diag_10.setText("");
        chk_diag_10.setSelected(false);
    }//GEN-LAST:event_batal_10ActionPerformed

    private void batal_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_1ActionPerformed
        // TODO add your handling code here:
        kd_diag_1.setText("");
        nm_diag_1.setText("");
        chk_diag_1.setSelected(false);
    }//GEN-LAST:event_batal_1ActionPerformed

    private void batal_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_2ActionPerformed
        // TODO add your handling code here:
        kd_diag_2.setText("");
        nm_diag_2.setText("");
        chk_diag_2.setSelected(false);
    }//GEN-LAST:event_batal_2ActionPerformed

    private void batal_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_3ActionPerformed
        // TODO add your handling code here:
        kd_diag_3.setText("");
        nm_diag_3.setText("");
        chk_diag_3.setSelected(false);
    }//GEN-LAST:event_batal_3ActionPerformed

    private void batal_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_4ActionPerformed
        // TODO add your handling code here:
        kd_diag_4.setText("");
        nm_diag_4.setText("");
        chk_diag_4.setSelected(false);
    }//GEN-LAST:event_batal_4ActionPerformed

    private void batal_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_5ActionPerformed
        // TODO add your handling code here:
        kd_diag_5.setText("");
        nm_diag_5.setText("");
        chk_diag_5.setSelected(false);
    }//GEN-LAST:event_batal_5ActionPerformed

    private void batal_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_6ActionPerformed
        // TODO add your handling code here:
        kd_diag_6.setText("");
        nm_diag_6.setText("");
        chk_diag_6.setSelected(false);
    }//GEN-LAST:event_batal_6ActionPerformed

    private void batal_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_7ActionPerformed
        // TODO add your handling code here:
        kd_diag_7.setText("");
        nm_diag_7.setText("");
        chk_diag_7.setSelected(false);
    }//GEN-LAST:event_batal_7ActionPerformed

    private void batal_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_8ActionPerformed
        // TODO add your handling code here:
        kd_diag_8.setText("");
        nm_diag_8.setText("");
        chk_diag_8.setSelected(false);
    }//GEN-LAST:event_batal_8ActionPerformed

    private void batal_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_9ActionPerformed
        // TODO add your handling code here:
        kd_diag_9.setText("");
        nm_diag_9.setText("");
        chk_diag_9.setSelected(false);
    }//GEN-LAST:event_batal_9ActionPerformed

    private void batal_psdr_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_10ActionPerformed
        // TODO add your handling code here:
        kd_psdr_10.setText("");
        nm_psdr_10.setText("");
        multiple10.setText("");
    }//GEN-LAST:event_batal_psdr_10ActionPerformed

    private void batal_psdr_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_1ActionPerformed
        // TODO add your handling code here:
        kd_psdr_1.setText("");
        nm_psdr_1.setText("");
        multiple1.setText("");
    }//GEN-LAST:event_batal_psdr_1ActionPerformed

    private void batal_psdr_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_2ActionPerformed
        // TODO add your handling code here:
        kd_psdr_2.setText("");
        nm_psdr_2.setText("");
        multiple2.setText("");
    }//GEN-LAST:event_batal_psdr_2ActionPerformed

    private void batal_psdr_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_3ActionPerformed
        // TODO add your handling code here:
        kd_psdr_3.setText("");
        nm_psdr_3.setText("");
        multiple3.setText("");
    }//GEN-LAST:event_batal_psdr_3ActionPerformed

    private void batal_psdr_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_4ActionPerformed
        // TODO add your handling code here:
        kd_psdr_4.setText("");
        nm_psdr_4.setText("");
        multiple4.setText("");
    }//GEN-LAST:event_batal_psdr_4ActionPerformed

    private void batal_psdr_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_5ActionPerformed
        // TODO add your handling code here:
        kd_psdr_5.setText("");
        nm_psdr_5.setText("");
        multiple5.setText("");
    }//GEN-LAST:event_batal_psdr_5ActionPerformed

    private void batal_psdr_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_6ActionPerformed
        // TODO add your handling code here:
        kd_psdr_6.setText("");
        nm_psdr_6.setText("");
        multiple6.setText("");
    }//GEN-LAST:event_batal_psdr_6ActionPerformed

    private void batal_psdr_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_7ActionPerformed
        // TODO add your handling code here:
        kd_psdr_7.setText("");
        nm_psdr_7.setText("");
        multiple7.setText("");
    }//GEN-LAST:event_batal_psdr_7ActionPerformed

    private void batal_psdr_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_8ActionPerformed
        // TODO add your handling code here:
        kd_psdr_8.setText("");
        nm_psdr_8.setText("");
        multiple8.setText("");
    }//GEN-LAST:event_batal_psdr_8ActionPerformed

    private void batal_psdr_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_9ActionPerformed
        // TODO add your handling code here:
        kd_psdr_9.setText("");
        nm_psdr_9.setText("");
        multiple9.setText("");
    }//GEN-LAST:event_batal_psdr_9ActionPerformed

    private void DiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){

            // Refresh tabel hasil pencarian
            tampildiagnosa();

            int rowCount = tbDiagnosa.getRowCount();

            // Jika hanya satu hasil → auto select
            if (rowCount == 1) {

                String kode = tbDiagnosa.getValueAt(0, 1).toString();
                String nama = tbDiagnosa.getValueAt(0, 2).toString();
                String acc = tbDiagnosa.getValueAt(0, 3).toString();
                String asteriks = tbDiagnosa.getValueAt(0, 4).toString();
                String valid = tbDiagnosa.getValueAt(0, 5).toString();

                // ==== CEK DUPLIKASI ====
                boolean sudahAda = false;
                for (int k = 0; k < 21; k++) {
                    if (kd_diag[k].getText().equals(kode)) {
                        sudahAda = true;
                        break;
                    }
                }

                if (!sudahAda) {

                    // ==== VALIDASI KHUSUS IDRG ====
                    if ((valid.equals("0") && acc.equals("Y") && asteriks.equals("0")) ||
                        (valid.equals("0") && acc.equals("N") && asteriks.equals("0"))) {

                        JOptionPane.showMessageDialog(null,
                            "DIAGNOSA INI TIDAK VALID UNTUK GROUPING IDRG");
                        return;
                    }

                    if ((valid.equals("1") && acc.equals("N") && asteriks.equals("0") && kd_diag_1.getText().trim().equals("")) ||
                        (valid.equals("1") && acc.equals("N") && asteriks.equals("1") && kd_diag_1.getText().trim().equals(""))) {

                        JOptionPane.showMessageDialog(null,
                            "DIAGNOSA INI TIDAK BISA DIJADIKAN PRIMARY DI IDRG");
                        return;
                    }

                    // ==== MASUKKAN KE SLOT KOSONG ====
                    for (int j = 0; j < 21; j++) {
                        if (kd_diag[j].getText().trim().equals("")) {
                            kd_diag[j].setText(kode);
                            nm_diag[j].setText(nama);
                            break;
                        }
                    }
                }

            } else {
                // Jika lebih dari satu hasil → tetap fokus pada tabel
                tbDiagnosa.requestFocus();
            }
        }
    }//GEN-LAST:event_DiagnosaKeyPressed

    private void idrgPenyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idrgPenyakitActionPerformed
        // TODO add your handling code here:
        tampildiagnosa();
    }//GEN-LAST:event_idrgPenyakitActionPerformed

    private void idrgPenyakittambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idrgPenyakittambahActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tbDiagnosa.getRowCount(); i++) {
            if (tbDiagnosa.getValueAt(i, 0).toString().equals("true")) {
                String kode = tbDiagnosa.getValueAt(i, 1).toString();
                String nama = tbDiagnosa.getValueAt(i, 2).toString();
                String acc = tbDiagnosa.getValueAt(i, 3).toString();
                String asteriks = tbDiagnosa.getValueAt(i, 4).toString();
                String valid = tbDiagnosa.getValueAt(i, 5).toString();

                // ✅ Cek duplikasi dulu
                boolean sudahAda = false;
                for (int k = 0; k < 21; k++) {
                    if (kd_diag[k].getText().equals(kode)) {
                        sudahAda = true;
                        tbDiagnosa.setValueAt(false, i, 0);
                        break;
                    }
                }

                // ✅ Jika belum ada, tambah ke slot kosong
                if (!sudahAda) {
                    for (int j = 0; j < 21; j++) {
                        if (valid.trim().equals("0") && acc.trim().equals("Y") && asteriks.trim().equals("0")) {
                            JOptionPane.showMessageDialog(null, "DIAGNOSA INI TIDAK VALID UNTUK GROUPING IDRG");
                            break;
                        } else if (valid.trim().equals("0") && acc.trim().equals("N") && asteriks.trim().equals("0")) {
                            JOptionPane.showMessageDialog(null, "DIAGNOSA INI TIDAK VALID UNTUK GROUPING IDRG");
                            break;
                        } else if (valid.trim().equals("1") && acc.trim().equals("N") && asteriks.trim().equals("0") && kd_diag_1.getText().trim().equals("") ) {
                            JOptionPane.showMessageDialog(null, "DIAGNOSA INI TIDAK BISA DI JADIKAN PRIMARY DI IDRG");
                            break;
                        } else if (valid.trim().equals("1") && acc.trim().equals("N") && asteriks.trim().equals("1") && kd_diag_1.getText().trim().equals("") ){
                            JOptionPane.showMessageDialog(null, "DIAGNOSA INI TIDAK BISA DI JADIKAN PRIMARY DI IDRG");
                            break;
                        }else {
                            if (kd_diag[j].getText().trim().equals("")) { 
                                kd_diag[j].setText(kode);
                                nm_diag[j].setText(nama);
                                tbDiagnosa.setValueAt(false, i, 0);
                                break; 
                            }
                        } 
                    }
                } else {
                    // Jika sudah ada, tetap uncheck
                    tbDiagnosa.setValueAt(false, i, 0);
                }
            }
        }
        
    }//GEN-LAST:event_idrgPenyakittambahActionPerformed

    private void ProsedurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){

            // Refresh dulu daftar prosedur
            tampilprosedure();

            int rowCount = tbProsedur.getRowCount();

            // Jika hasil hanya 1 baris → auto pilih
            if (rowCount == 1) {

                String kode = tbProsedur.getValueAt(0, 1).toString();
                String nama = tbProsedur.getValueAt(0, 2).toString();
                String valid = tbProsedur.getValueAt(0, 5).toString();

                // ---- VALIDASI ----
                if (valid.trim().equals("0")) {
                    JOptionPane.showMessageDialog(null,
                        "PROSEDUR INI TIDAK VALID UNTUK GROUPING IDRG");
                    return;
                }

                // ---- MASUKKAN KE SLOT KOSONG ----
                boolean berhasilDitambah = false;
                for (int j = 0; j < 21; j++) {
                    if (kd_psdr[j].getText().trim().equals("")) {
                        kd_psdr[j].setText(kode);
                        nm_psdr[j].setText(nama);
                        multiple[j].setText("1");
                        berhasilDitambah = true;
                        break;
                    }
                }

                if (!berhasilDitambah) {
                    JOptionPane.showMessageDialog(null,
                        "Slot prosedur sudah penuh (maksimal 21)");
                }

                // ---- UPDATE STRING ICD9 ----
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < kd_psdr.length; i++) {
                    String kd = kd_psdr[i].getText().trim();
                    if (!kd.equals("")) {
                        if (sb.length() > 0) sb.append("#");
                        sb.append(kd);

                        String mult = multiple[i].getText().trim();
                        if (!mult.equals("") && !mult.equals("1")) {
                            sb.append("+").append(mult);
                        }
                    }
                }
                icd9 = sb.toString();
                System.out.println("icd9 : " + icd9);

            } else {
                // jika hasil lebih dari 1 → fokus ke tabel
                tbProsedur.requestFocus();
            }
        }
    }//GEN-LAST:event_ProsedurKeyPressed

    private void idrgProsedurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idrgProsedurActionPerformed
        // TODO add your handling code here:
        tampilprosedure();
    }//GEN-LAST:event_idrgProsedurActionPerformed

    private void idrgProseduttambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idrgProseduttambahActionPerformed
        for (int i = 0; i < tbProsedur.getRowCount(); i++) {
            if (tbProsedur.getValueAt(i, 0).toString().equals("true")) {
                String kode = tbProsedur.getValueAt(i, 1).toString();
                String nama = tbProsedur.getValueAt(i, 2).toString();
                String valid = tbProsedur.getValueAt(i, 5).toString();

                // Cek validitas
                if (valid.trim().equals("0")) {
                    JOptionPane.showMessageDialog(null, "PROSEDUR INI TIDAK VALID UNTUK GROUPING IDRG");
                    tbProsedur.setValueAt(false, i, 0);
                    continue;
                }

                // ✅ LANGSUNG TAMBAH KE SLOT KOSONG (TIDAK CEK DUPLIKASI)
                boolean berhasilDitambah = false;
                for (int j = 0; j < 21; j++) {
                    if (kd_psdr[j].getText().trim().equals("")) { 
                        kd_psdr[j].setText(kode);
                        nm_psdr[j].setText(nama);
                        multiple[j].setText("1");
                        tbProsedur.setValueAt(false, i, 0);
                        berhasilDitambah = true;
                        break; 
                    }
                }

                if (!berhasilDitambah) {
                    JOptionPane.showMessageDialog(null, "Slot prosedur sudah penuh (maksimal 21)");
                    tbProsedur.setValueAt(false, i, 0);
                }
            }
        }
    
        // Generate string icd9
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kd_psdr.length; i++) {
            String kode = kd_psdr[i].getText().trim();
            if (!kode.equals("")) {
                if (sb.length() > 0) {
                    sb.append("#");
                }
                sb.append(kode);

                String mult = multiple[i].getText().trim();
                if (!mult.equals("") && !mult.equals("1")) {
                    sb.append("+").append(mult);
                }
            }
        }
        icd9 = sb.toString();
        System.out.println("icd9 : " + icd9);
    }//GEN-LAST:event_idrgProseduttambahActionPerformed

    private void DiagnosaINACBGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaINACBGKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            // Refresh pencarian
            tampildiagnosaINA();

            int rowCount = tbDiagnosa2.getRowCount();

            // Jika hasil hanya 1 baris → pilih otomatis tanpa centang
            if (rowCount == 1) {
                String kode = tbDiagnosa2.getValueAt(0, 1).toString();
                String nama = tbDiagnosa2.getValueAt(0, 2).toString();

                // === Langsung masukkan ke slot diagnosa (logika sama seperti tombol "+" ) ===

                boolean sudahAda = false;

                // Cek duplikasi
                for (int k = 0; k < 21; k++) {
                    if (kd_diag2[k].getText().equals(kode)) {
                        sudahAda = true;
                        break;
                    }
                }

                // Masukkan ke slot kosong
                if (!sudahAda) {
                    for (int j = 0; j < 21; j++) {
                        if (kd_diag2[j].getText().trim().equals("")) {
                            kd_diag2[j].setText(kode);
                            nm_diag2[j].setText(nama);
                            break;
                        }
                    }
                }

            } else {
                // Jika lebih dari 1 baris, fokus ke tabel (normal)
                tbDiagnosa2.requestFocus();
            }
        }
    }//GEN-LAST:event_DiagnosaINACBGKeyPressed

    private void INAdiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INAdiagnosaActionPerformed
        // TODO add your handling code here:
        tampildiagnosaINA();
    }//GEN-LAST:event_INAdiagnosaActionPerformed

    private void INAdiagnosatambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INAdiagnosatambahActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tbDiagnosa2.getRowCount(); i++) {
            if (tbDiagnosa2.getValueAt(i, 0).toString().equals("true")) {
                String kode = tbDiagnosa2.getValueAt(i, 1).toString();
                String nama = tbDiagnosa2.getValueAt(i, 2).toString();

                // Cek duplikasi di slot 11-21
                boolean sudahAda = false;
                for (int k = 0; k < 21; k++) {
                    if (kd_diag2[k].getText().equals(kode)) {
                        sudahAda = true;
                        tbDiagnosa2.setValueAt(false, i, 0);
                        break;
                    }
                }

                // Tambah ke slot kosong jika belum ada
                if (!sudahAda) {
                    for (int j = 0; j < 21; j++) {
                        if (kd_diag2[j].getText().trim().equals("")) {
                            kd_diag2[j].setText(kode);
                            nm_diag2[j].setText(nama);
                            tbDiagnosa2.setValueAt(false, i, 0);
                            break;
                        }
                    }
                } else {
                    tbDiagnosa2.setValueAt(false, i, 0);
                }
            }
        }
    }//GEN-LAST:event_INAdiagnosatambahActionPerformed

    private void ProsedurINACBGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurINACBGKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
        
            // Refresh pencarian
            tampilprosedureINA();

            int rowCount = tbProsedur2.getRowCount();

            // Jika hanya 1 hasil, auto select tanpa perlu centang
            if (rowCount == 1) {

                String kode = tbProsedur2.getValueAt(0, 1).toString();
                String nama = tbProsedur2.getValueAt(0, 2).toString();

                // ==== Logika sama seperti tombol tambah ====

                boolean sudahAda = false;

                // Cek duplikasi dulu
                for (int k = 0; k < 21; k++) {
                    if (kd_psdr2[k].getText().equals(kode)) {
                        sudahAda = true;
                        break;
                    }
                }

                // Jika belum ada → masukkan ke slot kosong
                if (!sudahAda) {
                    for (int j = 0; j < 21; j++) {
                        if (kd_psdr2[j].getText().trim().equals("")) {
                            kd_psdr2[j].setText(kode);
                            nm_psdr2[j].setText(nama);
                            break;
                        }
                    }
                }

            } else {
                // Jika lebih dari satu, fokuskan ke tabel seperti biasa
                tbProsedur2.requestFocus();
            }
        }
    }//GEN-LAST:event_ProsedurINACBGKeyPressed

    private void INAprosedurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INAprosedurActionPerformed
        // TODO add your handling code here:
        tampilprosedureINA();
    }//GEN-LAST:event_INAprosedurActionPerformed

    private void INAprosedurtambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INAprosedurtambahActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tbProsedur2.getRowCount(); i++) {
            if (tbProsedur2.getValueAt(i, 0).toString().equals("true")) {
                String kode = tbProsedur2.getValueAt(i, 1).toString();
                String nama = tbProsedur2.getValueAt(i, 2).toString();

                // Cek duplikasi di slot 11–21
                boolean sudahAda = false;
                for (int k = 0; k < 21; k++) {
                    if (kd_psdr2[k].getText().equals(kode)) {
                        sudahAda = true;
                        tbProsedur2.setValueAt(false, i, 0);
                        break;
                    }
                }

                // Tambah ke slot kosong 11–21
                if (!sudahAda) {
                    for (int j = 0; j < 21; j++) {
                        if (kd_psdr2[j].getText().trim().equals("")) {
                            kd_psdr2[j].setText(kode);
                            nm_psdr2[j].setText(nama);
                            tbProsedur2.setValueAt(false, i, 0);
                            break;
                        }
                    }
                } else {
                    tbProsedur2.setValueAt(false, i, 0);
                }
            }
        }

    }//GEN-LAST:event_INAprosedurtambahActionPerformed

    private void sistoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sistoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sistoleActionPerformed

    private void BtnGrouperIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrouperIDRGActionPerformed
        // TODO add your handling code here:
        
        boolean adaIsi = false;
        for (int i = 0; i < kd_diag.length; i++) {
            if (!kd_diag[i].getText().trim().equals("")) {
                adaIsi = true;
                break; 
            }
        }
        
        boolean belumAdaDicentang = true;
        
        for (JCheckBox chk : chk_diag) {
            if (chk.isSelected()) {
                belumAdaDicentang = false;
                break;
            }
        }

        if (!adaIsi) {
            JOptionPane.showMessageDialog(null, "Belum ada diagnosa yang diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else if (belumAdaDicentang) {
            JOptionPane.showMessageDialog(null, "Belum ada diagnosa yang dicentang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }else{
            getICD9();
            getICD10();
            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (Sequel.cariInteger("select count(no_sep) from inacbg_klaim_baru where no_sep='"+Tsep.getText().trim()+"' ") == 0) {
                klaimbaru();   
            }
            setdataklaim();
            idrg_diagnosa();
            idrg_prosedur();
            grouperidrg();
            saveTarifData();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            tombolinacbg();
        }
    }//GEN-LAST:event_BtnGrouperIDRGActionPerformed

    private void finalklaimIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaimIDRGActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU FINAL IDRG PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                // PEMBARUAN API 2026-04: sebelum final, bind id_encounter SatuSehat ke klaim.
                // Server Kemkes butuh validasi RME terintegrasi sebagai syarat klaim BPJS.
                // Method satusehat_encounter_set akan show ringkasan resourceType count.
                // (Tidak blocking — kalau pasien belum ada Encounter ter-sync, user akan lihat warning
                // dan bisa decide manual apakah lanjut atau lengkapi bridging dulu.)
                satusehatEncounterSet();

                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"idrg_grouper_final\"\n" +    
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response grouper idrg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            System.out.println("response final idrg : "+code.getString("message"));
                            Sequel.meghapus("inacbg_idrg_final", "no_sep", Tsep.getText());
                            Sequel.meghapus("idrg_prosedur", "no_sep", Tsep.getText());
                            Sequel.meghapus("idrg_diagnosa", "no_sep", Tsep.getText());
                            Sequel.menyimpantf2("inacbg_idrg_final","?,?","DATA IDRG",2,new String[]{
                                   Tsep.getText(),coder
                            });
                            
                            BtnGrouperIDRG.setVisible(false);
                            finalklaimIDRG.setVisible(false);
                            editKlaimIDRG.setVisible(true);
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            
                            for (int i = 0; i < kd_psdr.length; i++) {
                                String val = kd_psdr[i].getText().trim();
                                if (!val.equals("")) {
                                    Sequel.menyimpantf2("idrg_prosedur","?,?,?,?","SIMPAN PROSEDUR IDRG",4,new String[]{
                                        Tsep.getText(),
                                        kd_psdr[i].getText().trim(),
                                        nm_psdr[i].getText().trim(),
                                        multiple[i].getText().trim()
                                    });
                                }
                            }
                            
                            for (int i = 0; i < chk_diag.length; i++) {
                                // Pastikan checkbox terpilih dan kode diagnosa tidak kosong
                                if (chk_diag[i].isSelected() && !kd_diag[i].getText().trim().equals("")) {
                                    Sequel.menyimpantf2("idrg_diagnosa","?,?,?,?","SIMPAN DIAGNOSA IDRG",4,new String[]{
                                            Tsep.getText(),
                                            kd_diag[i].getText().trim(),
                                            nm_diag[i].getText().trim(),
                                            "1"
                                        }
                                    );
                                }else if(!kd_diag[i].getText().trim().equals("")){
                                    Sequel.menyimpantf2("idrg_diagnosa","?,?,?,?","SIMPAN DIAGNOSA IDRG",4,new String[]{
                                            Tsep.getText(),
                                            kd_diag[i].getText().trim(),
                                            nm_diag[i].getText().trim(),
                                            "0"
                                        }
                                    );
                                }
                            }
                            
                            
                            lbltarifProc.setText("0");
                            lblCodeProc.setText("-");
                            lblTarifPros.setText("0");
                            lblCodePros.setText("-");
                            lblTarifInv.setText("0");
                            lblCodeInv.setText("-");
                            lblTarifDrugs.setText("0");
                            lblCodeDrugs.setText("-");
                            
                            TabRawat.setSelectedIndex(1);
                            FormIDRG.setEnabled(false);
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        tombolinacbg();
    }//GEN-LAST:event_finalklaimIDRGActionPerformed

    private void editKlaimIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaimIDRGActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU EDIT IDRG PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"idrg_grouper_reedit\"\n" +    
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response grouper idrg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            System.out.println("response final idrg : "+code.getString("message"));
                            Sequel.meghapus("inacbg_idrg_final", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_final", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_grouping_stage1", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_grouping_stage2", "no_sep", Tsep.getText());
                            Sequel.meghapus("idrg_grouper", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_diagnosa", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_prosedur", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_selisihbayar", "no_sep", Tsep.getText());
                            Txtgroup.setText("");
                            
                            for (i = 0; i < kd_diag2.length; i++) {
                                kd_diag2[i].setText("");
                                nm_diag2[i].setText("");
                                kd_psdr2[i].setText("");
                                nm_psdr2[i].setText("");
                                chk_diag2[i].setSelected(false);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        tombolinacbg();
    }//GEN-LAST:event_editKlaimIDRGActionPerformed

    private void prosedurINA() {
        icd9INA = "";

        StringBuilder sb = new StringBuilder();
        for (JTextField field : kd_psdr2) {
            String kode = field.getText().trim();
            if (!kode.equals("")) {
                if (sb.length() > 0) {
                    sb.append("#");
                }
                sb.append(kode);
            }
        }

        icd9INA = sb.toString();
        System.out.println("icd9 INA : " + icd9INA);

        // Validasi input wajib
        if (TNoRM.getText().trim().equals("") ||
            Tsep.getText().trim().equals("") ||
            no_kartu.getText().trim().equals("")) {

            JOptionPane.showMessageDialog(null,
                "DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");

        } else {
            try {
                // Buat JSON request
                String requestJson = "{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_procedure_set\",\n" +
                                            "\"nomor_sep\":\"" + Tsep.getText().trim() + "\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"procedure\":\"" + icd9INA.trim() + "\"\n" +
                                        "}\n" +
                                     "}";

                System.out.println("request JSON : " + requestJson);

                
                JSONObject response = inacbg.request(requestJson);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response set prosedur : " + response);

                if (code.getInt("code") == 200) {
                    System.out.println("Prosedur berhasil diset.");
                } else {
                    JOptionPane.showMessageDialog(null, "RESPON : " + code.getString("message"));
                }
                
            } catch (Exception e) {
                System.err.println("Error in prosedurINA: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void diagnosaINA(){
        icd10INA = "";

        String primary = "";
        List<String> secondary = new ArrayList<>();

        for (int i = 0; i < kd_diag2.length; i++) {
            String kode = kd_diag2[i].getText().trim();
            if (!kode.equals("")) {
                if (chk_diag2[i].isSelected()) {
                    // Kalau checkbox dicentang → anggap primary
                    primary = kode;
                } else {
                    secondary.add(kode);
                }
            }
        }

        // Susun ICD10 (diagnosa) → primary dulu, baru sisanya
        StringBuilder sb = new StringBuilder();
        if (!primary.equals("")) {
            sb.append(primary);
        }
        for (String kode : secondary) {
            if (sb.length() > 0) {
                sb.append("#");
            }
            sb.append(kode);
        }

        icd10INA = sb.toString();
        System.out.println("icd10 INA : " + icd10INA);

        // Validasi input wajib
        if (TNoRM.getText().trim().equals("") ||
            Tsep.getText().trim().equals("") ||
            no_kartu.getText().trim().equals("")) {

            JOptionPane.showMessageDialog(null,
                "DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Buat JSON request untuk diagnosa (ICD10)
                String requestJson = "{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_diagnosa_set\",\n" +
                                            "\"nomor_sep\":\"" + Tsep.getText().trim() + "\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"diagnosa\":\"" + icd10INA.trim() + "\"\n" +
                                        "}\n" +
                                     "}";

                System.out.println("request JSON : " + requestJson);

                JSONObject response = inacbg.request(requestJson);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response set diagnosa : " + response);

                if (code.getInt("code") == 200) {
                    System.out.println("Diagnosa berhasil diset.");
                } else {
                    JOptionPane.showMessageDialog(null, "RESPON : " + code.getString("message"));
                }
            } catch (Exception e) {
                System.err.println("Error in diagnosaINA: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
    
    private void BtnGrouperINAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrouperINAActionPerformed
        boolean adaIsi = false;
        for (int i = 0; i < kd_diag2.length; i++) {
            if (!kd_diag2[i].getText().trim().equals("")) {
                adaIsi = true;
                break; 
            }
        }
        
        boolean belumAdaDicentang = true;
        
        for (JCheckBox chk : chk_diag2) {
            if (chk.isSelected()) {
                belumAdaDicentang = false;
                break;
            }
        }

        if (!adaIsi) {
            JOptionPane.showMessageDialog(null, "Belum ada diagnosa yang diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else if (belumAdaDicentang) {
            JOptionPane.showMessageDialog(null, "Belum ada diagnosa yang dicentang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            prosedurINA();
            diagnosaINA();

            grouper1();
        }
        
        tombolinacbg();
    }//GEN-LAST:event_BtnGrouperINAActionPerformed

    private void BtnGrouperINAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGrouperINAKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGrouperINAKeyPressed

    private void finalINAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalINAActionPerformed
        // TODO add your handling code here:
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_grouper_final\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response final inacbg : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        Sequel.meghapus("inacbg_selisihbayar", "no_sep", Tsep.getText());
                        Sequel.meghapus("inacbg_final", "no_sep", Tsep.getText());
                        Sequel.meghapus("diagnosa_pasien", "no_rawat", no_rawat.getText());

                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Sequel.menyimpan("inacbg_final", "'"+Tsep.getText().trim()+"'");

                        Sequel.menyimpantf2("inacbg_selisihbayar","?,?,?,?","SELISIH EKLAIM",4,new String[]{
                           Tsep.getText(),cmbSelisih.getSelectedItem().toString().trim(),lblSelisih.getText(),coder
                        });

                        // ========== PERBAIKAN: SIMPAN DIAGNOSA KONSISTEN DENGAN diagnosaINA() ==========
                        String Jns = jenis_rawat.getSelectedItem().toString().split(":")[0].trim();
                        String statusRawat = (Jns.equals("1") ? "Ranap" : "Ralan");

                        // Identifikasi primary (checked) dan secondary (unchecked) sesuai urutan array
                        String primaryDiagnosa = "";
                        List<String> secondaryDiagnosa = new ArrayList<>();

                        for (int i = 0; i < kd_diag2.length; i++) {
                            String kode = kd_diag2[i].getText().trim();
                            if (!kode.equals("")) {
                                if (chk_diag2[i].isSelected()) {
                                    // Yang checked = Primary (Utama) - hanya 1
                                    primaryDiagnosa = kode;
                                } else {
                                    // Yang tidak checked = Secondary (Sekunder) - sesuai urutan
                                    secondaryDiagnosa.add(kode);
                                }
                            }
                        }

                        // Simpan PRIMARY dulu (Prioritas 1)
                        if (!primaryDiagnosa.equals("")) {
                            Sequel.menyimpantf2("diagnosa_pasien", "?,?,?,?,?", "DIAGNOSA", 5, new String[]{
                                no_rawat.getText(),
                                primaryDiagnosa,
                                statusRawat,
                                "1", // Prioritas 1 = UTAMA
                                "Baru"
                            });
                            System.out.println("✓ Diagnosa PRIMARY: " + primaryDiagnosa + " (prioritas: 1)");
                        }

                        // Simpan SECONDARY (Prioritas 2, 3, 4, ... sesuai urutan array)
                        int prioritas = 2;
                        for (String kode : secondaryDiagnosa) {
                            Sequel.menyimpantf2("diagnosa_pasien", "?,?,?,?,?", "DIAGNOSA", 5, new String[]{
                                no_rawat.getText(),
                                kode,
                                statusRawat,
                                String.valueOf(prioritas),
                                "Baru"
                            });
                            System.out.println("✓ Diagnosa SECONDARY: " + kode + " (prioritas: " + prioritas + ")");
                            prioritas++;
                        }

                        // ========== SIMPAN PROSEDUR DENGAN URUTAN BENAR ==========
                        Sequel.meghapus("prosedur_pasien", "no_rawat", no_rawat.getText());

                        int prioritasProsedur = 1;

                        for (int i = 0; i < kd_psdr2.length; i++) {
                            String kode = kd_psdr2[i].getText().trim();

                            if (!kode.equals("")) {
                                // Jumlahkan semua multiple yang kodenya cocok
                                int totalJumlah = 0;
                                for (int j = 0; j < kd_psdr.length; j++) {
                                    if (kd_psdr[j].getText().trim().equals(kode)) {
                                        String nilaiMultiple = multiple[j].getText().trim();
                                        try {
                                            totalJumlah += Integer.parseInt(nilaiMultiple);
                                        } catch (NumberFormatException e) {
                                            totalJumlah += 1; // jika nilai multiple tidak valid, anggap 1
                                        }
                                    }
                                }

                                // Jika tidak ditemukan sama sekali di kd_psdr, pakai default 1
                                if (totalJumlah == 0) {
                                    totalJumlah = 1;
                                }

                                Sequel.menyimpantf2("prosedur_pasien", "?,?,?,?,?", "PROSEDUR", 5, new String[]{
                                    no_rawat.getText(),
                                    kode,
                                    statusRawat,
                                    String.valueOf(prioritasProsedur),
                                    String.valueOf(totalJumlah)
                                });
                                System.out.println("✓ Prosedur: " + kode + " (prioritas: " + prioritasProsedur + 
                                                   ", jumlah: " + totalJumlah + ")");
                                prioritasProsedur++;
                            }
                        }

                        // ========== AUTO UPDATE KE RESUME_PASIEN_RANAP ==========
                        if (statusRawat.equals("Ranap")) {
                            updateResumePasienRanap(no_rawat.getText());
                        }

                        tombolinacbg();
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }

        tombolinacbg();
    }//GEN-LAST:event_finalINAActionPerformed

    private void editINAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editINAActionPerformed
        // TODO add your handling code here:
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_grouper_reedit\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";


                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response edit : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        if(code.getInt("code")==200){
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Sequel.meghapus("inacbg_final", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_diagnosa", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_prosedur", "no_sep", Tsep.getText());
                            tombolinacbg();
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        tombolinacbg();
    }//GEN-LAST:event_editINAActionPerformed

    private void finalklaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU FINAL KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"claim_final\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\",\n" +
                                                "\"coder_nik\": \""+coder+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response final inacbg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            if(code.getInt("code")==200){
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Sequel.menyimpan("inacbg_grouping_final", "'"+Tsep.getText().trim()+"'");
                                
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        tombolinacbg();
    }//GEN-LAST:event_finalklaimActionPerformed

    private void editKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU EDIT KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"reedit_claim\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response edit : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            if(code.getInt("code")==200){
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Sequel.meghapus("inacbg_grouping_final", "no_sep", Tsep.getText());
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        tombolinacbg();
    }//GEN-LAST:event_editKlaimActionPerformed

    private void kirimonlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonlineActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU KIRIM ONLINE PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
                } else {
                    try {
                        // Create a human-readable JSON string
                        String requestJson ="{\n" +
                                                "\"metadata\": {\n" +
                                                    "\"method\":\"send_claim_individual\"\n" +
                                                "},\n" +
                                                "\"data\": {\n" +
                                                    "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                                "}\n" +
                                            "}";


                        // Use the JSON string in your request
                        String request = requestJson;
                        System.out.println("request JSON : "+requestJson);
                        JSONObject response = inacbg.request(request);
                        JSONObject code = response.getJSONObject("metadata");
                        System.out.println("response kirim online : "+response);

                        if(response.has("metadata")){
                            if(code.getInt("code")==200){
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }
                        } else {
                            System.out.println("response : "+response); 
                            System.err.println("\n The 'data' field is missing in the response.");
                        }

                    } catch (Exception e) {
                        System.err.println("Error in sitb: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }catch (Exception ex) {
                System.out.println("Notifikasi Bridging Hapus : "+ex);
            }
        }
        
        tombolinacbg();
    }//GEN-LAST:event_kirimonlineActionPerformed

    private void hapusKlaim4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusKlaim4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusKlaim4ActionPerformed

    private void hapusKlaim4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusKlaim4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusKlaim4KeyPressed

    private void BtnKeluar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_BtnKeluar4ActionPerformed

    private void BtnKeluar4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluar4KeyPressed

    private void nm_diag_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_diag_5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_diag_5ActionPerformed

    private void nm_diag_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_diag_15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_diag_15ActionPerformed

    private void chk_diag_11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_11ItemStateChanged

    private void chk_diag_12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_12ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_12ItemStateChanged

    private void chk_diag_13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_13ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_13ItemStateChanged

    private void chk_diag_14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_14ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_14ItemStateChanged

    private void chk_diag_15ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_15ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_15ItemStateChanged

    private void chk_diag_16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_16ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_16ItemStateChanged

    private void chk_diag_17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_17ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_17ItemStateChanged

    private void chk_diag_18ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_18ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_18ItemStateChanged

    private void chk_diag_19ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_19ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_19ItemStateChanged

    private void chk_diag_20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_20ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_20ItemStateChanged

    private void batal_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_11ActionPerformed
        // TODO add your handling code here:
        kd_diag_11.setText("");
        nm_diag_11.setText("");
        chk_diag_11.setSelected(false);
    }//GEN-LAST:event_batal_11ActionPerformed

    private void batal_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_12ActionPerformed
        // TODO add your handling code here:
        kd_diag_12.setText("");
        nm_diag_12.setText("");
        chk_diag_12.setSelected(false);
    }//GEN-LAST:event_batal_12ActionPerformed

    private void batal_13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_13ActionPerformed
        // TODO add your handling code here:
        kd_diag_13.setText("");
        nm_diag_13.setText("");
        chk_diag_13.setSelected(false);
    }//GEN-LAST:event_batal_13ActionPerformed

    private void batal_14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_14ActionPerformed
        // TODO add your handling code here:
        kd_diag_14.setText("");
        nm_diag_14.setText("");
        chk_diag_14.setSelected(false);
    }//GEN-LAST:event_batal_14ActionPerformed

    private void batal_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_15ActionPerformed
        // TODO add your handling code here:
        kd_diag_15.setText("");
        nm_diag_15.setText("");
        chk_diag_15.setSelected(false);
    }//GEN-LAST:event_batal_15ActionPerformed

    private void batal_16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_16ActionPerformed
        // TODO add your handling code here:
        kd_diag_16.setText("");
        nm_diag_16.setText("");
        chk_diag_16.setSelected(false);
    }//GEN-LAST:event_batal_16ActionPerformed

    private void batal_17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_17ActionPerformed
        // TODO add your handling code here:
        kd_diag_17.setText("");
        nm_diag_17.setText("");
        chk_diag_17.setSelected(false);
    }//GEN-LAST:event_batal_17ActionPerformed

    private void batal_18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_18ActionPerformed
        // TODO add your handling code here:
        kd_diag_18.setText("");
        nm_diag_18.setText("");
        chk_diag_18.setSelected(false);
    }//GEN-LAST:event_batal_18ActionPerformed

    private void batal_19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_19ActionPerformed
        // TODO add your handling code here:
        kd_diag_19.setText("");
        nm_diag_19.setText("");
        chk_diag_19.setSelected(false);
    }//GEN-LAST:event_batal_19ActionPerformed

    private void batal_20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_20ActionPerformed
        // TODO add your handling code here:
        kd_diag_20.setText("");
        nm_diag_20.setText("");
        chk_diag_20.setSelected(false);
    }//GEN-LAST:event_batal_20ActionPerformed

    private void batal_psdr_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_11ActionPerformed
        // TODO add your handling code here:
        kd_psdr_11.setText("");
        nm_psdr_11.setText("");
    }//GEN-LAST:event_batal_psdr_11ActionPerformed

    private void batal_psdr_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_12ActionPerformed
        // TODO add your handling code here:
        kd_psdr_12.setText("");
        nm_psdr_12.setText("");
    }//GEN-LAST:event_batal_psdr_12ActionPerformed

    private void batal_psdr_13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_13ActionPerformed
        // TODO add your handling code here:
        kd_psdr_13.setText("");
        nm_psdr_13.setText("");
    }//GEN-LAST:event_batal_psdr_13ActionPerformed

    private void batal_psdr_14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_14ActionPerformed
        // TODO add your handling code here:
        kd_psdr_14.setText("");
        nm_psdr_14.setText("");
    }//GEN-LAST:event_batal_psdr_14ActionPerformed

    private void batal_psdr_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_15ActionPerformed
        // TODO add your handling code here:
        kd_psdr_15.setText("");
        nm_psdr_15.setText("");
    }//GEN-LAST:event_batal_psdr_15ActionPerformed

    private void batal_psdr_16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_16ActionPerformed
        // TODO add your handling code here:
        kd_psdr_16.setText("");
        nm_psdr_16.setText("");
    }//GEN-LAST:event_batal_psdr_16ActionPerformed

    private void batal_psdr_17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_17ActionPerformed
        // TODO add your handling code here:
        kd_psdr_17.setText("");
        nm_psdr_17.setText("");
    }//GEN-LAST:event_batal_psdr_17ActionPerformed

    private void batal_psdr_18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_18ActionPerformed
        // TODO add your handling code here:
        kd_psdr_18.setText("");
        nm_psdr_18.setText("");
    }//GEN-LAST:event_batal_psdr_18ActionPerformed

    private void batal_psdr_19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_19ActionPerformed
        // TODO add your handling code here:
        kd_psdr_19.setText("");
        nm_psdr_19.setText("");
    }//GEN-LAST:event_batal_psdr_19ActionPerformed

    private void batal_psdr_20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_20ActionPerformed
        // TODO add your handling code here:
        kd_psdr_20.setText("");
        nm_psdr_20.setText("");
    }//GEN-LAST:event_batal_psdr_20ActionPerformed

    private void tbDiagnosaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDiagnosaMouseClicked
        // TODO add your handling code here:
        for (i = 0; i < tbDiagnosa.getRowCount(); i++) {
            tbDiagnosa.setValueAt(false, i, 0);
        }
        tabModeDiagnosa.setValueAt(true,tbDiagnosa.getSelectedRow(), 0);
    }//GEN-LAST:event_tbDiagnosaMouseClicked

    private void tbProsedurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProsedurMouseClicked
        // TODO add your handling code here:
        for (i = 0; i < tbProsedur.getRowCount(); i++) {
            tbProsedur.setValueAt(false, i, 0);
        }
        tabModeProsedur.setValueAt(true,tbProsedur.getSelectedRow(), 0);
    }//GEN-LAST:event_tbProsedurMouseClicked

    private void tbDiagnosa2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDiagnosa2MouseClicked
        // TODO add your handling code here:
        for (i = 0; i < tbDiagnosa2.getRowCount(); i++) {
            tbDiagnosa2.setValueAt(false, i, 0);
        }
        tabModeDiagnosa2.setValueAt(true,tbDiagnosa2.getSelectedRow(), 0);
    }//GEN-LAST:event_tbDiagnosa2MouseClicked

    private void tbProsedur2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProsedur2MouseClicked
        // TODO add your handling code here:
        for (i = 0; i < tbProsedur2.getRowCount(); i++) {
            tbProsedur2.setValueAt(false, i, 0);
        }
        tabModeProsedur2.setValueAt(true,tbProsedur2.getSelectedRow(), 0);
    }//GEN-LAST:event_tbProsedur2MouseClicked

    private void nm_psdr_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_psdr_11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_psdr_11ActionPerformed

    private void chk_diag_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_diag_2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_2ActionPerformed

    private void chk_diag_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_diag_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_1ActionPerformed

    private void chk_diag_12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chk_diag_12MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_12MouseClicked

    private void TabRawatStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabRawatStateChanged
        //        if (TabRawat.getSelectedIndex() == 0) {
            //            tampilDxPx("idrg", Status.getSelectedItem().toString());
            //        } else if (TabRawat.getSelectedIndex() == 1) {
            //            tampilDxPx("inacbg", Status.getSelectedItem().toString());
            //        }
    }//GEN-LAST:event_TabRawatStateChanged

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if (TabRawat.getSelectedIndex() == 0) { 
            tampildiagnosa();
            tampilprosedure();
        } else if (TabRawat.getSelectedIndex() == 1) {
            tampildiagnosaINA();
            tampilprosedureINA();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void ImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportActionPerformed
        // TODO add your handling code here:
        for (i = 0; i < kd_psdr.length; i++) {
            kd_diag2[i].setText("");
            nm_diag2[i].setText("");
            kd_psdr2[i].setText("");
            nm_psdr2[i].setText("");
            chk_diag2[i].setSelected(false); 
        }
        
        lbltarifProc.setText("0");
        lblCodeProc.setText("-");
        lblTarifPros.setText("0");
        lblCodePros.setText("-");
        lblTarifInv.setText("0");
        lblCodeInv.setText("-");
        lblTarifDrugs.setText("0");
        lblCodeDrugs.setText("-");

        idrgtoinacbg();
    }//GEN-LAST:event_ImportActionPerformed

    private void TxtgroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtgroupKeyPressed
        //        Valid.pindah2(evt,KdPetugas,Diagnosis);
    }//GEN-LAST:event_TxtgroupKeyPressed
    
    private void cmbProcItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProcItemStateChanged
        // TODO add your handling code here:
        if (isLoading) return;
        if (cmbProc.getSelectedItem() != null && cmbPros.getSelectedItem() != null && cmbInv.getSelectedItem() != null && cmbDrugs.getSelectedItem() != null) {
            if (cmbProc.getSelectedItem().toString().toLowerCase().contains("none") &&
                   cmbPros.getSelectedItem().toString().toLowerCase().contains("none") &&
                   cmbDrugs.getSelectedItem().toString().toLowerCase().contains("none") &&
                   cmbInv.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lbltarifProc.setText("0");
                lblCodeProc.setText("-");
                grouper2();
                System.out.println("1");
            }else if(cmbProc.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lbltarifProc.setText("0");
                lblCodeProc.setText("-");
                handleComboChange();
            }else{
                if (cmbProc.getSelectedIndex() > 0 && evt.getStateChange() == ItemEvent.SELECTED) {
                    handleComboChange();
                }
            }   
        }
    }//GEN-LAST:event_cmbProcItemStateChanged

    private void cmbProsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProsItemStateChanged
        // TODO add your handling code here:
        if (isLoading) return;
        if (cmbProc.getSelectedItem() != null && cmbPros.getSelectedItem() != null && cmbInv.getSelectedItem() != null && cmbDrugs.getSelectedItem() != null) {
            if (cmbProc.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbPros.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbDrugs.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbInv.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED) {
                lblTarifPros.setText("0");
                lblCodePros.setText("-");
                grouper2();
                System.out.println("2");
            }else if(cmbPros.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lblTarifPros.setText("0");
                lblCodePros.setText("-");
                handleComboChange();
            }else{
                if (cmbPros.getSelectedIndex()> 0 && evt.getStateChange() == ItemEvent.SELECTED) {
                    handleComboChange();
                }
            }   
        }
    }//GEN-LAST:event_cmbProsItemStateChanged

    private void cmbInvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInvItemStateChanged
        // TODO add your handling code here:
        if (isLoading) return;
        if (cmbProc.getSelectedItem() != null && cmbPros.getSelectedItem() != null && cmbInv.getSelectedItem() != null && cmbDrugs.getSelectedItem() != null) {
            if (cmbProc.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbPros.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbDrugs.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbInv.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lblTarifInv.setText("0");
                lblCodeInv.setText("-");
                grouper2();
                System.out.println("3");
            }else if(cmbInv.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lblTarifInv.setText("0");
                lblCodeInv.setText("-");
                handleComboChange();
            }else{
                if (cmbInv.getSelectedIndex()> 0 && evt.getStateChange() == ItemEvent.SELECTED) {
                    handleComboChange();
                }
            }    
        }
    }//GEN-LAST:event_cmbInvItemStateChanged

    private void cmbDrugsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDrugsItemStateChanged
        // TODO add your handling code here:
        if (isLoading) return;
        if (cmbProc.getSelectedItem() != null && cmbPros.getSelectedItem() != null && cmbInv.getSelectedItem() != null && cmbDrugs.getSelectedItem() != null) {
            if (cmbProc.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbPros.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbDrugs.getSelectedItem().toString().toLowerCase().contains("none") &&
                    cmbInv.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED) {
                lblTarifDrugs.setText("0");
                lblCodeDrugs.setText("-");
                grouper2();
                System.out.println("4");
            }else if(cmbDrugs.getSelectedItem().toString().toLowerCase().contains("none") && evt.getStateChange() == ItemEvent.SELECTED ) {
                lblTarifDrugs.setText("0");
                lblCodeDrugs.setText("-");
                handleComboChange();
            }else{
                if (cmbDrugs.getSelectedIndex()> 0 && evt.getStateChange() == ItemEvent.SELECTED) {
                    handleComboChange();
                }
            }
        }
    }//GEN-LAST:event_cmbDrugsItemStateChanged

    private void cmbSelisihItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSelisihItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSelisihItemStateChanged

    private void CetakKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakKlaimActionPerformed
        try {
            // Create a human-readable JSON string
            String requestJson = "{\n"
            + " \"metadata\": {\n"
            + " \"method\": \"claim_print\"\n"
            + " },\n"
            + " \"data\": {\n"
            + " \"nomor_sep\": \"" + Tsep.getText() + "\"\n"
            + " }\n"
            + "}";

            String request = requestJson;
            JSONObject response = inacbg.request(request);

            // Extract the 'data' field from the response
            if (response.has("data")) {
                String data = response.getString("data");
                try {
                    // Decode the Base64 data
                    byte[] byteArray = Base64.getDecoder().decode(data);

                    // Show print preview dialog
                    showPrintPreview(byteArray, Tsep.getText());

                } catch (Exception e) {
                    System.err.println("Error decoding Base64 data: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("response : " + response);
                System.err.println("\n The 'data' field is missing in the response.");
            }
        } catch (Exception e) {
            System.err.println("Error in cetakKlaim: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_CetakKlaimActionPerformed

   private void showPrintPreview(byte[] pdfData, String sepNumber) {
    SwingUtilities.invokeLater(() -> {
        // Perbaikan: cari parent window dengan cara yang lebih aman
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog printDialog;
        
        if (parentWindow instanceof JFrame) {
            printDialog = new JDialog((JFrame) parentWindow, "Print Preview", true);
        } else if (parentWindow instanceof JDialog) {
            printDialog = new JDialog((JDialog) parentWindow, "Print Preview", true);
        } else {
            printDialog = new JDialog((Frame) null, "Print Preview", true);
        }
        
        // Perbaikan: Atur ukuran dan posisi window
        printDialog.setSize(900, 750);
        
        // Posisikan di tengah layar, bukan relative ke parent
        printDialog.setLocationRelativeTo(null);
        
        // Atau jika ingin di tengah parent tapi lebih ke atas:
        // printDialog.setLocationRelativeTo(this);
        // Point location = printDialog.getLocation();
        // printDialog.setLocation(location.x, Math.max(50, location.y - 50));
        
        printDialog.setLayout(new BorderLayout());
        
        // Panel atas untuk kontrol print
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // Combo box untuk pilihan printer
        JLabel printerLabel = new JLabel("Printer:");
        JComboBox<String> printerCombo = new JComboBox<>();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        
        for (PrintService service : printServices) {
            printerCombo.addItem(service.getName());
            if (defaultPrinter != null && service.getName().equals(defaultPrinter.getName())) {
                printerCombo.setSelectedItem(service.getName());
            }
        }
        
        // Combo box untuk ukuran kertas
        JLabel paperLabel = new JLabel("Ukuran Kertas:");
        JComboBox<String> paperCombo = new JComboBox<>(new String[]{
            "A4 (210 x 297 mm)",
            "Letter (8.5 x 11 in)",
            "Legal (8.5 x 14 in)",
            "A5 (148 x 210 mm)"
        });
        
        controlPanel.add(printerLabel);
        controlPanel.add(printerCombo);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(paperLabel);
        controlPanel.add(paperCombo);
        
        // Panel untuk preview PDF dengan ukuran yang lebih kecil
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewPanel.setPreferredSize(new Dimension(880, 550)); // Batasi tinggi preview
        
        try {
            // Load PDF untuk preview menggunakan PDFRenderer
            PDDocument document = PDDocument.load(pdfData);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            
            // Render halaman pertama sebagai preview dengan ukuran lebih kecil
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 100);
            ImageIcon icon = new ImageIcon(image.getScaledInstance(650, -1, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            
            JScrollPane scrollPane = new JScrollPane(imageLabel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            previewPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Panel tombol dengan padding yang lebih jelas
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            JButton printButton = new JButton("Print");
            printButton.setPreferredSize(new Dimension(100, 35));
            printButton.addActionListener(e -> {
                try {
                    // Get selected printer
                    String selectedPrinterName = (String) printerCombo.getSelectedItem();
                    PrintService selectedPrinter = null;

                    for (PrintService service : printServices) {
                        if (service.getName().equals(selectedPrinterName)) {
                            selectedPrinter = service;
                            break;
                        }
                    }

                    if (selectedPrinter != null) {
                        // Setup print job
                        PrinterJob job = PrinterJob.getPrinterJob();
                        job.setPrintService(selectedPrinter);

                        // Setup paper size
                        PageFormat pageFormat = job.defaultPage();
                        Paper paper = pageFormat.getPaper();

                        String selectedPaper = (String) paperCombo.getSelectedItem();
                        if (selectedPaper.startsWith("A4")) {
                            paper.setSize(595, 842); // A4 in points
                        } else if (selectedPaper.startsWith("Letter")) {
                            paper.setSize(612, 792); // Letter in points
                        } else if (selectedPaper.startsWith("Legal")) {
                            paper.setSize(612, 1008); // Legal in points
                        } else if (selectedPaper.startsWith("A5")) {
                            paper.setSize(420, 595); // A5 in points
                        }

                        pageFormat.setPaper(paper);
                        job.setPrintable(new PDFPrintable(document), pageFormat);

                        // Tampilkan dialog print dan cek apakah user menekan OK atau Cancel
                        boolean userAccepted = job.printDialog();

                        if (userAccepted) {
                            try {
                                // Print hanya jika user menekan OK
                                job.print();
                                JOptionPane.showMessageDialog(printDialog, 
                                    "Dokumen berhasil dicetak!", 
                                    "Sukses", 
                                    JOptionPane.INFORMATION_MESSAGE);

                                document.close();
                                printDialog.dispose();

                            } catch (PrinterAbortException pae) {
                                // User membatalkan print dari dialog printer
                                System.out.println("Print dibatalkan oleh user");
                                JOptionPane.showMessageDialog(printDialog, 
                                    "Pencetakan dibatalkan", 
                                    "Informasi", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                // Jangan close document dan dialog, biarkan user bisa coba lagi
                            } catch (PrinterException pe) {
                                // Error lain saat printing
                                JOptionPane.showMessageDialog(printDialog, 
                                    "Error saat mencetak: " + pe.getMessage(), 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                                pe.printStackTrace();
                            }
                        } else {
                            // User menekan Cancel di dialog print
                            System.out.println("User membatalkan print dialog");
                        }
                    } else {
                        JOptionPane.showMessageDialog(printDialog, 
                            "Printer tidak ditemukan!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(printDialog, 
                        "Error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            JButton saveButton = new JButton("Simpan");
            saveButton.setPreferredSize(new Dimension(100, 35));
            saveButton.addActionListener(e -> {
                try {
                    // Buat file chooser untuk memilih lokasi penyimpanan
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Simpan File PDF");
                    
                    // Set default nama file
                    fileChooser.setSelectedFile(new File(sepNumber + ".pdf"));
                    
                    // Set filter untuk file PDF
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
                        }
                        
                        @Override
                        public String getDescription() {
                            return "PDF Documents (*.pdf)";
                        }
                    });
                    
                    // Set default directory ke berkasklaim
                    File defaultDir = new File("./berkasklaim/");
                    if (!defaultDir.exists()) {
                        defaultDir.mkdirs();
                    }
                    fileChooser.setCurrentDirectory(defaultDir);
                    
                    // Tampilkan dialog save
                    int userSelection = fileChooser.showSaveDialog(printDialog);
                    
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        
                        // Pastikan file berekstensi .pdf
                        if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
                        }
                        
                        // Cek jika file sudah ada
                        if (fileToSave.exists()) {
                            int confirm = JOptionPane.showConfirmDialog(
                                printDialog,
                                "File sudah ada. Apakah Anda ingin menimpa file ini?",
                                "Konfirmasi",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                            );
                            
                            if (confirm != JOptionPane.YES_OPTION) {
                                return; // Batalkan penyimpanan
                            }
                            
                            fileToSave.delete();
                        }
                        
                        // Simpan file
                        InputStream inputStream = new ByteArrayInputStream(pdfData);
                        Path path = fileToSave.toPath();
                        Files.copy(inputStream, path);
                        
                        JOptionPane.showMessageDialog(
                            printDialog,
                            "PDF berhasil disimpan di:\n" + path.toAbsolutePath(),
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        System.out.println("PDF saved successfully at " + path);
                        
                        // Tanya apakah ingin membuka file
                        int openFile = JOptionPane.showConfirmDialog(
                            printDialog,
                            "Apakah Anda ingin membuka file yang baru disimpan?",
                            "Buka File",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (openFile == JOptionPane.YES_OPTION) {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(fileToSave);
                            }
                        }
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        printDialog,
                        "Error saat menyimpan: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            });
            
            JButton cancelButton = new JButton("Batal");
            cancelButton.setPreferredSize(new Dimension(100, 35));
            cancelButton.addActionListener(e -> {
                try {
                    document.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                printDialog.dispose();
            });
            
            buttonPanel.add(printButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            printDialog.add(controlPanel, BorderLayout.NORTH);
            printDialog.add(previewPanel, BorderLayout.CENTER);
            printDialog.add(buttonPanel, BorderLayout.SOUTH);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(printDialog, 
                "Error saat memuat preview: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        printDialog.setVisible(true);
    });
}

// Helper class untuk print PDF
class PDFPrintable implements Printable {
    private PDDocument document;
    
    public PDFPrintable(PDDocument document) {
        this.document = document;
    }
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex >= document.getNumberOfPages()) {
            return NO_SUCH_PAGE;
        }
        
        try {
            Graphics2D g2d = (Graphics2D) graphics;
            
            // Set rendering hints untuk kualitas terbaik
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            // Translate ke posisi yang bisa dicetak
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            
            // Render PDF dengan DPI tinggi
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300);
            
            // Dapatkan ukuran area cetak
            double printWidth = pageFormat.getImageableWidth();
            double printHeight = pageFormat.getImageableHeight();
            
            // Dapatkan ukuran image
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            
            // Hitung scale untuk fit ke kertas dengan mempertahankan aspect ratio
            double scaleX = printWidth / imageWidth;
            double scaleY = printHeight / imageHeight;
            double scale = Math.min(scaleX, scaleY);
            
            // Jika image lebih kecil dari area cetak, jangan scale up, gunakan ukuran asli
            // Ini mencegah hasil print menjadi terlalu kecil
            if (scale > 1.0) {
                scale = 1.0;
            }
            
            // Hitung ukuran akhir setelah scaling
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);
            
            // Center image di area cetak
            int x = (int) ((printWidth - scaledWidth) / 2);
            int y = (int) ((printHeight - scaledHeight) / 2);
            
            // Draw image
            g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            
            return PAGE_EXISTS;
        } catch (Exception e) {
            e.printStackTrace();
            return NO_SUCH_PAGE;
        }
    }
}
    
    private void check_tbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_check_tbItemStateChanged
        if (check_tb.isSelected() == true) {
            pasien_tb.setVisible(true);                    
            validasi_tb.setVisible(true);
            batal_validasi_tb.setVisible(true);
            
        }else{
            pasien_tb.setVisible(false);                    
            validasi_tb.setVisible(false);
            batal_validasi_tb.setVisible(false);
             
        }
    }//GEN-LAST:event_check_tbItemStateChanged

    private void DiagnosaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyReleased
        // Auto-search saat user selesai mengetik
        String searchText = Diagnosa.getText().trim();

        // Hanya search jika ada input dan minimal 2 karakter
        if (!searchText.isEmpty() && searchText.length() >= 2) {

            // Stop timer sebelumnya jika ada
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }

            // Buat timer baru dengan delay 300ms
            searchTimer = new javax.swing.Timer(300, e -> {
                tampildiagnosa();
            });
            searchTimer.setRepeats(false); // Hanya jalankan sekali
            searchTimer.start();

        } else if (searchText.isEmpty()) {
            // Stop timer jika textbox dikosongkan
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }
        }
    }//GEN-LAST:event_DiagnosaKeyReleased

    private void DiagnosaINACBGKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaINACBGKeyReleased
        // Auto-search saat user selesai mengetik
        String searchText = DiagnosaINACBG.getText().trim();

        // Hanya search jika ada input dan minimal 2 karakter
        if (!searchText.isEmpty() && searchText.length() >= 2) {

            // Stop timer sebelumnya jika ada
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }

            // Buat timer baru dengan delay 300ms
            searchTimer = new javax.swing.Timer(300, e -> {
                tampildiagnosaINA();
            });
            searchTimer.setRepeats(false); // Hanya jalankan sekali
            searchTimer.start();

        } else if (searchText.isEmpty()) {
            // Stop timer jika textbox dikosongkan
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }
        }
    }//GEN-LAST:event_DiagnosaINACBGKeyReleased

    private void ProsedurKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurKeyReleased
        // Auto-search saat user selesai mengetik
        String searchText = Prosedur.getText().trim();

        // Hanya search jika ada input dan minimal 2 karakter
        if (!searchText.isEmpty() && searchText.length() >= 2) {

            // Stop timer sebelumnya jika ada
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }

            // Buat timer baru dengan delay 300ms
            searchTimer = new javax.swing.Timer(300, e -> {
                tampilprosedure();
            });
            searchTimer.setRepeats(false); // Hanya jalankan sekali
            searchTimer.start();

        } else if (searchText.isEmpty()) {
            // Stop timer jika textbox dikosongkan
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }
        }
    }//GEN-LAST:event_ProsedurKeyReleased

    private void ProsedurINACBGKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurINACBGKeyReleased
        // Auto-search saat user selesai mengetik
        String searchText = ProsedurINACBG.getText().trim();

        // Hanya search jika ada input dan minimal 2 karakter
        if (!searchText.isEmpty() && searchText.length() >= 2) {

            // Stop timer sebelumnya jika ada
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }

            // Buat timer baru dengan delay 300ms
            searchTimer = new javax.swing.Timer(300, e -> {
                tampilprosedureINA();
            });
            searchTimer.setRepeats(false); // Hanya jalankan sekali
            searchTimer.start();

        } else if (searchText.isEmpty()) {
            // Stop timer jika textbox dikosongkan
            if (searchTimer != null && searchTimer.isRunning()) {
                searchTimer.stop();
            }
        }
    }//GEN-LAST:event_ProsedurINACBGKeyReleased

    private void ApgarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ApgarItemStateChanged
        if (Apgar.isSelected() == true) {
            showApgarComponents(true);
        } else {
            showApgarComponents(false);
        }
    }//GEN-LAST:event_ApgarItemStateChanged

    private void KehamilanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_KehamilanItemStateChanged
        if (Kehamilan.isSelected() == true) {
            showKehamilanComponents(true);
        } else {
            showKehamilanComponents(false);
        }
    }//GEN-LAST:event_KehamilanItemStateChanged

    private void batal_psdr_21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_21ActionPerformed
        kd_psdr_21.setText("");
        nm_psdr_21.setText("");
        multiple11.setText("");
    }//GEN-LAST:event_batal_psdr_21ActionPerformed

    private void batal_psdr_22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_22ActionPerformed
        kd_psdr_22.setText("");
        nm_psdr_22.setText("");
        multiple12.setText("");
    }//GEN-LAST:event_batal_psdr_22ActionPerformed

    private void batal_psdr_23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_23ActionPerformed
        kd_psdr_23.setText("");
        nm_psdr_23.setText("");
        multiple13.setText("");
    }//GEN-LAST:event_batal_psdr_23ActionPerformed

    private void batal_psdr_24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_24ActionPerformed
        kd_psdr_24.setText("");
        nm_psdr_24.setText("");
        multiple14.setText("");
    }//GEN-LAST:event_batal_psdr_24ActionPerformed

    private void batal_psdr_25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_25ActionPerformed
        kd_psdr_25.setText("");
        nm_psdr_25.setText("");
        multiple15.setText("");
    }//GEN-LAST:event_batal_psdr_25ActionPerformed

    private void batal_psdr_26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_26ActionPerformed
        kd_psdr_26.setText("");
        nm_psdr_26.setText("");
        multiple16.setText("");
    }//GEN-LAST:event_batal_psdr_26ActionPerformed

    private void batal_psdr_27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_27ActionPerformed
        kd_psdr_27.setText("");
        nm_psdr_27.setText("");
        multiple17.setText("");
    }//GEN-LAST:event_batal_psdr_27ActionPerformed

    private void batal_psdr_28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_28ActionPerformed
        kd_psdr_28.setText("");
        nm_psdr_28.setText("");
        multiple18.setText("");
    }//GEN-LAST:event_batal_psdr_28ActionPerformed

    private void batal_psdr_29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_29ActionPerformed
        kd_psdr_29.setText("");
        nm_psdr_29.setText("");
        multiple19.setText("");
    }//GEN-LAST:event_batal_psdr_29ActionPerformed

    private void batal_psdr_30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_30ActionPerformed
        kd_psdr_30.setText("");
        nm_psdr_30.setText("");
        multiple20.setText("");
    }//GEN-LAST:event_batal_psdr_30ActionPerformed

    private void batal_psdr_36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_36ActionPerformed
        kd_psdr_36.setText("");
        nm_psdr_36.setText("");
    }//GEN-LAST:event_batal_psdr_36ActionPerformed

    private void batal_psdr_37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_37ActionPerformed
        kd_psdr_37.setText("");
        nm_psdr_37.setText("");
    }//GEN-LAST:event_batal_psdr_37ActionPerformed

    private void batal_psdr_38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_38ActionPerformed
        kd_psdr_38.setText("");
        nm_psdr_38.setText("");
    }//GEN-LAST:event_batal_psdr_38ActionPerformed

    private void batal_psdr_39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_39ActionPerformed
        kd_psdr_39.setText("");
        nm_psdr_39.setText("");
    }//GEN-LAST:event_batal_psdr_39ActionPerformed

    private void batal_psdr_40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_40ActionPerformed
        kd_psdr_40.setText("");
        nm_psdr_40.setText("");
    }//GEN-LAST:event_batal_psdr_40ActionPerformed

    private void batal_psdr_41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_41ActionPerformed
        kd_psdr_41.setText("");
        nm_psdr_41.setText("");
    }//GEN-LAST:event_batal_psdr_41ActionPerformed

    private void batal_psdr_42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_42ActionPerformed
        kd_psdr_42.setText("");
        nm_psdr_42.setText("");
    }//GEN-LAST:event_batal_psdr_42ActionPerformed

    private void batal_psdr_43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_43ActionPerformed
        kd_psdr_43.setText("");
        nm_psdr_43.setText("");
    }//GEN-LAST:event_batal_psdr_43ActionPerformed

    private void batal_psdr_44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_44ActionPerformed
        kd_psdr_44.setText("");
        nm_psdr_44.setText("");
    }//GEN-LAST:event_batal_psdr_44ActionPerformed

    private void batal_psdr_45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_45ActionPerformed
        kd_psdr_45.setText("");
        nm_psdr_45.setText("");
    }//GEN-LAST:event_batal_psdr_45ActionPerformed

    private void nm_diag_25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_diag_25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_diag_25ActionPerformed

    private void chk_diag_21ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_21ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_21ItemStateChanged

    private void chk_diag_21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_diag_21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_21ActionPerformed

    private void chk_diag_22ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_22ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_22ItemStateChanged

    private void chk_diag_22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_diag_22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_22ActionPerformed

    private void chk_diag_23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_23ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_23ItemStateChanged

    private void chk_diag_24ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_24ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_24ItemStateChanged

    private void chk_diag_25ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_25ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_25ItemStateChanged

    private void chk_diag_26ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_26ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_26ItemStateChanged

    private void chk_diag_27ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_27ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_27ItemStateChanged

    private void chk_diag_28ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_28ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_28ItemStateChanged

    private void chk_diag_29ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_29ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_29ItemStateChanged

    private void chk_diag_30ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_30ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_30ItemStateChanged

    private void batal_21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_21ActionPerformed
        kd_diag_21.setText("");
        nm_diag_21.setText("");
        chk_diag_21.setSelected(false);
    }//GEN-LAST:event_batal_21ActionPerformed

    private void batal_22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_22ActionPerformed
        kd_diag_22.setText("");
        nm_diag_22.setText("");
        chk_diag_22.setSelected(false);
    }//GEN-LAST:event_batal_22ActionPerformed

    private void batal_23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_23ActionPerformed
        kd_diag_23.setText("");
        nm_diag_23.setText("");
        chk_diag_23.setSelected(false);
    }//GEN-LAST:event_batal_23ActionPerformed

    private void batal_24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_24ActionPerformed
        kd_diag_24.setText("");
        nm_diag_24.setText("");
        chk_diag_24.setSelected(false);
    }//GEN-LAST:event_batal_24ActionPerformed

    private void batal_25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_25ActionPerformed
        kd_diag_25.setText("");
        nm_diag_25.setText("");
        chk_diag_25.setSelected(false);
    }//GEN-LAST:event_batal_25ActionPerformed

    private void batal_26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_26ActionPerformed
        kd_diag_26.setText("");
        nm_diag_26.setText("");
        chk_diag_26.setSelected(false);
    }//GEN-LAST:event_batal_26ActionPerformed

    private void batal_27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_27ActionPerformed
        kd_diag_27.setText("");
        nm_diag_27.setText("");
        chk_diag_27.setSelected(false);
    }//GEN-LAST:event_batal_27ActionPerformed

    private void batal_28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_28ActionPerformed
        kd_diag_28.setText("");
        nm_diag_28.setText("");
        chk_diag_28.setSelected(false);
    }//GEN-LAST:event_batal_28ActionPerformed

    private void batal_29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_29ActionPerformed
        kd_diag_29.setText("");
        nm_diag_29.setText("");
        chk_diag_29.setSelected(false);
    }//GEN-LAST:event_batal_29ActionPerformed

    private void batal_30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_30ActionPerformed
        kd_diag_30.setText("");
        nm_diag_30.setText("");
        chk_diag_30.setSelected(false);
    }//GEN-LAST:event_batal_30ActionPerformed

    private void nm_diag_35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_diag_35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_diag_35ActionPerformed

    private void chk_diag_31ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_31ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_31ItemStateChanged

    private void chk_diag_32ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_32ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_32ItemStateChanged

    private void chk_diag_32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chk_diag_32MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_32MouseClicked

    private void chk_diag_33ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_33ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_33ItemStateChanged

    private void chk_diag_34ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_34ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_34ItemStateChanged

    private void chk_diag_35ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_35ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_35ItemStateChanged

    private void chk_diag_36ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_36ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_36ItemStateChanged

    private void chk_diag_37ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_37ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_37ItemStateChanged

    private void chk_diag_38ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_38ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_38ItemStateChanged

    private void chk_diag_39ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_39ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_39ItemStateChanged

    private void chk_diag_40ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_40ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_40ItemStateChanged

    private void batal_31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_31ActionPerformed
        kd_diag_31.setText("");
        nm_diag_31.setText("");
        chk_diag_31.setSelected(false);
    }//GEN-LAST:event_batal_31ActionPerformed

    private void batal_32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_32ActionPerformed
        kd_diag_32.setText("");
        nm_diag_32.setText("");
        chk_diag_32.setSelected(false);
    }//GEN-LAST:event_batal_32ActionPerformed

    private void batal_33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_33ActionPerformed
        kd_diag_33.setText("");
        nm_diag_33.setText("");
        chk_diag_33.setSelected(false);
    }//GEN-LAST:event_batal_33ActionPerformed

    private void batal_34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_34ActionPerformed
        kd_diag_34.setText("");
        nm_diag_34.setText("");
        chk_diag_34.setSelected(false);
    }//GEN-LAST:event_batal_34ActionPerformed

    private void batal_35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_35ActionPerformed
        kd_diag_35.setText("");
        nm_diag_35.setText("");
        chk_diag_35.setSelected(false);
    }//GEN-LAST:event_batal_35ActionPerformed

    private void batal_36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_36ActionPerformed
        kd_diag_36.setText("");
        nm_diag_36.setText("");
        chk_diag_36.setSelected(false);
    }//GEN-LAST:event_batal_36ActionPerformed

    private void batal_37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_37ActionPerformed
        kd_diag_37.setText("");
        nm_diag_37.setText("");
        chk_diag_37.setSelected(false);
    }//GEN-LAST:event_batal_37ActionPerformed

    private void batal_38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_38ActionPerformed
        kd_diag_38.setText("");
        nm_diag_38.setText("");
        chk_diag_38.setSelected(false);
    }//GEN-LAST:event_batal_38ActionPerformed

    private void batal_39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_39ActionPerformed
        kd_diag_39.setText("");
        nm_diag_39.setText("");
        chk_diag_39.setSelected(false);
    }//GEN-LAST:event_batal_39ActionPerformed

    private void batal_40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_40ActionPerformed
        kd_diag_40.setText("");
        nm_diag_40.setText("");
        chk_diag_40.setSelected(false);
    }//GEN-LAST:event_batal_40ActionPerformed

    private void chk_diag_41ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_41ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_41ItemStateChanged

    private void batal_41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_41ActionPerformed
        kd_diag_41.setText("");
        nm_diag_41.setText("");
        chk_diag_41.setSelected(false);
    }//GEN-LAST:event_batal_41ActionPerformed

    private void batal_psdr_31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_31ActionPerformed
        kd_psdr_31.setText("");
        nm_psdr_31.setText("");
        multiple21.setText("");
    }//GEN-LAST:event_batal_psdr_31ActionPerformed

    private void chk_diag_42ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_42ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chk_diag_42ItemStateChanged

    private void batal_42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_42ActionPerformed
        kd_diag_42.setText("");
        nm_diag_42.setText("");
        chk_diag_42.setSelected(false);
    }//GEN-LAST:event_batal_42ActionPerformed

    private void batal_psdr_46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_psdr_46ActionPerformed
        kd_psdr_46.setText("");
        nm_psdr_46.setText("");
    }//GEN-LAST:event_batal_psdr_46ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            INACBGDataIDRG dialog = new INACBGDataIDRG(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                } 
           });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox Abortus;
    private widget.TextBox Activity1;
    private widget.TextBox Activity5;
    private javax.swing.JCheckBox Apgar;
    private widget.TextBox Appear1;
    private widget.TextBox Appear5;
    private widget.Button BtnGrouperIDRG;
    private widget.Button BtnGrouperINA;
    private widget.Button BtnKeluar4;
    private widget.Button CetakKlaim;
    public widget.TextBox Diagnosa;
    public widget.TextBox DiagnosaINACBG;
    private widget.panelisi FormDATAKLAIM;
    private widget.panelisi FormIDRG;
    private widget.panelisi FormIDRG1;
    private widget.panelisi FormINACBG;
    private widget.TextBox Gravida;
    private widget.TextBox Grimace1;
    private widget.TextBox Grimace5;
    private widget.Button INAdiagnosa;
    public widget.Button INAdiagnosatambah;
    private widget.Button INAprosedur;
    public widget.Button INAprosedurtambah;
    private widget.Button Import;
    private javax.swing.JCheckBox Kehamilan;
    private widget.ComboBox Kontraksi;
    private javax.swing.JPanel Panel1;
    private widget.TextBox Partus;
    private widget.TextBox Prosedur;
    private widget.TextBox ProsedurINACBG;
    private widget.TextBox Pulse1;
    private widget.TextBox Pulse5;
    private widget.TextBox Respiration1;
    private widget.TextBox Respiration5;
    private widget.ScrollPane Scroll4;
    private widget.ScrollPane Scroll5;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.TextBox TNoRM;
    private widget.TextBox TPasien;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox Tsep;
    private widget.TextArea Txtgroup;
    private widget.TextBox UsiaKehamilan;
    private widget.TextBox ahli;
    private widget.TextBox akomodasi;
    private widget.TextBox alkes;
    private widget.Button batal_1;
    private widget.Button batal_10;
    private widget.Button batal_11;
    private widget.Button batal_12;
    private widget.Button batal_13;
    private widget.Button batal_14;
    private widget.Button batal_15;
    private widget.Button batal_16;
    private widget.Button batal_17;
    private widget.Button batal_18;
    private widget.Button batal_19;
    private widget.Button batal_2;
    private widget.Button batal_20;
    private widget.Button batal_21;
    private widget.Button batal_22;
    private widget.Button batal_23;
    private widget.Button batal_24;
    private widget.Button batal_25;
    private widget.Button batal_26;
    private widget.Button batal_27;
    private widget.Button batal_28;
    private widget.Button batal_29;
    private widget.Button batal_3;
    private widget.Button batal_30;
    private widget.Button batal_31;
    private widget.Button batal_32;
    private widget.Button batal_33;
    private widget.Button batal_34;
    private widget.Button batal_35;
    private widget.Button batal_36;
    private widget.Button batal_37;
    private widget.Button batal_38;
    private widget.Button batal_39;
    private widget.Button batal_4;
    private widget.Button batal_40;
    private widget.Button batal_41;
    private widget.Button batal_42;
    private widget.Button batal_5;
    private widget.Button batal_6;
    private widget.Button batal_7;
    private widget.Button batal_8;
    private widget.Button batal_9;
    private widget.Button batal_psdr_1;
    private widget.Button batal_psdr_10;
    private widget.Button batal_psdr_11;
    private widget.Button batal_psdr_12;
    private widget.Button batal_psdr_13;
    private widget.Button batal_psdr_14;
    private widget.Button batal_psdr_15;
    private widget.Button batal_psdr_16;
    private widget.Button batal_psdr_17;
    private widget.Button batal_psdr_18;
    private widget.Button batal_psdr_19;
    private widget.Button batal_psdr_2;
    private widget.Button batal_psdr_20;
    private widget.Button batal_psdr_21;
    private widget.Button batal_psdr_22;
    private widget.Button batal_psdr_23;
    private widget.Button batal_psdr_24;
    private widget.Button batal_psdr_25;
    private widget.Button batal_psdr_26;
    private widget.Button batal_psdr_27;
    private widget.Button batal_psdr_28;
    private widget.Button batal_psdr_29;
    private widget.Button batal_psdr_3;
    private widget.Button batal_psdr_30;
    private widget.Button batal_psdr_31;
    private widget.Button batal_psdr_36;
    private widget.Button batal_psdr_37;
    private widget.Button batal_psdr_38;
    private widget.Button batal_psdr_39;
    private widget.Button batal_psdr_4;
    private widget.Button batal_psdr_40;
    private widget.Button batal_psdr_41;
    private widget.Button batal_psdr_42;
    private widget.Button batal_psdr_43;
    private widget.Button batal_psdr_44;
    private widget.Button batal_psdr_45;
    private widget.Button batal_psdr_46;
    private widget.Button batal_psdr_5;
    private widget.Button batal_psdr_6;
    private widget.Button batal_psdr_7;
    private widget.Button batal_psdr_8;
    private widget.Button batal_psdr_9;
    private widget.Button batal_validasi_tb;
    private widget.TextBox berat_bayi;
    private widget.TextBox bmhp;
    private widget.ComboBox cara_masuk;
    private javax.swing.JCheckBox check_hd;
    private javax.swing.JCheckBox check_tb;
    private javax.swing.JCheckBox chk_diag_1;
    private javax.swing.JCheckBox chk_diag_10;
    private javax.swing.JCheckBox chk_diag_11;
    private javax.swing.JCheckBox chk_diag_12;
    private javax.swing.JCheckBox chk_diag_13;
    private javax.swing.JCheckBox chk_diag_14;
    private javax.swing.JCheckBox chk_diag_15;
    private javax.swing.JCheckBox chk_diag_16;
    private javax.swing.JCheckBox chk_diag_17;
    private javax.swing.JCheckBox chk_diag_18;
    private javax.swing.JCheckBox chk_diag_19;
    private javax.swing.JCheckBox chk_diag_2;
    private javax.swing.JCheckBox chk_diag_20;
    private javax.swing.JCheckBox chk_diag_21;
    private javax.swing.JCheckBox chk_diag_22;
    private javax.swing.JCheckBox chk_diag_23;
    private javax.swing.JCheckBox chk_diag_24;
    private javax.swing.JCheckBox chk_diag_25;
    private javax.swing.JCheckBox chk_diag_26;
    private javax.swing.JCheckBox chk_diag_27;
    private javax.swing.JCheckBox chk_diag_28;
    private javax.swing.JCheckBox chk_diag_29;
    private javax.swing.JCheckBox chk_diag_3;
    private javax.swing.JCheckBox chk_diag_30;
    private javax.swing.JCheckBox chk_diag_31;
    private javax.swing.JCheckBox chk_diag_32;
    private javax.swing.JCheckBox chk_diag_33;
    private javax.swing.JCheckBox chk_diag_34;
    private javax.swing.JCheckBox chk_diag_35;
    private javax.swing.JCheckBox chk_diag_36;
    private javax.swing.JCheckBox chk_diag_37;
    private javax.swing.JCheckBox chk_diag_38;
    private javax.swing.JCheckBox chk_diag_39;
    private javax.swing.JCheckBox chk_diag_4;
    private javax.swing.JCheckBox chk_diag_40;
    private javax.swing.JCheckBox chk_diag_41;
    private javax.swing.JCheckBox chk_diag_42;
    private javax.swing.JCheckBox chk_diag_5;
    private javax.swing.JCheckBox chk_diag_6;
    private javax.swing.JCheckBox chk_diag_7;
    private javax.swing.JCheckBox chk_diag_8;
    private javax.swing.JCheckBox chk_diag_9;
    private widget.TextBox chronic;
    private widget.ComboBox cmbDrugs;
    private widget.ComboBox cmbInv;
    private widget.ComboBox cmbProc;
    private widget.ComboBox cmbPros;
    private widget.ComboBox cmbSelisih;
    private widget.ComboBox cob;
    private widget.TextBox distole;
    private widget.TextBox dpjp;
    private widget.Button editINA;
    private widget.Button editKlaim;
    private widget.Button editKlaimIDRG;
    private widget.Button finalINA;
    private widget.Button finalklaim;
    private widget.Button finalklaimIDRG;
    private widget.Label grupCode;
    private widget.ComboBox hak_kelas;
    private widget.Button hapusKlaim4;
    private widget.TextBox hari_intensif;
    private widget.Button idrgPenyakit;
    public widget.Button idrgPenyakittambah;
    private widget.Button idrgProsedur;
    public widget.Button idrgProseduttambah;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel129;
    private widget.Label jLabel183;
    private widget.Label jLabel216;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel237;
    private widget.Label jLabel238;
    private widget.Label jLabel239;
    private widget.Label jLabel240;
    private widget.Label jLabel241;
    private widget.Label jLabel242;
    private widget.Label jLabel25;
    private widget.Label jLabel49;
    private widget.Label jLabel58;
    private widget.Label jLabel61;
    private widget.Label jLabel66;
    private widget.Label jLabel68;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private widget.ComboBox jam_ekstubasi;
    private widget.ComboBox jam_intubasi;
    private widget.ComboBox jam_keluar;
    private widget.ComboBox jam_masuk;
    private widget.TextBox jam_ventilator;
    private widget.ComboBox jaminan;
    private widget.TextBox jenis_kelamin;
    private widget.ComboBox jenis_rawat;
    private widget.TextBox kd_diag_1;
    private widget.TextBox kd_diag_10;
    private widget.TextBox kd_diag_11;
    private widget.TextBox kd_diag_12;
    private widget.TextBox kd_diag_13;
    private widget.TextBox kd_diag_14;
    private widget.TextBox kd_diag_15;
    private widget.TextBox kd_diag_16;
    private widget.TextBox kd_diag_17;
    private widget.TextBox kd_diag_18;
    private widget.TextBox kd_diag_19;
    private widget.TextBox kd_diag_2;
    private widget.TextBox kd_diag_20;
    private widget.TextBox kd_diag_21;
    private widget.TextBox kd_diag_22;
    private widget.TextBox kd_diag_23;
    private widget.TextBox kd_diag_24;
    private widget.TextBox kd_diag_25;
    private widget.TextBox kd_diag_26;
    private widget.TextBox kd_diag_27;
    private widget.TextBox kd_diag_28;
    private widget.TextBox kd_diag_29;
    private widget.TextBox kd_diag_3;
    private widget.TextBox kd_diag_30;
    private widget.TextBox kd_diag_31;
    private widget.TextBox kd_diag_32;
    private widget.TextBox kd_diag_33;
    private widget.TextBox kd_diag_34;
    private widget.TextBox kd_diag_35;
    private widget.TextBox kd_diag_36;
    private widget.TextBox kd_diag_37;
    private widget.TextBox kd_diag_38;
    private widget.TextBox kd_diag_39;
    private widget.TextBox kd_diag_4;
    private widget.TextBox kd_diag_40;
    private widget.TextBox kd_diag_41;
    private widget.TextBox kd_diag_42;
    private widget.TextBox kd_diag_5;
    private widget.TextBox kd_diag_6;
    private widget.TextBox kd_diag_7;
    private widget.TextBox kd_diag_8;
    private widget.TextBox kd_diag_9;
    private widget.TextBox kd_psdr_1;
    private widget.TextBox kd_psdr_10;
    private widget.TextBox kd_psdr_11;
    private widget.TextBox kd_psdr_12;
    private widget.TextBox kd_psdr_13;
    private widget.TextBox kd_psdr_14;
    private widget.TextBox kd_psdr_15;
    private widget.TextBox kd_psdr_16;
    private widget.TextBox kd_psdr_17;
    private widget.TextBox kd_psdr_18;
    private widget.TextBox kd_psdr_19;
    private widget.TextBox kd_psdr_2;
    private widget.TextBox kd_psdr_20;
    private widget.TextBox kd_psdr_21;
    private widget.TextBox kd_psdr_22;
    private widget.TextBox kd_psdr_23;
    private widget.TextBox kd_psdr_24;
    private widget.TextBox kd_psdr_25;
    private widget.TextBox kd_psdr_26;
    private widget.TextBox kd_psdr_27;
    private widget.TextBox kd_psdr_28;
    private widget.TextBox kd_psdr_29;
    private widget.TextBox kd_psdr_3;
    private widget.TextBox kd_psdr_30;
    private widget.TextBox kd_psdr_31;
    private widget.TextBox kd_psdr_36;
    private widget.TextBox kd_psdr_37;
    private widget.TextBox kd_psdr_38;
    private widget.TextBox kd_psdr_39;
    private widget.TextBox kd_psdr_4;
    private widget.TextBox kd_psdr_40;
    private widget.TextBox kd_psdr_41;
    private widget.TextBox kd_psdr_42;
    private widget.TextBox kd_psdr_43;
    private widget.TextBox kd_psdr_44;
    private widget.TextBox kd_psdr_45;
    private widget.TextBox kd_psdr_46;
    private widget.TextBox kd_psdr_5;
    private widget.TextBox kd_psdr_6;
    private widget.TextBox kd_psdr_7;
    private widget.TextBox kd_psdr_8;
    private widget.TextBox kd_psdr_9;
    private javax.swing.JCheckBox kelas_eksekutif;
    private widget.Button kirimonline;
    private widget.TextBox kpr;
    private widget.TextBox kronis;
    private widget.TextBox ksl;
    private widget.TextBox kt_darah;
    private widget.Label lChronic;
    private widget.Label lJenis1;
    private widget.Label lSubacute;
    private widget.TextBox lab;
    private widget.Label label1;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label2;
    private widget.Label label3;
    private widget.Label label4;
    private widget.Label label5;
    private widget.Label label7;
    private widget.Label label8;
    private widget.TextBox lama_naik_kelas;
    private widget.TextBox lamaharirawat;
    private widget.TextBox lamajamrawat;
    private widget.Label lblBaseTariff;
    private widget.Label lblCodeDrugs;
    private widget.Label lblCodeInv;
    private widget.Label lblCodeProc;
    private widget.Label lblCodePros;
    private widget.Label lblSelisih;
    private widget.Label lblStatusEncounter;
    private widget.Label lblStatusKlaim;
    private widget.Label lblTarifAcute;
    private widget.Label lblTarifDrugs;
    private widget.Label lblTarifGrup;
    private widget.Label lblTarifInv;
    private widget.Label lblTarifKronis;
    private widget.Label lblTarifPros;
    private widget.Label lblTarifRS;
    private widget.Label lblTarifTotal;
    private widget.Label lbltarifProc;
    private widget.Label lbltarifrs;
    private widget.ComboBox menit_ekstubasi;
    private widget.ComboBox menit_intubasi;
    private widget.ComboBox menit_keluar;
    private widget.ComboBox menit_masuk;
    private widget.TextBox multiple1;
    private widget.TextBox multiple10;
    private widget.TextBox multiple11;
    private widget.TextBox multiple12;
    private widget.TextBox multiple13;
    private widget.TextBox multiple14;
    private widget.TextBox multiple15;
    private widget.TextBox multiple16;
    private widget.TextBox multiple17;
    private widget.TextBox multiple18;
    private widget.TextBox multiple19;
    private widget.TextBox multiple2;
    private widget.TextBox multiple20;
    private widget.TextBox multiple21;
    private widget.TextBox multiple3;
    private widget.TextBox multiple4;
    private widget.TextBox multiple5;
    private widget.TextBox multiple6;
    private widget.TextBox multiple7;
    private widget.TextBox multiple8;
    private widget.TextBox multiple9;
    private widget.ComboBox naik_kelas_pelayanan;
    private javax.swing.JCheckBox naik_turun_kelas;
    private widget.TextBox nm_diag_1;
    private widget.TextBox nm_diag_10;
    private widget.TextBox nm_diag_11;
    private widget.TextBox nm_diag_12;
    private widget.TextBox nm_diag_13;
    private widget.TextBox nm_diag_14;
    private widget.TextBox nm_diag_15;
    private widget.TextBox nm_diag_16;
    private widget.TextBox nm_diag_17;
    private widget.TextBox nm_diag_18;
    private widget.TextBox nm_diag_19;
    private widget.TextBox nm_diag_2;
    private widget.TextBox nm_diag_20;
    private widget.TextBox nm_diag_21;
    private widget.TextBox nm_diag_22;
    private widget.TextBox nm_diag_23;
    private widget.TextBox nm_diag_24;
    private widget.TextBox nm_diag_25;
    private widget.TextBox nm_diag_26;
    private widget.TextBox nm_diag_27;
    private widget.TextBox nm_diag_28;
    private widget.TextBox nm_diag_29;
    private widget.TextBox nm_diag_3;
    private widget.TextBox nm_diag_30;
    private widget.TextBox nm_diag_31;
    private widget.TextBox nm_diag_32;
    private widget.TextBox nm_diag_33;
    private widget.TextBox nm_diag_34;
    private widget.TextBox nm_diag_35;
    private widget.TextBox nm_diag_36;
    private widget.TextBox nm_diag_37;
    private widget.TextBox nm_diag_38;
    private widget.TextBox nm_diag_39;
    private widget.TextBox nm_diag_4;
    private widget.TextBox nm_diag_40;
    private widget.TextBox nm_diag_41;
    private widget.TextBox nm_diag_42;
    private widget.TextBox nm_diag_5;
    private widget.TextBox nm_diag_6;
    private widget.TextBox nm_diag_7;
    private widget.TextBox nm_diag_8;
    private widget.TextBox nm_diag_9;
    private widget.TextBox nm_psdr_1;
    private widget.TextBox nm_psdr_10;
    private widget.TextBox nm_psdr_11;
    private widget.TextBox nm_psdr_12;
    private widget.TextBox nm_psdr_13;
    private widget.TextBox nm_psdr_14;
    private widget.TextBox nm_psdr_15;
    private widget.TextBox nm_psdr_16;
    private widget.TextBox nm_psdr_17;
    private widget.TextBox nm_psdr_18;
    private widget.TextBox nm_psdr_19;
    private widget.TextBox nm_psdr_2;
    private widget.TextBox nm_psdr_20;
    private widget.TextBox nm_psdr_21;
    private widget.TextBox nm_psdr_22;
    private widget.TextBox nm_psdr_23;
    private widget.TextBox nm_psdr_24;
    private widget.TextBox nm_psdr_25;
    private widget.TextBox nm_psdr_26;
    private widget.TextBox nm_psdr_27;
    private widget.TextBox nm_psdr_28;
    private widget.TextBox nm_psdr_29;
    private widget.TextBox nm_psdr_3;
    private widget.TextBox nm_psdr_30;
    private widget.TextBox nm_psdr_31;
    private widget.TextBox nm_psdr_36;
    private widget.TextBox nm_psdr_37;
    private widget.TextBox nm_psdr_38;
    private widget.TextBox nm_psdr_39;
    private widget.TextBox nm_psdr_4;
    private widget.TextBox nm_psdr_40;
    private widget.TextBox nm_psdr_41;
    private widget.TextBox nm_psdr_42;
    private widget.TextBox nm_psdr_43;
    private widget.TextBox nm_psdr_44;
    private widget.TextBox nm_psdr_45;
    private widget.TextBox nm_psdr_46;
    private widget.TextBox nm_psdr_5;
    private widget.TextBox nm_psdr_6;
    private widget.TextBox nm_psdr_7;
    private widget.TextBox nm_psdr_8;
    private widget.TextBox nm_psdr_9;
    private widget.TextBox no_kartu;
    private widget.TextBox no_rawat;
    private widget.TextBox obat;
    private widget.TextBox obat_kemo;
    private widget.panelisi panelBawah;
    private widget.panelisi panelGlass9;
    private widget.ComboBox pasien_hd;
    private widget.TextBox pasien_tb;
    private widget.TextBox pb;
    private widget.TextBox pl_darah;
    private widget.TextBox pnb;
    private widget.TextBox pnj;
    private widget.TextBox rad;
    private javax.swing.JCheckBox rawat_intensif;
    private widget.TextBox rehab;
    private widget.Label responIDRG;
    private widget.TextBox ri;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollRespon1;
    private widget.TextBox sewa_alat;
    private widget.TextBox sistole;
    private widget.ComboBox status_pulang;
    private widget.TextBox sub_acute;
    private widget.Label tarifRS;
    private widget.TextBox tarif_eksekutif;
    public widget.Table tbDiagnosa;
    public widget.Table tbDiagnosa2;
    public widget.Table tbProsedur;
    public widget.Table tbProsedur2;
    private widget.Tanggal tgl_ekstubasi;
    private widget.Tanggal tgl_intubasi;
    private widget.Tanggal tgl_keluar;
    private widget.TextBox tgl_lahir;
    private widget.Tanggal tgl_masuk;
    private widget.Button validasi_tb;
    private javax.swing.JCheckBox ventilator;
    // End of variables declaration//GEN-END:variables

    public void emptTeks() {
        TNoRM.setText("");
        TPasien.setText("");
        Tsep.setText("");
        tgl_masuk.setDate(new Date());
        tgl_masuk.requestFocus();
    }
    
    // Method helper untuk reset fields APGAR
    private void resetApgarFields() {
        berat_bayi.setText("0");

        // Reset APGAR Menit ke-1
        Pulse1.setText("0");        // f1 - Frekuensi Jantung
        Respiration1.setText("0");  // u1 - Usaha Nafas
        Activity1.setText("0");     // t1 - Tonus Otot
        Grimace1.setText("0");      // r1 - Refleks
        Appear1.setText("0");       // w1 - Warna

        // Reset APGAR Menit ke-5
        Pulse5.setText("0");        // f5 - Frekuensi Jantung
        Respiration5.setText("0");  // u5 - Usaha Nafas
        Activity5.setText("0");     // t5 - Tonus Otot
        Grimace5.setText("0");      // r5 - Refleks
        Appear5.setText("0");       // w5 - Warna
    }
    
    // Method untuk cek dan auto-enable APGAR jika bayi baru lahir
    private void checkAndEnableApgar() {
        try {
            // Validasi tanggal masuk dan tanggal lahir sudah terisi
            if (tgl_masuk.getDate() == null || tgl_lahir.getText().trim().equals("")) {
                System.out.println("DEBUG: Tanggal masuk atau tanggal lahir kosong");
                return;
            }

            // Ambil tanggal masuk
            Date tanggalMasuk = tgl_masuk.getDate();

            // Parse tanggal lahir dari textbox (format: yyyy-MM-dd)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date tanggalLahir = sdf.parse(tgl_lahir.getText().trim());

            // Format untuk perbandingan (hanya tanggal, tanpa jam)
            String strTglMasuk = sdf.format(tanggalMasuk);
            String strTglLahir = sdf.format(tanggalLahir);

            // Debug info
            System.out.println("DEBUG APGAR CHECK:");
            System.out.println("Tanggal Masuk  : " + strTglMasuk);
            System.out.println("Tanggal Lahir  : " + strTglLahir);
            System.out.println("Sama?          : " + strTglMasuk.equals(strTglLahir));

            // Cek apakah tanggal masuk sama dengan tanggal lahir
            if (strTglMasuk.equals(strTglLahir)) {
                // Otomatis cara masuk lahir di RS
                cara_masuk.setSelectedIndex(6);
                // Otomatis centang checkbox Apgar                
                Apgar.setSelected(true);

                // Tampilkan komponen APGAR Score
                showApgarComponents(true);

                // Load data APGAR dari database
                loadApgarData();

                System.out.println("✓ Pasien baru lahir terdeteksi. APGAR Score diaktifkan.");

                // Optional: Tampilkan notifikasi ke user
                // JOptionPane.showMessageDialog(null, "Pasien baru lahir terdeteksi.\nAPGAR Score otomatis diaktifkan.");
            } else {
                // Jika bukan bayi baru lahir, non-aktifkan APGAR
                Apgar.setSelected(false);
                showApgarComponents(false);
                System.out.println("✗ Bukan bayi baru lahir. APGAR Score tidak diaktifkan.");
            }

        } catch (ParseException e) {
            System.out.println("Error parsing tanggal lahir: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error checkAndEnableApgar: " + e);
            e.printStackTrace();
        }
    }
    
    // Method untuk load data APGAR Score dari database
    private void loadApgarData() {
        // Validasi no rekam medis
        if (TNoRM.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "No. Rekam Medis tidak boleh kosong!");
            return;
        }

        String noRM = TNoRM.getText().trim();

        try {
            // Query untuk mengambil data APGAR Score
            String sql = "SELECT berat_badan, f1, u1, t1, r1, w1, f5, u5, t5, r5, w5 " +
                        "FROM pasien_bayi WHERE TRIM(no_rkm_medis) = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRM);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Isi data berat bayi
                berat_bayi.setText(rs.getString("berat_badan") != null ? rs.getString("berat_badan") : "0");

                // Isi data APGAR Score Menit ke-1
                // f = frekuensi jantung (Pulse), u = usaha nafas (Respiration), 
                // t = tonus otot (Activity), r = refleks (Grimace), w = warna (Appear)
                Pulse1.setText(rs.getString("f1") != null ? rs.getString("f1") : "0");
                Respiration1.setText(rs.getString("u1") != null ? rs.getString("u1") : "0");
                Activity1.setText(rs.getString("t1") != null ? rs.getString("t1") : "0");
                Grimace1.setText(rs.getString("r1") != null ? rs.getString("r1") : "0");
                Appear1.setText(rs.getString("w1") != null ? rs.getString("w1") : "0");

                // Isi data APGAR Score Menit ke-5
                Pulse5.setText(rs.getString("f5") != null ? rs.getString("f5") : "0");
                Respiration5.setText(rs.getString("u5") != null ? rs.getString("u5") : "0");
                Activity5.setText(rs.getString("t5") != null ? rs.getString("t5") : "0");
                Grimace5.setText(rs.getString("r5") != null ? rs.getString("r5") : "0");
                Appear5.setText(rs.getString("w5") != null ? rs.getString("w5") : "0");

                System.out.println("✓ Data APGAR Score berhasil dimuat untuk No. RM: " + noRM);

            } else {
                // Jika data tidak ditemukan, kosongkan atau set default value
                JOptionPane.showMessageDialog(null, 
                    "Data APGAR Score untuk pasien dengan No. RM: " + noRM + " tidak ditemukan!\n" +
                    "Silakan isi manual atau periksa data pasien bayi.");

                // Reset semua field ke default
                resetApgarFields();
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data APGAR Score!\nError: " + e);
            System.out.println("Notifikasi Error APGAR: " + e);
        }
    }

    // Method untuk menghitung lama rawat (sistem medis)
private void hitungLamaRawat() {
    try {
        // Validasi input tanggal & jam
        if (tgl_masuk.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Tanggal masuk belum diisi!");
            return;
        }
        if (tgl_keluar.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Tanggal keluar belum diisi!");
            return;
        }
        if (jam_masuk.getSelectedItem() == null || menit_masuk.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Jam masuk belum lengkap!");
            return;
        }
        if (jam_keluar.getSelectedItem() == null || menit_keluar.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Jam keluar belum lengkap!");
            return;
        }

        // Ambil data tanggal & jam masuk
        Calendar calMasuk = Calendar.getInstance();
        calMasuk.setTime(tgl_masuk.getDate());
        int jamMasuk = Integer.parseInt(jam_masuk.getSelectedItem().toString());
        int menitMasuk = Integer.parseInt(menit_masuk.getSelectedItem().toString());
        calMasuk.set(Calendar.HOUR_OF_DAY, jamMasuk);
        calMasuk.set(Calendar.MINUTE, menitMasuk);
        calMasuk.set(Calendar.SECOND, 0);
        calMasuk.set(Calendar.MILLISECOND, 0);

        // Ambil data tanggal & jam keluar
        Calendar calKeluar = Calendar.getInstance();
        calKeluar.setTime(tgl_keluar.getDate());
        int jamKeluar = Integer.parseInt(jam_keluar.getSelectedItem().toString());
        int menitKeluar = Integer.parseInt(menit_keluar.getSelectedItem().toString());
        calKeluar.set(Calendar.HOUR_OF_DAY, jamKeluar);
        calKeluar.set(Calendar.MINUTE, menitKeluar);
        calKeluar.set(Calendar.SECOND, 0);
        calKeluar.set(Calendar.MILLISECOND, 0);

        // Validasi: tanggal keluar tidak boleh LEBIH AWAL dari masuk (bukan sama dengan)
        if (calKeluar.before(calMasuk)) {
            JOptionPane.showMessageDialog(null,
                "Tanggal/waktu keluar tidak boleh lebih awal dari tanggal/waktu masuk!");
            lamaharirawat.setText("0");
            lamajamrawat.setText("00:00");
            return;
        }

        // === PERHITUNGAN HARI RAWAT MEDIS ===

        // 1. Hitung jumlah hari berdasarkan pergantian tanggal (kalender)
        Calendar calTglMasuk = Calendar.getInstance();
        calTglMasuk.setTime(tgl_masuk.getDate());
        calTglMasuk.set(Calendar.HOUR_OF_DAY, 0);
        calTglMasuk.set(Calendar.MINUTE, 0);
        calTglMasuk.set(Calendar.SECOND, 0);
        calTglMasuk.set(Calendar.MILLISECOND, 0);

        Calendar calTglKeluar = Calendar.getInstance();
        calTglKeluar.setTime(tgl_keluar.getDate());
        calTglKeluar.set(Calendar.HOUR_OF_DAY, 0);
        calTglKeluar.set(Calendar.MINUTE, 0);
        calTglKeluar.set(Calendar.SECOND, 0);
        calTglKeluar.set(Calendar.MILLISECOND, 0);

        long selisihHariMs = calTglKeluar.getTimeInMillis() - calTglMasuk.getTimeInMillis();
        long hariRawat = (selisihHariMs / (24 * 60 * 60 * 1000)) + 1; // +1 karena hari masuk dihitung

        // 2. Hitung total jam dan menit aktual
        long selisihMs = calKeluar.getTimeInMillis() - calMasuk.getTimeInMillis();
        long totalMenit = selisihMs / (60 * 1000);

        // Hitung total jam dan menit
        long totalJam = totalMenit / 60;
        long menit = totalMenit % 60;

        // Tampilkan hasil
        lamaharirawat.setText(String.valueOf(hariRawat));
        lamajamrawat.setText(String.format("%02d:%02d", totalJam, menit));

        // Debug log
        System.out.println("=== DETAIL PERHITUNGAN LAMA RAWAT (MEDIS) ===");
        System.out.println("Masuk  : " + calMasuk.getTime());
        System.out.println("Keluar : " + calKeluar.getTime());
        System.out.println("Selisih Tanggal : " + (selisihHariMs / (24 * 60 * 60 * 1000)) + " hari");
        System.out.println("Hari Rawat Medis : " + hariRawat + " hari (termasuk hari masuk)");
        System.out.println("Total Menit Aktual : " + totalMenit + " menit");
        System.out.println("Total Jam:Menit : " + String.format("%02d:%02d", totalJam, menit));
        System.out.println("HASIL AKHIR: " + hariRawat + " hari " + String.format("%02d:%02d", totalJam, menit) + " jam");
        System.out.println("=============================================");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Format jam/menit tidak valid!");
        System.out.println("Error NumberFormat: " + e);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal menghitung lama rawat!\n" + e.getMessage());
        System.out.println("Error Hitung Lama Rawat: " + e);
    }
}
    
    public void setDiagnosa(String norw, String norm, String pasien, String nosep) {
        //cek koder
        try {
            /*moddy fix dual-sep: prioritaskan query by no_sep agar 2 SEP dalam 1 no_rawat bisa dibedakan*/
            if (!nosep.trim().equals("")) {
                rs = koneksi.prepareStatement("select * from bridging_sep where no_sep='" + nosep + "'").executeQuery();
            } else {
                // Jika no_rawat punya beberapa SEP (misal IGD + ranap), prioritaskan SEP ranap (jnspelayanan='1')
                rs = koneksi.prepareStatement(
                        "select * from bridging_sep where no_rawat='" + norw + "' "
                        + "ORDER BY CASE WHEN jnspelayanan='1' THEN 0 ELSE 1 END ASC, tglsep DESC LIMIT 1"
                ).executeQuery();
            }
            
            rs = koneksi.prepareStatement("select * from bridging_sep where no_rawat='"+norw+"'").executeQuery();
            while (rs.next()) {
                coder = Sequel.cariIsi("select no_ik from inacbg_coder_nik where nik='"+akses.getkode()+"'");
                if (akses.getkode().equals("Admin Utama")){
                   coder=Sequel.cariIsi("select no_ik from inacbg_coder_nik LIMIT 1");
                } else if (!coder.equals("")){
                   coder=coder;
                } else {
                    JOptionPane.showMessageDialog(null,"ANDA BUKAN CODER KLAIM, JADINYA TIDAK BISA !!!!!");
                }

                no_rawat.setText(norw);
                no_kartu.setText(rs.getString("no_kartu"));  
                Tsep.setText(rs.getString("no_sep"));  
                
                TPasien.setText(pasien);   
                jenis_kelamin.setText(rs.getString("jkel"));
                tgl_lahir.setText(rs.getString("tanggal_lahir"));
                TNoRM.setText(norm);
                
                if (rs.getString("jnspelayanan").equals("2")) {
                    hak_kelas.setSelectedIndex(2);
                } else {
                    switch (rs.getString("klsrawat")) {
                        case "1":
                            hak_kelas.setSelectedIndex(0);
                            break;
                        case "2":
                            hak_kelas.setSelectedIndex(1);
                            break;
                        case "3":
                            hak_kelas.setSelectedIndex(2);
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
                
                if (rs.getString("kdpolitujuan").equals("HDL")) {
                    check_hd.setSelected(true);
                } else {
                    check_hd.setSelected(false);
                }
                
                // Cek apakah sudah ada data tarif yang tersimpan
                boolean tarifTersimpan = loadTarifData();

                // Jika belum ada data tersimpan, load dari billing
                if (!tarifTersimpan) {
                    System.out.println(">>> Load data tarif dari BILLING (belum ada di tarif_data)");

                    // ===== PNB: Prosedur Non Bedah =====
                    // PHP: status='Ralan Dokter Paramedis' + 'Ranap Dokter Paramedis', NOT terapi
                    pnb.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status IN ('Ralan Dokter Paramedis', 'Ranap Dokter Paramedis') " +
                        "AND nm_perawatan NOT LIKE '%terapi%'"
                    ));

                    // ===== KSL: Konsultasi=====
                    // "PEMERIKSAAN DOKTER SPESIALIS" status='Ralan Dokter' → harus masuk sini
                    ksl.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status IN ('Ralan Dokter', 'Ranap Dokter')"
                    ));

                    // ===== REHAB: Rehabilitasi =====
                    // PHP: sama seperti PNB tapi WITH terapi
                    rehab.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status IN ('Ralan Dokter Paramedis', 'Ranap Dokter Paramedis') " +
                        "AND nm_perawatan LIKE '%terapi%'"
                    ));

                    // ===== KPR: Keperawatan =====
                    kpr.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status IN ('Ranap Paramedis', 'Ralan Paramedis')"
                    ));

                    // ===== PB: Prosedur Bedah (sudah benar) =====
                    pb.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Operasi'"
                    ));

                    // ===== RAD: Radiologi (sudah benar) =====
                    rad.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Radiologi'"
                    ));

                    // ===== LAB: Laboratorium (sudah benar) =====
                    lab.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Laborat'"
                    ));

                    // ===== BMHP (PHP: status='Tambahan') =====
                    bmhp.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Tambahan'"
                    ));

                    // ===== SEWA ALAT (PHP: Harian + Service) =====
                    sewa_alat.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status IN ('Harian', 'Service')"
                    ));

                    // ===== AKOMODASI / KAMAR =====
                    // PHP: Kamar + biaya_reg dari reg_periksa
                    akomodasi.setText(Sequel.cariIsi(
                        "SELECT IFNULL(SUM(b.totalbiaya), 0) + " +
                        "       (SELECT IFNULL(biaya_reg, 0) FROM reg_periksa WHERE no_rawat = '" + no_rawat.getText() + "') " +
                        "FROM billing b " +
                        "WHERE b.no_rawat = '" + no_rawat.getText() + "' " +
                        "AND b.status = 'Kamar'"
                    ));

                    // ===== OBAT KRONIS (sudah benar logikanya) =====
                    String valKronis = Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Obat' AND nm_perawatan LIKE '%kronis%'"
                    );
                    kronis.setText(valKronis);

                    // ===== OBAT KEMO =====
                    String valKemo = Sequel.cariIsi(
                        "SELECT IFNULL(SUM(totalbiaya), 0) FROM billing " +
                        "WHERE no_rawat = '" + no_rawat.getText() + "' " +
                        "AND status = 'Obat' AND nm_perawatan LIKE '%kemo%'"
                    );
                    obat_kemo.setText(valKemo);

                    // ===== OBAT (total - kronis - kemo) =====
                    obat.setText(Sequel.cariIsi(
                        "SELECT ( " +
                        "  (SELECT IFNULL(SUM(totalbiaya),0) FROM billing WHERE no_rawat='" + no_rawat.getText() + "' AND status='Obat') + " +
                        "  (SELECT IFNULL(SUM(totalbiaya),0) FROM billing WHERE no_rawat='" + no_rawat.getText() + "' AND status='Retur Obat') + " +
                        "  (SELECT IFNULL(SUM(totalbiaya),0) FROM billing WHERE no_rawat='" + no_rawat.getText() + "' AND status='Resep Pulang') - " +
                        "  (SELECT IFNULL(SUM(totalbiaya),0) FROM billing WHERE no_rawat='" + no_rawat.getText() + "' AND status='Obat' AND nm_perawatan LIKE '%kronis%') - " +
                        "  (SELECT IFNULL(SUM(totalbiaya),0) FROM billing WHERE no_rawat='" + no_rawat.getText() + "' AND status='Obat' AND nm_perawatan LIKE '%kemo%') " +
                        ") AS total"
                    ));

                    // ===== ALKES, PNJ, PL_DARAH, RI, AHLI = 0 (sesuai PHP) =====
                    alkes.setText("0");
                    pnj.setText("0");
                    pl_darah.setText("0");
                    ri.setText("0");
                    ahli.setText("0");
                } else {
                    System.out.println(">>> Data tarif dimuat dari TARIF_DATA (sudah tersimpan)");
                }
                
                //  set jumlah darah hd
                kt_darah.setText(Sequel.cariIsi("select jumlah from billing where nm_perawatan like '%(DRH)%' and no_rawat='"+no_rawat.getText()+"'"));
                pasien_tb.setText(Sequel.cariIsi("select no_sitb from inacbg_validasi_tb where no_rkm_medis='"+TNoRM.getText().trim()+"' "));
                hitungtotalbiaya();
                
                no_rawat_bayi = Sequel.cariIsi("SELECT ranap_gabung.no_rawat2 from ranap_gabung WHERE ranap_gabung.no_rawat='"+no_rawat.getText().trim()+"' ");
                berat_bayi.setText(Sequel.cariIsi("SELECT pasien_bayi.berat_badan FROM pasien_bayi INNER JOIN reg_periksa ON reg_periksa.no_rkm_medis=pasien_bayi.no_rkm_medis WHERE reg_periksa.no_rawat='"+no_rawat_bayi+"'"));

                
                try {
                    rs = koneksi.prepareStatement("select reg_periksa.tgl_registrasi, reg_periksa.jam_reg, reg_periksa.status_lanjut, reg_periksa.status_bayar, dokter.nm_dokter from reg_periksa inner join "
                            + "dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat='"+no_rawat.getText()+"'").executeQuery();
                    if (rs.next()) {
                        if (rs.getString("status_lanjut").equals("Ralan")) {
                            dokterkonsul = Sequel.cariIsi("select concat(', ',dokter.nm_dokter) from reg_periksa inner "
                                    + "join konsultasi_medik on konsultasi_medik.no_rawat=reg_periksa.no_rawat inner join dokter on konsultasi_medik.kd_dokter_dikonsuli=dokter.kd_dokter where "
                                    + "reg_periksa.no_rawat='"+no_rawat.getText()+"'");
                             
                            tgl_masuk.setDate(rs.getDate("tgl_registrasi"));
                            tgl_keluar.setDate(rs.getDate("tgl_registrasi"));
                            jam_masuk.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            jam_keluar.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            menit_masuk.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            menit_keluar.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            dpjp.setText(rs.getString("nm_dokter")+dokterkonsul);
                            
                            // TAMBAHKAN BARIS INI:
                            setCaraMasuk();
                            setStatusPulang();
                            checkAndEnableApgar();
                            fillTekananDarah();                            
                            hitungLamaRawat();
                            // checkDataTB();                            
                                                        
                        } else {
                            jenis_rawat.setSelectedIndex(0);
            
                            // ✅ AMBIL DARI REG_PERIKSA (bukan kamar_inap)
                            tgl_masuk.setDate(rs.getDate("tgl_registrasi"));
                            jam_masuk.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            menit_masuk.setSelectedItem(rs.getString("jam_reg").substring(3,5));

                            // ✅ TGL KELUAR TETAP DARI KAMAR_INAP
                            tgl_keluar_ranap = Sequel.cariIsi("SELECT DISTINCT(concat(tgl_keluar,' ',jam_keluar)) as jam_keluar FROM kamar_inap WHERE no_rawat='"+no_rawat.getText()+"' AND tgl_keluar<>'0000-00-00' ORDER BY tgl_masuk DESC LIMIT 1");

                            if (tgl_keluar_ranap.equals("")) {
                                // Jika belum dipulangkan, gunakan tgl_registrasi sebagai default
                                tgl_keluar.setDate(rs.getDate("tgl_registrasi"));
                                jam_keluar.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                                menit_keluar.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                                JOptionPane.showMessageDialog(null,"PASIEN INI BELUM DIPULANGKAN !!!!!!");
                            } else {
                                Valid.SetTgl(tgl_keluar, tgl_keluar_ranap.substring(0,10));
                                jam_keluar.setSelectedItem(tgl_keluar_ranap.substring(11,13));
                                menit_keluar.setSelectedItem(tgl_keluar_ranap.substring(14,16));
                            }

                            // ✅ AMBIL DPJP RANAP
                            try {
                                rs3 = koneksi.prepareStatement(
                                    "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='"+no_rawat.getText()+"'").executeQuery();

                                dpjp_ranap = "";
                                while (rs3.next()) {
                                    dpjp_ranap = dpjp_ranap + rs3.getString("nm_dokter") + ", ";
                                }
                            } catch (Exception e) {
                                System.out.println("Error DPJP Ranap : "+e);
                            }

                            dpjp.setText(dpjp_ranap);

                            setCaraMasuk();
                            setStatusPulang();
                            checkAndEnableApgar();
                            checkAndEnableKehamilan();
                            fillTekananDarah();
                            hitungLamaRawat();
                            checkRawatICU();
                            // checkDataTB();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error eklaim : "+e);
                }
                
                
                jLabel49.setVisible(false);                
                //jLabel53.setVisible(false);
                
                check_tbItemStateChanged(null);
                check_hdItemStateChanged(null);
                jenis_rawatItemStateChanged(null);
                rawat_intensifItemStateChanged(null);
                kelas_eksekutifItemStateChanged(null);
                naik_turun_kelasItemStateChanged(null);
                KehamilanItemStateChanged(null);
                
//                cek sudah di grouper 
                try {
                    rsgrouper = koneksi.prepareStatement("select * from inacbg_grouping_stage1 where no_sep='"+Tsep.getText().trim()+"'").executeQuery();
                    while (rsgrouper.next()) {
                        lblTarifRS.setText(tarifRS.getText());
                        lblBaseTariff.setText(Valid.SetAngka(rsgrouper.getDouble("tarif")));
                        Txtgroup.setText(rsgrouper.getString("deskripsi"));
                        lJenis1.setText(rsgrouper.getString("code_cbg"));
                    }
                } catch (Exception e) {
                    System.out.println("error tampil : "+e);
                }
                
                for (i = 0; i < kd_psdr.length; i++) {
                    kd_diag[i].setText("");
                    kd_diag2[i].setText("");
                    nm_diag[i].setText("");
                    nm_diag2[i].setText("");
                    kd_psdr[i].setText("");
                    kd_psdr2[i].setText("");
                    nm_psdr[i].setText("");
                    nm_psdr2[i].setText("");
                    chk_diag[i].setSelected(false);
                    chk_diag2[i].setSelected(false);
                    multiple[i].setText(""); 
                }
                
                try {

                    rs = koneksi.prepareStatement(
                        "select * from idrg_diagnosa where no_sep='" + Tsep.getText().trim() + "'"
                    ).executeQuery();

                    j = 0;
                    while (rs.next() && j < kd_diag.length) {
                        kd_diag[j].setText(rs.getString("icd10"));
                        nm_diag[j].setText(rs.getString("icd10_desc"));
                        if(rs.getString("prioritas").equals("1")){
                            chk_diag[j].setSelected(true);
                        }else{
                            chk_diag[j].setSelected(false);
                        }
                        
                        j++;
                    }
                    
                    // Jika idrg_diagnosa kosong, tarik dari diagnosa_pasien urut prioritas
                    if (j == 0) {
                        ResultSet rsDiagPasien = koneksi.prepareStatement(
                                "SELECT dp.kd_penyakit, IFNULL(p.nm_penyakit, '') AS nm_penyakit, dp.prioritas "
                                + "FROM diagnosa_pasien dp "
                                + "LEFT JOIN penyakit p ON dp.kd_penyakit = p.kd_penyakit "
                                + "WHERE dp.no_rawat = '" + no_rawat.getText().trim() + "' "
                                + "ORDER BY dp.prioritas ASC LIMIT 10"
                        ).executeQuery();
                        while (rsDiagPasien.next() && j < kd_diag.length) {
                            kd_diag[j].setText(rsDiagPasien.getString("kd_penyakit"));
                            nm_diag[j].setText(rsDiagPasien.getString("nm_penyakit"));
                            chk_diag[j].setSelected(rsDiagPasien.getString("prioritas").equals("1"));
                            j++;
                        }
                        rsDiagPasien.close();
                    }
                } catch (Exception e) {
                    System.out.println("error idrg diagnosa : " + e);
                }
                
                try {

                    rs = koneksi.prepareStatement(
                        "select * from inacbg_diagnosa where no_sep='" + Tsep.getText().trim() + "'"
                    ).executeQuery();

                    j = 0;
                    while (rs.next() && j < kd_diag2.length) {
                        kd_diag2[j].setText(rs.getString("icd10"));
                        nm_diag2[j].setText(rs.getString("icd10_desc"));
                        if(rs.getString("prioritas").equals("1")){
                            chk_diag2[j].setSelected(true);
                        }else{
                            chk_diag2[j].setSelected(false);
                        }
                        
                        j++;
                    }
                    
                } catch (Exception e) {
                    System.out.println("error INA diagnosa : " + e);
                }
                
                try {

                    rs = koneksi.prepareStatement(
                        "select * from idrg_prosedur where no_sep='" + Tsep.getText().trim() + "'"
                    ).executeQuery();

                    j = 0;
                    while (rs.next() && j < kd_psdr.length) {
                        kd_psdr[j].setText(rs.getString("kd_icd9"));
                        nm_psdr[j].setText(rs.getString("nm_icd9"));
                        multiple[j].setText(rs.getString("multiplicity"));
                        
                        j++;
                    }
                } catch (Exception e) {
                    System.out.println("error idrg diagnosa : " + e);
                }
                
                try {

                    rs = koneksi.prepareStatement(
                        "select * from inacbg_prosedur where no_sep='" + Tsep.getText().trim() + "'"
                    ).executeQuery();

                    j = 0;
                    while (rs.next() && j < kd_psdr2.length) {
                        kd_psdr2[j].setText(rs.getString("kd_icd9"));
                        nm_psdr2[j].setText(rs.getString("nm_icd9"));
                        
                        j++;
                    }
                    
                } catch (Exception e) {
                    System.out.println("error INA prosedur : " + e);
                }
                
                try {
                    rs = koneksi.prepareStatement("select * from idrg_grouper where no_sep='"+Tsep.getText().trim()+"'").executeQuery();
                    while (rs.next()) {
                        responIDRG.setText(
                            "<html>" +
                            "JENIS RAWAT : " + jenis_rawat.getSelectedItem().toString() + "<br><br>" +
                            "MDC : " + rs.getString("mdc_desc") + " (" + rs.getString("mdc") + ")<br><br>" +
                            "DRG : " + rs.getString("drg_desc") + " (" + rs.getString("drg") + ")" +
                            "</html>"
                        );
                    }
                } catch (Exception e) {
                    System.out.println("error tampil : "+e);
                }
                
                isLoading = false;
                
                                try {

                    rs = koneksi.prepareStatement(
                            "select * from idrg_prosedur where no_sep='" + Tsep.getText().trim() + "'"
                    ).executeQuery();

                    j = 0;
                    while (rs.next() && j < kd_psdr.length) {
                        kd_psdr[j].setText(rs.getString("kd_icd9"));
                        nm_psdr[j].setText(rs.getString("nm_icd9"));
                        multiple[j].setText(rs.getString("multiplicity"));

                        j++;
                    }

                    // Jika idrg_prosedur kosong, tarik dari prosedur_pasien urut prioritas
                    if (j == 0) {
                        ResultSet rsProsedurPasien = koneksi.prepareStatement(
                                "SELECT pp.kode, IFNULL(i.deskripsi_panjang, '') AS nm_prosedur "
                                + "FROM prosedur_pasien pp "
                                + "LEFT JOIN icd9 i ON pp.kode = i.kode "
                                + "WHERE pp.no_rawat = '" + no_rawat.getText().trim() + "' "
                                + "ORDER BY pp.prioritas ASC LIMIT 10"
                        ).executeQuery();
                        while (rsProsedurPasien.next() && j < kd_psdr.length) {
                            kd_psdr[j].setText(rsProsedurPasien.getString("kode"));
                            nm_psdr[j].setText(rsProsedurPasien.getString("nm_prosedur"));
                            j++;
                        }
                        rsProsedurPasien.close();
                    }
                } catch (Exception e) {
                    System.out.println("error idrg prosedur : " + e);
                }
                
                try {
                    rs = koneksi.prepareStatement("select * from inacbg_grouping_stage2 where no_sep='"+Tsep.getText().trim()+"'").executeQuery();
                    while (rs.next()) {
                        if (rs.getString("type").toLowerCase().contains("prosthesis")) {
                            lblTarifPros.setText(rs.getString("tarif"));
                            cmbPros.addItem(rs.getString("deskripsi"));
                            cmbPros.setSelectedIndex(1);
                        }

                        if (rs.getString("type").toLowerCase().contains("procedure")) {
                            lbltarifProc.setText(rs.getString("tarif"));
                            cmbProc.addItem(rs.getString("deskripsi"));
                            cmbProc.setSelectedIndex(1);
                        }

                        if (rs.getString("type").toLowerCase().contains("investigation")) {
                            lblTarifInv.setText(rs.getString("tarif"));
                            cmbInv.addItem(rs.getString("deskripsi"));
                            cmbInv.setSelectedIndex(1);
                        }

                        if (rs.getString("type").toLowerCase().contains("drugs")) {
                            lblTarifDrugs.setText(rs.getString("tarif"));
                            cmbDrugs.addItem(rs.getString("deskripsi"));
                            cmbDrugs.setSelectedIndex(1);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error tampil : "+e);
                }
                
                double totalTarif = 0;
                
                double totalStage1 = Sequel.cariIsiAngka(
                    "SELECT IFNULL(SUM(tarif), 0) FROM inacbg_grouping_stage1 WHERE no_sep = ?",Tsep.getText().trim()
                );
                
                double totalStage2 = Sequel.cariIsiAngka(
                    "SELECT IFNULL(SUM(tarif), 0) FROM inacbg_grouping_stage2 WHERE no_sep = ?", Tsep.getText().trim()
                );
                
                totalTarif = totalStage1 + totalStage2;
                
                lblTarifTotal.setText(Valid.SetAngka(totalTarif));
                
                getICD10();
                getICD10INA();
                getICD9();
                getICD9INA();
                
                check_tbItemStateChanged(null);
                check_hdItemStateChanged(null);
                jenis_rawatItemStateChanged(null);
                rawat_intensifItemStateChanged(null);
                kelas_eksekutifItemStateChanged(null);
                naik_turun_kelasItemStateChanged(null);
                
                tombolinacbg();
            }
                
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
    }
    
    // Method helper untuk show/hide komponen APGAR
    private void showApgarComponents(boolean show) {
        Appear1.setVisible(show);
        Pulse1.setVisible(show);
        Grimace1.setVisible(show);
        Activity1.setVisible(show);
        Respiration1.setVisible(show);
        jLabel237.setVisible(show);

        Appear5.setVisible(show);
        Pulse5.setVisible(show);
        Grimace5.setVisible(show);
        Activity5.setVisible(show);
        Respiration5.setVisible(show);
        jLabel238.setVisible(show);

        jLabel239.setVisible(show);
        jLabel235.setVisible(show);
        jLabel240.setVisible(show);
        jLabel241.setVisible(show);
        jLabel242.setVisible(show);
        
        jLabel221.setVisible(show);
        berat_bayi.setVisible(show);
        jLabel219.setVisible(show);

    }
    
    // Method untuk cek dan auto-enable Kehamilan jika ada data catatan persalinan
    private void checkAndEnableKehamilan() {
        try {
            // Validasi no_rawat sudah terisi
            if (no_rawat.getText().trim().equals("")) {
                System.out.println("DEBUG: No Rawat kosong");
                return;
            }

            String noRawat = no_rawat.getText().trim();

            // Query count untuk cek keberadaan data
            int jumlah = Sequel.cariInteger(
                "SELECT COUNT(*) FROM catatan_persalinan WHERE no_rawat = '" + noRawat + "'"
            );

            // Debug info
            System.out.println("DEBUG KEHAMILAN CHECK:");
            System.out.println("No Rawat       : " + noRawat);
            System.out.println("Data ditemukan : " + jumlah);

            if (jumlah > 0) {
                // Data ditemukan, otomatis centang checkbox Kehamilan
                Kehamilan.setSelected(true);
                showKehamilanComponents(true);
                System.out.println("✓ Data persalinan ditemukan. Checkbox Kehamilan diaktifkan.");
                
                // Ambil dan isi data kehamilan dari tabel penilaian_awal_keperawatan_kebidanan_ranap
                loadDataKehamilanFromKebidanan(noRawat);

            } else {
                // Data tidak ditemukan, non-aktifkan checkbox Kehamilan
                Kehamilan.setSelected(false);
                showKehamilanComponents(false);
                System.out.println("✗ Data persalinan tidak ditemukan. Checkbox Kehamilan tidak diaktifkan.");
            }

        } catch (Exception e) {
            System.out.println("Error checkAndEnableKehamilan: " + e);
            e.printStackTrace();
        }
    }

    // Method helper untuk show/hide komponen Kehamilan
    private void showKehamilanComponents(boolean show) {
        label14.setVisible(show);
        UsiaKehamilan.setVisible(show);
        label10.setVisible(show);

        label7.setVisible(show);
        label11.setVisible(show);
        Gravida.setVisible(show);
        label12.setVisible(show);
        Partus.setVisible(show);
        label13.setVisible(show);
        Abortus.setVisible(show);

        label8.setVisible(show);
        Kontraksi.setVisible(show);
    }
    
    // Method untuk load data kehamilan dari tabel penilaian_awal_keperawatan_kebidanan_ranap
    private void loadDataKehamilanFromKebidanan(String noRawat) {
        try {
            // Query untuk ambil data kehamilan
            String sql = "SELECT riwayat_hamil_usiahamil, riwayat_persalinan_g, " +
                         "riwayat_persalinan_p, riwayat_persalinan_a " +
                         "FROM penilaian_awal_keperawatan_kebidanan_ranap " +
                         "WHERE no_rawat = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Isi data ke komponen
                String usiaHamil = rs.getString("riwayat_hamil_usiahamil");
                String gravida = rs.getString("riwayat_persalinan_g");
                String partus = rs.getString("riwayat_persalinan_p");
                String abortus = rs.getString("riwayat_persalinan_a");

                // Set ke textbox (cek null terlebih dahulu)
                UsiaKehamilan.setText(usiaHamil != null ? usiaHamil : "");
                Gravida.setText(gravida != null ? gravida : "");
                Partus.setText(partus != null ? partus : "");
                Abortus.setText(abortus != null ? abortus : "");

                System.out.println("✓ Data kehamilan berhasil dimuat:");
                System.out.println("  - Usia Kehamilan : " + (usiaHamil != null ? usiaHamil : "-"));
                System.out.println("  - Gravida        : " + (gravida != null ? gravida : "-"));
                System.out.println("  - Partus         : " + (partus != null ? partus : "-"));
                System.out.println("  - Abortus        : " + (abortus != null ? abortus : "-"));

            } else {
                System.out.println("⚠ Data di tabel penilaian_awal_keperawatan_kebidanan_ranap tidak ditemukan.");
                // Kosongkan field jika data tidak ditemukan
                UsiaKehamilan.setText("");
                Gravida.setText("");
                Partus.setText("");
                Abortus.setText("");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error loadDataKehamilanFromKebidanan: " + e);
            e.printStackTrace();
        }
    }
    
    // Method utama untuk mengisi data tekanan darah    
    private void fillTekananDarah() {
        try {
            if (no_rawat.getText().trim().equals("")) {
                return;
            }

            String noRawat = no_rawat.getText().trim();
            String tensiData = "";

            // Cek dari combobox jenis_rawat (jika ada)
            if (jenis_rawat.getSelectedIndex() == 1) {
                // Rawat Jalan
                tensiData = getTensiRalan(noRawat);
            } else {
                // Rawat Inap (default index 0)
                tensiData = getTensiRanap(noRawat);
            }

            if (!tensiData.equals("") && !tensiData.equals("-")) {
                parseTensiData(tensiData);
            }

        } catch (Exception e) {
            System.out.println("Error fillTekananDarahAlternatif: " + e);
        }
    }

    // Method untuk ambil tensi dari rawat jalan
    private String getTensiRalan(String noRawat) {
        try {
            // STRATEGI 2 (AKTIF): Ambil data PERTAMA/AWAL (pemeriksaan pertama)
            String sql = "SELECT tensi FROM pemeriksaan_ralan " +
                         "WHERE no_rawat = ? " +
                         "ORDER BY tgl_perawatan ASC, jam_rawat ASC " +
                         "LIMIT 1";

            // STRATEGI 1: Ambil data TERAKHIR/TERBARU
            // Uncomment jika ingin menggunakan strategi ini:
            /*
            String sql = "SELECT tensi FROM pemeriksaan_ralan " +
                         "WHERE no_rawat = ? " +
                         "ORDER BY tgl_perawatan DESC, jam_rawat DESC " +
                         "LIMIT 1";
            */

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            String tensi = "";
            if (rs.next()) {
                tensi = rs.getString("tensi");
                System.out.println("  Data tensi Ralan (pertama): " + (tensi != null ? tensi : "-"));
            }

            rs.close();
            ps.close();

            return (tensi != null) ? tensi : "";

        } catch (Exception e) {
            System.out.println("Error getTensiRalan: " + e);
            return "";
        }
    }

    // Method untuk ambil tensi dari rawat inap (triase IGD)
    private String getTensiRanap(String noRawat) {
        try {
            String sql = "SELECT tekanan_darah FROM data_triase_igd " +
                         "WHERE no_rawat = ? " +
                         "LIMIT 1";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            String tekananDarah = "";
            if (rs.next()) {
                tekananDarah = rs.getString("tekanan_darah");
                System.out.println("  Data tensi Ranap: " + (tekananDarah != null ? tekananDarah : "-"));
            }

            rs.close();
            ps.close();

            return (tekananDarah != null) ? tekananDarah : "";

        } catch (Exception e) {
            System.out.println("Error getTensiRanap: " + e);
            return "";
        }
    }

    // Method untuk parsing data tensi format "120/70"
    private void parseTensiData(String tensiData) {
        try {
            // Trim whitespace
            tensiData = tensiData.trim();

            System.out.println("DEBUG PARSING TENSI:");
            System.out.println("Data asli: " + tensiData);

            // Cek apakah ada karakter "/"
            if (tensiData.contains("/")) {
                // Split berdasarkan "/"
                String[] parts = tensiData.split("/");

                if (parts.length >= 2) {
                    String sistoleValue = parts[0].trim();
                    String diastoleValue = parts[1].trim();

                    // Validasi: pastikan nilai numerik
                    if (isNumeric(sistoleValue) && isNumeric(diastoleValue)) {
                        sistole.setText(sistoleValue);
                        distole.setText(diastoleValue);

                        System.out.println("✓ Tensi berhasil diparsing:");
                        System.out.println("  Sistole  : " + sistoleValue);
                        System.out.println("  Diastole : " + diastoleValue);
                    } else {
                        System.out.println("⚠ Format tensi tidak valid (bukan angka): " + tensiData);
                        sistole.setText("");
                        distole.setText("");
                    }
                } else {
                    System.out.println("⚠ Format tensi tidak lengkap: " + tensiData);
                    sistole.setText("");
                    distole.setText("");
                }
            } else {
                // Jika tidak ada "/", coba parsing format lain atau set kosong
                System.out.println("⚠ Format tensi tidak mengandung '/': " + tensiData);
                sistole.setText("");
                distole.setText("");
            }

        } catch (Exception e) {
            System.out.println("Error parseTensiData: " + e);
            sistole.setText("");
            distole.setText("");
        }
    }

    // Helper method untuk validasi numerik
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Method utama untuk set combobox cara_masuk
    private void setCaraMasuk() {
        try {
            // Validasi no_rawat sudah terisi
            if (no_rawat.getText().trim().equals("")) {
                System.out.println("DEBUG: No Rawat kosong");
                return;
            }

            String noRawat = no_rawat.getText().trim();

            System.out.println("=== DEBUG CARA MASUK ===");
            System.out.println("No Rawat: " + noRawat);

            // Ambil data dari bridging_sep
            String sql = "SELECT asal_rujukan, kdppkrujukan, kdppkpelayanan " +
                         "FROM bridging_sep " +
                         "WHERE no_rawat = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String asalRujukan = rs.getString("asal_rujukan");
                String kdPpkRujukan = rs.getString("kdppkrujukan");
                String kdPpkPelayanan = rs.getString("kdppkpelayanan");

                System.out.println("Asal Rujukan     : " + asalRujukan);
                System.out.println("Kode PPK Rujukan : " + kdPpkRujukan);
                System.out.println("Kode PPK Pelayanan: " + kdPpkPelayanan);

                // Logika penentuan cara masuk
                if (asalRujukan != null) {

                    // CASE 1: Faskes 1
                    if (asalRujukan.equals("1. Faskes 1")) {
                        cara_masuk.setSelectedIndex(0);
                        System.out.println("✓ Cara Masuk: Index 0 (Rujukan FKTP)");

                    // CASE 2: Faskes 2 (RS)
                    } else if (asalRujukan.equals("2. Faskes 2(RS)")) {

                        // Cek apakah kdppkrujukan != kdppkpelayanan
                        if (kdPpkRujukan != null && kdPpkPelayanan != null) {

                            if (!kdPpkRujukan.equals(kdPpkPelayanan)) {
                                // Beda PPK
                                cara_masuk.setSelectedIndex(1);
                                System.out.println("✓ Cara Masuk: Index 1 (Rujukan FKRTL)");

                            } else {
                                // PPK sama, cek kd_poli
                                String kdPoli = getKdPoli(noRawat);
                                System.out.println("Kode Poli        : " + kdPoli);

                                if (kdPoli.equals("IGDK") || kdPoli.equals("U0042")) {
                                    // IGD
                                    cara_masuk.setSelectedIndex(5);
                                    System.out.println("✓ Cara Masuk: Index 5 (Dari Rawat Darurat)");

                                } else {
                                    // Poli Biasa
                                    cara_masuk.setSelectedIndex(3);
                                    System.out.println("✓ Cara Masuk: Index 3 (Dari Rawat Jalan)");
                                }
                            }
                        } else {
                            System.out.println("⚠ Kode PPK null, set default index 0");
                            cara_masuk.setSelectedIndex(10);
                        }

                    } else {
                        // Asal rujukan lainnya
                        System.out.println("⚠ Asal rujukan tidak dikenali: " + asalRujukan);
                        cara_masuk.setSelectedIndex(10);
                    }

                } else {
                    System.out.println("⚠ Asal rujukan null");
                    cara_masuk.setSelectedIndex(10);
                }

            } else {
                System.out.println("⚠ Data bridging_sep tidak ditemukan");
                cara_masuk.setSelectedIndex(10);
            }

            rs.close();
            ps.close();
            System.out.println("========================");

        } catch (Exception e) {
            System.out.println("Error setCaraMasuk: " + e);
            e.printStackTrace();
        }
    }

    // Method helper untuk ambil kd_poli dari reg_periksa
    private String getKdPoli(String noRawat) {
        try {
            String sql = "SELECT kd_poli FROM reg_periksa WHERE no_rawat = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            String kdPoli = "";
            if (rs.next()) {
                kdPoli = rs.getString("kd_poli");
            }

            rs.close();
            ps.close();

            return (kdPoli != null) ? kdPoli : "";

        } catch (Exception e) {
            System.out.println("Error getKdPoli: " + e);
            return "";
        }
    }
    
    // Method utama untuk set combobox status_pulang
    private void setStatusPulang() {
        try {
            // Validasi no_rawat sudah terisi
            if (no_rawat.getText().trim().equals("")) {
                System.out.println("DEBUG: No Rawat kosong");
                return;
            }

            String noRawat = no_rawat.getText().trim();

            System.out.println("=== DEBUG STATUS PULANG ===");
            System.out.println("No Rawat: " + noRawat);

            // Cek status_lanjut untuk menentukan Ralan/Ranap
            String statusLanjut = Sequel.cariIsi(
                "SELECT status_lanjut FROM reg_periksa WHERE no_rawat = '" + noRawat + "'"
            );

            System.out.println("Status Lanjut: " + statusLanjut);

            String statusData = "";

            if (statusLanjut.equals("Ranap")) {
                // Rawat Inap - cek dari kamar_inap
                statusData = getStatusPulangRanap(noRawat);
                System.out.println("Status dari kamar_inap: " + statusData);

            } else if (statusLanjut.equals("Ralan")) {
                // Rawat Jalan - cek dari reg_periksa
                statusData = getStatusPulangRalan(noRawat);
                System.out.println("Status dari reg_periksa: " + statusData);

            } else {
                System.out.println("⚠ Status lanjut tidak dikenali");
            }

            // Mapping status ke index combobox
            if (!statusData.equals("")) {
                mapStatusToCombobox(statusData);
            } else {
                System.out.println("⚠ Status pulang tidak ditemukan, set default index 0");
                status_pulang.setSelectedIndex(0);
            }

            System.out.println("===========================");

        } catch (Exception e) {
            System.out.println("Error setStatusPulang: " + e);
            e.printStackTrace();
        }
    }

    // Method untuk ambil status pulang dari kamar_inap (Rawat Inap)
    private String getStatusPulangRanap(String noRawat) {
        try {
            // Ambil status pulang terakhir
            String sql = "SELECT stts_pulang FROM kamar_inap " +
                         "WHERE no_rawat = ? " +
                         "ORDER BY tgl_keluar DESC, jam_keluar DESC " +
                         "LIMIT 1";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            String sttsPulang = "";
            if (rs.next()) {
                sttsPulang = rs.getString("stts_pulang");
            }

            rs.close();
            ps.close();

            return (sttsPulang != null) ? sttsPulang : "";

        } catch (Exception e) {
            System.out.println("Error getStatusPulangRanap: " + e);
            return "";
        }
    }

    // Method untuk ambil status dari reg_periksa (Rawat Jalan)
    private String getStatusPulangRalan(String noRawat) {
        try {
            String sql = "SELECT stts FROM reg_periksa WHERE no_rawat = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            String stts = "";
            if (rs.next()) {
                stts = rs.getString("stts");
            }

            rs.close();
            ps.close();

            return (stts != null) ? stts : "";

        } catch (Exception e) {
            System.out.println("Error getStatusPulangRalan: " + e);
            return "";
        }
    }

    // Method untuk mapping status ke index combobox
    private void mapStatusToCombobox(String statusData) {
        /*
        Combobox status_pulang:
        Index 0: "1 : Atas persetujuan dokter"
        Index 1: "2 : Dirujuk"
        Index 2: "3 : Atas permintaan sendiri"
        Index 3: "4 : Meninggal"
        Index 4: "5 : Lain-lain"
        */

        // Normalisasi status (lowercase dan trim)
        String status = statusData.toLowerCase().trim();

        System.out.println("Mapping status: " + statusData);

        // Mapping berdasarkan status
        switch (status) {
            // Index 0: Atas persetujuan dokter
            case "sehat":
            case "sembuh":
            case "membaik":
            case "atas persetujuan dokter":
            case "sudah":
                status_pulang.setSelectedIndex(0);
                System.out.println("✓ Set Index 0: Atas persetujuan dokter");
                break;

            // Index 1: Dirujuk
            case "rujuk":
            case "dirujuk":
                status_pulang.setSelectedIndex(1);
                System.out.println("✓ Set Index 1: Dirujuk");
                break;

            // Index 2: Atas permintaan sendiri
            case "aps":
            case "atas permintaan sendiri":
            case "pulang paksa":
                status_pulang.setSelectedIndex(2);
                System.out.println("✓ Set Index 2: Atas permintaan sendiri");
                break;

            // Index 3: Meninggal
            case "+":
            case "meninggal":
                status_pulang.setSelectedIndex(3);
                System.out.println("✓ Set Index 3: Meninggal");
                break;

            // Index 4: Lain-lain
            case "-":
            case "pindah kamar":
            case "status belum lengkap":
            case "isoman":
            case "lain-lain":
            case "belum":
            case "batal":
            case "berkas diterima":
            case "dirawat":
                status_pulang.setSelectedIndex(4);
                System.out.println("✓ Set Index 4: Lain-lain");
                break;

            default:
                // Status tidak dikenali, set default
                System.out.println("⚠ Status tidak dikenali: " + statusData + ", set index 0");
                status_pulang.setSelectedIndex(0);
                break;
        }
    }
    
    // Method utama untuk cek dan set rawat intensif (ICU)
    private void checkRawatICU() {
        try {
            // Validasi no_rawat sudah terisi
            if (no_rawat.getText().trim().equals("")) {
                System.out.println("DEBUG: No Rawat kosong");
                return;
            }

            String noRawat = no_rawat.getText().trim();
            System.out.println("=== DEBUG RAWAT ICU ===");
            System.out.println("No Rawat: " + noRawat);

            // Query untuk cek riwayat rawat di ICU
            // Asumsi: Bangsal ICU memiliki kd_bangsal atau nm_bangsal yang mengandung kata "ICU"
            String sql = "SELECT SUM(kamar_inap.lama) as total_hari_icu " +
                         "FROM kamar_inap " +
                         "INNER JOIN kamar ON kamar_inap.kd_kamar = kamar.kd_kamar " +
                         "INNER JOIN bangsal ON kamar.kd_bangsal = bangsal.kd_bangsal " +
                         "WHERE kamar_inap.no_rawat = ? " +
                         "AND (bangsal.nm_bangsal LIKE '%ICU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%ICCU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%NICU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%PICU%' OR " +
                         "     bangsal.kd_bangsal LIKE '%ICU%')";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            int totalHariICU = 0;
            boolean pernahDiICU = false;

            if (rs.next()) {
                totalHariICU = rs.getInt("total_hari_icu");
                if (totalHariICU > 0) {
                    pernahDiICU = true;
                }
            }

            System.out.println("Pernah rawat ICU: " + (pernahDiICU ? "Ya" : "Tidak"));
            System.out.println("Total hari di ICU: " + totalHariICU + " hari");

            // Set checkbox dan textbox
            if (pernahDiICU) {
                rawat_intensif.setSelected(true);
                hari_intensif.setText(String.valueOf(totalHariICU));
                System.out.println("✓ Rawat Intensif diaktifkan dengan " + totalHariICU + " hari");

                // CEK PENGGUNAAN VENTILATOR
                checkVentilator(noRawat);

                // Tampilkan detail ICU (optional)
                tampilkanDetailICU(noRawat);
            } else {
                rawat_intensif.setSelected(false);
                hari_intensif.setText("0");
                ventilator.setSelected(false); // Reset ventilator jika tidak di ICU
                System.out.println("✗ Tidak pernah rawat di ICU");
            }

            rs.close();
            ps.close();
            System.out.println("=======================");

        } catch (Exception e) {
            System.out.println("Error checkRawatICU: " + e);
            e.printStackTrace();
        }
    }

    private void checkVentilator(String noRawat) {
        try {
            System.out.println("--- Cek Penggunaan Ventilator ---");

            // Ambil waktu observasi awal & akhir langsung
            String sql = "SELECT " +
                         "MIN(CONCAT(tgl_perawatan,' ',jam_rawat)) AS awal, " +
                         "MAX(CONCAT(tgl_perawatan,' ',jam_rawat)) AS akhir " +
                         "FROM catatan_observasi_ventilator " +
                         "WHERE no_rawat = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            boolean menggunakanVentilator = false;

            if (rs.next()) {
                String awal = rs.getString("awal");
                String akhir = rs.getString("akhir");

                if (awal != null && akhir != null) {
                    menggunakanVentilator = true;

                    // ====== Set Intubasi (awal) ======
                    LocalDateTime start = LocalDateTime.parse(awal.replace(" ", "T"));
                    tgl_intubasi.setDate(java.sql.Date.valueOf(start.toLocalDate()));
                    jam_intubasi.setSelectedItem(String.format("%02d", start.getHour()));
                    menit_intubasi.setSelectedItem(String.format("%02d", start.getMinute()));

                    // ====== Set Ekstubasi (akhir) ======
                    LocalDateTime stop = LocalDateTime.parse(akhir.replace(" ", "T"));
                    tgl_ekstubasi.setDate(java.sql.Date.valueOf(stop.toLocalDate()));
                    jam_ekstubasi.setSelectedItem(String.format("%02d", stop.getHour()));
                    menit_ekstubasi.setSelectedItem(String.format("%02d", stop.getMinute()));

                    // ====== Hitung total jam (dibulatkan ke atas) ======
                    long durasiMenit = java.time.Duration.between(start, stop).toMinutes();
                    long jamTotal = (long) Math.ceil(durasiMenit / 60.0); // pembulatan ke atas

                    jam_ventilator.setText(String.valueOf(jamTotal));

                    System.out.println("Intubasi : " + awal);
                    System.out.println("Ekstubasi: " + akhir);
                    System.out.println("Durasi   : " + jamTotal + " jam (dibulatkan)");
                }
            }

            // Set checkbox ventilator
            ventilator.setSelected(menggunakanVentilator);
            System.out.println("Menggunakan ventilator: " + (menggunakanVentilator ? "Ya" : "Tidak"));
            System.out.println("---------------------------------");

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error checkVentilator: " + e);
            e.printStackTrace();
        }
    }

    // Method untuk menampilkan detail riwayat ICU (optional - untuk debugging)
    private void tampilkanDetailICU(String noRawat) {
        try {
            String sql = "SELECT kamar_inap.kd_kamar, bangsal.nm_bangsal, " +
                         "kamar_inap.tgl_masuk, kamar_inap.tgl_keluar, " +
                         "kamar_inap.lama, kamar_inap.jam_masuk, kamar_inap.jam_keluar " +
                         "FROM kamar_inap " +
                         "INNER JOIN kamar ON kamar_inap.kd_kamar = kamar.kd_kamar " +
                         "INNER JOIN bangsal ON kamar.kd_bangsal = bangsal.kd_bangsal " +
                         "WHERE kamar_inap.no_rawat = ? " +
                         "AND (bangsal.nm_bangsal LIKE '%ICU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%ICCU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%NICU%' OR " +
                         "     bangsal.nm_bangsal LIKE '%PICU%' OR " +
                         "     bangsal.kd_bangsal LIKE '%ICU%') " +
                         "ORDER BY kamar_inap.tgl_masuk, kamar_inap.jam_masuk";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            System.out.println("--- Detail Riwayat ICU ---");
            int no = 1;
            while (rs.next()) {
                System.out.println(no + ". " + rs.getString("nm_bangsal") + 
                                 " (" + rs.getString("kd_kamar") + ")");
                System.out.println("   Masuk  : " + rs.getString("tgl_masuk") + " " + 
                                 rs.getString("jam_masuk"));
                System.out.println("   Keluar : " + rs.getString("tgl_keluar") + " " + 
                                 rs.getString("jam_keluar"));
                System.out.println("   Lama   : " + rs.getInt("lama") + " hari");
                System.out.println();
                no++;
            }
            System.out.println("--------------------------");

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error tampilkanDetailICU: " + e);
        }
    }
//    
//   // Method untuk cek data TB pasien
//    private void checkDataTB() {
//        try {
//            // Validasi TNoRM sudah terisi (diasumsikan variabel untuk No. RM adalah TNoRM)
//            if (TNoRM.getText().trim().equals("")) {
//                System.out.println("DEBUG: No. RM kosong");
//                return;
//            }
//
//            String noRM = TNoRM.getText().trim();
//            System.out.println("=== DEBUG CEK DATA TB ===");
//            System.out.println("No. RM: " + noRM);
//
//            // Ambil data TB dari label_tb melalui CacheLabelTB
//            CacheLabelTB.DataTB dataTB = CacheLabelTB.getDataTB(noRM);
//
//            if (dataTB != null) {
//                // Cek apakah hasil akhir termasuk yang perlu alert
//                boolean pasienTB = dataTB.hasilAkhir.equals("Belum") || 
//                                  dataTB.hasilAkhir.equals("Lost To Follow Up") || 
//                                  dataTB.hasilAkhir.equals("Gagal") || 
//                                  dataTB.hasilAkhir.equals("Pindah");
//
//                if (pasienTB) {
//                    check_tb.setSelected(true);
//                    pasien_tb.setText(dataTB.idTB03 != null ? dataTB.idTB03 : "");
//                    System.out.println("✓ Data TB ditemukan!");
//                    System.out.println("✓ Hasil Akhir: " + dataTB.hasilAkhir);
//                    System.out.println("✓ ID TB 03: " + dataTB.idTB03);
//                    System.out.println("✓ Checkbox TB diaktifkan");
//                } else {
//                    check_tb.setSelected(false);
//                    pasien_tb.setText("");
//                    System.out.println("✗ Pasien TB sudah sembuh/selesai: " + dataTB.hasilAkhir);
//                }
//            } else {
//                check_tb.setSelected(false);
//                pasien_tb.setText("");
//                System.out.println("✗ Tidak ada data TB untuk pasien ini");
//            }
//
//            System.out.println("=========================");
//        } catch (Exception e) {
//            System.out.println("Error checkDataTB: " + e);
//            e.printStackTrace();
//        }
//    }
    
    private void saveTarifData() {
        try {

            if (no_rawat.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "No Rawat tidak boleh kosong!");
                return;
            }

            String noRawat = no_rawat.getText().trim();
            System.out.println("=== SIMPAN DATA TARIF ===");
            System.out.println("No Rawat: " + noRawat);

            // Cek apakah data sudah ada
            String sqlCheck = "SELECT COUNT(*) as jumlah FROM tarif_data WHERE no_rawat = ?";
            PreparedStatement psCheck = koneksi.prepareStatement(sqlCheck);
            psCheck.setString(1, noRawat);
            ResultSet rsCheck = psCheck.executeQuery();

            boolean dataAda = false;
            if (rsCheck.next()) {
                dataAda = rsCheck.getInt("jumlah") > 0;
            }
            rsCheck.close();
            psCheck.close();

            if (dataAda) {
                // UPDATE
                String sqlUpdate = 
                    "UPDATE tarif_data SET " +
                    "prosedur_non_bedah = ?, prosedur_bedah = ?, konsultasi = ?, " +
                    "tenaga_ahli = ?, keperawatan = ?, penunjang = ?, " +
                    "radiologi = ?, laboratorium = ?, pelayanan_darah = ?, " +
                    "rehabilitasi = ?, kamar = ?, rawat_intensif = ?, " +
                    "obat = ?, obat_kronis = ?, obat_kemoterapi = ?, " +
                    "alkes = ?, bmhp = ?, sewa_alat = ?, " +
                    "updated_at = NOW() " +
                    "WHERE no_rawat = ?";

                PreparedStatement psUpdate = koneksi.prepareStatement(sqlUpdate);
                psUpdate.setString(1, cleanNumeric(pnb.getText()));
                psUpdate.setString(2, cleanNumeric(pb.getText()));
                psUpdate.setString(3, cleanNumeric(ksl.getText()));
                psUpdate.setString(4, cleanNumeric(ahli.getText()));
                psUpdate.setString(5, cleanNumeric(kpr.getText()));
                psUpdate.setString(6, cleanNumeric(pnj.getText()));
                psUpdate.setString(7, cleanNumeric(rad.getText()));
                psUpdate.setString(8, cleanNumeric(lab.getText()));
                psUpdate.setString(9, cleanNumeric(pl_darah.getText()));
                psUpdate.setString(10, cleanNumeric(rehab.getText()));
                psUpdate.setString(11, cleanNumeric(akomodasi.getText()));
                psUpdate.setString(12, cleanNumeric(ri.getText()));
                psUpdate.setString(13, cleanNumeric(obat.getText()));
                psUpdate.setString(14, cleanNumeric(kronis.getText()));
                psUpdate.setString(15, cleanNumeric(obat_kemo.getText()));
                psUpdate.setString(16, cleanNumeric(alkes.getText()));
                psUpdate.setString(17, cleanNumeric(bmhp.getText()));
                psUpdate.setString(18, cleanNumeric(sewa_alat.getText()));
                psUpdate.setString(19, noRawat);

                int rowsUpdated = psUpdate.executeUpdate();
                psUpdate.close();

                if (rowsUpdated > 0) {
                    System.out.println("✓ Data tarif berhasil diupdate");
                }
            } else {
                // INSERT
                String sqlInsert = 
                    "INSERT INTO tarif_data " +
                    "(no_rawat, prosedur_non_bedah, prosedur_bedah, konsultasi, " +
                    "tenaga_ahli, keperawatan, penunjang, radiologi, laboratorium, " +
                    "pelayanan_darah, rehabilitasi, kamar, rawat_intensif, " +
                    "obat, obat_kronis, obat_kemoterapi, alkes, bmhp, sewa_alat) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement psInsert = koneksi.prepareStatement(sqlInsert);
                psInsert.setString(1, noRawat);
                psInsert.setString(2, cleanNumeric(pnb.getText()));
                psInsert.setString(3, cleanNumeric(pb.getText()));
                psInsert.setString(4, cleanNumeric(ksl.getText()));
                psInsert.setString(5, cleanNumeric(ahli.getText()));
                psInsert.setString(6, cleanNumeric(kpr.getText()));
                psInsert.setString(7, cleanNumeric(pnj.getText()));
                psInsert.setString(8, cleanNumeric(rad.getText()));
                psInsert.setString(9, cleanNumeric(lab.getText()));
                psInsert.setString(10, cleanNumeric(pl_darah.getText()));
                psInsert.setString(11, cleanNumeric(rehab.getText()));
                psInsert.setString(12, cleanNumeric(akomodasi.getText()));
                psInsert.setString(13, cleanNumeric(ri.getText()));
                psInsert.setString(14, cleanNumeric(obat.getText()));
                psInsert.setString(15, cleanNumeric(kronis.getText()));
                psInsert.setString(16, cleanNumeric(obat_kemo.getText()));
                psInsert.setString(17, cleanNumeric(alkes.getText()));
                psInsert.setString(18, cleanNumeric(bmhp.getText()));
                psInsert.setString(19, cleanNumeric(sewa_alat.getText()));

                int rowsInserted = psInsert.executeUpdate();
                psInsert.close();

                if (rowsInserted > 0) {
                    System.out.println("✓ Data tarif berhasil disimpan");
                }
            }

            System.out.println("=========================");

        } catch (Exception e) {
            System.out.println("Error saveTarifData: " + e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data tarif!\nError: " + e.getMessage());
        }
    }

    private String cleanNumeric(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "0";
        }
        String cleaned = value.trim().replaceAll("[^0-9.]", "");
        return cleaned.isEmpty() ? "0" : cleaned;
    }
    
    private boolean loadTarifData() {
        try {
            if (no_rawat.getText().trim().equals("")) {
                System.out.println("DEBUG: No Rawat kosong");
                return false;
            }

            String noRawat = no_rawat.getText().trim();
            System.out.println("=== LOAD DATA TARIF ===");
            System.out.println("No Rawat: " + noRawat);

            String sql = "SELECT * FROM tarif_data WHERE no_rawat = ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Langsung mapping kolom ke textbox
                pnb.setText(rs.getString("prosedur_non_bedah"));
                pb.setText(rs.getString("prosedur_bedah"));
                ksl.setText(rs.getString("konsultasi"));
                ahli.setText(rs.getString("tenaga_ahli"));
                kpr.setText(rs.getString("keperawatan"));
                pnj.setText(rs.getString("penunjang"));
                rad.setText(rs.getString("radiologi"));
                lab.setText(rs.getString("laboratorium"));
                pl_darah.setText(rs.getString("pelayanan_darah"));
                rehab.setText(rs.getString("rehabilitasi"));
                akomodasi.setText(rs.getString("kamar"));
                ri.setText(rs.getString("rawat_intensif"));
                obat.setText(rs.getString("obat"));
                kronis.setText(rs.getString("obat_kronis"));
                obat_kemo.setText(rs.getString("obat_kemoterapi"));
                alkes.setText(rs.getString("alkes"));
                bmhp.setText(rs.getString("bmhp"));
                sewa_alat.setText(rs.getString("sewa_alat"));

                System.out.println("✓ Data tarif berhasil dimuat dari tarif_data");

                rs.close();
                ps.close();
                System.out.println("=======================");

                return true; // Data ditemukan

            } else {
                System.out.println("✗ Data tarif tidak ditemukan di tarif_data");

                rs.close();
                ps.close();
                System.out.println("=======================");

                return false; // Data tidak ditemukan
            }

        } catch (Exception e) {
            System.out.println("Error loadTarifData: " + e);
            e.printStackTrace();
            return false; // Error dianggap data tidak ditemukan
        }
    }

    private void resetTarifFields() {
        pnb.setText("0");
        pb.setText("0");
        ksl.setText("0");
        ahli.setText("0");
        kpr.setText("0");
        pnj.setText("0");
        rad.setText("0");
        lab.setText("0");
        pl_darah.setText("0");
        rehab.setText("0");
        akomodasi.setText("0");
        ri.setText("0");
        obat.setText("0");
        kronis.setText("0");
        obat_kemo.setText("0");
        alkes.setText("0");
        bmhp.setText("0");
        sewa_alat.setText("0");
    }
    
    public void hitungtotalbiaya(){
        if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pb.requestFocus();
        }        
        else if (rad.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rad.requestFocus();
        }
        else if (rehab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rehab.requestFocus();
        }
        else if (obat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat.requestFocus();
        }
        else if (alkes.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            alkes.requestFocus();
        }
        else if (kpr.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kpr.requestFocus();
        }
        else if (lab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            lab.requestFocus();
        }
        else if (akomodasi.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            akomodasi.requestFocus();
        }
        else if (kronis.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kronis.requestFocus();
        }
        else if (bmhp.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            bmhp.requestFocus();
        }
        else if (ksl.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ksl.requestFocus();
        }
        else if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pnj.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnj.requestFocus();
        }
        else if (pl_darah.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pl_darah.requestFocus();
        }
        else if (ri.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ri.requestFocus();
        }
        else if (obat_kemo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat_kemo.requestFocus();
        }
        else if (sewa_alat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            sewa_alat.requestFocus();
        } 
        else if (ahli.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ahli.requestFocus();
        }else {
            total_biaya = (Integer.parseInt(pnb.getText()) + Integer.parseInt(pb.getText()) + Integer.parseInt(ksl.getText()) + Integer.parseInt(rad.getText()) + Integer.parseInt(kpr.getText()) +  Integer.parseInt(rad.getText()) + Integer.parseInt(lab.getText()) + Integer.parseInt(alkes.getText()) + Integer.parseInt(kronis.getText()) + Integer.parseInt(obat_kemo.getText()) + Integer.parseInt(obat.getText()) + Integer.parseInt(pnj.getText()) + Integer.parseInt(pl_darah.getText()) + Integer.parseInt(bmhp.getText()) + Integer.parseInt(sewa_alat.getText()) + Integer.parseInt(rehab.getText()) + Integer.parseInt(ri.getText()) + Integer.parseInt(akomodasi.getText()) + Integer.parseInt(ahli.getText()) );
            tarifRS.setText(String.valueOf(Valid.SetAngka(total_biaya)));
        }
    }
    
    private String mapOnsetKontraksi(String selectedValue) {
        if (selectedValue == null || selectedValue.trim().isEmpty()) {
            return "";
        }

        String value = selectedValue.toLowerCase().trim();

        switch (value) {
            // Mapping ke "spontan" (untuk web service)
            case "timbul spontan":
            case "spontan":
            case "kontraksi spontan":
            case "onset spontan":
                return "spontan";

            // Mapping ke "induksi" (untuk web service)
            case "dengan induksi":
            case "induksi":
            case "induksi persalinan":
            case "diinduksi":
                return "induksi";

            // Mapping ke "non_spontan_non_induksi" (untuk web service)
            case "sc tanpa kontraksi/induksi":
            case "sc":
            case "sectio caesarea":
            case "caesar":
            case "sc tanpa kontraksi":
            case "non spontan non induksi":
                return "non_spontan_non_induksi";

            default:
                System.out.println("WARNING: Onset kontraksi tidak dikenali: '" + selectedValue + "'");
                return selectedValue;
        }
    }
    
    private void idrg_diagnosa(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"idrg_diagnosa_set\",\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"diagnosa\":\""+icd10.trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response idrg1 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void idrg_prosedur(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            if (kd_psdr_1.getText().equals("")) {
                System.out.println("Tidak Ada Prosedur");
            }else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"idrg_procedure_set\",\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"procedure\":\""+icd9.trim()+"\"\n" +
                                            "}\n" +
                                        "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response idrg prosedur 1 : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void tombolinacbg(){
        String cek_grouper_idrg = Sequel.cariIsi("select * from idrg_grouper where no_sep=?",Tsep.getText().trim());
        String cek_grouper_ina1 = Sequel.cariIsi("select * from inacbg_grouping_stage1 where no_sep=?",Tsep.getText().trim());
        String cek_final_idrg = Sequel.cariIsi("select * from inacbg_idrg_final where no_sep=?",Tsep.getText().trim());
        String cek_final_ina = Sequel.cariIsi("select * from inacbg_final where no_sep=?",Tsep.getText().trim());
        String cek_final = Sequel.cariIsi("select * from inacbg_grouping_final where no_sep=?",Tsep.getText().trim());
        String gagal_idrg = Sequel.cariIsi("select mdc_desc from idrg_grouper where no_sep=?",Tsep.getText().trim());
        String gagal_ina = Txtgroup.getText().trim();
        if (cek_grouper_idrg.trim().equals("")) {
            TabRawat.setSelectedIndex(0);
            BtnGrouperIDRG.setVisible(true);
            finalklaimIDRG.setVisible(false);
            editKlaimIDRG.setVisible(false);
            Import.setVisible(false);
            BtnGrouperINA.setVisible(false);
            editKlaim.setVisible(false);
            finalklaim.setVisible(false);
            editINA.setVisible(false);
            finalINA.setVisible(false);
            kirimonline.setVisible(false);
            CetakKlaim.setVisible(false);
            
            for (int i = 0; i < h_diag.length; i++) {
                h_diag[i].setEnabled(true);
            }
            
            for (int i = 0; i < h_diag2.length; i++) {
                h_diag2[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_psdr.length; i++) {
                h_psdr[i].setEnabled(true);
            }
            
            for (int i = 0; i < h_psdr2.length; i++) {
                h_psdr2[i].setEnabled(false);
            }
            
            for (int i = 0; i < chk_diag.length; i++) {
                chk_diag[i].setEnabled(true);
            }
            
            for (int i = 0; i < chk_diag2.length; i++) {
                chk_diag2[i].setEnabled(false);
            }
            
            INAdiagnosatambah.setEnabled(false);
            INAprosedurtambah.setEnabled(false);
            idrgPenyakittambah.setEnabled(true);
            idrgProseduttambah.setEnabled(true);
            
            cmbProc.setEnabled(false);
            cmbDrugs.setEnabled(false);
            cmbPros.setEnabled(false);
            cmbInv.setEnabled(false);
            cmbSelisih.setEnabled(false);
            
        } else if (!cek_grouper_idrg.trim().equals("") && cek_final_idrg.trim().equals("")) {
            TabRawat.setSelectedIndex(0);
            BtnGrouperIDRG.setVisible(true);
            if (gagal_idrg.trim().toLowerCase().contains("ungroupable") || 
                    gagal_idrg.trim().toLowerCase().contains("unrelated") || 
                    gagal_idrg.trim().toLowerCase().contains("gagal") || 
                    gagal_idrg.trim().toLowerCase().contains("error") || 
                    gagal_idrg.trim().toLowerCase().contains("failed") || 
                    gagal_idrg.trim().toLowerCase().contains("invalid")) {
                finalklaimIDRG.setVisible(false);
            } else {
                finalklaimIDRG.setVisible(true);
            }
            editKlaimIDRG.setVisible(false);
            Import.setVisible(false);
            BtnGrouperINA.setVisible(false);
            editKlaim.setVisible(false);
            finalklaim.setVisible(false);
            editINA.setVisible(false);
            finalINA.setVisible(false);
            kirimonline.setVisible(false);
            CetakKlaim.setVisible(false);
            
            cmbProc.setEnabled(false);
            cmbDrugs.setEnabled(false);
            cmbPros.setEnabled(false);
            cmbInv.setEnabled(false);
            cmbSelisih.setEnabled(false);
            
        } else if (!cek_final_idrg.trim().equals("") && cek_grouper_ina1.trim().equals("")) {
            TabRawat.setSelectedIndex(1);
            BtnGrouperIDRG.setVisible(false);
            finalklaimIDRG.setVisible(false);
            editKlaimIDRG.setVisible(true);
            Import.setVisible(true);
            BtnGrouperINA.setVisible(true);
            editKlaim.setVisible(false);
            finalklaim.setVisible(false);
            editINA.setVisible(false);
            finalINA.setVisible(false);
            kirimonline.setVisible(false);
            CetakKlaim.setVisible(false);
            
            for (int i = 0; i < h_diag.length; i++) {
                h_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_diag2.length; i++) {
                h_diag2[i].setEnabled(true);
            }
            
            for (int i = 0; i < h_psdr.length; i++) {
                h_psdr[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_psdr2.length; i++) {
                h_psdr2[i].setEnabled(true);
            }
            
            for (int i = 0; i < chk_diag.length; i++) {
                chk_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < chk_diag2.length; i++) {
                chk_diag2[i].setEnabled(true);
            }
            
            INAdiagnosatambah.setEnabled(true);
            INAprosedurtambah.setEnabled(true);
            idrgPenyakittambah.setEnabled(false);
            idrgProseduttambah.setEnabled(false);
            
            cmbProc.setEnabled(false);
            cmbDrugs.setEnabled(false);
            cmbPros.setEnabled(false);
            cmbInv.setEnabled(false);
            cmbSelisih.setEnabled(false);
            
        } else if (!cek_grouper_ina1.trim().equals("") && cek_final_ina.trim().equals("")) {
            TabRawat.setSelectedIndex(1);
            BtnGrouperIDRG.setVisible(false);
            finalklaimIDRG.setVisible(false);
            editKlaimIDRG.setVisible(true);
            Import.setVisible(true);
            BtnGrouperINA.setVisible(true);
            finalklaim.setVisible(false);
            editKlaim.setVisible(false);
            editINA.setVisible(false);
            kirimonline.setVisible(false);
            CetakKlaim.setVisible(false);
            
            if (gagal_ina.trim().toLowerCase().contains("ungroupable") || 
                    gagal_idrg.trim().toLowerCase().contains("unrelated") || 
                    gagal_idrg.trim().toLowerCase().contains("gagal") || 
                    gagal_idrg.trim().toLowerCase().contains("error") || 
                    gagal_idrg.trim().toLowerCase().contains("failed") || 
                    gagal_idrg.trim().toLowerCase().contains("invalid")) {
                finalINA.setVisible(false);
            } else {
                finalINA.setVisible(true);
            }
            
            for (int i = 0; i < h_diag.length; i++) {
                h_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_psdr.length; i++) {
                h_psdr[i].setEnabled(false);
            }
            
            for (i = 0; i < h_diag2.length; i++) {
                h_diag2[i].setEnabled(true);
            }
            
            for (i = 0; i < h_psdr2.length; i++) {
                h_psdr2[i].setEnabled(true);
            }
            
            for (int i = 0; i < chk_diag.length; i++) {
                chk_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < chk_diag2.length; i++) {
                chk_diag2[i].setEnabled(true);
            }
            
            INAdiagnosatambah.setEnabled(true);
            INAprosedurtambah.setEnabled(true);
            idrgPenyakittambah.setEnabled(false);
            idrgProseduttambah.setEnabled(false);
            
            cmbProc.setEnabled(true);
            cmbDrugs.setEnabled(true);
            cmbPros.setEnabled(true);
            cmbInv.setEnabled(true);
            cmbSelisih.setEnabled(true);
            
        } else if (!cek_grouper_ina1.trim().equals("") && !cek_final_ina.trim().equals("") && cek_final.trim().equals("")) {
            TabRawat.setSelectedIndex(1);
            BtnGrouperIDRG.setVisible(false);
            finalklaimIDRG.setVisible(false);
            editKlaimIDRG.setVisible(true);
            Import.setVisible(false);
            BtnGrouperINA.setVisible(false);
            finalklaim.setVisible(true);
            editKlaim.setVisible(false);
            finalINA.setVisible(false);
            editINA.setVisible(true);
            kirimonline.setVisible(false);
            CetakKlaim.setVisible(false);
            
            for (int i = 0; i < h_diag.length; i++) {
                h_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_psdr.length; i++) {
                h_psdr[i].setEnabled(false);
            }
            
            for (i = 0; i < h_diag2.length; i++) {
                h_diag2[i].setEnabled(false);
            }
            
            for (i = 0; i < h_psdr2.length; i++) {
                h_psdr2[i].setEnabled(false);
            }
            for (int i = 0; i < chk_diag.length; i++) {
                chk_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < chk_diag2.length; i++) {
                chk_diag2[i].setEnabled(false);
            }
            
            INAdiagnosatambah.setEnabled(false);
            INAprosedurtambah.setEnabled(false);
            idrgPenyakittambah.setEnabled(false);
            idrgProseduttambah.setEnabled(false);
            
            cmbProc.setEnabled(false);
            cmbDrugs.setEnabled(false);
            cmbPros.setEnabled(false);
            cmbInv.setEnabled(false);
            cmbSelisih.setEnabled(false);
            
        } else if (!cek_final.trim().equals("")) {
            
            for (int i = 0; i < h_diag.length; i++) {
                h_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < h_psdr.length; i++) {
                h_psdr[i].setEnabled(false);
            }
            
            for (i = 0; i < h_diag2.length; i++) {
                h_diag2[i].setEnabled(false);
            }
            
            for (i = 0; i < h_psdr2.length; i++) {
                h_psdr2[i].setEnabled(false);
            }
            for (int i = 0; i < chk_diag.length; i++) {
                chk_diag[i].setEnabled(false);
            }
            
            for (int i = 0; i < chk_diag2.length; i++) {
                chk_diag2[i].setEnabled(false);
            }
            
            INAdiagnosatambah.setEnabled(false);
            INAprosedurtambah.setEnabled(false);
            idrgPenyakittambah.setEnabled(false);
            idrgProseduttambah.setEnabled(false);
            
            cmbProc.setEnabled(false);
            cmbDrugs.setEnabled(false);
            cmbPros.setEnabled(false);
            cmbInv.setEnabled(false);
            cmbSelisih.setEnabled(false);
            
            TabRawat.setSelectedIndex(1);
            BtnGrouperIDRG.setVisible(false);
            finalklaimIDRG.setVisible(false);
            editKlaimIDRG.setVisible(false);
            Import.setVisible(false);
            BtnGrouperINA.setVisible(false);
            finalklaim.setVisible(false);
            editKlaim.setVisible(true);
            finalINA.setVisible(false);
            editINA.setVisible(false);
            kirimonline.setVisible(true);
            CetakKlaim.setVisible(true);
        } 
        panelBawah.revalidate();
        panelBawah.repaint();
    }
    
    private void grouperidrg() {
        responIDRG.setText("");

        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                String requestJson = "{\n" +
                        "\"metadata\": {\n" +
                            "\"method\":\"grouper\",\n" +
                            "\"stage\":\"1\",\n" +
                            "\"grouper\":\"idrg\"\n" +
                        "},\n" +
                        "\"data\": {\n" +
                            "\"nomor_sep\":\"" + Tsep.getText().trim() + "\"\n" +
                        "}\n" +
                        "}";

                System.out.println("request JSON : " + requestJson);
                JSONObject response = inacbg.request(requestJson);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response grouper idrg : " + response);

                if (response.has("metadata")) {
                    if (code.getInt("code") == 200) {
                        Sequel.meghapus("idrg_grouper", "no_sep", Tsep.getText());
                        JSONObject idrg = response.getJSONObject("response_idrg");

                        // Hitung total klaim stage 1 (tanpa top-up)
                        double cw1 = 0.0;
                        double nbr1 = 0.0;
                        try { cw1  = Double.parseDouble(idrg.optString("cost_weight", "0")); } catch (NumberFormatException ignored) {}
                        try { nbr1 = Double.parseDouble(idrg.optString("nbr", "0")); }         catch (NumberFormatException ignored) {}
                        double totalKlaim1 = Math.ceil(nbr1 * cw1);

                        responIDRG.setText(
                            "<html>" +
                            "JENIS RAWAT : " + jenis_rawat.getSelectedItem().toString() + "<br><br>" +
                            "MDC : " + idrg.getString("mdc_description") + " (" + idrg.getString("mdc_number") + ")<br><br>" +
                            "DRG : " + idrg.getString("drg_description") + " (" + idrg.getString("drg_code") + ")<br>" +
                            "DRG Cost Weight : " + idrg.optString("cost_weight", "-") + "<br><br>" +
                            "<b>Total Cost Weight : " + String.format("%.2f", cw1) + "</b><br>" +
                            "NBR : " + String.format("%,.0f", nbr1) + "<br>" +
                            "<b>Total Klaim : Rp " + String.format("%,.0f", totalKlaim1) + "</b>" +
                            "</html>"
                        );

                        Sequel.menyimpantf2("idrg_grouper", "?,?,?,?,?", "GROUPER IDRG", 5, new String[]{
                            Tsep.getText(),
                            idrg.getString("mdc_number"),
                            idrg.getString("mdc_description"),
                            idrg.getString("drg_code"),
                            idrg.getString("drg_description")
                        });

                        // Tawarkan top-up jika ada opsi eligible
                        if (idrg.has("topup_options")) {
                            org.json.JSONArray options = idrg.getJSONArray("topup_options");
                            if (options.length() > 0) {
                                String[] labels = new String[options.length() + 1];
                                String[] codes  = new String[options.length() + 1];
                                labels[0] = "(Tidak Pakai Top-Up)";
                                codes[0]  = "";
                                for (int k = 0; k < options.length(); k++) {
                                    org.json.JSONObject opt = options.getJSONObject(k);
                                    labels[k + 1] = opt.optString("description", "?") + " ["
                                                  + opt.optString("type", "?") + ", CW "
                                                  + opt.optString("cost_weight", "?") + "]";
                                    codes[k + 1]  = opt.optString("code", "");
                                }
                                String pick = (String) JOptionPane.showInputDialog(null,
                                    "Pilih Top-Up iDRG (kasus eligible terdeteksi)\n"
                                  + "DRG: " + idrg.getString("drg_description") + " (CW " + idrg.optString("cost_weight", "-") + ")",
                                    "Top-Up iDRG", JOptionPane.QUESTION_MESSAGE,
                                    null, labels, labels[0]);
                                if (pick != null && !pick.equals(labels[0])) {
                                    for (int k = 0; k < labels.length; k++) {
                                        if (labels[k].equals(pick)) {
                                            grouperidrg2(codes[k]);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        finalklaimIDRG.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "RESPON : " + code.getString("message"));
                    }
                } else {
                    System.out.println("response : " + response);
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error idrg : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void grouperidrg2(String topupCodes) {
        if (Tsep.getText().trim().isEmpty()) return;
        try {
            String requestJson = "{\n"
                    + "\"metadata\": {\n"
                    + "\"method\":\"grouper\",\n"
                    + "\"stage\":\"2\",\n"
                    + "\"grouper\":\"idrg\"\n"
                    + "},\n"
                    + "\"data\": {\n"
                    + "\"nomor_sep\":\"" + Tsep.getText().trim() + "\",\n"
                    + "\"topup_codes\":\"" + topupCodes + "\"\n"
                    + "}\n"
                    + "}";
            System.out.println("request JSON (iDRG Stage 2 Top-Up) : " + requestJson);
            JSONObject response = inacbg.request(requestJson);
            System.out.println("response iDRG Stage 2 : " + response);

            JSONObject meta = response.getJSONObject("metadata");
            if (meta.getInt("code") != 200) {
                JOptionPane.showMessageDialog(null, "RESPON Top-Up iDRG : " + meta.getString("message"));
                return;
            }
            JSONObject idrg = response.getJSONObject("response_idrg");

            // Hitung total cost weight: DRG base + semua topup
            double drgCw = 0.0;
            double nbr   = 0.0;
            try { drgCw = Double.parseDouble(idrg.optString("cost_weight", "0")); } catch (NumberFormatException ignored) {}
            try { nbr   = Double.parseDouble(idrg.optString("nbr", "0")); }         catch (NumberFormatException ignored) {}

            double totalCw = drgCw;
            org.json.JSONArray topups = idrg.has("topup") ? idrg.getJSONArray("topup") : new org.json.JSONArray();
            for (int k = 0; k < topups.length(); k++) {
                try {
                    totalCw += Double.parseDouble(topups.getJSONObject(k).optString("cost_weight", "0"));
                } catch (NumberFormatException ignored) {}
            }

            double totalKlaim = Math.ceil(nbr * totalCw);

            // Dialog summary
            StringBuilder summary = new StringBuilder(
                "<html>"
              + "<b>Top-Up iDRG berhasil di-apply.</b><br><br>"
              + "DRG: " + idrg.optString("drg_description", "-") + "<br>"
              + "DRG Cost Weight: " + idrg.optString("cost_weight", "-") + "<br>");
            for (int k = 0; k < topups.length(); k++) {
                org.json.JSONObject t = topups.getJSONObject(k);
                summary.append("Top-Up ").append(t.optString("type", "-")).append(": ")
                       .append(t.optString("description", "-"))
                       .append(" (CW ").append(t.optString("cost_weight", "-")).append(")<br>");
            }
            summary.append("Total Cost Weight: ").append(String.format("%.2f", totalCw)).append("<br>")
                   .append("NBR: ").append(String.format("%,.0f", nbr)).append("<br>")
                   .append("<b>Total Klaim: Rp ").append(String.format("%,.0f", totalKlaim)).append("</b></html>");
            JOptionPane.showMessageDialog(null, summary.toString(), "Top-Up iDRG", JOptionPane.INFORMATION_MESSAGE);

            // Update responIDRG
            StringBuilder responText = new StringBuilder(
                "<html>"
              + "JENIS RAWAT : " + jenis_rawat.getSelectedItem().toString() + "<br><br>"
              + "MDC : " + idrg.optString("mdc_description", "-") + " (" + idrg.optString("mdc_number", "-") + ")<br><br>"
              + "DRG : " + idrg.optString("drg_description", "-") + " (" + idrg.optString("drg_code", "-") + ")<br>"
              + "DRG Cost Weight : " + idrg.optString("cost_weight", "-") + "<br><br>");
            for (int k = 0; k < topups.length(); k++) {
                org.json.JSONObject t = topups.getJSONObject(k);
                responText.append("Top-Up [").append(t.optString("type", "-")).append("] : ")
                          .append(t.optString("description", "-"))
                          .append(" (CW ").append(t.optString("cost_weight", "-")).append(")<br>");
            }
            responText.append("<br>")
                      .append("<b>Total Cost Weight : ").append(String.format("%.2f", totalCw)).append("</b><br>")
                      .append("NBR : ").append(String.format("%,.0f", nbr)).append("<br>")
                      .append("<b>Total Klaim : Rp ").append(String.format("%,.0f", totalKlaim)).append("</b>")
                      .append("</html>");
            responIDRG.setText(responText.toString());

            // Simpan tracking top-up (idempotent)
            Sequel.meghapus("idrg_topup", "no_sep", Tsep.getText());
            for (int k = 0; k < topups.length(); k++) {
                org.json.JSONObject t = topups.getJSONObject(k);
                Sequel.menyimpantf2("idrg_topup", "?,?,?,?,?", "TOP-UP IDRG", 5, new String[]{
                    Tsep.getText(),
                    t.optString("code", ""),
                    t.optString("description", ""),
                    t.optString("type", ""),
                    t.optString("cost_weight", "")
                });
            }

        } catch (Exception e) {
            System.err.println("Error grouperidrg2 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Kirim id_encounter SatuSehat ke E-Klaim untuk validasi bridging RME.
     * Method: satusehat_encounter_set
     *
     * Data: encounters[] = array UUID id_encounter dari tabel satu_sehat_encounter (kalau pasien
     * sudah punya >1 kunjungan terbridge, semuanya dikirim).
     *
     * Response success: data[].valid=true + statistic count per resourceType (NutritionOrder,
     * Observation, Procedure, dll) — proof bahwa encounter punya cukup data klinis di SatuSehat.
     */
    private void satusehatEncounterSet() {
        if (Tsep.getText().trim().isEmpty() || no_rawat.getText().trim().isEmpty()) {
            lblStatusEncounter.setText("Encounter: SEP / No.Rawat kosong");
            lblStatusEncounter.setForeground(new java.awt.Color(180, 0, 0));
            return;
        }

        // Lookup id_encounter dari satu_sehat_encounter sesuai no_rawat
        java.util.List<String> encounters = new java.util.ArrayList<>();
        try (java.sql.PreparedStatement ps = fungsi.koneksiDB.condb().prepareStatement(
                "select id_encounter from satu_sehat_encounter " +
                "where no_rawat=? and ifnull(id_encounter,'') <> ''")) {
            ps.setString(1, no_rawat.getText().trim());
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) encounters.add(rs.getString(1));
            rs.close();
        } catch (Exception e) {
            System.err.println("Notif lookup satu_sehat_encounter : " + e);
            lblStatusEncounter.setText("Encounter: gagal cek DB");
            lblStatusEncounter.setForeground(new java.awt.Color(180, 0, 0));
            return;
        }

        if (encounters.isEmpty()) {
            lblStatusEncounter.setText("Encounter: belum ada di SatuSehat");
            lblStatusEncounter.setForeground(new java.awt.Color(204, 102, 0));  // oranye
            return;
        }

        // Kirim ke E-Klaim
        StringBuilder encArr = new StringBuilder();
        for (int k = 0; k < encounters.size(); k++) {
            if (k > 0) encArr.append(",");
            encArr.append("\"").append(encounters.get(k)).append("\"");
        }

        try {
            String requestJson = "{\n"
                    + "\"metadata\": {\n"
                    + "\"method\":\"satusehat_encounter_set\",\n"
                    + "\"nomor_sep\":\"" + Tsep.getText().trim() + "\"\n"
                    + "},\n"
                    + "\"data\": {\n"
                    + "\"encounters\": [" + encArr + "]\n"
                    + "}\n"
                    + "}";
            System.out.println("request JSON (SatuSehat Encounter Set) : " + requestJson);
            JSONObject response = inacbg.request(requestJson);
            System.out.println("response SatuSehat Encounter Set : " + response);

            JSONObject meta = response.getJSONObject("metadata");
            if (meta.getInt("code") != 200) {
                lblStatusEncounter.setText("Encounter: " + meta.optString("message", "gagal"));
                lblStatusEncounter.setForeground(new java.awt.Color(180, 0, 0));
                return;
            }

            // Hitung total resource dari statistic
            int totalValid   = 0;
            int totalInvalid = 0;
            int totalRes     = 0;
            if (response.has("data")) {
                org.json.JSONArray data = response.getJSONArray("data");
                for (int k = 0; k < data.length(); k++) {
                    org.json.JSONObject enc = data.getJSONObject(k);
                    if (enc.optBoolean("valid", false)) totalValid++;
                    else totalInvalid++;
                    if (enc.has("statistic")) {
                        org.json.JSONArray stat = enc.getJSONArray("statistic");
                        for (int j = 0; j < stat.length(); j++) {
                            totalRes += stat.getJSONObject(j).optInt("count", 0);
                        }
                    }
                }
            }

            // Tampilkan ringkasan di label — tanpa dialog
            String encCount = encounters.size() + " encounter";
            lblStatusEncounter.setText(
                "Encounter: " + encCount
                + " | valid=" + totalValid
                + " invalid=" + totalInvalid
                + " | " + totalRes + " resource"
            );
            lblStatusEncounter.setForeground(
                totalInvalid > 0
                    ? new java.awt.Color(204, 102, 0)   // oranye jika ada invalid
                    : new java.awt.Color(0, 153, 0)     // hijau jika semua valid
            );

            // Simpan tracking
            Sequel.meghapus("inacbg_satusehat_encounter", "no_sep", Tsep.getText());
            for (String idEnc : encounters) {
                Sequel.menyimpantf2("inacbg_satusehat_encounter", "?,?",
                    "INACBG-SATUSEHAT", 2,
                    new String[]{Tsep.getText(), idEnc});
            }

        } catch (Exception e) {
            System.err.println("Error satusehatEncounterSet : " + e.getMessage());
            lblStatusEncounter.setText("Encounter: error koneksi E-Klaim");
            lblStatusEncounter.setForeground(new java.awt.Color(180, 0, 0));
        }
    }

    private void idrgtoinacbg() {
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"idrg_to_inacbg_import\"\n" +    
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response import idrg to inacbg : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        System.out.println("response import idrg : "+code.getString("message"));
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }
                
                if (response.has("data")) {
                    JSONObject data = response.getJSONObject("data");
                    
                    // Ambil diagnosa
                    JSONObject diagnosa = data.getJSONObject("diagnosa");
                    String diagnosaString = diagnosa.getString("string");
                    JSONArray diagnosaExpanded = diagnosa.getJSONArray("expanded");

                    // Ambil prosedur
                    JSONObject prosedur = data.getJSONObject("procedure");
                    String prosedurString = prosedur.getString("string");
                    JSONArray prosedurExpanded = prosedur.getJSONArray("expanded");
                    
                    System.out.println("Diagnosa: " + diagnosaString);
                    for (int i = 0; i < diagnosaExpanded.length(); i++) {
                        JSONObject dx = diagnosaExpanded.getJSONObject(i);
                        if (dx.getString("validcode").equals("0")) {
                            JOptionPane.showMessageDialog(null,"DIAGNOSA "+dx.getString("code")+" "+dx.getString("display")+" (TIDAK BERLAKU)");
                        }else{
                            kd_diag2[i].setText(dx.getString("code"));
                            nm_diag2[i].setText(dx.getString("display"));
                            System.out.println("  - " + dx.getString("code") + " : " + dx.getString("display"));
                        }
                    }

                    System.out.println("Prosedur: " + prosedurString);
                    for (int i = 0; i < prosedurExpanded.length(); i++) {
                        JSONObject pr = prosedurExpanded.getJSONObject(i);
                        if (pr.getString("validcode").equals("0")) {
                            JOptionPane.showMessageDialog(null,"DIAGNOSA "+pr.getString("code")+" "+pr.getString("display")+" (IM TIDAK BERLAKU)");
                        }else{
                            kd_psdr2[i].setText(pr.getString("code"));
                            nm_psdr2[i].setText(pr.getString("display"));
                            System.out.println("  - " + pr.getString("code") + " : " + pr.getString("display"));
                        }
                    }
                    
                    for (i = 0; i < kd_diag2.length ; i++) {
                        if (!kd_diag2[i].getText().trim().equals("")) {
                            Sequel.menyimpantf2("inacbg_diagnosa","?,?,?,?","INA EKLAIM",4,new String[]{
                                Tsep.getText(),kd_diag2[i].getText(),nm_diag2[i].getText(),(chk_diag2[i].isSelected() == true ? "1" : "0")
                            });
                        }
                    }

                    for (i = 0; i < kd_psdr2.length ; i++) {
                        if (!kd_psdr2[i].getText().trim().equals("")) {
                            Sequel.menyimpantf2("inacbg_prosedur","?,?,?","INA PSDR EKLAIM",3,new String[]{
                                Tsep.getText(),kd_psdr2[i].getText(),nm_psdr2[i].getText()
                            });
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Fungsi fallback ke database lokal
    private void tampildiagnosa() {
        try {
            Valid.tabelKosong(tabModeDiagnosa);
            psdiagnosa = koneksi.prepareStatement(
                "select * from icd_idrg where SYSTEM='ICD_10_2010_IM' and " +
                "(DESCRIPTION like ? or CODE like ?) order by CODE LIMIT 50"
            );
            try {
                psdiagnosa.setString(1, "%" + Diagnosa.getText().trim() + "%");
                psdiagnosa.setString(2, "%" + Diagnosa.getText().trim() + "%");
                rs = psdiagnosa.executeQuery();

                int count = 0;
                while (rs.next()) {
                    tabModeDiagnosa.addRow(new Object[]{
                        false,
                        rs.getString("CODE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("ACCPDX"),
                        rs.getString("ASTERISK"),
                        rs.getString("VALIDCODE")
                    });
                    count++;
                }
                //System.out.println("Berhasil memuat " + count + " data diagnosa dari database");

            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) rs.close();
                if (psdiagnosa != null) psdiagnosa.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }
    
    private void tampildiagnosaINA() {
        try {
            Valid.tabelKosong(tabModeDiagnosa2);
            psdiagnosa = koneksi.prepareStatement(
                "select * from icd_inacbg where SYSTEM='ICD_10_2010' and VALIDCODE='1' and " +
                "(DESCRIPTION like ? or CODE like ?) order by CODE LIMIT 50"
            );
            try {
                psdiagnosa.setString(1, "%" + DiagnosaINACBG.getText().trim() + "%");
                psdiagnosa.setString(2, "%" + DiagnosaINACBG.getText().trim() + "%");
                rs = psdiagnosa.executeQuery();
                while (rs.next()) {
                    tabModeDiagnosa2.addRow(new Object[]{
                        false,
                        rs.getString("CODE"),
                        rs.getString("DESCRIPTION")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) rs.close();
                if (psdiagnosa != null) psdiagnosa.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilprosedure() {
        try {
            Valid.tabelKosong(tabModeProsedur);
            psprosedur = koneksi.prepareStatement(
                "select * from icd_idrg where SYSTEM='ICD_9CM_2010_IM' " +
                "and (DESCRIPTION like ? or CODE like ?) order by CODE LIMIT 50"
            );
            try {
                psprosedur.setString(1, "%" + Prosedur.getText().trim() + "%");
                psprosedur.setString(2, "%" + Prosedur.getText().trim() + "%");
                rs = psprosedur.executeQuery();
                while (rs.next()) {
                    tabModeProsedur.addRow(new Object[]{
                        false,
                        rs.getString("CODE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("ACCPDX"),
                        rs.getString("ASTERISK"),
                        rs.getString("VALIDCODE")
                    });
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
            } finally {
                if (rs != null) rs.close();
                if (psprosedur != null) psprosedur.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    
    private void tampilprosedureINA() {
        try {
            
            Valid.tabelKosong(tabModeProsedur2);
            psprosedur = koneksi.prepareStatement(
                "select * from icd_inacbg where VALIDCODE='1' and SYSTEM='ICD_9CM_2010' " +
                "and (DESCRIPTION like ? or CODE like ?) order by CODE LIMIT 50"
            );
            try {
                psprosedur.setString(1, "%" + ProsedurINACBG.getText().trim() + "%");
                psprosedur.setString(2, "%" + ProsedurINACBG.getText().trim() + "%");
                rs = psprosedur.executeQuery();
                while (rs.next()) {
                    tabModeProsedur2.addRow(new Object[]{
                        false,
                        rs.getString("CODE"),
                        rs.getString("DESCRIPTION")
                    });
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
            } finally {
                if (rs != null) rs.close();
                if (psprosedur != null) psprosedur.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }
    
    private void getICD9(){
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kd_psdr.length; i++) {
            String kode = kd_psdr[i].getText().trim();
            String multiVal = multiple[i].getText().trim();

            if (!kode.equals("")) {
                // Tambahkan separator antar prosedur
                if (sb.length() > 0) {
                    sb.append("#");
                }

                // Kalau multiple kosong atau "1" → tampilkan kode saja
                // Kalau lebih dari 1 → tambahkan +angka
                if (multiVal.equals("") || multiVal.equals("1")) {
                    sb.append(kode);
                } else {
                    sb.append(kode).append("+").append(multiVal);
                }
            }
        }
        icd9 = sb.toString();
        System.out.println("icd9 : " + icd9);
        responIDRG.setText("");
    }
    
    private void getICD9INA(){
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kd_psdr2.length; i++) {
            String kode = kd_psdr2[i].getText().trim();

            if (!kode.equals("")) {
                // Tambahkan separator antar prosedur
                if (sb.length() > 0) {
                    sb.append("#");
                }
            }
        }
        icd9INA = sb.toString();
        System.out.println("icd9 INA : " + icd9INA);
        responIDRG.setText("");
    }
    
    private void getICD10(){
        icd10 = "";

        String primary = "";
        List<String> secondary = new ArrayList<>();

        for (int i = 0; i < kd_diag.length; i++) {
            String kode = kd_diag[i].getText().trim();
            if (!kode.equals("")) {
                if (chk_diag[i].isSelected()) {
                    // Kalau checkbox dicentang → anggap primary
                    primary = kode;
                } else {
                    secondary.add(kode);
                }
            }
        }

        // Susun ICD10 (diagnosa) → primary dulu, baru sisanya
        StringBuilder sb = new StringBuilder();
        if (!primary.equals("")) {
            sb.append(primary);
        }
        for (String kode : secondary) {
            if (sb.length() > 0) {
                sb.append("#");
            }
            sb.append(kode);
        }

        icd10 = sb.toString();
        System.out.println("icd10 : " + icd10);
    }
    
    private void getICD10INA(){
        icd10 = "";

        String primary = "";
        List<String> secondary = new ArrayList<>();

        for (int i = 0; i < kd_diag2.length; i++) {
            String kode = kd_diag2[i].getText().trim();
            if (!kode.equals("")) {
                if (chk_diag2[i].isSelected()) {
                    // Kalau checkbox dicentang → anggap primary
                    primary = kode;
                } else {
                    secondary.add(kode);
                }
            }
        }

        // Susun ICD10 (diagnosa) → primary dulu, baru sisanya
        StringBuilder sb = new StringBuilder();
        if (!primary.equals("")) {
            sb.append(primary);
        }
        for (String kode : secondary) {
            if (sb.length() > 0) {
                sb.append("#");
            }
            sb.append(kode);
        }

        icd10INA = sb.toString();
        System.out.println("icd10 INA : " + icd10INA);
    }
    
    
    private String getSelectedCode(JComboBox combo, JLabel lbl) {
        String val = combo.getSelectedItem().toString().trim();
        if (combo.getSelectedItem() == null) {
            return "";
        } else if (val.toLowerCase().contains("none")) {
            lbl.setText("0");
        } else if (val.contains(":")) {
            return val.split(":", 2)[0].trim();
        }

        // Jika tidak ada tanda ":" kembalikan apa adanya
        return val;
    }

    private String getCombinedCodes() {
        List<String> codes = new ArrayList<>();

        String proc = getSelectedCode(cmbProc, lbltarifProc);
        String pros = getSelectedCode(cmbPros, lblTarifPros);
        String inv  = getSelectedCode(cmbInv, lblTarifInv);
        String drug = getSelectedCode(cmbDrugs, lblTarifDrugs);

        if (!proc.isEmpty()) codes.add(proc);
        if (!pros.isEmpty()) codes.add(pros);
        if (!inv.isEmpty())  codes.add(inv);
        if (!drug.isEmpty()) codes.add(drug);

        return String.join("#", codes);
    }
    
    private void handleComboChange() {
        String combined = getCombinedCodes();

        if (!combined.isEmpty()) {
            grouper2();
        }
    }
    
    private void cekDiagnosaUtama(int i){
//        String cek_primary = Sequel.cariIsi(
//            "SELECT CODE FROM icd_idrg " +
//            "WHERE CODE = '" + kd_diag[i].getText().trim() + "' " +
//            "AND ( (VALIDCODE = '1' AND ACCPDX = 'N' AND ASTERISK = '0') " +
//            "OR (VALIDCODE = '1' AND ACCPDX = 'N' AND ASTERISK = '1') )"
//        );               
//        System.out.println("ini : "+i);
//        System.out.println("SELECT CODE FROM icd_idrg " +
//            "WHERE CODE = '" + kd_diag[i].getText().trim() + "' " +
//            "AND ( (VALIDCODE = '1' AND ACCPDX = 'N' AND ASTERISK = '0') " +
//            "OR (VALIDCODE = '1' AND ACCPDX = 'N' AND ASTERISK = '1') )");
//        if (!cek_primary.trim().equals("")) {
//            JOptionPane.showMessageDialog(null, "DIAGNOSA INI TIDAK BISA DI JADIKAN PRIMARY DI IDRG");
//            chk_diag[i].setSelected(false);
//        }
    }
    
    private void setupAutoCalculation() {
        JTextField[] fields = {pnb, pb, rad, rehab, obat, alkes, kpr, lab, 
                              akomodasi, kronis, bmhp, ksl, pnj, pl_darah, 
                              ri, obat_kemo, sewa_alat, ahli};

        for (JTextField field : fields) {
            field.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { calculate(); }
                public void removeUpdate(DocumentEvent e) { calculate(); }
                public void changedUpdate(DocumentEvent e) { calculate(); }

                private void calculate() {
                    SwingUtilities.invokeLater(() -> calculateIfAllFilled());
                }
            });
        }
    }
    
    private void calculateIfAllFilled() {
        try {
            // Cek apakah semua field terisi
            if (pnb.getText().trim().isEmpty() || pb.getText().trim().isEmpty() ||
                rad.getText().trim().isEmpty() || rehab.getText().trim().isEmpty() ||
                obat.getText().trim().isEmpty() || alkes.getText().trim().isEmpty() ||
                kpr.getText().trim().isEmpty() || lab.getText().trim().isEmpty() ||
                akomodasi.getText().trim().isEmpty() || kronis.getText().trim().isEmpty() ||
                bmhp.getText().trim().isEmpty() || ksl.getText().trim().isEmpty() ||
                pnj.getText().trim().isEmpty() || pl_darah.getText().trim().isEmpty() ||
                ri.getText().trim().isEmpty() || obat_kemo.getText().trim().isEmpty() ||
                sewa_alat.getText().trim().isEmpty() || ahli.getText().trim().isEmpty()) {
                return; // Keluar jika ada yang kosong
            }

            // Hitung total
            int total = Integer.parseInt(pnb.getText()) + 
                        Integer.parseInt(pb.getText()) + 
                        Integer.parseInt(ksl.getText()) + 
                        Integer.parseInt(rad.getText()) + 
                        Integer.parseInt(kpr.getText()) + 
                        Integer.parseInt(lab.getText()) + 
                        Integer.parseInt(alkes.getText()) + 
                        Integer.parseInt(kronis.getText()) + 
                        Integer.parseInt(obat_kemo.getText()) + 
                        Integer.parseInt(obat.getText()) + 
                        Integer.parseInt(pnj.getText()) + 
                        Integer.parseInt(pl_darah.getText()) + 
                        Integer.parseInt(bmhp.getText()) + 
                        Integer.parseInt(sewa_alat.getText()) + 
                        Integer.parseInt(rehab.getText()) + 
                        Integer.parseInt(ri.getText()) + 
                        Integer.parseInt(akomodasi.getText())+
                        Integer.parseInt(ahli.getText());

            tarifRS.setText(String.valueOf(Valid.SetAngka(total)));

        } catch (NumberFormatException e) {
            // Abaikan error jika input bukan angka
        }
    }
    
    // ===== HELPER METHOD UNTUK SET FIELD DENGAN NULL CHECK =====
    private void setFieldSafe(javax.swing.JTextField field, String value) {
        if (value != null && !value.trim().isEmpty()) {
            field.setText(value);
        } else {
            field.setText("");
        }
    }
    
     public void emptIDRG() {
        for (i = 0; i < kd_psdr.length; i++) {
            kd_diag[i].setText("");                   
            nm_diag[i].setText("");
            kd_psdr[i].setText("");
            nm_psdr[i].setText("");
            chk_diag[i].setSelected(false);
            multiple[i].setText(""); 
        }
     }
     
    // Fungsi untuk mengambil data dari idrg_diagnosa dan idrg_prosedur
    private void ambilDataDariIdrgGrouping() {
        if (Tsep.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor SEP tidak boleh kosong");
            return;
        }

        try {
            boolean dataFound = false;

            // Ambil data diagnosa dari idrg_diagnosa
            String sqlDiagnosa = "SELECT * FROM idrg_diagnosa WHERE no_sep=? ORDER BY prioritas DESC";
            PreparedStatement psDiagnosa = koneksi.prepareStatement(sqlDiagnosa);
            psDiagnosa.setString(1, Tsep.getText().trim());
            ResultSet rsDiagnosa = psDiagnosa.executeQuery();

            int j = 0;
            while (rsDiagnosa.next() && j < kd_diag.length) {
                setJTextFieldSafe(kd_diag[j], rsDiagnosa.getString("icd10"));
                setJTextFieldSafe(nm_diag[j], rsDiagnosa.getString("icd10_desc"));

                // Set checkbox prioritas
                if (chk_diag != null && j < chk_diag.length) {
                    String prioritas = rsDiagnosa.getString("prioritas");
                    chk_diag[j].setSelected("1".equals(prioritas));
                }

                dataFound = true;
                j++;
            }
            rsDiagnosa.close();
            psDiagnosa.close();

            // Ambil data prosedur dari idrg_prosedur
            String sqlProsedur = "SELECT * FROM idrg_prosedur WHERE no_sep=?";
            PreparedStatement psProsedur = koneksi.prepareStatement(sqlProsedur);
            psProsedur.setString(1, Tsep.getText().trim());
            ResultSet rsProsedur = psProsedur.executeQuery();

            j = 0;
            while (rsProsedur.next() && j < kd_psdr.length) {
                setJTextFieldSafe(kd_psdr[j], rsProsedur.getString("kd_icd9"));
                setJTextFieldSafe(nm_psdr[j], rsProsedur.getString("nm_icd9"));

                // Set multiplicity
                if (multiple != null && j < multiple.length) {
                    setJTextFieldSafe(multiple[j], rsProsedur.getString("multiplicity"));
                }

                dataFound = true;
                j++;
            }
            rsProsedur.close();
            psProsedur.close();

            // Tampilkan pesan sesuai hasil
            if (dataFound) {
                JOptionPane.showMessageDialog(null, "Data berhasil diambil dari data grouping IDRG");
            } else {
                JOptionPane.showMessageDialog(null, "Data IDRG tidak ditemukan untuk no SEP: " + Tsep.getText().trim());
            }

        } catch (Exception ex) {
            System.out.println("Error ambil data IDRG Grouping: " + ex);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data: " + ex.getMessage());
        }
    }
    
    // Helper method untuk JTextField dengan null check
    private void setJTextFieldSafe(javax.swing.JTextField field, String value) {
        if (value != null && !value.trim().isEmpty()) {
            field.setText(value);
        } else {
            field.setText("");
        }
    }
    
    // ========== METHOD BARU: UPDATE RESUME PASIEN RANAP ==========
    private void updateResumePasienRanap(String noRawat) {
        try {
            koneksi.setAutoCommit(false);

            // ========== BERSIHKAN DATA DIAGNOSA DAN PROSEDUR ==========
            String sqlClear = "UPDATE resume_pasien_ranap SET " +
                             "kd_diagnosa_utama = '', diagnosa_utama = '', " +
                             "kd_diagnosa_sekunder = '', diagnosa_sekunder = '', " +
                             "kd_diagnosa_sekunder2 = '', diagnosa_sekunder2 = '', " +
                             "kd_diagnosa_sekunder3 = '', diagnosa_sekunder3 = '', " +
                             "kd_diagnosa_sekunder4 = '', diagnosa_sekunder4 = '', " +
                             "kd_diagnosa_sekunder5 = '', diagnosa_sekunder5 = '', " +
                             "kd_diagnosa_sekunder6 = '', diagnosa_sekunder6 = '', " +
                             "kd_diagnosa_sekunder7 = '', diagnosa_sekunder7 = '', " +
                             "kd_diagnosa_sekunder8 = '', diagnosa_sekunder8 = '', " +
                             "kd_diagnosa_sekunder9 = '', diagnosa_sekunder9 = '', " +
                             "kd_diagnosa_sekunder10 = '', diagnosa_sekunder10 = '', " +
                             "kd_prosedur_utama = '', prosedur_utama = '', " +
                             "kd_prosedur_sekunder = '', prosedur_sekunder = '', " +
                             "kd_prosedur_sekunder2 = '', prosedur_sekunder2 = '', " +
                             "kd_prosedur_sekunder3 = '', prosedur_sekunder3 = '', " +
                             "kd_prosedur_sekunder4 = '', prosedur_sekunder4 = '', " +
                             "kd_prosedur_sekunder5 = '', prosedur_sekunder5 = '', " +
                             "kd_prosedur_sekunder6 = '', prosedur_sekunder6 = '', " +
                             "kd_prosedur_sekunder7 = '', prosedur_sekunder7 = '', " +
                             "kd_prosedur_sekunder8 = '', prosedur_sekunder8 = '', " +
                             "kd_prosedur_sekunder9 = '', prosedur_sekunder9 = '', " +
                             "kd_prosedur_sekunder10 = '', prosedur_sekunder10 = '', " +
                             "kd_prosedur_sekunder11 = '', prosedur_sekunder11 = '', " +
                             "kd_prosedur_sekunder12 = '', prosedur_sekunder12 = '', " +
                             "kd_prosedur_sekunder13 = '', prosedur_sekunder13 = '', " +
                             "kd_prosedur_sekunder14 = '', prosedur_sekunder14 = '', " +
                             "kd_prosedur_sekunder15 = '', prosedur_sekunder15 = '', " +
                             "kd_prosedur_sekunder16 = '', prosedur_sekunder16 = '', " +
                             "kd_prosedur_sekunder17 = '', prosedur_sekunder17 = '', " +
                             "kd_prosedur_sekunder18 = '', prosedur_sekunder18 = '' " +
                             "WHERE no_rawat = ?";

            PreparedStatement psClear = koneksi.prepareStatement(sqlClear);
            psClear.setString(1, noRawat);
            psClear.executeUpdate();
            psClear.close();

            // ========== UPDATE DIAGNOSA (LIMIT 11: utama + sekunder 1-10) ==========
            String sqlDiagnosa = "SELECT dp.kd_penyakit, p.nm_penyakit FROM diagnosa_pasien dp " +
                               "INNER JOIN penyakit p ON dp.kd_penyakit = p.kd_penyakit " +
                               "WHERE dp.no_rawat=? AND dp.status='Ranap' " +
                               "ORDER BY dp.prioritas ASC LIMIT 11";

            PreparedStatement psDiagnosa = koneksi.prepareStatement(sqlDiagnosa);
            psDiagnosa.setString(1, noRawat);
            ResultSet rsDiagnosa = psDiagnosa.executeQuery();

            String[] kdDiagnosa = new String[11];
            String[] nmDiagnosa = new String[11];

            int indexDiagnosa = 0;
            while (rsDiagnosa.next() && indexDiagnosa < 11) {
                kdDiagnosa[indexDiagnosa] = rsDiagnosa.getString("kd_penyakit");
                nmDiagnosa[indexDiagnosa] = rsDiagnosa.getString("nm_penyakit");
                System.out.println("  → Diagnosa ke-" + (indexDiagnosa + 1) + ": " +
                                 kdDiagnosa[indexDiagnosa] + " - " + nmDiagnosa[indexDiagnosa]);
                indexDiagnosa++;
            }
            rsDiagnosa.close();
            psDiagnosa.close();

            // Utama + sekunder (tanpa angka) + sekunder2 s/d sekunder10 = 11 kolom
            String sqlUpdateDiagnosa = "UPDATE resume_pasien_ranap SET " +
                "kd_diagnosa_utama = ?, diagnosa_utama = ?, " +
                "kd_diagnosa_sekunder = ?, diagnosa_sekunder = ?, " +
                "kd_diagnosa_sekunder2 = ?, diagnosa_sekunder2 = ?, " +
                "kd_diagnosa_sekunder3 = ?, diagnosa_sekunder3 = ?, " +
                "kd_diagnosa_sekunder4 = ?, diagnosa_sekunder4 = ?, " +
                "kd_diagnosa_sekunder5 = ?, diagnosa_sekunder5 = ?, " +
                "kd_diagnosa_sekunder6 = ?, diagnosa_sekunder6 = ?, " +
                "kd_diagnosa_sekunder7 = ?, diagnosa_sekunder7 = ?, " +
                "kd_diagnosa_sekunder8 = ?, diagnosa_sekunder8 = ?, " +
                "kd_diagnosa_sekunder9 = ?, diagnosa_sekunder9 = ?, " +
                "kd_diagnosa_sekunder10 = ?, diagnosa_sekunder10 = ? " +
                "WHERE no_rawat = ?";

            PreparedStatement psUpdateDiagnosa = koneksi.prepareStatement(sqlUpdateDiagnosa);
            for (int i = 0; i < 11; i++) {
                psUpdateDiagnosa.setString((i * 2) + 1, kdDiagnosa[i] != null ? kdDiagnosa[i] : "");
                psUpdateDiagnosa.setString((i * 2) + 2, nmDiagnosa[i] != null ? nmDiagnosa[i] : "");
            }
            psUpdateDiagnosa.setString(23, noRawat);
            psUpdateDiagnosa.executeUpdate();
            psUpdateDiagnosa.close();

            // ========== UPDATE PROSEDUR (tidak ada perubahan) ==========
            String sqlProsedur = "SELECT kode FROM prosedur_pasien " +
                               "WHERE no_rawat=? AND status='Ranap' " +
                               "ORDER BY prioritas ASC LIMIT 19";

            PreparedStatement psProsedur = koneksi.prepareStatement(sqlProsedur);
            psProsedur.setString(1, noRawat);
            ResultSet rsProsedur = psProsedur.executeQuery();

            int indexProsedur = 1;
            while (rsProsedur.next()) {
                String kdProsedur = rsProsedur.getString("kode");
                String deskripsiProsedur = Sequel.cariIsi("SELECT deskripsi_panjang FROM icd9 WHERE kode=?", kdProsedur);

                String fieldKode = "";
                String fieldDeskripsi = "";

                if (indexProsedur == 1) {
                    fieldKode = "kd_prosedur_utama";
                    fieldDeskripsi = "prosedur_utama";
                } else if (indexProsedur >= 2 && indexProsedur <= 19) {
                    fieldKode = "kd_prosedur_sekunder" + (indexProsedur == 2 ? "" : (indexProsedur - 1));
                    fieldDeskripsi = "prosedur_sekunder" + (indexProsedur == 2 ? "" : (indexProsedur - 1));
                }

                if (!fieldKode.isEmpty()) {
                    Sequel.mengedit("resume_pasien_ranap", "no_rawat=?", fieldKode + "=?", 2,
                        new String[]{kdProsedur, noRawat});

                    if (deskripsiProsedur != null && !deskripsiProsedur.isEmpty()) {
                        Sequel.mengedit("resume_pasien_ranap", "no_rawat=?", fieldDeskripsi + "=?", 2,
                            new String[]{deskripsiProsedur, noRawat});
                    }

                    System.out.println("  → Prosedur ke-" + indexProsedur + ": " + kdProsedur);
                }

                indexProsedur++;
            }
            rsProsedur.close();
            psProsedur.close();

            koneksi.commit();
            koneksi.setAutoCommit(true);

            System.out.println("✓ Data resume pasien ranap berhasil disinkronkan untuk no_rawat: " + noRawat);

        } catch (Exception e) {
            try {
                koneksi.rollback();
                koneksi.setAutoCommit(true);
            } catch (Exception ex) {
                System.out.println("Notifikasi rollback: " + ex);
            }
            System.out.println("Error update resume: " + e);
            e.printStackTrace();
        }
    }
    
    private void loadStatusKlaim() {
        if (Tsep.getText().trim().isEmpty()) return;
        try {
            String requestJson = "{\n"
                    + "\"metadata\": {\n"
                    + "\"method\":\"get_claim_status\"\n"
                    + "},\n"
                    + "\"data\": {\n"
                    + "\"nomor_sep\":\"" + Tsep.getText().trim() + "\"\n"
                    + "}\n"
                    + "}";
            JSONObject response = inacbg.request(requestJson);
            System.out.println("response get_claim_status : " + response);

            if (response.has("metadata")) {
                JSONObject meta = response.getJSONObject("metadata");
                if (meta.getInt("code") == 200 && response.has("response")) {
                    JSONObject data = response.getJSONObject("response");
                    String kdStatus = data.optString("kdStatusSep", "-");
                    String nmStatus = data.optString("nmStatusSep", "-")
                                          .replace("_", " ");

                    // Warna berdasarkan kode status
                    java.awt.Color warna;
                    switch (kdStatus) {
                        case "1":  // Final / sudah dikirim
                            warna = new java.awt.Color(0, 153, 0);    // hijau
                            break;
                        case "40": // Proses Cabang
                            warna = new java.awt.Color(255, 140, 0);  // oranye
                            break;
                        case "3":  // Ditolak
                            warna = new java.awt.Color(204, 0, 0);    // merah
                            break;
                        default:
                            warna = new java.awt.Color(0, 102, 204);  // biru
                            break;
                    }
                    lblStatusKlaim.setForeground(warna);
                    lblStatusKlaim.setText("Status: [" + kdStatus + "] " + nmStatus);
                } else {
                    lblStatusKlaim.setText("Status Klaim di BPJS tidak tersedia");
                    lblStatusKlaim.setForeground(new java.awt.Color(150, 150, 150));
                }
            }
        } catch (Exception e) {
            System.err.println("Error get_claim_status : " + e.getMessage());
            lblStatusEncounter.setText("Status: -");
        }
    }
    
}
