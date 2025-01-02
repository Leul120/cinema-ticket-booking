package com.cinema_ticket.services.Impl;

import com.cinema_ticket.entities.Image;
import com.cinema_ticket.entities.Movie;
import com.cinema_ticket.repositories.ImageRepository;
import com.cinema_ticket.repositories.MovieRepository;
import com.cinema_ticket.requests.MovieRequest;
import com.cinema_ticket.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ImageRepository imageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository,ImageRepository imageRepository){
        this.movieRepository=movieRepository;
        this.imageRepository=imageRepository;
    }
    @Override
    @Transactional
    public Movie addMovie(MovieRequest movieRequest) {
        try {
            System.out.println("hello");
            Movie movie=new Movie();
            movie.setTitle(movieRequest.getTitle());
            movie.setDescription(movieRequest.getDescription());
            movie.setDuration(movieRequest.getDuration());
            movie.setActors(movieRequest.getActors());
            movie.setGenre(movieRequest.getGenre());
            movie.setYear(movieRequest.getYear());
            movie.setWriter(movieRequest.getWriter());
            movie.setDirector(movieRequest.getDirector());
            movie.setCountry(movie.getCountry());
            movie.setSubtitle_availability(movieRequest.getSubtitle_availability());
            Image poster=new Image();
            poster.setImageUrl(movieRequest.getPoster().getImageUrl());
            poster.setPublicId(movieRequest.getPoster().getPublicId());
            Image savedPoster=imageRepository.save(poster);
            movie.setPoster(savedPoster);
            movie.setLanguage(movieRequest.getLanguage());
            List<Image> imageList=new ArrayList<>();
            for(Image image:movieRequest.getImages()){
                Image img=new Image();
                img.setPublicId(image.getPublicId());
                img.setImageUrl(image.getImageUrl());
                Image savedImage=imageRepository.save(img);
                imageList.add(savedImage);
            }
            System.out.println(movie.getId());
            movie.setImages(imageList);
            movie.setRelease_date(movieRequest.getRelease_date());
            movie.setCreated_at(new Date());
            movie.setUpdated_at(new Date());
            movie= movieRepository.save(movie);
            messagingTemplate.convertAndSend("/topic/update-movie", "update");
            return movie;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Override
    @Transactional
    public Movie updateMovie(Long id,MovieRequest movieRequest){
        Movie movie=movieRepository.findById(id).map(existingMovie->updateExistingMovie(existingMovie,movieRequest)).map(movieRepository::save).orElseThrow(()->new RuntimeException("Movie not found!"));
        messagingTemplate.convertAndSend("/topic/update-movie", "update");
        return movie;
    }

    private Movie updateExistingMovie(Movie movie, MovieRequest movieRequest) {
        try {
//            Movie movie=movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not found!"));
            movie.setTitle(movieRequest.getTitle());
            movie.setDescription(movieRequest.getDescription());
            movie.setDuration(movieRequest.getDuration());
            movie.setActors(movieRequest.getActors());
            movie.setGenre(movieRequest.getGenre());
            movie.setYear(movieRequest.getYear());
            movie.setWriter(movieRequest.getWriter());
            Image poster=new Image();
            poster.setImageUrl(movieRequest.getPoster().getImageUrl());
            poster.setPublicId(movieRequest.getPoster().getPublicId());
            Image savedPoster=imageRepository.save(poster);
            movie.setPoster(savedPoster);
            movie.setLanguage(movieRequest.getLanguage());
            List<Image> imageList=new ArrayList<>();
            for(Image image:movieRequest.getImages()){
                Image img=new Image();
                img.setPublicId(image.getPublicId());
                img.setImageUrl(image.getImageUrl());
                Image savedImage=imageRepository.save(img);
                imageList.add(savedImage);
            }
            movie.setImages(imageList);
            movie.setRelease_date(movieRequest.getRelease_date());
            movie.setCreated_at(new Date());
            movie.setUpdated_at(new Date());

            return movie;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMovie(Long id) {
        try {
            movieRepository.deleteById(id);
            messagingTemplate.convertAndSend("/topic/update-movie", "update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        try {
            return movieRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Movie getMovieDetails(Long id) {
        try {
            return movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not found!"));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
