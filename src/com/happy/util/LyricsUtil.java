package com.happy.util;

import java.io.File;

import javax.swing.SwingWorker;

import com.happy.common.Constants;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;

/**
 * 歌词处理类
 * 
 * @author zhangliangming
 * 
 */
public class LyricsUtil {

	/**
	 * 加载歌词文件
	 * 
	 * @param context
	 * @param sid
	 * @param title
	 * @param singer
	 * @param lrcUrl
	 */
	public static void loadLyrics(final String sid, String title,
			String singer, final String displayName, String lrcUrl,
			final int type) {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {

				String lrcFilePath = Constants.PATH_LYRICS + File.separator
						+ displayName + ".hrc";

				File lrcFile = new File(lrcFilePath);
				if (!lrcFile.exists()) {

					lrcFilePath = Constants.PATH_LYRICS + File.separator
							+ displayName + ".ksc";

					lrcFile = new File(lrcFilePath);
					if (!lrcFile.exists()) {
						return null;
					}

				}

				SongMessage songMessage = new SongMessage();

				if (type == SongMessage.LRCTYPELRC) {

					songMessage.setType(SongMessage.LRCLOADED);
				} else if (type == SongMessage.LRCTYPEDES) {

					songMessage.setType(SongMessage.DESLOADED);
				} else if (type == SongMessage.LRCTYPELOCK) {

					songMessage.setType(SongMessage.LOCKLOADED);
				}

				songMessage.setLrcFilePath(lrcFilePath);
				songMessage.setSid(sid);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);

				return null;

			}

			@Override
			protected void done() {

			}
		}.execute();
	}
}
