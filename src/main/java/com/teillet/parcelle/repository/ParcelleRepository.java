package com.teillet.parcelle.repository;

import com.teillet.parcelle.dto.ParcelleClusterDto;
import com.teillet.parcelle.model.Parcelle;
import org.geolatte.geom.jts.JTS;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ParcelleRepository extends JpaRepository<Parcelle, String> {

	@Query("""
		SELECT p
		FROM Parcelle p
		LEFT JOIN FETCH p.commune
		LEFT JOIN FETCH p.adresse
		WHERE p.supprime = false

	""")
	List<Parcelle> findBySupprimeFalse();

	@Transactional
	@Modifying
	@Query("update Parcelle p set p.supprime = true where p.id in ?1")
	int updateSupprimeByIdIn(Collection<String> ids);

	List<Parcelle> findByAdresseIsNull();

	Long countByAdresseIsNotNull();


	@Query(nativeQuery = true, value =
			"""
			SELECT array_agg(id) AS intersecting_ids, ST_ConcaveHull(ST_Union(geom), 0.5) AS geometry
			FROM (
				SELECT id, geom, ST_ClusterDBSCAN(geom, 0.000001, 1) OVER () AS cluster_id
				FROM parcelle
				WHERE supprime = false
			) subquery
			GROUP BY cluster_id, geom
			"""
	)
	List<Object[]> getParcelleClustersQuery();

	default List<ParcelleClusterDto> getParcelleClusters() {
		return getParcelleClustersQuery()
				.parallelStream()
				.map(ParcelleRepository::creationParcelleCluster)
				.collect(Collectors.toList());
	}

	private static ParcelleClusterDto creationParcelleCluster(Object[] row) {
		List<String> intersectingIds = Arrays.asList((String[]) row[0]);
		//noinspection unchecked
		Polygon jtsPolygon = JTS.to( (org.geolatte.geom.Polygon<org.geolatte.geom.Position>) row[1]);
		return new ParcelleClusterDto(intersectingIds, jtsPolygon);
	}
}
