package com.banksystem;

/**
 * Ngoại lệ gốc của hệ thống ngân hàng.
 *
 * <p>Tất cả các ngoại lệ nghiệp vụ đều kế thừa từ lớp này để cho phép
 * caller bắt theo nhóm hoặc bắt riêng lẻ tùy nhu cầu.</p>
 */
public class BankException extends Exception {

  /**
   * Tạo ngoại lệ với thông điệp mô tả lỗi.
   *
   * @param message mô tả lỗi
   */
  public BankException(String message) {
    super(message);
  }
}
