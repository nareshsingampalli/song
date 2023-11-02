package com.example.song.service;

import java.util.*;
import com.example.song.model.SongRowMapper;
import com.example.song.model.Song;
import com.example.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class SongH2Service implements SongRepository {

	@Autowired
	private JdbcTemplate db;

	@Override
	public ArrayList<Song> getSongs() {
		List<Song> songList = db.query("select * from playlist", new SongRowMapper());

		return new ArrayList<>(songList);
	}

	@Override
	public Song getSongById(int songId) {
		try {
			return db.queryForObject("select * from playlist where songid = ?", new SongRowMapper(), songId);
		} catch (Exception E) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Song addSong(Song song) {
		db.update("insert into playlist(songname,lyricist,singer,musicdirector) values(?,?,?,?)", song.getSongName(),
				song.getLyricist(), song.getSinger(), song.getMusicDirector());
		return db.queryForObject(
				"select * from playlist where songname = ? and lyricist= ? and singer = ? and musicdirector = ?",
				new SongRowMapper(), song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
	}

	@Override
	public Song updateSong(Song song, int songId) {
		if (song.getSongName() != null)
			db.update("update playlist set songName = ? where songid = ?", song.getSongName(), songId);
		if (song.getLyricist() != null)
			db.update("update playlist set lyricist = ? where songid= ?", song.getLyricist(), songId);
		if (song.getSinger() != null)
			db.update("update playlist set singer =? where songid = ?", song.getSinger(), songId);
		if (song.getMusicDirector() != null)
			db.update("update playlist set musicdirector = ? where songid = ?", song.getMusicDirector(), songId);
		return getSongById(songId);
	}

	@Override
	public void deleteSong(int songId) {
		db.update("delete from playlist where songid = ?", songId);
	}

}