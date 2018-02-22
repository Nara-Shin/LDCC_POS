package pos;

/*------------------------------------------------------------------
[ProductDAO JAVA]

Project    : LDCC_POS
Version : 1.0
Last change : 2018/02/22
Developer : Nara Shin
-------------------------------------------------------------------*/
/*------------------------------------------------------------------
 [Table of contents]

 1. public Connection getConn()
 
 -------------------------------------------------------------------*/

//이름 규칙 : 테이블명DAO , 테이블명DTO
//CRUD : Create;insert , Read;Select, Update, delete

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//DB 처리
public class ProductDAO {

	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/ldccpos";

	private static final String USERNAME = "root"; // DB ID
	private static final String PASSWORD = "intern05@ldcc"; // DB 패스워드

	private static Statement stmt;

	public ProductDAO() {

	}

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
	public List getProductCheck(ProductDTO vProd) {

		ProductDTO prod = new ProductDTO();

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과
		List<ProductDTO> result_list = new ArrayList<ProductDTO>();

		try {

			con = getConn();
			String sql = "select * from prod_info where prod_code=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, vProd.getProd_code());

			rs = ps.executeQuery();

			while (rs.next()) {
//				ProductDTO prod = new ProductDTO();
				String code = rs.getString("prod_code");
				String name = rs.getString("prod_name");
				int price = rs.getInt("prod_price");
				int weight = rs.getInt("prod_weight");
				int quantity = rs.getInt("prod_quantity");
				prod.setProd_code(code);
				prod.setProd_name(name);
				prod.setProd_price(price);
				prod.setProd_quantity(weight);
				prod.setProd_weight(quantity);
				result_list.add(prod);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}

	/** 품목리스트 출력 */
	public List getProductList() {

		// Vector data = new Vector(); // Jtable에 값을 쉽게 넣는 방법 1. 2차원배열 2. Vector 에
		// vector추가

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과
		List<ProductDTO> result_list = new ArrayList<ProductDTO>();
		try {

			con = getConn();
			String sql = "select * from prod_info order by prod_code asc";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ProductDTO prod = new ProductDTO();
				String code = rs.getString("prod_code");
				String name = rs.getString("prod_name");
				int price = rs.getInt("prod_price");
				int weight = rs.getInt("prod_weight");
				int quantity = rs.getInt("prod_quantity");
				prod.setProd_code(code);
				prod.setProd_name(name);
				prod.setProd_price(price);
				prod.setProd_quantity(weight);
				prod.setProd_weight(quantity);
				result_list.add(prod);
			} // while
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return data;
		return result_list;
	}

	/** 판매품목리스트 출력 */
	public List sellProductList(ProductDTO vProd) {
		System.out.println("dto=" + vProd.toStringUpdate());
		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과

		List<ProductDTO> result_list = new ArrayList<ProductDTO>();
		try {
			con = getConn();
			String sql = "select * from prod_info where prod_code=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, vProd.getProd_code());

			rs = ps.executeQuery();

			while (rs.next()) {
				ProductDTO prod = new ProductDTO();
				String code = rs.getString("prod_code");
				String name = rs.getString("prod_name");
				int price = rs.getInt("prod_price");
				int weight = rs.getInt("prod_weight");
				int quantity = rs.getInt("prod_quantity");
				prod.setProd_code(code);
				prod.setProd_name(name);
				prod.setProd_price(price);
				prod.setProd_quantity(weight);
				prod.setProd_weight(quantity);
				result_list.add(prod);
			} // while
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return data;
		return result_list;
	}

	/** 품목 결제 */
	public boolean sellProduct(ProductDTO vProd) {
		boolean ok = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			// javac ClientExam.java -encoding UTF-8
			// java -Dfile.encoding="UTF8" pos.ClientExam
			con = getConn();
			String sql = "UPDATE prod_info SET prod_quantity=prod_quantity-? WHERE prod_code=?";

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

	/** 품목 환불 */
	public boolean updateProduct(ProductDTO vProd) {
		System.out.println("dto=" + vProd.toStringUpdate());
		boolean ok = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			// javac ClientExam.java -encoding UTF-8
			// java -Dfile.encoding="UTF8" pos.ClientExam
			con = getConn();
			String sql = "UPDATE prod_info SET prod_quantity=prod_quantity+? WHERE prod_code=?";

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