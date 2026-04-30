package com.banksystem;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Đại diện cho một giao dịch tài chính trong hệ thống ngân hàng.
 *
 * <p>Mỗi giao dịch lưu trữ kiểu giao dịch, số tiền, số dư ban đầu
 * và số dư sau giao dịch để phục vụ tra cứu lịch sử.</p>
 */
public class Transaction {

  /** Kiểu giao dịch: nạp tiền vào tài khoản vãng lai. */
  public static final int TYPE_DEPOSIT_CHECKING = 1;

  /** Kiểu giao dịch: rút tiền từ tài khoản vãng lai. */
  public static final int TYPE_WITHDRAW_CHECKING = 2;

  /** Kiểu giao dịch: nạp tiền vào tài khoản tiết kiệm. */
  public static final int TYPE_DEPOSIT_SAVINGS = 3;

  /** Kiểu giao dịch: rút tiền từ tài khoản tiết kiệm. */
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  /**
   * Tạo một giao dịch với đầy đủ thông tin.
   *
   * @param type           kiểu giao dịch (dùng hằng số TYPE_*)
   * @param amount         số tiền giao dịch
   * @param initialBalance số dư tài khoản trước giao dịch
   * @param finalBalance   số dư tài khoản sau giao dịch
   */
  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  /**
   * Trả về kiểu giao dịch.
   *
   * @return kiểu giao dịch
   */
  public int getType() {
    return type;
  }

  /**
   * Đặt kiểu giao dịch.
   *
   * @param type kiểu giao dịch mới
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Trả về số tiền giao dịch.
   *
   * @return số tiền
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Đặt số tiền giao dịch.
   *
   * @param amount số tiền mới
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Trả về số dư tài khoản trước khi giao dịch.
   *
   * @return số dư ban đầu
   */
  public double getInitialBalance() {
    return initialBalance;
  }

  /**
   * Đặt số dư ban đầu.
   *
   * @param initialBalance số dư trước giao dịch
   */
  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  /**
   * Trả về số dư tài khoản sau khi giao dịch.
   *
   * @return số dư cuối
   */
  public double getFinalBalance() {
    return finalBalance;
  }

  /**
   * Đặt số dư cuối.
   *
   * @param finalBalance số dư sau giao dịch
   */
  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

  /**
   * Trả về tên mô tả của kiểu giao dịch.
   *
   * @param transactionType kiểu giao dịch (dùng hằng số TYPE_*)
   * @return chuỗi mô tả kiểu giao dịch
   */
  public static String getTypeString(int transactionType) {
    switch (transactionType) {
      case TYPE_DEPOSIT_CHECKING:
        return "Nạp tiền vãng lai";
      case TYPE_WITHDRAW_CHECKING:
        return "Rút tiền vãng lai";
      case TYPE_DEPOSIT_SAVINGS:
        return "Nạp tiền tiết kiệm";
      case TYPE_WITHDRAW_SAVINGS:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  /**
   * Tạo chuỗi tóm tắt thông tin giao dịch theo định dạng chuẩn.
   *
   * <p>Ghi log ở cấp DEBUG khi tạo tóm tắt để hỗ trợ theo dõi luồng dữ liệu.</p>
   *
   * @return chuỗi tóm tắt giao dịch
   */
  public String getTransactionSummary() {
    // DEBUG: ghi lại khi tạo tóm tắt — hữu ích khi trace lịch sử giao dịch
    logger.debug("Tạo tóm tắt giao dịch kiểu={}", getTypeString(type));

    String typeLabel = getTypeString(type);
    String formattedInitial = String.format(Locale.US, "%.2f", initialBalance);
    String formattedAmount = String.format(Locale.US, "%.2f", amount);
    String formattedFinal = String.format(Locale.US, "%.2f", finalBalance);

    return "- Kiểu giao dịch: " + typeLabel
        + ". Số dư ban đầu: $" + formattedInitial
        + ". Số tiền: $" + formattedAmount
        + ". Số dư cuối: $" + formattedFinal + ".";
  }
}
