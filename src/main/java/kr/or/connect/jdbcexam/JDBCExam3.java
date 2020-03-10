package kr.or.connect.jdbcexam;

import kr.or.connect.jdbcexam.dao.RoleDao;
import kr.or.connect.jdbcexam.dto.Role;
import java.util.List;

public class JDBCExam3 {
	public static void main(String[] args) {
		RoleDao dao = new RoleDao();
		
		List<Role> list = dao.getRoles();
		
		for(Role role : list) {
			System.out.println(role);
		}
	}
}
