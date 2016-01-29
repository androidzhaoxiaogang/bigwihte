package com.xst.bigwhite.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>{
	Optional<Picture> findTop1ByImageurl(String imageurl);
}
