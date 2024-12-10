package com.management.kbbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class UserSimpleLoanDTO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long loanid;
    private Long bookid;
    private String bookname;
    private String message;

    public UserSimpleLoanDTO(Long loanId, Long bookId, String bookName, String message) {
        this.loanid = loanId;
        this.bookid = bookId;
        this.bookname = bookName;
        this.message = formatMessage(message);
    }

    private String formatMessage(String rawMessage) {
        if (rawMessage.startsWith("已於")) {
            // 格式化歸還日期
            String[] parts = rawMessage.split(" ");
            String returnDate = parts[1]; // 提取原始日期部分
            LocalDate parsedDate = LocalDate.parse(returnDate, DateTimeFormatter.ofPattern("dd-MMM-yy")); // 假設原始日期格式
            return "已於 " + parsedDate.format(FORMATTER) + " 歸還";
        }
        else if (rawMessage.startsWith("還剩") || rawMessage.startsWith("已到期")) {
            // 處理還書期限的天數問題
            try {
                System.out.println("原始訊息：" + rawMessage);
                String[] parts = rawMessage.split(" ");
                long rawDays = Long.parseLong(parts[1]); // 提取原始天數部分
                long days = rawDays / (24 * 60 * 60 * 1000); // 將毫秒轉換為天數

                // 將 days 轉為字符串並截取前兩位數字
                String daysFormatted = String.valueOf(days);
                if (daysFormatted.length() > 2) {
                    daysFormatted = daysFormatted.substring(0, 2); // 只保留前兩位
                }

                if (rawMessage.startsWith("還剩")) {
                    return "還剩 " + daysFormatted + " 天到期";
                } else {
                    return "已到期 " + Math.abs(Long.parseLong(daysFormatted)) + " 天";
                }
            } catch (NumberFormatException e) {
                return rawMessage; // 若解析失敗，返回原始訊息
            }
        }
        return rawMessage; // 默認返回原始訊息
    }
}