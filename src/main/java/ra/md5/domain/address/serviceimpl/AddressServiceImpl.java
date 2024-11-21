package ra.md5.domain.address.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.address.dto.req.AddressAddDto;
import ra.md5.domain.address.dto.req.AddressAddResDto;
import ra.md5.domain.address.dto.req.AddressDto;
import ra.md5.domain.address.dto.req.AddressListForUserResDto;
import ra.md5.domain.address.dto.res.AddressAddResponse;
import ra.md5.domain.address.dto.res.AddressDeleteOneResponse;
import ra.md5.domain.address.dto.res.AddressGetByIdForUserResponse;
import ra.md5.domain.address.dto.res.AddressListForUserResponse;
import ra.md5.domain.address.entity.Address;
import ra.md5.domain.address.exception.AddressNotFoundException;
import ra.md5.domain.address.repository.AddressRepository;
import ra.md5.domain.address.service.AddressService;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.DuplicateException;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public AddressAddResponse addAddress(UserDetailsCustom userDetailsCustom, AddressAddDto request) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Kiểm tra xem địa chỉ này đã tồn tại chưa
        boolean addressExists = addressRepository.existsByAddressAndPhoneAndReceiveNameAndUser(
                request.getAddress(),
                request.getPhone(),
                request.getReceiveName(),
                user
        );
        if (addressExists) {
            throw new DuplicateException("Địa chỉ này đã tồn tại.");
        }
        // Tạo đối tượng Address mới từ DTO bằng ModelMapper
        Address addressEntity = modelMapper.map(request, Address.class);
        // Gán thông tin người dùng vào địa chỉ
        addressEntity.setUser(user);
        // Lưu địa chỉ vào cơ sở dữ liệu
        Address savedAddress = addressRepository.save(addressEntity);
        // Ánh xạ Entity đã lưu sang DTO để trả về
        AddressAddResDto responseDto = modelMapper.map(savedAddress, AddressAddResDto.class);
        // Tạo phản hồi
        AddressAddResponse response = new AddressAddResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData(responseDto);
        return response;
    }

    @Override
    public AddressDeleteOneResponse deleteOneAddress(UserDetailsCustom userDetailsCustom, Integer addressId) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra địa chỉ id có tồn tại hay không
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Không tìm thấy id " + addressId));
        //Xoá địa chỉ
        addressRepository.delete(address);
        // Tạo phản hồi
        AddressDeleteOneResponse response = new AddressDeleteOneResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Xoá thành công địa chỉ với id là: " + addressId);
        return response;
    }

    @Override
    public AddressListForUserResponse listAddressForUser(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Lấy danh sách địa chỉ của người dùng
        List<Address> addresses = addressRepository.findByUser(user);
        // Ánh xạ danh sách Address sang danh sách AddressDto
        List<AddressDto> addressDtos = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toList());
        // Tạo DTO chứa thông tin người dùng và danh sách địa chỉ
        AddressListForUserResDto addressListForUserResDto = new AddressListForUserResDto();
        addressListForUserResDto.setUsername(user.getUsername());
        addressListForUserResDto.setAddresses(addressDtos);

        // Tạo phản hồi
        AddressListForUserResponse response = new AddressListForUserResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(addressListForUserResDto);

        return response;
    }

    @Override
    public AddressGetByIdForUserResponse getAddressById(UserDetailsCustom userDetailsCustom, Integer addressId) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra địa chỉ id có tồn tại hay không
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Không tìm thấy id " + addressId));
        //Ánh xạ
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        // Tạo phản hồi
        AddressGetByIdForUserResponse response = new AddressGetByIdForUserResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(addressDto);
        return response;
    }

}
