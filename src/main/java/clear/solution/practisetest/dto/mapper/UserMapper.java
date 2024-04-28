package clear.solution.practisetest.dto.mapper;

import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AddressMapper addressMapper;


    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                addressMapper.toDTO(user.getAddress()),
                user.getPhoneNumber());
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.id())
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .birthDate(dto.birthDate())
                .address(addressMapper.toEntity(dto.address()))
                .phoneNumber(dto.phoneNumber())
                .build();
    }

    public void fullUpdate(UserDTO dto, User user) {
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setBirthDate(dto.birthDate());
        if (dto.address() != null) {
            if (user.getAddress() != null) {
                addressMapper.update(dto.address(), user.getAddress());
            } else {
                user.setAddress(addressMapper.toEntity(dto.address()));
            }
        } else {
            user.setAddress(null);
        }
        user.setPhoneNumber(dto.phoneNumber());
    }

    public void partialUpdate(UserDTO dto, User user) {
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.birthDate() != null) {
            user.setBirthDate(dto.birthDate());
        }
        if (dto.address() != null) {
            if (user.getAddress() != null) {
                addressMapper.update(dto.address(), user.getAddress());
            } else {
                user.setAddress(addressMapper.toEntity(dto.address()));
            }
        }
        if (dto.phoneNumber() != null) {
            user.setPhoneNumber(dto.phoneNumber());
        }
    }
}
