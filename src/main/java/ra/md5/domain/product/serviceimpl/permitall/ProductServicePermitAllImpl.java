package ra.md5.domain.product.serviceimpl.permitall;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
import ra.md5.domain.product.service.permitall.ProductServicePermitAll;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServicePermitAllImpl implements ProductServicePermitAll {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public ProductSearchResponse searchProduct(String keyword) {
        // Tìm kiếm sản phẩm theo tên
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        // Kiểm tra xem có sản phẩm nào tìm thấy không
        if (products.isEmpty()){
            throw new ProductSearchException("Không tìm thấy sản phẩm");
        }
        // Ánh xạ từ Product sang ProductSearchDto
        ModelMapper modelMapper = new ModelMapper();
        List<ProductSearchDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductSearchDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        ProductSearchResponse response = new ProductSearchResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productDtos);  // Trả về danh sách ProductSearchDto
        return response;
    }

    @Override
    public ProductListResponse listProduct(int page, int size, String sort, String direction) {
        // Sắp xếp
        Sort sortName = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        // Phân trang
        Pageable pageable = PageRequest.of(page, size, sortName);
        // Lấy danh sách sản phẩm từ repository
        Page<Product> productPage = productRepository.findAll(pageable);
        // Lọc các sản phẩm có trạng thái là true
        List<Product> filteredProducts = productPage.getContent().stream()
                .filter(product -> product.isStatus())  // Giả sử 'status' là trường boolean
                .collect(Collectors.toList());
        // Chuyển đổi các sản phẩm đã lọc thành ProductListDto
        ModelMapper modelMapper = new ModelMapper();
        List<ProductListDto> productListDtos = filteredProducts.stream()
                .map(product -> modelMapper.map(product, ProductListDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        ProductListResponse response = new ProductListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productListDtos);  // Trả về danh sách sản phẩm đã chuyển đổi
        response.setTotalElements(filteredProducts.size());  // Tổng số sản phẩm có trạng thái true
        response.setTotalPages((int) Math.ceil((double) filteredProducts.size() / size));  // Tổng số trang
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
    public ProductForCategoryResponse getAllCategoryForProduct(Integer categoryId) {
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
        List<ProductForCategoryDto> categoryForProductDtoList = products.stream()
                .map(product -> modelMapper.map(product, ProductForCategoryDto.class))
                .collect(Collectors.toList());
        // Tạo response trả về
        ProductForCategoryResponse response = new ProductForCategoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(categoryForProductDtoList); // Danh sách sản phẩm thuộc danh mục
        return response;
    }

    @Override
    public ProductNewResponse getNewProduct() {
        List<Product> newProducts = productRepository.findTop5ByStatusTrueOrderByCreatedAtDesc();
        // Chuyển đổi sang ProductNewResponse
        ModelMapper modelMapper = new ModelMapper();
        List<ProductNewDto> productNewDtoList = newProducts.stream()
               .map(product -> modelMapper.map(product, ProductNewDto.class))
               .collect(Collectors.toList());
        // Tạo response trả về
        ProductNewResponse response = new ProductNewResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(productNewDtoList);
        return response;
    }
}
