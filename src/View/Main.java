/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.LoanTermComparator;
import Controller.SavingTermComparator;
import Model.Customer;
import Model.Loan;
import Model.LoanInterest;
import Model.Saving;
import Model.SavingInterest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author nam_t
 */
public class Main extends javax.swing.JFrame {

    //TableModel của 4 bảng ds sổ tk, ds sổ vay, ds lãi suất tk, ds lãi suất vay
    DefaultTableModel modelS, modelL, modelSI, modelLI, modeRP1, modeRP2;
    TableColumn columnSI, columnLI;
    //ds sổ tk
    ArrayList<Saving> savingList = new ArrayList<>();
    //ds sổ vay
    ArrayList<Loan> loanList = new ArrayList<>();
    //ds khách hàng
    ArrayList<Customer> customerList = new ArrayList<>();
    //ds lãi suất tk
    ArrayList<SavingInterest> savingInterestList = new ArrayList<>();
    //ds lãi suất vay
    ArrayList<LoanInterest> loanInterestList = new ArrayList<>();
    //dữ liệu tab Cấu hình có thay đổi không
    boolean isDataUpdated = false;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        Menu.setTitle("Menu");
        Menu.setSize(500, 250);
        List.setTitle("Danh sách sổ tiết kiệm/vay lãi");
        List.setSize(800, 400);
        Spec.setTitle("Cấu hình");
        Spec.setSize(700, 500);
//        Repost.setTitle("Báo cáo");
//        Repost.setSize(700, 500);
        modelS = (DefaultTableModel) saving_list.getModel();
        modelL = (DefaultTableModel) loan_list.getModel();
        modelSI = (DefaultTableModel) saving_interest.getModel();
        modelLI = (DefaultTableModel) loan_interest.getModel();
//        modeRP1 = (DefaultTableModel) saving_list2.getModel();
//        modeRP2 = (DefaultTableModel) loan_list2.getModel();
        columnSI = saving_interest.getColumnModel().getColumn(1);
        columnLI = loan_interest.getColumnModel().getColumn(1);
        JComboBox amountCB = new JComboBox();
        amountCB.addItem("<100 triệu");
        amountCB.addItem("100 triệu - <250 triệu");
        amountCB.addItem("250 triệu - <500 triệu");
        amountCB.addItem("500 triệu - <1 tỷ");
        amountCB.addItem("1 tỷ - <3 tỷ");
        amountCB.addItem(">=3 tỷ");
        columnSI.setCellEditor(new DefaultCellEditor(amountCB));
        columnLI.setCellEditor(new DefaultCellEditor(amountCB));
        isDataUpdated = false;
        savingList = saving(null, null);
        loanList = loan(null, null);
    }

    //Hiển thị ds sổ tk, sổ vay
    public void showList() {
        modelS.setRowCount(0);
        modelL.setRowCount(0);
        for (Saving i : savingList) {
            modelS.addRow(new Object[]{
                i.getId(), i.getAmount(), i.getStartDate(), i.getTerm(),
                i.getInterest(), customer(i.getIdCustomer())
            });
        }
        for (Loan i : loanList) {
            modelL.addRow(new Object[]{
                i.getId(), i.getAmount(), i.getStartDate(), i.getTerm(),
                i.getInterest(), customer(i.getIdCustomer())
            });
        }
    }

    //Hiển thị Cấu hình
    public void showSpec(String version) {
        savingInterestList = savingInterest(version);
        loanInterestList = loanInterest(version);
        modelSI.setRowCount(0);
        modelLI.setRowCount(0);
        Collections.sort(savingInterestList, new SavingTermComparator());
        Collections.sort(loanInterestList, new LoanTermComparator());
        for (SavingInterest i : savingInterestList) {
            modelSI.addRow(new Object[]{
                i.getTerm(), i.getAmount(), i.getInterest()
            });
        }
        for (LoanInterest i : loanInterestList) {
            modelLI.addRow(new Object[]{
                i.getTerm(), i.getAmount(), i.getInterest()
            });
        }
        //Lấy ds phiên bản
        Connection con = getConnection();
        String query = "SELECT DISTINCT version FROM saving_interest ORDER BY version DESC LIMIT 3";
        PreparedStatement st;
        ResultSet rs;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            String versionItem;
            versionCB.removeAllItems();
            while (rs.next()) {
                versionItem = rs.getString(1);
                versionCB.addItem(versionItem);
            }
        } catch (Exception e) {
        }
        if (!version.equals("")) {
            versionCB.setSelectedItem(version);
        }
    }

    //Hiển thị báo cáo
//    public void showRepost(String ngay, int check){
//        savingList = saving(null, null);
//        loanList = loan();
//        modeRP1.setRowCount(0);
//        modeRP2.setRowCount(0);
//        Date date1 = null;
//        Date date2 = null;
//        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
//        try {
//            if(ngay == ""){
//                date1=new SimpleDateFormat("dd/MM/yyyy").parse(timeStamp);
//            }
//            else{
//                date1=new SimpleDateFormat("dd/MM/yyyy").parse(ngay);
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //String[] parts = timeStamp.split("/");
//        for (Saving i : savingList) {
//            try {
//                date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//            } catch (ParseException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            String[] words=i.getTerm().split("\\s");
//            Calendar cal = Calendar.getInstance(); 
//            cal.setTime(date2);
//            cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//            Date date3 = cal.getTime();
//            if(date3.compareTo(date1)==0){
//                modeRP1.addRow(new Object[]{
//                    i.getId(), i.getAmount(), i.getStartDate(), i.getTerm(),
//                    i.getInterest(), customer(i.getIdCustomer())
//                });
//            } 
//        }
//        for (Loan i : loanList) {
//            try {
//                date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//            } catch (ParseException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            String[] words=i.getTerm().split("\\s");
//            Calendar cal = Calendar.getInstance(); 
//            cal.setTime(date2);
//            cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//            Date date3 = cal.getTime();
//            if(date3.compareTo(date1)==0){
//                modeRP2.addRow(new Object[]{
//                    i.getId(), i.getAmount(), i.getStartDate(), i.getTerm(),
//                    i.getInterest(), customer(i.getIdCustomer())
//                });
//            }
//        }
//        int tongthu = 0;
//        int tongchi = 0;
//        if(check == 1){
//            // Xem theo ngày
//            for (Saving i : savingList) {
//                try {
//                    date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//                } catch (ParseException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String[] words=i.getTerm().split("\\s");
//                Calendar cal = Calendar.getInstance(); 
//                cal.setTime(date2);
//                cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//                Date date3 = cal.getTime();
//                if(date3.compareTo(date1)==0){
//                    tongchi += i.getAmount();
//                }
//                if(date2.compareTo(date1)==0){
//                    tongthu += i.getAmount();
//                }
//            }
//        
//            for (Loan i : loanList) {
//                try {
//                    date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//                } catch (ParseException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String[] words=i.getTerm().split("\\s");
//                Calendar cal = Calendar.getInstance(); 
//                cal.setTime(date2);
//                cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//                Date date3 = cal.getTime();
//                if(date3.compareTo(date1)==0){
//                    tongthu += i.getAmount();
//                }
//                if(date2.compareTo(date1)==0){
//                    tongchi += i.getAmount();
//                }
//            } 
//        }
//        
//        if(check == 2){
//            // Xem theo tháng
//            for (Saving i : savingList) {
//                try {
//                    date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//                } catch (ParseException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String[] words=i.getTerm().split("\\s");
//                Calendar cal = Calendar.getInstance(); 
//                cal.setTime(date2);
//                cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//                Date date3 = cal.getTime();
//                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                String[] lstStrDate3 = dateFormat.format(date3).split("/");
//                String[] lstStrDate1 = dateFormat.format(date1).split("/");
//                String[] lstStrDate2 = i.getStartDate().split("/");
//                if(lstStrDate1[1].contains(lstStrDate3[1])){
//                    tongchi += i.getAmount();
//                }
//                if(lstStrDate2[1].contains(lstStrDate1[1]) && lstStrDate2[2].contains(lstStrDate1[2])){
//                    tongthu += i.getAmount();
//                }
//            }
//        
//            for (Loan i : loanList) {
//                try {
//                    date2=new SimpleDateFormat("dd/MM/yyyy").parse(i.getStartDate());
//                } catch (ParseException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String[] words=i.getTerm().split("\\s");
//                Calendar cal = Calendar.getInstance(); 
//                cal.setTime(date2);
//                cal.add(Calendar.MONTH, Integer.parseInt(words[0])); 
//                Date date3 = cal.getTime();
//                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                String[] lstStrDate3 = dateFormat.format(date3).split("/");
//                String[] lstStrDate1 = dateFormat.format(date1).split("/");
//                String[] lstStrDate2 = i.getStartDate().split("/");
//                if(lstStrDate1[1].contains(lstStrDate3[1]) && lstStrDate1[2].contains(lstStrDate3[2])){
//                    tongthu += i.getAmount();
//                }
//                if(lstStrDate2[1].contains(lstStrDate1[1]) && lstStrDate2[2].contains(lstStrDate1[2])){
//                    tongchi += i.getAmount();
//                }
//            } 
//        }
//        KetQuaThu.setText(Integer.toString(tongthu)+ " Đồng");
//        KetQuaChi.setText(Integer.toString(tongchi)+ " Đồng");
//    }
    //Kết nối DB
    public Connection getConnection() {
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bank?useUnicode=true&characterEncoding=UTF-8", "root", "");
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    // Lấy ds sổ tk từ DB
    public ArrayList<Saving> saving(String dateStart, String dateEnd) {
        ArrayList<Saving> savingList = new ArrayList<>();
        Connection con = getConnection();
        String query = "";
        PreparedStatement st;
        ResultSet rs;
        try {
            if (dateStart == null || dateEnd == null) {
                query = "SELECT * FROM saving";
                st = con.prepareStatement(query);
                rs = st.executeQuery();
            } else {
                query = "SELECT * FROM saving WHERE start_date >= ? AND start_date <= ?";
                st = con.prepareStatement(query);
                st.setString(1, dateStart);
                st.setString(2, dateEnd);
                rs = st.executeQuery();
            }
            if (rs.next() == false) {
                JOptionPane.showMessageDialog(null, "Không có kết quả cần tìm!");
            } else {
                Saving saving;
                do {
                    saving = new Saving(rs.getInt(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                    savingList.add(saving);
                } while (rs.next());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối với máy chủ. Thử lại sau!");
            List.setVisible(false);
        }
        return savingList;
    }

    //Lấy ds sổ vay từ DB
    public ArrayList<Loan> loan(String dateStart, String dateEnd) {
        ArrayList<Loan> loanList = new ArrayList<>();
        Connection con = getConnection();
        String query = "";
        PreparedStatement st;
        ResultSet rs;
        try {
            if (dateStart == null || dateEnd == null) {
                query = "SELECT * FROM loan";
                st = con.prepareStatement(query);
                rs = st.executeQuery();
            } else {
                query = "SELECT * FROM loan WHERE start_date >= ? AND start_date <= ?";
                st = con.prepareStatement(query);
                st.setString(1, dateStart);
                st.setString(2, dateEnd);
                rs = st.executeQuery();
            }
            System.out.println(st.toString());
            if (rs.next() == false) {
                JOptionPane.showMessageDialog(null, "Không có kết quả cần tìm!");
            } else {
                Loan loan;
                do {
                    loan = new Loan(rs.getInt(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                    loanList.add(loan);
                } while (rs.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Mất kết nối với máy chủ. Thử lại sau!");
            List.setVisible(false);
        }
        return loanList;
    }

    //Lấy ds khách hàng từ DB
    public String customer(int i) {
        Connection con = getConnection();
        String query = "SELECT * FROM customer WHERE id=?";
        Customer cus = new Customer();
        PreparedStatement st;
        ResultSet rs;
        try {
            st = con.prepareStatement(query);
            st.setInt(1, i);
            rs = st.executeQuery();
            while (rs.next()) {
                cus = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (Exception e) {
        }
        return cus.getName();
    }

    //Lấy ds lãi suất tk
    public ArrayList<SavingInterest> savingInterest(String version) {
        ArrayList<SavingInterest> savingInterestList = new ArrayList<>();
        Connection con = getConnection();
        String query = "";
        if (version.equals("")) {
            query = "SELECT * FROM saving_interest WHERE version=(SELECT MAX(version) FROM saving_interest) ORDER BY amount";
        } else {
            query = "SELECT * FROM saving_interest WHERE version='" + version + "' ORDER BY amount";
        }
        PreparedStatement st;
        ResultSet rs;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            SavingInterest si;
            while (rs.next()) {
                si = new SavingInterest(rs.getString(2), rs.getString(3), rs.getString(4));
                savingInterestList.add(si);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối với máy chủ. Thử lại sau!");
            Spec.setVisible(false);
        }
        return savingInterestList;
    }

    //Lấy ds lãi suât vay
    public ArrayList<LoanInterest> loanInterest(String version) {
        ArrayList<LoanInterest> loanInterestList = new ArrayList<>();
        Connection con = getConnection();
        String query = "";
        if (version.equals("")) {
            query = "SELECT * FROM loan_interest WHERE version=(SELECT MAX(version) FROM loan_interest) ORDER BY amount";
        } else {
            query = "SELECT * FROM loan_interest WHERE version='" + version + "' ORDER BY amount";
        }
        PreparedStatement st;
        ResultSet rs;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            LoanInterest li;
            while (rs.next()) {
                li = new LoanInterest(rs.getString(2), rs.getString(3), rs.getString(4));
                loanInterestList.add(li);
            }
        } catch (Exception e) {
        }
        return loanInterestList;
    }

    //Lưu Cấu hình
    public void updateInterestList() {
        Connection con = getConnection();
        String query = "";
        PreparedStatement st;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
            String version = dateFormat.format(new Date());
            query = "INSERT INTO saving_interest VALUES (?,?,?,?,?)";
            for (int i = 0; i < saving_interest.getRowCount(); i++) {
                st = con.prepareStatement(query);
                st.setString(1, null);
                st.setString(2, saving_interest.getValueAt(i, 0).toString());
                st.setString(3, saving_interest.getValueAt(i, 1).toString());
                st.setString(4, saving_interest.getValueAt(i, 2).toString());
                st.setString(5, version);
                st.executeUpdate();
            }
            query = "INSERT INTO loan_interest VALUES (?,?,?,?,?)";
            for (int i = 0; i < loan_interest.getRowCount(); i++) {
                st = con.prepareStatement(query);
                st.setString(1, null);
                st.setString(2, loan_interest.getValueAt(i, 0).toString());
                st.setString(3, loan_interest.getValueAt(i, 1).toString());
                st.setString(4, loan_interest.getValueAt(i, 2).toString());
                st.setString(5, version);
                st.executeUpdate();
            }
        } catch (Exception e) {
        }
        JOptionPane.showMessageDialog(null, "Lưu thành công!");
    }

    //Kiểm tra tính hợp lệ của kỳ hạn
    public boolean TermValidate(String term) {
        String n, m;
        if (term.equals("Không kỳ hạn")) {
            return true;
        } else {
            try {
                n = term.split(" ")[0];
                m = term.split(" ")[1];
                if (Integer.parseInt(n) >= 1 && Integer.parseInt(n) <= 36 && m.equals("tháng")) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    //Kiểm tra tính hợp lệ của lãi suất
    public boolean FloatValidate(String interest) {
        return interest.matches("^[1-9]\\.[0-9][0-9]");
    }

    //Kiểm tra tính hợp lệ của cấu hình
    public boolean InterestValidate() {
        ArrayList<String> savingInterestList2 = new ArrayList<>();
        ArrayList<String> loanInterestList2 = new ArrayList<>();
        for (int i = 0; i < saving_interest.getRowCount(); i++) {
            if (!TermValidate(saving_interest.getValueAt(i, 0).toString())) {
                JOptionPane.showMessageDialog(null, "Nhập lại kỳ hạn theo định dạng: 'x tháng' (1<=x<=36) hoặc 'Không kỳ hạn'");
                saving_interest.requestFocus();
                saving_interest.editCellAt(i, 0);
                return false;
            }
            if (saving_interest.getValueAt(i, 1).equals("")) {
                JOptionPane.showMessageDialog(null, "Phải chọn mức tiền!");
                saving_interest.requestFocus();
                saving_interest.editCellAt(i, 1);
                return false;
            }
            if (saving_interest.getValueAt(i, 2).toString().matches("^[1-9]\\.[0-9]")) {
                saving_interest.setValueAt(saving_interest.getValueAt(i, 2).toString() + "0", i, 2);
            }
            if (!FloatValidate(saving_interest.getValueAt(i, 2).toString())) {
                JOptionPane.showMessageDialog(null, "Nhập lại lãi suất theo định dạng a.bc (0<a<10)");
                saving_interest.requestFocus();
                saving_interest.editCellAt(i, 2);
                return false;
            }
            if (saving_interest.getValueAt(i, 0).toString().split(" ")[0].matches("^[1-9]")) {
                saving_interest.setValueAt("0" + saving_interest.getValueAt(i, 0).toString(), i, 0);
            }
            savingInterestList2.add(new String(saving_interest.getValueAt(i, 0).toString() + saving_interest.getValueAt(i, 1).toString()));
        }
        for (int i = 0; i < loan_interest.getRowCount(); i++) {
            if (!TermValidate(loan_interest.getValueAt(i, 0).toString())) {
                JOptionPane.showMessageDialog(null, "Nhập lại kỳ hạn theo định dạng: 'x tháng' (1<=x<=36) hoặc 'Không kỳ hạn'");
                loan_interest.requestFocus();
                loan_interest.editCellAt(i, 0);
                return false;
            }
            if (loan_interest.getValueAt(i, 1).equals("")) {
                JOptionPane.showMessageDialog(null, "Phải chọn mức tiền!");
                loan_interest.requestFocus();
                loan_interest.editCellAt(i, 1);
                return false;
            }
            if (loan_interest.getValueAt(i, 2).toString().matches("^[1-9]\\.[0-9]")) {
                loan_interest.setValueAt(loan_interest.getValueAt(i, 2).toString() + "0", i, 2);
            }
            if (!FloatValidate(loan_interest.getValueAt(i, 2).toString())) {
                JOptionPane.showMessageDialog(null, "Nhập lại lãi suất theo định dạng a.bc (0<a<10)");
                loan_interest.requestFocus();
                loan_interest.editCellAt(i, 2);
                return false;
            }
            if (loan_interest.getValueAt(i, 0).toString().split(" ")[0].matches("^[1-9]")) {
                loan_interest.setValueAt("0" + loan_interest.getValueAt(i, 0).toString(), i, 0);
            }
            loanInterestList2.add(new String(loan_interest.getValueAt(i, 0).toString() + loan_interest.getValueAt(i, 1).toString()));
        }
        Set<String> set1 = new HashSet<String>(savingInterestList2);
        Set<String> set2 = new HashSet<String>(loanInterestList2);
        if (set1.size() < savingInterestList2.size()) {
            JOptionPane.showMessageDialog(null, "Kỳ hạn tiết kiệm bị lặp!");
            return false;
        }
        if (set2.size() < loanInterestList2.size()) {
            JOptionPane.showMessageDialog(null, "Kỳ hạn vay bị lặp!");
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu = new javax.swing.JFrame();
        jLabel4 = new javax.swing.JLabel();
        btn_list = new javax.swing.JButton();
        btn_report = new javax.swing.JButton();
        btn_spec = new javax.swing.JButton();
        List = new javax.swing.JFrame();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jsp_list_saving = new javax.swing.JScrollPane();
        saving_list = new javax.swing.JTable();
        jsp_list_loan = new javax.swing.JScrollPane();
        loan_list = new javax.swing.JTable();
        btn_back1 = new javax.swing.JButton();
        jdc_dateStart = new com.toedter.calendar.JDateChooser();
        jdc_dateEnd = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btn_xem_danh_sach = new javax.swing.JButton();
        Spec = new javax.swing.JFrame();
        btn_back2 = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        loan_interest = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        saving_interest = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        add_saving = new javax.swing.JButton();
        delete_saving = new javax.swing.JButton();
        delete_loan = new javax.swing.JButton();
        add_loan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        versionCB = new javax.swing.JComboBox<>();
        btn_versionSelect = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        txt_password = new javax.swing.JPasswordField();
        btn_login = new javax.swing.JButton();

        Menu.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Menu");

        btn_list.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_list.setText("Danh sách sổ tiết kiệm/ vay lãi");
        btn_list.setToolTipText("");
        btn_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_listMouseClicked(evt);
            }
        });
        btn_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_listActionPerformed(evt);
            }
        });

        btn_report.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_report.setText("Báo cáo");

        btn_spec.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_spec.setText("Cấu hình");
        btn_spec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_specMouseClicked(evt);
            }
        });
        btn_spec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_specActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu.getContentPane());
        Menu.getContentPane().setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLayout.createSequentialGroup()
                            .addComponent(btn_report, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_spec, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btn_list, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_list, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_report, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_spec, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        List.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        saving_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Số tiền", "Ngày gửi", "Kỳ hạn", "Lãi suất", "Người gửi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jsp_list_saving.setViewportView(saving_list);
        if (saving_list.getColumnModel().getColumnCount() > 0) {
            saving_list.getColumnModel().getColumn(0).setResizable(false);
            saving_list.getColumnModel().getColumn(0).setPreferredWidth(50);
            saving_list.getColumnModel().getColumn(1).setResizable(false);
            saving_list.getColumnModel().getColumn(1).setPreferredWidth(150);
            saving_list.getColumnModel().getColumn(2).setResizable(false);
            saving_list.getColumnModel().getColumn(2).setPreferredWidth(150);
            saving_list.getColumnModel().getColumn(3).setResizable(false);
            saving_list.getColumnModel().getColumn(3).setPreferredWidth(100);
            saving_list.getColumnModel().getColumn(4).setResizable(false);
            saving_list.getColumnModel().getColumn(4).setPreferredWidth(100);
            saving_list.getColumnModel().getColumn(5).setResizable(false);
            saving_list.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jTabbedPane1.addTab("Danh sách sổ tiết kiệm", jsp_list_saving);

        loan_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Số tiền", "Ngày vay", "Kỳ hạn", "Lãi suất", "Người vay"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jsp_list_loan.setViewportView(loan_list);
        if (loan_list.getColumnModel().getColumnCount() > 0) {
            loan_list.getColumnModel().getColumn(0).setResizable(false);
            loan_list.getColumnModel().getColumn(0).setPreferredWidth(50);
            loan_list.getColumnModel().getColumn(1).setResizable(false);
            loan_list.getColumnModel().getColumn(1).setPreferredWidth(150);
            loan_list.getColumnModel().getColumn(2).setResizable(false);
            loan_list.getColumnModel().getColumn(2).setPreferredWidth(150);
            loan_list.getColumnModel().getColumn(3).setResizable(false);
            loan_list.getColumnModel().getColumn(3).setPreferredWidth(100);
            loan_list.getColumnModel().getColumn(4).setResizable(false);
            loan_list.getColumnModel().getColumn(4).setPreferredWidth(100);
            loan_list.getColumnModel().getColumn(5).setResizable(false);
            loan_list.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jTabbedPane1.addTab("Danh sách sổ vay lãi", jsp_list_loan);

        btn_back1.setText("Quay lại");
        btn_back1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_back1MouseClicked(evt);
            }
        });
        btn_back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Từ ngày");

        jLabel9.setText("Đến ngày");

        btn_xem_danh_sach.setText("Xem danh sách");
        btn_xem_danh_sach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xem_danh_sachActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListLayout = new javax.swing.GroupLayout(List.getContentPane());
        List.getContentPane().setLayout(ListLayout);
        ListLayout.setHorizontalGroup(
            ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addGroup(ListLayout.createSequentialGroup()
                        .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jdc_dateStart, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jdc_dateEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(38, 38, 38)
                        .addComponent(btn_xem_danh_sach)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_back1)))
                .addContainerGap())
        );
        ListLayout.setVerticalGroup(
            ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_back1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ListLayout.createSequentialGroup()
                            .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jdc_dateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(7, 7, 7)
                            .addGroup(ListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jdc_dateEnd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ListLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_xem_danh_sach))))
                .addContainerGap())
        );

        Spec.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_back2.setText("Quay lại");
        btn_back2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_back2MouseClicked(evt);
            }
        });

        btn_save.setText("Lưu");
        btn_save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_saveMouseClicked(evt);
            }
        });
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        loan_interest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kỳ hạn", "Mức tiền", "Lãi suất"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        loan_interest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loan_interestMouseExited(evt);
            }
        });
        jScrollPane3.setViewportView(loan_interest);
        if (loan_interest.getColumnModel().getColumnCount() > 0) {
            loan_interest.getColumnModel().getColumn(0).setResizable(false);
            loan_interest.getColumnModel().getColumn(0).setPreferredWidth(80);
            loan_interest.getColumnModel().getColumn(1).setResizable(false);
            loan_interest.getColumnModel().getColumn(1).setPreferredWidth(160);
            loan_interest.getColumnModel().getColumn(2).setResizable(false);
            loan_interest.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        saving_interest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kỳ hạn", "Mức tiền", "Lãi suất"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        saving_interest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saving_interestMouseExited(evt);
            }
        });
        jScrollPane4.setViewportView(saving_interest);
        if (saving_interest.getColumnModel().getColumnCount() > 0) {
            saving_interest.getColumnModel().getColumn(0).setResizable(false);
            saving_interest.getColumnModel().getColumn(0).setPreferredWidth(80);
            saving_interest.getColumnModel().getColumn(1).setResizable(false);
            saving_interest.getColumnModel().getColumn(1).setPreferredWidth(160);
            saving_interest.getColumnModel().getColumn(2).setResizable(false);
            saving_interest.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Lãi suất tiết kiệm:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Lãi suất vay:");

        add_saving.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add_saving.setText("+");
        add_saving.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_savingMouseClicked(evt);
            }
        });

        delete_saving.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        delete_saving.setText("-");
        delete_saving.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                delete_savingMouseClicked(evt);
            }
        });

        delete_loan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        delete_loan.setText("-");
        delete_loan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                delete_loanMouseClicked(evt);
            }
        });

        add_loan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add_loan.setText("+");
        add_loan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_loanMouseClicked(evt);
            }
        });
        add_loan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_loanActionPerformed(evt);
            }
        });

        jLabel7.setText("Phiên bản:");

        versionCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                versionCBItemStateChanged(evt);
            }
        });
        versionCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionCBActionPerformed(evt);
            }
        });

        btn_versionSelect.setText("Chọn");
        btn_versionSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_versionSelectMouseClicked(evt);
            }
        });
        btn_versionSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_versionSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SpecLayout = new javax.swing.GroupLayout(Spec.getContentPane());
        Spec.getContentPane().setLayout(SpecLayout);
        SpecLayout.setHorizontalGroup(
            SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SpecLayout.createSequentialGroup()
                .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SpecLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addGroup(SpecLayout.createSequentialGroup()
                                .addComponent(add_loan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delete_loan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(SpecLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btn_save)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(versionCB, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_versionSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_back2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SpecLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(add_saving, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete_saving, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        SpecLayout.setVerticalGroup(
            SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SpecLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add_saving)
                    .addComponent(delete_saving)
                    .addComponent(add_loan)
                    .addComponent(delete_loan))
                .addGap(32, 32, 32)
                .addGroup(SpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back2)
                    .addComponent(btn_save)
                    .addComponent(jLabel7)
                    .addComponent(versionCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_versionSelect))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng nhập");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tên người dùng:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Mật khẩu:");

        txt_username.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_username.setText("namtran");

        txt_password.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_password.setText("12345678");

        btn_login.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_login.setText("Đăng nhập");
        btn_login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_loginMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_username, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(txt_password)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(btn_login)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btn_login)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//        jRadioButton1.setSelected(true);
//        jRadioButton2.setSelected(false);
//        String text = NgayThang.getText();
//        if(!text.isEmpty()){
//            if(checkngaythang(text)){
//                showRepost(text, 1);
//                isDataUpdated = true;;
//            }
//            else {
//                JOptionPane.showMessageDialog(null, "Sai định dạng ngày tháng. Ngày tháng có dạng dd/MM/yyyy");
//            }
//        }
//        else{
//            showRepost("", 1);
//            isDataUpdated = true;;
//        }
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
//        jRadioButton1.setSelected(false);
//        jRadioButton2.setSelected(true);
//        String text = NgayThang.getText();
//        if(!text.isEmpty()){
//            if(checkngaythang(text)){
//                showRepost(text, 2);
//                isDataUpdated = true;;
//            }
//            else {
//                JOptionPane.showMessageDialog(null, "Sai định dạng ngày tháng. Ngày tháng có dạng dd/MM/yyyy");
//            }
//        }
//        else{
//            showRepost("", 2);
//            isDataUpdated = true;;
//        }
    }

    private void btn_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_listActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_listActionPerformed

    private void btn_reportActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void OKActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_back1ActionPerformed
    private void btn_back3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    //Nhấn Đăng nhập
    private void btn_loginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_loginMouseClicked
        // TODO add your handling code here:
        String query;
        String dbUsername, dbPassword;
        boolean login = false;
        Connection con = getConnection();
        query = "SELECT username, password FROM user;";
        if (txt_username.getText().isEmpty() || txt_password.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Phải nhập tên đăng nhập và mật khẩu!");

        } else {
            try {
                PreparedStatement st = con.prepareStatement(query);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    dbUsername = rs.getString("username");
                    dbPassword = rs.getString("password");
                    if (dbUsername.equals(txt_username.getText()) && !dbPassword.equals(txt_password.getText())) {
                        JOptionPane.showMessageDialog(null, "Sai mật khẩu!");
                        txt_password.setText("");
                        txt_password.requestFocus();
                    } else if (dbUsername.equals(txt_username.getText()) && dbPassword.equals(txt_password.getText())) {
                        login = true;
                        this.setVisible(false);
                        Menu.setVisible(true);
                        Menu.setLocationRelativeTo(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Sai tên đăng nhập!");
                        txt_username.requestFocus();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Mất kết nối với máy chủ. Thử lại sau!");
            }
        }
    }//GEN-LAST:event_btn_loginMouseClicked
    //Nhấn Xem ds sổ tk/vay
    private void btn_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_listMouseClicked
        // TODO add your handling code here:
        List.setVisible(true);
        List.setLocationRelativeTo(null);
        showList();
    }//GEN-LAST:event_btn_listMouseClicked

    private void btn_reportMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
//        Repost.setVisible(true);
//        Repost.setLocationRelativeTo(null);
//        jRadioButton1.setSelected(true);
//        showRepost("", 1);
    }

    // Chọn ngày tháng
    private void OKMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
//        String text = NgayThang.getText();
//        if(!text.isEmpty()){
//            if(checkngaythang(text)){
//                showRepost(text, 1);
//                isDataUpdated = true;;
//            }
//            else {
//                JOptionPane.showMessageDialog(null, "Sai định dạng ngày tháng. Ngày tháng có dạng dd/MM/yyyy");
//            }
//        }
//        else {
//            showRepost("", 1);
//            isDataUpdated = true;;
//        }

    }

    //Nhấn Cấu hình
    private void btn_specMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_specMouseClicked
        // TODO add your handling code here:
        Spec.setVisible(true);
        Spec.setLocationRelativeTo(null);
        showSpec("");
    }//GEN-LAST:event_btn_specMouseClicked
    //Nhấn quay lại tại Xem ds sổ tk/vay
    private void btn_back1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back1MouseClicked
        // TODO add your handling code here:
        List.setVisible(false);
    }//GEN-LAST:event_btn_back1MouseClicked

    private void btn_back3MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
//        Repost.setVisible(false);
    }

    //Nhấn quay lại tại Cấu hình
    private void btn_back2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back2MouseClicked
        // TODO add your handling code here:
        if (isDataUpdated) {
            int n = JOptionPane.showConfirmDialog(null, "Lưu lại trước khi thoát?", "Xác nhận", JOptionPane.YES_NO_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION && InterestValidate()) {
                updateInterestList();
                Spec.setVisible(false);
                isDataUpdated = false;
            } else if (n == JOptionPane.NO_OPTION) {
                Spec.setVisible(false);
                isDataUpdated = false;
            } else {
                isDataUpdated = true;
            }
        } else {
            Spec.setVisible(false);
        }
        isDataUpdated = false;
    }//GEN-LAST:event_btn_back2MouseClicked
    //Thêm hàng vào bảng ds lãi suất tk
    private void add_savingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_savingMouseClicked
        // TODO add your handling code here:
        modelSI.addRow(new Object[]{"", ""});
    }//GEN-LAST:event_add_savingMouseClicked
    //Xóa hàng vào bảng ds lãi suất tk
    private void delete_savingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delete_savingMouseClicked
        // TODO add your handling code here:
        int i = saving_interest.getSelectedRow();
        if (i != -1) {
            modelSI.removeRow(i);
        } else {
            JOptionPane.showMessageDialog(null, "Chọn hàng cần xóa!");
        }
    }//GEN-LAST:event_delete_savingMouseClicked

    private void saving_interestMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saving_interestMouseExited
        // TODO add your handling code here:
        isDataUpdated = true;
    }//GEN-LAST:event_saving_interestMouseExited
    //Nhấn nút Lưu tại Cấu hình
    private void btn_saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseClicked
        // TODO add your handling code here:
        if (saving_interest.isEditing()) {
            saving_interest.getCellEditor().stopCellEditing();
        }
        if (loan_interest.isEditing()) {
            loan_interest.getCellEditor().stopCellEditing();
        }
        int n = JOptionPane.showConfirmDialog(null, "Lưu lại?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION && loan_interest.getRowCount() != 0 && saving_interest.getRowCount() != 0 && InterestValidate()) {
            try {
                Connection con = getConnection();
                PreparedStatement st;
                String query = "DELETE FROM loan_interest WHERE version='" + versionCB.getItemAt(2) + "'";
                st = con.prepareStatement(query);
                st.executeUpdate();
                query = "DELETE FROM saving_interest WHERE version='" + versionCB.getItemAt(2) + "'";
                st = con.prepareStatement(query);
                st.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
            updateInterestList();
            showSpec("");
            isDataUpdated = false;
        } else {
            isDataUpdated = true;
        }
    }//GEN-LAST:event_btn_saveMouseClicked
    //Thêm hàng vào bảng ds lãi suất vay
    private void add_loanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_loanMouseClicked
        // TODO add your handling code here:
        modelLI.addRow(new Object[]{"", ""});
    }//GEN-LAST:event_add_loanMouseClicked
    //Xóa hàng vào bảng ds lãi suất vay
    private void delete_loanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delete_loanMouseClicked
        // TODO add your handling code here:
        int i = loan_interest.getSelectedRow();
        if (i != -1) {
            modelLI.removeRow(i);
        } else {
            JOptionPane.showMessageDialog(null, "Chọn hàng cần xóa!");
        }
    }//GEN-LAST:event_delete_loanMouseClicked

    private void btn_specActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_specActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_specActionPerformed

    private void add_loanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_loanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_add_loanActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_saveActionPerformed

    private void versionCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versionCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_versionCBActionPerformed

    private void versionCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_versionCBItemStateChanged
        // TODO add your handling code here:   
    }//GEN-LAST:event_versionCBItemStateChanged
    //Chọn phiên bản
    private void btn_versionSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_versionSelectMouseClicked
        // TODO add your handling code here:
        int n = JOptionPane.showConfirmDialog(null, "Quay lại bản " + versionCB.getSelectedItem() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            showSpec(versionCB.getSelectedItem().toString());
            isDataUpdated = true;
        }
    }//GEN-LAST:event_btn_versionSelectMouseClicked

    private void btn_versionSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_versionSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_versionSelectActionPerformed

    private void loan_interestMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loan_interestMouseExited
        // TODO add your handling code here:
        isDataUpdated = true;
    }//GEN-LAST:event_loan_interestMouseExited

    private void btn_xem_danh_sachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xem_danh_sachActionPerformed
        // TODO add your handling code here:
        System.out.println(jsp_list_loan.isShowing());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStart = format.format(jdc_dateStart.getDate());
        String dateEnd = format.format(jdc_dateEnd.getDate());
        if (jsp_list_saving.isShowing()) {
            savingList = saving(dateStart, dateEnd);
        } else if(jsp_list_loan.isShowing()){
            loanList = loan(dateStart, dateEnd);
        }
        showList();
    }//GEN-LAST:event_btn_xem_danh_sachActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main m = new Main();
                m.setTitle("Quản lý sổ tiết kiệm/vay lãi");
                m.setVisible(true);
                m.setLocationRelativeTo(null);
            }
        });
    }

    private boolean checkngaythang(String ngay) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date check = null;
        try {
            check = df.parse(ngay);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame List;
    private javax.swing.JFrame Menu;
    private javax.swing.JFrame Spec;
    private javax.swing.JButton add_loan;
    private javax.swing.JButton add_saving;
    private javax.swing.JButton btn_back1;
    private javax.swing.JButton btn_back2;
    private javax.swing.JButton btn_list;
    private javax.swing.JButton btn_login;
    private javax.swing.JButton btn_report;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_spec;
    private javax.swing.JButton btn_versionSelect;
    private javax.swing.JButton btn_xem_danh_sach;
    private javax.swing.JButton delete_loan;
    private javax.swing.JButton delete_saving;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdc_dateEnd;
    private com.toedter.calendar.JDateChooser jdc_dateStart;
    private javax.swing.JScrollPane jsp_list_loan;
    private javax.swing.JScrollPane jsp_list_saving;
    private javax.swing.JTable loan_interest;
    private javax.swing.JTable loan_list;
    private javax.swing.JTable saving_interest;
    private javax.swing.JTable saving_list;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_username;
    private javax.swing.JComboBox<String> versionCB;
    // End of variables declaration//GEN-END:variables
}
