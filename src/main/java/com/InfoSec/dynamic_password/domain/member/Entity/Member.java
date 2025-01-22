package com.InfoSec.dynamic_password.domain.member.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "description" ,nullable = true)
    private String description;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_mobile", nullable = false)
    private String memberMobile;

    @Column(name = "member_role", nullable = false)
    private String memberRole;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
}
