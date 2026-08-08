package com.aeropelican.userservice.entity;

import com.aeropelican.userservice.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(
            name = "address_id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private UUID addressId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "address_type",
            nullable = false,
            length = 20
    )
    private AddressType addressType;

    @Column(
            name = "recipient_name",
            nullable = false,
            length = 150
    )
    private String recipientName;

    @Column(
            name = "phone_number",
            length = 20
    )
    private String phoneNumber;

    @Column(
            name = "address_line_1",
            nullable = false,
            length = 255
    )
    private String addressLine1;

    @Column(
            name = "address_line_2",
            length = 255
    )
    private String addressLine2;

    @Column(
            name = "landmark",
            length = 255
    )
    private String landmark;

    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;

    @Column(
            name = "state",
            nullable = false,
            length = 100
    )
    private String state;

    @Column(
            name = "country",
            nullable = false,
            length = 100
    )
    private String country;

    @Column(
            name = "postal_code",
            nullable = false,
            length = 20
    )
    private String postalCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(
            name = "is_default",
            nullable = false
    )
    @Builder.Default
    private Boolean isDefault = false;
}