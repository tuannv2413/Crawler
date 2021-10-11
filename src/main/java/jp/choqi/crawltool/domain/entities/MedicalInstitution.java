package jp.choqi.crawltool.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "medical_institution")
public class MedicalInstitution implements Serializable {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", allocationSize = 2000)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "annex_code")
    private String annexCode;

    @Column(name = "name")
    private String name;

    @Column(name = "post")
    private String post;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "doctors_info")
    private String doctorsInfo;

    @Column(name = "founder")
    private String founder;

    @Column(name = "manager")
    private String manager;

    @Column(name = "datetime1")
    private String datetime1;

    @Column(name = "type")
    private String type;

    @Column(name = "datetime2")
    private String datetime2;

    @Column(name = "beds_departments")
    private String bedsDepartments;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pref_id")
    private Prefecture prefecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Urls urls;
}
