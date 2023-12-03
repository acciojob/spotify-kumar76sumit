package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album=new Album(title);
        albums.add(album);
        Artist key=new Artist();
        for(Artist artist:artists)
        {
            if(artist.getName().equals(artistName))
            {
                key=artist;
            }
        }
        artistAlbumMap.put(key,albums);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song=new Song(title,length);
        songs.add(song);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user=null;
        for(User user1:users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }
        Playlist playlist=new Playlist(title);
        creatorPlaylistMap.put(user,playlist);

        playlistListenerMap.putIfAbsent(playlist,new ArrayList<User>());
        List<User> listOfListener=playlistListenerMap.get(playlist);
        listOfListener.add(user);
        playlistListenerMap.put(playlist,listOfListener);

        playlistSongMap.putIfAbsent(playlist,new ArrayList<Song>());
        List<Song> listWithSameLength=playlistSongMap.get(playlist);
        for(Song song:songs)
        {
            if(song.getLength()==length)
            {
                listWithSameLength.add(song);
            }
        }
        playlistSongMap.put(playlist,listWithSameLength);

        userPlaylistMap.putIfAbsent(user,new ArrayList<Playlist>());
        List<Playlist> listOfPlayLists=userPlaylistMap.get(user);
        listOfPlayLists.add(playlist);
        userPlaylistMap.put(user,listOfPlayLists);

        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user=null;
        for(User user1:users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }
        Playlist playlist=new Playlist(title);
        creatorPlaylistMap.put(user,playlist);

        playlistListenerMap.putIfAbsent(playlist,new ArrayList<User>());
        List<User> listOfListener=playlistListenerMap.get(playlist);
        listOfListener.add(user);
        playlistListenerMap.put(playlist,listOfListener);

        playlistSongMap.putIfAbsent(playlist,new ArrayList<Song>());
        List<Song> listWithGivenSongTitles=playlistSongMap.get(playlist);
        for(Song song:songs)
        {
            for(String songTitle:songTitles)
            {
                if(song.getTitle().equals(songTitle))
                {
                    listWithGivenSongTitles.add(song);
                }
            }
        }
        playlistSongMap.put(playlist,listWithGivenSongTitles);

        userPlaylistMap.putIfAbsent(user,new ArrayList<Playlist>());
        List<Playlist> listOfPlayLists=userPlaylistMap.get(user);
        listOfPlayLists.add(playlist);
        userPlaylistMap.put(user,listOfPlayLists);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user=null;
        for(User user1:users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }

        Playlist playlist=null;
        for(Playlist playlist1: playlists)
        {
            if(playlist1.getTitle().equals(playlistTitle))
            {
                playlist=playlist1;
            }
        }

        boolean isAlreadyListener=false;
        List<User> listOfListeners=playlistListenerMap.get(playlist);
        for(User user1:listOfListeners)
        {
            if(user1==user)
            {
                isAlreadyListener=true;
            }
        }
        if(!isAlreadyListener)
        {
            listOfListeners.add(user);
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song song=new Song();
        for(Song song1:songs)
        {
            if(song1.getTitle().equals(songTitle))
            {
                song=song1;
            }
        }

        User user=new User();
        for(User user1:users)
        {
            if(user1.getMobile().equals(mobile))
            {
                user=user1;
            }
        }
        songLikeMap.putIfAbsent(song,new ArrayList<User>());
        List<User> getLikeList=songLikeMap.get(song);

        boolean isUserLiked=false;
        for(User user1:getLikeList)
        {
            if(user1==user)
            {
                isUserLiked=true;
            }
        }

        Album album=null;
        for(Album key:albumSongMap.keySet())
        {
            for(Song song1:albumSongMap.get(key))
            {
                if(song1.getTitle().equals(songTitle))
                {
                    album=key;
                }
            }
        }

        Artist artist=null;
        for(Artist key:artistAlbumMap.keySet())
        {
            for(Album album1:artistAlbumMap.get(key))
            {
                if(album!=null && album1==album)
                {
                    artist=key;
                }
            }
        }

        if(!isUserLiked)
        {
            song.setLikes(song.getLikes()+1);
            getLikeList.add(user);
            if(artist!=null)
            {
                artist.setLikes(artist.getLikes()+1);
            }
        }
        songLikeMap.put(song,getLikeList);
        return song;
    }

    public String mostPopularArtist() {
        int maxLikes=0;
        String mostPopularArtistName="";
        for(Artist artist:artists)
        {
            if(artist.getLikes()>maxLikes)
            {
                mostPopularArtistName=artist.getName();
                maxLikes= artist.getLikes();
            }
        }
        return mostPopularArtistName;
    }

    public String mostPopularSong() {
        int maxLikes=0;
        String mostPopularSongName="";
        for(Song song:songs)
        {
            if(song.getLikes()>maxLikes)
            {
                mostPopularSongName=song.getTitle();
                maxLikes= song.getLikes();
            }
        }
        return mostPopularSongName;
    }
}
