# Shopping Cart Database Queries and Operations

## Introduction

This project involves working with a shopping cart database to perform various queries and operations. The database schema consists of three tables: `Users`, `Products`, and `Orders`. The project aims to showcase SQL queries ranging from simple to complex, including joins, group by, triggers, transactions, and more.

## Schema Definitions

The schema includes three main tables: `Users`, `Products`, and `Orders`. Additional tables, `persons_archive` and `backup`, are also part of the schema.

### Users Table

This table stores details of users of the Shopping Cart application.

| Column        | Data Type | Description                    |
|---------------|-----------|--------------------------------|
| id            | INT       | Primary Key                    |
| first_name    | VARCHAR   | First Name of the user         |
| last_name     | VARCHAR   | Last Name of the user          |
| phone_number  | VARCHAR   | Phone Number of the user       |
| address       | VARCHAR   | Address of the user            |
| region        | VARCHAR   | Region of the user             |
| email         | VARCHAR   | Email of the user              |
| password      | VARCHAR   | Password of the user           |
| createdAt     | DATE      | Date of creation               |
| updatedAt     | DATE      | Date of last update            |

### Products Table

This table stores details of grocery items.

| Column        | Data Type | Description                    |
|---------------|-----------|--------------------------------|
| id            | INT       | Primary Key                    |
| title         | VARCHAR   | Title of the product           |
| price         | VARCHAR   | Price of the product           |
| count         | INT       | Quantity available             |
| published     | TINYINT   | Published status               |
| createdAt     | DATE      | Date of creation               |
| updatedAt     | DATE      | Date of last update            |

### Orders Table

This table stores details of orders made by customers.

| Column        | Data Type | Description                    |
|---------------|-----------|--------------------------------|
| id            | INT       | Primary Key                    |
| userId        | INT       | Foreign Key referencing Users  |
| productId     | INT       | Foreign Key referencing Products|
| quantity      | INT       | Quantity of product ordered    |
| orderActive   | TINYINT   | Order Active status            |
| orderCancelled| TINYINT   | Order Cancelled status         |
| createdAt     | DATE      | Date of creation               |
| updatedAt     | DATE      | Date of last update            |

## Queries and Operations

The provided text demonstrates various SQL queries and operations performed on the shopping cart database. These include:

- Simple and complex queries involving JOIN, GROUP BY, self-join, nested queries, and more.
- Insert/Update/Delete (IUD) operations on the database tables.
- Creating views and performing insert/update on views using triggers.
- Transactions with examples of committing and rolling back operations.
- Demonstrating constraint violations and maintaining database consistency.
- Utilizing stored procedures and triggers for data manipulation.

Please note that the output of each query or operation is included, showcasing the results achieved.

1. **Find average sales made on a certain day or timeframe.**
   ```sql
   -- Query:
   SELECT
       AVG(o.quantity * TRIM(LEADING '$' FROM p.price)) AS avgtotalSales,
       MONTH(o.updatedAt) AS saleMonth,
       YEAR(o.updatedAt) AS saleYear
   FROM orders o
   JOIN products p ON o.productId = p.id
   GROUP BY saleMonth, saleYear
   ORDER BY avgtotalSales DESC, saleYear DESC, saleMonth DESC;
Output: 
![1](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/1.PNG)

2. **Group orders by region.**
    ```sql
    -- Query:
    SELECT
        SUM(o.quantity * TRIM(LEADING '$' FROM p.price)) AS totalOfAllOrders,
        u.region
    FROM orders o
    JOIN products p ON o.productId = p.id
    JOIN users u ON o.userId = u.id
    GROUP BY region
    ORDER BY totalOfAllOrders DESC;
Output: 
![2](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/2.PNG)

3. **Find customers who generate most sales for the store.**
    ```sql
    -- Query
    SELECT
        userId,
        users.first_name,
        users.last_name,
        users.email,
        users.address,
        users.region,
        users.phone_number,
        SUM(orders.quantity) AS total_orders,
        SUM(REPLACE(products.price, '$', '') * orders.quantity) AS total_sales
    FROM orders
    JOIN users ON orders.userId = users.id
    JOIN products ON orders.productId = products.id
    GROUP BY userId
    ORDER BY total_sales DESC;
Output 
![3](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/3.PNG)

4. **Find users who bought the same product.**
     ```sql
    -- Query
    SELECT
        userId,
        users.first_name,
        users.last_name,
        users.region,
        orders.productId,
        products.title,
        orders.quantity,
        products.price
    FROM users
    INNER JOIN orders ON users.id = orders.userId
    INNER JOIN products ON orders.productId = products.id
    HAVING orders.productId IN (
        SELECT productId
        FROM users
        INNER JOIN orders ON users.id = orders.userId
        INNER JOIN products ON orders.productId = products.id
        GROUP BY productId
        HAVING COUNT(orders.productId) > 1
    )
    ORDER BY orders.productId ASC;
 Output 
![4](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/4.PNG)

5. **Find products usually bought by the same user.**
    ```sql
    -- Query
    SELECT
        userId,
        orders.productId,
        orders.quantity,
        products.title,
        products.price
    FROM
        users
    INNER JOIN
        orders ON users.id = orders.userId
    INNER JOIN
        products ON orders.productId = products.id
    HAVING
        orders.userId IN (
            SELECT
                orders.userId
            FROM
                users
            INNER JOIN
                orders ON users.id = orders.userId
            INNER JOIN
                products ON orders.productId = products.id
            GROUP BY
                orders.userId
            HAVING
                COUNT(orders.userId) > 1
        )
    ORDER BY
        orders.userId ASC;

6. **Find products not being bought by customers.**
    ```sql
        -- Query
        SELECT t1.*
        FROM products AS t1
        LEFT JOIN orders AS t2 ON t1.id = t2.productId
        WHERE t2.productId IS NULL;
Output 
![6](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/6.PNG)

7. **Find orders whose total amount is in a certain range.**
    ```sql
    -- Query
    SELECT *,
    SUM(REPLACE(products.price, '$', '') * orders.quantity) AS total_sales
    FROM orders
    INNER JOIN products ON orders.productId = products.id
    GROUP BY orders.userId
    HAVING total_sales BETWEEN 500 AND 1000;
Output 
![7](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/7.PNG)

8. **Alter table query.**
    ```sql
    -- Query
    ALTER TABLE orders
    ADD CONSTRAINT productIdFK FOREIGN KEY (productId)
    REFERENCES products(id)
    ON DELETE CASCADE;

    ALTER TABLE orders
    ADD CONSTRAINT userIdFK FOREIGN KEY (userId)
    REFERENCES users(id)
    ON DELETE CASCADE;
Output 
![8](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/8.PNG)

9. **Find orders made in the last six months.**
    ```sql
    -- Query
    SELECT * FROM orders
    WHERE createdAt > DATE_SUB(NOW(), INTERVAL 6 MONTH);
Output 
![9](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/9.PNG)

10. **Update and delete examples.**
    ```sql
    -- Query
    UPDATE products
    SET price = "$4.00"
    WHERE id = 2;

    DELETE FROM orders
    WHERE id = 1;
Output 
![10](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/10.PNG)

11. **Demonstrate Commit/Rollback after CRUD operations.**
    ```sql
    -- Query
    SET autocommit = 0;
    commit;
    UPDATE users SET region
    = "Michigan" WHERE id =
    3;
    SELECT * FROM users;
    rollback;
    SELECT * FROM users;
    SET autocommit = 0;
    SAVEPOINT s1;
    SELECT * FROM orders;
    DELETE FROM orders
    WHERE id = 1;
    SELECT * FROM orders;
    ROLLBACK TO s1;
    SELECT * FROM orders;
    RELEASE SAVEPOINT s1;
Output 
![11](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/11.PNG)

12. **Order/Group products according to region.**
    ```sql
    -- Query
    SELECT products.id AS products_id,
        products.title,
        users.region, products.price,
        products.count,
        products.published
    FROM users
    INNER JOIN orders ON users.id = orders.userId
    INNER JOIN products ON orders.productId = products.id
    ORDER BY region ASC;
Output 
![12](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/12.PNG)

13. **Find order total and total amount for users.**
    ```sql
    -- Query
    SELECT users.id,
        users.first_name,
        users.last_name,
        users.address, products.title,
        SUM(orders.quantity) AS order_total,
        SUM(REPLACE(products.price,'$','') * orders.quantity) AS amount_total
    FROM orders
    INNER JOIN products ON orders.productId = products.id
    INNER JOIN users ON orders.userId = users.id
    GROUP BY orders.userId
    ORDER BY orders.userId;
Output 
![13](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/13.PNG)

14. **Create a cursor to take a backup of the product table.**
    ```sql
    -- Query
    DELIMITER //
    CREATE PROCEDURE backupProduct()
    BEGIN
        DECLARE done INT DEFAULT 0;
        DECLARE productID, productCount INTEGER;
        DECLARE productPublished TINYINT;
        DECLARE productTitle, productPrice VARCHAR(100);
        DECLARE createdAt, updatedAt DATE;
        DECLARE cur CURSOR FOR SELECT * FROM products;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
        
        OPEN cur;
        label: LOOP
            FETCH cur INTO productID, productTitle, productPrice, productCount, productPublished, createdAt, updatedAt;
            INSERT INTO backup VALUES (productID, productTitle, productPrice, productCount, productPublished, createdAt, updatedAt);
            IF done = 1 THEN
                LEAVE label;
            END IF;
        END LOOP;
        CLOSE cur;
    END//
    DELIMITER ;

    -- Calling the Stored Procedure
    CALL backupProduct();

    -- Selecting Records from the Backup Table
    SELECT * FROM backup;
Output 
![14](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/14.PNG)

15. **Perform a transaction violating check constraints to check database consistency.**
    ```sql
    -- Query
    UPDATE products
    SET count = -1
    WHERE id = 2;
Output 
![15](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/15.PNG)

16. **Insert old users into the archive by using trigger before delete.**
    ```sql
    -- Query
    delimiter //
    CREATE TRIGGER user_bd BEFORE DELETE ON users
    FOR EACH ROW
    INSERT INTO person_archive (
        id, first_name, last_name,
        phone_number, address, region,
        email, password, createdAt,
        updatedAt
    )
    VALUES (
        old.id, old.first_name,
        old.last_name, old.phone_number,
        old.address, old.region,
        old.email, old.password,
        old.createdAt, old.updatedAt
    );
    delimiter ;
Output 
![16](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/16.PNG)

17. **Trigger error if quantity in ordercarts is greater than count in products. (trigger before insert).**
    ```sql
    -- Query
    delimiter //
    CREATE TRIGGER before_insert_order
    BEFORE INSERT ON orders
    FOR EACH ROW
    BEGIN
        DECLARE c INT;
        SET c = (SELECT count FROM products WHERE id = new.productId);
        IF new.quantity > c THEN
            SIGNAL SQLSTATE '50001' SET MESSAGE_TEXT = 'Not enough units of this product in stock. Please reduce the quantity and try again.';
        END IF;
    END;
    //
    delimiter ;
Output 
![17](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/17.PNG)

18. **Find repeated purchases.**
    ```sql
    -- Query
    SELECT orders.productId,
       products.title,
       COUNT(orders.productId) AS product_count,
       products.price,
       products.count
    FROM orders
    JOIN products ON orders.productId = products.id
    GROUP BY orders.productId
    HAVING COUNT(orders.productId) > 1
    ORDER BY orders.productId;
Output 
![18](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/18.PNG)

19. **Find products frequently being purchased from a locality.**
    ```sql
    -- Query
    SELECT products.id,
       COUNT(products.id) AS product_count,
       products.title,
       users.region,
       products.price,
       products.count,
       products.published
    FROM users
    INNER JOIN orders ON users.id = orders.userId
    INNER JOIN products ON orders.productId = products.id
    GROUP BY products.id, region
    HAVING COUNT(products.id) > 1
    ORDER BY region ASC;
Output 
![19](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/19.PNG)

20. **Check inventory at the end of each month.**
    ```sql
    -- Query
    SELECT b.x - a.x
    FROM inventory b
    INNER JOIN inventory a ON b.year = a.year AND b.month = a.month - 1;
Output 
![20](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/20.PNG)

21. **Create a view for inventory.**
    ```sql
    -- Query
    CREATE VIEW inventory AS
    SELECT COUNT(id) AS x,
        YEAR(createdAt) AS year,
        MONTH(createdAt) AS month
    FROM products
    GROUP BY YEAR(createdAt), MONTH(createdAt);
    SELECT * FROM inventory;
Output 
![21](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/21.PNG)

22. **Trigger: update count in products after insertion in orders.**
    ```sql
    -- Query
    CREATE TRIGGER after_insert_order
    AFTER INSERT ON orders
    FOR EACH ROW
    UPDATE products
    SET count = count - new.quantity
    WHERE id = new.productId;
Output 
![22](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/22.PNG)

23. **Create results of joining users and orders to find active orders as a view.**
    ```sql
    -- Query
    CREATE VIEW active_orders AS
    SELECT o.id, o.userId,
        u.first_name, u.last_name,
        u.region, o.orderActive
    FROM orders o, users u
    WHERE orderActive = 1 AND o.userId = u.id;
    SELECT * FROM active_orders;
Output 
![23](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/23.PNG)

24. **Trigger update of active_orders view when an order is delivered.**
    ```sql
    -- Query
    delimiter //
    CREATE TRIGGER after_update_orders
    AFTER UPDATE ON orders
    FOR EACH ROW
    BEGIN
        IF (old.orderActive = '1' AND new.orderActive = '0') THEN
            DELETE FROM active_orders WHERE id = old.id;
        END IF;
    END;
    //
    delimiter ;
Output 
![24](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/24.PNG)

25. **Join products and users to see who purchased what items.**
    ```sql
    -- Query
    SELECT users.id AS user_id,
       users.first_name,
       users.last_name,
       users.region,
       products.id AS product_id,
       products.title,
       products.price,
       products.count
    FROM users
    INNER JOIN orders ON users.id = orders.userId
    INNER JOIN products ON orders.productId = products.id
    ORDER BY orders.userId;
Output 
![25](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/25.PNG)

26. **Group orders by date purchased.**
    ```sql
    -- Query
    SELECT users.id AS user_id,
       users.first_name,
       users.last_name,
       users.region,
       products.id AS product_id,
       products.title,
       products.price,
       products.count,
       orders.createdAt,
       orders.updatedAt
    FROM users
    INNER JOIN orders ON users.id = orders.userId
    INNER JOIN products ON orders.productId = products.id
    ORDER BY YEAR(orders.createdAt) DESC,
             MONTH(orders.createdAt) DESC;
Output 
![26](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/26.PNG)

27. **Group orders by “is active” in ordercarts using active_orders view.**
    ```sql
    -- Query
    SELECT region, COUNT(*) AS numberOfActiveOrders
    FROM active_orders
    GROUP BY region
    ORDER BY region;
Output 
![27](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/27.PNG)

28. **Group orders created at the same date.**
    ```sql
    -- Query
    SELECT o1.id as OrderId,
       o1.userId as Customer,
       o1.productId as Product,
       o1.quantity as Quantity,
       o1.orderActive as Delivered,
       o1.orderCancelled as Cancelled,
       o1.createdAt as OrderDate,
       o2.id as OrderId,
       o2.userId as Customer,
       o2.productId as Product,
       o2.quantity as Quantity,
       o2.orderActive as Delivered,
       o1.orderCancelled as Cancelled,
       o2.createdAt as OrderDate
    FROM orders o1, orders o2
    WHERE o1.createdAt = o2.createdAt
    AND o1.userId <> o2.userId
    GROUP BY o1.createdAt
    ORDER BY o1.createdAt;
Output 
![28](https://github.com/pooja-krishan/Food-Ordering-Web-App/blob/main/fig/28.PNG)

## Conclusion

This project showcases a variety of SQL operations and queries on a shopping cart database. By working through different scenarios, it highlights the functionality and power of SQL for managing and retrieving data effectively.