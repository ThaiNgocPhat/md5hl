package ra.md5.domain.product.serviceimpl.admin;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.product.dto.res.admin.ProductFindByIdResponse;
import ra.md5.domain.product.dto.res.admin.ProductListResponse;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.product.service.admin.ProductServiceAdmin;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductServiceAdminImpl implements ProductServiceAdmin {
    private final ProductRepository productRepository;


    @Override
    public ProductListResponse listProduct(int page, int size, String sort, String direction) {
        //Sắp xếp
        Sort sortName = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        //Phân trang
        Pageable pageable = PageRequest.of(page, size, sortName);
        // Lấy danh sách sản phẩm
        Page<Product> productPage = productRepository.findAll(pageable);
        // Tạo response trả về
        ProductListResponse response = new ProductListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productPage.getContent());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        return response;
    }

    @Override
    public ProductFindByIdResponse getProductById(Integer productId) {
        //Kiểm tra sản phẩm có tồn tịa hay không
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + productId));
        //Thông báo trả về
        ProductFindByIdResponse response = new ProductFindByIdResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(product);
        return response;
    }

}
