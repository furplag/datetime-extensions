/**
 *
 */
package jp.furplag.util.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jp.furplag.util.commons.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.GJChronology;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author furplag.jp
 *
 */
public class JapaneseEra {

  public JapaneseEra() {
    // TODO 自動生成されたコンストラクター・スタブ
  }

  private static final String RESOURCE = "JapaneseEra";

  private static final DateTime D = DateTime.now(GJChronology.getInstanceUTC()).withZone(DateTimeZone.forID("Asia/Tokyo")).withTimeAtStartOfDay();

  private static final List<Era> PERIODS;
  private static final Map<String, List<Era>> ERAS;
  static {
    List<Era> periods = new ArrayList<Era>();
    Map<String, List<Era>> era = new HashMap<String, List<Era>>();
    List<Era> aska = new ArrayList<Era>();
    aska.add(new Era("taika.0645", new Interval(D.withDate(645, 7, 17), D.withDate(650, 3, 22))));
    aska.add(new Era("hakuchi.0650", new Interval(D.withDate(650, 3, 22), D.withDate(654, 11, 24))));
    aska.add(new Era("unknown.0654", new Interval(D.withDate(654, 11, 24), D.withDate(686, 8, 14))));
    aska.add(new Era("shucho.0686", new Interval(D.withDate(686, 8, 14), D.withDate(686, 10, 1))));
    aska.add(new Era("unknown.0686", new Interval(D.withDate(686, 10, 1), D.withDate(701, 5, 3))));
    aska.add(new Era("taiho.0701", new Interval(D.withDate(701, 5, 3), D.withDate(704, 6, 16))));
    aska.add(new Era("keiun.0704", new Interval(D.withDate(704, 6, 16), D.withDate(708, 2, 7))));
    aska.add(new Era("wado.0708", new Interval(D.withDate(708, 2, 7), D.withDate(715, 10, 3))));
    era.put("aska", aska);
    periods.add(new Era("aska", new Interval(aska.get(0).interval.getStart(), aska.get(aska.size() - 1).interval.getEnd())));

    List<Era> nara = new ArrayList<Era>();
    nara.add(new Era("reiki.0715", new Interval(D.withDate(715, 10, 3), D.withDate(717, 12, 24))));
    nara.add(new Era("yoro.0717", new Interval(D.withDate(717, 12, 24), D.withDate(724, 3, 3))));
    nara.add(new Era("jinki.0724", new Interval(D.withDate(724, 3, 3), D.withDate(729, 9, 2))));
    nara.add(new Era("tenpyo.0729", new Interval(D.withDate(729, 9, 2), D.withDate(749, 5, 4))));
    nara.add(new Era("tenpyo.kanpo.0749", new Interval(D.withDate(749, 5, 4), D.withDate(749, 8, 19))));
    nara.add(new Era("tenpyo.shoho.0749", new Interval(D.withDate(749, 8, 19), D.withDate(757, 9, 6))));
    nara.add(new Era("tenpyo.hoji.0757", new Interval(D.withDate(757, 9, 6), D.withDate(765, 2, 1))));
    nara.add(new Era("tenpyo.jingo.0765", new Interval(D.withDate(765, 2, 1), D.withDate(767, 9, 13))));
    nara.add(new Era("jingo.keiun.0767", new Interval(D.withDate(767, 9, 13), D.withDate(770, 10, 23))));
    nara.add(new Era("hoki.0770", new Interval(D.withDate(770, 10, 23), D.withDate(781, 1, 30))));
    nara.add(new Era("teno.0781", new Interval(D.withDate(781, 1, 30), D.withDate(782, 9, 30))));
    nara.add(new Era("enryaku.0782", new Interval(D.withDate(782, 9, 30), D.withDate(806, 6, 8))));
    era.put("nara", nara);
    periods.add(new Era("nara", new Interval(nara.get(0).interval.getStart(), nara.get(nara.size() - 1).interval.getEnd())));

    List<Era> heian = new ArrayList<Era>();
    heian.add(new Era("daido.0806", new Interval(D.withDate(806, 6, 8), D.withDate(810, 10, 20))));
    heian.add(new Era("konin.0810", new Interval(D.withDate(810, 10, 20), D.withDate(824, 2, 8))));
    heian.add(new Era("tencho.0824", new Interval(D.withDate(824, 2, 8), D.withDate(834, 2, 14))));
    heian.add(new Era("jowa.0834", new Interval(D.withDate(834, 2, 14), D.withDate(848, 7, 16))));
    heian.add(new Era("kasho.0848", new Interval(D.withDate(848, 7, 16), D.withDate(851, 6, 1))));
    heian.add(new Era("ninju.0851", new Interval(D.withDate(851, 6, 1), D.withDate(854, 12, 23))));
    heian.add(new Era("saiko.0854", new Interval(D.withDate(854, 12, 23), D.withDate(857, 3, 20))));
    heian.add(new Era("tennan.0857", new Interval(D.withDate(857, 3, 20), D.withDate(859, 5, 20))));
    heian.add(new Era("jogan.0859", new Interval(D.withDate(859, 5, 20), D.withDate(877, 6, 1))));
    heian.add(new Era("gangyo.0877", new Interval(D.withDate(877, 6, 1), D.withDate(885, 3, 11))));
    heian.add(new Era("ninna.0885", new Interval(D.withDate(885, 3, 11), D.withDate(889, 5, 30))));
    heian.add(new Era("kanpyo.0889", new Interval(D.withDate(889, 5, 30), D.withDate(898, 5, 20))));
    heian.add(new Era("shotai.0898", new Interval(D.withDate(898, 5, 20), D.withDate(901, 8, 31))));
    heian.add(new Era("engi.0901", new Interval(D.withDate(901, 8, 31), D.withDate(923, 5, 29))));
    heian.add(new Era("encho.0923", new Interval(D.withDate(923, 5, 29), D.withDate(931, 5, 16))));
    heian.add(new Era("johei.0931", new Interval(D.withDate(931, 5, 16), D.withDate(938, 6, 22))));
    heian.add(new Era("tengyo.0938", new Interval(D.withDate(938, 6, 22), D.withDate(947, 5, 15))));
    heian.add(new Era("tenryaku.0947", new Interval(D.withDate(947, 5, 15), D.withDate(957, 11, 21))));
    heian.add(new Era("tentoku.0957", new Interval(D.withDate(957, 11, 21), D.withDate(961, 3, 5))));
    heian.add(new Era("owa.0961", new Interval(D.withDate(961, 3, 5), D.withDate(964, 8, 19))));
    heian.add(new Era("koho.0964", new Interval(D.withDate(964, 8, 19), D.withDate(968, 9, 8))));
    heian.add(new Era("anna.0968", new Interval(D.withDate(968, 9, 8), D.withDate(970, 5, 3))));
    heian.add(new Era("tenroku.0970", new Interval(D.withDate(970, 5, 3), D.withDate(974, 1, 16))));
    heian.add(new Era("tenen.0974", new Interval(D.withDate(974, 1, 16), D.withDate(976, 8, 11))));
    heian.add(new Era("jogen.0976", new Interval(D.withDate(976, 8, 11), D.withDate(978, 12, 31))));
    heian.add(new Era("tengen.0978", new Interval(D.withDate(978, 12, 31), D.withDate(983, 5, 29))));
    heian.add(new Era("eikan.0983", new Interval(D.withDate(983, 5, 29), D.withDate(985, 5, 19))));
    heian.add(new Era("kanna.0985", new Interval(D.withDate(985, 5, 19), D.withDate(987, 5, 5))));
    heian.add(new Era("eien.0987", new Interval(D.withDate(987, 5, 5), D.withDate(989, 9, 10))));
    heian.add(new Era("eiso.0989", new Interval(D.withDate(989, 9, 10), D.withDate(990, 11, 26))));
    heian.add(new Era("shoryaku.0990", new Interval(D.withDate(990, 11, 26), D.withDate(995, 3, 25))));
    heian.add(new Era("chotoku.0995", new Interval(D.withDate(995, 3, 25), D.withDate(999, 2, 1))));
    heian.add(new Era("choho.0999", new Interval(D.withDate(999, 2, 1), D.withDate(1004, 8, 8))));
    heian.add(new Era("kanko.1004", new Interval(D.withDate(1004, 8, 8), D.withDate(1013, 2, 8))));
    heian.add(new Era("chowa.1013", new Interval(D.withDate(1013, 2, 8), D.withDate(1017, 5, 21))));
    heian.add(new Era("kannin.1017", new Interval(D.withDate(1017, 5, 21), D.withDate(1021, 3, 17))));
    heian.add(new Era("jian.1021", new Interval(D.withDate(1021, 3, 17), D.withDate(1024, 8, 19))));
    heian.add(new Era("manju.1024", new Interval(D.withDate(1024, 8, 19), D.withDate(1028, 8, 18))));
    heian.add(new Era("chogen.1028", new Interval(D.withDate(1028, 8, 18), D.withDate(1037, 5, 9))));
    heian.add(new Era("choryaku.1037", new Interval(D.withDate(1037, 5, 9), D.withDate(1040, 12, 16))));
    heian.add(new Era("chokyu.1040", new Interval(D.withDate(1040, 12, 16), D.withDate(1044, 12, 16))));
    heian.add(new Era("kantoku.1044", new Interval(D.withDate(1044, 12, 16), D.withDate(1046, 5, 22))));
    heian.add(new Era("eisho.1046", new Interval(D.withDate(1046, 5, 22), D.withDate(1053, 2, 2))));
    heian.add(new Era("tenki.1053", new Interval(D.withDate(1053, 2, 2), D.withDate(1058, 9, 19))));
    heian.add(new Era("kohei.1058", new Interval(D.withDate(1058, 9, 19), D.withDate(1065, 9, 4))));
    heian.add(new Era("jiryaku.1065", new Interval(D.withDate(1065, 9, 4), D.withDate(1069, 5, 6))));
    heian.add(new Era("enkyu.1069", new Interval(D.withDate(1069, 5, 6), D.withDate(1074, 9, 16))));
    heian.add(new Era("joho.1074", new Interval(D.withDate(1074, 9, 16), D.withDate(1077, 12, 5))));
    heian.add(new Era("joryaku.1077", new Interval(D.withDate(1077, 12, 5), D.withDate(1081, 3, 22))));
    heian.add(new Era("eiho.1081", new Interval(D.withDate(1081, 3, 22), D.withDate(1084, 3, 15))));
    heian.add(new Era("otoku.1084", new Interval(D.withDate(1084, 3, 15), D.withDate(1087, 5, 11))));
    heian.add(new Era("kanji.1087", new Interval(D.withDate(1087, 5, 11), D.withDate(1095, 1, 23))));
    heian.add(new Era("kaho.1095", new Interval(D.withDate(1095, 1, 23), D.withDate(1097, 1, 3))));
    heian.add(new Era("eicho.1097", new Interval(D.withDate(1097, 1, 3), D.withDate(1097, 12, 27))));
    heian.add(new Era("jotoku.1097", new Interval(D.withDate(1097, 12, 27), D.withDate(1099, 9, 15))));
    heian.add(new Era("kowa.1099", new Interval(D.withDate(1099, 9, 15), D.withDate(1104, 3, 8))));
    heian.add(new Era("choji.1104", new Interval(D.withDate(1104, 3, 8), D.withDate(1106, 5, 13))));
    heian.add(new Era("kasho.1106", new Interval(D.withDate(1106, 5, 13), D.withDate(1108, 9, 9))));
    heian.add(new Era("tennin.1108", new Interval(D.withDate(1108, 9, 9), D.withDate(1110, 7, 31))));
    heian.add(new Era("ten-ei.1110", new Interval(D.withDate(1110, 7, 31), D.withDate(1113, 8, 25))));
    heian.add(new Era("eikyu.1113", new Interval(D.withDate(1113, 8, 25), D.withDate(1118, 4, 25))));
    heian.add(new Era("gen-ei.1118", new Interval(D.withDate(1118, 4, 25), D.withDate(1120, 5, 9))));
    heian.add(new Era("hoan.1120", new Interval(D.withDate(1120, 5, 9), D.withDate(1124, 5, 18))));
    heian.add(new Era("tenji.1124", new Interval(D.withDate(1124, 5, 18), D.withDate(1126, 2, 15))));
    heian.add(new Era("daiji.1126", new Interval(D.withDate(1126, 2, 15), D.withDate(1131, 2, 28))));
    heian.add(new Era("tensho.1131", new Interval(D.withDate(1131, 2, 28), D.withDate(1132, 9, 21))));
    heian.add(new Era("chosho.1132", new Interval(D.withDate(1132, 9, 21), D.withDate(1135, 6, 10))));
    heian.add(new Era("hoen.1135", new Interval(D.withDate(1135, 6, 10), D.withDate(1141, 8, 13))));
    heian.add(new Era("eiji.1141", new Interval(D.withDate(1141, 8, 13), D.withDate(1142, 5, 25))));
    heian.add(new Era("koji.1142", new Interval(D.withDate(1142, 5, 25), D.withDate(1144, 3, 28))));
    heian.add(new Era("tenyo.1144", new Interval(D.withDate(1144, 3, 28), D.withDate(1145, 8, 12))));
    heian.add(new Era("kyuan.1145", new Interval(D.withDate(1145, 8, 12), D.withDate(1151, 2, 14))));
    heian.add(new Era("ninpei.1151", new Interval(D.withDate(1151, 2, 14), D.withDate(1154, 12, 4))));
    heian.add(new Era("kyuju.1154", new Interval(D.withDate(1154, 12, 4), D.withDate(1156, 5, 18))));
    heian.add(new Era("hogen.1156", new Interval(D.withDate(1156, 5, 18), D.withDate(1159, 5, 9))));
    heian.add(new Era("heiji.1159", new Interval(D.withDate(1159, 5, 9), D.withDate(1160, 2, 18))));
    heian.add(new Era("eiryaku.1160", new Interval(D.withDate(1160, 2, 18), D.withDate(1161, 9, 24))));
    heian.add(new Era("oho.1161", new Interval(D.withDate(1161, 9, 24), D.withDate(1163, 5, 4))));
    heian.add(new Era("chokan.1163", new Interval(D.withDate(1163, 5, 4), D.withDate(1165, 7, 14))));
    heian.add(new Era("eiman.1165", new Interval(D.withDate(1165, 7, 14), D.withDate(1166, 9, 23))));
    heian.add(new Era("ninnan.1166", new Interval(D.withDate(1166, 9, 23), D.withDate(1169, 5, 6))));
    heian.add(new Era("kao.1169", new Interval(D.withDate(1169, 5, 6), D.withDate(1171, 5, 27))));
    heian.add(new Era("shoan.1171", new Interval(D.withDate(1171, 5, 27), D.withDate(1175, 8, 16))));
    heian.add(new Era("angen.1175", new Interval(D.withDate(1175, 8, 16), D.withDate(1177, 8, 29))));
    heian.add(new Era("jisho.1177", new Interval(D.withDate(1177, 8, 29), D.withDate(1181, 8, 25))));
    heian.add(new Era("yowa.1181", new Interval(D.withDate(1181, 8, 25), D.withDate(1182, 6, 29))));
    heian.add(new Era("juei.1182", new Interval(D.withDate(1182, 6, 29), D.withDate(1184, 5, 27))));
    heian.add(new Era("genryaku.1184", new Interval(D.withDate(1184, 5, 27), D.withDate(1185, 9, 9))));
    era.put("heian", heian);
    periods.add(new Era("heian", new Interval(heian.get(0).interval.getStart(), heian.get(heian.size() - 1).interval.getEnd())));

    List<Era> kamakura = new ArrayList<Era>();
    kamakura.add(new Era("bunji.1185", new Interval(D.withDate(1185, 9, 9), D.withDate(1190, 5, 16))));
    kamakura.add(new Era("kenkyu.1190", new Interval(D.withDate(1190, 5, 16), D.withDate(1199, 5, 23))));
    kamakura.add(new Era("shoji.1199", new Interval(D.withDate(1199, 5, 23), D.withDate(1201, 3, 19))));
    kamakura.add(new Era("kennin.1201", new Interval(D.withDate(1201, 3, 19), D.withDate(1204, 3, 23))));
    kamakura.add(new Era("genkyu.1204", new Interval(D.withDate(1204, 3, 23), D.withDate(1206, 6, 5))));
    kamakura.add(new Era("kenei.1206", new Interval(D.withDate(1206, 6, 5), D.withDate(1207, 11, 16))));
    kamakura.add(new Era("jogen.1207", new Interval(D.withDate(1207, 11, 16), D.withDate(1211, 4, 23))));
    kamakura.add(new Era("kenryaku.1211", new Interval(D.withDate(1211, 4, 23), D.withDate(1214, 1, 18))));
    kamakura.add(new Era("kenpo.1214", new Interval(D.withDate(1214, 1, 18), D.withDate(1219, 5, 27))));
    kamakura.add(new Era("jokyu.1219", new Interval(D.withDate(1219, 5, 27), D.withDate(1222, 5, 25))));
    kamakura.add(new Era("joo.1222", new Interval(D.withDate(1222, 5, 25), D.withDate(1224, 12, 31))));
    kamakura.add(new Era("gennin.1224", new Interval(D.withDate(1224, 12, 31), D.withDate(1225, 5, 28))));
    kamakura.add(new Era("karoku.1225", new Interval(D.withDate(1225, 5, 28), D.withDate(1228, 1, 18))));
    kamakura.add(new Era("antei.1228", new Interval(D.withDate(1228, 1, 18), D.withDate(1229, 3, 31))));
    kamakura.add(new Era("kanki.1229", new Interval(D.withDate(1229, 3, 31), D.withDate(1232, 4, 23))));
    kamakura.add(new Era("joei.1232", new Interval(D.withDate(1232, 4, 23), D.withDate(1233, 5, 25))));
    kamakura.add(new Era("tenpuku.1233", new Interval(D.withDate(1233, 5, 25), D.withDate(1234, 11, 27))));
    kamakura.add(new Era("bunryaku.1234", new Interval(D.withDate(1234, 11, 27), D.withDate(1235, 11, 1))));
    kamakura.add(new Era("katei.1235", new Interval(D.withDate(1235, 11, 1), D.withDate(1238, 12, 30))));
    kamakura.add(new Era("ryakunin.1238", new Interval(D.withDate(1238, 12, 30), D.withDate(1239, 3, 13))));
    kamakura.add(new Era("eno.1239", new Interval(D.withDate(1239, 3, 13), D.withDate(1240, 8, 5))));
    kamakura.add(new Era("ninji.1240", new Interval(D.withDate(1240, 8, 5), D.withDate(1243, 3, 18))));
    kamakura.add(new Era("kangen.1243", new Interval(D.withDate(1243, 3, 18), D.withDate(1247, 4, 5))));
    kamakura.add(new Era("hoji.1247", new Interval(D.withDate(1247, 4, 5), D.withDate(1249, 5, 2))));
    kamakura.add(new Era("kencho.1249", new Interval(D.withDate(1249, 5, 2), D.withDate(1256, 10, 24))));
    kamakura.add(new Era("kogen.1256", new Interval(D.withDate(1256, 10, 24), D.withDate(1257, 3, 31))));
    kamakura.add(new Era("shoka.1257", new Interval(D.withDate(1257, 3, 31), D.withDate(1259, 4, 20))));
    kamakura.add(new Era("shogen.1259", new Interval(D.withDate(1259, 4, 20), D.withDate(1260, 5, 24))));
    kamakura.add(new Era("buno.1260", new Interval(D.withDate(1260, 5, 24), D.withDate(1261, 3, 22))));
    kamakura.add(new Era("kocho.1261", new Interval(D.withDate(1261, 3, 22), D.withDate(1264, 3, 27))));
    kamakura.add(new Era("bunei.1264", new Interval(D.withDate(1264, 3, 27), D.withDate(1275, 5, 22))));
    kamakura.add(new Era("kenji.1275", new Interval(D.withDate(1275, 5, 22), D.withDate(1278, 3, 23))));
    kamakura.add(new Era("koan.1278", new Interval(D.withDate(1278, 3, 23), D.withDate(1288, 5, 29))));
    kamakura.add(new Era("shoo.1288", new Interval(D.withDate(1288, 5, 29), D.withDate(1293, 9, 6))));
    kamakura.add(new Era("einin.1293", new Interval(D.withDate(1293, 9, 6), D.withDate(1299, 5, 25))));
    kamakura.add(new Era("shoan.1299", new Interval(D.withDate(1299, 5, 25), D.withDate(1302, 12, 10))));
    kamakura.add(new Era("kengen.1302", new Interval(D.withDate(1302, 12, 10), D.withDate(1303, 9, 16))));
    kamakura.add(new Era("kagen.1303", new Interval(D.withDate(1303, 9, 16), D.withDate(1307, 1, 18))));
    kamakura.add(new Era("tokuji.1307", new Interval(D.withDate(1307, 1, 18), D.withDate(1308, 11, 22))));
    kamakura.add(new Era("enkyo.1308", new Interval(D.withDate(1308, 11, 22), D.withDate(1311, 5, 17))));
    kamakura.add(new Era("ocho.1311", new Interval(D.withDate(1311, 5, 17), D.withDate(1312, 4, 27))));
    kamakura.add(new Era("showa.1312", new Interval(D.withDate(1312, 4, 27), D.withDate(1317, 3, 16))));
    kamakura.add(new Era("bunpo.1317", new Interval(D.withDate(1317, 3, 16), D.withDate(1319, 5, 18))));
    kamakura.add(new Era("geno.1319", new Interval(D.withDate(1319, 5, 18), D.withDate(1321, 3, 22))));
    kamakura.add(new Era("genko.1321", new Interval(D.withDate(1321, 3, 22), D.withDate(1324, 12, 25))));
    kamakura.add(new Era("shochu.1324", new Interval(D.withDate(1324, 12, 25), D.withDate(1326, 5, 28))));
    kamakura.add(new Era("karyaku.1326", new Interval(D.withDate(1326, 5, 28), D.withDate(1329, 9, 22))));
    era.put("kamakura", kamakura);
    periods.add(new Era("kamakura", new Interval(kamakura.get(0).interval.getStart(), kamakura.get(kamakura.size() - 1).interval.getEnd())));

    List<Era> daikakujito = new ArrayList<Era>();
    daikakujito.add(new Era("gentoku.1329", new Interval(D.withDate(1329, 9, 22), D.withDate(1331, 9, 11))));
    daikakujito.add(new Era("genko.1331", new Interval(D.withDate(1331, 9, 11), D.withDate(1334, 3, 5))));
    daikakujito.add(new Era("kenmu.1334", new Interval(D.withDate(1334, 3, 5), D.withDate(1336, 4, 11))));
    daikakujito.add(new Era("engen.1336", new Interval(D.withDate(1336, 4, 11), D.withDate(1340, 5, 25))));
    daikakujito.add(new Era("kokoku.1340", new Interval(D.withDate(1340, 5, 25), D.withDate(1347, 1, 20))));
    daikakujito.add(new Era("shohei.1347", new Interval(D.withDate(1347, 1, 20), D.withDate(1370, 8, 16))));
    daikakujito.add(new Era("kentoku.1370", new Interval(D.withDate(1370, 8, 16), D.withDate(1372, 5, 4))));
    daikakujito.add(new Era("bunchu.1372", new Interval(D.withDate(1372, 5, 4), D.withDate(1375, 6, 26))));
    daikakujito.add(new Era("tenju.1375", new Interval(D.withDate(1375, 6, 26), D.withDate(1381, 3, 6))));
    daikakujito.add(new Era("kowa.1381", new Interval(D.withDate(1381, 3, 6), D.withDate(1384, 5, 18))));
    daikakujito.add(new Era("genchu.1384", new Interval(D.withDate(1384, 5, 18), D.withDate(1392, 11, 19))));
    daikakujito.add(new Era("meitoku.1392", new Interval(D.withDate(1392, 11, 19), D.withDate(1394, 8, 2))));
    era.put("nanboku-cho.daikakujito", daikakujito);
    periods.add(new Era("nanboku-cho", new Interval(daikakujito.get(0).interval.getStart(), daikakujito.get(daikakujito.size() - 1).interval.getEnd())));

    List<Era> jimyointo = new ArrayList<Era>();
    jimyointo.add(new Era("gentoku.1329", new Interval(D.withDate(1329, 9, 22), D.withDate(1332, 5, 23))));
    jimyointo.add(new Era("shokei.1332", new Interval(D.withDate(1332, 5, 23), D.withDate(1333, 7, 7))));
    jimyointo.add(new Era("unknown.1334", new Interval(D.withDate(1333, 7, 7), D.withDate(1334, 3, 5))));
    jimyointo.add(new Era("kenmu.1334", new Interval(D.withDate(1334, 3, 5), D.withDate(1338, 10, 11))));
    jimyointo.add(new Era("ryakuo.1338", new Interval(D.withDate(1338, 10, 11), D.withDate(1342, 6, 1))));
    jimyointo.add(new Era("koei.1342", new Interval(D.withDate(1342, 6, 1), D.withDate(1345, 11, 15))));
    jimyointo.add(new Era("jowa.1345", new Interval(D.withDate(1345, 11, 15), D.withDate(1350, 4, 4))));
    jimyointo.add(new Era("kano.1350", new Interval(D.withDate(1350, 4, 4), D.withDate(1352, 11, 4))));
    jimyointo.add(new Era("bunna.1352", new Interval(D.withDate(1352, 11, 4), D.withDate(1356, 4, 29))));
    jimyointo.add(new Era("enbun.1356", new Interval(D.withDate(1356, 4, 29), D.withDate(1361, 5, 4))));
    jimyointo.add(new Era("koan.1361", new Interval(D.withDate(1361, 5, 4), D.withDate(1362, 10, 11))));
    jimyointo.add(new Era("joji.1362", new Interval(D.withDate(1362, 10, 11), D.withDate(1368, 3, 7))));
    jimyointo.add(new Era("oan.1368", new Interval(D.withDate(1368, 3, 7), D.withDate(1375, 3, 29))));
    jimyointo.add(new Era("eiwa.1375", new Interval(D.withDate(1375, 3, 29), D.withDate(1379, 4, 9))));
    jimyointo.add(new Era("koryaku.1379", new Interval(D.withDate(1379, 4, 9), D.withDate(1381, 3, 20))));
    jimyointo.add(new Era("eitoku.1381", new Interval(D.withDate(1381, 3, 20), D.withDate(1384, 3, 19))));
    jimyointo.add(new Era("shitoku.1384", new Interval(D.withDate(1384, 3, 19), D.withDate(1387, 10, 5))));
    jimyointo.add(new Era("kakyo.1387", new Interval(D.withDate(1387, 10, 5), D.withDate(1389, 3, 7))));
    jimyointo.add(new Era("koo.1389", new Interval(D.withDate(1389, 3, 7), D.withDate(1390, 4, 12))));
    jimyointo.add(new Era("meitoku.1390", new Interval(D.withDate(1390, 4, 12), D.withDate(1394, 8, 2))));
    era.put("nanboku-cho.jimyointo", jimyointo);

    List<Era> muromachi = new ArrayList<Era>();
    muromachi.add(new Era("oei.1394", new Interval(D.withDate(1394, 8, 2), D.withDate(1428, 6, 10))));
    muromachi.add(new Era("shocho.1428", new Interval(D.withDate(1428, 6, 10), D.withDate(1429, 10, 3))));
    muromachi.add(new Era("eikyo.1429", new Interval(D.withDate(1429, 10, 3), D.withDate(1441, 3, 10))));
    muromachi.add(new Era("kakitsu.1441", new Interval(D.withDate(1441, 3, 10), D.withDate(1444, 2, 23))));
    muromachi.add(new Era("bunnan.1444", new Interval(D.withDate(1444, 2, 23), D.withDate(1449, 8, 16))));
    muromachi.add(new Era("hotoku.1449", new Interval(D.withDate(1449, 8, 16), D.withDate(1452, 8, 10))));
    muromachi.add(new Era("kyotoku.1452", new Interval(D.withDate(1452, 8, 10), D.withDate(1455, 9, 6))));
    muromachi.add(new Era("kosho.1455", new Interval(D.withDate(1455, 9, 6), D.withDate(1457, 10, 16))));
    muromachi.add(new Era("choroku.1457", new Interval(D.withDate(1457, 10, 16), D.withDate(1461, 2, 1))));
    muromachi.add(new Era("kansho.1461", new Interval(D.withDate(1461, 2, 1), D.withDate(1466, 3, 14))));
    muromachi.add(new Era("bunsho.1466", new Interval(D.withDate(1466, 3, 14), D.withDate(1467, 4, 9))));
    era.put("muromachi", muromachi);
    periods.add(new Era("muromachi", new Interval(muromachi.get(0).interval.getStart(), muromachi.get(muromachi.size() - 1).interval.getEnd())));

    List<Era> sengoku = new ArrayList<Era>();
    sengoku.add(new Era("onin.1467", new Interval(D.withDate(1467, 4, 9), D.withDate(1469, 6, 8))));
    sengoku.add(new Era("bunmei.1469", new Interval(D.withDate(1469, 6, 8), D.withDate(1487, 8, 9))));
    sengoku.add(new Era("chokyo.1487", new Interval(D.withDate(1487, 8, 9), D.withDate(1489, 9, 16))));
    sengoku.add(new Era("entoku.1489", new Interval(D.withDate(1489, 9, 16), D.withDate(1492, 8, 12))));
    sengoku.add(new Era("meio.1492", new Interval(D.withDate(1492, 8, 12), D.withDate(1501, 3, 18))));
    sengoku.add(new Era("bunki.1501", new Interval(D.withDate(1501, 3, 18), D.withDate(1504, 3, 16))));
    sengoku.add(new Era("eisho.1504", new Interval(D.withDate(1504, 3, 16), D.withDate(1521, 9, 23))));
    sengoku.add(new Era("daiei.1521", new Interval(D.withDate(1521, 9, 23), D.withDate(1528, 9, 3))));
    sengoku.add(new Era("kyoroku.1528", new Interval(D.withDate(1528, 9, 3), D.withDate(1532, 8, 29))));
    sengoku.add(new Era("tenbun.1532", new Interval(D.withDate(1532, 8, 29), D.withDate(1555, 11, 7))));
    sengoku.add(new Era("koji.1555", new Interval(D.withDate(1555, 11, 7), D.withDate(1558, 3, 18))));
    sengoku.add(new Era("eiroku.1558", new Interval(D.withDate(1558, 3, 18), D.withDate(1570, 5, 27))));
    sengoku.add(new Era("genki.1570", new Interval(D.withDate(1570, 5, 27), D.withDate(1573, 8, 25))));
    era.put("sengoku", sengoku);
    periods.add(new Era("sengoku", new Interval(sengoku.get(0).interval.getStart(), sengoku.get(sengoku.size() - 1).interval.getEnd())));

    List<Era> azuchiMomoyama = new ArrayList<Era>();
    azuchiMomoyama.add(new Era("tensho.1573", new Interval(D.withDate(1573, 8, 25), D.withDate(1593, 1, 10))));
    azuchiMomoyama.add(new Era("bunroku.1593", new Interval(D.withDate(1593, 1, 10), D.withDate(1596, 12, 16))));
    azuchiMomoyama.add(new Era("keicho.1596", new Interval(D.withDate(1596, 12, 16), D.withDate(1615, 9, 5))));
    era.put("azuchi-momoyama", azuchiMomoyama);
    periods.add(new Era("azuchi-momoyama", new Interval(azuchiMomoyama.get(0).interval.getStart(), azuchiMomoyama.get(azuchiMomoyama.size() - 1).interval.getEnd())));

    List<Era> edo = new ArrayList<Era>();
    edo.add(new Era("genna.1615", new Interval(D.withDate(1615, 9, 5), D.withDate(1624, 4, 17))));
    edo.add(new Era("kanei.1624", new Interval(D.withDate(1624, 4, 17), D.withDate(1645, 1, 13))));
    edo.add(new Era("shoho.1645", new Interval(D.withDate(1645, 1, 13), D.withDate(1648, 4, 7))));
    edo.add(new Era("keian.1648", new Interval(D.withDate(1648, 4, 7), D.withDate(1652, 10, 20))));
    edo.add(new Era("joo.1652", new Interval(D.withDate(1652, 10, 20), D.withDate(1655, 5, 18))));
    edo.add(new Era("meireki.1655", new Interval(D.withDate(1655, 5, 18), D.withDate(1658, 8, 21))));
    edo.add(new Era("manji.1658", new Interval(D.withDate(1658, 8, 21), D.withDate(1661, 5, 23))));
    edo.add(new Era("kanbun.1661", new Interval(D.withDate(1661, 5, 23), D.withDate(1673, 10, 30))));
    edo.add(new Era("enpo.1673", new Interval(D.withDate(1673, 10, 30), D.withDate(1681, 11, 9))));
    edo.add(new Era("tenna.1681", new Interval(D.withDate(1681, 11, 9), D.withDate(1684, 4, 5))));
    edo.add(new Era("jokyo.1684", new Interval(D.withDate(1684, 4, 5), D.withDate(1688, 10, 23))));
    edo.add(new Era("genroku.1688", new Interval(D.withDate(1688, 10, 23), D.withDate(1704, 4, 16))));
    edo.add(new Era("hoei.1704", new Interval(D.withDate(1704, 4, 16), D.withDate(1711, 6, 11))));
    edo.add(new Era("shotoku.1711", new Interval(D.withDate(1711, 6, 11), D.withDate(1716, 8, 9))));
    edo.add(new Era("kyoho.1716", new Interval(D.withDate(1716, 8, 9), D.withDate(1736, 6, 7))));
    edo.add(new Era("genbun.1736", new Interval(D.withDate(1736, 6, 7), D.withDate(1741, 4, 12))));
    edo.add(new Era("kanpo.1741", new Interval(D.withDate(1741, 4, 12), D.withDate(1744, 4, 3))));
    edo.add(new Era("enkyo.1744", new Interval(D.withDate(1744, 4, 3), D.withDate(1748, 8, 5))));
    edo.add(new Era("kanen.1748", new Interval(D.withDate(1748, 8, 5), D.withDate(1751, 12, 14))));
    edo.add(new Era("horeki.1751", new Interval(D.withDate(1751, 12, 14), D.withDate(1764, 6, 30))));
    edo.add(new Era("meiwa.1764", new Interval(D.withDate(1764, 6, 30), D.withDate(1772, 12, 10))));
    edo.add(new Era("anei.1772", new Interval(D.withDate(1772, 12, 10), D.withDate(1781, 4, 25))));
    edo.add(new Era("tenmei.1781", new Interval(D.withDate(1781, 4, 25), D.withDate(1789, 2, 19))));
    edo.add(new Era("kansei.1789", new Interval(D.withDate(1789, 2, 19), D.withDate(1801, 3, 19))));
    edo.add(new Era("kyowa.1801", new Interval(D.withDate(1801, 3, 19), D.withDate(1804, 3, 22))));
    edo.add(new Era("bunka.1804", new Interval(D.withDate(1804, 3, 22), D.withDate(1818, 5, 26))));
    edo.add(new Era("bunsei.1818", new Interval(D.withDate(1818, 5, 26), D.withDate(1831, 1, 23))));
    edo.add(new Era("tenpo.1831", new Interval(D.withDate(1831, 1, 23), D.withDate(1845, 1, 9))));
    edo.add(new Era("koka.1845", new Interval(D.withDate(1845, 1, 9), D.withDate(1848, 4, 1))));
    edo.add(new Era("kaei.1848", new Interval(D.withDate(1848, 4, 1), D.withDate(1855, 1, 15))));
    edo.add(new Era("ansei.1855", new Interval(D.withDate(1855, 1, 15), D.withDate(1860, 4, 8))));
    edo.add(new Era("manen.1860", new Interval(D.withDate(1860, 4, 8), D.withDate(1861, 3, 29))));
    edo.add(new Era("bunkyu.1861", new Interval(D.withDate(1861, 3, 29), D.withDate(1864, 3, 27))));
    edo.add(new Era("genji.1864", new Interval(D.withDate(1864, 3, 27), D.withDate(1865, 5, 1))));
    edo.add(new Era("keio.1865", new Interval(D.withDate(1865, 5, 1), D.withDate(1868, 10, 23))));
    era.put("edo", edo);
    periods.add(new Era("edo", new Interval(edo.get(0).interval.getStart(), edo.get(edo.size() - 1).interval.getEnd())));

    List<Era> meiji = new ArrayList<Era>();
    meiji.add(new Era("meiji.1868", new Interval(D.withDate(1868, 1, 25), D.withDate(1912, 7, 29))));
    era.put("meiji", meiji);
    periods.add(new Era("meiji", new Interval(meiji.get(0).interval.getStart(), meiji.get(meiji.size() - 1).interval.getEnd())));

    List<Era> taisho = new ArrayList<Era>();
    taisho.add(new Era("taisho.1912", new Interval(D.withDate(1912, 7, 30), D.withDate(1926, 12, 24))));
    era.put("taisho", taisho);
    periods.add(new Era("taisho", new Interval(taisho.get(0).interval.getStart(), taisho.get(taisho.size() - 1).interval.getEnd())));

    List<Era> showa = new ArrayList<Era>();
    showa.add(new Era("showa.1926", new Interval(D.withDate(1926, 12, 25), D.withDate(1989, 1, 7))));
    era.put("showa", showa);
    periods.add(new Era("showa", new Interval(showa.get(0).interval.getStart(), showa.get(showa.size() - 1).interval.getEnd())));

    List<Era> heisei = new ArrayList<Era>();
    heisei.add(new Era("heisei.1989", new Interval(D.withDate(1989, 1, 8), D.plusDays(1))));
    era.put("heisei", heisei);
    periods.add(new Era("heisei", new Interval(heisei.get(0).interval.getStart(), heisei.get(heisei.size() - 1).interval.getEnd())));

    PERIODS = ImmutableList.copyOf(periods);
    ERAS = ImmutableMap.copyOf(era);

  }

  private static Era getPeriod(DateTime then) {
    DateTime d = DateTime.now(GJChronology.getInstanceUTC()).withZone(DateTimeZone.forID("Asia/Tokyo")).withTimeAtStartOfDay();
    if (then != null) d = d.withMillis(then.getMillis());
    for (Era period : PERIODS) {
      if (period.contains(then)) return period;
    }

    return null;
  }

  public static Era getEra(DateTime then) {
    return getEra(then, getPeriod(then), false);
  }

  private static Era getEra(DateTime then, Era period, boolean isJimyoin) {
    if (period == null) return null;
    String key = period.key;
    if (key.startsWith("nanboku-cho")) key += isJimyoin ? ".jimyointo" : ".daikakujito";
    if (!ERAS.containsKey(key)) return null;
    DateTime d = DateTime.now(GJChronology.getInstanceUTC()).withZone(DateTimeZone.forID("Asia/Tokyo")).withTimeAtStartOfDay();
    if (then != null) d = d.withMillis(then.getMillis());
    for (Era era : ERAS.get(period.key)) {
      if (era.contains(then)) return era;
    }

    return null;
  }

  public static String getEraAsText(DateTime then) {
    return getEraAsText(then, Locale.JAPAN, false);
  }

  public static String getEraAsText(DateTime then, Locale locale) {
    return getEraAsText(then, locale, false);
  }

  public static String getEraAsText(DateTime then, Locale locale, boolean isJimyoin) {
    if (then == null) return "";
    Era era = getEra(then, getPeriod(then), isJimyoin);
    if (era == null) return "";
    String text = getResourceString(era.key, locale);

    return StringUtils.isSimilarToBlank(text) ? StringUtils.truncateAll(era.key, "\\.\\d") : text;
  }

  public static String getYearOfEra(DateTime then, Locale locale, boolean isJimyoin) {
    if (then == null) return "";
    Era era = getEra(then, getPeriod(then), isJimyoin);
    if (era == null) return "";

    return Integer.toString(new Period(era.start, then, PeriodType.years()).getYears() + 1);
  }

  private static String getResourceString(String key, Locale locale) {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, locale == null ? Locale.ROOT : locale);

      return bundle.getString(StringUtils.defaultString(key));
    } catch (MissingResourceException e) {}

    return "";
  }

  static class Era {

    final String key;
    final Interval interval;
    final DateTime start;
    final DateTime end;

    Locale locale = Locale.ROOT;
    ResourceBundle bundle;

    Era(String key, Interval interval) {
      this(key,interval, Locale.getDefault());
    }

    Era(String key, DateTime start, DateTime end) {
      this(key,start, end, Locale.getDefault());
    }

    Era(String key, DateTime start, DateTime end, Locale locale) {
      if (StringUtils.isBlank(key)) throw new IllegalArgumentException("key must not be null.");
      if (start == null) throw new IllegalArgumentException("start must not be null.");
      if (end == null) throw new IllegalArgumentException("end must not be null.");
      this.key = key;
      this.start = start;
      this.end = end;
      this.interval = new Interval(start, end);
      if (locale != null) this.locale = locale;
      try {
        bundle = ResourceBundle.getBundle(RESOURCE, this.locale);
      } catch (MissingResourceException e) {}
    }

    Era(String key, Interval interval, Locale locale) {
      if (StringUtils.isBlank(key)) throw new IllegalArgumentException("key must not be null.");
      if (interval == null) throw new IllegalArgumentException("interval must not be null.");
      this.key = key;
      this.interval = interval;
      start = this.interval.getStart();
      end = this.interval.getEnd();
      if (locale != null) this.locale = locale;
      try {
        bundle = ResourceBundle.getBundle(RESOURCE, this.locale);
      } catch (MissingResourceException e) {}
    }

    Era withLocale(Locale locale) {
      return new Era(key, interval, locale);
    }

    String getName() {
      try {
        ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, locale);
        if (bundle.keySet().contains(StringUtils.defaultString(key))) return bundle.getString(StringUtils.defaultString(key));
      } catch (MissingResourceException e) {}

      return "";
    }

    String getYear(DateTime then) {
      if (!contains(then)) return "";
      long diff = then.getMillis() - interval.getStartMillis();

      return Integer.toString(new Period(interval.getStartMillis(), diff).getYears() + 1);
    }

    boolean contains(DateTime then) {
      return interval.contains(then);
    }
  }
}
