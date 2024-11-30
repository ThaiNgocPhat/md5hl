package ra.md5.domain.order.dto.req.admin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyRevenue {
    String date;
    BigDecimal revenue;

    public DailyRevenue(LocalDateTime date, BigDecimal revenue) {
        // Format the LocalDateTime to date only (yyyy-MM-dd)
        this.date = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.revenue = revenue;
    }
}
