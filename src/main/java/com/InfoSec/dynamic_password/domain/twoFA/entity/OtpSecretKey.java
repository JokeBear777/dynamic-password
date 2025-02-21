package com.InfoSec.dynamic_password.domain.twoFA.entity;

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
@Table(name = "otp_secret_key")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OtpSecretKey {

    @Id
    @Column(name = "otp_secret_key_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long otpSecretKeyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @Column(name = "otp_secret_key", nullable = true, length = 2048)
    private String otpSecretKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
}

