package ra.md5.domain.product.dto.req.admin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MostLikedRequest {
    String from;
    String to;

    public LocalDateTime getFromDateTime() {
        return LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    }

    public LocalDateTime getToDateTime() {
        return LocalDate.parse(to, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    }
}
