package ra.md5.domain.product.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.product.dto.req.admin.BestSellerData;
import ra.md5.domain.product.dto.req.admin.BestSellerRequest;
import ra.md5.domain.product.dto.req.admin.MostLikedProductData;
import ra.md5.domain.product.dto.req.admin.MostLikedRequest;
import ra.md5.domain.product.dto.res.admin.BestSellerResponse;
import ra.md5.domain.product.dto.res.admin.MostLikedResponse;
import ra.md5.domain.product.dto.res.admin.ProductFindByIdResponse;
import ra.md5.domain.product.dto.res.admin.ProductListResponse;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.product.service.ProductServiceAdmin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        response.setPage(page);
        response.setSize(size);
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

    @Override
    public BestSellerResponse getBestSellerProducts(BestSellerRequest request) {
        LocalDateTime fromDateTime = request.getFromDateTime();
        LocalDateTime toDateTime = request.getToDateTime();

        // Call the repository method, passing LocalDateTime parameters
        List<Object[]> result = productRepository.findBestSellers(fromDateTime, toDateTime);

        // Process the result and return the response
        List<BestSellerData> data = result.stream()
                .map(row -> {
                    BestSellerData bestSeller = new BestSellerData(
                            // Convert productId to String if it's not already
                            String.valueOf(row[0]),
                            // Convert productName to String if it's not already
                            String.valueOf(row[1]),
                            // Cast the totalSold (sum of quantities) to long
                            ((Number) row[2]).longValue()
                    );
                    return bestSeller;
                })
                .collect(Collectors.toList());

        BestSellerResponse response = new BestSellerResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }

    @Override
    public MostLikedResponse getMostLikedProducts(MostLikedRequest request) {
        LocalDateTime fromDateTime = request.getFromDateTime();
        LocalDateTime toDateTime = request.getToDateTime();
        List<Object[]> result = productRepository.findMostWishlistProducts(fromDateTime, toDateTime);
        List<MostLikedProductData> data = result.stream()
                .map(row -> {
                    MostLikedProductData product = new MostLikedProductData();
                    // Ensure productId and productName are treated as String
                    product.setProductId(String.valueOf(row[0])); // Safely cast to String
                    product.setProductName(String.valueOf(row[1])); // Safely cast to String
                    // Safely cast to long
                    if (row[2] instanceof Number) {
                        product.setTotalLikes(((Number) row[2]).longValue());
                    } else {
                        product.setTotalLikes(0L); // Default value if not a Number
                    }
                    return product;
                })
                .collect(Collectors.toList());
        MostLikedResponse response = new MostLikedResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }
}
