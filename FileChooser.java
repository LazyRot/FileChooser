import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import javax.swing.*;

public class FileChooser extends JFrame{

    File file = new File("");
    JFileChooser jfc = new JFileChooser();
    JLabel lj = new JLabel("路径：");
    JTextField p1 = new JTextField(26);
    JButton open = new JButton("...");
    JButton upload = new JButton("上传文件");
    JButton back = new JButton("返回主页");

    public FileChooser() {
        setTitle("文件选择器");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(400, 200, 500, 200);
        this.setVisible(true);
        this.add(p1);
        this.add(open);
        this.add(lj);
        this.add(upload);
        this.add(back);

        p1.setEditable(false);
        p1.setText("可将文件拖入该窗口，或浏览文件");
        lj.setBounds(40, 40, 40, 30);
        lj.setFont(new Font("宋体", 0, 12));
        p1.setBounds(80, 40, 320, 30);
        open.setBounds(410, 40, 40, 30);
        back.setBounds(120, 100, 100, 30);
        upload.setBounds(260, 100, 100, 30);

        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                file = jfc.getSelectedFile();
                if (file.isDirectory()) {
                    p1.setText("文件夹:" + file.getAbsolutePath());
                } else if (file.isFile()) {
                    p1.setText("文件:" + file.getAbsolutePath());
                }
                System.out.println(jfc.getSelectedFile().getName());
            }
        });

        p1.setTransferHandler(new TransferHandler() {
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    p1.setText(filepath);
                    file = new File(filepath);

                    return true;
                } catch (Exception e) {

                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });

        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!file.isFile()) {
                    JOptionPane.showMessageDialog(null, "还未选择任何视频！");
                } else if (file.isFile()) {
                    File dest = new File("D:\\");
                    copyFile(file.getAbsolutePath(), dest + file.getName());
                    JOptionPane.showMessageDialog(null, "上传成功");
                    //JOptionPane.showMessageDialog(null,file.getName());
                }
            }
        });

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage();
            }
        });
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("上传出错");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileChooser();
    }
}