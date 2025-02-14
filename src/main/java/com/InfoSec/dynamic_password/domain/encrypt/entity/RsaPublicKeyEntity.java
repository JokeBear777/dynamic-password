package com.InfoSec.dynamic_password.domain.encrypt.entity;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "rsa_public_key")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RsaPublicKeyEntity {

    @Id
    @Column(name = "rsa_public_key_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rsaPublicKeyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @Column(name = "public_key", nullable = true, length = 2048)
    private String publicKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
}
