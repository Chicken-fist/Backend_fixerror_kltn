package com.iuh.backendkltn32.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DiemThanhPhan")
public class DiemThanhPhan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maPhieu", nullable = false)
	private PhieuCham phieuCham;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maChuanDauRa", nullable = false)
	private TieuChiChamDiem tieuChiChamDiem;
	
	@Column(name = "diemThanhPhan", nullable = false)
	private String diemThanhPhan;
}
