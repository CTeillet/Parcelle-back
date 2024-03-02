package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlotRepository extends JpaRepository<Plot, String> {
	List<Plot> findByAdresse_DestinationPrincipaleAndSupprime(@NonNull String destinationPrincipale, @NonNull Boolean supprime);

	@Query("""
		SELECT p
		FROM Plot p
		LEFT JOIN FETCH p.commune
		LEFT JOIN FETCH p.adresse
		WHERE p.supprime = false
	""")
	List<Plot> findBySupprimeFalse();

	@Transactional
	@Modifying
	@Query("update Plot p set p.supprime = true where p.id in ?1")
	int updateSupprimeByIdIn(Collection<String> ids);

	List<Plot> findByAdresseIsNull();

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

	@Query(nativeQuery = true, value =
			"""
			SELECT array_agg(id) AS intersecting_ids, ST_ConcaveHull(ST_Union(parcelle.geom), 0.5) AS geometry
			FROM parcelle
			WHERE id IN ?1
			GROUP BY parcelle.geom
			"""
	)
	List<Object[]> getPlotCluster(List<String> plotIds);

}
