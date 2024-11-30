package ra.md5.domain.address.service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.address.dto.req.AddressAddDto;
import ra.md5.domain.address.dto.res.AddressResponse;
import ra.md5.domain.address.dto.res.AddressListForUserResponse;

public interface AddressService {
    AddressResponse addAddress(UserDetailsCustom userDetailsCustom, AddressAddDto request);
    void deleteOneAddress(UserDetailsCustom userDetailsCustom, Integer addressId);
    AddressListForUserResponse listAddressForUser(UserDetailsCustom userDetailsCustom);
    AddressResponse getAddressById(UserDetailsCustom userDetailsCustom, Integer addressId);
}
