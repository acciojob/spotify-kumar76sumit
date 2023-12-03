package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        return spotifyRepository.createUser(name,mobile);
    }

    public Artist createArtist(String name) {
        return spotifyRepository.createArtist(name);
    }

    public Album createAlbum(String title, String artistName) {
        boolean isArtistPresent=false;
        for(Artist artist:spotifyRepository.artists)
        {
            if(artist.getName().equals(artistName))
            {
                isArtistPresent=true;
            }
        }
        if(!isArtistPresent)
        {
            createArtist(artistName);
        }
        return spotifyRepository.createAlbum(title,artistName);
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        boolean isAlbumPresent=false;
        for(Album album:spotifyRepository.albums)
        {
            if(album.getTitle().equals(albumName))
            {
                isAlbumPresent=true;
            }
        }
        if(!isAlbumPresent)
        {
            throw new Exception("Album does not exist");
        }
        return spotifyRepository.createSong(title,albumName,length);
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user=null;
        for(User user1: spotifyRepository.users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }
        if(user==null)
        {
            throw new Exception("User does not exist");
        }
        return spotifyRepository.createPlaylistOnLength(mobile,title,length);
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return spotifyRepository.createPlaylistOnName(mobile,title,songTitles);
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user=null;
        for(User user1:spotifyRepository.users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }
        if(user==null)
        {
            throw new Exception("User does not exist");
        }

        Playlist playlist=null;
        for(Playlist playlist1: spotifyRepository.playlists)
        {
            if(playlist1.getTitle().equals(playlistTitle))
            {
                playlist=playlist1;
            }
        }
        if(playlist==null)
        {
            throw new Exception("Playlist does not exist");
        }
        return spotifyRepository.findPlaylist(mobile,playlistTitle);
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean isUserPresent=false;
        for(User user: spotifyRepository.users)
        {
            if(user.getMobile().equals(mobile))
            {
                isUserPresent=true;
            }
        }
        if(!isUserPresent)
        {
            throw new Exception("User does not exist");
        }

        boolean isSongPresent=false;
        for(Song song: spotifyRepository.songs)
        {
            if(song.getTitle().equals(songTitle))
            {
                isSongPresent=true;
            }
        }
        if(!isSongPresent)
        {
            throw new Exception("Song does not exist");
        }
        return spotifyRepository.likeSong(mobile,songTitle);
    }

    public String mostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}
