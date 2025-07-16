package com.example.marketingagencymanagement.model;

import com.example.marketingagencymanagement.dto.ClientDto;
import jakarta.ejb.Local;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "client_id")
public class Client extends User{
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tin")
    private String tin;

    @Column(name = "client_type")
    private ClientType clientType;

    @Column(name = "service_package")
    private ServicePackageType servicePackage;

    @Column(name = "verification")
    private String verificationToken;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commercial> commercials;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests;

    public Client(Long id, String name, String surname, String address, String city, String country, String phone, String username, String password, boolean enabled,boolean activationPending, LocalDateTime blocked, ArrayList<Role> roles, String companyName, String tin, ClientType clientType, ServicePackageType servicePackage, boolean using2FA, String secret2FA, boolean isBlockedUser) {
        super(id,
                name,
                surname,
                address,
                city,
                country,
                phone,
                username,
                password,
                enabled,
                activationPending,
                blocked,
                using2FA,
                secret2FA,
                isBlockedUser,
                roles
                );
        this.companyName = companyName;
        this.tin = tin;
        this.clientType = clientType;
        this.servicePackage = servicePackage;
    }

    public Client(ClientDto clientDto) {
        super(null,
                clientDto.getName(),
                clientDto.getSurname(),
                clientDto.getAddress(),
                clientDto.getCity(),
                clientDto.getCountry(),
                clientDto.getPhone(),
                clientDto.getUsername(),
                null,
                false,
                false,
                null,
                clientDto.getUsing2FA(),
                null,
                false,
                null
                );
        this.companyName = clientDto.getCompanyName();
        this.tin = clientDto.getTin();
        this.clientType = clientDto.getClientType();
        this.servicePackage = clientDto.getServicePackage();
    }
}