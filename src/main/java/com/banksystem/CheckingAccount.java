package com.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản vãng lai (Checking Account).
 *
 * <p>Không giới hạn số tiền rút mỗi lần (miễn là đủ số dư).
 * Mọi giao dịch được ghi lại và log theo cấp độ phù hợp.</p>
 */
public class CheckingAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

  /**
   * Khởi tạo tài khoản vãng lai.
   *
   * @param accountNumber số tài khoản
   * @param balance       số dư ban đầu
   */
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản vãng lai.
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
          Transaction.TYPE_DEPOSIT_CHECKING, amount, initialBalance, finalBalance));
      // INFO: sự kiện nghiệp vụ quan trọng — nạp tiền thành công
      logger.info("Nạp tiền vãng lai thành công. tài_khoản={} số_tiền={} số_dư_mới={}",
          getAccountNumber(), amount, finalBalance);
    } catch (BankException e) {
      // WARN: thao tác thất bại nhưng hệ thống vẫn tiếp tục — cần theo dõi
      logger.warn("Nạp tiền vãng lai thất bại. tài_khoản={} lý_do={}",
          getAccountNumber(), e.getMessage());
    }
  }

  /**
   * Rút tiền từ tài khoản vãng lai.
   *
   * <p>Ghi log INFO khi thành công, WARN khi thất bại.</p>
   *
   * @param amount số tiền cần rút
   */
  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      double finalBalance = getBalance();
      addTransaction(new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING, amount, initialBalance, finalBalance));
      // INFO: sự kiện nghiệp vụ quan trọng — rút tiền thành công
      logger.info("Rút tiền vãng lai thành công. tài_khoản={} số_tiền={} số_dư_còn={}",
          getAccountNumber(), amount, finalBalance);
    } catch (BankException e) {
      // WARN: rút tiền thất bại — có thể do số dư không đủ, cần theo dõi
      logger.warn("Rút tiền vãng lai thất bại. tài_khoản={} lý_do={}",
          getAccountNumber(), e.getMessage());
    }
  }
}
