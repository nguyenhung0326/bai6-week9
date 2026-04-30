package com.banksystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Kiểm thử các chức năng cốt lõi của hệ thống ngân hàng.
 */
class BankSystemTest {

  private CheckingAccount checkingAccount;
  private SavingsAccount savingsAccount;

  /**
   * Thiết lập dữ liệu kiểm thử trước mỗi test case.
   */
  @BeforeEach
  void setUp() {
    checkingAccount = new CheckingAccount(1001L, 10000.0);
    savingsAccount = new SavingsAccount(2001L, 10000.0);
  }

  @Test
  @DisplayName("Nạp tiền hợp lệ vào tài khoản vãng lai")
  void depositCheckingValidAmount() {
    checkingAccount.deposit(500.0);
    Assertions.assertEquals(10500.0, checkingAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Nạp tiền âm vào vãng lai không thay đổi số dư")
  void depositCheckingNegativeAmountNoChange() {
    checkingAccount.deposit(-100.0);
    Assertions.assertEquals(10000.0, checkingAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Rút tiền hợp lệ từ tài khoản vãng lai")
  void withdrawCheckingValidAmount() {
    checkingAccount.withdraw(1000.0);
    Assertions.assertEquals(9000.0, checkingAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Rút tiền vượt số dư vãng lai không thay đổi số dư")
  void withdrawCheckingInsufficientFundsNoChange() {
    checkingAccount.withdraw(20000.0);
    Assertions.assertEquals(10000.0, checkingAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Nạp tiền hợp lệ vào tài khoản tiết kiệm")
  void depositSavingsValidAmount() {
    savingsAccount.deposit(2000.0);
    Assertions.assertEquals(12000.0, savingsAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Rút tiền không vượt giới hạn và không vi phạm số dư tối thiểu")
  void withdrawSavingsValidAmount() {
    savingsAccount.withdraw(500.0);
    Assertions.assertEquals(9500.0, savingsAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Rút tiền vượt MAX_WITHDRAWAL_AMOUNT không thay đổi số dư tiết kiệm")
  void withdrawSavingsExceedsMaxAmount() {
    savingsAccount.withdraw(SavingsAccount.MAX_WITHDRAWAL_AMOUNT + 1);
    Assertions.assertEquals(10000.0, savingsAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Rút tiền vi phạm MIN_BALANCE không thay đổi số dư tiết kiệm")
  void withdrawSavingsViolatesMinBalance() {
    // Số dư 10000, rút 5001 → còn 4999 < MIN_BALANCE(5000) → bị từ chối
    savingsAccount.withdraw(5001.0);
    Assertions.assertEquals(10000.0, savingsAccount.getBalance(), 0.001);
  }

  @Test
  @DisplayName("Giao dịch được ghi vào lịch sử sau khi nạp tiền")
  void transactionRecordedAfterDeposit() {
    checkingAccount.deposit(300.0);
    Assertions.assertEquals(1, checkingAccount.getTransactionList().size());
  }

  @Test
  @DisplayName("Transaction.getTypeString trả về đúng mô tả")
  void transactionGetTypeString() {
    Assertions.assertEquals("Nạp tiền vãng lai",
        Transaction.getTypeString(Transaction.TYPE_DEPOSIT_CHECKING));
    Assertions.assertEquals("Rút tiền tiết kiệm",
        Transaction.getTypeString(Transaction.TYPE_WITHDRAW_SAVINGS));
    Assertions.assertEquals("Không rõ", Transaction.getTypeString(99));
  }

  @Test
  @DisplayName("Account.equals so sánh đúng theo số tài khoản")
  void accountEqualsById() {
    CheckingAccount other = new CheckingAccount(1001L, 9999.0);
    Assertions.assertEquals(checkingAccount, other);
  }
}
