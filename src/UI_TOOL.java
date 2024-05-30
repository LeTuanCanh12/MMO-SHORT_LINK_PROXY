
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class UI_TOOL {
	static String filePath = " ";
	static MainTool tool = new MainTool();
	static Thread threadCheckSearch, threadCheckNewfeed;
	static Thread threadNewfeed, threadSearch;

	public static void main(String[] args) {
		// Tạo một JFrame để chứa giao diện
		JFrame frame = new JFrame("Donate momo: 0333283469");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Tạo JTabbedPane để chứa hai tab "Search" và "Newfeed"
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tạo panel cho tab "Search"
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(5, 1));

		// Tạo các thành phần cho panel "Search"
		JTextField importLink = new JTextField();
		JTextField timeOut = new JTextField();

		// Xử lý sự kiện khi người dùng nhấn nút "Browse"

		// Thêm các thành phần vào panel "Search"
		searchPanel.add(importLink);
		searchPanel.add(timeOut);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				try {
					tool.start(importLink.getText(), timeOut.getText());
				} catch (InterruptedException | IOException | ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		searchPanel.add(btnStart);

		JButton btnStop = new JButton("check");
		searchPanel.add(btnStop);

		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// Thêm panel "Search" vào JTabbedPane
		tabbedPane.addTab("Search", searchPanel);

		// Tạo panel cho tab "Newfeed"

		// Thêm panel "Newfeed" vào JTabbedPane

		// Thêm JTabbedPane vào JFrame
		frame.add(tabbedPane);

		// Thiết lập kích thước và hiển thị JFrame
		frame.setSize(400, 300);
		frame.setVisible(true);
	}

}