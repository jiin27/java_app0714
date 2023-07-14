package org.sp.app0714.table;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//자바의 GUI 컴포넌트 중, 표 형태의 레코드를 출력하기에 가장 최적화된 컴포넌트인 JTable을 이용해본다 
public class TableMain extends JFrame{
	JTable table;
	JScrollPane scroll;
	String[][] data= {
			{"1", "Junior", "로버트", "5621"},
			{"2", "Daniel", "jiyoon", "2154"},
			{"3", "Ariel", "jisu", "3651"}
	};
	String[] column = {"member_idx", "id", "name", "phone"};
	
	public TableMain() {
		table = new JTable(data, column);
		scroll = new JScrollPane(table);

		add(scroll);
		
		setVisible(true);
		setSize(400, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new TableMain();
	}

}
