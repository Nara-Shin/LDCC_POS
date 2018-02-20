package pos;

//이름 규칙 : 테이블명DAO , 테이블명DTO
//CRUD : Create;insert , Read;Select, Update, delete

import java.sql.*;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

//DB 처리
public class ProductDAO {

	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/ldccpos";

	private static final String USERNAME = "root"; // DB ID
	private static final String PASSWORD = "intern05@ldcc"; // DB 패스워드

	private static Statement stmt;
	// Product_List pList;

	public ProductDAO() {

	}

	// public ProductDAO(Product_List pList){
	// this.pList = pList;
	// System.out.println("DAO=>"+pList);
	// }

	/** DB연결 메소드 */
	public Connection getConn() {
		Connection con = null;

		try {
			Class.forName(JDBC_DRIVER); // 1. 드라이버 로딩
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // 2. 드라이버 연결

		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	/** 한 품목의 정보를 얻는 메소드 */
	public ProductDTO getProductDTO(String id) {

		ProductDTO dto = new ProductDTO();

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과

		try {

			con = getConn();
			String sql = "select * from tb_member where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				dto.setProd_code(rs.getString("prod_code"));
				dto.setProd_name(rs.getString("prod_name"));
				dto.setProd_price(rs.getInt("prod_price"));
				dto.setProd_weight(rs.getInt("prod_weight"));
				dto.setProd_quantity(rs.getInt("prod_quantity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	/** 품목리스트 출력 */
	public Vector getProductList() {

		Vector data = new Vector(); // Jtable에 값을 쉽게 넣는 방법 1. 2차원배열 2. Vector 에 vector추가

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과

		try {

			con = getConn();
			String sql = "select * from prod_info order by prod_name asc";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String code = rs.getString("prod_code");
				String name = rs.getString("prod_name");
				String price = rs.getString("prod_price");
				String weight = rs.getString("prod_weight");
				String quantity = rs.getString("prod_quantity");

				Vector row = new Vector();
				row.add(code);
				row.add(name);
				row.add(price);
				row.add(weight);
				row.add(quantity);

				data.add(row);
			} // while
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/** 품목 수정 */
	public boolean updateProduct(ProductDTO vProd) {
		System.out.println("dto=" + vProd.toStringUpdate());
		boolean ok = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			// javac ClientExam.java -encoding UTF-8
			// java -Dfile.encoding="UTF8" pos.ClientExam
			con = getConn();
			String sql = "UPDATE prod_info SET prod_quantity=? WHERE prod_code=?";

			System.out.println(ps);

			ps = con.prepareStatement(sql);

			ps.setInt(1, vProd.getProd_quantity());
			ps.setString(2, vProd.getProd_code());


			int r = ps.executeUpdate(); // 실행 -> 수정
			// 1~n: 성공 , 0 : 실패

			if (r > 0)
				ok = true; // 수정이 성공되면 ok값을 true로 변경

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ok;
	}

}