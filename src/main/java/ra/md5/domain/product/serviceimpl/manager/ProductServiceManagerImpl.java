package ra.md5.domain.product.serviceimpl.manager;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.md5.common.utils.UploadService;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.product.dto.req.manager.ProductAddDto;
import ra.md5.domain.product.dto.req.manager.ProductUpdateDto;
import ra.md5.domain.product.dto.res.manager.ProductAddResponse;
import ra.md5.domain.product.dto.res.manager.ProductDeleteResponse;
import ra.md5.domain.product.dto.res.manager.ProductUpdateResponse;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ImageUploadException;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.product.service.manager.ProductServiceManager;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceManagerImpl implements ProductServiceManager {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UploadService uploadService;
    private final ModelMapper modelMapper;
    @Override
    public ProductAddResponse createProduct(ProductAddDto request){
        // Ánh xạ từ DTO sang Entity
        Product product = modelMapper.map(request, Product.class);
        // Kiểm tra và upload ảnh
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                imageUrl = uploadService.uploadFileToDrive(request.getImage());
            } catch (IOException e) {
                throw new ImageUploadException("Lỗi khi tải ảnh lên: " + e.getMessage());
            }
        } else {
            imageUrl = "URL_ẢNH_MẶC_ĐỊNH"; // Bạn có thể thay thế bằng URL ảnh mặc định nếu không có ảnh
        }
        // Kiểm tra tồn tại của danh mục
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy id danh mục"));
        // Gán danh mục và ảnh cho sản phẩm
        product.setCategory(category);
        product.setImage(imageUrl);
        product.setCreatedAt(LocalDateTime.now());
        product.setIsDeleted(false);
        // Lưu sản phẩm vào cơ sở dữ liệu
        Product savedProduct = productRepository.save(product);
        // Tạo response trả về
        ProductAddResponse response = new ProductAddResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData(savedProduct);
        return response;
    }

    @Override
    public ProductUpdateResponse updateProduct(Integer productId, ProductUpdateDto request) {
        // Tìm sản phẩm theo productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + productId));
        // Kiểm tra và upload ảnh nếu có ảnh mới
        String imageUrl = product.getImage();  // Lấy ảnh cũ trước, nếu có
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                imageUrl = uploadService.uploadFileToDrive(request.getImage());
            } catch (IOException e) {
                throw new ImageUploadException("Lỗi tải ảnh lên: " + e.getMessage());
            }
        }
        // Cập nhật ảnh (hoặc giữ nguyên nếu không có ảnh mới)
        product.setImage(imageUrl);
        // Sử dụng ModelMapper để ánh xạ các trường từ DTO sang Entity
        modelMapper.map(request, product);
        // Cập nhật thời gian sửa đổi
        product.setUpdatedAt(LocalDateTime.now());
        // Lưu sản phẩm đã được cập nhật
        Product updatedProduct = productRepository.save(product);
        // Tạo và trả về phản hồi
        ProductUpdateResponse response = new ProductUpdateResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(updatedProduct);
        return response;
    }

    @Override
    public ProductDeleteResponse deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + productId));

        // Cập nhật trạng thái isDeleted thành true để "ẩn" sản phẩm
        product.setIsDeleted(!product.getIsDeleted());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);  // Lưu cập nhật vào cơ sở dữ liệu

        // Tạo phản hồi sau khi xóa
        String action = product.getIsDeleted() ? " đã ẩn " : " đã hiện ";
        ProductDeleteResponse response = new ProductDeleteResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Sản phẩm " + productId + action + " thành công");

        return response;
    }
}
