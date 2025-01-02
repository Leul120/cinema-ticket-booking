package com.cinema_ticket.repositories;

import com.cinema_ticket.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
