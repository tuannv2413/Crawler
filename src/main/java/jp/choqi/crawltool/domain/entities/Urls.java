package jp.choqi.crawltool.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "urls")
public class Urls implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "urls", cascade = CascadeType.ALL)
    private List<MedicalInstitution> medicalInstitutions;
}
