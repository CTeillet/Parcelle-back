package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository  extends JpaRepository<Block, Long> {
}
