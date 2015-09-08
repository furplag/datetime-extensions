/**
 *
 */
package jp.furplag.util.time.lunisolar;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jp.furplag.util.Localizer;
import jp.furplag.util.commons.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;

import com.google.common.collect.ImmutableMap;

public class JapaneseLunisolar extends AbstractLunisolar {

  /** the epoch millis of 1872-12-31T23:59:59.999+09:00:00 */
  private static final long JAPANESE_LUNISOLAR_ENDPOINT = -3061011600001L;

  private static final DateTimeZone ZONE = DateTimeZone.forID("Asia/Tokyo");

  private static final String[] SOLAR_TERMS = "shunbun,seimei,kokuu,rikka,shoman,boshu,geshi,sho-sho,taisho,risshu,shosho,hakuro,shubun,kanro,soko,ritto,shosetsu,taisetsu,toji,shokan,taikan,risshun,usui,keichitsu".split(",");

  private static final String[] DAY_OF_WEEKS = "taian,shakko,sensho,tomobiki,senpu,butsumetsu".split(",");

  private static final Map<Integer, JapaneseLunisolar> ORIGIN = init();

  private static Map<Integer, JapaneseLunisolar> init() {
    Map<Integer, JapaneseLunisolar> map = new HashMap<Integer, JapaneseLunisolar>();
    map.put(00, new JapaneseLunisolar(false, false));
    map.put(01, new JapaneseLunisolar(false, true));
    map.put(10, new JapaneseLunisolar(true, false));
    map.put(11, new JapaneseLunisolar(true, true));

    return ImmutableMap.copyOf(map);
  }

  private JapaneseLunisolar(boolean isHeishi, boolean isJimyoin) {
    super(ZONE);
    Object[][] periods = { // Japanese Imperial Era
    {"ancient" //
    , new Object[]{"jinmu", "-0660-02-18", null} //
    , new Object[]{"suizei", "-0581-02-23", null} //
    , new Object[]{"annei", "-0548-02-10", null} //
    , new Object[]{"itoku", "-0510-03-15", null} //
    , new Object[]{"kosho", "-0475-02-21", null} //
    , new Object[]{"koan", "-0392-02-02", null} //
    , new Object[]{"korei", "-0290-02-19", null} //
    , new Object[]{"kogen", "-0214-02-21", null} //
    , new Object[]{"kaika", "-0157-02-08", null} //
    , new Object[]{"sujin", "-0097-02-17", null} //
    , new Object[]{"suinin", "-0029-02-04", null} //
    , new Object[]{"keiko", "0071-08-24", null} //
    , new Object[]{"seimu", "0131-02-19", null} //
    , new Object[]{"chuai", "0192-02-11", null} //
    , new Object[]{"jingu", "0201-01-22", null} //
    , new Object[]{"ojin", "0270-02-08", null} //
    , new Object[]{"nintoku", "0313-02-14", null} //
    , new Object[]{"richu", "0400-03-12", null} //
    , new Object[]{"hanzei", "0406-02-05", null} //
    , new Object[]{"ingyo", "0413-01-18", null} //
    , new Object[]{"anko", "0454-02-14", null} //
    , new Object[]{"yuryaku", "0457-02-10", null} //
    , new Object[]{"seinei", "0480-02-11", null} //
    , new Object[]{"kenzo", "0485-02-01", null} //
    , new Object[]{"ninken", "0488-02-02", null} //
    , new Object[]{"buretsu", "0499-01-28", null} //
    , new Object[]{"keitai", "0507-03-03", null} //
    , new Object[]{"ankan", "0534-01-30", null} //
    , new Object[]{"senka", "0536-02-08", null} //
    , new Object[]{"kinmei", "0540-01-25", null} //
    , new Object[]{"bidatsu", "0572-04-30", null} //
    , new Object[]{"yomei", "0586-01-25", null} //
    , new Object[]{"sushun", "0588-02-03", "0593-02-06"} //
    }, {"aska" //
    , new Object[]{"suiko", "0593-02-07", null} //
    , new Object[]{"jomei", "0629-02-02", null} //
    , new Object[]{"kogyoku", "0642-02-19", null} //
    , new Object[]{"taika", "0645-07-17", null} //
    , new Object[]{"hakuchi", "0650-03-22", null} //
    , new Object[]{"seimei", "0655-02-14", null} //
    , new Object[]{"tenchi", "0662-01-25", null} //
    , new Object[]{"kobun", "0672-02-04", null} //
    , new Object[]{"tenmu", "0673-03-20", null} //
    , new Object[]{"shucho", "0686-08-14", null} //
    , new Object[]{"jito", "0687-02-18", null} //
    , new Object[]{"monmu", "0697-08-22", null} //
    , new Object[]{"taiho", "0701-05-03", null} //
    , new Object[]{"keiun", "0704-06-16", null} //
    , new Object[]{"wado", "0708-02-07", "0710-04-12"} //
    }, {"nara" //
    , new Object[]{"wado", "0708-02-07", null} //
    , new Object[]{"reiki", "0715-10-03", null} //
    , new Object[]{"yoro", "0717-12-24", null} //
    , new Object[]{"jinki", "0724-03-03", null} //
    , new Object[]{"tenpyo", "0729-09-02", null} //
    , new Object[]{"tenpyokanpo", "0749-05-04", null} //
    , new Object[]{"tenpyoshoho", "0749-08-19", null} //
    , new Object[]{"tenpyohoji", "0757-09-06", null} //
    , new Object[]{"tenpyojingo", "0765-02-01", null} //
    , new Object[]{"jingokeiun", "0767-09-13", null} //
    , new Object[]{"hoki", "0770-10-23", null} //
    , new Object[]{"teno", "0781-01-30", null} //
    , new Object[]{"enryaku", "0782-09-30", "0794-11-18"} //
    }, {"heian" //
    , new Object[]{"enryaku", "0782-09-30", null} //
    , new Object[]{"daido", "0806-06-08", null} //
    , new Object[]{"konin", "0810-10-20", null} //
    , new Object[]{"tencho", "0824-02-08", null} //
    , new Object[]{"jowa", "0834-02-14", null} //
    , new Object[]{"kasho", "0848-07-16", null} //
    , new Object[]{"ninju", "0851-06-01", null} //
    , new Object[]{"saiko", "0854-12-23", null} //
    , new Object[]{"tenan", "0857-03-20", null} //
    , new Object[]{"jogan", "0859-05-20", null} //
    , new Object[]{"gangyo", "0877-06-01", null} //
    , new Object[]{"ninna", "0885-03-11", null} //
    , new Object[]{"kanpyo", "0889-05-30", null} //
    , new Object[]{"shotai", "0898-05-20", null} //
    , new Object[]{"engi", "0901-08-31", null} //
    , new Object[]{"encho", "0923-05-29", null} //
    , new Object[]{"johei", "0931-05-16", null} //
    , new Object[]{"tengyo", "0938-06-22", null} //
    , new Object[]{"tenryaku", "0947-05-15", null} //
    , new Object[]{"tentoku", "0957-11-21", null} //
    , new Object[]{"owa", "0961-03-05", null} //
    , new Object[]{"koho", "0964-08-19", null} //
    , new Object[]{"anna", "0968-09-08", null} //
    , new Object[]{"tenroku", "0970-05-03", null} //
    , new Object[]{"tenen", "0974-01-16", null} //
    , new Object[]{"jogen", "0976-08-11", null} //
    , new Object[]{"tengen", "0978-12-31", null} //
    , new Object[]{"eikan", "0983-05-29", null} //
    , new Object[]{"kanna", "0985-05-15", null} //
    , new Object[]{"eien", "0987-05-05", null} //
    , new Object[]{"eiso", "0989-09-10", null} //
    , new Object[]{"shoryaku", "0990-11-26", null} //
    , new Object[]{"chotoku", "0995-03-25", null} //
    , new Object[]{"choho", "0999-02-01", null} //
    , new Object[]{"kanko", "1004-08-08", null} //
    , new Object[]{"chowa", "1013-02-08", null} //
    , new Object[]{"kannin", "1017-05-21", null} //
    , new Object[]{"jian", "1021-03-17", null} //
    , new Object[]{"manju", "1024-08-19", null} //
    , new Object[]{"chogen", "1028-08-18", null} //
    , new Object[]{"choryaku", "1037-05-09", null} //
    , new Object[]{"chokyu", "1040-12-16", null} //
    , new Object[]{"kantoku", "1044-12-16", null} //
    , new Object[]{"eisho", "1046-05-22", null} //
    , new Object[]{"tengi", "1053-02-02", null} //
    , new Object[]{"kohei", "1058-09-19", null} //
    , new Object[]{"jiryaku", "1065-09-04", null} //
    , new Object[]{"enkyu", "1069-05-06", null} //
    , new Object[]{"joho", "1074-09-16", null} //
    , new Object[]{"joryaku", "1077-12-05", null} //
    , new Object[]{"eiho", "1081-03-22", null} //
    , new Object[]{"otoku", "1084-03-15", null} //
    , new Object[]{"kanji", "1087-05-11", null} //
    , new Object[]{"kaho", "1095-01-23", null} //
    , new Object[]{"eicho", "1097-01-03", null} //
    , new Object[]{"jotoku", "1097-12-27", null} //
    , new Object[]{"kowa", "1099-09-15", null} //
    , new Object[]{"choji", "1104-03-08", null} //
    , new Object[]{"kajo", "1106-05-13", null} //
    , new Object[]{"tennin", "1108-09-09", null} //
    , new Object[]{"tenei", "1110-07-31", null} //
    , new Object[]{"eikyu", "1113-08-25", null} //
    , new Object[]{"genei", "1118-04-25", null} //
    , new Object[]{"hoan", "1120-05-09", null} //
    , new Object[]{"tenji", "1124-05-18", null} //
    , new Object[]{"daiji", "1126-02-15", null} //
    , new Object[]{"tensho", "1131-02-28", null} //
    , new Object[]{"chosho", "1132-09-21", null} //
    , new Object[]{"hoen", "1135-06-10", null} //
    , new Object[]{"eiji", "1141-08-13", null} //
    , new Object[]{"koji", "1142-05-25", null} //
    , new Object[]{"tenyo", "1144-03-28", null} //
    , new Object[]{"kyuan", "1145-08-12", null} //
    , new Object[]{"ninpei", "1151-02-14", null} //
    , new Object[]{"kyuju", "1154-12-04", null} //
    , new Object[]{"hogen", "1156-05-18", null} //
    , new Object[]{"heiji", "1159-05-09", null} //
    , new Object[]{"eiryaku", "1160-02-18", null} //
    , new Object[]{"oho", "1161-09-24", null} //
    , new Object[]{"chokan", "1163-05-04", null} //
    , new Object[]{"eiman", "1165-07-14", null} //
    , new Object[]{"ninan", "1166-09-23", null} //
    , new Object[]{"kao", "1169-05-06", null} //
    , new Object[]{"joan", "1171-05-27", null} //
    , new Object[]{"angen", "1175-08-16", null} //
    , new Object[]{"jisho", "1177-08-29", "1181-08-24"} //
    }, isHeishi ? new Object[]{ //
    "heian.heishi" //
    , new Object[]{"yowa", "1181-08-25", null} //
    , new Object[]{"juei", "1182-06-29", "1185-04-25"} //
    } : new Object[]{ //
    "heian.genji" //
    , new Object[]{"jisho", "1177-08-29", null} //
    , new Object[]{"genryaku", "1184-05-27", "1185-04-25"} //
    }, {"kamakura" //
    , new Object[]{"genryaku", "1184-05-27", null} //
    , new Object[]{"bunji", "1185-09-09", null} //
    , new Object[]{"kenkyu", "1190-05-16", null} //
    , new Object[]{"shoji", "1199-05-23", null} //
    , new Object[]{"kennin", "1201-03-19", null} //
    , new Object[]{"genkyu", "1204-03-23", null} //
    , new Object[]{"kenei", "1206-06-05", null} //
    , new Object[]{"jogen", "1207-11-16", null} //
    , new Object[]{"kenryaku", "1211-04-23", null} //
    , new Object[]{"kenpo", "1214-01-18", null} //
    , new Object[]{"jokyu", "1219-05-27", null} //
    , new Object[]{"joo", "1222-05-25", null} //
    , new Object[]{"gennin", "1224-12-31", null} //
    , new Object[]{"karoku", "1225-05-28", null} //
    , new Object[]{"antei", "1228-01-18", null} //
    , new Object[]{"kanki", "1229-03-31", null} //
    , new Object[]{"joei", "1232-04-23", null} //
    , new Object[]{"tenpuku", "1233-05-25", null} //
    , new Object[]{"bunryaku", "1234-11-27", null} //
    , new Object[]{"katei", "1235-11-01", null} //
    , new Object[]{"ryakunin", "1238-12-30", null} //
    , new Object[]{"eno", "1239-03-13", null} //
    , new Object[]{"ninji", "1240-08-05", null} //
    , new Object[]{"kangen", "1243-03-18", null} //
    , new Object[]{"hoji", "1247-04-05", null} //
    , new Object[]{"kencho", "1249-05-02", null} //
    , new Object[]{"kogen", "1256-10-24", null} //
    , new Object[]{"shoka", "1257-03-31", null} //
    , new Object[]{"shogen", "1259-04-20", null} //
    , new Object[]{"buno", "1260-05-24", null} //
    , new Object[]{"kocho", "1261-03-22", null} //
    , new Object[]{"bunei", "1264-03-27", null} //
    , new Object[]{"kenji", "1275-05-22", null} //
    , new Object[]{"koan", "1278-03-23", null} //
    , new Object[]{"shoo", "1288-05-29", null} //
    , new Object[]{"einin", "1293-09-06", null} //
    , new Object[]{"shoan", "1299-05-25", null} //
    , new Object[]{"kengen", "1302-12-10", null} //
    , new Object[]{"kagen", "1303-09-16", null} //
    , new Object[]{"tokuji", "1307-01-18", null} //
    , new Object[]{"enkyo", "1308-11-22", null} //
    , new Object[]{"ocho", "1311-05-17", null} //
    , new Object[]{"showa", "1312-04-27", null} //
    , new Object[]{"bunpo", "1317-03-16", null} //
    , new Object[]{"geno", "1319-05-18", null} //
    , new Object[]{"genko", "1321-03-22", null} //
    , new Object[]{"shochu", "1324-12-25", null} //
    , new Object[]{"karyaku", "1326-05-28", null} //
    , new Object[]{"gentoku", "1329-09-22", "1331-09-10"} //
    }, isJimyoin ? new Object[]{ //
    "kamakura.jimyoin" //
    , new Object[]{"gentoku", "1329-09-22", null} //
    , new Object[]{"shokei", "1332-05-23", "1334-03-04"} //
    } : new Object[]{ //
    "kamakura.daikakuji" //
    , new Object[]{"genko", "1331-09-11", "1333-06-29"} //
    }, {"nanboku-cho" //
    , new Object[]{"genko", "1331-09-11", null} //
    , new Object[]{"kenmu", "1334-03-05", "1336-04-10"} //
    }, isJimyoin ? new Object[]{ //
    "nanboku-cho.jimyoin" //
    , new Object[]{"kenmu", "1334-03-05", null} //
    , new Object[]{"ryakuo", "1338-10-11", null} //
    , new Object[]{"koei", "1342-06-01", null} //
    , new Object[]{"jowa", "1345-11-15", null} //
    , new Object[]{"kano", "1350-04-04", null} //
    , new Object[]{"bunna", "1352-11-04", null} //
    , new Object[]{"enbun", "1356-04-29", null} //
    , new Object[]{"koan", "1361-05-04", null} //
    , new Object[]{"joji", "1362-10-11", null} //
    , new Object[]{"oan", "1368-03-07", null} //
    , new Object[]{"eiwa", "1375-03-29", null} //
    , new Object[]{"koryaku", "1379-04-09", null} //
    , new Object[]{"eitoku", "1381-03-20", null} //
    , new Object[]{"shitoku", "1384-03-19", null} //
    , new Object[]{"kakyo", "1387-10-05", null} //
    , new Object[]{"koo", "1389-03-07", null} //
    , new Object[]{"meitoku", "1390-04-12", "1392-11-19"} //
    } : new Object[]{ //
    "nanboku-cho.daikakuji" //
    , new Object[]{"engen", "1336-04-11", null} //
    , new Object[]{"kokoku", "1340-05-25", null} //
    , new Object[]{"shohei", "1347-01-20", null} //
    , new Object[]{"kentoku", "1370-08-16", null} //
    , new Object[]{"bunchu", "1372-05-04", null} //
    , new Object[]{"tenju", "1375-06-26", null} //
    , new Object[]{"kowa", "1381-03-06", null} //
    , new Object[]{"genchu", "1384-05-18", "1392-11-19"} //
    }, {"muromachi" //
    , new Object[]{"meitoku", "1390-04-12", null} //
    , new Object[]{"oei", "1394-08-02", null} //
    , new Object[]{"shocho", "1428-06-10", null} //
    , new Object[]{"eikyo", "1429-10-03", null} //
    , new Object[]{"kakitsu", "1441-03-19", null} //
    , new Object[]{"bunan", "1444-02-23", null} //
    , new Object[]{"hotoku", "1449-08-16", null} //
    , new Object[]{"kyotoku", "1452-08-10", null} //
    , new Object[]{"kosho", "1455-09-06", null} //
    , new Object[]{"choroku", "1457-10-16", null} //
    , new Object[]{"kansho", "1461-02-01", null} //
    , new Object[]{"bunsho", "1466-03-14", "1467-04-08"} //
    }, {"muromachi.sengoku" //
    , new Object[]{"onin", "1467-04-09", null} //
    , new Object[]{"bunmei", "1469-06-08", null} //
    , new Object[]{"chokyo", "1487-08-09", null} //
    , new Object[]{"entoku", "1489-09-16", null} //
    , new Object[]{"meio", "1492-08-12", null} //
    , new Object[]{"bunki", "1501-03-18", null} //
    , new Object[]{"eisei", "1504-03-16", null} //
    , new Object[]{"daiei", "1521-09-23", null} //
    , new Object[]{"kyoroku", "1528-09-03", null} //
    , new Object[]{"tenbun", "1532-08-29", null} //
    , new Object[]{"koji", "1555-11-07", null} //
    , new Object[]{"eiroku", "1558-03-28", null} //
    , new Object[]{"genki", "1570-05-27", "1573-08-24"} //
    }, {"azuchi-momoyama" //
    , new Object[]{"tensho", "1573-08-25", null} //
    , new Object[]{"bunroku", "1593-01-10", null} //
    , new Object[]{"keicho", "1596-12-16", "1603-03-24"} //
    }, {"edo" //
    , new Object[]{"keicho", "1596-12-16", null} //
    , new Object[]{"genna", "1615-09-05", null} //
    , new Object[]{"kanei", "1624-04-17", null} //
    , new Object[]{"shoho", "1645-01-13", null} //
    , new Object[]{"keian", "1648-04-07", null} //
    , new Object[]{"joo", "1652-10-20", null} //
    , new Object[]{"meireki", "1655-05-18", null} //
    , new Object[]{"manji", "1658-08-21", null} //
    , new Object[]{"kanbun", "1661-05-23", null} //
    , new Object[]{"enpo", "1673-10-30", null} //
    , new Object[]{"tenna", "1681-11-09", null} //
    , new Object[]{"jokyo", "1684-04-05", null} //
    , new Object[]{"genroku", "1688-10-23", null} //
    , new Object[]{"hoei", "1704-04-16", null} //
    , new Object[]{"shotoku", "1711-06-11", null} //
    , new Object[]{"kyoho", "1716-08-09", null} //
    , new Object[]{"genbun", "1736-06-07", null} //
    , new Object[]{"kanpo", "1741-04-12", null} //
    , new Object[]{"enkyo", "1744-04-03", null} //
    , new Object[]{"kanen", "1748-08-05", null} //
    , new Object[]{"horeki", "1751-12-14", null} //
    , new Object[]{"meiwa", "1764-06-30", null} //
    , new Object[]{"anei", "1772-12-10", null} //
    , new Object[]{"tenmei", "1781-04-25", null} //
    , new Object[]{"kansei", "1789-02-19", null} //
    , new Object[]{"kyowa", "1801-03-19", null} //
    , new Object[]{"bunka", "1804-03-22", null} //
    , new Object[]{"bunsei", "1818-05-26", null} //
    , new Object[]{"tenpo", "1831-01-23", null} //
    , new Object[]{"koka", "1845-01-09", null} //
    , new Object[]{"kaei", "1848-04-01", null} //
    , new Object[]{"ansei", "1855-01-15", null} //
    , new Object[]{"manen", "1860-04-08", null} //
    , new Object[]{"bunkyu", "1861-03-29", null} //
    , new Object[]{"genji", "1864-03-27", null} //
    , new Object[]{"keio", "1865-05-02", "1868-10-22"} //
    }, {"meiji" // 1868-1912 : in Japan uses the Gregorian calendar since 1873.
    , new Object[]{"meiji", "1868-10-23", "1912-07-29"} //
    }, {"taisho" // 1912-1926
    , new Object[]{"taisho", "1912-07-30", "1926-12-24"} //
    }, {"showa" // 1926-1989
    , new Object[]{"showa", "1926-12-25", "1989-01-07"} //
    }, {"heisei" // current in 2015.
    , new Object[]{"heisei", "1989-01-08", null} //
    }};

    initialize(buildMap(periods));
  }

  public static JapaneseLunisolar getInstance() {
    return getInstance(false, false, Locale.getDefault());
  }

  public static JapaneseLunisolar getInstance(Object locale) {
    return getInstance(false, false, locale);
  }

  public static JapaneseLunisolar getInstance(boolean isHeishi, boolean isJimyoin, Object locale) {
    return ORIGIN.get(isHeishi ? 10 : 0 + (isJimyoin ? 1 : 0)).withLocale(JapaneseLunisolar.class, Localizer.newLocale(locale));
  }

  public JapaneseLunisolar withLocale(Object locale) {
    return this.withLocale(Localizer.newLocale(locale));
  }

  public Map<String, Object> toLunisolar(DateTime then) {
    Map<String, Object> lunisolar = super.toLunisolar(then);
    if (lunisolar == null) return null;
    if (lunisolar.containsKey("longitude")) lunisolar.put("nameOfSolarTerm", getNameOfSolarTerm(SOLAR_TERMS, Double.valueOf(lunisolar.get("longitude").toString())));
    lunisolar.put("nameOfSolarTerm", getNameOfSolarTerm(SOLAR_TERMS, Double.valueOf(lunisolar.get("longitude").toString())));
    lunisolar.put("nameOfDayOfWeek", getNameOfDayOfWeek(DAY_OF_WEEKS, Integer.valueOf(lunisolar.get("dayOfWeek").toString())));
    Era era = getEra(then);
    if (era == null) return lunisolar;
    lunisolar.put("nameOfEra", getNameOfEra(era));
    lunisolar.put("keyOfEra", era.getKey());
    lunisolar.put("yearOfEra", era.getYearOfEra(then));

    return lunisolar;
  }

  public String toStringLunisolar(Object dateTime) throws Exception {
    return toStringLunisolar(dateTime, "F-");
  }


  @Override
  public String toStringLunisolar(Object dateTime, String formatStyle) throws Exception {
    String pattern = "";
    try {
    DateTime then = LunisolarDateTimeUtils.toDT(dateTime, GJChronology.getInstance(ZONE));
    pattern = LunisolarDateTimeUtils.getLocalizedPattern(formatStyle, getLocale());
    if (StringUtils.isSimilarToBlank(pattern)) return "";
    Map<String, Object> lunisolar = toLunisolar(then);
    if (!(lunisolar != null && lunisolar.size() > 0)) return "";
    if (!lunisolar.containsKey("keyOfEra")) return then.toString(LunisolarDateTimeUtils.getLocalizedPattern(formatStyle, Locale.ROOT));
    pattern = StringUtils.truncateLast(pattern, "(?i)\\s*z").trim();
    if (then.getMillis() > JAPANESE_LUNISOLAR_ENDPOINT) {
      pattern = pattern.replaceAll("GGGG", "'" + lunisolar.get("nameOfEra").toString() + "'").replaceAll("G+", "'" + lunisolar.get("keyOfEra").toString().substring(0, 1).toUpperCase(Locale.ROOT) + "'");
      pattern = pattern.replaceAll("y+", new SimpleDateFormat(StringUtils.truncateAll(pattern, "[^y]*"), Localizer.newLocale("ja_JP_JP")).format(then.toDate()));

      return then.toString(pattern);
    }
    pattern = pattern.replaceAll("GGGG", "'" + lunisolar.get("nameOfEra").toString() + "'").replaceAll("G+", "'" + lunisolar.get("keyOfEra").toString().substring(0, 1).toUpperCase(Locale.ROOT) + "'");
    pattern = pattern.replaceAll("y+", StringUtils.replaceAll(lunisolar.get("yearOfEra").toString(), "^1$", "ja".equalsIgnoreCase(getLocale().getLanguage()) && formatStyle.startsWith("F") ? "元" : "1"));
    pattern = pattern.replaceAll("M+", ((Boolean) lunisolar.get("isIntercalary") ? "閏" : "") + lunisolar.get("monthOfYear").toString());
    pattern = pattern.replaceAll("d+", lunisolar.get("dayOfMonth").toString());

//    if (lunisolar != null) {
//      for (Entry<String, Object> e : lunisolar.entrySet()) {
//        System.out.println(e.getKey() + ":" + e.getValue());
//      }
//    }

    return then.toString(pattern);
    } catch (Exception e) {
      System.err.println(pattern);
        throw e;
    }
  }

  public static void main(String[] args) throws Exception {
    double julianDay = LunisolarDateTimeUtils.toAJD(DateTime.now());
    JapaneseLunisolar lunisolar = JapaneseLunisolar.getInstance(Localizer.newLocale("ja_JP_JP"));
    do {
      System.out.print(LunisolarDateTimeUtils.toDT(julianDay, ZONE));
      System.out.print(" is S:");
      System.out.print(lunisolar.toStringLunisolar(LunisolarDateTimeUtils.toDT(julianDay), "S-"));
      System.out.print(" M:");
      System.out.print(lunisolar.toStringLunisolar(LunisolarDateTimeUtils.toDT(julianDay), "M-"));
      System.out.print(" L:");
      System.out.print(lunisolar.toStringLunisolar(LunisolarDateTimeUtils.toDT(julianDay), "L-"));
      System.out.print(" F:");
      System.out.println(lunisolar.toStringLunisolar(LunisolarDateTimeUtils.toDT(julianDay), "F-"));
      julianDay -= LunisolarDateTimeUtils.JULIAN_YEAR * 100d;
    } while (julianDay > 0);
  }
}
