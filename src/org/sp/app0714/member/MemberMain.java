package org.sp.app0714.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MemberMain extends JFrame{
	
	//서쪽 영역 구성 컴포넌트들
	JPanel p_west;
	JTextField t_id;
	JTextField t_name;
	JTextField t_phone;
	JButton bt_regist;

	//센터 영역 구성 컴포넌트들
	JPanel p_center;
	JButton bt_select;
	JTextArea area;
	JScrollPane scroll;
	
	//오라클 db 접속 정보
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="java";
	String pass="1234";
	Connection con=null;
	
	public MemberMain() {
		p_west = new JPanel();
		t_id  = new JTextField();
		t_name = new JTextField();
		t_phone = new JTextField();
		bt_regist = new JButton("등록");
		
		p_center = new JPanel();
		bt_select = new JButton("조회");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		//스타일 지정
		//p_west.setBackground(Color.PINK);
		p_west.setPreferredSize(new Dimension(120, 500));
		//p_center.setBackground(Color.GRAY);
		p_center.setPreferredSize(new Dimension(480, 500));
		
		Dimension d= new Dimension(110, 40);
		t_id.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);
		
		//서쪽 영역 조립
		p_west.add(t_id);
		p_west.add(t_name);
		p_west.add(t_phone);
		p_west.add(bt_regist);
		
		area.setPreferredSize(new Dimension(420, 450));
		
		//센터 영역 조립
		p_center.add(bt_select);
		p_center.add(scroll);
		
		//패널 윈도우에 부착
		add(p_west, BorderLayout.WEST);
		add(p_center, BorderLayout.CENTER);
		
		setSize(600, 500);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				//db닫기, 각종 자원 닫기!
				try {
					if(con!=null) {
						con.close();						
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		//조회 버튼과 리스너 연결
		bt_select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getList();
			}
		});
		
		//등록 버튼과 리스너 연결
		bt_regist.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
		
		connect(); //오라클 접속
		
	}
	
	//회원 등록
	public void regist() {
		PreparedStatement pstmt=null;
		String id=t_id.getText();
		String name=t_name.getText();
		String phone=t_phone.getText();
		try {
			String sql="insert into member(member_idx, id, name, phone)";
			sql=sql+" values(seq_member.nextval, '"+id+"', '"+name+"', '"+phone+"')";
			pstmt=con.prepareStatement(sql);
			
			int result=pstmt.executeUpdate();
			if(result>0) {
				//조회 시도
				getList();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//회원 조회
	public void getList() {
		PreparedStatement pstmt=null; //소모적인 요소.
		ResultSet rs=null; //지역변수는 반드시 초기화
		
		try {
			String sql="select * from member";
			pstmt=con.prepareStatement(sql);
			
			//쿼리 실행(DDL, DML, DCL, select문)
			//select 문의 경우 아래의 executeQuery() 메서드를 이용해야 하며, 표를 표현하는 ResultSet을 반환받는다.
			//개발시 ResultSet은 DB의 표 자체를 표현한다고 인식.
			rs=pstmt.executeQuery();
			
			//커서란, 레코드를 가리키는 포인터
			while(rs.next()) { //커서 한 칸 전진		
				int idx=rs.getInt("member_idx");
				String id=rs.getString("id");
				String name=rs.getString("name");
				String phone=rs.getString("phone");
				
				//기존 데이터를 지우고 출력하자
				area.setText(""); 
				area.append(idx+", "+id+", "+name+", "+phone+"\n");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
		
	public void connect() {
		//1. 드라이버 로드
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로드 성공");
			
			//2. 접속
			con=DriverManager.getConnection(url, user, pass);
			if(con==null) {
				System.out.println("접속 실패");
			}else {
				System.out.println("접속 성공");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new MemberMain();
	}
}
