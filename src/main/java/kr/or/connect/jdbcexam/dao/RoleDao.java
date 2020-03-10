package kr.or.connect.jdbcexam.dao;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.or.connect.jdbcexam.dto.Role;

public class RoleDao {
	
	private static String dburl = "jdbc:mysql://localhost:3306/connectdb"; //DB연결에 필요한 정보를 상수화해서 사용
	private static String dbUser = "root";
	private static String dbpassword = "0000";
	
	public Role getRole(Integer roleId) {
		Role role = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try { //데이터베이스에 연결이 혹시 끊기는 등의 상황에 대해 예외를 처리해야한다.
			Class.forName("com.mysql.jdbc.Driver"); //데이터베이스 드라이버 로드
			conn = (Connection) DriverManager.getConnection(dburl, dbUser, dbpassword); //접속할 DB의 url, 유저, 비번
			String sql = "SELECT description, role_id FROM role WHERE role_id = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, roleId); //sql의 첫번째 ?에 roleId를 넣어준다.
			rs = ps.executeQuery(); //실행한다.
			
			if(rs.next()) { //다음 데이터가 있다면,
				String description = rs.getString(1); //첫번째 속성(컬럼)의 값(select 기준, 위의 sql문에서 첫번째 컬럼이 description임 )
				int id = rs.getInt("role_id");  //role_id 를 얻어온다. 속성명으로 얻어올 수 있다.
				role = new Role(id,description); //위 두 줄에서 얻어온 데이터로 Role 객체를 만든다. 
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally { //finally 구절은 어떤일이 있어도 반드시 실행된다.
			if(rs != null) {//rs, ps, conn 순서로 close를 해준다. 만약 위에서 conn까지만 할당되고 ps, rs 는 할당이 되지 않았을 수 있다. 그래서 rs가 null인지 체크한다.(ps, conn에도 똑같이 적용)
				try {
					rs.close(); //close라는 메소드도 예외를 발생시킬 수 있기에 예외처리를 한다. 
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(ps != null) {
				try {
					ps.close(); //close라는 메소드도 예외를 발생시킬 수 있기에 예외처리를 한다. 
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null) {
				try {
					conn.close(); //close라는 메소드도 예외를 발생시킬 수 있기에 예외처리를 한다. 
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return role;
	}
	
	public List<Role> getRoles(){
		List<Role> list = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver"); //데이터베이스 드라이버 로드
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT description, role_id FROM role order by role_id desc";
		try (Connection conn = (Connection) DriverManager.getConnection(dburl, dbUser, dbpassword); 
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql)){ //이렇게 작성하면, DB에서 할당받은 객체를 자동으로 close 한다. (보면 finally 문구가 없다.)
			
			try (ResultSet rs = ps.executeQuery()){ //여기서도 이렇게 쓰면 객체를 자동으로 close함
				
				while(rs.next()) { //튜플을 하나씩 꺼내온다. 더 이상 꺼낼 튜플이 없으면 false를 반환하여 종료됨.
					int id = rs.getInt("role_id"); //db 튜플에서 id를 꺼냄
					String description = rs.getString(1); // 튜플에서 description을 꺼냄
					Role role = new Role(id, description); // 꺼낸 데이터로 Role 객체를 만듬
					list.add(role); //리스트에 추가함
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public int addRole(Role role){
		int insertCount = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try { //데이터베이스에 연결이 혹시 끊기는 등의 상황에 대해 예외를 처리해야한다.
			Class.forName("com.mysql.jdbc.Driver"); //데이터베이스 드라이버 로드
			
			conn = (Connection) DriverManager.getConnection(dburl, dbUser, dbpassword); //접속할 DB의 url, 유저, 비번
			
			String sql = "INSERT INTO role (role_id, description) VALUES (?,?)";
			
			ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ps.setInt(1, role.getRoleId()); // 1번째 ?에 얻어온 Role 객체의 ID를 넣어줌
			ps.setString(2, role.getDescription()); //2번째 ?에 얻어온 Role 객체의 Description를 넣어줌
			
			insertCount = ps.executeUpdate(); //insert, update를 실행할때 이렇게함
		
		}catch(Exception e){
			e.printStackTrace();
		}finally { //finally 구절은 어떤일이 있어도 반드시 실행된다.
			if(ps != null) {
				try {
					ps.close(); 
				} catch (SQLException e) {}
			}
			
			if(conn != null) {
				try {
					conn.close(); 
				} catch (SQLException e) {}
			}
		}
		return insertCount;
	}
	
	
	public int deleteRole(Integer roleId){
		int deleteCount = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver"); //데이터베이스 드라이버 로드
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String sql = "DELETE FROM role WHERE role_id = ?";
		try (Connection conn = (Connection) DriverManager.getConnection(dburl, dbUser, dbpassword); 
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql)){ //이렇게 작성하면, 예외처리도 하고, DB에서 할당받은 객체를 자동으로 close 한다. (보면 finally 문구가 없다.)
		
				ps.setInt(1, roleId); //sql의 첫번째 ?에 roleId를 넣어준다.
				deleteCount = ps.executeUpdate(); //실행한다.
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return deleteCount;
	}
	
	public int updateRole(Role role){
		int updateCount = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver"); //데이터베이스 드라이버 로드
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String sql = "UPDATE role SET description = ? where role_id = ?";
		try (Connection conn = (Connection) DriverManager.getConnection(dburl, dbUser, dbpassword); 
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql)){ //이렇게 작성하면, 예외처리도 하고, DB에서 할당받은 객체를 자동으로 close 한다. (보면 finally 문구가 없다.)
				
				ps.setString(1,role.getDescription());
				ps.setInt(2, role.getRoleId()); //sql의 첫번째 ?에 roleId를 넣어준다.
				updateCount = ps.executeUpdate(); 
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return updateCount;
	}
}
