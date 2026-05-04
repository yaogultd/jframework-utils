package j.util.log;

import org.slf4j.LoggerFactory;

/**
 *
 * @author 肖炯
 *
 */
public class LoggerDefault extends Logger{
	private org.slf4j.Logger log;
	
	/**
	 *
	 * @param clazz
	 */
	public LoggerDefault(Class clazz){
		log=LoggerFactory.getLogger(clazz);
	}

	/**
	 *
	 * @param name
	 */
	public LoggerDefault(String name){
		log=LoggerFactory.getLogger(name);
	}

	/**
	 * 如果level小于0或<=系统设定级别，则输出日志
	 * @param exception
	 * @param level
	 */
	public void log(Exception exception,int level){
		if(level<logLevel&&level>=0) return;

		if(level==LEVEL_DEBUG||level==LEVEL_DEBUG_ADV){
			log.debug(exception.getMessage(), exception);
		}else if(level==LEVEL_INFO){
			log.info(exception.getMessage(), exception);
		}else if(level==LEVEL_WARNING){
			log.warn(exception.getMessage(), exception);
		}else if(level==LEVEL_ERROR){
			log.error(exception.getMessage(), exception);
		}else if(level==LEVEL_FATAL){
			log.error(exception.getMessage(), exception);
		}else{
			log.info(exception.getMessage(), exception);
		}
	}

	/**
	 * 如果level小于0或<=系统设定级别，则输出日志
	 * @param message
	 * @param level
	 */
	public void log(String message,int level){
		if(level<logLevel&&level>=0) return;

		if(level==LEVEL_DEBUG||level==LEVEL_DEBUG_ADV){
			log.debug(message);
		}else if(level==LEVEL_INFO){
			log.info(message);
		}else if(level==LEVEL_WARNING){
			log.warn(message);
		}else if(level==LEVEL_ERROR){
			log.error(message);
		}else if(level==LEVEL_FATAL){
			log.error(message);
		}else{
			log.info(message);
		}
	}
}