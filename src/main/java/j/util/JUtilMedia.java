package j.util;

import j.core.OS;
import j.core.type.JArray;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.process.ProcessLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 肖炯
 *
 */
public class JUtilMedia {
	private static Map<String, String> properties=new HashMap<>();

	private File source;//文件路径
	private long size;//大小 bytes
	private String format;//格式
	private long duration;//时长ms
	private int bitRate;//比特率
	private String decoder;//解码器
	
	private int videoWidth;//宽
	private int videoHeight;//高
	private double videoFrameRate;//帧率
	private boolean rotate=false;//长宽颠倒
	
	private int audioSamplingRate;//
	private int audioChannels;//声道数


	/**
	 *
	 * @param _source
	 * @param rotate
	 * @throws Exception
	 */
	public JUtilMedia(File _source, boolean rotate) throws Exception{
		if(_source==null||!_source.exists()) throw new Exception("source not exists");
		this.source=_source;
		this.rotate=rotate;
		this.init();
	}

	/**
	 *
	 * @param _source
	 * @throws Exception
	 */
	public JUtilMedia(File _source) throws Exception{
		if(_source==null||!_source.exists()) throw new Exception("source not exists");
		this.source=_source;
		this.init();
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, String value){
		properties.put(key, value);
	}

	public static void setProperties(Map<String, String> _properties){
		properties.putAll(_properties);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
		return properties.get(key);
	}

	/**
	 *
	 */
	private void init(){
		if(source==null) return;

		size=source.length();

		MultimediaInfo info=info(source);

		if(info!=null){
			format=info.getFormat();
			duration=info.getDuration();

			AudioInfo ai=info.getAudio();
			if(ai!=null){
				bitRate=ai.getBitRate();
				decoder=ai.getDecoder();
				audioChannels=ai.getChannels();
				audioSamplingRate=ai.getSamplingRate();
			}

			VideoInfo vi=info.getVideo();
			if(vi!=null){
				bitRate=vi.getBitRate();
				decoder=vi.getDecoder();
				if(this.rotate){
					videoHeight=vi.getSize().getWidth();
					videoWidth=vi.getSize().getHeight();
				}else{
					videoWidth=vi.getSize().getWidth();
					videoHeight=vi.getSize().getHeight();
				}
				videoFrameRate=vi.getFrameRate();
			}
		}
	}

	public File getSource(){
		return this.source;
	}
	public long getSize(){
		return this.size;
	}
	public String getFormat(){
		return this.format;
	}
	public long getDuration(){
		return this.duration;
	}
	public int getBitRate(){
		return this.bitRate;
	}
	public String getDecoder(){
		return this.decoder;
	}
	public int getVideoWidth(){
		return this.videoWidth;
	}
	public int getVideoHeight(){
		return this.videoHeight;
	}
	public double getVideoFrameRate(){
		return this.videoFrameRate;
	}
	public int getAudioSamplingRate(){
		return this.audioSamplingRate;
	}
	public int getAudioChannels(){
		return this.audioChannels;
	}

	/**
	 *
	 * @param savedTo
	 * @return
	 */
	public boolean getFrameImage(File savedTo){
		return getFrameImage(savedTo, 1000L);
	}

	/**
	 *
	 * @param savedTo
	 * @param timeout 等待超时时间（ms），小于登录0表示不等待
	 * @return
	 */
	public boolean getFrameImage(File savedTo, long timeout){
		try{
			String executable=(new MyFFMPEGExecutableLocator()).getExecutablePath();

			List<String> commands = new ArrayList<>();
			commands.add(executable);
			commands.add("-y");
			commands.add("-stats");
			commands.add("-i");
			commands.add("\""+source.getAbsolutePath()+"\"");
			commands.add("-f");
			commands.add("image2");
			commands.add("-ss");
			commands.add("1");//这个参数是设置截取视频多少秒时的画面
			commands.add("-t");
			commands.add("0.001");//记录多长时间（秒）
			commands.add("-s");
			commands.add(this.videoWidth+"x"+this.videoHeight);//宽X高
			commands.add("\""+savedTo.getAbsolutePath()+"\"");

			String command=JUtilFile.adjustFileSeperator(JArray.toString(commands, " "), OS.getOsType());
			//System.out.println("执行 => "+command);

			OS.executeCommand(commands, timeout, false);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	private static MultimediaInfo info(File source){
		try{
			MultimediaObject object = new MultimediaObject(source, new MyFFMPEGExecutableLocator());
			return  object.getInfo();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
  	/**
  	 * 
  	 * @param sourcePath
  	 * @param targetPath
  	 * @throws Exception
  	 */
	public static void changeToMp3(String sourcePath, String targetPath) throws Exception{
		ProcessLocator locator=new MyFFMPEGExecutableLocator();
		MultimediaObject source = new MultimediaObject(new File(sourcePath), locator);

        File target = new File(targetPath);  
        
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        
        attrs.setAudioAttributes(audio);  

        Encoder encoder = new Encoder(locator);
        encoder.encode(source,target,attrs);  
	}

	/**
	 *
	 * @param sourcePath
	 * @param targetPath
	 * @param rotate
	 * @throws Exception
	 */
	public static void changeToMp4(String sourcePath, String targetPath, boolean rotate) throws Exception{
		ProcessLocator locator=new MyFFMPEGExecutableLocator();
		MultimediaObject source = new MultimediaObject(new File(sourcePath), locator);

		//ffmpeg -i  source -vcodec copy -acodec copy dest;

		File target = new File(targetPath);

		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("copy");

		VideoAttributes video = new VideoAttributes();
		video.setCodec("copy");

		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		attrs.setMapMetaData(true);

		Map<String, String> extraContext=new HashMap();
		extraContext.put("flags", "global_header");
		attrs.setExtraContext(extraContext);

		Encoder encoder = new Encoder(locator);

		encoder.encode(source,target,attrs);
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JUtilMedia.setProperty("FFMPEGLocale.linux", "D:\\work\\jshop-20220816\\data\\ffmpeg\\linux\\ffmpeg-5.0-i686\\ffmpeg");
		JUtilMedia.setProperty("FFMPEGLocale.windows", "D:\\work\\jshop-20220816\\data\\ffmpeg\\windows\\ffmpeg.exe");

		File dir = new File("D:\\instraw\\运营\\超级VIP\\03-02");
		File[] files = dir.listFiles();
		for(File file : files){
			if(!file.getName().endsWith(".mp4")) continue;

			String coverName = JUtilString.replaceAll(file.getAbsolutePath(), ".mp4", "_cover.jpg");
			File cover = new File(coverName);

			JUtilMedia media = new JUtilMedia(file,false);
			media.getFrameImage(cover, 2000);
		}
		
		System.exit(0);
    }  
}  

class MyFFMPEGExecutableLocator implements ProcessLocator {
	@Override
	public String getExecutablePath() {
		String os=System.getProperty("os.name");
		if(os==null) os="Windows";
		
		os=os.toLowerCase();
		
		String locale="";
		if(os.indexOf("windows")>-1){
			locale=JUtilMedia.getProperty("FFMPEGLocale.windows");
		}else{
			locale=JUtilMedia.getProperty("FFMPEGLocale.linux");
		}
		
		return JUtilFile.adjustFileSeperator(locale, OS.getOsType());
	}
}
