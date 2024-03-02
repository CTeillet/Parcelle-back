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
	List<Plot> findByAddress_MainDestinationAndDeleted(@NonNull String mainDestination, @NonNull Boolean deleted);

	@Query("""
		SELECT p
		FROM Plot p
		LEFT JOIN FETCH p.city
		LEFT JOIN FETCH p.address
		WHERE p.deleted = false
	""")
	List<Plot> findByDeletedFalse();

	@Transactional
	@Modifying
	@Query("update Plot p set p.deleted = true where p.id in ?1")
	int updateDeletedByIdIn(Collection<String> ids);

	List<Plot> findByAddressIsNull();

	Long countByAddressIsNotNull();


	@Query(nativeQuery = true, value =
			"""
			SELECT array_agg(id) AS intersecting_ids, ST_ConcaveHull(ST_Union(geom), 0.5) AS geometry
			FROM (
				SELECT id, geom, ST_ClusterDBSCAN(geom, 0.000001, 1) OVER () AS cluster_id
				FROM plot
				WHERE deleted = false
			) subquery
			GROUP BY cluster_id, geom
			"""
	)
	List<Object[]> getPlotClustersQuery();

	@Query(nativeQuery = true, value =
			"""
			SELECT array_agg(id) AS intersecting_ids, ST_ConcaveHull(ST_Union(plot.geom), 0.5) AS geometry
			FROM plot
			WHERE id IN ?1
			GROUP BY plot.geom
			"""
	)
	List<Object[]> getPlotCluster(List<String> plotIds);

}
