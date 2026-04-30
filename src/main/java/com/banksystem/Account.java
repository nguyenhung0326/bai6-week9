package com.banksystem;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng.
 *
 * <p>Cung cấp các hoạt động cơ bản (nạp tiền, rút tiền, lịch sử giao dịch)
 * và yêu cầu lớp con cài đặt logic nghiệp vụ cụ thể.</p>
 */
public abstract class Account {

  /** Hằng số định danh loại tài khoản vãng lai. */
  public static final String CHECKING_TYPE = "CHECKING";

  /** Hằng số định danh loại tài khoản tiết kiệm. */
  public static final String SAVINGS_TYPE = "SAVINGS";

  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  private long accountNumber;
  private double balance;
  /** Danh sách giao dịch của tài khoản. */
  protected List<Transaction> transactionList;

  /**
   * Khởi tạo tài khoản với số tài khoản và số dư ban đầu.
   *
   * @param accountNumber số tài khoản
   * @param balance       số dư ban đầu
   */
  public Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactionList = new ArrayList<>();
  }

  /**
   * Trả về số tài khoản.
   *
   * @return số tài khoản
   */
  public long getAccountNumber() {
    return accountNumber;
  }

  /**
   * Đặt số tài khoản.
   *
   * @param accountNumber số tài khoản mới
   */
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Trả về số dư hiện tại.
   *
   * @return số dư
   */
  public double getBalance() {
    return balance;
  }

  /**
   * Đặt số dư tài khoản (chỉ dùng nội bộ trong package).
   *
   * @param balance số dư mới
   */
  protected void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * Trả về danh sách giao dịch.
   *
   * @return danh sách giao dịch
   */
  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  /**
   * Đặt danh sách giao dịch; nếu null thì khởi tạo danh sách rỗng.
   *
   * @param transactions danh sách giao dịch mới
   */
  public void setTransactionList(List<Transaction> transactions) {
    if (transactions == null) {
      this.transactionList = new ArrayList<>();
    } else {
      this.transactionList = transactions;
    }
  }

  /**
   * Nạp tiền vào tài khoản.
   *
   * @param amount số tiền cần nạp
   */
  public abstract void deposit(double amount);

  /**
   * Rút tiền từ tài khoản.
   *
   * @param amount số tiền cần rút
   */
  public abstract void withdraw(double amount);

  /**
   * Thực hiện nạp tiền: kiểm tra hợp lệ rồi cộng vào số dư.
   *
   * @param amount số tiền nạp
   * @throws InvalidFundingAmountException nếu số tiền không hợp lệ (âm hoặc bằng 0)
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  /**
   * Thực hiện rút tiền: kiểm tra hợp lệ và số dư rồi trừ khỏi số dư.
   *
   * @param amount số tiền rút
   * @throws InvalidFundingAmountException nếu số tiền không hợp lệ
   * @throws InsufficientFundsException    nếu số dư không đủ
   */
  protected void doWithdrawing(double amount)
      throws InvalidFundingAmountException, InsufficientFundsException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Thêm một giao dịch vào lịch sử; bỏ qua nếu giao dịch là null.
   *
   * @param transaction giao dịch cần thêm
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactionList.add(transaction);
    }
  }

  /**
   * Trả về lịch sử giao dịch của tài khoản dưới dạng chuỗi.
   *
   * <p>Ghi log ở cấp INFO khi phương thức được gọi để theo dõi tần suất truy vấn.</p>
   *
   * @return chuỗi lịch sử giao dịch
   */
  public String getTransactionHistory() {
    // INFO: ghi nhận sự kiện tra cứu lịch sử — dữ liệu quan trọng cho audit
    logger.info("Truy vấn lịch sử giao dịch tài khoản={}", accountNumber);

    StringBuilder sb = new StringBuilder();
    sb.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");
    for (int i = 0; i < transactionList.size(); i++) {
      sb.append(transactionList.get(i).getTransactionSummary());
      if (i < transactionList.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    return (int) (accountNumber ^ (accountNumber >>> 32));
  }
}
