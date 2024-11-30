package ra.md5.domain.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.address.entity.Address;
import ra.md5.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    boolean existsByAddressAndPhoneAndReceiveNameAndUser(String address, String phone, String receiveName, User user);
    List<Address> findByUser(User user);
}
