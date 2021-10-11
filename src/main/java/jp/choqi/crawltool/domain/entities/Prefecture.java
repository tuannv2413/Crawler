package jp.choqi.crawltool.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "prefecture")
public class Prefecture implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_kana")
    private String nameKana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "prefecture", cascade = CascadeType.ALL)
    private List<AdAddress> adAddresses;

    @OneToMany(mappedBy = "prefecture", cascade = CascadeType.ALL)
    private List<MedicalInstitution> medicalInstitutions;
}
