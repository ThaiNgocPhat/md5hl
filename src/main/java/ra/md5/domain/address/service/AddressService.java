package ra.md5.domain.address.service;

import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.address.dto.req.AddressAddDto;
import ra.md5.domain.address.dto.res.AddressAddResponse;
import ra.md5.domain.address.dto.res.AddressDeleteOneResponse;
import ra.md5.domain.address.dto.res.AddressGetByIdForUserResponse;
import ra.md5.domain.address.dto.res.AddressListForUserResponse;

public interface AddressService {
    AddressAddResponse addAddress(UserDetailsCustom userDetailsCustom, AddressAddDto request);
    AddressDeleteOneResponse deleteOneAddress(UserDetailsCustom userDetailsCustom, Integer addressId);
    AddressListForUserResponse listAddressForUser(UserDetailsCustom userDetailsCustom);
    AddressGetByIdForUserResponse getAddressById(UserDetailsCustom userDetailsCustom, Integer addressId);
}
