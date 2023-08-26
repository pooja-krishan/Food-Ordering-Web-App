package com.jdbc.run;

import java.sql.SQLException;
import java.util.Scanner;

import com.jdbc.service.ServiceClassForJdbc;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
		
		ServiceClassForJdbc service = new ServiceClassForJdbc();
		//service.connectService();
		
		Scanner sc = new Scanner (System.in);
		int loop = 8;
		
		System.out.println("MENU -- SELECT");
		System.out.println("1. Find average sales made on a certain day or timeframe. ");
		System.out.println("2. Group orders region wise");
		System.out.println("3. Find customers who generate most/least sales for the store");
		System.out.println("4. Find users who bought the same product");
		System.out.println("5. Find products usually bought together");
		System.out.println("6. Find products not being bought by customers");
		System.out.println("7. Find orders whose total amount is between 500 and 1000");
		System.out.println("9. Find orders made in the last 6 months");
		System.out.println("12. Group products according to region");
		System.out.println("13. Find order total and total amount from userId foreign key, productId foreign key");
		System.out.println("15. Perform a transaction violating constraints to check database consistency");
		System.out.println("18. Find repeated purchases");
		System.out.println("19. Find products frequently being purchased by people from a locality");
		System.out.println("20. Check inventory at the end of each month");
		System.out.println("23. Show all active orders.");
	    System.out.println("25. Find who purchased what items");
		System.out.println("26. Group orders by date purchased");
		System.out.println("27. Group active orders by region");
		System.out.println("28. Find orders made on the same date");
		System.out.println();
		
		System.out.println("MENU -- TRIGGERS and VIEWS and CURSORS");
		System.out.println("17. Trigger error if quantity in ordercarts is greater than count in products.");
		System.out.println("21. Create views: Inventory and Active Orders");
		System.out.println("22. Trigger: update count in products after insertion in orders.");
		System.out.println("24. Trigger: delete from Active view when an order is delivered. ");
		System.out.println("29. Trigger: backup user information before delete ");
		System.out.println();
		
		System.out.println("MENU -- IUD");
		System.out.println("8. Alter table query");
		System.out.println("10. Update and delete query");
		System.out.println();
		
		while (loop == 8) {
			System.out.println("Enter your choice: ");
			int ch = sc.nextInt();
			
			switch(ch) {
			case 1: service.joinAvgSalesTimeframeService1();
			        break;
			case 2: service.groupByRegionService2();
	        	break;
			case 3: service.multiwayJoinMostSalesService3();
	        	break;
			case 4: service.subQueryMwjSamePurchaseService4();
	        	break;
			case 5: service.subQueryMwjBoughtTogetherService5();
	        	break;
			case 6: service.leftJoinNotBoughtService6();
	        	break;
			case 7: service.subQueryJoinSaleRangeService7();
				break;
			case 8: service.alterTableQueryService8();
				break;
			case 9: service.lastSixMonthsService9();
	        	break;
			case 10: service.UDService10();
				break;
			case 11: service.commitRollbackSavepointService11();
				break;
			case 12: service.groupProdsRegionService12();
	        	break;
			case 13: service.foreignKeyService13();
	        	break;
			case 15: service.violateContraintService15();
	    		break;
			case 18: service.joinGroupByRepeatedBuyService18();
	    	break;
			case 19: service.multiwayJoinLocalityService19();
	    	break;
			case 20: service.checkInventoryService20();
			break;
			case 23: service.selectActiveViewService23();
	    	break;
			case 25: service.multiwayJoinService25();
	    	break;
			case 26: service.groupByOrderDateMwjService26();
	    	break;
			case 27: service.groupActiveViewByRegionService27();
	    	break;
			case 28: service.selfJoinSameDateOrdersService28();
	    	break;
	    	
			case 14: service.cursorService14();
			break;
			case 16: service.beforeDeleteProductService16();
	    	break;
			case 17: service.beforeInsertOrderService17();
	    	break;
			case 21: service.createViewActiveService21();
	    	break;
			case 22: service.afterInsertOrderService22();
	    	break;
			case 24: service.afterUpdateOrdersService24();
	    	break;
			case 29: service.beforeDeleteUserService29();
			
			default: System.out.println("incorrect option, pls try again");
					 break;
			}
			
			System.out.println("Enter 8 to continue");
			loop = sc.nextInt();
		}
		
	}
}

		

