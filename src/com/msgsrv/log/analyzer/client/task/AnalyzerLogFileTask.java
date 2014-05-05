/**
 * <b>包名：</b>com.msgsrv.log.analyzer.client.task<br/>
 * <b>文件名：</b>AnalyzerLogFileTask.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-24-上午11:17:49<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.client.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.ASS_KEYS_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.MULTI_KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.NOKEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.SELECTABLE_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.START_STEPS;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.TIME_POINT;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.containsKey;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.getAssKeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.client.core.LogFileComparator;
import com.msgsrv.log.analyzer.client.entity.AnluzerClientConfig;
import com.msgsrv.log.analyzer.client.entity.LogInfo;
import com.msgsrv.log.analyzer.client.entity.MultiKeyConfig;
import com.msgsrv.log.analyzer.client.entity.RuleConfig;
import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.StringUtil;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AlarmEmailService;
import com.msgsrv.log.analyzer.core.AlarmService;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.entity.AnluzerServerConfig;
import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>AnalyzerLogFileTask<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-24 上午11:17:49<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalyzerLogFileTask implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(AnalyzerLogFileTask.class);
	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	private String client;// 名称
	private String filePath = "";// 分析客户端文件路径
	private String filePrefix = "";// 分析文件的格式

	private int counter = 0;// 本次任务分析条数

	private Date lastAnalysisDate = null;// 日志的最后分析时间

	private BufferedReader currentReader = null;// 当前文件的读取器
	private File readFile = null;
	private String upLine = null;

	private int currentCounter = 0;// 当前文件的计数器
	private File currentFile = null;// 当前读取的文件
	private final String FILE_DATE_PATTERN = "yyyyMMdd";// 当前读取的文件
	private final String FILE_PATTERN = ".*\\d{8}\\.\\d+";// 合法的文件命名
	private final long DAYS = 24 * 60 * 60 * 1000;// 当前读取的文件
	private final int MAX_VALUE = 10000000;
	private int NO_FIND_COUNTER = 0;

	private static final Set<String> CHARGE_LIST_SETS = new HashSet<String>();
	static {
		CHARGE_LIST_SETS.add("110");
		CHARGE_LIST_SETS.add("111");
		CHARGE_LIST_SETS.add("112");
		CHARGE_LIST_SETS.add("113");
		CHARGE_LIST_SETS.add("114");
		CHARGE_LIST_SETS.add("115");
		CHARGE_LIST_SETS.add("116");
		CHARGE_LIST_SETS.add("117");
	}

	private FileInputStream currentFileInputStream;

	public AnalyzerLogFileTask(String client, String filePath, String filePrefix) {
		this.client = client;
		this.filePath = filePath;
		this.filePrefix = filePrefix;
	}

	@Override
	public void run() {
		long startTime = System.nanoTime();
		try {
			while (true) {
				// 如果最后的分析时间比下发的时间大，直接返回分析完毕消息
				if (lastAnalysisDate != null && lastAnalysisDate.after(TIME_POINT)) {
					long endTime = System.nanoTime();
					long useTime = (endTime - startTime) / 1000000;
					MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] [" + client + "] 本次分析结束,分析完毕的日志数量["
							+ counter + "] 分析结果 [正常] 当前分析文件[" + currentFile.getName() + "] 最后分析时间 [" + DateUtil.format(lastAnalysisDate) + "]");
					return;
				}
				LogInfo validLog = getValidLog();
				if (validLog == null) {
					long endTime = System.nanoTime();
					long useTime = (endTime - startTime) / 1000000;
					MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] [" + client + "] 本次分析结束,分析完毕的日志数量["
							+ counter + "] 分析结果  [超时] 当前分析文件[" + currentFile.getName() + "] 最后分析时间 [" + DateUtil.format(lastAnalysisDate) + "]");
					return;
				}
				Date logDate = null;
				try {
					logDate = validLog.getLogDate();
				} catch (ParseException e) {
					LOGGER.error(e.getMessage(), e);
				}
				// 对这条有效日志进行分析
				analysisLog(validLog);
				lastAnalysisDate = logDate;//
				// 如果分析完毕，向服务端发送分析完毕
				if (logDate.after(TIME_POINT)) {
					long endTime = System.nanoTime();
					long useTime = (endTime - startTime) / 1000000;
					MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] [" + client + "] 本次分析结束,分析完毕的日志数量["
							+ counter + "] 分析结果 [正常] 当前分析文件[" + currentFile.getName() + "] 最后分析时间 [" + DateUtil.format(lastAnalysisDate) + "]");
					return;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			counter = 0;// 统计完毕后归零
		}
	}

	public LogInfo getValidLog() {
		LogInfo validLog = null;
		initReader();
		try {
			String line = null;
			while ((line = currentReader.readLine()) != null) {
				if (upLine != null && !StringUtil.isEmpty(line.trim()) && !line.matches("\\d{14}\\.\\d{6}.*")) {
					LOGGER.info("将截断的日志信息[" + upLine + "][" + line + "]组合成完整的日志信息");
					line = upLine + line;
					upLine = null;

				}
				if (!StringUtil.isEmpty(line)) {
					upLine = line;
				}
				validLog = isValidLog(line);
				if (validLog != null) {
					break;
				}
			}

			if (line == null) {
				BufferedReader nextReader = nextReader();
				if (nextReader != null) {
					try {
						currentReader.close();
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
					currentReader = nextReader;
					validLog = getValidLog();
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			try {
				if (currentReader != null) {
					currentReader.close();
				}
			} catch (IOException e1) {
			}
			currentReader = null;
			LOGGER.error(e.getMessage(), e);
		}

		return validLog;
	}

	public File nextLogFile() {
		File file = null;
		String _MsgSrvLogFileStr = filePath + "/" + filePrefix;
		if (currentFile == null) {
			if (TIME_POINT != null) {
				String date = DateUtil.format(TIME_POINT, FILE_DATE_PATTERN);
				do {
					String path = String.format(_MsgSrvLogFileStr, date, currentCounter);
					file = new File(path);
					if (file.exists()) {
						NO_FIND_COUNTER = 0;
						currentFile = file;
						LOGGER.info("获取到初次读取文件[" + file.getName() + "]");
						return file;
					} else {
						if (currentCounter == MAX_VALUE) {
							LOGGER.error("系统已经累加到计数器的最大值[" + MAX_VALUE + "]请检查配置或相关程序,系统异常退出");
							System.exit(-1);
						}
						currentCounter++;
					}
				} while (!file.exists());
			} else {
				LOGGER.info("还未设置系统分析时间.................");
			}
		} else {
			String currentFileName = currentFile.getName();
			String date = currentFileName.substring(currentFileName.lastIndexOf("-") + 1, currentFileName.lastIndexOf("."));
			String path = String.format(_MsgSrvLogFileStr, date, currentCounter + 1);
			file = new File(path);
			if (file.exists()) {
				LOGGER.info("获取到下一读取文件[" + file.getName() + "],前一个文件[" + currentFile.getName() + "]");
				currentCounter++;
				try {
					if (SystemConfig.FILE_DELETE_ENABLED) {
						currentFile.delete();
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				NO_FIND_COUNTER = 0;
				currentFile = file;
				return file;
			} else {
				try {
					Date currentFileDate = DateUtil.parse(date, FILE_DATE_PATTERN);
					Date nextDate = new Date(currentFileDate.getTime() + DAYS);
					String nextPath = String.format(_MsgSrvLogFileStr, DateUtil.format(nextDate, FILE_DATE_PATTERN), currentCounter + 1);
					file = new File(nextPath);
					if (file.exists()) {
						LOGGER.info("获取到下一读取文件[" + file.getName() + "],前一个文件[" + currentFile.getName() + "]");
						currentCounter++;
						try {
							if (SystemConfig.FILE_DELETE_ENABLED) {
								currentFile.delete();
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}

						NO_FIND_COUNTER = 0;
						currentFile = file;
						return file;
					} else {
						do {
							Thread.sleep(60 * 1000);
							NO_FIND_COUNTER++;
							// 如果尝试50次还没获取到合适日志文件，则List一次目录，查看是否存在一个计数器比自己大的文件
							File logs = new File(filePath);
							File[] listFiles = logs.listFiles();
							List<File> findFiles = new ArrayList<File>();
							for (int i = 0; i < listFiles.length; i++) {
								File temp = listFiles[i];
								if (temp.getName().matches(FILE_PATTERN)) {
									findFiles.add(temp);
								} else {
									try {
										if (temp.isFile()) {
											LOGGER.info("文件[" + temp.getName() + "]不符合命名规则[删除]");
											temp.delete();
										}
									} catch (Exception e) {

									}
								}
							}
							Collections.sort(findFiles, new LogFileComparator());
							for (File logFile : findFiles) {
								String fileName = logFile.getName();
								int currentFileCounter = Integer.parseInt(fileName.substring(fileName.lastIndexOf(".") + 1));
								if (currentFileCounter > currentCounter) {
									currentCounter = currentFileCounter;// 把计数器设置问当前文件的计数器
									NO_FIND_COUNTER = 0;
									try {
										if (SystemConfig.FILE_DELETE_ENABLED) {
											currentFile.delete();
										}
									} catch (Exception e) {

									}
									LOGGER.info("获取到下一读取文件[" + logFile.getName() + "],前一个文件[" + currentFile.getName() + "]");
									currentFile = logFile;
									return currentFile;
								}

								if (NO_FIND_COUNTER >= 30 && NO_FIND_COUNTER % 30 == 0) {
									String alarmMessage = "当前日志文件[" + currentFile.getName() + "]尝试[" + NO_FIND_COUNTER + "]次未找到下一个合适的读取文件,当前计数器["
											+ currentCounter + "]";
									MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] [" + client + "] " + alarmMessage);
									LOGGER.info(alarmMessage + ",发送报警信息");
									String alarmContent = "[" + client + "] [" + NO_FIND_COUNTER + "分钟] 没有拷贝日志到到线下来进行分析，最拷贝到线下的日志文件[" + currentFile.getName()
											+ "]";
									AlarmService.alarm(alarmContent);// 发送报警信息
									AlarmEmailService.sendEmail(alarmContent);// 发送报警邮件
									return null;
								}
							}
							LOGGER.info("当前日志文件[" + currentFile.getName() + "]尝试[" + NO_FIND_COUNTER + "]次未找到下一个合适的读取文件,当前计数器[" + currentCounter
									+ "]，线程休眠[6s]后尝试获取下一文件");
							String alarmContent = "[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] [" + client + "] 当前日志文件[" + currentFile.getName()
									+ "]尝试[" + NO_FIND_COUNTER + "]次未找到下一个合适的读取文件,当前计数器[" + currentCounter + "]，线程休眠[1分钟]后尝试获取下一文件";
							MONITOR_LOGGER.info(alarmContent);
							if (NO_FIND_COUNTER % 60 == 0) {
								AlarmService.alarm(alarmContent, "1");
							}
						} while (true);
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					System.exit(-1);
				}
			}
		}
		return file;
	}

	public LogInfo isValidLog(String line) {

		LogInfo logInfo = null;
		if (line == null) {
			return null;
		}
		if (line.trim().equals("")) {
			return null;
		}
		if (line.length() < 26) {
			return null;
		}
		if (line.indexOf("|") < 30) {
			return null;
		}
		if (line.indexOf("|") + 1 == line.length()) {
			return null;
		}
		if (line.indexOf(".") != 14) {
			return null;
		}
		if (line.indexOf(":") != 21) {
			return null;
		}
		try {
			String dateStr = line.substring(0, 21);
			String dir = line.substring(22, 25);
			String msgFlow = line.substring(25, line.indexOf("|"));
			String[] msgFlows = msgFlow.split("->");
			if (msgFlows.length != 2) {
				return null;
			}
			String contentStr = line.substring(line.indexOf("|") + 1, line.length() - 1);
			String[] fullcontent = contentStr.split(" +");
			if (msgFlow.startsWith("ProgComp")) {
				msgFlows[0] = fullcontent[0];
				msgFlows[1] = fullcontent[1];
				String[] tempContent = new String[fullcontent.length - 1];
				System.arraycopy(fullcontent, 1, tempContent, 0, tempContent.length);
				fullcontent = tempContent;

			}

			if (fullcontent.length < 3) {
				return null;
			}

//			// 不分析三包并发的逻辑，避免出现时间上的差异
//			if (fullcontent[1].startsWith("[") || fullcontent[2].startsWith("[")) {
//				return null;
//			}
//			// if (fullcontent[2].startsWith("[")) {
//			//
//			// msgFlows[1] = fullcontent[1];
//			// String[] contents = new String[fullcontent.length - 2];
//			// contents[0] = fullcontent[0];
//			// System.arraycopy(fullcontent, 3, contents, 1, contents.length -
//			// 1);
//			// fullcontent = contents;
//			//
//			// } else if (fullcontent[1].startsWith("[")) {
//			// String[] contents = new String[fullcontent.length - 1];
//			// contents[0] = fullcontent[0];
//			// System.arraycopy(fullcontent, 2, contents, 1, contents.length -
//			// 1);
//			// fullcontent = contents;
//			// }

			String src = null;
			String dst = null;
			String cmd = null;

			// 确定是否是程序发送的消息
			if (!"(R)".equals(dir) && !"(S)".equals(dir)) {
				return null;
			}
			logInfo = new LogInfo();

			if ("(R)".equals(dir)) {
				if (msgFlows[0].indexOf(".") < 0) {
					src = msgFlows[0];
				} else {
					src = msgFlows[0].substring(0, msgFlows[0].indexOf("."));
				}
				if (fullcontent[0].indexOf(".") > 0) {
					dst = fullcontent[0].substring(0, fullcontent[0].indexOf("."));
				} else {
					dst = fullcontent[0];
				}
				logInfo.setFullSrc(msgFlows[0]);
				logInfo.setFullDst(fullcontent[0]);
			} else if ("(S)".equals(dir)) {
				if (fullcontent[0].indexOf(".") < 0) {
					src = fullcontent[0];
				} else {
					src = fullcontent[0].substring(0, fullcontent[0].indexOf("."));
				}
				if (msgFlows[1].indexOf(".") > 0) {
					dst = msgFlows[1].substring(0, msgFlows[1].indexOf("."));
				} else {
					dst = msgFlows[1];
				}
				logInfo.setFullSrc(fullcontent[0]);
				logInfo.setFullDst(msgFlows[1]);
			} else {
				src = "";
				dst = "";
			}
			cmd = fullcontent[1];

			// String content = "";
			// for (int i = 0; i < fullcontent.length; i++) {
			// if (i == (fullcontent.length - 1)) {
			// content = content + fullcontent[i];
			// } else {
			// content = content + fullcontent[i] + " ";
			// }
			// }
			logInfo.setDate(dateStr);
			logInfo.setDir(dir);
			logInfo.setSrc(src);
			logInfo.setDst(dst);
			logInfo.setCmd(cmd);
			logInfo.setContent(contentStr);
			logInfo.setContents(fullcontent);
			logInfo.setSrcLogContent(line);
		} catch (Exception e) {
			LOGGER.error("读取出错的日志文件内容：" + line);
			LOGGER.error(e.getMessage(), e);
			logInfo = null;
		}

		return logInfo;
	}

	// 更换日志读取器
	private BufferedReader nextReader() {
		BufferedReader nextReader = null;
		readFile = nextLogFile();
		if (readFile == null) {
			return nextReader;
		}
		try {
			if (currentFileInputStream != null) {
				currentFileInputStream.close();
			}
		} catch (IOException e) {
		}
		try {
			currentFileInputStream = new FileInputStream(readFile);
			InputStreamReader in = new InputStreamReader(currentFileInputStream);
			nextReader = new BufferedReader(in);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return nextReader;
	}

	// 初始化日志读取器
	public void initReader() {
		if (currentReader == null) {
			while ((readFile = nextLogFile()) == null) {
			}
			try {
				FileInputStream fileInputStream = new FileInputStream(readFile);
				InputStreamReader in = new InputStreamReader(fileInputStream);
				currentReader = new BufferedReader(in);
				String str = null;
				long counter = 0;
				while ((str = currentReader.readLine()) != null) {
					upLine = str;
					counter++;
					if ("".equals(str.trim())) {
						continue;
					}
					if (str.length() < 23) {
						continue;
					}
					if (str.indexOf(".") != 14) {
						continue;
					}
					if (str.indexOf(":") != 21) {
						continue;
					}
					String dateStr = str.substring(0, 14);
					try {
						Date date = DateUtil.parse(dateStr);
						if ((date.getTime() >= TIME_POINT.getTime())) {
							break;
						}
					} catch (Exception e) {
						continue;
					}
				}
				if (StringUtil.isEmpty(str)) {
					initReader();
				}
				LOGGER.info("初始化日志读取器完毕，从日志[" + readFile + "]第[" + counter + "]行，内容[" + str + "]开始进行分析");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public StepVo analysisLog(LogInfo log) {
		StepVo vo = null;
		AnluzerClientConfig anluzerClientConfig = AnluzerClientConfig.load();
		List<RuleConfig> rules = anluzerClientConfig.getRules();
		// 和规则进行匹配
		for (RuleConfig rc : rules) {
			if (rc.baseMatch(log)) {
				String identifyValue = "";
				String step = rc.getStep();
				String occTime = log.getDate();
				String sn = "";
				String eigenValue = "";
				List<String> multiKeys = Collections.emptyList();

				boolean exactFlag = true;
				int[] eigenValueIndex = rc.getContent().getEigenvalue().getEigenValueIndex();
				if (rc.getContent() != null && rc.getContent().getContentType().equals("fix-field")) {// fix-field
																										// 报文日志分析
					String[] content = log.getContents();// 发送的内容
					if (eigenValueIndex != null) {
						for (int index : eigenValueIndex) {
							if (StringUtil.isEmpty(eigenValue)) {
								eigenValue = content[index];
							} else {
								eigenValue = eigenValue + "_" + content[index];
							}
						}

					}

					identifyValue = rc.getKey(log);
					exactFlag = rc.getExactFlag(content);
					if (!exactFlag) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("日志匹配规则:" + rc);
							LOGGER.debug("日志信息:" + log + "获取日志是否是精确匹配");
						}
					}
					List<MultiKeyConfig> multiKeyConfigs = rc.getContent().getMultiKeys();
					if (!multiKeyConfigs.isEmpty()) {
						multiKeys = new ArrayList<String>();
						for (MultiKeyConfig mk : multiKeyConfigs) {
							int index = mk.getIndex();
							if (content.length > index) {
								multiKeys.add(content[index]);
							}
						}
					}
					if (CHARGE_LIST_SETS.contains(step)) {
						sn = content[2];
					}
				} else if (rc.getContent() != null && rc.getContent().getContentType().equals("DBSRV")) {// //DBSRV
					String[] content = log.getContent().split(" +", 4);// 发送的内容
					if (content.length >= 4) {
						String match = rc.getContent().getEigenvalue().getMatch();
						if (StringUtil.isNotEmpty(match)) {
							Pattern p = Pattern.compile(match);
							Matcher matcher = p.matcher(content[3].replaceFirst("\"", ""));
							if (matcher.matches()) {
								if (eigenValueIndex != null) {
									for (int index : eigenValueIndex) {
										if (StringUtil.isEmpty(eigenValue)) {
											eigenValue = matcher.group(index);
										} else {
											eigenValue = eigenValue + "_" + matcher.group(index);
										}
									}
								}
							} else {
								continue;
							}
						} else {
							if (eigenValueIndex != null) {
								String[] contents = content[3].split(" +");
								if (contents.length > eigenValueIndex[0]) {
									if (!"0".equals(contents[eigenValueIndex[0]])) {
										eigenValue = contents[eigenValueIndex[0]];
									}
								}
							}
						}
						sn = content[2];
					} else {
						continue;
					}
				}
				if (exactFlag) {
					if (identifyValue.trim().equals("")) {
						identifyValue = "NA";
						exactFlag = false;
					}
					// TODO 将结果放到内存中

					vo = new StepVo(identifyValue, step, occTime, exactFlag, multiKeys, sn, eigenValue);
					vo.setTimeout(rc.getTimeout());
					if ("NA".equals(identifyValue)) {// 如果是非KEY的步骤
						if (multiKeys.isEmpty()) {
							if (StringUtil.isNotEmpty(sn)) {
								SELECTABLE_RESULT_CACHE.add(vo);
							} else {
								List<StepVo> steps = NOKEY_RESULT_CACHE.get(step);
								if (steps == null) {
									steps = new ArrayList<StepVo>();
									steps.add(vo);
									NOKEY_RESULT_CACHE.put(step, steps);
								} else {
									steps.add(vo);
								}
							}

						} else {
							String key = multiKeys.get(0);
							List<StepVo> steps = MULTI_KEY_RESULT_CACHE.get(key);
							if (steps == null) {
								steps = new ArrayList<StepVo>();
								steps.add(vo);
								NOKEY_RESULT_CACHE.put(key, steps);
							} else {
								steps.add(vo);
							}
						}

					} else {
						String[] keys = identifyValue.split(",");// 存在多个KEY的情况采用","分隔
						boolean isContainsStep = false;// 集合中是否包含此步骤
						for (String key : keys) {
							if (KEY_RESULT_CACHE.containsKey(key)) {
								isContainsStep = true;
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("缓存中存在KEY=[" + key + "],步骤：" + vo + "使用此key存储在缓存中");
								}
								KEY_RESULT_CACHE.get(key).add(vo);
								break;
							}
						}
						// 如果在集合中找不到对应的步骤，则默认使用第一个key存储
						if (!isContainsStep) {
							List<StepVo> steps = new ArrayList<StepVo>();
							steps.add(vo);
							KEY_RESULT_CACHE.put(keys[0], steps);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("缓存中没有匹配的KEY,步骤：" + vo + "使用key[" + keys[0] + "]存储在缓存中");
							}
						}
						// 如果存在多个key的情况，构造关联key关系
						if (keys.length > 1) {
							// 设置key之间的关联关系
							setAssociateKyes(keys);
						}

						AnluzerServerConfig config = AnluzerServerConfig.load();// 加载配置文件
						// 如果该步骤属于起始步骤
						if (config.getStartSteps().contains(step)) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("标记起始步骤:" + vo);
							}
							if ("100".equals(vo.getStep())) {
								vo.setAnalyzeTimeout(rc.getTimeout());
							}
							START_STEPS.add(vo);
						}
					}
					counter++;
					break;
				}
			}
		}
		return vo;
	}

	// 设置关联KEY
	public void setAssociateKyes(String[] keys) {
		if (keys.length > 1) {
			for (String key : keys) {
				if (!containsKey(key)) {
					Set<String> associate_keys = new HashSet<String>();
					for (String key2 : keys) {
						if (!key.equals(key2)) {
							associate_keys.add(key2);
						}
					}
					ASS_KEYS_CACHE.put(key, associate_keys);
				}
			}
			Set<String> keySet = new HashSet<String>();
			for (String key : keys) {
				keySet.add(key);
				if (containsKey(key)) {
					keySet.addAll(getAssKeys(key));
				}
			}
			for (String key : keySet) {
				if (containsKey(key)) {
					getAssKeys(key).addAll(keySet);
					getAssKeys(key).remove(key);
				}
			}
		}
	}
}
