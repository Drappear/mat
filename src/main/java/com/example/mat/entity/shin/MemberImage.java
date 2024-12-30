package com.example.mat.entity.shin;

import com.example.mat.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "member")
@Entity
public class MemberImage extends BaseEntity {
    // inum(seq), uuid(문자), imgName(문자),path(문자)
    @Id
    @SequenceGenerator(name = "profile_image_seq_gen", sequenceName = "profile_image_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_image_seq_gen")
    private Long inum;

    private String uuid;

    private String imgName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}