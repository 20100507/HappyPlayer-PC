package com.happy.util;

import java.io.File;
import java.util.Date;

import com.happy.lyrics.utils.FileUtils;
import com.happy.lyrics.utils.TimeUtils;
import com.happy.model.SongInfo;
import com.tulskiy.musique.audio.AudioFileReader;
import com.tulskiy.musique.model.Track;
import com.tulskiy.musique.model.TrackData;
import com.tulskiy.musique.system.TrackIO;
import com.tulskiy.musique.util.AudioMath;

public class MediaUtils {
	/**
	 * 通过文件获取mp3的相关数据信息
	 * 
	 * @param filePath
	 * @return
	 */

	public static SongInfo getSongInfoByFile(String filePath) {
		File sourceFile = new File(filePath);
		if (!sourceFile.exists())
			return null;

		if (sourceFile.length() < 1024 * 1024) {
			return null;
		}

		SongInfo songInfo = null;
		try {

			AudioFileReader audioFileReader = TrackIO
					.getAudioFileReader(sourceFile.getName());
			if (audioFileReader == null
					|| audioFileReader.read(sourceFile) == null
					|| audioFileReader.read(sourceFile).getTrackData() == null) {
				return null;
			}
			Track track = audioFileReader.read(sourceFile);
			TrackData trackData = track.getTrackData();

			double totalMS = AudioMath.samplesToMillis(
					trackData.getTotalSamples(), trackData.getSampleRate());
			long duration = Math.round(totalMS);

			String durationStr = TimeUtils.parseString((int) duration);

			songInfo = new SongInfo();

			// 文件名
			String displayName = FileUtils.removeExt(sourceFile.getName());
			String artist = trackData.getArtist();
			String title = trackData.getTitle();
			if (title == null || !title.contains("-")) {

				if (displayName.contains("-")) {
					String[] titleArr = displayName.split("-");
					artist = titleArr[0].trim();
					title = titleArr[1].trim();
				} else {
					artist = "";
					title = displayName;
				}
			} else {
				String[] titleArr = title.split("-");
				artist = titleArr[0].trim();
				title = titleArr[1].trim();
			}

			if (StringUtils.isMessyCode(artist)) {

				if (displayName.contains("-")) {
					String[] titleArr = displayName.split("-");
					artist = titleArr[0].trim();
				} else
					artist = "";
			}

			if (StringUtils.isMessyCode(title)) {

				if (displayName.contains("-")) {
					String[] titleArr = displayName.split("-");
					title = titleArr[1].trim();
				} else
					title = "";
			}

			displayName = artist + " - " + title;

			songInfo.setSid(IDGenerate.getId("SI-"));
			songInfo.setDisplayName(displayName);
			songInfo.setSinger(artist);
			songInfo.setTitle(title);
			songInfo.setDuration(duration);
			songInfo.setDurationStr(durationStr);
			songInfo.setSize(sourceFile.length());
			songInfo.setSizeStr(FileUtils.getFileSize(sourceFile.length()));
			songInfo.setFilePath(filePath);
			songInfo.setType(SongInfo.LOCALSONG);

			songInfo.setCreateTime(DateUtil.dateToString(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return songInfo;

	}
}
