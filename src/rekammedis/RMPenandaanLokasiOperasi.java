/*
 * Formulir Penandaan Lokasi Operasi (RM 36a) - versi Wanita.
 * Operator menandai lokasi operasi langsung pada diagram anatomi (klik kiri = tandai,
 * klik kanan = hapus tanda terakhir). Tanda disimpan sebagai koordinat ternormalisasi
 * (0..1000) sehingga skala-bebas, lalu dicetak menempel di diagram.
 * 2 tab UX (Input + Data Input), single tabel database.
 * Cetakan HTML mirror PDF + kop RS dinamis + QR TTD operator.
 * Tabel : penandaan_lokasi_operasi (key: no_rawat, tanggal).
 */

package rekammedis;

import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import kepegawaian.DlgCariPetugas;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author SIMRS-Khanza
 */
public final class RMPenandaanLokasiOperasi extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private final Connection koneksi = koneksiDB.condb();
    private final sekuel Sequel = new sekuel();
    private final validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private DlgCariPetugas petugas;
    private int barisPilih = -1;
    private static final String IMG = "/picture/penandaan_lokasi_wanita.png";

    public RMPenandaanLokasiOperasi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(980, 760);
        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat","Tanggal","Ruangan","Rencana Tindakan","Jml Tanda","Operator"
        }) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tb.setModel(tabMode);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.setPreferredScrollableViewportSize(new java.awt.Dimension(900, 500));
        
        int[] lebarKolom = {150, 150, 160, 320, 80, 140};
        // No.Rawat, Tanggal, Ruangan, Rencana Tindakan, Jml Tanda, Operator
        for (int i = 0; i < lebarKolom.length; i++) {
            TableColumn kolom = tb.getColumnModel().getColumn(i);
            kolom.setPreferredWidth(lebarKolom[i]);
            if (i == 4) { // Jml Tanda -> rata tengah
                DefaultTableCellRenderer center = new DefaultTableCellRenderer();
                center.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                kolom.setCellRenderer(center);
            }
        }
        tb.setDefaultRenderer(Object.class, new fungsi.WarnaTable());
        fungsi.UtilCari.pasang(panelBtm, tb, tabMode, Tab, () -> TNoRw.getText());
        fungsi.UtilJam.jalan(Tanggal, () -> barisPilih < 0);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
    }

    public void isCek() {
        BtnSimpan.setEnabled(true); BtnBaru.setEnabled(true);
        BtnEdit.setEnabled(true); BtnHapus.setEnabled(true);
        KdPetugas.setText(akses.getkode());
        isiNamaOperator(akses.getkode());
    }

    public void setNoRm(String noRawat, Date tgl) {
        TNoRw.setText(noRawat);
        Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat=?", TNoRM, noRawat);
        Sequel.cariIsi("select pasien.nm_pasien from pasien inner join reg_periksa on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=?", TPasien, noRawat);
        Sequel.cariIsi("select pasien.tgl_lahir from pasien inner join reg_periksa on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=?", TTglLahir, noRawat);
        Sequel.cariIsi("select pasien.jk from pasien inner join reg_periksa on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=?", JK, noRawat);
        Sequel.cariIsi("select concat(bangsal.nm_bangsal,' - ',kamar_inap.kd_kamar) from kamar_inap "
                + "inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "
                + "where kamar_inap.no_rawat=? order by kamar_inap.tgl_masuk desc limit 1", TRuangan, noRawat);
        // Rencana tindakan diambil dari booking operasi (paket_operasi.nm_perawatan)
        Sequel.cariIsi("select paket_operasi.nm_perawatan from booking_operasi inner join paket_operasi on booking_operasi.kode_paket=paket_operasi.kode_paket where booking_operasi.no_rawat=? order by booking_operasi.tanggal desc limit 1", TRencana, noRawat);
        if (tgl != null) Tanggal.setDate(tgl);
        tampil();
    }

    private String tgl() { return Valid.SetTglJam(Tanggal.getSelectedItem() + ""); }

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps = koneksi.prepareStatement(
                "select no_rawat, tanggal, ruangan, rencana_tindakan, tanda_titik, nip " +
                "from penandaan_lokasi_operasi order by tanggal desc limit 1000");
            rs = ps.executeQuery();
            while (rs.next()) {
                tabMode.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    trunc(rs.getString(4), 45), jmlTanda(rs.getString(5)), rs.getString(6)
                });
            }
            LCount.setText("Record : " + tabMode.getRowCount());
        } catch (Exception e) { System.out.println("Notif tampil: " + e); }
        finally { try { if (rs!=null) rs.close(); if (ps!=null) ps.close(); } catch (Exception ex) {} }
    }
    private String trunc(String s, int n) { if (s==null) return ""; return s.length()<=n ? s : s.substring(0,n)+"…"; }
    private String jmlTanda(String s) { if (s==null || s.trim().isEmpty()) return "0"; return ""+s.split(";").length; }

    private String[] dataIsi() {
        return new String[]{
            TNoRw.getText(), tgl(), TRuangan.getText(), TRencana.getText(),
            TKeterangan.getText(), mark.serialize(), "Wanita", KdPetugas.getText()
        };
    }
    private String[] colNames() {
        return new String[]{"no_rawat","tanggal","ruangan","rencana_tindakan","keterangan","tanda_titik","jenis","nip"};
    }

    public void emptTeks() {
        barisPilih = -1;
        Tanggal.setDate(new Date());
        TRencana.setText(""); TKeterangan.setText("");
        mark.clearMarks();
    }

    private void pilihBaris() {
        if (tb.getSelectedRow() < 0) return;
        barisPilih = tb.convertRowIndexToModel(tb.getSelectedRow());
        String noRw = sCell(barisPilih, 0); String t = sCell(barisPilih, 1);
        try {
            ps = koneksi.prepareStatement("select * from penandaan_lokasi_operasi where no_rawat=? and tanggal=?");
            ps.setString(1, noRw); ps.setString(2, t);
            rs = ps.executeQuery();
            if (rs.next()) loadRow(rs);
        } catch (Exception e) { System.out.println("Notif pilihBaris: " + e); }
        finally { try { if (rs!=null) rs.close(); if (ps!=null) ps.close(); } catch (Exception ex) {} }
        Valid.SetTgl2(Tanggal, t);
    }
    private String sCell(int r, int c) { Object o = tabMode.getValueAt(r, c); return o == null ? "" : o.toString(); }
    private String g(ResultSet r, String c) throws Exception { String v=r.getString(c); return v==null?"":v; }

    private void loadRow(ResultSet r) throws Exception {
        TRuangan.setText(g(r,"ruangan")); TRencana.setText(g(r,"rencana_tindakan"));
        TKeterangan.setText(g(r,"keterangan")); mark.load(g(r,"tanda_titik"));
        KdPetugas.setText(g(r,"nip"));
        isiNamaOperator(g(r,"nip"));
    }

    // ===== CRUD =====
    private boolean validUmum() {
        if (TNoRw.getText().trim().isEmpty()) { Valid.textKosong(TNoRw, "pasien"); return false; }
        if (KdPetugas.getText().trim().isEmpty()) { Valid.textKosong(KdPetugas, "Operator"); return false; }
        return true;
    }
    private String placeholders(int n) { StringBuilder sb=new StringBuilder("?"); for(int i=1;i<n;i++) sb.append(",?"); return sb.toString(); }

    private void simpan() {
        if (!validUmum()) return;
        String[] data = dataIsi();
        int n = data.length;
        if (Sequel.menyimpantf("penandaan_lokasi_operasi", placeholders(n), "Data", n, data)) {
            tampil(); emptTeks();
        }
    }
    private void hapus() {
        if (barisPilih < 0) { JOptionPane.showMessageDialog(rootPane, "Pilih baris dulu..!!"); return; }
        if (!akses.getkode().equals("Admin Utama") && !KdPetugas.getText().equals(sCell(barisPilih, 5))) {
            JOptionPane.showMessageDialog(null, "Hanya operator yang bersangkutan!"); return;
        }
        if (Sequel.queryu2tf("delete from penandaan_lokasi_operasi where no_rawat=? and tanggal=?", 2,
                new String[]{sCell(barisPilih, 0), sCell(barisPilih, 1)})) {
            tampil(); emptTeks();
        }
    }
    private void ganti() {
        if (!validUmum() || barisPilih < 0) return;
        String[] d = dataIsi();
        String[] cols = colNames();
        StringBuilder sql = new StringBuilder("update penandaan_lokasi_operasi set ");
        for (int i = 2; i < cols.length; i++) { if (i > 2) sql.append(","); sql.append(cols[i]).append("=?"); }
        sql.append(",tanggal=? where no_rawat=? and tanggal=?");
        String[] args = new String[cols.length - 2 + 3];
        int k = 0;
        for (int i = 2; i < cols.length; i++) args[k++] = d[i];
        args[k++] = tgl();
        args[k++] = sCell(barisPilih, 0);
        args[k++] = sCell(barisPilih, 1);
        Sequel.queryu2tf(sql.toString(), args.length, args);
        tampil(); emptTeks();
    }

    // ===== UI =====
    private void initComponents() {
        internalFrame1 = new widget.InternalFrame();
        panelHeader = new widget.PanelBiasa();
        Tab = new javax.swing.JTabbedPane();
        panelBottom = new javax.swing.JPanel();
        panelBtm = new widget.panelisi();
        BtnSimpan = newBtn("Simpan","/picture/save-16x16.png",'S');
        BtnBaru   = newBtn("Baru",  "/picture/Cancel-2-16x16.png",'B');
        BtnEdit   = newBtn("Ganti", "/picture/inventaris.png",'G');
        BtnHapus  = newBtn("Hapus", "/picture/stop_f2.png",'H');
        BtnPrint  = newBtn("Cetak", "/picture/print.png",'C');
        BtnKeluar = newBtn("Keluar","/picture/exit.png",'K');
        BtnPetugas = new widget.Button();
        LCount = new widget.Label();
        KdPetugas = new widget.TextBox(); NmPetugas = new widget.TextBox();
        TNoRw = new widget.TextBox(); TNoRM = new widget.TextBox(); TPasien = new widget.TextBox();
        TTglLahir = new widget.TextBox(); JK = new widget.TextBox();
        TRuangan = new widget.TextBox(); TRencana = new widget.TextBox(); TKeterangan = new widget.TextBox();
        Tanggal = new widget.Tanggal();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formulir Penandaan Lokasi Operasi (RM 36a)");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/picture/addressbook-edit24.png")).getImage());
        setName("RMPenandaanLokasiOperasi");
        internalFrame1.setBorder(null);
        internalFrame1.setLayout(new java.awt.BorderLayout());

        buildHeader();
        internalFrame1.add(panelHeader, java.awt.BorderLayout.PAGE_START);

        // Dua tab: "Input" (diagram penandaan) & "Data Input" (tabel hasil simpan)
        buildInputTab();
        buildDataTab();
        internalFrame1.add(Tab, java.awt.BorderLayout.CENTER);

        panelBottom.setLayout(new java.awt.BorderLayout(1,1));
        panelBtm.setPreferredSize(new java.awt.Dimension(44, 44));
        panelBtm.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));
        BtnSimpan.addActionListener((ActionEvent e) -> simpan());
        BtnBaru.addActionListener((ActionEvent e) -> emptTeks());
        BtnEdit.addActionListener((ActionEvent e) -> ganti());
        BtnHapus.addActionListener((ActionEvent e) -> hapus());
        BtnPrint.addActionListener((ActionEvent e) -> cetakPilih());
        BtnKeluar.addActionListener((ActionEvent e) -> dispose());
        panelBtm.add(BtnSimpan); panelBtm.add(BtnBaru); panelBtm.add(BtnEdit); panelBtm.add(BtnHapus);
        panelBtm.add(new javax.swing.JLabel(" | ")); panelBtm.add(BtnPrint);
        LCount.setText("Record : 0"); LCount.setPreferredSize(new java.awt.Dimension(120, 23));
        panelBtm.add(LCount); panelBtm.add(BtnKeluar);
        panelBottom.add(panelBtm, java.awt.BorderLayout.CENTER);
        internalFrame1.add(panelBottom, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private widget.Button newBtn(String text, String icon, char mnemonic) {
        widget.Button b = new widget.Button();
        b.setText(text);
        try { b.setIcon(new javax.swing.ImageIcon(getClass().getResource(icon))); } catch (Exception e) {}
        b.setMnemonic(mnemonic);
        b.setPreferredSize(new java.awt.Dimension(95, 30));
        return b;
    }

    private void buildHeader() {
        panelHeader.setLayout(null);
        panelHeader.setPreferredSize(new java.awt.Dimension(100, 150));
        widget.Label title = new widget.Label();
        title.setFont(new java.awt.Font("Tahoma", 1, 12));
        title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        title.setText("FORMULIR PENANDAAN LOKASI OPERASI (RM 36a)");
        panelHeader.add(title); title.setBounds(10, 4, 920, 22);

        addH("No.Rawat :", 10, 32, 85);
        TNoRw.setEditable(false); panelHeader.add(TNoRw); TNoRw.setBounds(100, 32, 180, 23);
        addH("No.RM :", 295, 32, 80);
        TNoRM.setEditable(false); panelHeader.add(TNoRM); TNoRM.setBounds(380, 32, 180, 23);
        addH("Nama :", 580, 32, 80);
        TPasien.setEditable(false); panelHeader.add(TPasien); TPasien.setBounds(665, 32, 250, 23);

        addH("JK :", 10, 60, 85);
        JK.setEditable(false); panelHeader.add(JK); JK.setBounds(100, 60, 80, 23);
        addH("Tgl Lahir :", 295, 60, 80);
        TTglLahir.setEditable(false); panelHeader.add(TTglLahir); TTglLahir.setBounds(380, 60, 180, 23);
        addH("Ruangan :", 580, 60, 80);
        panelHeader.add(TRuangan); TRuangan.setBounds(665, 60, 250, 23);

        addH("Tgl & Jam :", 10, 88, 85);
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        panelHeader.add(Tanggal); Tanggal.setBounds(100, 88, 160, 23);
        addH("Operator :", 295, 88, 80);
        KdPetugas.setEditable(false); panelHeader.add(KdPetugas); KdPetugas.setBounds(380, 88, 80, 23);
        NmPetugas.setEditable(false); panelHeader.add(NmPetugas); NmPetugas.setBounds(465, 88, 250, 23);
        try { BtnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); } catch (Exception e) {}
        BtnPetugas.setPreferredSize(new java.awt.Dimension(30, 23));
        BtnPetugas.addActionListener((ActionEvent e) -> openCariPetugas());
        panelHeader.add(BtnPetugas); BtnPetugas.setBounds(720, 88, 28, 23);

        addH("Rencana Tindakan :", 10, 116, 120);
        panelHeader.add(TRencana); TRencana.setBounds(135, 116, 780, 23);
    }

    private void addH(String text, int x, int y, int w) {
        widget.Label l = new widget.Label();
        l.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l.setText(text);
        panelHeader.add(l); l.setBounds(x, y, w, 23);
    }

    private void openCariPetugas() {
        if (petugas==null || !petugas.isDisplayable()) {
            petugas = new DlgCariPetugas(null, false);
            petugas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            petugas.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent w) {
                    if (petugas.getTable().getSelectedRow()!=-1) {
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                    }
                }
            });
            petugas.setSize(internalFrame1.getWidth()-20, internalFrame1.getHeight()-20);
            petugas.setLocationRelativeTo(internalFrame1);
        }
        if (!petugas.isVisible()) { petugas.isCek(); petugas.emptTeks(); }
        if (petugas.isVisible()) { petugas.toFront(); return; }
        petugas.setVisible(true);
    }

    private javax.swing.JScrollPane wrapScroll(javax.swing.JComponent c) {
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(c);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBorder(null);
        return sp;
    }
    private widget.Label lbl(String text, int x, int y, int w, int h, widget.PanelBiasa p) {
        widget.Label l=new widget.Label(); l.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l.setText(text); p.add(l); l.setBounds(x,y,w,h); return l;
    }

    private void buildInputTab() {
        widget.PanelBiasa top = new widget.PanelBiasa(); top.setLayout(null);
        lbl("Keterangan :", 16, 8, 80, 23, top);
        top.add(TKeterangan); TKeterangan.setBounds(96, 8, 470, 23);
        BtnUndo = new widget.Button(); BtnUndo.setText("Hapus Tanda Terakhir");
        BtnUndo.setPreferredSize(new java.awt.Dimension(160,25));
        BtnUndo.addActionListener((ActionEvent e) -> mark.undo());
        top.add(BtnUndo); BtnUndo.setBounds(575, 8, 160, 25);
        BtnClear = new widget.Button(); BtnClear.setText("Hapus Semua");
        BtnClear.addActionListener((ActionEvent e) -> mark.clearMarks());
        top.add(BtnClear); BtnClear.setBounds(740, 8, 110, 25);
        widget.Label hint = new widget.Label();
        hint.setForeground(new java.awt.Color(150, 40, 40));
        hint.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        hint.setText("Klik-tahan & geser mouse untuk mencoret lokasi operasi (garis merah). Klik KANAN untuk menghapus coretan terakhir.");
        top.add(hint); hint.setBounds(16, 36, 900, 20);
        top.setPreferredSize(new java.awt.Dimension(925, 62));

        mark = new MarkPanel();
        mark.setBorder(null);

        // Diagram mengisi lebar penuh (besar) + scroll vertikal untuk melihat seluruhnya
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(mark,
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null); sp.getVerticalScrollBar().setUnitIncrement(16);
        widget.PanelBiasa cont = new widget.PanelBiasa();
        cont.setLayout(new java.awt.BorderLayout(0, 2));
        cont.add(top, java.awt.BorderLayout.NORTH);
        cont.add(sp, java.awt.BorderLayout.CENTER);
        Tab.addTab("Input", cont);
    }

    private void buildDataTab() {
        tb = new widget.Table();
        tb.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { pilihBaris(); }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { pilihKlikKanan(e); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { pilihKlikKanan(e); }
        });
        setupPopupCetak();
        widget.ScrollPane scTb = new widget.ScrollPane(); scTb.setViewportView(tb);
        widget.PanelBiasa data = new widget.PanelBiasa();
        data.setLayout(new java.awt.BorderLayout(1, 1));
        data.add(scTb, java.awt.BorderLayout.CENTER);
        Tab.addTab("Data Input", data);
    }

    // Popup klik-kanan pada tabel Data Input untuk cetak formulir RM
    private void setupPopupCetak() {
        final javax.swing.JPopupMenu pop = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem miCetak = new javax.swing.JMenuItem("Cetak Penandaan Ini");
        try { miCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/print.png"))); } catch (Exception ex) {}
        miCetak.addActionListener((ActionEvent e) -> cetakPilih());
        pop.add(miCetak);
        tb.setComponentPopupMenu(pop);
    }

    private void pilihKlikKanan(java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            int r = tb.rowAtPoint(e.getPoint());
            if (r >= 0) {
                tb.setRowSelectionInterval(r, r);
                pilihBaris();
            }
        }
    }

    // ===== Panel penandaan interaktif (coretan bebas mengikuti mouse) =====
    private final class MarkPanel extends javax.swing.JPanel implements javax.swing.Scrollable {
        private java.awt.image.BufferedImage img;
        private final java.util.List<java.util.List<int[]>> strokes = new java.util.ArrayList<>();
        private java.util.List<int[]> current;
        MarkPanel() {
            try { img = javax.imageio.ImageIO.read(getClass().getResource(IMG)); }
            catch (Exception e) { System.out.println("Notif gambar: " + e); }
            setBackground(java.awt.Color.WHITE);
            java.awt.event.MouseAdapter ma = new java.awt.event.MouseAdapter() {
                @Override public void mousePressed(java.awt.event.MouseEvent e) {
                    if (javax.swing.SwingUtilities.isRightMouseButton(e)) { undo(); return; }
                    current = new java.util.ArrayList<>(); strokes.add(current); tambah(e);
                }
                @Override public void mouseDragged(java.awt.event.MouseEvent e) {
                    if (!javax.swing.SwingUtilities.isRightMouseButton(e)) tambah(e);
                }
                @Override public void mouseReleased(java.awt.event.MouseEvent e) { current = null; }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }
        @Override public java.awt.Dimension getPreferredSize() {
            int w = getWidth();
            if (w <= 0) { java.awt.Container p = getParent(); w = (p != null && p.getWidth() > 0) ? p.getWidth() : 1000; }
            int h = (img != null) ? (int)Math.round((double) w * img.getHeight() / img.getWidth()) : 800;
            return new java.awt.Dimension(w, h);
        }
        @Override public java.awt.Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(java.awt.Rectangle r, int orient, int dir) { return 16; }
        @Override public int getScrollableBlockIncrement(java.awt.Rectangle r, int orient, int dir) { return 240; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
        private void tambah(java.awt.event.MouseEvent e) {
            if (current == null || getWidth() <= 0 || getHeight() <= 0) return;
            int nx = (int)Math.round(1000.0 * e.getX() / getWidth());
            int ny = (int)Math.round(1000.0 * e.getY() / getHeight());
            nx = Math.max(0, Math.min(1000, nx)); ny = Math.max(0, Math.min(1000, ny));
            current.add(new int[]{nx, ny}); repaint();
        }
        void clearMarks() { strokes.clear(); current = null; repaint(); }
        void undo() { if (!strokes.isEmpty()) { strokes.remove(strokes.size()-1); repaint(); } }
        String serialize() {
            StringBuilder s = new StringBuilder();
            for (java.util.List<int[]> st : strokes) {
                if (st.isEmpty()) continue;
                if (s.length() > 0) s.append(";");
                StringBuilder p = new StringBuilder();
                for (int[] pt : st) { if (p.length() > 0) p.append(" "); p.append(pt[0]).append(",").append(pt[1]); }
                s.append(p);
            }
            return s.toString();
        }
        void load(String str) {
            strokes.clear(); current = null;
            if (str != null && !str.trim().isEmpty()) {
                for (String stStr : str.split(";")) {
                    java.util.List<int[]> st = new java.util.ArrayList<>();
                    for (String ptStr : stStr.trim().split("\\s+")) {
                        String[] xy = ptStr.split(",");
                        if (xy.length == 2) { try { st.add(new int[]{ Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim()) }); } catch (Exception ex) {} }
                    }
                    if (!st.isEmpty()) strokes.add(st);
                }
            }
            repaint();
        }
        @Override protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            if (img != null) g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            g2.setColor(java.awt.Color.RED);
            g2.setStroke(new java.awt.BasicStroke(3f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            for (java.util.List<int[]> st : strokes) {
                if (st.size() == 1) {
                    int x = st.get(0)[0]*getWidth()/1000, yy = st.get(0)[1]*getHeight()/1000;
                    g2.fillOval(x-2, yy-2, 4, 4);
                } else {
                    for (int i = 1; i < st.size(); i++) {
                        int x1 = st.get(i-1)[0]*getWidth()/1000, y1 = st.get(i-1)[1]*getHeight()/1000;
                        int x2 = st.get(i)[0]*getWidth()/1000, y2 = st.get(i)[1]*getHeight()/1000;
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
        byte[] toPng() {
            try {
                if (img == null) return null;
                int w = img.getWidth(), h = img.getHeight();
                java.awt.image.BufferedImage out = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g2 = out.createGraphics();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(img, 0, 0, w, h, null);
                g2.setColor(java.awt.Color.RED);
                float sw = Math.max(2f, w/250f);
                g2.setStroke(new java.awt.BasicStroke(sw, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                for (java.util.List<int[]> st : strokes) {
                    if (st.size() == 1) { int x=st.get(0)[0]*w/1000, y=st.get(0)[1]*h/1000, rr=(int)Math.max(2,sw); g2.fillOval(x-rr,y-rr,2*rr,2*rr); }
                    else { for (int i=1;i<st.size();i++){ int x1=st.get(i-1)[0]*w/1000,y1=st.get(i-1)[1]*h/1000,x2=st.get(i)[0]*w/1000,y2=st.get(i)[1]*h/1000; g2.drawLine(x1,y1,x2,y2);} }
                }
                g2.dispose();
                java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(out, "PNG", b);
                return b.toByteArray();
            } catch (Exception e) { System.out.println("Notif toPng: "+e); return null; }
        }
    }

    // ===== CETAK HTML mirror PDF =====
    private void cetakReplika() {
        if (TNoRw.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(rootPane, "Pilih pasien dulu..!!"); return; }
        if (barisPilih < 0 && tabMode.getRowCount() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Belum ada data tersimpan..!!"); return;
        }
        String tglAcuan = barisPilih>=0 ? sCell(barisPilih,1) : (tabMode.getRowCount()>0 ? sCell(0,1) : tgl());
        cetakHtml(buildReplika(tglAcuan));
    }

    private void cetakPilih() {
        if (TNoRw.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(rootPane, "Pilih pasien dulu..!!"); return; }
        Object[] opsi = {"Jasper", "HTML", "Batal"};
        int pil = JOptionPane.showOptionDialog(rootPane, "Pilih format cetak:", "Cetak Penandaan Lokasi Operasi",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opsi, opsi[0]);
        if (pil == 0) cetakJasper();
        else if (pil == 1) cetakReplika();
    }

    private void cetakJasper() {
        try {
            byte[] logo = null;
            try { logo = Sequel.cariGambar("select setting.logo from setting").readAllBytes(); } catch (Exception e2) {}
            byte[] diag = (mark != null) ? mark.toPng() : null;
            String info = "Ruangan : "+TRuangan.getText()
                        + "\nRencana Tindakan : "+TRencana.getText()
                        + "\nKeterangan : "+TKeterangan.getText();
            java.util.Map<String,Object> p = new java.util.HashMap<>();
            p.put("namars", akses.getnamars());
            p.put("alamatrs", akses.getalamatrs());
            p.put("kotars", akses.getkabupatenrs());
            p.put("propinsirs", akses.getpropinsirs());
            p.put("kontakrs", akses.getkontakrs());
            p.put("emailrs", akses.getemailrs());
            p.put("norm", TNoRM.getText());
            p.put("nama", TPasien.getText());
            p.put("tgllahir", TTglLahir.getText());
            p.put("rmkode", "RM.36a");
            p.put("judul2", "WANITA");
            p.put("info", info);
            p.put("petugas", "Operator\n\n(  "+NmPetugas.getText()+"  )"+(KdPetugas.getText().trim().isEmpty()?"":"\nNIP. "+KdPetugas.getText()));
            p.put("revisi", "RM.36a /Rev.001/01/2020");
            p.put("logo", logo==null?null:new java.io.ByteArrayInputStream(logo));
            p.put("diagram", diag==null?null:new java.io.ByteArrayInputStream(diag));
            Valid.MyReportqry("rptPenandaanLokasiOperasi.jasper", "report", "::[ Penandaan Lokasi Operasi (Wanita) ]::", "select 1", p);
        } catch (Exception e) { System.out.println("Notif cetakJasper: "+e); }
    }
    
    private void cetakHtml(String html) {
        try {
            java.io.File f = java.io.File.createTempFile("penandaan_lokasi_", "_"+System.currentTimeMillis()+".html");
            f.deleteOnExit();
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f), "UTF-8"))) { pw.write(html); }
            java.awt.Desktop.getDesktop().browse(f.toURI());
        } catch (Exception ex) {
            System.out.println("Notif Cetak: " + ex);
            JOptionPane.showMessageDialog(rootPane, "Gagal cetak: " + ex.getMessage());
        }
    }
    
    private void isiNamaOperator(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            NmPetugas.setText("");
            return;
        }
        if (kode.equals("Admin Utama")) {
            NmPetugas.setText("Admin Utama");
            return;
        }
        String nama = Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", kode);
        if (nama == null || nama.trim().isEmpty()) {
            nama = Sequel.cariIsi("select nama from petugas where nip=?", kode);
        }
        NmPetugas.setText(nama == null ? "" : nama);
    }
    
    private String esc(String s) {
        if (s==null || s.isEmpty()) return "&nbsp;";
        StringBuilder b = new StringBuilder(s.length()+8);
        for (int i = 0; i < s.length(); i++) { char c=s.charAt(i);
            switch (c) { case '&': b.append("&amp;"); break; case '<': b.append("&lt;"); break;
                case '>': b.append("&gt;"); break; case '"': b.append("&quot;"); break;
                case '\'': b.append("&#39;"); break; default: b.append(c); } }
        return b.toString();
    }
    private String orDash(String s) { return (s==null||s.trim().isEmpty())?"-":s; }
    private String v(java.util.Map<String,String> m, String k) { String r=m.get(k); return r==null?"":r; }

    private String ambilLogoDataUrl() {
        try {
            ps = koneksi.prepareStatement("select logo from setting");
            rs = ps.executeQuery();
            if (rs.next()) {
                java.sql.Blob blob = rs.getBlob(1);
                if (blob != null) {
                    byte[] bytes = blob.getBytes(1L, (int) blob.length());
                    if (bytes != null && bytes.length > 0) {
                        String mime = "image/png";
                        if (bytes.length>2 && (bytes[0]&0xff)==0xFF && (bytes[1]&0xff)==0xD8) mime="image/jpeg";
                        return "data:" + mime + ";base64," + java.util.Base64.getEncoder().encodeToString(bytes);
                    }
                }
            }
        } catch (Exception e) { System.out.println("Notif logo: " + e); }
        finally { try { if (rs!=null) rs.close(); if (ps!=null) ps.close(); } catch (Exception ex) {} }
        return null;
    }
    private String diagramDataUrl() {
        try (java.io.InputStream in = getClass().getResourceAsStream(IMG)) {
            if (in == null) return null;
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[8192]; int n; while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
            return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) { System.out.println("Notif diagram: " + e); return null; }
    }
    private String buildQrDataUrl(String text, int size) {
        if (text == null || text.isEmpty()) return null;
        try {
            java.util.Map<com.google.zxing.EncodeHintType,Object> hints = new java.util.HashMap<>();
            hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET,"UTF-8");
            hints.put(com.google.zxing.EncodeHintType.MARGIN, 1);
            hints.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);
            com.google.zxing.qrcode.QRCodeWriter w = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix mx = w.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, size, size, hints);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(mx, "PNG", baos);
            return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) { System.out.println("Notif QR: " + e); return null; }
    }
    private java.util.Map<String,String> ambilRow(String tglAcuan) {
        java.util.Map<String,String> m = new java.util.HashMap<>();
        try {
            ps = koneksi.prepareStatement("select * from penandaan_lokasi_operasi where no_rawat=? and tanggal=?");
            ps.setString(1, TNoRw.getText()); ps.setString(2, tglAcuan);
            rs = ps.executeQuery();
            if (rs.next()) {
                java.sql.ResultSetMetaData md = rs.getMetaData();
                for (int i=1; i<=md.getColumnCount(); i++) { String val = rs.getString(i); m.put(md.getColumnName(i), val==null?"":val); }
            }
        } catch (Exception e) { System.out.println("Notif ambilRow: " + e); }
        finally { try { if (rs!=null) rs.close(); if (ps!=null) ps.close(); } catch (Exception ex) {} }
        return m;
    }

    private String buildReplika(String tglAcuan) {
        java.util.Map<String,String> d = ambilRow(tglAcuan);
        String logoUrl = ambilLogoDataUrl();
        String diagUrl = diagramDataUrl();
        String namaRs   = orDash(akses.getnamars());
        String alamatRs = orDash(akses.getalamatrs());
        String kabRs    = orDash(akses.getkabupatenrs());
        String propRs   = orDash(akses.getpropinsirs());
        String kontakRs = orDash(akses.getkontakrs());
        String emailRs  = orDash(akses.getemailrs());
        String tglAja = tglAcuan.length()>=10 ? tglAcuan.substring(0,10) : tglAcuan;
        String jamAja = tglAcuan.length()>=16 ? tglAcuan.substring(11,16) : "";

        StringBuilder sb = new StringBuilder(2_000_000);
        sb.append("<!DOCTYPE html><html lang=\"id\"><head><meta charset=\"UTF-8\">");
        sb.append("<title>Formulir Penandaan Lokasi Operasi</title><style>");
        sb.append("@page { size: A4; margin: 8mm; }");
        sb.append("*{box-sizing:border-box;} body{font-family:Arial,sans-serif;font-size:11px;color:#000;margin:5mm;background:#eee;}");
        sb.append(".page{background:#fff;padding:7mm 9mm;margin:0 auto;width:210mm;min-height:297mm;border:1px solid #aaa;}");
        sb.append(".toolbar{text-align:center;margin:8px 0 10px;} .toolbar button{padding:6px 14px;margin:0 4px;cursor:pointer;border:1px solid #888;background:#eee;border-radius:4px;}");
        sb.append(".kop{width:100%;border-collapse:collapse;margin-bottom:4px;} .kop td{vertical-align:top;padding:2px;}");
        sb.append(".kop .logo{width:64px;text-align:center;}");
        sb.append(".kop .rs{padding-left:8px;font-size:11px;} .kop .rs b{font-size:14px;}");
        sb.append(".kop .rm{width:90px;border:1px solid #000;text-align:center;font-weight:bold;padding:4px;}");
        sb.append(".kop .stiker{width:240px;border:1px solid #000;padding:5px 7px;font-size:11px;} .kop .stiker .lbl{display:inline-block;width:70px;}");
        sb.append("hr.kop-line{border:none;border-top:1.5px solid #000;margin:0 0 6px 0;}");
        sb.append("h1.judul{text-align:center;font-size:17px;margin:6px 0 10px;letter-spacing:1px;}");
        sb.append(".meta{font-size:12px;margin-bottom:6px;line-height:1.9;} .meta .u{border-bottom:1px solid #000;display:inline-block;min-width:120px;padding:0 6px;}");
        sb.append(".diagram{position:relative;width:100%;margin:6px 0;} .diagram img{width:100%;display:block;}");
        sb.append(".mk{position:absolute;transform:translate(-50%,-50%);width:16px;height:16px;border:3px solid #d00;border-radius:50%;box-sizing:border-box;}");
        sb.append(".sig{margin-top:10px;text-align:right;font-size:12px;} .sig .box{display:inline-block;text-align:center;}");
        sb.append(".footer-rm{margin-top:6px;font-size:10px;font-style:italic;}");
        sb.append("@media print{body{background:#fff;margin:0;} .toolbar{display:none;} .page{margin:0;border:none;padding:6mm;}}");
        sb.append("</style></head><body>");
        sb.append("<div class=\"toolbar\"><button onclick=\"window.print()\">&#128424; Cetak / Print</button> <button onclick=\"window.close()\">Tutup</button></div>");

        sb.append("<div class=\"page\">");
        // kop
        sb.append("<table class=\"kop\"><tr><td class=\"logo\">");
        if (logoUrl!=null) sb.append("<img src=\"").append(logoUrl).append("\" style=\"width:60px;height:auto;\">"); else sb.append("<div style=\"width:58px;height:58px;border:1px solid #999;border-radius:50%;\"></div>");
        sb.append("</td><td class=\"rs\"><b>").append(esc(namaRs)).append("</b><br>");
        sb.append(esc(alamatRs)).append(", ").append(esc(kabRs)).append(", ").append(esc(propRs)).append("<br>");
        sb.append("Telp: ").append(esc(kontakRs)).append(" &nbsp; Email: ").append(esc(emailRs)).append("</td>");
        sb.append("<td class=\"rm\">RM.36a</td>");
        sb.append("<td class=\"stiker\">");
        sb.append("<div><span class=\"lbl\">No. RM</span>: ").append(esc(TNoRM.getText())).append("</div>");
        sb.append("<div><span class=\"lbl\">Nama</span>: ").append(esc(TPasien.getText())).append("</div>");
        sb.append("<div><span class=\"lbl\">Tgl Lahir</span>: ").append(esc(TTglLahir.getText())).append("</div>");
        sb.append("</td></tr></table><hr class=\"kop-line\">");
        sb.append("<h1 class=\"judul\">FORMULIR PENANDAAN LOKASI OPERASI</h1>");

        // meta
        sb.append("<div class=\"meta\">");
        sb.append("Ruangan : <span class=\"u\" style=\"min-width:260px\">").append(esc(v(d,"ruangan"))).append("</span> &nbsp;&nbsp; ");
        sb.append("Tanggal : <span class=\"u\">").append(esc(tglAja)).append("</span> &nbsp;&nbsp; Jam : <span class=\"u\" style=\"min-width:70px\">").append(esc(jamAja)).append("</span><br>");
        sb.append("Rencana Tindakan : <span class=\"u\" style=\"min-width:520px\">").append(esc(v(d,"rencana_tindakan"))).append("</span>");
        String ket = v(d,"keterangan");
        if (!ket.trim().isEmpty()) sb.append("<br>Keterangan : <span class=\"u\" style=\"min-width:520px\">").append(esc(ket)).append("</span>");
        sb.append("</div>");

        // diagram + tanda
        sb.append("<div class=\"diagram\">");
        if (diagUrl!=null) sb.append("<img src=\"").append(diagUrl).append("\">"); else sb.append("<div style=\"border:1px solid #000;height:200mm;text-align:center;\">(diagram tidak tersedia)</div>");
        String tanda = v(d,"tanda_titik");
        if (!tanda.trim().isEmpty()) {
            sb.append("<svg viewBox=\"0 0 1000 1000\" preserveAspectRatio=\"none\" style=\"position:absolute;left:0;top:0;width:100%;height:100%;\">");
            for (String st : tanda.split(";")) {
                String[] pts = st.trim().split("\\s+");
                StringBuilder poly = new StringBuilder();
                for (String p : pts) { String[] xy = p.split(","); if (xy.length==2) poly.append(xy[0].trim()).append(",").append(xy[1].trim()).append(" "); }
                String pp = poly.toString().trim();
                if (pp.isEmpty()) continue;
                if (pts.length == 1) { String[] xy = pts[0].split(","); if (xy.length==2) sb.append("<circle cx=\"").append(xy[0].trim()).append("\" cy=\"").append(xy[1].trim()).append("\" r=\"7\" fill=\"#d00\"/>"); }
                else sb.append("<polyline points=\"").append(pp).append("\" fill=\"none\" stroke=\"#d00\" stroke-width=\"6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"/>");
            }
            sb.append("</svg>");
        }
        sb.append("</div>");

        // TTD operator + QR
        String qrPayload = "TTD Operator Penandaan Lokasi Operasi:\nNama: "+NmPetugas.getText()+"\nNIP : "+KdPetugas.getText()+"\nTgl  : "+tglAcuan+"\nForm: RM 36a Penandaan Lokasi Operasi\nPasien: "+TPasien.getText()+" ("+TNoRM.getText()+")";
        String qrUrl = buildQrDataUrl(qrPayload, 130);
        sb.append("<div class=\"sig\"><div class=\"box\">Operator<br>");
        if (qrUrl!=null) sb.append("<img src=\"").append(qrUrl).append("\" style=\"width:90px;height:90px;margin:4px 0;\"><br>"); else sb.append("<br><br><br>");
        sb.append("(<b>").append(esc(NmPetugas.getText())).append("</b>)<br>NIP. ").append(esc(KdPetugas.getText())).append("</div></div>");

        sb.append("<div class=\"footer-rm\">RM.36a /Rev.001/01/2020</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    // ===== Field declarations =====
    private widget.InternalFrame internalFrame1;
    private widget.PanelBiasa panelHeader;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel panelBottom;
    private widget.panelisi panelBtm;
    private widget.Button BtnSimpan, BtnBaru, BtnEdit, BtnHapus, BtnPrint, BtnKeluar, BtnPetugas, BtnUndo, BtnClear;
    private widget.Label LCount;
    private widget.TextBox KdPetugas, NmPetugas;
    private widget.TextBox TNoRw, TNoRM, TPasien, TTglLahir, JK;
    private widget.TextBox TRuangan, TRencana, TKeterangan;
    private widget.Tanggal Tanggal;
    private widget.Table tb;
    private MarkPanel mark;
}
