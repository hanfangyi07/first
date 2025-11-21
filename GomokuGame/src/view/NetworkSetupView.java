// NetworkSetupView.java
package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkSetupView extends JFrame {
 private JTextField ipField;
 private JTextField portField;
 private JTextField usernameField;
 private JButton hostBtn;
 private JButton joinBtn;
 private JButton backBtn;
 
 public NetworkSetupView() {
     setTitle("网络对战设置");
     setSize(400, 350);
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setLocationRelativeTo(null);
     
     setLayout(new BorderLayout());
     
     // 标题
     JLabel titleLabel = new JLabel("网络对战设置", JLabel.CENTER);
     titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
     add(titleLabel, BorderLayout.NORTH);
     
     // 中央面板
     JPanel centerPanel = new JPanel();
     centerPanel.setLayout(new GridLayout(4, 2, 10, 10));
     
     centerPanel.add(new JLabel("用户名:"));
     usernameField = new JTextField("玩家" + (int)(Math.random() * 1000));
     centerPanel.add(usernameField);
     
     centerPanel.add(new JLabel("服务器IP:"));
     ipField = new JTextField("127.0.0.1");
     centerPanel.add(ipField);
     
     centerPanel.add(new JLabel("端口号:"));
     portField = new JTextField("8080");
     centerPanel.add(portField);
     
     centerPanel.add(new JLabel());
     centerPanel.add(new JLabel());
     
     add(centerPanel, BorderLayout.CENTER);
     
     // 按钮面板
     JPanel buttonPanel = new JPanel();
     buttonPanel.setLayout(new FlowLayout());
     
     hostBtn = new JButton("创建房间");
     joinBtn = new JButton("加入房间");
     backBtn = new JButton("返回");
     
     buttonPanel.add(hostBtn);
     buttonPanel.add(joinBtn);
     buttonPanel.add(backBtn);
     
     add(buttonPanel, BorderLayout.SOUTH);
     
     // 按钮事件
     hostBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             String username = usernameField.getText().trim();
             if (username.isEmpty()) {
                 username = "房主";
             }
             String portText = portField.getText().trim();
             int port = 8080;
             try {
                 port = Integer.parseInt(portText);
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(NetworkSetupView.this, 
                     "端口号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
                 return;
             }
             
             new OnlineGameView(username, "localhost", port, true);
             dispose();
         }
     });
     
     joinBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             String username = usernameField.getText().trim();
             if (username.isEmpty()) {
                 username = "玩家";
             }
             String host = ipField.getText().trim();
             String portText = portField.getText().trim();
             int port = 8080;
             try {
                 port = Integer.parseInt(portText);
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(NetworkSetupView.this, 
                     "端口号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
                 return;
             }
             
             new OnlineGameView(username, host, port, false);
             dispose();
         }
     });
     
     backBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             new StartView();
             dispose();
         }
     });
     
     setVisible(true);
 }
}
