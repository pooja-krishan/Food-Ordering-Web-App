package com.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.io.*;

public class Dao {
	
	private static Connection connection = null;
	
	String dbUrl="jdbc:mysql://localhost:3306/p2_shopping_cart";
	String user="root";
	String pwd="pizza4U*";
	
	public void connectDb ()
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	
			connection = DriverManager.getConnection(dbUrl, user, pwd);
			System.out.println("JDBC connection successfull");
			
		} catch (Exception e) {
			throw new RuntimeException ("An error occured while loading JDBC Driver: ", e);
		}
	}
	
	int executeUpdate(String sql) throws SQLException {
		connection = DriverManager.getConnection(dbUrl, user, pwd);
		PreparedStatement ps = connection.prepareStatement(sql);
		
		int rows = ps.executeUpdate();
		return rows;
	}
	
	public void retriveResultSet(String sql) throws SQLException {
		System.out.println();
		
		connection = DriverManager.getConnection(dbUrl, user, pwd);
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
	
		int colCount = rsmd.getColumnCount();
		System.out.println("no. of columns: "+colCount);
		
		for (int i=1; i<=colCount; i++) {
			System.out.print(rsmd.getColumnName(i)+"\t"+"|");
		}
		System.out.println();
		
		while (rs.next()) {
			for (int i=1; i<=colCount; i++) {
				System.out.print(rs.getString(i) + "\t"+"|");
			}
			System.out.println();
		}
	}

	public void joinAvgSalesTimeframe1() throws SQLException {
		String sql="select avg(o.quantity*trim(leading '$' from p.price)) as avgtotalSales, month(o.updatedAt) as saleMonth, year(o.updatedAt) as saleYear from orders o, products p where o.productId=p.id group by saleMonth, saleYear order by avgtotalSales desc, saleYear desc, saleMonth desc;"
				+ "";
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Join, Group by, Order by");
		
		retriveResultSet(sql);
	}

	public void groupByRegion2() throws SQLException {
		String sql="select sum(o.quantity*trim(leading '$' from p.price)) as totalOfAllOrders, u.region from orders o, products p, users u where o.userId = u.id group by region order by totalOfAllOrders desc;";
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Join, Group by, Order by");
		
		retriveResultSet(sql);
		
	}

	public void multiwayJoinMostSales3() throws SQLException {
		String sql1="SELECT userId, users.first_name, users.last_name, users.email, users.address, users.region, users.phone_number, SUM(orders.quantity) AS total_orders, SUM(REPLACE(products.price,'$','') * orders.quantity) AS total_sales FROM orders INNER JOIN users ON orders.userId = users.id INNER JOIN products ON orders.productId = products.id GROUP BY orders.userId ORDER BY total_sales desc;\r\n"
				+ "";
		System.out.println("Constructs used: Multiway Join, Group by, Order by");
		System.out.println();
		
		System.out.println("Most sales: "+sql1);
		retriveResultSet(sql1);
		System.out.println();
		
		String sql2="SELECT userId, users.first_name, users.last_name, users.email, users.address, users.region, users.phone_number, SUM(orders.quantity) AS total_orders, SUM(REPLACE(products.price,'$','') * orders.quantity) AS total_sales FROM orders INNER JOIN users ON orders.userId = users.id INNER JOIN products ON orders.productId = products.id GROUP BY orders.userId ORDER BY total_sales asc;\r\n"
				+ "";
        System.out.println();
		System.out.println("Least sales: "+sql2);
		retriveResultSet(sql2);
		System.out.println();
	}

	public void subQueryMwjSamePurchase4() throws SQLException {
		String sql = "SELECT userId, users.first_name, users.last_name, users.region, orders.productId, products.title, orders.quantity, products.price FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id HAVING orders.productId IN\r\n"
				+ "(SELECT productId FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id GROUP BY (productId) HAVING count(orders.productId) > 1) ORDER BY orders.productId asc;\r\n"
				+ "";	
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join, Sub query, aggregate functions");
		
		retriveResultSet(sql);
		
	}

	public void subQueryMwjBoughtTogether5() throws SQLException {
		String sql="SELECT userId, orders.productId, orders.quantity, products.title, products.price FROM  users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id HAVING orders.userId IN\r\n"
				+ "(SELECT orders.userId FROM  users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id GROUP BY (orders.userId) HAVING count(orders.userId) > 1) ORDER BY orders.userId asc;\r\n"
				+ "";
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join, Sub query, aggregate functions");
		
		retriveResultSet(sql);
		
	}

	public void leftJoinNotBought6() throws SQLException {
		String sql="SELECT t1.* FROM products AS t1 LEFT JOIN orders AS t2 ON t1.id=t2.productId WHERE t2.productId IS NULL;";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Left join, IS NULL operator");
		
		retriveResultSet(sql);
		
	}

	public void subQueryJoinSaleRange7() throws SQLException {
		String sql="SELECT *, SUM(REPLACE(products.price,'$','') * orders.quantity) AS total_sales FROM orders INNER JOIN products ON orders.productId = products.id GROUP BY orders.userId HAVING SUM(REPLACE(products.price,'$','') * orders.quantity) BETWEEN 500 AND 1000;\r\n"
				+ "";
		
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Inner join, Sub query, BETWEEN operator");
		
		retriveResultSet(sql);
		
	}

	public void lastSixMonths9() throws SQLException {
		String sql="select * from orders where createdAt > DATE_SUB(now(), INTERVAL 6 MONTH);";
		
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Date operation function");
		
		retriveResultSet(sql);
		
	}

	public void groupProdsRegion12() throws SQLException {
		System.out.println("Constructs used: Join, Group by, Order By");
		
		String sql1="SELECT products.id AS products_id, products.title, users.region, products.price, products.count, products.published FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id ORDER BY region asc;\r\n"
				+ "";
		
		System.out.println(sql1);
		System.out.println();
		retriveResultSet(sql1);
		
		String sql2="SELECT products.id AS products_id, count(products.id) AS product_count, products.title, users.region, products.price, products.count, products.published FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id GROUP BY region ORDER BY product_count desc;\r\n";
		
		System.out.println(sql2);
		System.out.println();
		retriveResultSet(sql2);
		
		String sql3="select orders.productId, users.region, count(productId) AS product_count from users join orders on orders.userId=users.id group by users.region;\r\n";
		
		System.out.println(sql3);
		System.out.println();
		retriveResultSet(sql3);
		
	}

	public void foreignKey13() throws SQLException {
		String sql="SELECT users.id, users.first_name, users.last_name, users.address, products.title, SUM(orders.quantity) AS order_total, SUM(REPLACE(products.price,'$','') * orders.quantity) AS amount_total FROM orders INNER JOIN products ON orders.productId = products.id INNER JOIN users on orders.userId = users.id GROUP BY orders.userId ORDER BY orders.userId;\r\n"
				+ "";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Foreign Key Contraints");
		
		retriveResultSet(sql);
		
	}

	public void violateContraint15() throws SQLException {
		String sql="insert into orders (id, userId, productId, quantity, orderActive, orderCancelled, createdAt, updatedAt) values (1001, '2001', '3001', 95, 0, 1, '2020-11-18', '2021-07-26');\r\n"
		+ "";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Check consistency of DB");
		
		retriveResultSet(sql);
		
	}

	public void joinGroupByRepeatedBuy18() throws SQLException {
		String sql="SELECT orders.productId, products.title, count(orders.productId) AS product_count, products.price, products.count FROM orders JOIN products ON orders.productId = products.id GROUP BY orders.productId HAVING count(orders.productId) > 1 ORDER BY orders.productId;\r\n"
				+ "";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join, Group by, order by, aggregate functions");
		
		retriveResultSet(sql);
		
	}
	
	public void multiwayJoinLocality19() throws SQLException {
		String sql="SELECT products.id, count(products.id) AS product_count, products.title, users.region, products.price, products.count, products.published FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id GROUP BY products.id, region HAVING count(products.id) > 1 ORDER BY region asc;\r\n"
		+ "";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join");
		
		retriveResultSet(sql);
		
	}

	public void selectActiveView23() throws SQLException {
		String sql="select * from active_orders;";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Select from a view");
		
		retriveResultSet(sql);
		
	}

	public void multiwayJoin25() throws SQLException {
		String sql="SELECT users.id AS user_id, users.first_name, users.last_name, users.region, products.id AS product_id, products.title, products.price, products.count FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id ORDER BY orders.userId;\r\n"
				+ "";
		
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join, order by");
		
		retriveResultSet(sql);
	}

	public void groupByOrderDateMwj26() throws SQLException {
		String sql="SELECT users.id AS user_id, users.first_name, users.last_name, users.region, products.id AS product_id, products.title, products.price, products.count, orders.createdAt, orders.updatedAt FROM users INNER JOIN orders ON users.id = orders.userId INNER JOIN products ON orders.productId = products.id ORDER BY YEAR(orders.createdAt) desc, MONTH(orders.createdAt) desc;\r\n"
				+ "";
		
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Multiway Join, order by date functions");
		
		retriveResultSet(sql);
		
	}

	public void groupActiveViewByRegion27() throws SQLException {
		String sql="select region, count(*) as numberOfActiveOrders from active_orders group by region order by region;\r\n"
				+ "";
		
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: group by on a view");
		
		retriveResultSet(sql);
		
	}

	public void selfJoinSameDateOrders28() throws SQLException {
		String sql = "select o1.id as OrderId, o1.userId as Customer, o1.productId as Product, o1.quantity as quantity, o1.orderActive as Delivered, o1.orderCancelled as Cancelled, o1.createdAt as OrderDate, \r\n"
				+ "o2.id as OrderId, o2.userId as Customer, o2.productId as Product, o2.quantity as quantity, o2.orderActive as Delivered, o1.orderCancelled as Cancelled, o2.createdAt as OrderDate\r\n"
				+ "from orders o1, orders o2 where o1.createdAt = o2.createdAt and o1.userId <> o2.userId group by o1.createdAt order by o1.createdAt;\r\n"
				+ "";
		System.out.println(sql);
		System.out.println();
		
		System.out.println("Constructs used: Self Join");
		
		retriveResultSet(sql);
		
	}

	public void triggerBeforeDeleteProduct16() throws SQLException {
		System.out.println("Constructs used: Trigger before delete");
		System.out.println();
		
		String t = "CREATE TRIGGER user_bd5 BEFORE DELETE ON users insert into person_archive(id, first_name, last_name, phone_number, address, region, email, password, createdAt, updatedAt) values (old.id, old.first_name, old.last_name, old.phone_number, old.address, old.region, old.email, old.password, old.createdAt, old.updatedAt);";
		System.out.println(t);
		System.out.println();
		
		int rows = executeUpdate(t);
		if(rows == 0) {
			System.out.println("created successfully");
		}
		
		System.out.println("testing trigger...");
		System.out.println();
		
		String sql2="delete from users where id = 2;";
		int r = executeUpdate(sql2);
		String sql3 = "SELECT * FROM person_archive;"
				+ "";
		
		retriveResultSet(sql3);
	}

	public void triggerBeforeInsertOrder17() throws SQLException {
		System.out.println("Constructs used: Trigger before insert");
		System.out.println();
		
		String t = "create trigger before_insert_order before insert on orders for each row begin DECLARE c INT; SET c = (select count from products where id=new.productId); if(new.quantity > c) then SIGNAL SQLSTATE '50001' SET MESSAGE_TEXT = 'not enough units of this product in stock. Pls reduce quantity and try again.'; end if; end;";
		
		System.out.println(t);
		System.out.println();
		
		int rows = executeUpdate(t);
		if(rows == 0) {
			System.out.println("created successfully");
		}
		
		System.out.println("testing trigger...");
		System.out.println();
		
		String sql="insert into orders (id, userId, productId, quantity, orderActive, orderCancelled, createdAt, updatedAt) values (1005, '170', '1', 95, 0, 1, '2020-11-18', '2021-07-26');\r\n"
				+ "";
		int rows1 = executeUpdate(sql);
		
	}

	public void createViewActive21() throws SQLException {
		System.out.println("Constructs used: Creating view...");
		System.out.println();
		
		String active="create view active_orders as select o.id, o.userId, u.first_name, u.last_name, u.region, o.orderActive from orders o, users u where orderActive=1 and o.userId=u.id;\r\n"
				+ "";
		String inventory = "create view inventory as select count(id) as x, YEAR(createdAt) as year, MONTH(createdAt) as month from products group by YEAR(createdAt), MONTH(createdAt);\r\n"
				+ "";
		
		System.out.println(active);
		System.out.println();
		
		int rows = executeUpdate(active);
		if(rows == 0) {
			System.out.println("created successfully");
		}
		
		String sql="select * from active_orders;";
		retriveResultSet(sql);
		
		System.out.println(active);
		System.out.println();
		int rows1 = executeUpdate(inventory);
		if(rows1 == 0) {
			System.out.println("created successfully");
		}
		String sql1="select * from inventory;";
		retriveResultSet(inventory);
		
	}

	public void triggerAfterInsertOrder22() throws SQLException {
		System.out.println("Constructs used: Trigger after insert");
		System.out.println();
		
		String t = "create trigger after_insert_order after insert on orders for each row update products set count = count-new.quantity where id = new.productId;\r\n"
				+ "";
		
		System.out.println(t);
		System.out.println();
		
		int rows = executeUpdate(t);
		if(rows == 0) {
			System.out.println("created successfully");
		}
		
		System.out.println("testing trigger...");
		System.out.println();
		
		String sql1="select * from products where id = 8;";
		retriveResultSet(sql1);
		String sql2="insert into orders (id, userId, productId, quantity, orderActive, orderCancelled, createdAt, updatedAt) values (1023, '170', '8', 1, 0, 1, '2020-11-18', '2021-07-26');\r\n"
				+ "";
		int r1 = executeUpdate(sql2);
		retriveResultSet(sql1);
	}

	public void triggerAfterUpdateOrders24() throws SQLException {
		System.out.println("Constructs used: Trigger after update");
		System.out.println();
		
		String t="create trigger afer_update_orders\r\n"
				+ "after update on orders \r\n"
				+ "for each row \r\n"
				+ "begin\r\n"
				+ "if (old.orderActive ='1' and new.orderActive = '0') then\r\n"
				+ "        delete from active_orders where id=old.id;\r\n"
				+ "        end if;\r\n"
				+ "end;";
		
		System.out.println(t);
		System.out.println();
		
		int rows = executeUpdate(t);
		if(rows == 0) {
			System.out.println("created successfully");
		}
		
		System.out.println("testing trigger...");
		System.out.println();
		
		String sql="select * from active_orders where id=6;";
		retriveResultSet(sql);
		String sql2="update orders set orderActive=0 where id=6;";
		int r = executeUpdate(sql2);
		retriveResultSet(sql);
	}

	public void alterTableQuery8() throws SQLException {
		System.out.println("Constructs used: Alter table query, adding foreign key constraint.");
		System.out.println();
		
		String at1 = "ALTER TABLE orders ADD CONSTRAINT productIdFK FOREIGN KEY (productId) REFERENCES products(id) on delete cascade;";
		String at2="ALTER TABLE orders ADD CONSTRAINT userIdFK FOREIGN KEY (userId) REFERENCES users(id) on delete cascade;";
		String len="select count(*) from orders";
		
		connection = DriverManager.getConnection(dbUrl, user, pwd);
		PreparedStatement ps = connection.prepareStatement(len);
		
		ResultSet rs = ps.executeQuery(len);
		rs.next();
		int tableCardinality = rs.getInt(1);
		
		int r1 = executeUpdate(at1);
		if(r1 == tableCardinality) {
			System.out.println("table altered - foreign key constraint added successfully");
		}
		int r2 = executeUpdate(at2);
		if(r2 == tableCardinality) {
			System.out.println("table altered - foreign key constraint added successfully");
		}
				
	}

	public void UD10() throws SQLException {
		
		System.out.println("Constructs used: update and delete on tables and views. ");
		
		
		String u = "update products set price=\"$4.00\" where id=2;";
		String d = "delete from orders where id=1;";
		String uv = "update active_orders set first_name = 'Felix' where id  = 290 ";
		
		int ru = executeUpdate(u);
		int rd = executeUpdate(d);
		int ruv = executeUpdate(uv);;
		
		if(ru == 1) {
			System.out.println("table updated successfully");
		}
		if(rd == 1) {
			System.out.println("record deleted successfully");
		}
		if(ruv == 1) {
			System.out.println("view updated successfully");
		}
		
	}

	public void commitRollbackSavepoint11() throws SQLException {
		/*
		 * String sql = "select price from products where id = 1;";
		 * 
		 * String disableAutoCommit = "SET autocommit = 0;";
		 * 
		 * connection = DriverManager.getConnection(dbUrl, user, pwd); PreparedStatement
		 * ps = connection.prepareStatement(sql);
		 * 
		 * ps.execute(disableAutoCommit);
		 * 
		 * String c = "update products set price=\"$10.50\" where id=1;";
		 * 
		 * connection = DriverManager.getConnection(dbUrl, user, pwd);
		 * 
		 * 
		 * //connection.setAutoCommit(false); executeUpdate(c); connection.commit();
		 * System.out.println("Commit performed successfully."); retriveResultSet(sql);
		 * 
		 * String r = "update products set price=\"$3.50\" where id=1;";
		 * executeUpdate(r); connection.rollback();
		 * System.out.println("rollback performed successfully.");
		 * retriveResultSet(sql);
		 */
		
		 //connection.close();
			/*
			 * String disableAutoCommit = "SET autocommit = 0;"; String uc =
			 * "UPDATE users SET region = \"California\" WHERE id = 3;"; String c =
			 * "commit;";
			 * 
			 * executeUpdate(disableAutoCommit); executeUpdate(uc); String br =
			 * "SELECT region FROM users where id = 3"; retriveResultSet(br);
			 * 
			 * if (executeUpdate(c) == 0) {
			 * System.out.println("Commit performed successfully."); }
			 * 
			 * retriveResultSet(br);
			 */
			/*
			 * retriveResultSet(br);
			 * 
			 * String u = "UPDATE users SET region = \"Michigan\" WHERE id = 3;"; String r =
			 * "rollback;";
			 * 
			 * System.out.println("Before rollback: "); executeUpdate(u);
			 * retriveResultSet(br); if (executeUpdate(r) == 0) {
			 * System.out.println("Rollback performed successfully.");
			 * 
			 * } System.out.println("After rollback: "); String ar =
			 * "SELECT region FROM users where id = 3"; retriveResultSet(ar);
			 */
		
			  //String s1 = "SAVEPOINT s1;"; 
			  String sql1 ="SELECT * FROM orders where id = 1;";
			  String d = "DELETE FROM orders WHERE id = 1;";
			  String r_s1 = "ROLLBACK TO s1;";
			  String rls_s1 = "RELEASE SAVEPOINT s1;";
			  
			  connection = DriverManager.getConnection(dbUrl, user, pwd);
			  
			  //executeUpdate(s1);
			  Savepoint s1  = connection.setSavepoint();
			  retriveResultSet(sql1);
			  executeUpdate(d);
			  retriveResultSet(sql1);
			  //executeUpdate(r_s1);
			  connection.rollback(s1);
			  connection.releaseSavepoint(s1);
			  retriveResultSet(sql1);  
	}

	public void cursor14() {
		String sql = "";
		
	}

	public void beforeDeleteUser29() {
		
	}

	public void checkInventory20() throws SQLException {
		String sql = "select b.x-a.x from inventory b inner join inventory a on b.year=a.year and b.month=a.month-1;\r\n"
				+ "";
		retriveResultSet(sql);
		
	}

}
