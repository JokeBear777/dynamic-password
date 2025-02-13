package com.InfoSec.dynamic_password.domain.password.entity;

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
@Table(name = "password")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Password {

    @Id
    @Column(name = "password_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @Column(name = "site_name", nullable = false)
    private String siteName;

    @Column(name = "site_address", nullable = false)
    private String siteAddress;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}
