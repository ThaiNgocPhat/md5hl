package ra.md5.domain.product.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.product.dto.req.permitall.*;
import ra.md5.domain.product.dto.res.permitall.*;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.exception.ProductSearchException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.product.service.ProductServicePermitAll;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServicePermitAllImpl implements ProductServicePermitAll {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public ProductResponse searchProduct(String keyword) {
        // Tìm kiếm sản phẩm theo tên
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        // Kiểm tra xem có sản phẩm nào tìm thấy không
        if (products.isEmpty()){
            throw new ProductSearchException("Không tìm thấy sản phẩm");
        }
        // Ánh xạ từ Product sang ProductSearchDto
        ModelMapper modelMapper = new ModelMapper();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        ProductResponse response = new ProductResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productDtos);  // Trả về danh sách ProductSearchDto
        return response;
    }

    @Override
    public ProductListResponse listProduct(int page, int size, String sort, String direction) {
        // Sắp xếp
        Sort sortName = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sortName);
        // Lấy toàn bộ sản phẩm (bỏ qua phân trang)
        List<Product> allProducts = productRepository.findAll();
        // Lọc các sản phẩm phù hợp (status=true, isDeleted=false, danh mục isDeleted=false)
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.isStatus() &&                      // Trạng thái sản phẩm phải là true
                        !product.getIsDeleted() &&                  // Sản phẩm chưa bị xóa
                        (product.getCategory() == null ||           // Danh mục có thể null
                                !product.getCategory().isDeleted())) // Danh mục chưa bị xóa
                .collect(Collectors.toList());
        // Tổng số sản phẩm phù hợp
        int totalElements = filteredProducts.size();
        // Phân trang thủ công với danh sách đã lọc
        List<Product> paginatedProducts = filteredProducts.stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
        // Chuyển đổi các sản phẩm đã lọc thành ProductDto
        ModelMapper modelMapper = new ModelMapper();
        List<ProductDto> productListDtos = paginatedProducts.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        ProductListResponse response = new ProductListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setTotalElements(totalElements);  // Số lượng sản phẩm phù hợp
        response.setTotalPages((int) Math.ceil((double) totalElements / size));  // Tổng số trang dựa trên số sản phẩm phù hợp
        response.setPage(page);  // Trang hiện tại
        response.setSize(paginatedProducts.size());  // Số lượng sản phẩm trên trang
        response.setData(productListDtos);  // Danh sách sản phẩm phù hợp trong trang hiện tại
        return response;
    }



    @Override
    public ProductFindByIdResponse findProductById(Integer productId) {
        //Tìm id sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + productId));
        //Chuyển đổi
        ModelMapper modelMapper = new ModelMapper();
        ProductFindByIdDto map = modelMapper.map(product, ProductFindByIdDto.class);
        //Trả về thông báo
        ProductFindByIdResponse response = new ProductFindByIdResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(map);
        return response;
    }

    @Override
    public CategoryForProductResponse getAllCategoryForProduct(Integer categoryId) {
        // Kiểm tra id có tồn tại hay không
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Danh mục không tồn tại");
        }
        // Lấy danh mục theo id
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        // Kiểm tra xem danh mục có tồn tại không
        Category category = categoryOptional.orElseThrow(() -> new CategoryNotFoundException("Danh mục không tồn tại"));
        // Lấy danh sách sản phẩm thuộc danh mục
        List<Product> products = productRepository.findByCategoryId(categoryId);
        // Ánh xạ các sản phẩm sang CategoryForProductDto
        ModelMapper modelMapper = new ModelMapper();
        List<CategoryForProductDto> categoryForProductDtoList = products.stream()
                .map(product -> modelMapper.map(product, CategoryForProductDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        CategoryForProductResponse response = new CategoryForProductResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(categoryForProductDtoList);
        return response;
    }

    @Override
    public ProductResponse getNewProduct() {
        List<Product> newProducts = productRepository.findTop5ByStatusTrueOrderByCreatedAtDesc();
        // Chuyển đổi sang ProductNewResponse
        ModelMapper modelMapper = new ModelMapper();
        List<ProductDto> productNewDtoList = newProducts.stream()
               .map(product -> modelMapper.map(product, ProductDto.class))
               .collect(Collectors.toList());
        // Tạo response trả về
        ProductResponse response = new ProductResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productNewDtoList);
        return response;
    }

    @Override
    public BestSellerProductResponse getTopSellingProducts() {
        // Lấy top 5 sản phẩm bán chạy nhất trực tiếp từ cơ sở dữ liệu
        List<Product> topSellingProducts = productRepository.findTop5BySoldCountGreaterThanOrderBySoldCountDesc(0);
        // Chuyển đổi từ danh sách sản phẩm sang danh sách BestSellerProduct
        List<BestSellerProduct> bestSellerProducts = topSellingProducts.stream()
                .map(product -> new BestSellerProduct(
                        product.getProductId(),
                        product.getProductName(),
                        product.getUnitPrice(),
                        product.getSoldCount()
                ))
                .collect(Collectors.toList());
        // Tạo đối tượng BestSellerProductResponse để trả về
        BestSellerProductResponse response = new BestSellerProductResponse();
        response.setCode(200);  // Mã trạng thái (hoặc mã khác tùy vào yêu cầu)
        response.setMessage(HttpStatus.OK);  // Trạng thái HTTP (hoặc tùy chỉnh)
        response.setData(bestSellerProducts);  // Dữ liệu là danh sách sản phẩm bán chạy
        return response;
    }
}
