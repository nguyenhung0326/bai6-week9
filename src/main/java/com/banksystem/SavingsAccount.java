package com.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm (Savings Account).
 *
 * <p>Áp dụng các ràng buộc nghiệp vụ:
 * <ul>
 *   <li>Số tiền rút mỗi lần không được vượt quá {@link #MAX_WITHDRAWAL_AMOUNT}.</li>
 *   <li>Số dư sau khi rút không được thấp hơn {@link #MIN_BALANCE}.</li>
 * </ul>
 * </p>
 */
public class SavingsAccount extends Account {

  /** Số tiền rút tối đa cho mỗi giao dịch (đồng). */
  public static final double MAX_WITHDRAWAL_AMOUNT = 1000.0;

  /** Số dư tối thiểu phải duy trì trong tài khoản (đồng). */
  public static final double MIN_BALANCE = 5000.0;

  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

  /**
   * Khởi tạo tài khoản tiết kiệm.
   *
   * @param accountNumber số tài khoản
   * @param balance       số dư ban đầu
   */
  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản tiết kiệm.
   *
   * <p>Ghi log INFO khi thành công, WARN khi thất bại.</p>
   *
   * @param amount số tiền cần nạp
   */
  @Override
  public void deposit(double amount) {
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      addTransaction(new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance));
      // INFO: sự kiện nghiệp vụ — nạp tiền tiết kiệm thành công
      logger.info("Nạp tiền tiết kiệm thành công. tài_khoản={} số_tiền={} số_dư_mới={}",
          getAccountNumber(), amount, finalBalance);
    } catch (BankException e) {
      // WARN: nạp tiền thất bại do số tiền không hợp lệ
      logger.warn("Nạp tiền tiết kiệm thất bại. tài_khoản={} lý_do={}",
          getAccountNumber(), e.getMessage());
    }
  }

  /**
   * Rút tiền từ tài khoản tiết kiệm.
   *
   * <p>Kiểm tra giới hạn rút tiền và số dư tối thiểu trước khi thực hiện.
   * Ghi log INFO khi thành công, WARN khi vi phạm quy tắc nghiệp vụ.</p>
   *
   * @param amount số tiền cần rút
   */
  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAWAL_AMOUNT) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE) {
        throw new InsufficientFundsException(amount);
      }
      doWithdrawing(amount);
      double finalBalance = getBalance();
      addTransaction(new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance));
      // INFO: sự kiện nghiệp vụ — rút tiền tiết kiệm thành công
      logger.info("Rút tiền tiết kiệm thành công. tài_khoản={} số_tiền={} số_dư_còn={}",
          getAccountNumber(), amount, finalBalance);
    } catch (BankException e) {
      // WARN: rút tiền thất bại do vi phạm quy tắc tiết kiệm
      logger.warn("Rút tiền tiết kiệm thất bại. tài_khoản={} lý_do={}",
          getAccountNumber(), e.getMessage());
    }
  }
}
