package ra.md5.domain.order.dto.res.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.order.dto.req.admin.InvoiceData;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoicesOverTimeResponse {
    int code;
    HttpStatus message;
    List<InvoiceData> data;
}