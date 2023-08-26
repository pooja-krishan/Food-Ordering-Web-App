package com.jdbc.service;

import java.sql.SQLException;
import java.util.Scanner;

import com.jdbc.dao.Dao;


public class ServiceClassForJdbc {
	
	Scanner sc = new Scanner (System.in);
	Dao dao = new Dao();
	
	public void connectService() {
		dao.connectDb();
	}
	
	public void joinAvgSalesTimeframeService1() throws SQLException {
		dao.joinAvgSalesTimeframe1();
	}


	public void groupByRegionService2() throws SQLException {
		dao.groupByRegion2();
		
	}


	public void multiwayJoinMostSalesService3() throws SQLException {
		dao.multiwayJoinMostSales3();
		
	}


	public void subQueryMwjSamePurchaseService4() throws SQLException {
		dao.subQueryMwjSamePurchase4();
		
	}


	public void subQueryMwjBoughtTogetherService5() throws SQLException {
		dao.subQueryMwjBoughtTogether5();
		
	}


	public void leftJoinNotBoughtService6() throws SQLException {
		dao.leftJoinNotBought6();
		
	}


	public void subQueryJoinSaleRangeService7() throws SQLException {
		dao.subQueryJoinSaleRange7();
		
	}


	public void lastSixMonthsService9() throws SQLException {
		dao.lastSixMonths9();
		
	}


	public void groupProdsRegionService12() throws SQLException {
		dao.groupProdsRegion12();
		
	}


	public void foreignKeyService13() throws SQLException {
		dao.foreignKey13();
		
	}


	public void violateContraintService15() throws SQLException {
		dao.violateContraint15();
		
	}


	public void joinGroupByRepeatedBuyService18() throws SQLException {
		dao.joinGroupByRepeatedBuy18();
		
	}


	public void multiwayJoinLocalityService19() throws SQLException {
		dao.multiwayJoinLocality19();
		
	}


	public void selectActiveViewService23() throws SQLException {
		dao.selectActiveView23();
		
	}


	public void multiwayJoinService25() throws SQLException {
		dao.multiwayJoin25();
		
	}


	public void groupByOrderDateMwjService26() throws SQLException {
		dao.groupByOrderDateMwj26();
		
	}


	public void groupActiveViewByRegionService27() throws SQLException {
		dao.groupActiveViewByRegion27();
		
	}


	public void selfJoinSameDateOrdersService28() throws SQLException {
		dao.selfJoinSameDateOrders28();
		
	}


	public void beforeDeleteProductService16() throws SQLException {
		dao.triggerBeforeDeleteProduct16();
		
	}


	public void beforeInsertOrderService17() throws SQLException {
		dao.triggerBeforeInsertOrder17();
		
	}


	public void createViewActiveService21() throws SQLException {
		dao.createViewActive21();
		
	}


	public void afterInsertOrderService22() throws SQLException {
		dao.triggerAfterInsertOrder22();
		
	}


	public void afterUpdateOrdersService24() throws SQLException {
		dao.triggerAfterUpdateOrders24();
		
	}

	public void alterTableQueryService8() throws SQLException {
		dao.alterTableQuery8();
		
	}

	public void UDService10() throws SQLException {
		dao.UD10();
		
	}

	public void commitRollbackSavepointService11() throws SQLException {
		dao.commitRollbackSavepoint11();
		
	}

	public void cursorService14() {
		dao.cursor14();
		
	}

	public void beforeDeleteUserService29() {
		dao.beforeDeleteUser29();
		
	}

	public void checkInventoryService20() throws SQLException {
		dao.checkInventory20();
		
	}
}
