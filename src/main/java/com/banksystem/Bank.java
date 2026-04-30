package com.banksystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp quản lý danh sách khách hàng và tài khoản của ngân hàng.
 *
 * <p>Cung cấp các chức năng đọc dữ liệu từ luồng đầu vào và
 * truy vấn thông tin khách hàng theo thứ tự khác nhau.</p>
 */
public class Bank {

  /** Regex xác định chuỗi 9 chữ số (số CMND). */
  private static final String PATTERN_ID_NUMBER = "\\d{9}";

  private static final Logger logger = LoggerFactory.getLogger(Bank.class);

  private List<Customer> customerList;

  /**
   * Khởi tạo ngân hàng với danh sách khách hàng rỗng.
   */
  public Bank() {
    this.customerList = new ArrayList<>();
  }

  /**
   * Trả về danh sách khách hàng.
   *
   * @return danh sách khách hàng
   */
  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Đặt danh sách khách hàng; nếu null thì khởi tạo danh sách rỗng.
   *
   * @param customers danh sách khách hàng mới
   */
  public void setCustomerList(List<Customer> customers) {
    if (customers == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customers;
    }
  }

  /**
   * Đọc danh sách khách hàng và tài khoản từ luồng đầu vào.
   *
   * <p>Định dạng dữ liệu đầu vào:
   * <ul>
   *   <li>Dòng khách hàng: {@code <họ tên> <9 chữ số CMND>}</li>
   *   <li>Dòng tài khoản: {@code <số tài khoản> <CHECKING|SAVINGS> <số dư>}</li>
   * </ul>
   * Ghi log INFO khi thêm khách hàng, DEBUG khi thêm tài khoản,
   * ERROR khi gặp ngoại lệ không mong đợi.</p>
   *
   * @param inputStream luồng dữ liệu đầu vào
   */
  public void readCustomerList(InputStream inputStream) {
    if (inputStream == null) {
      logger.warn("readCustomerList được gọi với inputStream=null, bỏ qua.");
      return;
    }

    // INFO: ghi lại thời điểm bắt đầu tải dữ liệu
    logger.info("Bắt đầu đọc dữ liệu khách hàng từ luồng đầu vào.");

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    Customer current = null;

    try {
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }
        current = parseLine(line, current);
      }
    } catch (Exception e) {
      // ERROR: lỗi IO không mong đợi — cần điều tra ngay
      logger.error("Lỗi khi đọc dữ liệu khách hàng: {}", e.getMessage(), e);
    }

    logger.info("Hoàn tất đọc dữ liệu. Tổng số khách hàng={}", customerList.size());
  }

  /**
   * Phân tích một dòng dữ liệu và cập nhật trạng thái khách hàng hiện tại.
   *
   * @param line    dòng dữ liệu cần xử lý
   * @param current khách hàng đang được xử lý
   * @return khách hàng sau khi xử lý dòng
   */
  private Customer parseLine(String line, Customer current) {
    int lastSpace = line.lastIndexOf(' ');
    if (lastSpace <= 0) {
      return current;
    }

    String token = line.substring(lastSpace + 1).trim();
    if (token.matches(PATTERN_ID_NUMBER)) {
      String name = line.substring(0, lastSpace).trim();
      current = new Customer(Long.parseLong(token), name);
      customerList.add(current);
      // INFO: thêm khách hàng mới vào hệ thống
      logger.info("Thêm khách hàng: tên={} cmnd={}", name, token);
    } else if (current != null) {
      current = parseAccountLine(line, current);
    }
    return current;
  }

  /**
   * Phân tích dòng tài khoản và thêm vào khách hàng hiện tại.
   *
   * @param line    dòng dữ liệu tài khoản
   * @param current khách hàng hiện tại
   * @return khách hàng sau khi thêm tài khoản
   */
  private Customer parseAccountLine(String line, Customer current) {
    String[] parts = line.split("\\s+");
    if (parts.length < 3) {
      return current;
    }

    try {
      long accountNumber = Long.parseLong(parts[0]);
      double balance = Double.parseDouble(parts[2]);
      String accountType = parts[1];

      if (Account.CHECKING_TYPE.equals(accountType)) {
        current.addAccount(new CheckingAccount(accountNumber, balance));
        // DEBUG: chi tiết tài khoản — hữu ích khi kiểm tra dữ liệu import
        logger.debug("Thêm tài khoản vãng lai: số={} số_dư={}", accountNumber, balance);
      } else if (Account.SAVINGS_TYPE.equals(accountType)) {
        current.addAccount(new SavingsAccount(accountNumber, balance));
        logger.debug("Thêm tài khoản tiết kiệm: số={} số_dư={}", accountNumber, balance);
      } else {
        logger.warn("Loại tài khoản không nhận dạng được: {}", accountType);
      }
    } catch (NumberFormatException e) {
      logger.error("Dòng tài khoản có định dạng sai: '{}' lý_do={}", line, e.getMessage());
    }
    return current;
  }

  /**
   * Trả về thông tin tất cả khách hàng, sắp xếp tăng dần theo số CMND.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByIdOrder() {
    List<Customer> sorted = new ArrayList<>(customerList);
    sorted.sort((c1, c2) -> Long.compare(c1.getIdNumber(), c2.getIdNumber()));
    return buildCustomerInfoString(sorted);
  }

  /**
   * Trả về thông tin tất cả khách hàng, sắp xếp theo họ tên (A-Z),
   * nếu trùng tên thì sắp theo số CMND.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByNameOrder() {
    List<Customer> sorted = new ArrayList<>(customerList);
    sorted.sort((c1, c2) -> {
      int nameCompare = c1.getFullName().compareTo(c2.getFullName());
      return nameCompare != 0 ? nameCompare : Long.compare(c1.getIdNumber(), c2.getIdNumber());
    });
    return buildCustomerInfoString(sorted);
  }

  /**
   * Xây dựng chuỗi thông tin từ danh sách khách hàng đã sắp xếp.
   *
   * @param customers danh sách khách hàng đã sắp xếp
   * @return chuỗi thông tin
   */
  private String buildCustomerInfoString(List<Customer> customers) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < customers.size(); i++) {
      sb.append(customers.get(i).getCustomerInfo());
      if (i < customers.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
