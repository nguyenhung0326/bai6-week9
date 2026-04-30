package com.banksystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một khách hàng của ngân hàng.
 *
 * <p>Một khách hàng có thể sở hữu nhiều tài khoản khác nhau
 * (vãng lai hoặc tiết kiệm).</p>
 */
public class Customer {

  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /**
   * Constructor mặc định — khởi tạo khách hàng với id=0 và tên rỗng.
   */
  public Customer() {
    this(0L, "");
  }

  /**
   * Khởi tạo khách hàng với số CMND và họ tên.
   *
   * @param idNumber số chứng minh nhân dân
   * @param fullName họ và tên đầy đủ
   */
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<>();
  }

  /**
   * Trả về số CMND.
   *
   * @return số CMND
   */
  public long getIdNumber() {
    return idNumber;
  }

  /**
   * Đặt số CMND.
   *
   * @param idNumber số CMND mới
   */
  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  /**
   * Trả về họ tên đầy đủ.
   *
   * @return họ tên
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Đặt họ tên.
   *
   * @param fullName họ tên mới
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Trả về danh sách tài khoản.
   *
   * @return danh sách tài khoản
   */
  public List<Account> getAccountList() {
    return accountList;
  }

  /**
   * Đặt danh sách tài khoản; nếu null thì khởi tạo danh sách rỗng.
   *
   * @param accountList danh sách tài khoản mới
   */
  public void setAccountList(List<Account> accountList) {
    if (accountList == null) {
      this.accountList = new ArrayList<>();
    } else {
      this.accountList = accountList;
    }
  }

  /**
   * Thêm tài khoản cho khách hàng nếu chưa tồn tại và không null.
   *
   * @param account tài khoản cần thêm
   */
  public void addAccount(Account account) {
    if (account == null) {
      return;
    }
    if (!accountList.contains(account)) {
      accountList.add(account);
    }
  }

  /**
   * Xóa tài khoản khỏi danh sách của khách hàng.
   *
   * @param account tài khoản cần xóa
   */
  public void removeAccount(Account account) {
    if (account == null) {
      return;
    }
    accountList.remove(account);
  }

  /**
   * Trả về thông tin khách hàng dạng văn bản.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
