/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import des_encryption.DES_Encryption;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import views.MainForm;

public class MainController {

    MainForm view;
    String path;
    JFileChooser fileChooser;
    String text;
    long start_time;
    long end_time;
    DES_Encryption des = new DES_Encryption();

    public MainController() {
        path = "";
        view = new MainForm();
        checkPath();
        view.getBtnSelectFile().addActionListener(al -> selectFile());
        view.getRdoDES().addActionListener(al -> selectedDES());
        view.getRdo3DES().addActionListener(al -> selected3DES());
        view.getBtnSave().addActionListener(al -> saveFile());
    }

    public void selectFile() {
        fileChooser = new JFileChooser("D:\\");
        FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Text(.txt)", "txt");
        fileChooser.setFileFilter(textFilter);
        fileChooser.setMultiSelectionEnabled(false);

        int result = fileChooser.showDialog(view, "Chọn file");
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            path = f.toPath().toString().replace("\\", "\\\\");
            view.getLabPath().setText(fileChooser.getSelectedFile().toPath().toString());
            try {
                FileReader fr = new FileReader(path);
                int i;
                text = "";
                List<Character> charsList = new ArrayList<Character>();
                while ((i = fr.read()) != -1) {
                    charsList.add((char) i);
                }
                for (char c : charsList) {
                    text = text + c;
                }
                System.out.println(text);
                fr.close();
                checkPath();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void checkPath() {
        if (path != "") {
            view.getRdoDES().setEnabled(true);
            view.getRdo3DES().setEnabled(true);
//            view.getTxtKey1().setEnabled(true);
//            view.getTxtKey2().setEnabled(true);
//            view.getTxtKey3().setEnabled(true);
//            view.getBtnSave().setEnabled(true);
        } else {
            view.getRdoDES().setEnabled(false);
            view.getRdo3DES().setEnabled(false);
            view.getTxtKey1().setEnabled(false);
            view.getTxtKey2().setEnabled(false);
            view.getTxtKey3().setEnabled(false);
            view.getBtnSave().setEnabled(false);
        }
    }

    public void selectedDES() {
        view.getTxtKey1().setEnabled(true);
        view.getTxtKey2().setEnabled(false);
        view.getTxtKey3().setEnabled(false);
        view.getBtnCrypto().addActionListener(al -> {
            String key = view.getTxtKey1().getText();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập key!");
            } else {
                try {
                    System.out.println("------------------DES----------------");
                    view.getPanResult().setBorder(BorderFactory.createTitledBorder("Bản mã - DES"));
                    System.out.println("key: " + key);
                    start_time = System.nanoTime(); // lấy mốc thời gian bắt đầu 
                    String cipher = des.encrypt(key, des.utfToBin(text));
                    System.out.println("Kết quả mã hoá hệ 16: " + des.binToHex(cipher));
                    view.getAreaResult().setText(des.binToHex(cipher));
                    end_time = System.nanoTime(); // lấy mốc thời gian kết thúc
                    double time = (end_time - start_time) / 1e6;
                    view.getLabTime().setText(String.valueOf(time) + " ms");
                    view.getBtnSave().setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi input!");
                }
            }
        });

        view.getBtnDecryp().addActionListener(al -> {
            String key = view.getTxtKey1().getText();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập key!");
            } else if (!view.getRdoDES().isSelected() && !view.getRdo3DES().isSelected()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn kiểu mã hoá!");
            } else {
                try {
                    System.out.println("----------------DES----------------");
                    view.getPanResult().setBorder(BorderFactory.createTitledBorder("Bản rõ - DES"));
                    System.out.println("key: " + key);
                    start_time = System.nanoTime(); // lấy mốc thời gian bắt đầu
                    String decryp = des.decrypt(key, des.hexToBin(text));
                    System.out.println("Kết quả giải mã: " + des.binToUTF(decryp));
                    view.getAreaResult().setText(des.binToUTF(decryp));
                    end_time = System.nanoTime(); // lấy mốc thời gian kết thúc
                    double time = (end_time - start_time) / 1e6;
                    view.getLabTime().setText(String.valueOf(time) + " ms");
                    view.getBtnSave().setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi input!");
                }
            }
        });
    }

    public void selected3DES() {
        view.getTxtKey1().setEnabled(true);
        view.getTxtKey2().setEnabled(true);
        view.getTxtKey3().setEnabled(true);
        view.getBtnCrypto().addActionListener(al -> {
            String key1 = view.getTxtKey1().getText();
            String key2 = view.getTxtKey2().getText();
            String key3 = view.getTxtKey3().getText();
            if (((key1.isEmpty() || key2.isEmpty() || key3.isEmpty()) && view.getRdo3DES().isSelected()) || (key1.isEmpty() && view.getRdoDES().isSelected())) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ key!");
            } else {
                try {
                    System.out.println("--------------TRIPLE DES---------------");
                    view.getPanResult().setBorder(BorderFactory.createTitledBorder("Bản mã - TRIPLE DES"));
                    System.out.println("key1: " + key1);
                    System.out.println("key2: " + key2);
                    System.out.println("key3: " + key3);
                    start_time = System.nanoTime(); // lấy mốc thời gian bắt đầu 
                    String des_3_encry = des.encrypt(key1, des.decrypt(key2, des.encrypt(key3, des.utfToBin(text))));
                    System.out.println("Kết quả mã hoá 3DES hệ 16: " + des.binToHex(des_3_encry));
                    view.getAreaResult().setText(des.binToHex(des_3_encry));
                    end_time = System.nanoTime(); // lấy mốc thời gian kết thúc
                    double time = (end_time - start_time) / 1e6;
                    view.getLabTime().setText(String.valueOf(time) + " ms");
                    view.getBtnSave().setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi input!");
                }
            }
        });

        view.getBtnDecryp().addActionListener(al -> {
            String key1 = view.getTxtKey1().getText();
            String key2 = view.getTxtKey2().getText();
            String key3 = view.getTxtKey3().getText();
            if (((key1.isEmpty() || key2.isEmpty() || key3.isEmpty()) && view.getRdo3DES().isSelected()) || (key1.isEmpty() && view.getRdoDES().isSelected())) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ key!");
            } else if (!view.getRdoDES().isSelected() && !view.getRdo3DES().isSelected()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn kiểu mã hoá!");
            } else {
                try {
                    System.out.println("------------- TRIPLE DES----------------");
                    view.getPanResult().setBorder(BorderFactory.createTitledBorder("Bản rõ - TRIPLE DES"));
                    System.out.println("key1: " + key1);
                    System.out.println("key2: " + key2);
                    System.out.println("key3: " + key3);
                    start_time = System.nanoTime(); // lấy mốc thời gian bắt đầu
                    String des_3_decry = des.decrypt(key3, des.encrypt(key2, des.decrypt(key1, des.hexToBin(text))));
                    System.out.println("Kết quả giải mã 3DES: " + des.binToUTF(des_3_decry));
                    view.getAreaResult().setText(des.binToUTF(des_3_decry));
                    end_time = System.nanoTime(); // lấy mốc thời gian kết thúc
                    double time = (end_time - start_time) / 1e6;
                    view.getLabTime().setText(String.valueOf(time) + " ms");
                    view.getBtnSave().setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi input!");
                }
            }
        });
    }

    public void saveFile() {
        JFileChooser jFileChooser = new JFileChooser("D:\\");
        jFileChooser.setDialogTitle("Chọn file text: ");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = jFileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jFileChooser.getSelectedFile().isFile()) {
                System.out.println("Bạn đã chọn: " + jFileChooser.getSelectedFile());
                try {
                    FileWriter fw = new FileWriter(jFileChooser.getSelectedFile().toPath().toString().replace("\\", "\\\\"));
                    fw.write(view.getAreaResult().getText());
                    fw.close();
                    JOptionPane.showMessageDialog(view, "Đã lưu thành công!");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new MainController();
    }
}
