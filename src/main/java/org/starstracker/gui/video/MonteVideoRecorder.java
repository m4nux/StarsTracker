package org.starstracker.gui.video;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.VideoFormatKeys.DataClassKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_MJPG;
import static org.monte.media.VideoFormatKeys.HeightKey;
import static org.monte.media.VideoFormatKeys.WidthKey;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.monte.media.Buffer;
import org.monte.media.Format;
import org.monte.media.MovieWriter;
import org.monte.media.Registry;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;


public class MonteVideoRecorder {
	private MovieWriter out;
	private int track;
	private Buffer buf;

	public void start() {

		try {
			out = Registry.getInstance().getWriter(new File("/home/manux/monte.avi"));
			Format format =  new
					Format(MediaTypeKey, MediaType.VIDEO,
							EncodingKey,ENCODING_AVI_MJPG,
							FrameRateKey,
							new Rational(30, 1),
									//
							WidthKey, 640,
									                 //
							HeightKey, 480,
									                                   //
							DepthKey,24);
			 buf = new Buffer();
			buf.format = new Format(DataClassKey, BufferedImage.class);
			buf.sampleDuration = format.get(FrameRateKey).inverse();

			track = out.addTrack(format);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	public void record(BufferedImage image) {
		
		try {
			buf.data=image;
			out.write(track, buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}