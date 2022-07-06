package ru.maxi.retail.osminina.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.maxi.retail.osminina.view.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueryManager {
    private static final String TOP_PRODUCT_SQL = "SELECT top (3) sum(count) as count, name, product_code FROM PRODUCTS" +
            " join SALES_PRODUCTS on PRODUCTS.ID = SALES_PRODUCTS.PRODUCTS_ID" +
            " join SALES on SALES.ID = SALES_PRODUCTS.SALE_ID" +
            " where card_number = %s group by product_code order by count desc";
    private static final String TOTAL_SUM_SQL = "SELECT sum(price*count) FROM PRODUCTS" +
            " join SALES_PRODUCTS on PRODUCTS.ID =SALES_PRODUCTS.PRODUCTS_ID" +
            " join SALES on SALES.ID = SALES_PRODUCTS .SALE_ID" +
            " where cast(SALES.DATE as date) = '%s'";
    public static final String SALES_COUNT_SQL = "SELECT count(id) FROM SALES";
    public static final String KPP_COUNT_SQL = "SELECT count(DISTINCT CARD_NUMBER) FROM SALES";
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryManager.class);


    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    public List<Product> getTop(String kpp) {
        List products = new ArrayList();
        if (kpp == null || kpp.isEmpty()) {
            LOGGER.warn("В метод getTop передан пустой номер КПП");
            return products;
        }
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(TOP_PRODUCT_SQL, kpp));
            while (resultSet.next()) {
                products.add(new Product(resultSet.getString("NAME"),
                        resultSet.getString("product_code"),
                        resultSet.getString("count")));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при выполнении метода getTop", e);
        }
        return products;
    }

    public BigDecimal getTotalSum(Date date) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(TOTAL_SUM_SQL, formattedDate));
            if (resultSet.next()) {
                BigDecimal totalSum = resultSet.getBigDecimal(1);
                return totalSum == null ? BigDecimal.ZERO : totalSum;
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при выполнении метода getTotalSum", e);
        }
        return BigDecimal.ZERO;
    }

    public Long getCheckCount() {
        return executeQueryIntResult(SALES_COUNT_SQL);
    }

    public Long getKppCount() {
        return executeQueryIntResult(KPP_COUNT_SQL);
    }

    private Long executeQueryIntResult(String query) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при выполнении метода executeQueryIntResult", e);
        }
        return 0L;
    }
}
