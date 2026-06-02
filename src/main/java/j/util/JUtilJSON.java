package j.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONParserConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 */
public class JUtilJSON{
	/**
	 *
	 * @param s
	 * @return
	 */
	public static Object isJson(String s){
		return isJson(s, false);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static Object isJson(String s, boolean toFix){
		if(toFix) s = fixJsonUnescapedNewlines(s);
		if(JUtilString.isBlank(s)) return null;
		s = s.trim();

		JSONObject object;
		try{
			object = new JSONObject(s, (new JSONParserConfiguration()).withStrictMode(true));
		}catch (Exception e){
			object = null;
		}


		JSONArray array;
		try{
			array = new JSONArray(s, (new JSONParserConfiguration()).withStrictMode(true));
		}catch (Exception e){
			array = null;
		}

		if(object==null && array==null) return null;

		if("{}".equals(s)){
			return new JSONObject(s);
		}

		if("[]".equals(s)){
			return new JSONArray(s);
		}

		s=s.trim();

		if(s.startsWith("{") && s.endsWith("}")){
			JSONObject json=parse(s);
			return json==null || !json.keys().hasNext()?null:json;
		}else if(s.startsWith("[") && s.endsWith("]")){
			JSONArray arr=array(s);
			return arr==null || arr.length()==0?null:arr;
		}
		return null;
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static JSONObject parse(String s){
		return parse(s, true);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static JSONObject parse(String s, boolean toFix){
		try{
			if(toFix) s = fixJsonUnescapedNewlines(s);
			if(JUtilString.isBlank(s)) s="{}";
			return new JSONObject(s, (new JSONParserConfiguration()).withStrictMode(false));
		}catch(Exception e){
			//throw new JSONException("Invalid JSON string");
			return new JSONObject("{}");
		}
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static JSONArray array(String s){
		return array(s, true);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static JSONArray array(String s, boolean toFix){
		try{
			if(toFix) s = fixJsonUnescapedNewlines(s);
			if(JUtilString.isBlank(s)) s="[]";
			return new JSONArray(s);
		}catch(Exception e){
			e.printStackTrace();
			//throw new JSONException("Invalid JSON string");
			return new JSONArray("[]");
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static String string(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String s="";
			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)) s=obj.toString();
			else if("java.lang.Integer".equals(cls)) s=obj.toString();
			else if("java.lang.Long".equals(cls)) s=obj.toString();
			else if("java.lang.Double".equals(cls)) s=JUtilMath.formatPrintPrecisionNoChange((Double)obj, (Double)obj, 0);
			else if("java.math.BigDecimal".equals(cls)) s=JUtilMath.formatPrintPrecisionNoChange(((BigDecimal)obj).doubleValue(), ((BigDecimal)obj).doubleValue(), 0);

			else s=obj.toString();

			if(s!=null&&s.startsWith("jis:")) s=JUtilString.intSequence2String(s);
			return s;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Double getDouble(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isNumber(s)) return Double.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return ((Integer)obj).doubleValue();
			}else if("java.lang.Long".equals(cls)){
				return ((Long)obj).doubleValue();
			}else if("java.lang.Double".equals(cls)){
				return (Double)obj;
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).doubleValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Integer getInteger(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isInt(s)) return Integer.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return (Integer)obj;
			}else if("java.lang.Long".equals(cls)){
				return ((Long)obj).intValue();
			}else if("java.lang.Double".equals(cls)){
				return ((Double)obj).intValue();
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).intValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Long getLong(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isLong(s)) return Long.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return ((Integer)obj).longValue();
			}else if("java.lang.Long".equals(cls)){
				return (Long)obj;
			}else if("java.lang.Double".equals(cls)){
				return ((Double)obj).longValue();
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).longValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Boolean getBoolean(JSONObject js,String key){
		try{
			return js.getBoolean(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static JSONArray array(JSONObject js,String key){
		try{
			return js.getJSONArray(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	public static JSONObject get(JSONArray array,int index){
		try{
			return array.getJSONObject(index);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object getObject(JSONArray array,int index){
		try{
			return array.get(index);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static JSONObject object(JSONObject js,String key){
		try{
			return js.getJSONObject(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Object get(JSONObject js,String key){
		try{
			return js.get(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String format(String s){
		return "jis:"+JUtilString.string2IntSequence(s);
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String encode(String s){
		if(s==null||"".equals(s)) return s;
		return JUtilString.encodeURI(s, "UTF-8");
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String convert(String s){
		if(s==null||"".equals(s)) return "";

		s=JUtilString.replaceAll(s, "\\", "\\\\");
		s=JUtilString.replaceAll(s, "\"", "\\\"");
		s=JUtilString.replaceAll(s, "\b", "\\b");
		s=JUtilString.replaceAll(s, "\f", "\\f");
		s=JUtilString.replaceAll(s, "\n", "\\n");
		s=JUtilString.replaceAll(s, "\r", "\\r");
		s=JUtilString.replaceAll(s, "\t", "\\t");

		return s;
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String convertChars(String s){
		if(s==null||"".equals(s)) return "";

		s=JUtilString.replaceAll(s, "\\", "\\\\");
		s=JUtilString.replaceAll(s, "\"", "\\\"");
		s=JUtilString.replaceAll(s, "\b", "\\b");
		s=JUtilString.replaceAll(s, "\f", "\\f");
		s=JUtilString.replaceAll(s, "\n", "\\n");
		s=JUtilString.replaceAll(s, "\r", "\\r");
		s=JUtilString.replaceAll(s, "\t", "\\t");

		return s;
	}

	/**
	 *
	 * @param cls
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Class cls, JSONObject json){
		try{
			return JUtilBean.json2Bean(cls, json);
		}catch (Exception ignored){
			//ignored.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param object
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Object object, JSONObject json){
		try{
			return JUtilBean.json2Bean(object, json);
		}catch (Exception ignored){
			return null;
		}
	}

	/**
	 *
	 * @param cls
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Class cls, String json){
		try{
			return JUtilBean.json2Bean(cls, parse(json));
		}catch (Exception ignored){
			//ignored.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param object
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Object object, String json){
		try{
			return JUtilBean.json2Bean(object, parse(json));
		}catch (Exception ignored){
			return null;
		}
	}

	/**
	 *
	 * @param cls
	 * @param array
	 * @return
	 */
	public static List json2Beans(Class cls, JSONArray array){
		List objects = new ArrayList<>();
		if(array == null) return objects;

		String clsName = cls.getCanonicalName();
		for(int i=0; i<array.length(); i++){
			if("java.lang.String".equals(clsName)){
				objects.add(array.getString(i));
			}else if("java.lang.Integer".equals(clsName) || "int".equals(clsName)){
				objects.add(array.getInt(i));
			}else if("java.lang.Long".equals(clsName) || "long".equals(clsName)){
				objects.add(array.getLong(i));
			}else if("java.lang.Double".equals(clsName) || "double".equals(clsName)){
				objects.add(array.getDouble(i));
			}else if("java.lang.Float".equals(clsName) || "float".equals(clsName)){
				objects.add(array.getFloat(i));
			}else if("java.lang.Short".equals(clsName) || "short".equals(clsName)){
				objects.add((short)array.getInt(i));
			}else if("java.lang.Boolean".equals(clsName) || "boolean".equals(clsName)){
				objects.add(array.getBoolean(i));
			}else{
				objects.add(json2Bean(cls, get(array, i)));
			}
		}
		return objects;
	}

	/**
	 *
	 * @param cls
	 * @param jsonArrayString
	 * @return
	 */
	public static List json2Beans(Class cls, String jsonArrayString){
		return json2Beans(cls, array(jsonArrayString));
	}

	/**
	 *
	 * @param json
	 * @return
	 */
	public static String fixJsonUnescapedNewlines(String json) {
		if (json == null || json.isEmpty()) return json;

		StringBuilder out = new StringBuilder(json.length() + 16);
		boolean inString = false;
		boolean escaping = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (!inString) {
				if (c == '"') inString = true;
				out.append(c);
				continue;
			}

			// inString == true
			if (escaping) {
				out.append(c);
				escaping = false;
				continue;
			}

			if (c == '\\') {
				out.append(c);
				escaping = true;
				continue;
			}

			if (c == '"') {
				out.append(c);
				inString = false;
				continue;
			}

			if (c == '\n') {
				out.append("\\n");
				continue;
			}
			if (c == '\r') {
				out.append("\\r");
				continue;
			}

			out.append(c);
		}

		String sOut = out.toString();
		sOut = JUtilString.replaceAll(sOut, "\\R", " \\\\ R");
		sOut = JUtilString.replaceAll(sOut, "\\N", " \\\\ N");
		return sOut;
	}

	public static void main(String[] args) throws Exception {
		String s="[ { \"chapterUuid\": \"ch-001-1\", \"howto\": \"研究具身智能机器人的发展历程、当前技术成熟度、社会对私人陪伴机器人的潜在需求（如老龄化、独居人口增长、消费升级），以及政策推动因素。搜索关键词：具身智能发展背景、私人陪伴机器人需求、人形机器人市场驱动力。\" }, { \"chapterUuid\": \"ch-001-2\", \"howto\": \"明确研究的时间窗口（2024-2029），界定私人贴身伙伴的定义、覆盖的功能范围（逛街、散步、跑步、代购），设定可行性分析的评价标准（技术/经济/社会可行性）。搜索关键词：具身智能机器人时间规划、私人伙伴机器人功能定义、可行性分析框架。\" }, { \"chapterUuid\": \"ch-001-3\", \"howto\": \"确定研究方法（文献综述、案例研究、专家访谈、技术路线图分析），列出数据来源（公司财报、行业报告、学术论文、专利数据库、政府白皮书）。搜索关键词：机器人行业研究方法、数据来源、技术趋势分析工具。\" }, { \"chapterUuid\": \"ch-002-1\", \"howto\": \"调研高收入城市家庭、老年人、行动不便者等群体的特征、支付意愿、对陪伴/辅助功能的具体需求，分析不同用户群的痛点与期望。搜索关键词：目标用户画像、老年人陪伴需求、高收入家庭消费习惯、行动不便者辅助需求。\" }, { \"chapterUuid\": \"ch-002-2\", \"howto\": \"深度拆解逛街、散步、跑步跟随、便利店代购四个场景的环境复杂度、动态障碍物、用户交互模式、任务失败容忍度，输出场景需求规格。搜索关键词：逛街场景机器人需求、跑步跟随技术挑战、便利店自主购物难点、非结构化环境任务分析。\" }, { \"chapterUuid\": \"ch-002-3\", \"howto\": \"归纳用户对安全（防碰撞、跌落）、续航（覆盖场景时长）、交互自然度（语音、手势、意图理解）、性价比（可接受价位）等关键指标的具体要求，设定量化门槛。搜索关键词：机器人用户体验指标、安全性标准、续航需求、自然交互评价方法、消费者可接受价格。\" }, { \"chapterUuid\": \"ch-002-4\", \"howto\": \"对比宠物（陪伴/跟随能力）、随行人员（灵活性/成本）、自动驾驶配送车（代购功能）的优缺点，分析现有方案在成本、可靠性、情感连接上的不足，突出机器人的差异化价值。搜索关键词：宠物陪伴机器人对比、随行人员服务成本、自动驾驶配送车局限性、替代方案竞争分析。\" }, { \"chapterUuid\": \"ch-003-1\", \"howto\": \"调研当前双足/轮式机器人的动态平衡算法（如MPC、WBC）、移动速度与越障能力、灵巧手抓取自由度与传感、本体轻量化材料（碳纤维、镁合金）进展，列出代表性硬件参数。搜索关键词：双足机器人动态平衡、灵巧手最新进展、轻量化材料机器人、波士顿动力Atlas硬件参数。\" }, { \"chapterUuid\": \"ch-003-2\", \"howto\": \"研究视觉-语言-动作大模型（如RT-2、PaLM-E、VLA）的泛化能力、仿真平台（Isaac Sim、MuJoCo、SAPIEN）的训练方法、Sim-to-Real迁移效果，评估当前AI在开放世界任务中的表现。搜索关键词：视觉语言动作大模型、仿真训练平台、Sim-to-Real迁移、具身智能泛化能力。\" }, { \"chapterUuid\": \"ch-003-3\", \"howto\": \"梳理特斯拉Optimus、Figure AI 02、宇树H1、小米CyberOne、波士顿动力Atlas等产品的发布时间、技术指标（自由度、负载、续航）、量产计划与定价，对比各玩家战略定位。搜索关键词：特斯拉Optimus进展、Figure AI人形机器人、宇树科技人形机器人、小米CyberOne、波士顿动力商业化。\" }, { \"chapterUuid\": \"ch-003-4\", \"howto\": \"调查具身机器人在物流（亚马逊Digit）、仓储（Ocado）、家庭原型测试（波士顿动力Stretch）等已落地案例中的性能、故障率、用户反馈，评估商业化成熟度。搜索关键词：具身机器人物流应用、仓储机器人落地案例、家庭服务机器人原型测试、商业化进度报告。\" }, { \"chapterUuid\": \"ch-004-1\", \"howto\": \"研究动态非结构化环境中的SLAM算法鲁棒性（应对人流、光照变化）、多模态融合（视觉+激光+IMU）方案、密集人群避让策略（社会力模型、强化学习），识别当前瓶颈。搜索关键词：非结构化环境SLAM、多模态融合导航、密集人群避让算法、机器人自主导航鲁棒性。\" }, { \"chapterUuid\": \"ch-004-2\", \"howto\": \"调研通用抓取中的商品识别（透明/反光物体、柔性包装）、货架密集场景下的避碰与路径规划、灵巧手对异形物体的适应能力，分析成功率与抓取效率。搜索关键词：透明物体识别、通用抓取灵巧手、货架场景机器人抓取、异形物体抓取技术。\" }, { \"chapterUuid\": \"ch-004-3\", \"howto\": \"研究人机交互中的意图理解（上下文推理、非语言信号）、安全社交距离（人体力场、避免惊吓）、嘈杂环境下的语音识别（降噪、远场唤醒），评估当前交互自然度缺陷。搜索关键词：人机自然交互意图理解、社交导航安全距离、嘈杂环境语音识别、具身机器人社交技能。\" }, { \"chapterUuid\": \"ch-004-4\", \"howto\": \"分析当前电池能量密度（~300Wh/kg）与未来固态电池（~500Wh/kg）进展、机器人重量与续航之间的trade-off、轻量化材料（镁锂合金、复合材料）应用，给出续航提升路径。搜索关键词：固态电池进展、机器人能量密度、续航重量折中、轻量化材料机器人、人形机器人电池续航。\" }, { \"chapterUuid\": \"ch-004-5\", \"howto\": \"研究硬件故障模式（关节磨损、传感器失效）、网络延迟下的控制策略、远程干预保障机制（断线重连、紧急制动、备份控制器），总结工业与消费级可靠性的差异。搜索关键词：机器人系统可靠性、硬件故障容错机制、远程干预方案、网络延迟影响、安全冗余设计。\" }, { \"chapterUuid\": \"ch-005-1\", \"howto\": \"评估2025-2026年散步/慢速跟随场景的技术可行性：开放环境半自主导航（需人工监督）、低动态平衡要求、简单障碍物避让，列出依赖条件与预期表现。搜索关键词：散步跟随机器人可行性、半自主导航人工监督、2025年机器人技术预测、慢速移动场景挑战。\" }, { \"chapterUuid\": \"ch-005-2\", \"howto\": \"评估2026-2027年跑步跟随场景：高速动态平衡（>5m/s）、地形自适应控制、急转弯与避障，分析现有平衡算法能否达到，指出关节力矩与响应时间需求。搜索关键词：跑步跟随动态平衡、高速移动机器人控制、跑步机实验数据、人形机器人奔跑能力。\" }, { \"chapterUuid\": \"ch-005-3\", \"howto\": \"分析便利店代购从远程辅助（2025）到完全自主（2028）的演进路径：第一阶段远程操作+自主导航，第二阶段自主抓取+人机确认，第三阶段全自主，梳理各阶段技术突破。搜索关键词：便利店自助代购机器人、远程辅助操作、自主取货路径规划、末端执行器可靠性。\" }, { \"chapterUuid\": \"ch-005-4\", \"howto\": \"评估多场景融合（逛街、跑步、代购切换）的通用性挑战：硬件通用性（轮式vs双足）、软件适应能力、场景感知模型泛化，讨论专用机vs通用机的取舍。搜索关键词：多场景通用机器人、专用机vs通用机、场景切换适应性、具身智能泛化难题。\" }, { \"chapterUuid\": \"ch-005-5\", \"howto\": \"整理关键技术突破时间线：固态电池量产（~2027）、VLM泛化能力提升（GPT-5\\RT-3）、轻量化关节电机（高扭矩密度）进展，画出依赖关系图。搜索关键词：固态电池量产时间线、视觉语言模型泛化进展、高扭矩密度关节电机、轻量化执行器突破。\" }, { \"chapterUuid\": \"ch-006-1\", \"howto\": \"搜集高盛、McKinsey、IFR、Tractica等机构对人形机器人（含服务机器人）2030年市场规模预测，区分乐观与保守情景，分析增长驱动因素。搜索关键词：人形机器人市场规模预测、高盛报告、McKinsey机器人市场、服务机器人全球市场。\" }, { \"chapterUuid\": \"ch-006-2\", \"howto\": \"研究当前人形机器人BOM成本（如Optimus预估15-20万）、未来规模效应+技术进步带来的成本下降（电池、芯片、传感器），绘制从15万到3-5万美元的路径图。搜索关键词：人形机器人BOM成本、Optimus成本分析、成本下降曲线、规模效应与降本路径。\" }, { \"chapterUuid\": \"ch-006-3\", \"howto\": \"探索商业模式：硬件销售+订阅（OTA升级、云服务）、共享租赁（按小时/场景）、广告植入（跟随中播放商圈信息）、数据服务（用户行为分析），评估各模式可行性。搜索关键词：机器人订阅模式、共享机器人租赁、服务机器人广告植入、数据变现商业模式。\" }, { \"chapterUuid\": \"ch-006-4\", \"howto\": \"分析竞争格局：特斯拉（全栈生态）、Figure AI（通用具身）、宇树（成本优势）、小米（家庭生态）、波士顿动力（技术领先），识别细分场景（代购、陪伴）的潜在独角兽及生态链整合机会。搜索关键词：具身机器人竞争格局、细分场景优势、人形机器人独角兽、生态链整合。\" }, { \"chapterUuid\": \"ch-006-5\", \"howto\": \"评估风险：技术突破不及预期（灵巧手、续航）、市场接受度低（价格敏感、信任缺失）、法规限制（上路许可、责任法），进行风险概率与影响矩阵分析。搜索关键词：机器人技术风险、市场接受度调研、法规限制人形机器人、风险分析矩阵。\" }, { \"chapterUuid\": \"ch-007-1\", \"howto\": \"研究机器人持续采集视觉、位置、行为习惯数据带来的隐私泄露风险，分析现有法律（GDPR、中国个人信息保护法）适用性，提出数据本地化、匿名化、用户控制权等保护方案。搜索关键词：机器人隐私数据安全、视觉记录法律风险、位置信息保护、GDPR机器人合规。\" }, { \"chapterUuid\": \"ch-007-2\", \"howto\": \"探讨碰撞伤人、商品损坏、决策失误时的法律责任主体（制造商、运营商、用户），借鉴自动驾驶责任判例，分析保险产品设计与法规空白。搜索关键词：机器人责任归属、侵权法律案例、自动驾驶责任判例、机器人保险产品。\" }, { \"chapterUuid\": \"ch-007-3\", \"howto\": \"分析私人贴身机器人对司机、配送员、个人助理等岗位的替代效应，结合历史技术就业替代研究，评估再就业培训与社会保障需求。搜索关键词：机器人就业替代、配送员岗位影响、个人助理自动化、技术性失业对策。\" }, { \"chapterUuid\": \"ch-007-4\", \"howto\": \"调研公众对与机器人共处的情感接受度（如信任、恐惧、依恋），参考日本（爱宝机器人）、国内扫地机器人的接受历程，提出伦理设计准则（透明性、可控制性、不伤害）。搜索关键词：社会接受度机器人、人机共处信任、伦理设计准则、日本机器人接受度调查。\" }, { \"chapterUuid\": \"ch-008-1\", \"howto\": \"设定2024-2025年关键里程碑：半自主代购试点（指定便利店、远程协助）、开放场地慢速跟随首秀（公园、步行街）、完成安全认证与用户测试，列出需达成的技术指标。搜索关键词：2025年机器人里程碑、半自主代购试点、开放场地跟随测试、安全认证标准。\" }, { \"chapterUuid\": \"ch-008-2\", \"howto\": \"设定2026-2027年关键突破：续航达到4小时以上（含跑步）、灵巧手通用抓取成功率>90%、动态障碍物避让响应<0.1秒，依托固态电池和新型关节电机实现。搜索关键词：机器人续航4小时、灵巧手成功率90%、机器人避让响应时间、固态电池机器人应用。\" }, { \"chapterUuid\": \"ch-008-3\", \"howto\": \"展望2028-2029年：完全自主陪伴（无需远程干预）、多场景无缝切换（室内外、购物、跑步）、成本降至3-5万美元（消费级），评估依赖条件（VLM泛化、材料成本）。搜索关键词：2028年人形机器人自主、消费级机器人成本、多场景切换技术、机器人成本下降至3万美元。\" }, { \"chapterUuid\": \"ch-008-4\", \"howto\": \"建议研发与投资方向：固态电池（优先）、VLM泛化（视觉语言模型）、轻量化材料（镁锂合金、复合材料）、仿真平台（加速训练），评估各方向投入产出比与风险。搜索关键词：固态电池投资前景、VLM研发方向、轻量化材料机器人、仿真平台机器人训练、投融资建议。\" }, { \"chapterUuid\": \"ch-009-1\", \"howto\": \"综合前面章节分析，给出核心结论：未来3-5年内私人贴身伙伴机器人可行性为中等，优先落地半自主场景（散步跟随、代购辅助），完全自主需更长时间。提炼关键证据与量化判断。搜索关键词：可行性总结、半自主优先落地、私人机器人中期展望。\" }, { \"chapterUuid\": \"ch-009-2\", \"howto\": \"针对企业与创业者提出战略建议：聚焦细分场景（如高端代购）、采用混合模式（自主+远程协助）、利用大模型提升交互、与地产/零售合作落地试点。搜索关键词：机器人创业策略、细分场景聚焦、混合远程协助模式、零售机器人合作案例。\" }, { \"chapterUuid\": \"ch-009-3\", \"howto\": \"向政策制定者建议：建立伦理审查与测试规范（类似自动驾驶路测牌照）、提供研发补贴引导、设立责任保险框架、鼓励公共空间测试。搜索关键词：机器人伦理框架、测试规范政策、政府补贴政策、责任保险建议。\" }, { \"chapterUuid\": \"ch-009-4\", \"howto\": \"总结未解决的关键开放问题：通用灵巧操作、长续航电池、社会接受度、成本下降曲线，指出未来研究热点与技术颠覆可能性（如能源突破、脑机接口融合）。搜索关键词：机器人开放问题、通用操作挑战、未来技术颠覆、脑机接口机器人。\" } ]";

		s = fixJsonUnescapedNewlines(s);
		System.out.println(s);

		JSONArray jsonObject = array(s);
		System.out.println(isJson(jsonObject.toString(2)));
	}
}
